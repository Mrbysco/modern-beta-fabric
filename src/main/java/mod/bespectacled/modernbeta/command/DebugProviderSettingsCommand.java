package mod.bespectacled.modernbeta.command;

import com.mojang.brigadier.CommandDispatcher;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsBiome;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsCaveBiome;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsChunk;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

public class DebugProviderSettingsCommand {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("printprovidersettings")
				.requires(source -> source.hasPermission(2))
				.executes(ctx -> execute(ctx.getSource()))
		);
	}

	private static int execute(CommandSourceStack source) {
		boolean validWorld = false;

		if (source.getLevel().getChunkSource().getGenerator() instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
			validWorld = true;

			StringBuilder builder = new StringBuilder();
			CompoundTag chunkSettings = ModernBetaSettingsChunk.fromCompound(modernBetaChunkGenerator.getChunkSettings()).toCompound();

			chunkSettings.getAllKeys().forEach(key -> {
				builder.append(String.format("* %s: %s\n", key, chunkSettings.get(key).toString()));
			});

			source.sendSuccess(() -> Component.literal("Chunk Provider Settings:").withStyle(ChatFormatting.YELLOW), false);
			source.sendSuccess(() -> Component.literal(builder.toString()), false);
		}

		if (source.getLevel().getChunkSource().getGenerator().getBiomeSource() instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
			validWorld = true;

			StringBuilder builder0 = new StringBuilder();
			CompoundTag biomeSettings = ModernBetaSettingsBiome.fromCompound(modernBetaBiomeSource.getBiomeSettings()).toCompound();

			biomeSettings.getAllKeys().forEach(key -> {
				builder0.append(String.format("* %s: %s\n", key, biomeSettings.get(key).toString()));
			});

			source.sendSuccess(() -> Component.literal("Biome Provider Settings:").withStyle(ChatFormatting.YELLOW), false);
			source.sendSuccess(() -> Component.literal(builder0.toString()), false);

			StringBuilder builder1 = new StringBuilder();
			CompoundTag caveBiomeSettings = ModernBetaSettingsCaveBiome.fromCompound(modernBetaBiomeSource.getCaveBiomeSettings()).toCompound();

			caveBiomeSettings.getAllKeys().forEach(key -> {
				builder1.append(String.format("* %s: %s\n", key, caveBiomeSettings.get(key).toString()));
			});

			source.sendSuccess(() -> Component.literal("Cave Biome Provider Settings:").withStyle(ChatFormatting.YELLOW), false);
			source.sendSuccess(() -> Component.literal(builder1.toString()), false);
		}

		if (validWorld) {
			return 0;
		}

		source.sendSuccess(() -> Component.literal("Not a Modern Beta world!").withStyle(ChatFormatting.RED), false);

		return -1;
	}
}
