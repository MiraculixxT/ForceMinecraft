package de.miraculixx.forcemc.commands

import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.utils.cmp
import de.miraculixx.forcemc.utils.plus
import de.miraculixx.forcemc.utils.prefix
import net.axay.kspigot.extensions.bukkit.register
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SkipCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val player = sender as? Player ?: return false

        ForceManager.next()
        player.sendMessage(prefix + cmp("Skipped the current goal"))
        return true
    }

    init {
        register("skip")
    }
}