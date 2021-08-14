package com.bespectacled.modernbeta.world.spawn;

import java.util.Optional;
import java.util.Random;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.gen.ChunkProvider;
import com.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.util.BlockStates;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;

/*
 * Port of Beta 1.7.3 player spawn locator.
 * 
 */
public class BeachSpawnLocator implements SpawnLocator {
    private final Random rand;
    
    private final ChunkProvider chunkProvider;
    private final PerlinOctaveNoise beachNoiseOctaves;
    
    public BeachSpawnLocator(ChunkProvider chunkProvider, PerlinOctaveNoise beachNoiseOctaves) {
        this.rand = new Random();
        
        this.chunkProvider = chunkProvider;
        this.beachNoiseOctaves = beachNoiseOctaves;
    }

    @Override
    public Optional<BlockPos> locateSpawn() {
        ModernBeta.log(Level.INFO, "Setting a beach spawn..");
        
        int x = 0;
        int z = 0;
        int attempts = 0;
        
        while(!this.isSandAt(x, z, null)) {
            if (attempts > 10000) {
                ModernBeta.log(Level.INFO, "Exceeded spawn attempts, spawning anyway at 0,0..");
                
                x = 0;
                z = 0;
                break;
            }
            
            x += this.rand.nextInt(64) - this.rand.nextInt(64);
            z += this.rand.nextInt(64) - this.rand.nextInt(64);
            
            attempts++;
        }
        
        int y = this.chunkProvider.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, null);
        
        return Optional.of(new BlockPos(x, y - 1, z));
    }
    
    private boolean isSandAt(int x, int z, HeightLimitView world) {
        double eighth = 0.03125D;
        int seaLevel = this.chunkProvider.getSeaLevel();
        
        int y = this.chunkProvider.getHeight(x, z, Heightmap.Type.OCEAN_FLOOR_WG, world);
        Biome biome = this.chunkProvider.getBiomeForNoiseGen(x >> 2, 0, z >> 2);
        
        return 
            (biome.getGenerationSettings().getSurfaceConfig().getTopMaterial().equals(BlockStates.SAND) && y >= seaLevel - 1) || 
            (this.beachNoiseOctaves.sample(x * eighth, z * eighth, 0.0) > 0.0 && y > seaLevel - 1 && y <= seaLevel + 1);
    }

}
