package mod.bespectacled.modernbeta.data;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomes;
import mod.bespectacled.modernbeta.world.carver.configured.ModernBetaConfiguredCarvers;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGeneratorSettings;
import mod.bespectacled.modernbeta.world.feature.configured.ModernBetaConfiguredFeatures;
import mod.bespectacled.modernbeta.world.feature.placed.ModernBetaPlacedFeatures;
import mod.bespectacled.modernbeta.world.preset.ModernBetaWorldPresets;
import mod.bespectacled.modernbeta.world.structure.ModernBetaStructureSets;
import mod.bespectacled.modernbeta.world.structure.ModernBetaStructures;
import net.minecraft.core.Cloner;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ModernBetaDataGenerator {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		CompletableFuture<HolderLookup.Provider> provider = CompletableFuture.supplyAsync(() -> ModernBetaDataGenerator.getProvider().full());
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(packOutput,
					CompletableFuture.supplyAsync(ModernBetaDataGenerator::getProvider), Set.of(ModernBeta.MOD_ID)));
			generator.addProvider(event.includeServer(), new ModernBetaBiomeTagProvider(packOutput, provider, helper));
			generator.addProvider(event.includeServer(), new ModernBetaBlockTagProvider(packOutput, lookupProvider, helper));
			generator.addProvider(event.includeServer(), new ModernBetaWorldPresetTagProvider(packOutput, provider, helper));
			generator.addProvider(event.includeServer(), new ModernBetaStructureTagProvider(packOutput, provider, helper));
		}
	}

	private static RegistrySetBuilder.PatchedRegistries getProvider() {
		final RegistrySetBuilder registryBuilder = new RegistrySetBuilder();
		registryBuilder.add(Registries.PLACED_FEATURE, ModernBetaPlacedFeatures::bootstrap);
		registryBuilder.add(Registries.CONFIGURED_FEATURE, ModernBetaConfiguredFeatures::bootstrap);
		registryBuilder.add(Registries.CONFIGURED_CARVER, ModernBetaConfiguredCarvers::bootstrap);
		registryBuilder.add(Registries.BIOME, ModernBetaBiomes::bootstrap);
		registryBuilder.add(Registries.NOISE_SETTINGS, ModernBetaChunkGeneratorSettings::bootstrap);
		registryBuilder.add(Registries.STRUCTURE, ModernBetaStructures::bootstrap);
		registryBuilder.add(Registries.STRUCTURE_SET, ModernBetaStructureSets::bootstrap);
		registryBuilder.add(Registries.WORLD_PRESET, ModernBetaWorldPresets::bootstrap);
		RegistryAccess.Frozen regAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
		Cloner.Factory cloner$factory = new Cloner.Factory();
		net.neoforged.neoforge.registries.DataPackRegistriesHooks.getDataPackRegistriesWithDimensions().forEach(p_311524_ -> p_311524_.runWithArguments(cloner$factory::addCodec));
		return registryBuilder.buildPatch(regAccess, VanillaRegistries.createLookup(), cloner$factory);
	}
}
