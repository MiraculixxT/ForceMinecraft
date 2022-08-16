package de.miraculixx.forcemc.modules.events

import net.axay.kspigot.event.listen
import org.bukkit.event.player.PlayerAdvancementDoneEvent

class GrantAdvancement: Event {
    override fun register() {
        onAdvancement.register()
    }

    override fun unregister() {
        onAdvancement.unregister()
    }

    val onAdvancement = listen<PlayerAdvancementDoneEvent> {
        if (ForceManager.currentGoal == it.advancement.key.key) {
            ForceManager.next()
        }
    }
}