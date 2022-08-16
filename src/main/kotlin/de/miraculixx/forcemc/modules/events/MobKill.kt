package de.miraculixx.forcemc.modules.events

import net.axay.kspigot.event.listen

class MobKill: Event {
    override fun register() {
        onAdvancement.register()
    }

    override fun unregister() {
        onAdvancement.unregister()
    }

    val onAdvancement = listen<EntityDamageByEntityEvent> {
        
    }
}