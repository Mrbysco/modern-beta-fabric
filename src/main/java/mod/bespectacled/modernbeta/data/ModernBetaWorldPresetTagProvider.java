package mod.bespectacled.modernbeta.data;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.WorldPresetTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.WorldPresetTags;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModernBetaWorldPresetTagProvider extends WorldPresetTagsProvider {
	public static final ResourceKey<WorldPreset> MODERN_BETA = keyOf(ModernBeta.MOD_ID);

	public ModernBetaWorldPresetTagProvider(PackOutput output, CompletableFuture<Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, provider, ModernBeta.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider lookup) {
		tag(WorldPresetTags.NORMAL)
				.add(MODERN_BETA);
	}

	private static ResourceKey<WorldPreset> keyOf(String id) {
		return ResourceKey.create(Registries.WORLD_PRESET, ModernBeta.createId(ModernBeta.MOD_ID));
	}
}
