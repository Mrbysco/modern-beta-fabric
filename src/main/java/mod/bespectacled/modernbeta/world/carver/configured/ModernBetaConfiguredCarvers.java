package mod.bespectacled.modernbeta.world.carver.configured;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.data.ModernBetaBlockTagProvider;
import mod.bespectacled.modernbeta.world.carver.BetaCaveCarverConfig;
import mod.bespectacled.modernbeta.world.carver.ModernBetaCarvers;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.heightproviders.BiasedToBottomHeight;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;

import java.util.Optional;

public class ModernBetaConfiguredCarvers {
	public static final ResourceKey<ConfiguredWorldCarver<?>> BETA_CAVE = of("beta_cave");
	public static final ResourceKey<ConfiguredWorldCarver<?>> BETA_CAVE_DEEP = of("beta_cave_deep");

	@SuppressWarnings("unchecked")
	public static void bootstrap(BootstrapContext<?> registerable) {
		BootstrapContext<ConfiguredWorldCarver<?>> carverRegisterable = (BootstrapContext<ConfiguredWorldCarver<?>>) registerable;
		HolderGetter<Block> registryBlock = carverRegisterable.lookup(Registries.BLOCK);

		boolean useFixedCaves = false;
		boolean useAquifers = false;

		BetaCaveCarverConfig configCave = new BetaCaveCarverConfig(
				0.0f,                                                                               // Probability, unused here
				BiasedToBottomHeight.of(VerticalAnchor.absolute(0), VerticalAnchor.absolute(127), 8),       // Y Level
				ConstantFloat.of(0.5f),                                                 // Y scale, for large cave case(?)
				VerticalAnchor.aboveBottom(10),                                                            // Lava Level
				CarverDebugSettings.of(false, Blocks.WARPED_BUTTON.defaultBlockState()),
				registryBlock.getOrThrow(ModernBetaBlockTagProvider.OVERWORLD_CARVER_REPLACEABLES),
				ConstantFloat.of(1.0f),                                                 // Tunnel horizontal scale
				ConstantFloat.of(1.0f),                                                 // Tunnel vertical scale
				ConstantFloat.of(-0.7f),                                                // Y Floor Level
				Optional.of(useFixedCaves),
				Optional.of(useAquifers)
		);

		CaveCarverConfiguration configCaveDeep = new CaveCarverConfiguration(
				0.15f,                                                                              // Probability, unused here
				UniformHeight.of(VerticalAnchor.aboveBottom(0), VerticalAnchor.absolute(0)),             // Y Level
				UniformFloat.of(0.1f, 0.9f),                                            // Y scale, for large cave case(?)
				VerticalAnchor.aboveBottom(10),                                                            // Lava Level
				CarverDebugSettings.of(false, Blocks.CRIMSON_BUTTON.defaultBlockState()),
				registryBlock.getOrThrow(ModernBetaBlockTagProvider.OVERWORLD_CARVER_REPLACEABLES),
				UniformFloat.of(0.7f, 1.4f),                                            // Tunnel horizontal scale
				UniformFloat.of(0.8f, 1.3f),                                            // Tunnel vertical scale
				UniformFloat.of(-1.0f, -0.4f)                                           // Y Floor Level
		);

		carverRegisterable.register(BETA_CAVE, ModernBetaCarvers.BETA_CAVE.get().configured(configCave));
		carverRegisterable.register(BETA_CAVE_DEEP, WorldCarver.CAVE.configured(configCaveDeep));
	}

	public static ResourceKey<ConfiguredWorldCarver<?>> of(String id) {
		return ResourceKey.create(Registries.CONFIGURED_CARVER, ModernBeta.createId(id));
	}
}
