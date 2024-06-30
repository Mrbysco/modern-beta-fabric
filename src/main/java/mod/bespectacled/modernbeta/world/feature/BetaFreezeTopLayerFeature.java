package mod.bespectacled.modernbeta.world.feature;

import com.mojang.serialization.Codec;
import mod.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class BetaFreezeTopLayerFeature extends Feature<NoneFeatureConfiguration> {
	public BetaFreezeTopLayerFeature(Codec<NoneFeatureConfiguration> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		BlockPos pos = context.origin();

		ChunkGenerator chunkGenerator = context.chunkGenerator();
		BiomeSource biomeSource = chunkGenerator.getBiomeSource();

		setFreezeTopLayer(world, pos, biomeSource);
		return true;
	}

	public static void setFreezeTopLayer(WorldGenLevel world, BlockPos pos, BiomeSource biomeSource) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		BlockPos.MutableBlockPos mutableDown = new BlockPos.MutableBlockPos();

		for (int localX = 0; localX < 16; ++localX) {
			for (int localZ = 0; localZ < 16; ++localZ) {
				int x = pos.getX() + localX;
				int z = pos.getZ() + localZ;
				int y = world.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);

				mutable.set(x, y, z);
				mutableDown.set(mutable).move(Direction.DOWN, 1);

				double temp;
				double coldThreshold;

				if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource &&
						modernBetaBiomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler &&
						climateSampler.useBiomeFeature()
				) {
					temp = climateSampler.sample(x, z).temp();
					coldThreshold = 0.5;

				} else {
					temp = world.getBiome(mutable).value().getBaseTemperature();
					coldThreshold = 0.15;
				}

				if (canSetIce(world, mutableDown, false, temp, coldThreshold)) {
					world.setBlock(mutableDown, Blocks.ICE.defaultBlockState(), 2);
				}

				if (canSetSnow(world, mutable, temp, coldThreshold)) {
					world.setBlock(mutable, Blocks.SNOW.defaultBlockState(), 2);

					BlockState blockState = world.getBlockState(mutableDown);
					if (blockState.hasProperty(SnowyDirtBlock.SNOWY)) {
						world.setBlock(mutableDown, blockState.setValue(SnowyDirtBlock.SNOWY, true), 2);
					}
				}
			}
		}
	}

	private static boolean canSetIce(
			LevelReader worldView,
			BlockPos blockPos,
			boolean doWaterCheck,
			double temp,
			double coldThreshold
	) {
		if (temp >= coldThreshold) {
			return false;
		}

		if (blockPos.getY() >= worldView.getMinBuildHeight() &&
				blockPos.getY() < worldView.getMaxBuildHeight() &&
				worldView.getBrightness(LightLayer.BLOCK, blockPos) < 10
		) {
			BlockState blockState = worldView.getBlockState(blockPos);
			FluidState fluidState = worldView.getFluidState(blockPos);

			if (fluidState.getType() == Fluids.WATER && blockState.getBlock() instanceof LiquidBlock) {
				if (!doWaterCheck) {
					return true;
				}

				boolean submerged =
						worldView.isWaterAt(blockPos.west()) &&
								worldView.isWaterAt(blockPos.east()) &&
								worldView.isWaterAt(blockPos.north()) &&
								worldView.isWaterAt(blockPos.south());

				if (!submerged) {
					return true;
				}
			}
		}

		return false;
	}

	private static boolean canSetSnow(LevelReader worldView, BlockPos blockPos, double temp, double coldThreshold) {
		double heightTemp = temp - ((double) (blockPos.getY() - 64) / 64.0) * 0.3;

		if (heightTemp >= coldThreshold) {
			return false;
		}

		if (blockPos.getY() >= 0 && blockPos.getY() < 256 && worldView.getBrightness(LightLayer.BLOCK, blockPos) < 10) {
			BlockState blockState = worldView.getBlockState(blockPos);

			if (blockState.isAir() && Blocks.SNOW.defaultBlockState().canSurvive(worldView, blockPos)) {
				return true;
			}
		}

		return false;
	}
}
