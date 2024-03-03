package de.miraculixx.forcemc.modules.data

import org.bukkit.Sound

class Invalidating {
    fun getInvalidSounds(): List<Sound> {
        return listOf(
            Sound.BLOCK_AMETHYST_BLOCK_CHIME,
            Sound.AMBIENT_CAVE,
            Sound.BLOCK_ANVIL_FALL,
            Sound.BLOCK_ANVIL_LAND,
            Sound.BLOCK_BEACON_ACTIVATE,
            Sound.BLOCK_BEACON_DEACTIVATE,
            Sound.BLOCK_BEACON_POWER_SELECT,
            Sound.BLOCK_BELL_RESONATE,
            Sound.BLOCK_BIG_DRIPLEAF_TILT_DOWN,
            Sound.BLOCK_BIG_DRIPLEAF_TILT_UP,
            Sound.BLOCK_CHORUS_FLOWER_DEATH,
            Sound.BLOCK_CHORUS_FLOWER_GROW,
            Sound.BLOCK_COMPOSTER_READY,
            Sound.BLOCK_END_GATEWAY_SPAWN,
            Sound.BLOCK_FROGSPAWN_HATCH,
            Sound.BLOCK_GROWING_PLANT_CROP,
            Sound.BLOCK_HONEY_BLOCK_SLIDE,
            Sound.BLOCK_LAVA_POP,
            Sound.BLOCK_POINTED_DRIPSTONE_DRIP_LAVA,
            Sound.BLOCK_POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON,
            Sound.BLOCK_POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON,
            Sound.BLOCK_POINTED_DRIPSTONE_DRIP_WATER,
            Sound.BLOCK_REDSTONE_TORCH_BURNOUT,
            Sound.BLOCK_SCULK_CATALYST_BLOOM,
            Sound.BLOCK_SCULK_CHARGE,
            Sound.BLOCK_SCULK_SENSOR_CLICKING,
            Sound.BLOCK_SCULK_SENSOR_CLICKING_STOP,
            Sound.BLOCK_SCULK_SHRIEKER_SHRIEK,
            Sound.BLOCK_SCULK_SPREAD,
            Sound.BLOCK_SMOKER_SMOKE,
        )
    }

    fun filterInvalidSounds(list: Collection<Sound>): MutableList<Sound> {
        return list.filter {
            !it.name.startsWith("MUSIC_") &&
                    !it.name.startsWith("AMBIENT_UNDERWATER_") &&
                    !it.name.startsWith("BLOCK_BEEHIVE_") &&
                    !it.name.startsWith("BLOCK_BUBBLE_COLUMN") &&
                    (!it.name.contains("_AMBIENT") && !it.name.startsWith("ENTITY_")) &&
                    !it.name.endsWith("_CRACKLE") &&
                    !it.name.endsWith("_EXTINGUISH") &&
                    !it.name.startsWith("BLOCK_CONDUIT") &&
                    !it.name.startsWith("BLOCK_DISPENSER") &&
                    !it.name.contains("_IDLE")
        }.toMutableList()
    }
}