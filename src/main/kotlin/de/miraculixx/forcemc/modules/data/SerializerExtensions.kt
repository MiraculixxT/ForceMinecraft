package de.miraculixx.forcemc.modules.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.advancement.Advancement

object AdvancementSerializer : KSerializer<Advancement> {
    override val descriptor = PrimitiveSerialDescriptor("Advancement", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Advancement {
        return Bukkit.getAdvancement(NamespacedKey.fromString(decoder.decodeString())!!)!!
    }

    override fun serialize(encoder: Encoder, value: Advancement) {
        encoder.encodeString(value.key.key)
    }
}