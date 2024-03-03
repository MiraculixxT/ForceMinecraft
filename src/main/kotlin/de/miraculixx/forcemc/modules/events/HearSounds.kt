package de.miraculixx.forcemc.modules.events

import de.miraculixx.forcemc.modules.ForceManager
import de.miraculixx.forcemc.modules.data.SearchType
import de.miraculixx.forcemc.modules.display.ToastNotification
import de.miraculixx.forcemc.modules.display.fancy
import de.miraculixx.forcemc.modules.display.toItem
import de.miraculixx.forcemc.utils.cError
import de.miraculixx.forcemc.utils.cmp
import de.miraculixx.forcemc.utils.plus
import de.miraculixx.forcemc.utils.prefix
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.console
import net.axay.kspigot.runnables.sync
import net.minecraft.advancements.FrameType
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket
import net.minecraft.network.protocol.game.ClientboundSoundPacket
import org.bukkit.Sound
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent


object HearSounds {
    val onJoin = listen<PlayerJoinEvent> {
        injectPlayer(it.player)
    }
    val onLeave = listen<PlayerQuitEvent> {
        removePlayer(it.player)
    }

    private fun removePlayer(player: Player) {
        val channel = (player as CraftPlayer).handle.connection.connection.channel
        channel.eventLoop().submit {
            channel.pipeline().remove(player.name)
        }
        console.sendMessage(prefix + cmp("Removed Player ${player.name} from packet stream"))
    }

    private fun injectPlayer(player: Player) {
        val channelDuplexHandler = PlayerChannelDuplexHandler()

        val pipeline = (player as CraftPlayer).handle.connection.connection.channel.pipeline()
        pipeline.addBefore("packet_handler", player.name, channelDuplexHandler)
        console.sendMessage(prefix + cmp("Injected Player ${player.name} to packet stream"))
    }

    class PlayerChannelDuplexHandler : ChannelDuplexHandler() {
        override fun channelRead(ctx: ChannelHandlerContext, packet: Any) {
            super.channelRead(ctx, packet)
        }

        override fun write(ctx: ChannelHandlerContext?, packet: Any?, promise: ChannelPromise?) {
            when (packet) {
                is ClientboundSoundPacket -> {
                    checkKey(packet.sound.location.toString())
                }

                is ClientboundSoundEntityPacket -> {
                    checkKey(packet.sound.location.toString())
                }

                is ClientboundCustomSoundPacket -> {
                    checkKey(packet.name.namespace)
                }
            }
            super.write(ctx, packet, promise)
        }

        private fun checkKey(key: String) {
            if (ForceManager.currentType != SearchType.SOUND) return
            val sound = Sound.values().firstOrNull { it.key.asString() == key }
            if (sound == null) {
                console.sendMessage(prefix + cmp("Could not fetch packet key! Key: $key", cError))
                return
            }
            if (ForceManager.currentGoal == sound.name) {
                sync {
                    ToastNotification("Sound Heard: ${ForceManager.currentGoal.fancy()}", toItem(key, SearchType.SOUND), "Force Minecraft by Miraculixx", FrameType.GOAL).broadcast()
                    ForceManager.next()
                }
            }
        }
    }
}