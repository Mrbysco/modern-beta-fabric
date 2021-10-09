package com.bespectacled.modernbeta.world.gen.provider;

import java.util.Random;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import com.bespectacled.modernbeta.api.world.gen.NoiseChunkProvider;
import com.bespectacled.modernbeta.noise.PerlinOctaveNoise;
import com.bespectacled.modernbeta.noise.SimplexNoise;
import com.bespectacled.modernbeta.util.BlockColumnHolder;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.NbtTags;
import com.bespectacled.modernbeta.util.NbtUtil;
import com.bespectacled.modernbeta.util.chunk.HeightmapChunk;
import com.bespectacled.modernbeta.world.biome.OldBiomeSource;
import com.bespectacled.modernbeta.world.biome.beta.climate.BetaClimateSampler;
import com.bespectacled.modernbeta.world.gen.OldChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.AquiferSampler;

public class BetaIslandsChunkProvider extends NoiseChunkProvider {
    private final PerlinOctaveNoise minLimitNoiseOctaves;
    private final PerlinOctaveNoise maxLimitNoiseOctaves;
    private final PerlinOctaveNoise mainNoiseOctaves;
    private final PerlinOctaveNoise beachNoiseOctaves;
    private final PerlinOctaveNoise surfaceNoiseOctaves;
    private final PerlinOctaveNoise scaleNoiseOctaves;
    private final PerlinOctaveNoise depthNoiseOctaves;
    private final PerlinOctaveNoise forestNoiseOctaves;
    
    private final SimplexNoise islandNoise;
    
    private final boolean generateOuterIslands;
    private final int centerIslandRadius;
    private final float centerIslandFalloff;
    private final int centerOceanLerpDistance;
    private final int centerOceanRadius;
    private final float outerIslandNoiseScale;
    private final float outerIslandNoiseOffset;

    private final ClimateSampler climateSampler;
    
    public BetaIslandsChunkProvider(OldChunkGenerator chunkGenerator) {
        super(chunkGenerator);
        //super(chunkGenerator, 0, 128, 64, 50, 0, -10, BlockStates.STONE, BlockStates.WATER, 2, 1, 1.0, 1.0, 80, 160, -10, 3, 0, 15, 3, 0, false, false, false, false, false);
        
        // Noise Generators
        this.minLimitNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.maxLimitNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.mainNoiseOctaves = new PerlinOctaveNoise(rand, 8, true);
        this.beachNoiseOctaves = new PerlinOctaveNoise(rand, 4, true);
        this.surfaceNoiseOctaves = new PerlinOctaveNoise(rand, 4, true);
        this.scaleNoiseOctaves = new PerlinOctaveNoise(rand, 10, true);
        this.depthNoiseOctaves = new PerlinOctaveNoise(rand, 16, true);
        this.forestNoiseOctaves = new PerlinOctaveNoise(rand, 8, true);
        this.islandNoise = new SimplexNoise(rand);

        setForestOctaves(forestNoiseOctaves);
        
        // Get climate sampler from biome provider if exists,
        // else create new default Beta climate sampler.
        this.climateSampler = 
            chunkGenerator.getBiomeSource() instanceof OldBiomeSource oldBiomeSource &&
            oldBiomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler ?
                climateSampler :
                new BetaClimateSampler(chunkGenerator.getWorldSeed());
        
        // Beta Islands settings
        this.generateOuterIslands = NbtUtil.readBoolean(NbtTags.GEN_OUTER_ISLANDS, providerSettings, ModernBeta.GEN_CONFIG.islandGenConfig.generateOuterIslands);
        this.centerIslandRadius = NbtUtil.readInt(NbtTags.CENTER_ISLAND_RADIUS, providerSettings, ModernBeta.GEN_CONFIG.islandGenConfig.centerIslandRadius);
        this.centerIslandFalloff = NbtUtil.readFloat(NbtTags.CENTER_ISLAND_FALLOFF, providerSettings, ModernBeta.GEN_CONFIG.islandGenConfig.centerIslandFalloff);
        this.centerOceanLerpDistance = NbtUtil.readInt(NbtTags.CENTER_OCEAN_LERP_DIST, providerSettings, ModernBeta.GEN_CONFIG.islandGenConfig.centerOceanLerpDistance);
        this.centerOceanRadius = NbtUtil.readInt(NbtTags.CENTER_OCEAN_RADIUS, providerSettings, ModernBeta.GEN_CONFIG.islandGenConfig.centerOceanRadius);
        this.outerIslandNoiseScale = NbtUtil.readFloat(NbtTags.OUTER_ISLAND_NOISE_SCALE, providerSettings, ModernBeta.GEN_CONFIG.islandGenConfig.outerIslandNoiseScale);
        this.outerIslandNoiseOffset = NbtUtil.readFloat(NbtTags.OUTER_ISLAND_NOISE_OFFSET, providerSettings, ModernBeta.GEN_CONFIG.islandGenConfig.outerIslandNoiseOffset);     
    }
    
