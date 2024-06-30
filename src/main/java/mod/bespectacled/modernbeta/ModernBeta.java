package mod.bespectacled.modernbeta;

import mod.bespectacled.modernbeta.client.ClientHandler;
import mod.bespectacled.modernbeta.command.DebugProviderSettingsCommand;
import mod.bespectacled.modernbeta.compat.Compat;
import mod.bespectacled.modernbeta.config.ModernBetaConfig;
import mod.bespectacled.modernbeta.world.ModernBetaWorldInitializer;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSources;
import mod.bespectacled.modernbeta.world.carver.ModernBetaCarvers;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerators;
import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatures;
import mod.bespectacled.modernbeta.world.feature.placement.ModernBetaPlacementTypes;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

@Mod(ModernBeta.MOD_ID)
public class ModernBeta {
	public static final String MOD_ID = "modern_beta";
	public static final String MOD_NAME = "Modern Beta";

	public static final boolean DEV_ENV = !FMLEnvironment.production;

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ResourceLocation createId(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}

	public static void log(Level level, String message) {
		LOGGER.atLevel(level).log("[" + MOD_NAME + "] {}", message);
	}

	public static void log(String message) {
		log(Level.INFO, message);
	}

	public ModernBeta(IEventBus eventBus, Dist dist, ModContainer container) {
		log(Level.INFO, "Initializing Modern Beta...");
		container.registerConfig(ModConfig.Type.STARTUP, ModernBetaConfig.commonSpec);
		eventBus.register(ModernBetaConfig.class);

		// Register mod stuff
		ModernBetaPlacementTypes.PLACEMENT_MODIFIER_TYPES.register(eventBus);
		ModernBetaFeatures.FEATURES.register(eventBus);
		ModernBetaCarvers.WORLD_CARVERS.register(eventBus);

		ModernBetaBiomeSources.BIOME_SOURCES.register(eventBus);
		ModernBetaChunkGenerators.CHUNK_GENERATORS.register(eventBus);

		// Set up mod compatibility
		Compat.setupCompat();

		// Register default providers
		ModernBetaBuiltInProviders.registerChunkProviders();
		ModernBetaBuiltInProviders.registerBiomeProviders();
		ModernBetaBuiltInProviders.registerCaveBiomeProviders();
		ModernBetaBuiltInProviders.registerSurfaceConfigs();
		ModernBetaBuiltInProviders.registerNoisePostProcessors();
		ModernBetaBuiltInProviders.registerBlockSources();
		ModernBetaBuiltInProviders.registerSettingsPresets();
		ModernBetaBuiltInProviders.registerSettingsPresetAlts();

		if (dist.isClient()) {
			eventBus.addListener(ClientHandler::registerBlockColors);
			eventBus.addListener(ClientHandler::registerReloadListener);
		}

		if (DEV_ENV) {
			NeoForge.EVENT_BUS.addListener(this::onCommandRegister);
		}

		// Initializes chunk and biome providers at server start-up.
		NeoForge.EVENT_BUS.addListener(this::onServerStarting);
	}

	public void onCommandRegister(RegisterCommandsEvent event) {
		DebugProviderSettingsCommand.register(event.getDispatcher());
	}

	private void onServerStarting(ServerAboutToStartEvent event) {
		ModernBetaWorldInitializer.init(event.getServer());
	}
}
