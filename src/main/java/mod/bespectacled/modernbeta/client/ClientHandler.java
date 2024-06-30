package mod.bespectacled.modernbeta.client;

import mod.bespectacled.modernbeta.client.color.BlockColorSampler;
import mod.bespectacled.modernbeta.client.resource.ModernBetaColormapResource;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public class ClientHandler {
	public static void registerReloadListener(RegisterClientReloadListenersEvent event) {
		event.registerReloadListener(new ModernBetaColormapResource(
				"textures/colormap/water.png",
				BlockColorSampler.INSTANCE.colormapWater::setColormap
		));

		event.registerReloadListener(new ModernBetaColormapResource(
				"textures/colormap/underwater.png",
				BlockColorSampler.INSTANCE.colormapUnderwater::setColormap
		));
	}

	public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		// Grass blocks
		event.register(
				BlockColorSampler.INSTANCE::getGrassColor,
				Blocks.GRASS_BLOCK
		);

		// Tall grass blocks
		event.register(
				BlockColorSampler.INSTANCE::getTallGrassColor,
				Blocks.FERN,
				Blocks.SHORT_GRASS,
				Blocks.POTTED_FERN,
				Blocks.TALL_GRASS,
				Blocks.LARGE_FERN
		);

		// Foliage blocks
		event.register(
				BlockColorSampler.INSTANCE::getFoliageColor,
				Blocks.OAK_LEAVES,
				Blocks.JUNGLE_LEAVES,
				Blocks.ACACIA_LEAVES,
				Blocks.DARK_OAK_LEAVES,
				Blocks.VINE
		);

		// Sugar cane
		event.register(
				BlockColorSampler.INSTANCE::getSugarCaneColor,
				Blocks.SUGAR_CANE
		);

		// Water blocks
		event.register(
				BlockColorSampler.INSTANCE::getWaterColor,
				Blocks.WATER,
				Blocks.BUBBLE_COLUMN,
				Blocks.WATER_CAULDRON
		);
	}
}
