package de.miraculixx.forcemc.commands

import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.utils.cmp
import de.miraculixx.forcemc.utils.plus
import de.miraculixx.forcemc.utils.prefix
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

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
                }
            }
            "stop" -> {
                ForceManager.currentEvent?.unregister()
                ForceManager.currentEvent = null
            }
            "reset" -> {
                ForceManager.next()
            }
        }
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>?): MutableList<String> {
        return when (args?.size ?: 0) {
            0, 1 -> mutableListOf("start", "stop", "reset")

            else -> mutableListOf()
        }
    }
}