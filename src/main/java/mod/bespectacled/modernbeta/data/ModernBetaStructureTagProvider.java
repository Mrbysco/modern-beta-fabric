package mod.bespectacled.modernbeta.data;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.world.structure.ModernBetaStructures;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraft.tags.StructureTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModernBetaStructureTagProvider extends StructureTagsProvider {
	public ModernBetaStructureTagProvider(PackOutput output, CompletableFuture<Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, provider, ModernBeta.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider lookup) {
		tag(StructureTags.EYE_OF_ENDER_LOCATED)
				.add(ModernBetaStructures.INDEV_STRONGHOLD);
	}
}
