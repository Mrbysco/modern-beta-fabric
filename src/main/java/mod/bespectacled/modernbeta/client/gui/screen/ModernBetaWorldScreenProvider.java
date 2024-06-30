package mod.bespectacled.modernbeta.client.gui.screen;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsChunk;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class ModernBetaWorldScreenProvider {
	public static WorldCreationContext.DimensionsUpdater createModifier(
			CompoundTag chunkSettings,
			CompoundTag biomeSettings,
			CompoundTag caveBiomeSettings
	) {
		return (dynamicRegistryManager, dimensionsRegistryHolder) -> {
			ModernBetaSettingsChunk modernBetaSettingsChunk = ModernBetaSettingsChunk.fromCompound(chunkSettings);
			ResourceKey<NoiseGeneratorSettings> modernBetaSettings = keyOfSettings(modernBetaSettingsChunk.chunkProvider);

			Registry<NoiseGeneratorSettings> registrySettings = dynamicRegistryManager.registryOrThrow(Registries.NOISE_SETTINGS);
			Holder.Reference<NoiseGeneratorSettings> settings = registrySettings.getHolderOrThrow(modernBetaSettings);
			HolderGetter<Biome> registryBiome = dynamicRegistryManager.lookupOrThrow(Registries.BIOME);

			ModernBetaChunkGenerator chunkGenerator = new ModernBetaChunkGenerator(
					new ModernBetaBiomeSource(
							registryBiome,
							biomeSettings,
							caveBiomeSettings
					),
					settings,
					chunkSettings
			);

			return dimensionsRegistryHolder.replaceOverworldGenerator(dynamicRegistryManager, chunkGenerator);
		};
	}

	private static ResourceKey<NoiseGeneratorSettings> keyOfSettings(String id) {
		return ResourceKey.create(Registries.NOISE_SETTINGS, ModernBeta.createId(id));
	}
}
