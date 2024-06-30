package mod.bespectacled.modernbeta.world.blocksource;

import mod.bespectacled.modernbeta.api.world.blocksource.BlockSource;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsChunk;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

public class BlockSourceDeepslate implements BlockSource {
	private final int minY;
	private final int maxY;
	private final boolean useDeepslate;
	private final BlockState deepslateBlock;
	private final PositionalRandomFactory randomSplitter;

	public BlockSourceDeepslate(ModernBetaSettingsChunk chunkSettings, PositionalRandomFactory randomSplitter) {
		this.minY = chunkSettings.deepslateMinY;
		this.maxY = chunkSettings.deepslateMaxY;
		this.useDeepslate = chunkSettings.useDeepslate;
		this.deepslateBlock = BuiltInRegistries.BLOCK.getOrThrow(keyOf(chunkSettings.deepslateBlock)).defaultBlockState();
		this.randomSplitter = randomSplitter;
	}

	@Override
	public BlockState apply(int x, int y, int z) {
		if (!this.useDeepslate || y >= maxY)
			return null;

		if (y <= minY)
			return this.deepslateBlock;

		double yThreshold = Mth.lerp(Mth.inverseLerp(y, minY, maxY), 1.0, 0.0);
		RandomSource random = this.randomSplitter.at(x, y, z);

		return (double) random.nextFloat() < yThreshold ? this.deepslateBlock : null;
	}

	private static ResourceKey<Block> keyOf(String block) {
		return ResourceKey.create(Registries.BLOCK, ResourceLocation.tryParse(block));
	}
}
