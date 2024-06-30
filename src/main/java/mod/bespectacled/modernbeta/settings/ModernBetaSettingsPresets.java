package mod.bespectacled.modernbeta.settings;

import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import mod.bespectacled.modernbeta.world.biome.provider.climate.ClimateMapping;
import mod.bespectacled.modernbeta.world.biome.voronoi.VoronoiPointBiome;
import mod.bespectacled.modernbeta.world.biome.voronoi.VoronoiPointCaveBiome;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevTheme;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevType;
import mod.bespectacled.modernbeta.world.chunk.provider.island.IslandShape;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.biome.Biomes;

import java.util.List;

public class ModernBetaSettingsPresets {
	public static final ModernBetaSettingsPreset PRESET_BETA = presetBeta();
	public static final ModernBetaSettingsPreset PRESET_ALPHA = presetAlpha();
	public static final ModernBetaSettingsPreset PRESET_SKYLANDS = presetSkylands();
	public static final ModernBetaSettingsPreset PRESET_INFDEV_415 = presetInfdev415();
	public static final ModernBetaSettingsPreset PRESET_INFDEV_420 = presetInfdev420();
	public static final ModernBetaSettingsPreset PRESET_INFDEV_611 = presetInfdev611();
	public static final ModernBetaSettingsPreset PRESET_INFDEV_227 = presetInfdev227();
	public static final ModernBetaSettingsPreset PRESET_INDEV = presetIndev();
	public static final ModernBetaSettingsPreset PRESET_CLASSIC = presetClassic();
	public static final ModernBetaSettingsPreset PRESET_PE = presetPE();

	public static final ModernBetaSettingsPreset PRESET_BETA_SKYLANDS = presetBetaSkylands();
	public static final ModernBetaSettingsPreset PRESET_BETA_ISLES = presetBetaIsles();
	public static final ModernBetaSettingsPreset PRESET_BETA_ISLE_LAND = presetBetaIsleLand();
	public static final ModernBetaSettingsPreset PRESET_BETA_CAVE_DELIGHT = presetBetaCaveDelight();
	public static final ModernBetaSettingsPreset PRESET_BETA_CAVE_CHAOS = presetBetaCaveChaos();
	public static final ModernBetaSettingsPreset PRESET_BETA_LARGE_BIOMES = presetBetaLargeBiomes();
	public static final ModernBetaSettingsPreset PRESET_BETA_XBOX_LEGACY = presetBetaXboxLegacy();
	public static final ModernBetaSettingsPreset PRESET_BETA_SURVIVAL_ISLAND = presetBetaSurvivalIsland();
	public static final ModernBetaSettingsPreset PRESET_BETA_VANILLA = presetBetaVanilla();
	public static final ModernBetaSettingsPreset PRESET_ALPHA_WINTER = presetAlphaWinter();
	public static final ModernBetaSettingsPreset PRESET_INDEV_PARADISE = presetIndevParadise();
	public static final ModernBetaSettingsPreset PRESET_INDEV_WOODS = presetIndevWoods();
	public static final ModernBetaSettingsPreset PRESET_INDEV_HELL = presetIndevHell();

