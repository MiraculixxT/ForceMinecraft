package de.miraculixx.forcemc.modules.display

import de.miraculixx.forcemc.modules.data.SearchType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey

fun toItem(key: String, type: SearchType): Material {
    return when (type) {
        SearchType.ITEM -> Material.getMaterial(key) ?: Material.STONE
        SearchType.MOB -> Material.getMaterial("${key}_SPAWN_EGG") ?: Material.POLAR_BEAR_SPAWN_EGG
        SearchType.ADVANCEMENT -> Bukkit.getAdvancement(NamespacedKey.fromString(key.lowercase())!!)?.display?.icon()?.type ?: Material.KNOWLEDGE_BOOK
        SearchType.SOUND -> Material.JUKEBOX
        SearchType.NOTHING -> Material.BARRIER
    }
}