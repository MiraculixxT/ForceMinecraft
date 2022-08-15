package de.miraculixx.forcemc.modules.data

import kotlinx.serialization.Serializable
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.advancement.Advancement
import org.bukkit.entity.EntityType

@Serializable
data class JsonFormat(val items: List<Material>,
                      val mobs: List<EntityType>,
                      val advancements: List<@Serializable(with = AdvancementSerializer::class) Advancement>,
                      val sounds: List<Sound>)