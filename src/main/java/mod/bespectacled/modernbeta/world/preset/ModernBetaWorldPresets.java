package mod.bespectacled.modernbeta.world.preset;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsPreset;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGeneratorSettings;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;

import java.util.Map;

public class ModernBetaWorldPresets {
	public static final ResourceKey<WorldPreset> MODERN_BETA = keyOf(ModernBeta.createId(ModernBeta.MOD_ID));

	public static void bootstrap(BootstrapContext<WorldPreset> presetRegisterable) {
		HolderGetter<DimensionType> registryDimensionType = presetRegisterable.lookup(Registries.DIMENSION_TYPE);
		HolderGetter<NoiseGeneratorSettings> registrySettings = presetRegisterable.lookup(Registries.NOISE_SETTINGS);
		HolderGetter<Biome> registryBiome = presetRegisterable.lookup(Registries.BIOME);
		HolderGetter<MultiNoiseBiomeSourceParameterList> registryParameters = presetRegisterable.lookup(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST);

		LevelStem overworld = createOverworldOptions(registryDimensionType, registrySettings, registryBiome);
		LevelStem nether = createNetherOptions(registryDimensionType, registrySettings, registryParameters);
		LevelStem end = createEndOptions(registryDimensionType, registrySettings, registryBiome);

		presetRegisterable.register(
				MODERN_BETA,
				new WorldPreset(Map.of(LevelStem.OVERWORLD, overworld, LevelStem.NETHER, nether, LevelStem.END, end))
		);
	}

	private static LevelStem createOverworldOptions(
			HolderGetter<DimensionType> registryDimensionType,
			HolderGetter<NoiseGeneratorSettings> registrySettings,
			HolderGetter<Biome> registryBiome
	) {
		Holder.Reference<DimensionType> dimensionType = registryDimensionType.getOrThrow(BuiltinDimensionTypes.OVERWORLD);
		Holder.Reference<NoiseGeneratorSettings> settings = registrySettings.getOrThrow(ModernBetaChunkGeneratorSettings.BETA);

		ModernBetaSettingsPreset defaultPreset = ModernBetaRegistries.SETTINGS_PRESET.get(ModernBetaBuiltInTypes.Chunk.BETA.id);

		return new LevelStem(
				dimensionType,
				new ModernBetaChunkGenerator(
						new ModernBetaBiomeSource(
								registryBiome,
								defaultPreset.settingsBiome().toCompound(),
								defaultPreset.settingsCaveBiome().toCompound()
						),
						settings,
						defaultPreset.settingsChunk().toCompound()
				)
		);
	}

	private static LevelStem createNetherOptions(
			HolderGetter<DimensionType> registryDimensionType,
			HolderGetter<NoiseGeneratorSettings> registrySettings,
			HolderGetter<MultiNoiseBiomeSourceParameterList> registryParameters
	) {
		Holder.Reference<DimensionType> dimensionType = registryDimensionType.getOrThrow(BuiltinDimensionTypes.NETHER);
		Holder.Reference<NoiseGeneratorSettings> settings = registrySettings.getOrThrow(NoiseGeneratorSettings.NETHER);
		Holder.Reference<MultiNoiseBiomeSourceParameterList> parameters = registryParameters.getOrThrow(MultiNoiseBiomeSourceParameterLists.NETHER);

		return new LevelStem(dimensionType, new NoiseBasedChunkGenerator(MultiNoiseBiomeSource.createFromPreset(parameters), settings));
	}

	private static LevelStem createEndOptions(
			HolderGetter<DimensionType> registryDimensionType,
			HolderGetter<NoiseGeneratorSettings> registrySettings,
			HolderGetter<Biome> registryBiome
	) {
		Holder.Reference<DimensionType> dimensionType = registryDimensionType.getOrThrow(BuiltinDimensionTypes.END);
		Holder.Reference<NoiseGeneratorSettings> settings = registrySettings.getOrThrow(NoiseGeneratorSettings.END);

		return new LevelStem(dimensionType, new NoiseBasedChunkGenerator(TheEndBiomeSource.create(registryBiome), settings));
	}

	private static ResourceKey<WorldPreset> keyOf(ResourceLocation id) {
		return ResourceKey.create(Registries.WORLD_PRESET, id);
	}
}
