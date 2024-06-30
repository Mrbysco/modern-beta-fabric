package mod.bespectacled.modernbeta.world.carver;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;

import java.util.Optional;

public class BetaCaveCarverConfig extends CaveCarverConfiguration {
	public static final Codec<BetaCaveCarverConfig> CAVE_CODEC = RecordCodecBuilder.create(instance ->
			fillConfigFields(instance).and(instance.group(
					Codec.BOOL.optionalFieldOf("use_fixed_caves").forGetter(config -> config.useFixedCaves),
					Codec.BOOL.optionalFieldOf("use_aquifers").forGetter(config -> config.useAquifers))
			).apply(instance, BetaCaveCarverConfig::new));

	public final Optional<Boolean> useFixedCaves;
	public final Optional<Boolean> useAquifers;

	public BetaCaveCarverConfig(
			float probability,
			HeightProvider y,
			FloatProvider yScale,
			VerticalAnchor lavaLevel,
			CarverDebugSettings debugConfig,
			HolderSet<Block> replaceable,
			FloatProvider horizontalRadiusMultiplier,
			FloatProvider verticalRadiusMultiplier,
			FloatProvider floorLevel,
			Optional<Boolean> useFixedCaves,
			Optional<Boolean> useAquifers
	) {
		super(
				probability,
				y,
				yScale,
				lavaLevel,
				debugConfig,
				replaceable,
				horizontalRadiusMultiplier,
				verticalRadiusMultiplier,
				floorLevel
		);

		this.useFixedCaves = useFixedCaves;
		this.useAquifers = useAquifers;
	}

	public BetaCaveCarverConfig(
			CarverConfiguration config,
			FloatProvider horizontalRadiusMultiplier,
			FloatProvider verticalRadiusMultiplier,
			FloatProvider floorLevel,
			Optional<Boolean> useFixedCaves,
			Optional<Boolean> useAquifers
	) {
		this(
				config.probability,
				config.y,
				config.yScale,
				config.lavaLevel,
				config.debugSettings,
				config.replaceable,
				horizontalRadiusMultiplier,
				verticalRadiusMultiplier,
				floorLevel,
				useFixedCaves,
				useAquifers
		);
	}

	private static <P extends CaveCarverConfiguration> Products.P4<RecordCodecBuilder.Mu<P>, CarverConfiguration, FloatProvider, FloatProvider, FloatProvider> fillConfigFields(RecordCodecBuilder.Instance<P> instance) {
		return instance.group(
				CarverConfiguration.CODEC.forGetter(config -> config),
				FloatProvider.CODEC.fieldOf("horizontal_radius_multiplier").forGetter(config -> config.horizontalRadiusMultiplier),
				FloatProvider.CODEC.fieldOf("vertical_radius_multiplier").forGetter(config -> config.verticalRadiusMultiplier),
				FloatProvider.codec(-1.0f, 1.0f).fieldOf("floor_level").forGetter(config -> config.floorLevel)
		);
	}
}
