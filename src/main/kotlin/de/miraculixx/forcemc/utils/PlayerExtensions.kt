package de.miraculixx.forcemc.utils

import org.bukkit.Sound
import org.bukkit.entity.Player

fun Player.click(volume: Float = 0.7f) {
    playSound(this, Sound.UI_BUTTON_CLICK, volume, 1f)
}

fun Player.error() {
    playSound(this, Sound.ENTITY_ENDERMAN_TELEPORT, 1.1f, 1f)
}
