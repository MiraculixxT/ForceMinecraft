package de.miraculixx.forcemc.modules.display

import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.utils.cmp
import de.miraculixx.forcemc.utils.emptyComponent
import de.miraculixx.forcemc.utils.mm
import de.miraculixx.forcemc.utils.plus
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.task
import net.kyori.adventure.bossbar.BossBar

class BossBar {
    private val bar = BossBar.bossBar(cmp("init..."), 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS)
    private var prefix = emptyComponent()
    private var rawGoal = ""
    private var suffix = emptyComponent()

    private var counter = .0f
    private val scheduler = task(false, 1,1) {
        bar.name(prefix + mm.deserialize("<b><gradient:#DB49B7:#5D6CD9:$counter>$rawGoal</gradient></b>") + suffix)
        counter = if (counter <= -1.0) .9f else counter - .1f
    }

    fun update() {
        val remaining = ForceManager.items.remaining.size + ForceManager.mobs.remaining.size + ForceManager.advancements.remaining.size + ForceManager.sounds.remaining.size
        val finished = ForceManager.items.finished.size + ForceManager.mobs.finished.size + ForceManager.advancements.finished.size + ForceManager.sounds.finished.size
        val progress = remaining.toFloat() / (remaining + finished)
        bar.progress(progress)

        prefix = mm.deserialize("<b><color:#a980cf>${fancy(ForceManager.currentType.name)}</color></b> ≫ ")
        rawGoal = fancy(ForceManager.currentGoal)
        suffix = mm.deserialize(" <color:#906db0>(<color:#a980cf>$finished/${remaining + finished}</color>)</color>")
    }

    fun shutdown() {
        scheduler?.cancel()
        onlinePlayers.forEach {
            it.hideBossBar(bar)
        }
    }

    init {
        onlinePlayers.forEach {
            it.showBossBar(bar)
        }
    }
}