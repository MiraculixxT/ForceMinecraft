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
        val key = it.entity.type.name
        if (ForceManager.currentGoal == key) {
            ToastNotification("Mob Killed: ${ForceManager.currentGoal.fancy()}", toItem(key, SearchType.MOB), "Force Minecraft by Miraculixx", FrameType.GOAL).broadcast()
            ForceManager.next()
        }
    }
}