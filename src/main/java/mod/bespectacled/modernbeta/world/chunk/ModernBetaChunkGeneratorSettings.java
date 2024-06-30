package mod.bespectacled.modernbeta.world.chunk;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules.SequenceRuleSource;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

import java.util.List;

public class ModernBetaChunkGeneratorSettings {
	@Deprecated
	public static final ResourceKey<NoiseGeneratorSettings> MODERN_BETA;
	public static final ResourceKey<NoiseGeneratorSettings> BETA;
	public static final ResourceKey<NoiseGeneratorSettings> ALPHA;
	public static final ResourceKey<NoiseGeneratorSettings> SKYLANDS;
	public static final ResourceKey<NoiseGeneratorSettings> INFDEV_611;
	public static final ResourceKey<NoiseGeneratorSettings> INFDEV_420;
	public static final ResourceKey<NoiseGeneratorSettings> INFDEV_415;
	public static final ResourceKey<NoiseGeneratorSettings> INFDEV_227;
	public static final ResourceKey<NoiseGeneratorSettings> INDEV;
	public static final ResourceKey<NoiseGeneratorSettings> CLASSIC_0_30;
	public static final ResourceKey<NoiseGeneratorSettings> PE;

	@SuppressWarnings("deprecation")
	public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> settingsRegisterable) {
		settingsRegisterable.register(MODERN_BETA, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.MODERN_BETA, 64, true));
		settingsRegisterable.register(BETA, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.BETA, 64, true));
		settingsRegisterable.register(ALPHA, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.ALPHA, 64, true));
		settingsRegisterable.register(SKYLANDS, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.SKYLANDS, 0, false));
		settingsRegisterable.register(INFDEV_611, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INFDEV_611, 64, true));
		settingsRegisterable.register(INFDEV_420, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INFDEV_420, 64, true));
		settingsRegisterable.register(INFDEV_415, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INFDEV_415, 64, true));
		settingsRegisterable.register(INFDEV_227, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INFDEV_227, 64, true));
		settingsRegisterable.register(INDEV, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.INDEV, 64, false));
		settingsRegisterable.register(CLASSIC_0_30, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.CLASSIC_0_30, 64, false));
		settingsRegisterable.register(PE, createGeneratorSettings(settingsRegisterable, ModernBetaShapeConfigs.PE, 64, true));
	}

	private static NoiseRouter createDensityFunctions(
			HolderGetter<DensityFunction> densityFunctionLookup,
			HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup
	) {
		Reference<NoiseParameters> aquiferBarrier = noiseParametersLookup.getOrThrow(Noises.AQUIFER_BARRIER);
		Reference<NoiseParameters> aquiferFloodedness = noiseParametersLookup.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_FLOODEDNESS);
		Reference<NoiseParameters> aquiferSpread = noiseParametersLookup.getOrThrow(Noises.AQUIFER_FLUID_LEVEL_SPREAD);
		Reference<NoiseParameters> aquiferLava = noiseParametersLookup.getOrThrow(Noises.AQUIFER_LAVA);

		DensityFunction functionAquiferBarrier = DensityFunctions.noise(aquiferBarrier, 0.5);
		DensityFunction functionAquiferFloodedness = DensityFunctions.noise(aquiferFloodedness, 0.67);
		DensityFunction functionAquiferSpread = DensityFunctions.noise(aquiferSpread, 0.7142857142857143);
		DensityFunction functionAquiferLava = DensityFunctions.noise(aquiferLava);

		return new NoiseRouter(
				functionAquiferBarrier,      // Barrier noise
				functionAquiferFloodedness,  // Fluid level floodedness noise
				functionAquiferSpread,       // Fluid level spread noise
				functionAquiferLava,         // Lava noise
				DensityFunctions.zero(), // Temperature
				DensityFunctions.zero(), // Vegetation
				DensityFunctions.zero(), // Continents
				DensityFunctions.zero(), // Erosion
				DensityFunctions.zero(), // Depth
				DensityFunctions.zero(), // Ridges
				DensityFunctions.zero(), // Initial Density
				DensityFunctions.zero(), // Final Density
				DensityFunctions.zero(), // Vein Toggle
				DensityFunctions.zero(), // Vein Ridged
				DensityFunctions.zero()  // Vein Gap
		);
	}

	private static NoiseGeneratorSettings createGeneratorSettings(
			BootstrapContext<NoiseGeneratorSettings> settingsRegisterable,
			NoiseSettings shapeConfig,
			int seaLevel,
			boolean useAquifers
	) {
		HolderGetter<DensityFunction> densityFunctionLookup = settingsRegisterable.lookup(Registries.DENSITY_FUNCTION);
		HolderGetter<NormalNoise.NoiseParameters> noiseParametersLookup = settingsRegisterable.lookup(Registries.NOISE);

		return new NoiseGeneratorSettings(
				shapeConfig,
				BlockStates.STONE,
				BlockStates.WATER,
				createDensityFunctions(densityFunctionLookup, noiseParametersLookup),
				new SequenceRuleSource(List.of()),
				List.of(),
				seaLevel,
				false,
				useAquifers,
				false,
				true
		);
	}

	static {
		MODERN_BETA = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBeta.createId(ModernBeta.MOD_ID));
		BETA = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.BETA.id));
		ALPHA = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.ALPHA.id));
		SKYLANDS = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.SKYLANDS.id));
		INFDEV_611 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_611.id));
		INFDEV_420 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_420.id));
		INFDEV_415 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_415.id));
		INFDEV_227 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INFDEV_227.id));
		INDEV = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.INDEV.id));
		CLASSIC_0_30 = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.id));
		PE = ResourceKey.create(Registries.NOISE_SETTINGS, ModernBeta.createId(ModernBetaBuiltInTypes.Chunk.PE.id));
	}
}
