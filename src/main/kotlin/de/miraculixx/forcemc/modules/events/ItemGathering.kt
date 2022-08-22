package de.miraculixx.forcemc.modules.events

import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.modules.data.Event
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.register
import net.axay.kspigot.event.unregister
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent

class ItemGathering: Event {
    override fun register() {
        onPickup.register()
        onClick.register()
    }

    override fun unregister() {
        onPickup.unregister()
        onClick.unregister()
    }

    private val onPickup = listen<EntityPickupItemEvent> {
        if (ForceManager.currentGoal == it.item.type.name) {
            ForceManager.next()
        }
    }

    private val onClick = listen<InventoryClickEvent> {
        if (it.isCancelled) return@listen
        if (ForceManager.currentGoal == it.currentItem?.type?.name) {
            ForceManager.next()
        } 
    }
}