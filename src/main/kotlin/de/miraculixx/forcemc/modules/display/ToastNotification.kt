package de.miraculixx.forcemc.modules.display

import de.miraculixx.forcemc.Manager
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.taskRunLater
import net.minecraft.advancements.FrameType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import java.util.*

/**
 * Create a Toast/Advancement display. If the title is too long to display, it will show full boxed fading into the description
 * @param title Title of Toast (See able message)
 * @param description Description of Toast (Normally not visible)
 * @param icon Displayed Item (NOT EVERY MATERIAL IS AN ITEM)
 * @param frame Frame type of Toast. Note, Challenge type plays a sound to the player
 */
class ToastNotification(title: String, icon: Material, description: String, frame: FrameType) {
    private val key: NamespacedKey = NamespacedKey(Manager, UUID.randomUUID().toString())

    fun send(player: Player) {
        show(listOf(player))
    }

    fun broadcast() {
        show(onlinePlayers)
    }

    private fun show(players: Collection<Player>) {
        add()
        grant(players)
        taskRunLater(20) {
            revoke(players)
            remove()
        }
    }

    @Suppress("DEPRECATION")
    private fun add() {
        Bukkit.getUnsafe().loadAdvancement(key, json)
    }

    @Suppress("DEPRECATION")
    private fun remove() {
        Bukkit.getUnsafe().removeAdvancement(key)
    }

    private fun grant(players: Collection<Player>) {
        val advancement = Bukkit.getAdvancement(key) ?: return
        players.forEach {
            val progress = it.getAdvancementProgress(advancement)
            if (!progress.isDone) progress.remainingCriteria.forEach { criteria ->
                progress.awardCriteria(criteria)
            }
        }
    }

    private fun revoke(players: Collection<Player>) {
        val advancement = Bukkit.getAdvancement(key) ?: return
        players.forEach {
            val progress = it.getAdvancementProgress(advancement)
            if (progress.isDone) {
                if (!progress.isDone) progress.remainingCriteria.forEach { criteria ->
                    progress.revokeCriteria(criteria)
                }
            }
        }
    }

    private val json = Json.encodeToString(
        ToastFormat(
            ToastCriteria(ToastTrigger("minecraft:impossible")),
            ToastDisplay(
                ToastItemKey(
                    "minecraft:${icon.name.lowercase()}"
                ),
                ToastText(title, "green"),
                ToastText(description, "light_purple"),
                frame.getName(),
                hidden = true,
                announce_to_chat = false,
                "minecraft:textures/block/tube_coral_block.png"
            )
        )
    )


    /*
    Serializer for JSON formation
     */
    @Serializable
    data class ToastFormat(val criteria: ToastCriteria, val display: ToastDisplay)

    @Serializable
    data class ToastCriteria(val impossible: ToastTrigger)

    @Serializable
    data class ToastTrigger(val trigger: String)

    @Serializable
    data class ToastDisplay(
        val icon: ToastItemKey,
        val title: ToastText,
        val description: ToastText,
        val frame: String,
        val hidden: Boolean,
        val announce_to_chat: Boolean,
        val background: String
    )

    @Serializable
    data class ToastText(val text: String, val color: String)

    @Serializable
    data class ToastItemKey(val item: String)
}