package mod.bespectacled.modernbeta.data;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModernBetaBiomeTagProvider extends BiomeTagsProvider {
	public static final TagKey<Biome> IS_MODERN_BETA = keyOf("is_modern_beta");
	public static final TagKey<Biome> IS_BETA = keyOf("is_beta");
	public static final TagKey<Biome> IS_PE = keyOf("is_pe");
	public static final TagKey<Biome> IS_ALPHA = keyOf("is_alpha");
	public static final TagKey<Biome> IS_INFDEV = keyOf("is_infdev");
	public static final TagKey<Biome> IS_INDEV = keyOf("is_indev");

	public static final TagKey<Biome> IS_FOREST = keyOf("is_forest");
	public static final TagKey<Biome> IS_SEASONAL_FOREST = keyOf("is_seasonal_forest");
	public static final TagKey<Biome> IS_RAINFOREST = keyOf("is_rainforest");
	public static final TagKey<Biome> IS_DESERT = keyOf("is_desert");
	public static final TagKey<Biome> IS_PLAINS = keyOf("is_plains");
	public static final TagKey<Biome> IS_SHRUBLAND = keyOf("is_shrubland");
	public static final TagKey<Biome> IS_SAVANNA = keyOf("is_savanna");
	public static final TagKey<Biome> IS_SWAMP = keyOf("is_swamp");
	public static final TagKey<Biome> IS_TAIGA = keyOf("is_taiga");
	public static final TagKey<Biome> IS_TUNDRA = keyOf("is_tundra");
	public static final TagKey<Biome> IS_OCEAN = keyOf("is_ocean");

	public static final TagKey<Biome> INDEV_STRONGHOLD_HAS_STRUCTURE = keyOf("has_structure/indev_stronghold");

	/*
	 * TODO: Deprecated, remove 1.20
	 */
	public static final TagKey<Biome> SURFACE_CONFIG_IS_DESERT = keyOf("surface_config/is_desert");
	public static final TagKey<Biome> SURFACE_CONFIG_IS_BADLANDS = keyOf("surface_config/is_badlands");
	public static final TagKey<Biome> SURFACE_CONFIG_IS_NETHER = keyOf("surface_config/is_nether");
	public static final TagKey<Biome> SURFACE_CONFIG_IS_END = keyOf("surface_config/is_end");
	public static final TagKey<Biome> SURFACE_CONFIG_SWAMP = keyOf("surface_config/swamp");

	public static final TagKey<Biome> SURFACE_CONFIG_SAND = keyOf("surface_config/sand");
	public static final TagKey<Biome> SURFACE_CONFIG_RED_SAND = keyOf("surface_config/red_sand");
	public static final TagKey<Biome> SURFACE_CONFIG_BADLANDS = keyOf("surface_config/badlands");
	public static final TagKey<Biome> SURFACE_CONFIG_NETHER = keyOf("surface_config/nether");
	public static final TagKey<Biome> SURFACE_CONFIG_WARPED_NYLIUM = keyOf("surface_config/warped_nylium");
	public static final TagKey<Biome> SURFACE_CONFIG_CRIMSON_NYLIUM = keyOf("surface_config/crimson_nylium");
	public static final TagKey<Biome> SURFACE_CONFIG_BASALT = keyOf("surface_config/basalt");
	public static final TagKey<Biome> SURFACE_CONFIG_SOUL_SOIL = keyOf("surface_config/soul_soil");
	public static final TagKey<Biome> SURFACE_CONFIG_END = keyOf("surface_config/end");
	public static final TagKey<Biome> SURFACE_CONFIG_GRASS = keyOf("surface_config/grass");
	public static final TagKey<Biome> SURFACE_CONFIG_MUD = keyOf("surface_config/mud");
	public static final TagKey<Biome> SURFACE_CONFIG_MYCELIUM = keyOf("surface_config/mycelium");
	public static final TagKey<Biome> SURFACE_CONFIG_PODZOL = keyOf("surface_config/podzol");
	public static final TagKey<Biome> SURFACE_CONFIG_STONE = keyOf("surface_config/stone");
	public static final TagKey<Biome> SURFACE_CONFIG_SNOW = keyOf("surface_config/snow");
	public static final TagKey<Biome> SURFACE_CONFIG_SNOW_DIRT = keyOf("surface_config/snow_dirt");
	public static final TagKey<Biome> SURFACE_CONFIG_SNOW_PACKED_ICE = keyOf("surface_config/snow_packed_ice");
	public static final TagKey<Biome> SURFACE_CONFIG_SNOW_STONE = keyOf("surface_config/snow_stone");

	public ModernBetaBiomeTagProvider(PackOutput output, CompletableFuture<Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, provider, ModernBeta.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider lookup) {
		this.configureModernBeta(lookup);
		this.configureVanilla(lookup);
		this.configureConventional(lookup);
	}

	private void configureModernBeta(Provider lookup) {
		/* Modern Beta Biome Tags */

		tag(IS_MODERN_BETA).add(
				ModernBetaBiomes.BETA_FOREST,
				ModernBetaBiomes.BETA_SHRUBLAND,
				ModernBetaBiomes.BETA_DESERT,
				ModernBetaBiomes.BETA_SAVANNA,
				ModernBetaBiomes.BETA_PLAINS,
				ModernBetaBiomes.BETA_SEASONAL_FOREST,
				ModernBetaBiomes.BETA_RAINFOREST,
				ModernBetaBiomes.BETA_SWAMPLAND,
				ModernBetaBiomes.BETA_TAIGA,
				ModernBetaBiomes.BETA_TUNDRA,
				ModernBetaBiomes.BETA_ICE_DESERT,
				ModernBetaBiomes.BETA_OCEAN,
				ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
				ModernBetaBiomes.BETA_WARM_OCEAN,
				ModernBetaBiomes.BETA_COLD_OCEAN,
				ModernBetaBiomes.BETA_FROZEN_OCEAN,
				ModernBetaBiomes.BETA_SKY,

				ModernBetaBiomes.PE_FOREST,
				ModernBetaBiomes.PE_SHRUBLAND,
				ModernBetaBiomes.PE_DESERT,
				ModernBetaBiomes.PE_SAVANNA,
				ModernBetaBiomes.PE_PLAINS,
				ModernBetaBiomes.PE_SEASONAL_FOREST,
				ModernBetaBiomes.PE_RAINFOREST,
				ModernBetaBiomes.PE_SWAMPLAND,
				ModernBetaBiomes.PE_TAIGA,
				ModernBetaBiomes.PE_TUNDRA,
				ModernBetaBiomes.PE_ICE_DESERT,
				ModernBetaBiomes.PE_OCEAN,
				ModernBetaBiomes.PE_LUKEWARM_OCEAN,
				ModernBetaBiomes.PE_WARM_OCEAN,
				ModernBetaBiomes.PE_COLD_OCEAN,
				ModernBetaBiomes.PE_FROZEN_OCEAN,

				ModernBetaBiomes.ALPHA,
				ModernBetaBiomes.ALPHA_WINTER,

				ModernBetaBiomes.INFDEV_611,
				ModernBetaBiomes.INFDEV_420,
				ModernBetaBiomes.INFDEV_415,
				ModernBetaBiomes.INFDEV_227,

				ModernBetaBiomes.INDEV_NORMAL,
				ModernBetaBiomes.INDEV_HELL,
				ModernBetaBiomes.INDEV_PARADISE,
				ModernBetaBiomes.INDEV_WOODS
		);

		tag(IS_BETA).add(
				ModernBetaBiomes.BETA_FOREST,
				ModernBetaBiomes.BETA_SHRUBLAND,
				ModernBetaBiomes.BETA_DESERT,
				ModernBetaBiomes.BETA_SAVANNA,
				ModernBetaBiomes.BETA_PLAINS,
				ModernBetaBiomes.BETA_SEASONAL_FOREST,
				ModernBetaBiomes.BETA_RAINFOREST,
				ModernBetaBiomes.BETA_SWAMPLAND,
				ModernBetaBiomes.BETA_TAIGA,
				ModernBetaBiomes.BETA_TUNDRA,
				ModernBetaBiomes.BETA_ICE_DESERT,
				ModernBetaBiomes.BETA_OCEAN,
				ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
				ModernBetaBiomes.BETA_WARM_OCEAN,
				ModernBetaBiomes.BETA_COLD_OCEAN,
				ModernBetaBiomes.BETA_FROZEN_OCEAN,
				ModernBetaBiomes.BETA_SKY
		);

		tag(IS_PE).add(
				ModernBetaBiomes.PE_FOREST,
				ModernBetaBiomes.PE_SHRUBLAND,
				ModernBetaBiomes.PE_DESERT,
				ModernBetaBiomes.PE_SAVANNA,
				ModernBetaBiomes.PE_PLAINS,
				ModernBetaBiomes.PE_SEASONAL_FOREST,
				ModernBetaBiomes.PE_RAINFOREST,
				ModernBetaBiomes.PE_SWAMPLAND,
				ModernBetaBiomes.PE_TAIGA,
				ModernBetaBiomes.PE_TUNDRA,
				ModernBetaBiomes.PE_ICE_DESERT,
				ModernBetaBiomes.PE_OCEAN,
				ModernBetaBiomes.PE_LUKEWARM_OCEAN,
				ModernBetaBiomes.PE_WARM_OCEAN,
				ModernBetaBiomes.PE_COLD_OCEAN,
				ModernBetaBiomes.PE_FROZEN_OCEAN
		);

		tag(IS_ALPHA).add(
				ModernBetaBiomes.ALPHA,
				ModernBetaBiomes.ALPHA_WINTER
		);

		tag(IS_INFDEV).add(
				ModernBetaBiomes.INFDEV_611,
				ModernBetaBiomes.INFDEV_420,
				ModernBetaBiomes.INFDEV_415,
				ModernBetaBiomes.INFDEV_227
		);

		tag(IS_INDEV).add(
				ModernBetaBiomes.INDEV_NORMAL,
				ModernBetaBiomes.INDEV_HELL,
				ModernBetaBiomes.INDEV_PARADISE,
				ModernBetaBiomes.INDEV_WOODS
		);

		tag(IS_FOREST).add(
				ModernBetaBiomes.BETA_FOREST,
				ModernBetaBiomes.PE_FOREST
		);

		tag(IS_SEASONAL_FOREST).add(
				ModernBetaBiomes.BETA_SEASONAL_FOREST,
				ModernBetaBiomes.PE_SEASONAL_FOREST
		);

		tag(IS_RAINFOREST).add(
				ModernBetaBiomes.BETA_RAINFOREST,
				ModernBetaBiomes.PE_RAINFOREST
		);

		tag(IS_DESERT).add(
				ModernBetaBiomes.BETA_DESERT,
				ModernBetaBiomes.PE_DESERT
		);

		tag(IS_PLAINS).add(
				ModernBetaBiomes.BETA_PLAINS,
				ModernBetaBiomes.PE_PLAINS
		);

		tag(IS_SHRUBLAND).add(
				ModernBetaBiomes.BETA_SHRUBLAND,
				ModernBetaBiomes.PE_SHRUBLAND
		);

		tag(IS_SAVANNA).add(
				ModernBetaBiomes.BETA_SAVANNA,
				ModernBetaBiomes.PE_SAVANNA
		);

		tag(IS_SWAMP).add(
				ModernBetaBiomes.BETA_SWAMPLAND,
				ModernBetaBiomes.PE_SWAMPLAND
		);

		tag(IS_TAIGA).add(
				ModernBetaBiomes.BETA_TAIGA,
				ModernBetaBiomes.PE_TAIGA
		);

		tag(IS_TUNDRA).add(
				ModernBetaBiomes.BETA_TUNDRA,
				ModernBetaBiomes.PE_TUNDRA,
				ModernBetaBiomes.BETA_ICE_DESERT,
				ModernBetaBiomes.PE_ICE_DESERT
		);

		tag(IS_OCEAN).add(
				ModernBetaBiomes.BETA_OCEAN,
				ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
				ModernBetaBiomes.BETA_WARM_OCEAN,
				ModernBetaBiomes.BETA_COLD_OCEAN,
				ModernBetaBiomes.BETA_FROZEN_OCEAN,

				ModernBetaBiomes.PE_OCEAN,
				ModernBetaBiomes.PE_LUKEWARM_OCEAN,
				ModernBetaBiomes.PE_WARM_OCEAN,
				ModernBetaBiomes.PE_COLD_OCEAN,
				ModernBetaBiomes.PE_FROZEN_OCEAN
		);

		/* Modern Beta Biome Structure Tags */

		tag(INDEV_STRONGHOLD_HAS_STRUCTURE)
				.addTag(IS_INDEV);

		/* Modern Beta Surface Config Tags */

		tag(SURFACE_CONFIG_SAND)
				.addOptionalTag(SURFACE_CONFIG_IS_DESERT)
				.add(
						ModernBetaBiomes.BETA_DESERT,
						ModernBetaBiomes.PE_DESERT,
						Biomes.DESERT,
						Biomes.BEACH,
						Biomes.SNOWY_BEACH
				);

		tag(SURFACE_CONFIG_RED_SAND);

		tag(SURFACE_CONFIG_BADLANDS)
				.addOptionalTag(SURFACE_CONFIG_IS_BADLANDS)
				.add(
						Biomes.BADLANDS,
						Biomes.ERODED_BADLANDS,
						Biomes.WOODED_BADLANDS
				);

		tag(SURFACE_CONFIG_NETHER)
				.addOptionalTag(SURFACE_CONFIG_IS_NETHER)
				.add(Biomes.NETHER_WASTES);

		tag(SURFACE_CONFIG_WARPED_NYLIUM)
				.add(Biomes.WARPED_FOREST);

		tag(SURFACE_CONFIG_CRIMSON_NYLIUM)
				.add(Biomes.CRIMSON_FOREST);

		tag(SURFACE_CONFIG_BASALT)
				.add(Biomes.BASALT_DELTAS);

		tag(SURFACE_CONFIG_SOUL_SOIL)
				.add(Biomes.SOUL_SAND_VALLEY);

		tag(SURFACE_CONFIG_END)
				.addOptionalTag(SURFACE_CONFIG_IS_END)
				.add(
						Biomes.THE_END,
						Biomes.END_BARRENS,
						Biomes.END_HIGHLANDS,
						Biomes.END_MIDLANDS,
						Biomes.SMALL_END_ISLANDS
				);

		tag(SURFACE_CONFIG_GRASS)
				.addOptionalTag(SURFACE_CONFIG_SWAMP)
				.add(Biomes.SWAMP);

		tag(SURFACE_CONFIG_MUD)
				.add(Biomes.MANGROVE_SWAMP);

		tag(SURFACE_CONFIG_MYCELIUM)
				.add(Biomes.MUSHROOM_FIELDS);

		tag(SURFACE_CONFIG_PODZOL)
				.add(
						Biomes.OLD_GROWTH_PINE_TAIGA,
						Biomes.OLD_GROWTH_SPRUCE_TAIGA
				);

		tag(SURFACE_CONFIG_STONE)
				.add(
						Biomes.STONY_PEAKS,
						Biomes.STONY_SHORE
				);

		tag(SURFACE_CONFIG_SNOW)
				.add(Biomes.SNOWY_SLOPES);

		tag(SURFACE_CONFIG_SNOW_DIRT)
				.add(
						Biomes.GROVE,
						Biomes.ICE_SPIKES
				);

		tag(SURFACE_CONFIG_SNOW_PACKED_ICE)
				.add(Biomes.FROZEN_PEAKS);

		tag(SURFACE_CONFIG_SNOW_STONE)
				.add(Biomes.JAGGED_PEAKS);
	}

	private void configureVanilla(Provider lookup) {
		/* Vanilla Biome Tags */

		tag(BiomeTags.IS_OVERWORLD)
				.addTag(IS_MODERN_BETA);

		tag(BiomeTags.IS_DEEP_OCEAN)
				.addTag(IS_OCEAN);

		tag(BiomeTags.IS_FOREST)
				.addTag(IS_FOREST)
				.addTag(IS_SEASONAL_FOREST);

		tag(BiomeTags.IS_JUNGLE)
				.addTag(IS_RAINFOREST);

		tag(BiomeTags.IS_OCEAN)
				.addTag(IS_OCEAN);

		tag(BiomeTags.IS_TAIGA)
				.addTag(IS_TAIGA);

		/* Vanilla Biome Structure Tags */

		tag(BiomeTags.HAS_BURIED_TREASURE)
				.addTag(IS_OCEAN);

		tag(BiomeTags.HAS_DESERT_PYRAMID)
				.addTag(IS_DESERT);

		tag(BiomeTags.HAS_IGLOO)
				.addTag(IS_TUNDRA);

		tag(BiomeTags.HAS_JUNGLE_TEMPLE)
				.addTag(IS_RAINFOREST);

		tag(BiomeTags.HAS_MINESHAFT)
				.addTag(IS_BETA)
				.addTag(IS_PE)
				.addTag(IS_ALPHA)
				.addTag(IS_INFDEV)
				.addTag(IS_INDEV);

		tag(BiomeTags.HAS_OCEAN_RUIN_COLD).add(
				ModernBetaBiomes.BETA_OCEAN,
				ModernBetaBiomes.BETA_COLD_OCEAN,
				ModernBetaBiomes.BETA_FROZEN_OCEAN,

				ModernBetaBiomes.PE_OCEAN,
				ModernBetaBiomes.PE_COLD_OCEAN,
				ModernBetaBiomes.PE_FROZEN_OCEAN
		);

		tag(BiomeTags.HAS_OCEAN_RUIN_WARM).add(
				ModernBetaBiomes.BETA_LUKEWARM_OCEAN,
				ModernBetaBiomes.BETA_WARM_OCEAN,

				ModernBetaBiomes.PE_LUKEWARM_OCEAN,
				ModernBetaBiomes.PE_WARM_OCEAN
		);

		tag(BiomeTags.HAS_PILLAGER_OUTPOST)
				.addTag(IS_DESERT)
				.addTag(IS_PLAINS)
				.addTag(IS_SAVANNA)
				.addTag(IS_SWAMP)
				.addTag(IS_TUNDRA)
				.add(ModernBetaBiomes.BETA_SKY);

		tag(BiomeTags.HAS_RUINED_PORTAL_DESERT)
				.addTag(IS_DESERT);

		tag(BiomeTags.HAS_RUINED_PORTAL_STANDARD)
				.addTag(IS_PLAINS)
				.addTag(IS_SAVANNA)
				.addTag(IS_TUNDRA)
				.add(ModernBetaBiomes.BETA_SKY);

		tag(BiomeTags.HAS_RUINED_PORTAL_SWAMP)
				.addTag(IS_SWAMP);

		tag(BiomeTags.HAS_STRONGHOLD)
				.addTag(IS_BETA)
				.addTag(IS_PE)
				.addTag(IS_ALPHA)
				.addTag(IS_INFDEV);

		tag(BiomeTags.HAS_SWAMP_HUT)
				.addTag(IS_SWAMP);

		tag(BiomeTags.HAS_VILLAGE_DESERT)
				.addTag(IS_DESERT);

		tag(BiomeTags.HAS_VILLAGE_PLAINS)
				.addTag(IS_PLAINS)
				.addTag(IS_SHRUBLAND)
				.addTag(IS_SAVANNA)
				.addTag(IS_ALPHA)
				.addTag(IS_INFDEV)
				.addTag(IS_INDEV);

		tag(BiomeTags.HAS_VILLAGE_SNOWY)
				.addTag(IS_TUNDRA);

		tag(BiomeTags.HAS_VILLAGE_TAIGA)
				.addTag(IS_TAIGA);

		tag(BiomeTags.HAS_WOODLAND_MANSION)
				.addTag(IS_SEASONAL_FOREST);

		/* Misc. Tags */

		tag(BiomeTags.PRODUCES_CORALS_FROM_BONEMEAL).add(
				ModernBetaBiomes.BETA_WARM_OCEAN,
				ModernBetaBiomes.PE_WARM_OCEAN
		);

		tag(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS).add(
				ModernBetaBiomes.BETA_FROZEN_OCEAN,
				ModernBetaBiomes.PE_FROZEN_OCEAN
		);

		tag(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS)
				.addTag(IS_SWAMP);

		tag(BiomeTags.HAS_TRAIL_RUINS)
				.add(
						ModernBetaBiomes.BETA_TAIGA,
						ModernBetaBiomes.BETA_RAINFOREST,

						ModernBetaBiomes.PE_TAIGA,
						ModernBetaBiomes.PE_RAINFOREST
				);
	}

	/*
	 * For determining climate tags, see:
	 * https://www.minecraftforum.net/forums/archive/alpha/alpha-survival-single-player/820956-biome-geography-algorithm-analysis-updated-11-4
	 *
	 */
	private void configureConventional(Provider lookup) {
		tag(Tags.Biomes.IS_AQUATIC)
				.addTag(IS_OCEAN);

		tag(Tags.Biomes.IS_AQUATIC_ICY).add(
				ModernBetaBiomes.BETA_FROZEN_OCEAN,
				ModernBetaBiomes.PE_FROZEN_OCEAN
		);

		tag(Tags.Biomes.IS_COLD)
				.addTag(IS_TAIGA)
				.addTag(IS_TUNDRA);

		tag(Tags.Biomes.IS_DRY)
				.addTag(IS_DESERT)
				.addTag(IS_PLAINS)
				.addTag(IS_SAVANNA)
				.addTag(IS_SHRUBLAND)
				.addTag(IS_TUNDRA);

		tag(Tags.Biomes.IS_HOT)
				.addTag(IS_DESERT)
				.addTag(IS_PLAINS)
				.addTag(IS_SEASONAL_FOREST)
				.addTag(IS_RAINFOREST);

//		tag(Tags.Biomes.IS_TEMPERATE)
//				.addTag(IS_SAVANNA)
//				.addTag(IS_SHRUBLAND)
//				.addTag(IS_FOREST)
//				.addTag(IS_SWAMP);

		tag(Tags.Biomes.IS_WET)
				.addTag(IS_OCEAN)
				.addTag(IS_SWAMP)
				.addTag(IS_RAINFOREST);

		tag(Tags.Biomes.IS_DESERT)
				.addTag(IS_DESERT);

		tag(Tags.Biomes.IS_FOREST)
				.addTag(IS_FOREST)
				.addTag(IS_SEASONAL_FOREST);

		tag(Tags.Biomes.IS_OVERWORLD)
				.addTag(IS_MODERN_BETA);

		tag(Tags.Biomes.IS_JUNGLE)
				.addTag(IS_RAINFOREST);

		tag(Tags.Biomes.IS_DEEP_OCEAN)
				.addTag(IS_OCEAN);

		tag(Tags.Biomes.IS_PLAINS)
				.addTag(IS_PLAINS)
				.addTag(IS_SHRUBLAND);

		tag(Tags.Biomes.IS_SAVANNA)
				.addTag(IS_SAVANNA);

		tag(Tags.Biomes.IS_SHALLOW_OCEAN)
				.addTag(IS_OCEAN);

		tag(Tags.Biomes.IS_SNOWY)
				.addTag(IS_TAIGA)
				.addTag(IS_TUNDRA);

		tag(Tags.Biomes.IS_SNOWY_PLAINS)
				.addTag(IS_TUNDRA);

		tag(Tags.Biomes.IS_SWAMP)
				.addTag(IS_SWAMP);

		tag(Tags.Biomes.IS_TAIGA)
				.addTag(IS_TAIGA);

		tag(Tags.Biomes.IS_CONIFEROUS_TREE)
				.addTag(IS_TAIGA);

		tag(Tags.Biomes.IS_DECIDUOUS_TREE)
				.addTag(IS_FOREST)
				.addTag(IS_SEASONAL_FOREST);

		tag(Tags.Biomes.IS_JUNGLE_TREE)
				.addTag(IS_RAINFOREST);

		tag(Tags.Biomes.IS_SAVANNA_TREE)
				.addTag(IS_SAVANNA);

		tag(Tags.Biomes.IS_DENSE_VEGETATION)
				.addTag(IS_RAINFOREST)
				.addTag(IS_PLAINS);

		tag(Tags.Biomes.IS_SPARSE_VEGETATION)
				.addTag(IS_DESERT)
				.addTag(IS_SAVANNA)
				.addTag(IS_SHRUBLAND)
				.addTag(IS_TUNDRA);
	}

	private static TagKey<Biome> keyOf(String id) {
		return TagKey.create(Registries.BIOME, ModernBeta.createId(id));
	}
}