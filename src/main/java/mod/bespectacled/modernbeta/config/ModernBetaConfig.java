package mod.bespectacled.modernbeta.config;

import mod.bespectacled.modernbeta.ModernBeta;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModernBetaConfig {
	public static class Common {
		public final ModConfigSpec.ConfigValue<? extends String> fixedSeed;
		public final ModConfigSpec.BooleanValue useFixedSeed;
		public final ModConfigSpec.BooleanValue useBetaSkyColor;
		public final ModConfigSpec.BooleanValue useBetaBiomeColor;
		public final ModConfigSpec.BooleanValue useBetaWaterColor;
		public final ModConfigSpec.BooleanValue usePEBetaSkyColor;
		public final ModConfigSpec.BooleanValue usePEBetaBiomeColor;
		public final ModConfigSpec.BooleanValue usePEBetaWaterColor;
		public final ModConfigSpec.BooleanValue useOldFogColor;

		Common(ModConfigSpec.Builder builder) {
			builder.comment("General settings")
					.push("General");

			fixedSeed = builder
					.comment("Fixed Seed [Default: \"\"]")
					.define("fixedSeed", "");

			useFixedSeed = builder
					.comment("Use Fixed Seed [Default: false]")
					.define("useFixedSeed", false);

			useBetaSkyColor = builder
					.comment("Use Beta Sky Color [Default: true]")
					.define("useBetaSkyColor", true);

			useBetaBiomeColor = builder
					.comment("Use Beta Biome Color [Default: true]")
					.define("useBetaBiomeColor", true);

			useBetaWaterColor = builder
					.comment("Use Beta Water Color [Default: false]")
					.define("useBetaWaterColor", false);

			usePEBetaSkyColor = builder
					.comment("Use PE Beta Sky Color [Default: false]")
					.define("usePEBetaSkyColor", false);

			usePEBetaBiomeColor = builder
					.comment("Use PE Beta Biome Color [Default: false]")
					.define("usePEBetaBiomeColor", false);

			usePEBetaWaterColor = builder
					.comment("Use PE Beta Water Color [Default: false]")
					.define("usePEBetaWaterColor", false);

			useOldFogColor = builder
					.comment("Use Old Fog Color [Default: true]")
					.define("useOldFogColor", true);

			builder.pop();
		}
	}

	public static final ModConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		ModernBeta.LOGGER.debug("Loaded Modern Beta's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		ModernBeta.LOGGER.debug("Modern Beta's config just got changed on the file system!");
	}
}
