package de.miraculixx.forcemc.modules.events

import net.axay.kspigot.event.listen

class ItemGathering: Event {
    override fun register() {
        onAdvancement.register()
    }

    override fun unregister() {
        onAdvancement.unregister()
    }

    val onAdvancement = listen<PlayerPickupItemEvent> {
        if (ForceManager.currentGoal == it.item.type) {
            ForceManager.next()
        }
    }
}