package de.miraculixx.forcemc.modules.display

import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.modules.data.SearchType
import de.miraculixx.forcemc.utils.*
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.axay.kspigot.items.customModel
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.task
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class HistoryDisplay(private val player: Player) {
    private val items = ForceManager.items.finished.map { itemStack(it) { meta { name = cmp(it.name.fancy(), cHighlight) } } }
    private val mobs = ForceManager.mobs.finished.map { itemStack(toItem(it.name, SearchType.MOB)) { meta { name = cmp(it.name.fancy(), cHighlight) } } }
    private val advancements =
        ForceManager.advancements.finished.map { itemStack(toItem(it.key.key, SearchType.ADVANCEMENT)) { meta { name = cmp(it.key.key.replace("/", "_-_").fancy(), cHighlight) } } }
    private val sounds = ForceManager.sounds.finished.map { itemStack(toItem(it.name, SearchType.SOUND)) { meta { name = cmp(it.name.fancy(), cHighlight) } } }

    private val title = cmp("Finished Goals", cHighlight)
    private val inventory = Bukkit.createInventory(null, 6 * 9, title)
    private var state = SearchType.ITEM
    private var site = 0

    private val onClick = listen<InventoryClickEvent> {
        if (it.whoClicked != player) return@listen
        if (it.inventory != inventory) return@listen
        val item = it.currentItem
        it.isCancelled = true

        when (item?.itemMeta?.customModel ?: 0) {
            1 -> {
                state = SearchType.ITEM
                craftInventory(inventory)
                fillHistoryContent(0)
                player.click()
            }

            2 -> {
                state = SearchType.MOB
                craftInventory(inventory)
                fillHistoryContent(0)
                player.click()
            }

            3 -> {
                state = SearchType.ADVANCEMENT
                craftInventory(inventory)
                fillHistoryContent(0)
                player.click()
            }

            4 -> {
                state = SearchType.SOUND
                craftInventory(inventory)
                fillHistoryContent(0)
                player.click()
            }

            10 -> {
                if (site == 0) {
                    craftInventory(inventory)
                    player.error()
                } else fillHistoryContent(-3)
            }

            11 -> {
                fillHistoryContent(3)
            }
        }
    }

    private val onClose = listen<InventoryCloseEvent> {
        if (it.player != player) return@listen
        if (it.inventory != inventory) return@listen
        close()
    }

    private fun craftInventory(inv: Inventory) {
        val ph = itemStack(Material.GRAY_STAINED_GLASS_PANE) {
            meta {
                name = cmp(" ")
                customModel = 0
            }
        }
        repeat(18) {
            inv.setItem(it, ph)
        }
        inv.setItem(1, itemStack(Material.CRAFTING_TABLE) {
            meta {
                val isCurrent = state == SearchType.ITEM
                name = cmp("Item History", cHighlight, bold = isCurrent)
                customModel = 1
                if (isCurrent) {
                    addEnchant(Enchantment.DURABILITY, 1, true)
                    addItemFlags(ItemFlag.HIDE_ENCHANTS)
                }
            }
        })
        inv.setItem(3, itemStack(Material.DIAMOND_SWORD) {
            meta {
                val isCurrent = state == SearchType.MOB
                name = cmp("Mob History", cHighlight, bold = isCurrent)
                customModel = 2
                addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                if (isCurrent) {
                    addEnchant(Enchantment.DURABILITY, 1, true)
                    addItemFlags(ItemFlag.HIDE_ENCHANTS)
                }
            }
        })
        inv.setItem(5, itemStack(Material.KNOWLEDGE_BOOK) {
            meta {
                val isCurrent = state == SearchType.ADVANCEMENT
                name = cmp("Advancement History", cHighlight, bold = isCurrent)
                customModel = 3
                if (isCurrent) {
                    addEnchant(Enchantment.DURABILITY, 1, true)
                    addItemFlags(ItemFlag.HIDE_ENCHANTS)
                }
            }
        })
        inv.setItem(7, itemStack(Material.JUKEBOX) {
            meta {
                val isCurrent = state == SearchType.SOUND
                name = cmp("Sound History", cHighlight, bold = isCurrent)
                customModel = 4
                if (isCurrent) {
                    addEnchant(Enchantment.DURABILITY, 1, true)
                    addItemFlags(ItemFlag.HIDE_ENCHANTS)
                }
            }
        })
        inv.setItem(
            9, buildItem(
                Material.PLAYER_HEAD, 10, cmp("Move Back"), emptyList(),
                if (site == 0) "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=" else "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjUzNDc0MjNlZTU1ZGFhNzkyMzY2OGZjYTg1ODE5ODVmZjUzODlhNDU0MzUzMjFlZmFkNTM3YWYyM2QifX19"
            )
        )
        inv.setItem(
            17, buildItem(
                Material.PLAYER_HEAD, 11, cmp("Move Forward"), emptyList(),
                if (this.inventory.getItem(53)?.type?.isAir == true) "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNmZTg4NDVhOGQ1ZTYzNWZiODc3MjhjY2M5Mzg5NWQ0MmI0ZmMyZTZhNTNmMWJhNzhjODQ1MjI1ODIyIn19fQ==" else "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGVmMzU2YWQyYWE3YjE2NzhhZWNiODgyOTBlNWZhNWEzNDI3ZTVlNDU2ZmY0MmZiNTE1NjkwYzY3NTE3YjgifX19"
            )
        )
    }

    private fun fillHistoryContent(moveSite: Int) {
        val categoryItems = when (state) {
            SearchType.ITEM -> items
            SearchType.MOB -> mobs
            SearchType.ADVANCEMENT -> advancements
            SearchType.SOUND -> sounds
            SearchType.NOTHING -> emptyList()
        }
        if (categoryItems.size < ((site + moveSite) * 9)) {
            player.error()
            return
        }
        site += moveSite
        when {
            moveSite > 0 -> {
                if (categoryItems.size < 4 * 9 + 1) return
                val newItems = categoryItems.subList(categoryItems.indexOfFirst { it == inventory.getItem(53) }.plus(1), categoryItems.size.minus(1))
                task(true, 0, 1, 3 * 9) {
                    val items = inventory.contents.clone()
                    items.forEachIndexed { index, itemStack ->
                        if (index < 18) return@forEachIndexed
                        val newPosition = index - 1
                        if (newPosition !in 18..53) return@forEachIndexed
                        inventory.setItem(newPosition, itemStack)
                        inventory.setItem(index, null)
                    }
                    inventory.addItem(newItems.getOrNull(it.counterUp!!.toInt()) ?: ItemStack(Material.AIR))
                    player.click(0.4f)
                }
            }

            else -> {
                site = 0
                craftInventory(inventory)
                inventory.forEachIndexed { index, _ ->
                    if (index > 17) inventory.setItem(index, ItemStack(Material.AIR))
                }
                categoryItems.forEachIndexed { index, itemStack ->
                    if ((index + 18) > inventory.size) return
                    inventory.setItem(index + 18, itemStack)
                }
            }
        }
    }


    private fun close() {
        onClose.unregister()
        onClick.unregister()
    }

    init {
        craftInventory(inventory)
        fillHistoryContent(0)
        player.openInventory(inventory)
    }
}