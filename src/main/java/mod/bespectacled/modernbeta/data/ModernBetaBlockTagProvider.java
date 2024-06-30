package mod.bespectacled.modernbeta.data;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModernBetaBlockTagProvider extends BlockTagsProvider {
	public static final TagKey<Block> OVERWORLD_CARVER_REPLACEABLES = keyOf("overworld_carver_replaceables");

	public ModernBetaBlockTagProvider(PackOutput output, CompletableFuture<Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, ModernBeta.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider lookup) {
		tag(OVERWORLD_CARVER_REPLACEABLES).add(
				Blocks.STONE,
				Blocks.COBBLESTONE,
				Blocks.DIRT,
				Blocks.GRASS_BLOCK,
				Blocks.DEEPSLATE,
				Blocks.TUFF,
				Blocks.GRANITE,
				Blocks.IRON_ORE,
				Blocks.DEEPSLATE_IRON_ORE,
				Blocks.RAW_IRON_BLOCK,
				Blocks.COPPER_ORE,
				Blocks.DEEPSLATE_COPPER_ORE,
				Blocks.RAW_COPPER_BLOCK,
				Blocks.COAL_ORE,
				Blocks.DEEPSLATE_COAL_ORE,
				Blocks.COAL_BLOCK
		);
	}

	private static TagKey<Block> keyOf(String id) {
		return TagKey.create(Registries.BLOCK, ModernBeta.createId(id));
	}
}
