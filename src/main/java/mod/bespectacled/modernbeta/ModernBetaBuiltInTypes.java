package mod.bespectacled.modernbeta;

import mod.bespectacled.modernbeta.data.ModernBetaBiomeTagProvider;

public final class ModernBetaBuiltInTypes {
	public static final String DEFAULT_ID = "default";

	public enum Chunk {
		BETA("beta"),
		SKYLANDS("skylands"),
		ALPHA("alpha"),
		INFDEV_611("infdev_611"),
		INFDEV_420("infdev_420"),
		INFDEV_415("infdev_415"),
		INFDEV_227("infdev_227"),
		INDEV("indev"),
		CLASSIC_0_30("classic_0_30"),
		PE("pe");

		public final String id;

		private Chunk(String id) {
			this.id = id;
		}
	}

	public enum Biome {
		BETA("beta"),
		SINGLE("single"),
		PE("pe"),
		VORONOI("voronoi");

		public final String id;

		private Biome(String id) {
			this.id = id;
		}
	}

	public enum CaveBiome {
		NONE("none"),
		VORONOI("voronoi"),
		BETA("beta"),
		SINGLE("single");

		public final String id;

		private CaveBiome(String id) {
			this.id = id;
		}
	}

	public enum SurfaceConfig {
		SAND(ModernBetaBiomeTagProvider.SURFACE_CONFIG_SAND.location().toString()),
		RED_SAND(ModernBetaBiomeTagProvider.SURFACE_CONFIG_RED_SAND.location().toString()),
		BADLANDS(ModernBetaBiomeTagProvider.SURFACE_CONFIG_BADLANDS.location().toString()),
		NETHER(ModernBetaBiomeTagProvider.SURFACE_CONFIG_NETHER.location().toString()),
		WARPED_NYLIUM(ModernBetaBiomeTagProvider.SURFACE_CONFIG_WARPED_NYLIUM.location().toString()),
		CRIMSON_NYLIUM(ModernBetaBiomeTagProvider.SURFACE_CONFIG_CRIMSON_NYLIUM.location().toString()),
		BASALT(ModernBetaBiomeTagProvider.SURFACE_CONFIG_BASALT.location().toString()),
		SOUL_SOIL(ModernBetaBiomeTagProvider.SURFACE_CONFIG_SOUL_SOIL.location().toString()),
		THEEND(ModernBetaBiomeTagProvider.SURFACE_CONFIG_END.location().toString()),
		GRASS(ModernBetaBiomeTagProvider.SURFACE_CONFIG_GRASS.location().toString()),
		MUD(ModernBetaBiomeTagProvider.SURFACE_CONFIG_MUD.location().toString()),
		MYCELIUM(ModernBetaBiomeTagProvider.SURFACE_CONFIG_MYCELIUM.location().toString()),
		PODZOL(ModernBetaBiomeTagProvider.SURFACE_CONFIG_PODZOL.location().toString()),
		STONE(ModernBetaBiomeTagProvider.SURFACE_CONFIG_STONE.location().toString()),
		SNOW(ModernBetaBiomeTagProvider.SURFACE_CONFIG_SNOW.location().toString()),
		SNOW_DIRT(ModernBetaBiomeTagProvider.SURFACE_CONFIG_SNOW_DIRT.location().toString()),
		SNOW_PACKED_ICE(ModernBetaBiomeTagProvider.SURFACE_CONFIG_SNOW_PACKED_ICE.location().toString()),
		SNOW_STONE(ModernBetaBiomeTagProvider.SURFACE_CONFIG_SNOW_STONE.location().toString());

		public final String id;

		private SurfaceConfig(String id) {
			this.id = id;
		}
	}

	public enum NoisePostProcessor {
		NONE("none");

		public final String id;

		private NoisePostProcessor(String id) {
			this.id = id;
		}
	}

	public enum BlockSource {
		DEEPSLATE("deepslate");

		public final String id;

		private BlockSource(String id) {
			this.id = id;
		}
	}

	public enum Preset {
		BETA(Chunk.BETA.id),
		SKYLANDS(Chunk.SKYLANDS.id),
		ALPHA(Chunk.ALPHA.id),
		INFDEV_611(Chunk.INFDEV_611.id),
		INFDEV_420(Chunk.INFDEV_420.id),
		INFDEV_415(Chunk.INFDEV_415.id),
		INFDEV_227(Chunk.INFDEV_227.id),
		INDEV(Chunk.INDEV.id),
		CLASSIC_0_30(Chunk.CLASSIC_0_30.id),
		PE(Chunk.PE.id);

		public final String id;

		private Preset(String id) {
			this.id = id;
		}
	}

	public enum PresetAlt {
		BETA_SKYLANDS("beta_skylands"),
		BETA_ISLES("beta_isles"),
		BETA_ISLE_LAND("beta_isle_land"),
		BETA_CAVE_DELIGHT("beta_cave_delight"),
		BETA_CAVE_CHAOS("beta_cave_chaos"),
		BETA_LARGE_BIOMES("beta_large_biomes"),
		BETA_XBOX_LEGACY("beta_xbox_legacy"),
		BETA_SURVIVAL_ISLAND("beta_survival_island"),
		BETA_VANILLA("beta_vanilla"),
		ALPHA_WINTER("alpha_winter"),
		INDEV_PARADISE("indev_paradise"),
		INDEV_WOODS("indev_woods"),
		INDEV_HELL("indev_hell");

		public final String id;

		private PresetAlt(String id) {
			this.id = id;
		}
	}
}
