package de.miraculixx.forcemc.modules.events

import de.miraculixx.forcemc.modules.data.Event
import net.axay.kspigot.event.listen

class HearingSound: Event {
    override fun register() {
        onEntityDamage.register()
    }

    override fun unregister() {
        onEntityDamage.unregister()
    }

    private val onEntityDamage = listen<EntityDamageEvent> {
        // Entity Damage & Entity Death 
        val name = it.entity.type.name
        checkKey("ENTITY_$name_HIT")
        val lv = it.entity as? LivingEntity ?: return@listen
        if ((it.entity.health - it.finalDamage) <= 0.0) {
            checkKey("ENTITY_$name_DEATH")
        }
    }


    private fun checkKey(key: String) {
        if (key == ForceManager.currentGoal) {
            ForceManager.next()
        }
    }
}