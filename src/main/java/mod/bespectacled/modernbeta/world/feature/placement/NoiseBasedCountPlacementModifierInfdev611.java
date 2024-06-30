package mod.bespectacled.modernbeta.world.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.feature.placement.noise.NoiseBasedCountInfdev611;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class NoiseBasedCountPlacementModifierInfdev611 extends NoiseBasedCountPlacementModifier {
	public static final MapCodec<NoiseBasedCountPlacementModifierInfdev611> MODIFIER_CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					Codec.INT.fieldOf("count").forGetter(arg -> arg.count),
					Codec.DOUBLE.fieldOf("extra_chance").forGetter(arg -> arg.extraChance),
					Codec.INT.fieldOf("extra_count").forGetter(arg -> arg.extraCount)
			).apply(instance, NoiseBasedCountPlacementModifierInfdev611::of));

	protected NoiseBasedCountPlacementModifierInfdev611(int count, double extraChance, int extraCount) {
		super(count, extraChance, extraCount);
	}

	public static NoiseBasedCountPlacementModifierInfdev611 of(int count, double extraChance, int extraCount) {
		return new NoiseBasedCountPlacementModifierInfdev611(count, extraChance, extraCount);
	}

	@Override
	public void setOctaves(PerlinOctaveNoise octaves) {
		this.noiseDecorator = new NoiseBasedCountInfdev611(octaves);
	}

	@Override
	public PlacementModifierType<?> type() {
		return ModernBetaPlacementTypes.INFDEV_611_NOISE_BASED_COUNT.get();
	}

}
