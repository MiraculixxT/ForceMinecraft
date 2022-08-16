package de.miraculixx.forcemc.modules.events

import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.modules.data.Event
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.register
import net.axay.kspigot.event.unregister
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent

class MobKill: Event {
    override fun register() {
        onKill.register()
    }

    override fun unregister() {
        onKill.unregister()
    }

    private val onKill = listen<EntityDamageByEntityEvent> {
        val entity = it.entity as? LivingEntity ?: return@listen
        if ((entity.health - it.finalDamage) > 0.0) return@listen
        if (ForceManager.currentGoal == it.entity.type.name) {
            ForceManager.next()
        }
    }
}