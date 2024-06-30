package mod.bespectacled.modernbeta.api.world.chunk.noise;

import mod.bespectacled.modernbeta.settings.ModernBetaSettingsChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public interface NoisePostProcessor {
	public static final NoisePostProcessor DEFAULT = (noise, noiseX, noiseY, noiseZ, generatorSettings, chunkSettings) -> noise;

	double sample(double noise, int noiseX, int noiseY, int noiseZ, NoiseGeneratorSettings generatorSettings, ModernBetaSettingsChunk chunkSettings);
}
