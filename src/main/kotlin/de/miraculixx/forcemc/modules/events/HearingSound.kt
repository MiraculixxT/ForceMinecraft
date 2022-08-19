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
        val key = it.entity.sound
    }


    private fun checkKey(key: String) {
        if (key == ForceManager.currentGoal) {
            ForceManager.next()
        }
    }
}