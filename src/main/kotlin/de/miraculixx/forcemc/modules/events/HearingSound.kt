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
import org.bukkit.Instrument
import org.bukkit.Material
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.entity.LivingEntity
import org.bukkit.event.block.*
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.util.Vector

class HearingSound : Event {
    override fun register() {
        onAnvil.register()
        onBlockBreak.register()
        onBlockExplode.register()
        onBlockPlace.register()
        onClick.register()
        onInvClose.register()
        onInvOpen.register()
        onMove.register()
        onPistonPush.register()
        onPistonRetract.register()
    }

    override fun unregister() {
        onAnvil.unregister()
        onBlockBreak.unregister()
        onBlockExplode.unregister()
        onBlockPlace.unregister()
        onClick.unregister()
        onInvClose.unregister()
        onInvOpen.unregister()
        onMove.unregister()
        onPistonPush.unregister()
        onPistonRetract.unregister()
    }

    private val onBlockBreak = listen<BlockBreakEvent> {
        val type = it.block.type
        checkKey("BLOCK_${type.name}_BREAK")
        when {
            type == Material.STRING -> checkKey("BLOCK_TRIPWIRE_DETACH")
            type.name.contains("_WOOL") -> checkKey("BLOCK_WOOL_BREAK")
            type.name.contains("_LOG") -> checkKey("BLOCK_WOOD_BREAK")
            else -> {}
        }
    }

    private val onBlockPlace = listen<BlockPlaceEvent> {
        val type = it.block.type
        checkKey("BLOCK_${it.block.type.name}_PLACE")
        when {
            type == Material.STRING -> checkKey("BLOCK_TRIPWIRE_ATTACH")
            type.name.contains("_WOOL") -> checkKey("BLOCK_WOOL_PLACE")
            type.name.contains("_LOG") -> checkKey("BLOCK_WOOD_PLACE")
            else -> {}
        }
    }

    private val onMove = listen<PlayerMoveEvent> {
        // Block Step & Fall
        val nameSubBlock = it.to.clone().subtract(Vector(0, 1, 0)).block.type.name
        checkKey("BLOCK_${nameSubBlock}_STEP")
        checkKey("BLOCK_${nameSubBlock}_FALL")

        // Check Ambiente
        val biome = it.to.block.biome.name
        checkKey("AMBIENT_${biome}_LOOP")
        checkKey("AMBIENT_${biome}_MOOD")
        checkKey("AMBIENT_${biome}_ADDITIONS")

        // Block Contact
        val block = it.to.block
        when (block.type) {
            Material.NETHER_PORTAL -> {
                checkKey("BLOCK_PORTAL_TRAVEL")
                checkKey("BLOCK_PORTAL_TRIGGER")
            }

            else -> {}
        }
    }

    private val onAnvil = listen<InventoryClickEvent> {
        if (it.inventory.type == InventoryType.ANVIL && it.slot == 2) checkKey("BLOCK_ANVIL_USE")
        if (it.inventory.type == InventoryType.ENCHANTING) checkKey("BLOCK_ENCHANTMENT_TABLE_USE")
    }

    private val onInvClose = listen<InventoryCloseEvent> {
        checkKey("BLOCK_${it.inventory.type.name}_CLOSE")
        if (it.inventory.type == InventoryType.BREWING) checkKey("BLOCK_BREWING_STAND_BREW")
    }
    private val onInvOpen = listen<InventoryCloseEvent> {
        checkKey("BLOCK_${it.inventory.type.name}_OPEN")
    }

    private val onPistonPush = listen<BlockPistonExtendEvent> {
        checkKey("BLOCK_PISTON_EXTEND")
    }
    private val onPistonRetract = listen<BlockPistonRetractEvent> {
        checkKey("BLOCK_PISTON_EXTEND")
    }

    private val onBlockExplode = listen<BlockExplodeEvent> {
        val block = it.block
        checkKey("BLOCK_${block.type}_DEPLETE")
    }

    private val onClick = listen<PlayerInteractEvent> {
        if (it.clickedBlock != null) {
            val blockType = it.clickedBlock!!.type
            val blockName = blockType.name
            when (blockType) {
                Material.CAKE -> checkKey("BLOCK_CAKE_ADD_CANDLE")
                Material.CAVE_VINES_PLANT -> checkKey("BLOCK_CAVE_VINES_PICK_BERRIES")
                Material.SWEET_BERRY_BUSH -> checkKey("BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES")
                Material.COMPOSTER -> {
                    checkKey("BLOCK_COMPOSTER_EMPTY")
                    checkKey("BLOCK_COMPOSTER_FILL")
                    checkKey("BLOCK_COMPOSTER_FILL_SUCCESS")
                }

                Material.END_PORTAL_FRAME -> {
                    checkKey("BLOCK_END_PORTAL_FRAME_FILL")
                    checkKey("BLOCK_END_PORTAL_SPAWN")
                }

                Material.NOTE_BLOCK -> {
                    val name = when (val i = (it.clickedBlock as NoteBlock).instrument) {
                        Instrument.PIANO -> "BASEDRUM"
                        Instrument.BASS_DRUM -> "BASS"
                        Instrument.SNARE_DRUM -> "HARP"
                        Instrument.STICKS -> "HAT"
                        Instrument.BASS_GUITAR -> "SNARE"
                        else -> i.name
                    }
                    checkKey("BLOCK_NOTE_BLOCK_$name")
                }

                Material.PUMPKIN -> if (it.item?.type == Material.SHEARS) checkKey("BLOCK_PUMPKIN_CARVE")
                Material.RESPAWN_ANCHOR -> {
                    checkKey("BLOCK_RESPAWN_ANCHOR_SET_SPAWN")
                    checkKey("BLOCK_RESPAWN_ANCHOR_CHARGE")
                }

                else -> {
                    when {
                        blockName.contains("_DOOR") -> {
                            checkKey("BLOCK_WOODEN_DOOR_CLOSE")
                            checkKey("BLOCK_WOODEN_DOOR_OPEN")
                        }

                        blockName.contains("_TRAPDOOR") -> {
                            checkKey("BLOCK_WOODEN_TRAPDOOR_CLOSE")
                            checkKey("BLOCK_WOODEN_TRAPDOOR_OPEN")
                        }
                    }
                }
            }
            if (it.hand == EquipmentSlot.HAND) checkKey("BLOCK_${blockName}_HIT")
            checkKey("BLOCK_${blockName}_USE")
            checkKey("BLOCK_${blockName}_CLICK")
        }
        if (it.action == Action.PHYSICAL) {
            val block = it.interactionPoint?.block?.type?.name ?: return@listen
            checkKey("BLOCK_${block}_CLICK_OFF")
            checkKey("BLOCK_${block}_CLICK_ON")
            checkKey("BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF")
            checkKey("BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON")
        }
    }


    private fun checkKey(key: String) {
        if (key == ForceManager.currentGoal) {
            ToastNotification("Sound Heard: ${ForceManager.currentGoal.fancy()}", toItem(key, SearchType.SOUND), "Force Minecraft by Miraculixx", FrameType.GOAL).broadcast()
            ForceManager.next()
        }
    }
}