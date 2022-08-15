package de.miraculixx.forcemc

import net.axay.kspigot.main.KSpigot

class ForceMinecraft: KSpigot() {
    companion object {
        lateinit var INSTANCE: ForceMinecraft; private set
    }

    override fun startup() {

    }

    override fun shutdown() {

    }
}

val Manager by lazy { ForceMinecraft.INSTANCE }