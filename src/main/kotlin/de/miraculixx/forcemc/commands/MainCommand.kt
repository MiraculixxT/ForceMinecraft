package de.miraculixx.forcemc.commands

import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.modules.data.SearchType
import de.miraculixx.forcemc.modules.display.HistoryDisplay
import de.miraculixx.forcemc.utils.*
import net.axay.kspigot.extensions.bukkit.register
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.io.File
import kotlin.io.path.Path

class MainCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = sender as? Player ?: return false

        if (args.isEmpty()) {
            player.sendMessage(prefix + cmp("Available Subcommands"))
            player.sendMessage(prefix + cmp("Start - Stop - Reset", cHighlight))
            return false
        }

        when (args.getOrNull(0)?.lowercase()) {
            "start" -> {
                if (ForceManager.start()) {
                    player.sendMessage(prefix + cmp("Force Minecraft startet!"))
                } else {
                    player.sendMessage(prefix + cmp("Force Minecraft is already started!", cError))
                }
            }
            "stop" -> {
                if (ForceManager.currentEvent == null) {
                    player.sendMessage(prefix + cmp("Force Minecraft is already stopped!", cError))
                    return false
                }
                ForceManager.currentEvent?.unregister()
                ForceManager.currentEvent = null
                ForceManager.bossBar?.shutdown()
                player.sendMessage(prefix + cmp("Force Minecraft stopped"))
            }
            "reset" -> {
                val file = File(Path("plugins/MUtils/history.json").toAbsolutePath().toString())
                file.delete()
                file.createNewFile()
                ForceManager.advancements.clear()
                ForceManager.sounds.clear()
                ForceManager.items.clear()
                ForceManager.mobs.clear()
                ForceManager.currentType = SearchType.NOTHING
                ForceManager.currentEvent?.unregister()
                ForceManager.currentEvent = null
                ForceManager.bossBar?.shutdown()
                ForceManager.bossBar = null
                ForceManager.currentGoal = "error"
                ForceManager.fill()
                player.sendMessage(prefix + cmp("Everything reset and stopped! ") + cmp("(This process cannot be undone)", cError))
            }
            "history" -> {
                player.playSound(player, Sound.BLOCK_ENDER_CHEST_OPEN, 1f, 1f)
                HistoryDisplay(player)
            }
            "remaining" -> {
                sender.sendMessage(prefix + cmp("Es fehlen noch...\n- ${ForceManager.items.remaining.size} Items" +
                        "\n- ${ForceManager.mobs.remaining.size} Mobs" +
                        "\n- ${ForceManager.advancements.remaining.size} Advancements" +
                        "\n- ${ForceManager.sounds.remaining.size} Sounds"))
            }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String> {
        return when (args?.size ?: 0) {
            0, 1 -> mutableListOf("start", "stop", "reset", "history")

            else -> mutableListOf()
        }
    }

    init {
        register("forcemc")
    }
}