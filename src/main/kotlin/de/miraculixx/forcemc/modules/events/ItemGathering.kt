package de.miraculixx.forcemc.modules.events

import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.modules.data.Event
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.register
import net.axay.kspigot.event.unregister
import org.bukkit.event.entity.EntityPickupItemEvent

class ItemGathering: Event {
    override fun register() {
        onPickup.register()
    }

    override fun unregister() {
        onPickup.unregister()
    }

    private val onPickup = listen<EntityPickupItemEvent> {
        if (ForceManager.currentGoal == it.item.type.name) {
            ForceManager.next()
        }
    }
}