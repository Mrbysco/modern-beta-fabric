package mod.bespectacled.modernbeta.world.feature.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.feature.placement.noise.NoiseBasedCountBeta;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class NoiseBasedCountPlacementModifierBeta extends NoiseBasedCountPlacementModifier {
	public static final MapCodec<NoiseBasedCountPlacementModifierBeta> MODIFIER_CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					Codec.INT.fieldOf("count").forGetter(arg -> arg.count),
					Codec.DOUBLE.fieldOf("extra_chance").forGetter(arg -> arg.extraChance),
					Codec.INT.fieldOf("extra_count").forGetter(arg -> arg.extraCount)
			).apply(instance, NoiseBasedCountPlacementModifierBeta::of));

	protected NoiseBasedCountPlacementModifierBeta(int count, double extraChance, int extraCount) {
		super(count, extraChance, extraCount);
	}

	public static NoiseBasedCountPlacementModifierBeta of(int count, double extraChance, int extraCount) {
		return new NoiseBasedCountPlacementModifierBeta(count, extraChance, extraCount);
	}

	@Override
	public void setOctaves(PerlinOctaveNoise octaves) {
		this.noiseDecorator = new NoiseBasedCountBeta(octaves);
	}

	@Override
	public PlacementModifierType<?> type() {
		return ModernBetaPlacementTypes.BETA_NOISE_BASED_COUNT.get();
	}

}
