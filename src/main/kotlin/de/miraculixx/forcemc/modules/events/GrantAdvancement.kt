package de.miraculixx.forcemc.modules.events

import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.modules.data.Event
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.register
import net.axay.kspigot.event.unregister
import org.bukkit.event.player.PlayerAdvancementDoneEvent

class GrantAdvancement: Event {
    override fun register() {
        onAdvancement.register()
    }

    override fun unregister() {
        onAdvancement.unregister()
    }

    private val onAdvancement = listen<PlayerAdvancementDoneEvent> {
        if (ForceManager.currentGoal == it.advancement.key.key) {
            ForceManager.next()
        }
    }
}