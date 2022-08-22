package de.miraculixx.forcemc.modules.events

import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.modules.data.Event
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.register
import net.axay.kspigot.event.unregister
import org.bukkit.event.entity.EntityDamageEvent

class HearingSound: Event {
    override fun register() {
        onEntityDamage.register()
    }

    override fun unregister() {
        onEntityDamage.unregister()
    }

    private val onEntityDamage = listen<EntityDamageEvent> {
        // Entity Damage & Entity Death 
        val key = it.entity
    }


    private fun checkKey(key: String) {
        if (key == ForceManager.currentGoal) {
            ForceManager.next()
        }
    }
}