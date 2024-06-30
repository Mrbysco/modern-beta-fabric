package mod.bespectacled.modernbeta.world.structure;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.data.ModernBetaBiomeTagProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.structures.StrongholdStructure;

import java.util.Map;

public class ModernBetaStructures {
	public static final ResourceKey<Structure> INDEV_STRONGHOLD = ResourceKey.create(Registries.STRUCTURE, ModernBeta.createId("indev_stronghold"));

	public static void bootstrap(BootstrapContext<Structure> structureRegisterable) {
		HolderGetter<Biome> registryBiome = structureRegisterable.lookup(Registries.BIOME);

		structureRegisterable.register(
				INDEV_STRONGHOLD,
				new StrongholdStructure(createConfig(registryBiome.getOrThrow(ModernBetaBiomeTagProvider.INDEV_STRONGHOLD_HAS_STRUCTURE), TerrainAdjustment.BURY))
		);
	}

	private static Structure.StructureSettings createConfig(HolderSet<Biome> biomes, TerrainAdjustment terrainAdaptation) {
		return createConfig(biomes, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, terrainAdaptation);
	}

	private static Structure.StructureSettings createConfig(HolderSet<Biome> biomes, Map<MobCategory, StructureSpawnOverride> spawns, GenerationStep.Decoration featureStep, TerrainAdjustment terrainAdaptation) {
		return new Structure.StructureSettings(biomes, spawns, featureStep, terrainAdaptation);
	}
}
