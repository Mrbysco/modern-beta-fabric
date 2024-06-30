package mod.bespectacled.modernbeta.world.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.feature.placement.noise.NoiseBasedCountAlpha;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class NoiseBasedCountPlacementModifierAlpha extends NoiseBasedCountPlacementModifier {
	public static final MapCodec<NoiseBasedCountPlacementModifierAlpha> MODIFIER_CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					Codec.INT.fieldOf("count").forGetter(arg -> arg.count),
					Codec.DOUBLE.fieldOf("extra_chance").forGetter(arg -> arg.extraChance),
					Codec.INT.fieldOf("extra_count").forGetter(arg -> arg.extraCount)
			).apply(instance, NoiseBasedCountPlacementModifierAlpha::of));

	protected NoiseBasedCountPlacementModifierAlpha(int count, double extraChance, int extraCount) {
		super(count, extraChance, extraCount);
	}

	public static NoiseBasedCountPlacementModifierAlpha of(int count, double extraChance, int extraCount) {
		return new NoiseBasedCountPlacementModifierAlpha(count, extraChance, extraCount);
	}

	@Override
	public void setOctaves(PerlinOctaveNoise octaves) {
		this.noiseDecorator = new NoiseBasedCountAlpha(octaves);
	}

	@Override
	public PlacementModifierType<?> type() {
		return ModernBetaPlacementTypes.ALPHA_NOISE_BASED_COUNT.get();
	}

}
