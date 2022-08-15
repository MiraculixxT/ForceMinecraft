package de.miraculixx.forcemc.modules

import de.miraculixx.forcemc.modules.data.JsonFormat
import de.miraculixx.forcemc.modules.data.ProgressSave
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.axay.kspigot.extensions.console
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.advancement.Advancement
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import java.io.File
import java.util.*
import kotlin.io.path.Path

object ForceManager {
    private val items = ProgressSave<Material>()
    private val mobs = ProgressSave<EntityType>()
    private val advancements = ProgressSave<Advancement>()
    private val sounds = ProgressSave<Sound>()


    private fun fill() {
        items.remaining.addAll(
            Material.values().filter {
                (it.creativeCategory != null && it != Material.DRAGON_HEAD) ||
                        it != Material.PLAYER_HEAD
            }
        )
        mobs.remaining.addAll(
            kotlin.collections.ArrayList<EntityType>(Arrays.stream(EntityType.values()).filter { type ->
                type.entityClass != null && LivingEntity::class.java.isAssignableFrom(type.entityClass) && type != EntityType.PLAYER
            }.toList())
        )
        Bukkit.advancementIterator().forEach { adv ->
            if (!adv.key.key.startsWith("recipes")) advancements.remaining.add(adv)
        }
        sounds.remaining.addAll(Sound.values().filter {
            it != Sound.ENCHANT_THORNS_HIT &&
                    it != Sound.EVENT_RAID_HORN &&
                    it != Sound.PARTICLE_SOUL_ESCAPE &&
                    it != Sound.UI_BUTTON_CLICK &&
                    it != Sound.UI_TOAST_IN &&
                    it != Sound.UI_TOAST_OUT &&
                    it != Sound.UI_TOAST_CHALLENGE_COMPLETE &&
                    !it.key.key.startsWith("music.")
        })
    }

    private fun filter() {
        val file = File(Path("MUtils/history.json").toAbsolutePath().toString())
        if (!file.exists()) {
            file.createNewFile()
            file.writeText(Json.encodeToString(JsonFormat(emptyList(), emptyList(), emptyList(), emptyList())))
            console.sendMessage("No history data found! `history.json` created for further use")
        } else {
            val input = file.readText()
            console.sendMessage("History data found! Loading ${file.length() / 1000.0}kb...")
            val data = Json.decodeFromString<JsonFormat>(input)
            items.finished.addAll(data.items)
            items.remaining.removeAll(data.items)
            mobs.finished.addAll(data.mobs)
            mobs.remaining.removeAll(data.mobs)
            advancements.finished.addAll(data.advancements)
            advancements.remaining.removeAll(data.advancements)
            sounds.finished.addAll(data.sounds)
            sounds.remaining.removeAll(data.sounds)
            console.sendMessage("§aSuccessfully load history!")
        }
    }

    init {
        fill()
        filter()
    }
}