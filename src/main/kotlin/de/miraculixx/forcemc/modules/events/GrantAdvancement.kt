package de.miraculixx.forcemc.modules.events

import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.modules.data.Event
import de.miraculixx.forcemc.modules.data.SearchType
import de.miraculixx.forcemc.modules.display.ToastNotification
import de.miraculixx.forcemc.modules.display.fancy
import de.miraculixx.forcemc.modules.display.toItem
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.register
import net.axay.kspigot.event.unregister
import net.minecraft.advancements.FrameType
import org.bukkit.Material
import org.bukkit.event.player.PlayerAdvancementDoneEvent

class GrantAdvancement : Event {
    override fun register() {
        onAdvancement.register()
    }

    override fun unregister() {
        onAdvancement.unregister()
    }

    private val onAdvancement = listen<PlayerAdvancementDoneEvent> {
        val key = it.advancement.key.key
        if (ForceManager.currentGoal == key) {
            ForceManager.next()
        }
    }
}