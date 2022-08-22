package de.miraculixx.forcemc

import de.miraculixx.forcemc.commands.MainCommand
import de.miraculixx.forcemc.commands.SkipCommand
import de.miraculixx.forcemc.modules.ForceManager
import net.axay.kspigot.main.KSpigot

class ForceMinecraft : KSpigot() {
    companion object {
        lateinit var INSTANCE: ForceMinecraft; private set
    }

    override fun startup() {
        // Initialize Systems
        ForceManager

        // Command registration
        MainCommand()
        SkipCommand()
    }

    override fun shutdown() {

    }
}

val Manager by lazy { ForceMinecraft.INSTANCE }