	private static ModernBetaSettingsPreset presetBeta() {
		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

		settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.BETA.id;
		settingsChunk.useDeepslate = true;
		settingsChunk.deepslateMinY = 0;
		settingsChunk.deepslateMaxY = 8;
		settingsChunk.deepslateBlock = "minecraft:deepslate";
		settingsChunk.noiseCoordinateScale = 684.412f;
		settingsChunk.noiseHeightScale = 684.412f;
		settingsChunk.noiseUpperLimitScale = 512f;
		settingsChunk.noiseLowerLimitScale = 512f;
		settingsChunk.noiseDepthNoiseScaleX = 200;
		settingsChunk.noiseDepthNoiseScaleZ = 200;
		settingsChunk.noiseMainNoiseScaleX = 80f;
		settingsChunk.noiseMainNoiseScaleY = 160f;
		settingsChunk.noiseMainNoiseScaleZ = 80f;
		settingsChunk.noiseBaseSize = 8.5f;
		settingsChunk.noiseStretchY = 12.0f;
		settingsChunk.noiseTopSlideTarget = -10;
		settingsChunk.noiseTopSlideSize = 3;
		settingsChunk.noiseTopSlideOffset = 0;
		settingsChunk.noiseBottomSlideTarget = 15;
		settingsChunk.noiseBottomSlideSize = 3;
		settingsChunk.noiseBottomSlideOffset = 0;

		settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.BETA.id;
		settingsBiome.climateTempNoiseScale = 0.025f;
		settingsBiome.climateRainNoiseScale = 0.05f;
		settingsBiome.climateDetailNoiseScale = 0.25f;
		settingsBiome.climateMappings = ModernBetaSettingsBiome.Builder.createClimateMapping(
				new ClimateMapping(
						ModernBetaBiomes.BETA_DESERT.location().toString(),
						ModernBetaBiomes.BETA_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.BETA_FOREST.location().toString(),
						ModernBetaBiomes.BETA_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.BETA_TUNDRA.location().toString(),
						ModernBetaBiomes.BETA_FROZEN_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.BETA_PLAINS.location().toString(),
						ModernBetaBiomes.BETA_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.BETA_RAINFOREST.location().toString(),
						ModernBetaBiomes.BETA_WARM_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.BETA_SAVANNA.location().toString(),
						ModernBetaBiomes.BETA_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.BETA_SHRUBLAND.location().toString(),
						ModernBetaBiomes.BETA_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.BETA_SEASONAL_FOREST.location().toString(),
						ModernBetaBiomes.BETA_LUKEWARM_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.BETA_SWAMPLAND.location().toString(),
						ModernBetaBiomes.BETA_COLD_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.BETA_TAIGA.location().toString(),
						ModernBetaBiomes.BETA_FROZEN_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.BETA_TUNDRA.location().toString(),
						ModernBetaBiomes.BETA_FROZEN_OCEAN.location().toString()
				)
		);

		settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.VORONOI.id;
		settingsCaveBiome.voronoiHorizontalNoiseScale = 32.0f;
		settingsCaveBiome.voronoiVerticalNoiseScale = 16.0f;
		settingsCaveBiome.voronoiDepthMinY = -64;
		settingsCaveBiome.voronoiDepthMaxY = 64;
		settingsCaveBiome.voronoiPoints = List.of(
				new VoronoiPointCaveBiome("", 0.0, 0.5, 0.75),
				new VoronoiPointCaveBiome("minecraft:lush_caves", 0.1, 0.5, 0.75),
				new VoronoiPointCaveBiome("", 0.5, 0.5, 0.75),
				new VoronoiPointCaveBiome("minecraft:dripstone_caves", 0.9, 0.5, 0.75),
				new VoronoiPointCaveBiome("", 1.0, 0.5, 0.75),

				new VoronoiPointCaveBiome("", 0.0, 0.5, 0.25),
				new VoronoiPointCaveBiome("minecraft:lush_caves", 0.2, 0.5, 0.25),
				new VoronoiPointCaveBiome("", 0.4, 0.5, 0.25),
				new VoronoiPointCaveBiome("minecraft:deep_dark", 0.5, 0.5, 0.25),
				new VoronoiPointCaveBiome("", 0.6, 0.5, 0.25),
				new VoronoiPointCaveBiome("minecraft:dripstone_caves", 0.8, 0.5, 0.25),
				new VoronoiPointCaveBiome("", 1.0, 0.5, 0.25)
		);

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetAlpha() {
		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

		settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.ALPHA.id;
		settingsChunk.useDeepslate = false;
		settingsChunk.noiseCoordinateScale = 684.412f;
		settingsChunk.noiseHeightScale = 684.412f;
		settingsChunk.noiseUpperLimitScale = 512f;
		settingsChunk.noiseLowerLimitScale = 512f;
		settingsChunk.noiseDepthNoiseScaleX = 100;
		settingsChunk.noiseDepthNoiseScaleZ = 100;
		settingsChunk.noiseMainNoiseScaleX = 80f;
		settingsChunk.noiseMainNoiseScaleY = 160f;
		settingsChunk.noiseMainNoiseScaleZ = 80f;
		settingsChunk.noiseBaseSize = 8.5f;
		settingsChunk.noiseStretchY = 12.0f;
		settingsChunk.noiseTopSlideTarget = -10;
		settingsChunk.noiseTopSlideSize = 3;
		settingsChunk.noiseTopSlideOffset = 0;
		settingsChunk.noiseBottomSlideTarget = 15;
		settingsChunk.noiseBottomSlideSize = 3;
		settingsChunk.noiseBottomSlideOffset = 0;

		settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
		settingsBiome.singleBiome = ModernBetaBiomes.ALPHA.location().toString();

		settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetSkylands() {
		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

		settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.SKYLANDS.id;
		settingsChunk.useDeepslate = false;
		settingsChunk.noiseCoordinateScale = 1368.824f;
		settingsChunk.noiseHeightScale = 684.412f;
		settingsChunk.noiseUpperLimitScale = 512f;
		settingsChunk.noiseLowerLimitScale = 512f;
		settingsChunk.noiseDepthNoiseScaleX = 100;
		settingsChunk.noiseDepthNoiseScaleZ = 100;
		settingsChunk.noiseMainNoiseScaleX = 80f;
		settingsChunk.noiseMainNoiseScaleY = 160f;
		settingsChunk.noiseMainNoiseScaleZ = 80f;
		settingsChunk.noiseBaseSize = 8.5f;
		settingsChunk.noiseStretchY = 12.0f;
		settingsChunk.noiseTopSlideTarget = -30;
		settingsChunk.noiseTopSlideSize = 31;
		settingsChunk.noiseTopSlideOffset = 0;
		settingsChunk.noiseBottomSlideTarget = -30;
		settingsChunk.noiseBottomSlideSize = 7;
		settingsChunk.noiseBottomSlideOffset = 1;

		settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
		settingsBiome.singleBiome = ModernBetaBiomes.BETA_SKY.location().toString();
		settingsBiome.useOceanBiomes = false;

		settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetInfdev415() {
		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

		settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_415.id;
		settingsChunk.useDeepslate = false;
		settingsChunk.useCaves = false;
		settingsChunk.noiseCoordinateScale = 684.412f;
		settingsChunk.noiseHeightScale = 984.412f;
		settingsChunk.noiseUpperLimitScale = 512f;
		settingsChunk.noiseLowerLimitScale = 512f;
		settingsChunk.noiseMainNoiseScaleX = 80f;
		settingsChunk.noiseMainNoiseScaleY = 400f;
		settingsChunk.noiseMainNoiseScaleZ = 80f;
		settingsChunk.noiseTopSlideTarget = 0;
		settingsChunk.noiseTopSlideSize = 0;
		settingsChunk.noiseTopSlideOffset = 0;
		settingsChunk.noiseBottomSlideTarget = 0;
		settingsChunk.noiseBottomSlideSize = 0;
		settingsChunk.noiseBottomSlideOffset = 0;

		settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
		settingsBiome.singleBiome = ModernBetaBiomes.INFDEV_415.location().toString();

		settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetInfdev420() {
		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

		settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_420.id;
		settingsChunk.useDeepslate = false;
		settingsChunk.noiseCoordinateScale = 684.412f;
		settingsChunk.noiseHeightScale = 684.412f;
		settingsChunk.noiseUpperLimitScale = 512f;
		settingsChunk.noiseLowerLimitScale = 512f;
		settingsChunk.noiseMainNoiseScaleX = 80f;
		settingsChunk.noiseMainNoiseScaleY = 160f;
		settingsChunk.noiseMainNoiseScaleZ = 80f;
		settingsChunk.noiseBaseSize = 8.5f;
		settingsChunk.noiseStretchY = 12.0f;
		settingsChunk.noiseTopSlideTarget = 0;
		settingsChunk.noiseTopSlideSize = 0;
		settingsChunk.noiseTopSlideOffset = 0;
		settingsChunk.noiseBottomSlideTarget = 0;
		settingsChunk.noiseBottomSlideSize = 0;
		settingsChunk.noiseBottomSlideOffset = 0;

		settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
		settingsBiome.singleBiome = ModernBetaBiomes.INFDEV_420.location().toString();

		settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetInfdev611() {
		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

		settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_611.id;
		settingsChunk.useDeepslate = false;
		settingsChunk.noiseCoordinateScale = 684.412f;
		settingsChunk.noiseHeightScale = 684.412f;
		settingsChunk.noiseUpperLimitScale = 512f;
		settingsChunk.noiseLowerLimitScale = 512f;
		settingsChunk.noiseDepthNoiseScaleX = 100;
		settingsChunk.noiseDepthNoiseScaleZ = 100;
		settingsChunk.noiseMainNoiseScaleX = 80f;
		settingsChunk.noiseMainNoiseScaleY = 160f;
		settingsChunk.noiseMainNoiseScaleZ = 80f;
		settingsChunk.noiseBaseSize = 8.5f;
		settingsChunk.noiseStretchY = 12.0f;
		settingsChunk.noiseTopSlideTarget = -10;
		settingsChunk.noiseTopSlideSize = 3;
		settingsChunk.noiseTopSlideOffset = 0;
		settingsChunk.noiseBottomSlideTarget = 15;
		settingsChunk.noiseBottomSlideSize = 3;
		settingsChunk.noiseBottomSlideOffset = 0;

		settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
		settingsBiome.singleBiome = ModernBetaBiomes.INFDEV_611.location().toString();

		settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetInfdev227() {
		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

		settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INFDEV_227.id;
		settingsChunk.useDeepslate = false;
		settingsChunk.useCaves = false;
		settingsChunk.infdevUsePyramid = true;
		settingsChunk.infdevUseWall = true;

		settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
		settingsBiome.singleBiome = ModernBetaBiomes.INFDEV_227.location().toString();

		settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetIndev() {
		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

		settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.INDEV.id;
		settingsChunk.useDeepslate = false;
		settingsChunk.useCaves = false;
		settingsChunk.indevLevelTheme = IndevTheme.NORMAL.getId();
		settingsChunk.indevLevelType = IndevType.ISLAND.getId();
		settingsChunk.indevLevelWidth = 256;
		settingsChunk.indevLevelLength = 256;
		settingsChunk.indevLevelHeight = 128;
		settingsChunk.indevCaveRadius = 1.0f;
		settingsChunk.indevUseCaves = true;

		settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
		settingsBiome.singleBiome = ModernBetaBiomes.INDEV_NORMAL.location().toString();

		settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetClassic() {
		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

		settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.CLASSIC_0_30.id;
		settingsChunk.useDeepslate = false;
		settingsChunk.useCaves = false;
		settingsChunk.indevLevelWidth = 256;
		settingsChunk.indevLevelLength = 256;
		settingsChunk.indevLevelHeight = 128;
		settingsChunk.indevCaveRadius = 1.0f;
		settingsChunk.indevUseCaves = true;

		settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.SINGLE.id;
		settingsBiome.singleBiome = ModernBetaBiomes.INDEV_NORMAL.location().toString();

		settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetPE() {
		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder();
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder();
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder();

		settingsChunk.chunkProvider = ModernBetaBuiltInTypes.Chunk.PE.id;
		settingsChunk.useDeepslate = false;
		settingsChunk.useCaves = false;
		settingsChunk.noiseCoordinateScale = 684.412f;
		settingsChunk.noiseHeightScale = 684.412f;
		settingsChunk.noiseUpperLimitScale = 512f;
		settingsChunk.noiseLowerLimitScale = 512f;
		settingsChunk.noiseDepthNoiseScaleX = 200;
		settingsChunk.noiseDepthNoiseScaleZ = 200;
		settingsChunk.noiseMainNoiseScaleX = 80f;
		settingsChunk.noiseMainNoiseScaleY = 160f;
		settingsChunk.noiseMainNoiseScaleZ = 80f;
		settingsChunk.noiseBaseSize = 8.5f;
		settingsChunk.noiseStretchY = 12.0f;
		settingsChunk.noiseTopSlideTarget = -10;
		settingsChunk.noiseTopSlideSize = 3;
		settingsChunk.noiseTopSlideOffset = 0;
		settingsChunk.noiseBottomSlideTarget = 15;
		settingsChunk.noiseBottomSlideSize = 3;
		settingsChunk.noiseBottomSlideOffset = 0;

		settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.PE.id;
		settingsBiome.climateTempNoiseScale = 0.025f;
		settingsBiome.climateRainNoiseScale = 0.05f;
		settingsBiome.climateDetailNoiseScale = 0.25f;
		settingsBiome.climateMappings = ModernBetaSettingsBiome.Builder.createClimateMapping(
				new ClimateMapping(
						ModernBetaBiomes.PE_DESERT.location().toString(),
						ModernBetaBiomes.PE_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.PE_FOREST.location().toString(),
						ModernBetaBiomes.PE_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.PE_TUNDRA.location().toString(),
						ModernBetaBiomes.PE_FROZEN_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.PE_PLAINS.location().toString(),
						ModernBetaBiomes.PE_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.PE_RAINFOREST.location().toString(),
						ModernBetaBiomes.PE_WARM_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.PE_SAVANNA.location().toString(),
						ModernBetaBiomes.PE_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.PE_SHRUBLAND.location().toString(),
						ModernBetaBiomes.PE_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.PE_SEASONAL_FOREST.location().toString(),
						ModernBetaBiomes.PE_LUKEWARM_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.PE_SWAMPLAND.location().toString(),
						ModernBetaBiomes.PE_COLD_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.PE_TAIGA.location().toString(),
						ModernBetaBiomes.PE_FROZEN_OCEAN.location().toString()
				),
				new ClimateMapping(
						ModernBetaBiomes.PE_TUNDRA.location().toString(),
						ModernBetaBiomes.PE_FROZEN_OCEAN.location().toString()
				)
		);

		settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.NONE.id;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetBetaSkylands() {
		ModernBetaSettingsPreset initial = presetSkylands();

		CompoundTag compoundChunk = initial.settingsChunk().toCompound();
		CompoundTag compoundBiome = initial.settingsBiome().toCompound();
		CompoundTag compoundCaveBiome = initial.settingsCaveBiome().toCompound();

		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);

		settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.BETA.id;
		settingsBiome.useOceanBiomes = false;

		settingsCaveBiome.biomeProvider = ModernBetaBuiltInTypes.CaveBiome.VORONOI.id;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetBetaIsles() {
		ModernBetaSettingsPreset initial = presetBeta();

		CompoundTag compoundChunk = initial.settingsChunk().toCompound();
		CompoundTag compoundBiome = initial.settingsBiome().toCompound();
		CompoundTag compoundCaveBiome = initial.settingsCaveBiome().toCompound();

		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);

		settingsChunk.islesUseIslands = true;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetBetaIsleLand() {
		ModernBetaSettingsPreset initial = presetBeta();

		CompoundTag compoundChunk = initial.settingsChunk().toCompound();
		CompoundTag compoundBiome = initial.settingsBiome().toCompound();
		CompoundTag compoundCaveBiome = initial.settingsCaveBiome().toCompound();

		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);

		settingsChunk.noiseCoordinateScale = 3000.0f;
		settingsChunk.noiseHeightScale = 6000.0f;
		settingsChunk.noiseStretchY = 10.0f;
		settingsChunk.noiseUpperLimitScale = 250.0f;
		settingsChunk.noiseLowerLimitScale = 512.0f;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}


	private static ModernBetaSettingsPreset presetBetaCaveDelight() {
		ModernBetaSettingsPreset initial = presetBeta();

		CompoundTag compoundChunk = initial.settingsChunk().toCompound();
		CompoundTag compoundBiome = initial.settingsBiome().toCompound();
		CompoundTag compoundCaveBiome = initial.settingsCaveBiome().toCompound();

		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);

		settingsChunk.noiseMainNoiseScaleX = 5000.0f;
		settingsChunk.noiseMainNoiseScaleY = 1000.0f;
		settingsChunk.noiseMainNoiseScaleZ = 5000.0f;
		settingsChunk.noiseStretchY = 5.0f;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetBetaCaveChaos() {
		ModernBetaSettingsPreset initial = presetBeta();

		CompoundTag compoundChunk = initial.settingsChunk().toCompound();
		CompoundTag compoundBiome = initial.settingsBiome().toCompound();
		CompoundTag compoundCaveBiome = initial.settingsCaveBiome().toCompound();

		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);

		settingsChunk.noiseUpperLimitScale = 2.0f;
		settingsChunk.noiseLowerLimitScale = 64.0f;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetBetaLargeBiomes() {
		ModernBetaSettingsPreset initial = presetBeta();

		CompoundTag compoundChunk = initial.settingsChunk().toCompound();
		CompoundTag compoundBiome = initial.settingsBiome().toCompound();
		CompoundTag compoundCaveBiome = initial.settingsCaveBiome().toCompound();

		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);

		settingsBiome.climateTempNoiseScale = 0.025f / 4.0f;
		settingsBiome.climateRainNoiseScale = 0.05f / 4.0f;
		settingsBiome.climateDetailNoiseScale = 0.25f / 2.0f;

		settingsCaveBiome.voronoiHorizontalNoiseScale = 32.0f * 4.0f;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetBetaXboxLegacy() {
		ModernBetaSettingsPreset initial = presetBeta();

		CompoundTag compoundChunk = initial.settingsChunk().toCompound();
		CompoundTag compoundBiome = initial.settingsBiome().toCompound();
		CompoundTag compoundCaveBiome = initial.settingsCaveBiome().toCompound();

		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);

		settingsChunk.islesUseIslands = true;
		settingsChunk.islesUseOuterIslands = false;
		settingsChunk.islesCenterIslandShape = IslandShape.SQUARE.getId();
		settingsChunk.islesCenterIslandRadius = 25;
		settingsChunk.islesCenterIslandFalloffDistance = 2;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetBetaSurvivalIsland() {
		ModernBetaSettingsPreset initial = presetBeta();

		CompoundTag compoundChunk = initial.settingsChunk().toCompound();
		CompoundTag compoundBiome = initial.settingsBiome().toCompound();
		CompoundTag compoundCaveBiome = initial.settingsCaveBiome().toCompound();

		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);

		settingsChunk.islesUseIslands = true;
		settingsChunk.islesUseOuterIslands = false;
		settingsChunk.islesCenterIslandRadius = 1;

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetBetaVanilla() {
		ModernBetaSettingsPreset initial = presetBeta();

		CompoundTag compoundChunk = initial.settingsChunk().toCompound();
		CompoundTag compoundBiome = initial.settingsBiome().toCompound();
		CompoundTag compoundCaveBiome = initial.settingsCaveBiome().toCompound();

		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);

		settingsBiome.biomeProvider = ModernBetaBuiltInTypes.Biome.VORONOI.id;
		settingsBiome.climateTempNoiseScale = 0.025f / 3.0f;
		settingsBiome.climateRainNoiseScale = 0.05f / 3.0f;
		settingsBiome.climateDetailNoiseScale = 0.25f / 1.5f;
		settingsBiome.voronoiPoints = List.of(
				// Standard Biomes

				new VoronoiPointBiome(
						Biomes.DESERT.location().toString(),
						Biomes.LUKEWARM_OCEAN.location().toString(),
						Biomes.DEEP_LUKEWARM_OCEAN.location().toString(),
						0.9, 0.1, 0.5
				),
				new VoronoiPointBiome(
						Biomes.PLAINS.location().toString(),
						Biomes.LUKEWARM_OCEAN.location().toString(),
						Biomes.DEEP_LUKEWARM_OCEAN.location().toString(),
						0.9, 0.3, 0.5
				),
				new VoronoiPointBiome(
						Biomes.FOREST.location().toString(),
						Biomes.LUKEWARM_OCEAN.location().toString(),
						Biomes.DEEP_LUKEWARM_OCEAN.location().toString(),
						0.9, 0.5, 0.5
				),
				new VoronoiPointBiome(
						Biomes.FOREST.location().toString(),
						Biomes.WARM_OCEAN.location().toString(),
						Biomes.WARM_OCEAN.location().toString(),
						0.9, 0.7, 0.5
				),
				new VoronoiPointBiome(
						Biomes.JUNGLE.location().toString(),
						Biomes.WARM_OCEAN.location().toString(),
						Biomes.WARM_OCEAN.location().toString(),
						0.9, 0.9, 0.5
				),

				new VoronoiPointBiome(
						Biomes.SAVANNA.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.1, 0.5
				),
				new VoronoiPointBiome(
						Biomes.PLAINS.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.3, 0.5
				),
				new VoronoiPointBiome(
						Biomes.FOREST.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.5, 0.5
				),
				new VoronoiPointBiome(
						Biomes.FOREST.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.7, 0.5
				),
				new VoronoiPointBiome(
						Biomes.FOREST.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.9, 0.5
				),

				new VoronoiPointBiome(
						Biomes.PLAINS.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.1, 0.5
				),
				new VoronoiPointBiome(
						Biomes.PLAINS.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.3, 0.5
				),
				new VoronoiPointBiome(
						Biomes.BIRCH_FOREST.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.5, 0.5
				),
				new VoronoiPointBiome(
						Biomes.BIRCH_FOREST.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.7, 0.5
				),
				new VoronoiPointBiome(
						Biomes.SWAMP.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.9, 0.5
				),

				new VoronoiPointBiome(
						Biomes.SNOWY_PLAINS.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.3, 0.1, 0.5
				),
				new VoronoiPointBiome(
						Biomes.TAIGA.location().toString(),
						Biomes.COLD_OCEAN.location().toString(),
						Biomes.DEEP_COLD_OCEAN.location().toString(),
						0.3, 0.3, 0.5
				),
				new VoronoiPointBiome(
						Biomes.TAIGA.location().toString(),
						Biomes.COLD_OCEAN.location().toString(),
						Biomes.DEEP_COLD_OCEAN.location().toString(),
						0.3, 0.5, 0.5
				),
				new VoronoiPointBiome(
						Biomes.SNOWY_TAIGA.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.3, 0.7, 0.5
				),
				new VoronoiPointBiome(
						Biomes.SNOWY_TAIGA.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.3, 0.9, 0.5
				),

				new VoronoiPointBiome(
						Biomes.SNOWY_PLAINS.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.1, 0.5
				),
				new VoronoiPointBiome(
						Biomes.SNOWY_PLAINS.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.3, 0.5
				),
				new VoronoiPointBiome(
						Biomes.SNOWY_PLAINS.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.5, 0.5
				),
				new VoronoiPointBiome(
						Biomes.SNOWY_PLAINS.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.7, 0.5
				),
				new VoronoiPointBiome(
						Biomes.SNOWY_PLAINS.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.9, 0.5
				),

				// Mutated Biomes

				new VoronoiPointBiome(
						Biomes.DESERT.location().toString(),
						Biomes.LUKEWARM_OCEAN.location().toString(),
						Biomes.DEEP_LUKEWARM_OCEAN.location().toString(),
						0.9, 0.1, 0.2
				),
				new VoronoiPointBiome(
						Biomes.SUNFLOWER_PLAINS.location().toString(),
						Biomes.LUKEWARM_OCEAN.location().toString(),
						Biomes.DEEP_LUKEWARM_OCEAN.location().toString(),
						0.9, 0.3, 0.2
				),
				new VoronoiPointBiome(
						Biomes.DARK_FOREST.location().toString(),
						Biomes.LUKEWARM_OCEAN.location().toString(),
						Biomes.DEEP_LUKEWARM_OCEAN.location().toString(),
						0.9, 0.5, 0.2
				),
				new VoronoiPointBiome(
						Biomes.DARK_FOREST.location().toString(),
						Biomes.WARM_OCEAN.location().toString(),
						Biomes.WARM_OCEAN.location().toString(),
						0.9, 0.7, 0.2
				),
				new VoronoiPointBiome(
						Biomes.BAMBOO_JUNGLE.location().toString(),
						Biomes.WARM_OCEAN.location().toString(),
						Biomes.WARM_OCEAN.location().toString(),
						0.9, 0.9, 0.2
				),

				new VoronoiPointBiome(
						Biomes.SAVANNA.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.1, 0.2
				),
				new VoronoiPointBiome(
						Biomes.MEADOW.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.3, 0.2
				),
				new VoronoiPointBiome(
						Biomes.FLOWER_FOREST.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.5, 0.2
				),
				new VoronoiPointBiome(
						Biomes.FLOWER_FOREST.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.7, 0.2
				),
				new VoronoiPointBiome(
						Biomes.FLOWER_FOREST.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.9, 0.2
				),

				new VoronoiPointBiome(
						Biomes.MEADOW.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.1, 0.2
				),
				new VoronoiPointBiome(
						Biomes.MEADOW.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.3, 0.2
				),
				new VoronoiPointBiome(
						Biomes.CHERRY_GROVE.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.5, 0.2
				),
				new VoronoiPointBiome(
						Biomes.CHERRY_GROVE.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.7, 0.2
				),
				new VoronoiPointBiome(
						Biomes.MANGROVE_SWAMP.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.9, 0.2
				),

				new VoronoiPointBiome(
						Biomes.SNOWY_PLAINS.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.3, 0.1, 0.2
				),
				new VoronoiPointBiome(
						Biomes.OLD_GROWTH_PINE_TAIGA.location().toString(),
						Biomes.COLD_OCEAN.location().toString(),
						Biomes.DEEP_COLD_OCEAN.location().toString(),
						0.3, 0.3, 0.2
				),
				new VoronoiPointBiome(
						Biomes.OLD_GROWTH_PINE_TAIGA.location().toString(),
						Biomes.COLD_OCEAN.location().toString(),
						Biomes.DEEP_COLD_OCEAN.location().toString(),
						0.3, 0.5, 0.2
				),
				new VoronoiPointBiome(
						Biomes.GROVE.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.3, 0.7, 0.2
				),
				new VoronoiPointBiome(
						Biomes.GROVE.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.3, 0.9, 0.2
				),

				new VoronoiPointBiome(
						Biomes.SNOWY_PLAINS.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.1, 0.2
				),
				new VoronoiPointBiome(
						Biomes.SNOWY_PLAINS.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.3, 0.2
				),
				new VoronoiPointBiome(
						Biomes.SNOWY_PLAINS.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.5, 0.2
				),
				new VoronoiPointBiome(
						Biomes.SNOWY_SLOPES.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.7, 0.2
				),
				new VoronoiPointBiome(
						Biomes.SNOWY_SLOPES.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.9, 0.2
				),

				// Mutated Biomes 2

				new VoronoiPointBiome(
						Biomes.BADLANDS.location().toString(),
						Biomes.LUKEWARM_OCEAN.location().toString(),
						Biomes.DEEP_LUKEWARM_OCEAN.location().toString(),
						0.9, 0.1, 0.8
				),
				new VoronoiPointBiome(
						Biomes.PLAINS.location().toString(),
						Biomes.LUKEWARM_OCEAN.location().toString(),
						Biomes.DEEP_LUKEWARM_OCEAN.location().toString(),
						0.9, 0.3, 0.8
				),
				new VoronoiPointBiome(
						Biomes.SPARSE_JUNGLE.location().toString(),
						Biomes.LUKEWARM_OCEAN.location().toString(),
						Biomes.DEEP_LUKEWARM_OCEAN.location().toString(),
						0.9, 0.5, 0.8
				),
				new VoronoiPointBiome(
						Biomes.SPARSE_JUNGLE.location().toString(),
						Biomes.WARM_OCEAN.location().toString(),
						Biomes.WARM_OCEAN.location().toString(),
						0.9, 0.7, 0.8
				),
				new VoronoiPointBiome(
						Biomes.MUSHROOM_FIELDS.location().toString(),
						Biomes.WARM_OCEAN.location().toString(),
						Biomes.WARM_OCEAN.location().toString(),
						0.9, 0.9, 0.8
				),

				new VoronoiPointBiome(
						Biomes.SAVANNA.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.1, 0.8
				),
				new VoronoiPointBiome(
						Biomes.PLAINS.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.3, 0.8
				),
				new VoronoiPointBiome(
						Biomes.DARK_FOREST.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.5, 0.8
				),
				new VoronoiPointBiome(
						Biomes.DARK_FOREST.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.7, 0.8
				),
				new VoronoiPointBiome(
						Biomes.DARK_FOREST.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.7, 0.9, 0.8
				),

				new VoronoiPointBiome(
						Biomes.PLAINS.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.1, 0.8
				),
				new VoronoiPointBiome(
						Biomes.PLAINS.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.3, 0.8
				),
				new VoronoiPointBiome(
						Biomes.OLD_GROWTH_BIRCH_FOREST.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.5, 0.8
				),
				new VoronoiPointBiome(
						Biomes.OLD_GROWTH_BIRCH_FOREST.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.7, 0.8
				),
				new VoronoiPointBiome(
						Biomes.MANGROVE_SWAMP.location().toString(),
						Biomes.OCEAN.location().toString(),
						Biomes.DEEP_OCEAN.location().toString(),
						0.5, 0.9, 0.8
				),

				new VoronoiPointBiome(
						Biomes.SNOWY_PLAINS.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.3, 0.1, 0.8
				),
				new VoronoiPointBiome(
						Biomes.OLD_GROWTH_SPRUCE_TAIGA.location().toString(),
						Biomes.COLD_OCEAN.location().toString(),
						Biomes.DEEP_COLD_OCEAN.location().toString(),
						0.3, 0.3, 0.8
				),
				new VoronoiPointBiome(
						Biomes.OLD_GROWTH_SPRUCE_TAIGA.location().toString(),
						Biomes.COLD_OCEAN.location().toString(),
						Biomes.DEEP_COLD_OCEAN.location().toString(),
						0.3, 0.5, 0.8
				),
				new VoronoiPointBiome(
						Biomes.GROVE.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.3, 0.7, 0.8
				),
				new VoronoiPointBiome(
						Biomes.GROVE.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.3, 0.9, 0.8
				),

				new VoronoiPointBiome(
						Biomes.SNOWY_PLAINS.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.1, 0.8
				),
				new VoronoiPointBiome(
						Biomes.SNOWY_PLAINS.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.3, 0.8
				),
				new VoronoiPointBiome(
						Biomes.SNOWY_PLAINS.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.5, 0.8
				),
				new VoronoiPointBiome(
						Biomes.ICE_SPIKES.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.7, 0.8
				),
				new VoronoiPointBiome(
						Biomes.ICE_SPIKES.location().toString(),
						Biomes.FROZEN_OCEAN.location().toString(),
						Biomes.DEEP_FROZEN_OCEAN.location().toString(),
						0.1, 0.9, 0.8
				)
		);

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetAlphaWinter() {
		ModernBetaSettingsPreset initial = presetAlpha();

		CompoundTag compoundChunk = initial.settingsChunk().toCompound();
		CompoundTag compoundBiome = initial.settingsBiome().toCompound();
		CompoundTag compoundCaveBiome = initial.settingsCaveBiome().toCompound();

		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);

		settingsBiome.singleBiome = ModernBetaBiomes.ALPHA_WINTER.location().toString();

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetIndevParadise() {
		ModernBetaSettingsPreset initial = presetIndev();

		CompoundTag compoundChunk = initial.settingsChunk().toCompound();
		CompoundTag compoundBiome = initial.settingsBiome().toCompound();
		CompoundTag compoundCaveBiome = initial.settingsCaveBiome().toCompound();

		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);

		settingsChunk.indevLevelTheme = IndevTheme.PARADISE.getId();

		settingsBiome.singleBiome = ModernBetaBiomes.INDEV_PARADISE.location().toString();

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetIndevWoods() {
		ModernBetaSettingsPreset initial = presetIndev();

		CompoundTag compoundChunk = initial.settingsChunk().toCompound();
		CompoundTag compoundBiome = initial.settingsBiome().toCompound();
		CompoundTag compoundCaveBiome = initial.settingsCaveBiome().toCompound();

		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);

		settingsChunk.indevLevelTheme = IndevTheme.WOODS.getId();

		settingsBiome.singleBiome = ModernBetaBiomes.INDEV_WOODS.location().toString();

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

	private static ModernBetaSettingsPreset presetIndevHell() {
		ModernBetaSettingsPreset initial = presetIndev();

		CompoundTag compoundChunk = initial.settingsChunk().toCompound();
		CompoundTag compoundBiome = initial.settingsBiome().toCompound();
		CompoundTag compoundCaveBiome = initial.settingsCaveBiome().toCompound();

		ModernBetaSettingsChunk.Builder settingsChunk = new ModernBetaSettingsChunk.Builder().fromCompound(compoundChunk);
		ModernBetaSettingsBiome.Builder settingsBiome = new ModernBetaSettingsBiome.Builder().fromCompound(compoundBiome);
		ModernBetaSettingsCaveBiome.Builder settingsCaveBiome = new ModernBetaSettingsCaveBiome.Builder().fromCompound(compoundCaveBiome);

		settingsChunk.indevLevelTheme = IndevTheme.HELL.getId();

		settingsBiome.singleBiome = ModernBetaBiomes.INDEV_HELL.location().toString();

		return new ModernBetaSettingsPreset(
				settingsChunk.build(),
				settingsBiome.build(),
				settingsCaveBiome.build()
		);
	}

}