    @Override
    public void provideSurface(ChunkRegion region, Chunk chunk, OldBiomeSource biomeSource) {
        double eighth = 0.03125D;

        ChunkPos chunkPos = chunk.getPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;
        
        int bedrockFloor = this.minY + this.bedrockFloor;
        
        Random rand = this.createSurfaceRandom(chunkX, chunkZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        
        double[] sandNoise = this.createSurfaceArray();
        double[] gravelNoise = this.createSurfaceArray();
        double[] surfaceNoise = this.createSurfaceArray();

        sandNoise = beachNoiseOctaves.sampleArrShelf(
            sandNoise, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            eighth, eighth, 1.0D);
        
        gravelNoise = beachNoiseOctaves.sampleArrShelf(
            gravelNoise, 
            chunkX * 16, 109.0134D, chunkZ * 16, 
            16, 1, 16, 
            eighth, 1.0D, eighth);
        
        surfaceNoise = surfaceNoiseOctaves.sampleArrShelf(
            surfaceNoise, 
            chunkX * 16, chunkZ * 16, 0.0D, 
            16, 16, 1,
            eighth * 2D, eighth * 2D, eighth * 2D
        );
        
        AquiferSampler aquiferSampler = this.createAquiferSampler(this.noiseMinY, this.noiseTopY, chunkPos);
        HeightmapChunk heightmapChunk = this.getHeightmapChunk(chunkX, chunkZ);
        BlockColumnHolder blockColumn = new BlockColumnHolder(chunk);

        for (int localZ = 0; localZ < 16; localZ++) {
            for (int localX = 0; localX < 16; localX++) {
                int x = (chunkX << 4) + localX;
                int z = (chunkZ << 4) + localZ;
                int topY = GenUtil.getLowestSolidHeight(chunk, this.worldHeight, this.minY, localX, localZ, this.defaultFluid) + 1;
                int surfaceMinY = (this.generateNoiseCaves || this.generateNoodleCaves) ? 
                    heightmapChunk.getHeight(x, z, HeightmapChunk.Type.SURFACE_FLOOR) - 8 : 
                    this.minY;
                
                boolean genSandBeach = sandNoise[localZ + localX * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean genGravelBeach = gravelNoise[localZ + localX * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;
                int surfaceDepth = (int) (surfaceNoise[localZ + localX * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                
                int flag = -1;
                
                Biome biome = biomeSource.getBiomeForSurfaceGen(region, mutable.set(x, topY, z));

                BlockState biomeTopBlock = biome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = biome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();

                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;
                
                boolean usedCustomSurface = this.useCustomSurfaceBuilder(biome, biomeSource.getBiomeRegistry().getId(biome), region, chunk, rand, mutable, blockColumn);
                
                // Generate from top to bottom of world
                for (int y = this.worldTopY - 1; y >= this.minY; y--) {
                    mutable.set(localX, y, localZ);

                    // Randomly place bedrock from y=0 (or minHeight) to y=5
                    if (y <= bedrockFloor + rand.nextInt(5)) {
                        chunk.setBlockState(mutable, BlockStates.BEDROCK, false);
                        continue;
                    }
                    
                    // Skip if at surface min y
                    if (y < surfaceMinY) {
                        continue;
                    }
                    
                    // Skip if used custom surface generation.
                    if (usedCustomSurface) {
                        continue;
                    }

                    BlockState blockState = chunk.getBlockState(mutable);

                    if (blockState.isAir()) { // Skip if air block
                        flag = -1;
                        continue;
                    }

                    if (!blockState.isOf(this.defaultBlock.getBlock())) { // Skip if not stone
                        continue;
                    }
                    
                    // At the first default block
                    if (flag == -1) {
                        if (surfaceDepth <= 0) { // Generate stone basin if noise permits
                            topBlock = BlockStates.AIR;
                            fillerBlock = this.defaultBlock;
                            
                        } else if (y >= this.seaLevel - 4 && y <= this.seaLevel + 1) { // Generate beaches at this y range
                            topBlock = biomeTopBlock;
                            fillerBlock = biomeFillerBlock;

                            if (genGravelBeach) {
                                topBlock = BlockStates.AIR; // This reduces gravel beach height by 1
                                fillerBlock = BlockStates.GRAVEL;
                            }

                            if (genSandBeach) {
                                topBlock = BlockStates.SAND;
                                fillerBlock = BlockStates.SAND;
                            }
                        }

                        flag = surfaceDepth;
                        
                        if (y < this.seaLevel && topBlock.isAir()) { // Generate water bodies
                            BlockState fluidBlock = aquiferSampler.apply(x, y, z, 0.0, 0.0);
                            
                            boolean isAir = fluidBlock == null;
                            topBlock = isAir ? BlockStates.AIR : fluidBlock;
                        }
                        
                        blockState = (y >= this.seaLevel - 1) ? 
                            topBlock : 
                            fillerBlock;
                        
                        chunk.setBlockState(mutable, blockState, false);

                        continue;
                    }

                    if (flag <= 0) {
                        continue;
                    }

                    flag--;
                    chunk.setBlockState(mutable, fillerBlock, false);

                    // Generates layer of sandstone starting at lowest block of sand, of height 1 to 4.
                    if (flag == 0 && fillerBlock.isOf(Blocks.SAND)) {
                        flag = rand.nextInt(4);
                        fillerBlock = BlockStates.SANDSTONE;
                    }
                }
            }
        }
    }

    @Override
    protected void sampleNoiseColumn(double[] primaryBuffer, double[] heightmapBuffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ) {
        int horizNoiseResolution = 16 / (this.noiseSizeX + 1);
        int x = (startNoiseX / this.noiseSizeX * 16) + localNoiseX * horizNoiseResolution + horizNoiseResolution / 2;
        int z = (startNoiseZ / this.noiseSizeZ * 16) + localNoiseZ * horizNoiseResolution + horizNoiseResolution / 2;
        
        int noiseX = startNoiseX + localNoiseX;
        int noiseZ = startNoiseZ + localNoiseZ;
        
        double depthNoiseScaleX = 200D;
        double depthNoiseScaleZ = 200D;
        
        //double baseSize = noiseResolutionY / 2D; // Or: 17 / 2D = 8.5
        double baseSize = 8.5D;
        
        double temp = this.climateSampler.sampleTemp(x, z);
        double rain = this.climateSampler.sampleRain(x, z) * temp;
        
        rain = 1.0D - rain;
        rain *= rain;
        rain *= rain;
        rain = 1.0D - rain;
    
        double scaleNoise = this.scaleNoiseOctaves.sample(noiseX, noiseZ, 1.121D, 1.121D);
        scaleNoise = (scaleNoise + 256D) / 512D;
        scaleNoise *= rain;
        
        if (scaleNoise > 1.0D) {
            scaleNoise = 1.0D;
        }
        
        double depthNoise = this.depthNoiseOctaves.sample(noiseX, noiseZ, depthNoiseScaleX, depthNoiseScaleZ);
        depthNoise /= 8000D;
    
        if (depthNoise < 0.0D) {
            depthNoise = -depthNoise * 0.3D;
        }
    
        depthNoise = depthNoise * 3D - 2D;
    
        if (depthNoise < 0.0D) {
            depthNoise /= 2D;
    
            if (depthNoise < -1D) {
                depthNoise = -1D;
            }
    
            depthNoise /= 1.4D;
            depthNoise /= 2D;
    
            scaleNoise = 0.0D;
    
        } else {
            if (depthNoise > 1.0D) {
                depthNoise = 1.0D;
            }
            depthNoise /= 8D;
        }
    
        if (scaleNoise < 0.0D) {
            scaleNoise = 0.0D;
        }
    
        scaleNoise += 0.5D;
        //depthVal = (depthVal * (double) noiseResolutionY) / 16D;
        //double depthVal2 = (double) noiseResolutionY / 2D + depthVal * 4D;
        depthNoise = depthNoise * baseSize / 8D;
        depthNoise = baseSize + depthNoise * 4D;
        
        double scale = scaleNoise;
        double depth = depthNoise;
        double islandOffset = this.generateIslandOffset(startNoiseX, startNoiseZ, localNoiseX, localNoiseZ);
        
        for (int y = 0; y < primaryBuffer.length; ++y) {
            int noiseY = y + this.noiseMinY;
            
            // Var names taken from old customized preset names
            double coordinateScale = 684.412D * this.xzScale; 
            double heightScale = 684.412D * this.yScale;
            
            double mainNoiseScaleX = this.xzFactor; // Default: 80
            double mainNoiseScaleY = this.yFactor;  // Default: 160
            double mainNoiseScaleZ = this.xzFactor;
    
            double lowerLimitScale = 512D;
            double upperLimitScale = 512D;
            
            double heightStretch = 12D;
            
            double density = 0.0D;
            double densityOffset = this.getOffset(noiseY, heightStretch, depth, scale);

            // Equivalent to current MC noise.sample() function, see NoiseColumnSampler.
            double mainNoise = (this.mainNoiseOctaves.sample(
                    noiseX, noiseY, noiseZ, 
                    coordinateScale / mainNoiseScaleX, 
                    heightScale / mainNoiseScaleY, 
                    coordinateScale / mainNoiseScaleZ
                ) / 10D + 1.0D) / 2D;
                
            if (mainNoise < 0.0D) {
                density = this.minLimitNoiseOctaves.sample(
                    noiseX, noiseY, noiseZ, 
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / lowerLimitScale;
                
            } else if (mainNoise > 1.0D) {
                density = this.maxLimitNoiseOctaves.sample(
                    noiseX, noiseY, noiseZ, 
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / upperLimitScale;
                
            } else {
                double minLimitNoise = this.minLimitNoiseOctaves.sample(
                    noiseX, noiseY, noiseZ, 
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / lowerLimitScale;
                
                double maxLimitNoise = this.maxLimitNoiseOctaves.sample(
                    noiseX, noiseY, noiseZ, 
                    coordinateScale, 
                    heightScale, 
                    coordinateScale
                ) / upperLimitScale;
                
                density = minLimitNoise + (maxLimitNoise - minLimitNoise) * mainNoise;
            }
            
            // Equivalent to current MC addition of density offset, see NoiseColumnSampler.
            density -= densityOffset; 
            
            // Add island offset
            density += islandOffset;
            
            // Sample without noise caves
            double heightmapDensity = density;
            
            // Sample for noise caves
            density = this.sampleNoiseCave(density, 170.0, noiseX, noiseY, noiseZ);
            
            // Apply slides
            density = this.applyTopSlide(density, noiseY, 4);
            density = this.applyBottomSlide(density, noiseY, -3);
            
            heightmapDensity = this.applyTopSlide(heightmapDensity, noiseY, 4);
            heightmapDensity = this.applyBottomSlide(heightmapDensity, noiseY, -3);
            
            primaryBuffer[y] = density;
            heightmapBuffer[y] = heightmapDensity;
        }
    }

    private double generateIslandOffset(int startNoiseX, int startNoiseZ, int curNoiseX, int curNoiseZ) {
        float noiseX = curNoiseX + startNoiseX;
        float noiseZ = curNoiseZ + startNoiseZ;
        
        float oceanDepth = 200.0F;
        
        int centerOceanLerpDistance = this.centerOceanLerpDistance * this.noiseSizeX;
        int centerOceanRadius = this.centerOceanRadius * this.noiseSizeX;
        
        float centerIslandRadius = this.centerIslandRadius * this.noiseSizeX;
        
        float outerIslandNoiseScale = this.outerIslandNoiseScale;
        float outerIslandNoiseOffset = this.outerIslandNoiseOffset;
        
        float dist = noiseX * noiseX + noiseZ * noiseZ;
        float radius = MathHelper.sqrt(dist);
        
        float islandOffset = centerIslandRadius - radius; 
        if (islandOffset < 0.0) 
            islandOffset *= this.centerIslandFalloff; // Increase the rate of falloff past the island radius
        
        islandOffset = MathHelper.clamp(islandOffset, -oceanDepth, 0.0F);
            
        if (this.generateOuterIslands && radius > centerOceanRadius) {
            float islandAddition = (float)this.islandNoise.sample(noiseX / outerIslandNoiseScale, noiseZ / outerIslandNoiseScale, 1.0, 1.0) + outerIslandNoiseOffset;
            
            // 0.885539 = Simplex upper range, but scale a little higher to ensure island centers have untouched terrain.
            islandAddition /= 0.8F;
            islandAddition = MathHelper.clamp(islandAddition, 0.0F, 1.0F);
            
            // Interpolate noise addition so there isn't a sharp cutoff at start of ocean ring edge.
            islandAddition = (float)MathHelper.clampedLerp(0.0F, islandAddition, (radius - centerOceanRadius) / centerOceanLerpDistance);
            
            islandOffset += islandAddition * oceanDepth;
            islandOffset = MathHelper.clamp(islandOffset, -oceanDepth, 0.0F);
        }
        
        return islandOffset;
    }
    
    private double getOffset(int noiseY, double heightStretch, double depth, double scale) {
        double offset = (((double)noiseY - depth) * heightStretch) / scale;
        
        if (offset < 0D)
            offset *= 4D;
        
        return offset;
    }
}
