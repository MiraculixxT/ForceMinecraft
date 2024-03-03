package de.miraculixx.forcemc

import de.miraculixx.forcemc.commands.MainCommand
import de.miraculixx.forcemc.commands.SkipCommand
import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.modules.events.HearSounds
import net.axay.kspigot.main.KSpigot

class ForceMinecraft : KSpigot() {
    companion object {
        lateinit var INSTANCE: ForceMinecraft; private set
    }

    override fun startup() {
        // Initialize Systems
        INSTANCE = this
        HearSounds
        ForceManager

        // Command registration
        MainCommand()
        SkipCommand()
    }

    override fun shutdown() {
        ForceManager.shutdown()
    }
}

val Manager by lazy { ForceMinecraft.INSTANCE }
