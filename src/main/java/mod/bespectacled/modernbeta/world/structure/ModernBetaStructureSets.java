package mod.bespectacled.modernbeta.world.structure;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;

public class ModernBetaStructureSets {
	public static final ResourceKey<StructureSet> INDEV_STRONGHOLDS = keyOf(ModernBeta.createId("indev_strongholds"));

	public static void bootstrap(BootstrapContext<StructureSet> structureSetRegisterable) {
		HolderGetter<Structure> registryStructure = structureSetRegisterable.lookup(Registries.STRUCTURE);
		HolderGetter<Biome> registryBiome = structureSetRegisterable.lookup(Registries.BIOME);

		structureSetRegisterable.register(
				INDEV_STRONGHOLDS,
				new StructureSet(
						registryStructure.getOrThrow(ModernBetaStructures.INDEV_STRONGHOLD),
						new ConcentricRingsStructurePlacement(0, 0, 1, registryBiome.getOrThrow(BiomeTags.STRONGHOLD_BIASED_TO))
				)
		);
	}

	private static ResourceKey<StructureSet> keyOf(ResourceLocation id) {
		return ResourceKey.create(Registries.STRUCTURE_SET, id);
	}
}
