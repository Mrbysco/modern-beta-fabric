package com.bespectacled.modernbeta.config;

import java.util.Arrays;
import java.util.List;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.world.biome.BiomeKeys;

@Config(name = "compat_config")
public class ConfigCompat implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public List<String> biomesWithCustomSurfaces = Arrays.asList(
        // Badlands
        BiomeKeys.BADLANDS.getValue().toString(),
        BiomeKeys.BADLANDS_PLATEAU.getValue().toString(),
        BiomeKeys.ERODED_BADLANDS.getValue().toString(),
        BiomeKeys.MODIFIED_BADLANDS_PLATEAU.getValue().toString(),
        BiomeKeys.MODIFIED_WOODED_BADLANDS_PLATEAU.getValue().toString(),
        BiomeKeys.WOODED_BADLANDS_PLATEAU.getValue().toString(),
        
        // Mountains
        BiomeKeys.MOUNTAINS.getValue().toString(),
        BiomeKeys.GRAVELLY_MOUNTAINS.getValue().toString(),
        BiomeKeys.MODIFIED_GRAVELLY_MOUNTAINS.getValue().toString(),
        
        // Giant Taigas
        BiomeKeys.GIANT_TREE_TAIGA.getValue().toString(),
        BiomeKeys.GIANT_TREE_TAIGA_HILLS.getValue().toString(),
        BiomeKeys.GIANT_SPRUCE_TAIGA.getValue().toString(),
        BiomeKeys.GIANT_SPRUCE_TAIGA_HILLS.getValue().toString(),
        
        // Savanna
        BiomeKeys.SHATTERED_SAVANNA.getValue().toString(),
        BiomeKeys.SHATTERED_SAVANNA_PLATEAU.getValue().toString(),
        
        // Swamp
        BiomeKeys.SWAMP.getValue().toString(),
        BiomeKeys.SWAMP_HILLS.getValue().toString(),
        
        // Nether
        BiomeKeys.NETHER_WASTES.getValue().toString(),
        BiomeKeys.WARPED_FOREST.getValue().toString(),
        BiomeKeys.CRIMSON_FOREST.getValue().toString(),
        BiomeKeys.BASALT_DELTAS.getValue().toString(),
        BiomeKeys.SOUL_SAND_VALLEY.getValue().toString()
    );
}
