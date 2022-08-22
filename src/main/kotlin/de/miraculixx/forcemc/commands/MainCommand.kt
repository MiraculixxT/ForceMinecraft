package de.miraculixx.forcemc.commands

import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.modules.data.SearchType
import de.miraculixx.forcemc.utils.cError
import de.miraculixx.forcemc.utils.cmp
import de.miraculixx.forcemc.utils.plus
import de.miraculixx.forcemc.utils.prefix
import net.axay.kspigot.extensions.bukkit.register
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
            player.sendMessage(prefix + cmp("Start - Stop - Reset"))
            return false
        }

        when (args.getOrNull(0)?.lowercase()) {
            "start" -> {
                if (ForceManager.start()) {
                    player.sendMessage(prefix + cmp("Force Minecraft startet!"))
                } else {
                    player.sendMessage(prefix + cmp("Force Minecraft is already started", cError))
                }
            }
            "stop" -> {
                ForceManager.currentEvent?.unregister()
                ForceManager.currentEvent = null
                ForceManager.bossBar?.shutdown()
                player.sendMessage(prefix + cmp("Force Minecraft stopped!"))
            }
            "reset" -> {
                val file = File(Path("MUtils/history.json").toAbsolutePath().toString())
                file.deleteOnExit()
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
                ForceManager.fill()
                player.sendMessage(prefix + cmp("Everything reset and stopped! ") + cmp("(This process cannot be undone)", cError))
            }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String> {
        return when (args?.size ?: 0) {
            0, 1 -> mutableListOf("start", "stop", "reset")

            else -> mutableListOf()
        }
    }

    init {
        register("forcemc")
    }
}