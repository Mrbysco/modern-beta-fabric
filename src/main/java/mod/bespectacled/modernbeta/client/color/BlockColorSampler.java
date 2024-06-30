package mod.bespectacled.modernbeta.client.color;

import mod.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public final class BlockColorSampler {
	public static final BlockColorSampler INSTANCE = new BlockColorSampler();

	public final BlockColormap colormapGrass;
	public final BlockColormap colormapFoliage;
	public final BlockColormap colormapWater;
	public final BlockColormap colormapUnderwater;

	private Optional<ClimateSampler> climateSampler;

	private BlockColorSampler() {
		this.colormapGrass = new BlockColormap();
		this.colormapFoliage = new BlockColormap();
		this.colormapWater = new BlockColormap();
		this.colormapUnderwater = new BlockColormap();

		this.climateSampler = Optional.empty();
	}

	public Optional<ClimateSampler> getClimateSampler() {
		return this.climateSampler;
	}

	public void setClimateSampler(ClimateSampler climateSampler) {
		this.climateSampler = Optional.ofNullable(climateSampler);
	}

	public int getGrassColor(BlockState state, BlockAndTintGetter view, BlockPos pos, int tintNdx) {
		if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
			return 8174955; // Default tint, from wiki
		}

		if (this.useBiomeColor()) {
			int x = pos.getX();
			int z = pos.getZ();

			Clime clime = this.climateSampler.get().sample(x, z);

			return GrassColor.get(clime.temp(), clime.rain());
		}

		return BiomeColors.getAverageGrassColor(view, pos);
	}

	public int getTallGrassColor(BlockState state, BlockAndTintGetter view, BlockPos pos, int tintNdx) {
		if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
			return 8174955; // Default tint, from wiki
		}

		if (this.useBiomeColor()) {
			int x = pos.getX();
			int y = pos.getY();
			int z = pos.getZ();

			long shift = x * 0x2fc20fL + z * 0x5d8875L + y;
			shift = shift * shift * 0x285b825L + shift * 11L;
			x = (int) ((long) x + (shift >> 14 & 31L));
			y = (int) ((long) y + (shift >> 19 & 31L));
			z = (int) ((long) z + (shift >> 24 & 31L));

			Clime clime = this.climateSampler.get().sample(x, z);

			return GrassColor.get(clime.temp(), clime.rain());
		}

		return BiomeColors.getAverageGrassColor(view, pos);
	}

	public int getFoliageColor(BlockState state, BlockAndTintGetter view, BlockPos pos, int tintNdx) {
		if (view == null || pos == null) { // Appears to enter here when loading color for inventory block
			return 4764952; // Default tint, from wiki
		}

		if (this.useBiomeColor()) {
			int x = pos.getX();
			int z = pos.getZ();

			Clime clime = this.climateSampler.get().sample(x, z);

			return FoliageColor.get(clime.temp(), clime.rain());
		}

		return BiomeColors.getAverageFoliageColor(view, pos);
	}

	public int getWaterColor(BlockState state, BlockAndTintGetter view, BlockPos pos, int tintNdx) {
		if (this.useWaterColor()) {
			int x = pos.getX();
			int z = pos.getZ();

			Clime clime = this.climateSampler.get().sample(x, z);

			return this.colormapWater.getColor(clime.temp(), clime.rain());
		}

		return BiomeColors.getAverageWaterColor(view, pos);
	}

	public int getSugarCaneColor(BlockState state, BlockAndTintGetter view, BlockPos pos, int tintNdx) {
		if (this.useBiomeColor()) {
			return 0xFFFFFF;
		}

		return BiomeColors.getAverageGrassColor(view, pos);
	}

	public boolean useBiomeColor() {
		return this.climateSampler.isPresent() && this.climateSampler.get().useBiomeColor();
	}

	public boolean useWaterColor() {
		return this.climateSampler.isPresent() && this.climateSampler.get().useWaterColor();
	}
}
