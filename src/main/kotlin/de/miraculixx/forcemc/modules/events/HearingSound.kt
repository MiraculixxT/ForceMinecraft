package de.miraculixx.forcemc.modules.events

import de.miraculixx.forcemc.modules.data.Event
import net.axay.kspigot.event.listen

class HearingSound: Event {
    override fun register() {
        onAdvancement.register()
    }

    override fun unregister() {
        onAdvancement.unregister()
    }

    private val onEntityDamage = listen<EntityDamageEvent> {
        // Entity Damage & Entity Death 
    }
}