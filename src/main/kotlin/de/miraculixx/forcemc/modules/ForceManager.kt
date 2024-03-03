package de.miraculixx.forcemc.modules

import de.miraculixx.forcemc.Manager
import de.miraculixx.forcemc.modules.data.*
import de.miraculixx.forcemc.modules.display.BossBar
import de.miraculixx.forcemc.modules.display.ToastNotification
import de.miraculixx.forcemc.modules.display.fancy
import de.miraculixx.forcemc.modules.display.toItem
import de.miraculixx.forcemc.modules.events.GrantAdvancement
import de.miraculixx.forcemc.modules.events.HearingSound
import de.miraculixx.forcemc.modules.events.ItemGathering
import de.miraculixx.forcemc.modules.events.MobKill
import de.miraculixx.forcemc.utils.cError
import de.miraculixx.forcemc.utils.cmp
import de.miraculixx.forcemc.utils.plus
import de.miraculixx.forcemc.utils.prefix
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.axay.kspigot.extensions.broadcast
import net.axay.kspigot.extensions.console
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.taskRunLater
import net.minecraft.advancements.FrameType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.advancement.Advancement
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import java.io.File
import java.util.*
import kotlin.io.path.Path

object ForceManager {
    val items = ProgressSave<Material>()
    val mobs = ProgressSave<EntityType>()
    val advancements = ProgressSave<Advancement>()
    val sounds = ProgressSave<Sound>()

    var bossBar: BossBar? = null

    var currentGoal = "error"
    var currentEvent: Event? = null
    var currentType = SearchType.NOTHING

    fun next() {
        currentEvent?.unregister()
        when (currentType) {
            SearchType.ITEM -> items.addFinished(Material.valueOf(currentGoal))
            SearchType.MOB -> mobs.addFinished(EntityType.valueOf(currentGoal))
            SearchType.ADVANCEMENT -> advancements.addFinished(Bukkit.getAdvancement(NamespacedKey("minecraft", currentGoal)) ?: return)
            SearchType.SOUND -> sounds.addFinished(Sound.valueOf(currentGoal))
            SearchType.NOTHING -> {}
        }
        val finishedTypes = buildList {
            if (items.remaining.isEmpty()) add(SearchType.ITEM)
            if (mobs.remaining.isEmpty()) add(SearchType.MOB)
            if (advancements.remaining.isEmpty()) add(SearchType.ADVANCEMENT)
            if (sounds.remaining.isEmpty()) add(SearchType.SOUND)
        }
        currentType = SearchType.values().filter { !finishedTypes.contains(it) && it != SearchType.NOTHING }.random()
        currentGoal = when (currentType) {
            SearchType.ITEM -> {
                currentEvent = ItemGathering()
                items.remaining.random().name
            }

            SearchType.MOB -> {
                currentEvent = MobKill()
                mobs.remaining.random().name
            }

            SearchType.ADVANCEMENT -> {
                currentEvent = GrantAdvancement()
                advancements.remaining.random().key.key
            }

            SearchType.SOUND -> {
                currentEvent = HearingSound()
                sounds.remaining.random().name
            }

            SearchType.NOTHING -> "error"
        }
        bossBar?.update(true)
        taskRunLater(90) { checkPresent() }
    }

    private fun checkPresent() {
        when (currentType) {
            SearchType.ITEM -> {
                val material = toItem(currentGoal, SearchType.ITEM)
                onlinePlayers.forEach { player ->
                    if (player.inventory.contains(material)) {
                        ToastNotification("Item In Inventory: ${currentGoal.fancy()}", toItem(currentGoal, SearchType.ITEM), "Force Minecraft by Miraculixx", FrameType.GOAL).broadcast()
                        next()
                        return
                    }
                }
            }

            SearchType.ADVANCEMENT -> {
                val key = NamespacedKey.fromString(currentGoal)!!
                val adv = Bukkit.getAdvancement(key)
                if (adv == null) {
                    broadcast(prefix + cmp("Something went wrong... (Error Code: 3)", cError))
                    broadcast(prefix + cmp("Use /skip if the current task is already finished or impossible", cError))
                    console.sendMessage(prefix + cmp("Key: $currentGoal (${key.asString()})"))
                    return
                }
                onlinePlayers.forEach { player ->
                    if (player.getAdvancementProgress(adv).isDone) {
                        ToastNotification(
                            "Advancement Already Done: ${currentGoal.replace("/", "_-_").fancy()}",
                            toItem(currentGoal, SearchType.ADVANCEMENT),
                            "Force Minecraft by Miraculixx",
                            FrameType.GOAL
                        ).broadcast()
                        next()
                        return
                    }
                }
            }

            else -> {}
        }
    }

    fun start(): Boolean {
        if (currentGoal == "error") next()
        else if (currentEvent == null) currentEvent = when (currentType) {
            SearchType.ITEM -> ItemGathering()
            SearchType.MOB -> MobKill()
            SearchType.ADVANCEMENT -> GrantAdvancement()
            SearchType.SOUND -> HearingSound()
            SearchType.NOTHING -> return false
        } else return false
        bossBar = BossBar()
        return true
    }

    fun shutdown() {
        bossBar?.shutdown()
        val file = File(Manager.dataFolder.path + "//history.json")
        val sb = Json.encodeToString(JsonFormat(items.finished, mobs.finished, advancements.finished, sounds.finished))
        file.writeText(sb)
        console.sendMessage("Saved history to file (${sb.length / 1000} kb)")
    }

    fun fill() {
        items.remaining.addAll(
            Material.values().filter {
                (it.isItem && (it.creativeCategory != null) && it != Material.PLAYER_HEAD && !it.name.contains("SPAWN_EGG")) || it == Material.DRAGON_HEAD
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

        val inv = Invalidating()
        val allSounds = inv.filterInvalidSounds(Sound.values().toList())
        allSounds.removeAll(inv.getInvalidSounds())
        sounds.remaining.addAll(allSounds)
    }

    private fun filter() {
        val file = File(Manager.dataFolder.path + "//history.json")
        if (!file.exists()) {
            Manager.dataFolder.mkdirs()
            file.createNewFile()
            file.writeText(Json.encodeToString(JsonFormat(emptyList(), emptyList(), emptyList(), emptyList())))
            console.sendMessage("No history data found! `history.json` created for further use")
        } else {
            val input = file.readText()
            console.sendMessage("History data found! Loading ${file.length() / 1000.0}kb...")
            val data = Json.decodeFromString<JsonFormat>(input)
            items.addFinished(data.items)
            mobs.addFinished(data.mobs)
            advancements.addFinished(data.advancements)
            sounds.addFinished(data.sounds)
            console.sendMessage("§aSuccessfully load history!")
        }
    }

    init {
        fill()
        filter()
    }
}