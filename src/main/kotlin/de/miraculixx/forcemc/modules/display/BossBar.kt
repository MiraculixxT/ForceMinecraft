package de.miraculixx.forcemc.modules.display

import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.utils.cHighlight
import de.miraculixx.forcemc.utils.cmp
import de.miraculixx.forcemc.utils.mm
import de.miraculixx.forcemc.utils.plus
import net.axay.kspigot.event.listen
import net.axay.kspigot.event.unregister
import net.axay.kspigot.extensions.onlinePlayers
import net.axay.kspigot.runnables.task
import net.axay.kspigot.runnables.taskRunLater
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.Component
import org.bukkit.event.player.PlayerJoinEvent

class BossBar {
    private val bar = BossBar.bossBar(cmp("loading data...", cHighlight), 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS)
    private lateinit var prefix: Component
    private lateinit var rawGoal: String
    private lateinit var suffix: Component

    private var counter = .0f
    private val modifier = .08f
    private var running = true
    private val scheduler = task(false, 3, 1) {
        if (!running) return@task
        counter = if ((counter - modifier) <= -0.99f) .9f else counter - modifier
        bar.name(prefix + mm.deserialize("<b><gradient:#DB49B7:#5D6CD9:$counter>   $rawGoal</gradient></b>") + suffix)
    }

    private val onJoin = listen<PlayerJoinEvent> {
        it.player.showBossBar(bar)
    }

    fun update(success: Boolean) {
        if (success) {
            running = false
            task(false, 0, 10, 8) {
                val color = if ((it.counterDownToZero?.rem(2)) == 0L) "#55ff55" else "#00aa00"
                bar.name(prefix + mm.deserialize("<b><color:$color>   $rawGoal</color></b>") + suffix)
            }
            taskRunLater(80) {
                running = true
                updateValues()
            }
        } else updateValues()
    }

    private fun updateValues() {
        val remaining = ForceManager.items.remaining.size + ForceManager.mobs.remaining.size + ForceManager.advancements.remaining.size + ForceManager.sounds.remaining.size
        val finished = ForceManager.items.finished.size + ForceManager.mobs.finished.size + ForceManager.advancements.finished.size + ForceManager.sounds.finished.size
        val progress = 1f - (remaining.toFloat() / (remaining + finished))
        bar.progress(progress)

        prefix = mm.deserialize("<color:#a980cf>${ForceManager.currentType.name.fancy()}</color> ≫ ")
        rawGoal = ForceManager.currentGoal.replace("/", "_-_").fancy()
        suffix = mm.deserialize(" <color:#906db0>(<color:#a980cf>$finished/${remaining + finished}</color>)</color>")
    }


    fun shutdown() {
        scheduler?.cancel()
        onJoin.unregister()
        onlinePlayers.forEach {
            it.hideBossBar(bar)
        }
    }

    init {
        update(false)
        onlinePlayers.forEach {
            it.showBossBar(bar)
        }
    }
}