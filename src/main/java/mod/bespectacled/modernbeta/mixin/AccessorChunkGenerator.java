package mod.bespectacled.modernbeta.mixin;

import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ChunkGenerator.class)
public interface AccessorChunkGenerator {
	@Accessor("featuresPerStep")
	public Supplier<List<FeatureSorter.StepFeatureData>> getIndexedFeaturesListSupplier();
}
