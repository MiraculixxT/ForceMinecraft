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
import net.axay.kspigot.extensions.broadcast
import net.minecraft.advancements.FrameType
import org.bukkit.Material
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent

class ItemGathering : Event {
    override fun register() {
        onPickup.register()
        onClick.register()
    }

    override fun unregister() {
        onPickup.unregister()
        onClick.unregister()
    }

    private val onPickup = listen<EntityPickupItemEvent> {
        check(it.item.itemStack.type.name)
    }

    private val onClick = listen<InventoryClickEvent> {
        if (it.isCancelled) return@listen
        check(it.currentItem?.type?.name ?: "")
    }

    private fun check(key: String) {
        if (ForceManager.currentGoal == key) {
            ToastNotification("Item Gathered: ${ForceManager.currentGoal.fancy()}", toItem(key, SearchType.ITEM), "Force Minecraft by Miraculixx", FrameType.GOAL).broadcast()
            ForceManager.next()
        }
    }
}