package mod.bespectacled.modernbeta.mixin;

import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PieceGeneratorSupplier.Context.class)
public abstract class MixinStructureGeneratorFactoryContext {
    /*
    @Shadow private ChunkGenerator comp_306;
    @Shadow private ChunkPos comp_309;
    @Shadow private HeightLimitView comp_311;
    @Shadow private Predicate<RegistryEntry<Biome>> comp_312;
    @Shadow private NoiseConfig noiseConfig;
    
    @Inject(method = "isBiomeValid", at = @At("HEAD"), cancellable = true)
    private void injectCheckForBiomeOnTop(Heightmap.Type heightmap, CallbackInfoReturnable<Boolean> info) {
        if (this.comp_306 instanceof ModernBetaChunkGenerator chunkGenerator) {
            int x = this.comp_309.getCenterX();
            int z = this.comp_309.getCenterZ();
            int y = this.comp_306.getHeightInGround(x, z, heightmap, this.comp_311, this.noiseConfig);
            
            RegistryEntry<Biome> biome = chunkGenerator.getInjectedBiomeAtBlock(x, y, z, this.noiseConfig.getMultiNoiseSampler());
            
            info.setReturnValue(this.comp_312.test(biome));
        }
    }
    */
}
