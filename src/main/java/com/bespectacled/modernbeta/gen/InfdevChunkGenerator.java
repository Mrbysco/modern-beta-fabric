package com.bespectacled.modernbeta.gen;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import org.apache.logging.log4j.Level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.JigsawJunction;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.AlphaBiomeSource;
import com.bespectacled.modernbeta.biome.BetaBiomeSource;
import com.bespectacled.modernbeta.biome.InfdevBiomeSource;
import com.bespectacled.modernbeta.decorator.BetaDecorator;
import com.bespectacled.modernbeta.feature.BetaFeature;
import com.bespectacled.modernbeta.gen.settings.AlphaGeneratorSettings;
import com.bespectacled.modernbeta.gen.settings.InfdevGeneratorSettings;
import com.bespectacled.modernbeta.noise.*;
import com.bespectacled.modernbeta.util.BlockStates;
import com.bespectacled.modernbeta.util.GenUtil;
import com.bespectacled.modernbeta.util.MutableBiomeArray;
import com.bespectacled.modernbeta.util.IndevUtil.Type;

//private final BetaGeneratorSettings settings;

public class InfdevChunkGenerator extends NoiseChunkGenerator implements IOldChunkGenerator {

    public static final Codec<InfdevChunkGenerator> CODEC = RecordCodecBuilder.create(instance -> instance
            .group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable().forGetter(generator -> generator.worldSeed),
                    InfdevGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings))
            .apply(instance, instance.stable(InfdevChunkGenerator::new)));

    private final InfdevGeneratorSettings settings;
    private final InfdevBiomeSource biomeSource;
    private final long seed;
    private boolean generateVanillaBiomes = false;

    private final OldNoiseGeneratorOctaves noiseOctavesA;
    private final OldNoiseGeneratorOctaves noiseOctavesB;
    private final OldNoiseGeneratorOctaves noiseOctavesC;
    private final OldNoiseGeneratorOctaves beachNoiseOctaves;
    private final OldNoiseGeneratorOctaves stoneNoiseOctaves;
    private final OldNoiseGeneratorOctaves forestNoiseOctaves;

    // Block Y-height cache, from Beta+
    private static final Map<BlockPos, Integer> GROUND_CACHE_Y = new HashMap<>();
    private static final int[][] CHUNK_Y = new int[16][16];
    
    private static final double HEIGHTMAP[][] = new double[33][4];
    
    private static final Mutable POS = new Mutable();
    
    private static final Random RAND = new Random();
    private static final ChunkRandom FEATURE_RAND = new ChunkRandom();
    
    private static final ObjectList<StructurePiece> STRUCTURE_LIST = (ObjectList<StructurePiece>) new ObjectArrayList(10);
    private static final ObjectList<JigsawJunction> JIGSAW_LIST = (ObjectList<JigsawJunction>) new ObjectArrayList(32);

    public InfdevChunkGenerator(BiomeSource biomes, long seed, InfdevGeneratorSettings settings) {
        super(biomes, seed, () -> settings.wrapped);
        this.settings = settings;
        this.biomeSource = (InfdevBiomeSource) biomes;
        this.seed = seed;
        if (settings.settings.contains("generateVanillaBiomesInfdev")) 
            this.generateVanillaBiomes = settings.settings.getBoolean("generateVanillaBiomesInfdev");
        
        RAND.setSeed(seed);
        
        // Noise Generators
        noiseOctavesA = new OldNoiseGeneratorOctaves(RAND, 16, false);
        noiseOctavesB = new OldNoiseGeneratorOctaves(RAND, 16, false);
        noiseOctavesC = new OldNoiseGeneratorOctaves(RAND, 8, false);
        beachNoiseOctaves = new OldNoiseGeneratorOctaves(RAND, 4, false);
        stoneNoiseOctaves = new OldNoiseGeneratorOctaves(RAND, 4, false);

        new OldNoiseGeneratorOctaves(RAND, 5, false); // Unused in original source
        
        forestNoiseOctaves = new OldNoiseGeneratorOctaves(RAND, 5, false);

        // Yes this is messy. What else am I supposed to do?
        BetaDecorator.COUNT_INFDEV_NOISE_DECORATOR.setOctaves(forestNoiseOctaves);
        
        GROUND_CACHE_Y.clear();
    }

    public static void register() {
        Registry.register(Registry.CHUNK_GENERATOR, new Identifier(ModernBeta.ID, "infdev"), CODEC);
        //ModernBeta.LOGGER.log(Level.INFO, "Registered Infdev chunk generator.");
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return InfdevChunkGenerator.CODEC;
    }

    @Override
    public void populateNoise(WorldAccess worldAccess, StructureAccessor structureAccessor, Chunk chunk) {
        RAND.setSeed((long) chunk.getPos().x * 341873128712L + (long) chunk.getPos().z * 132897987541L);

        generateTerrain(chunk, structureAccessor);
        
        if (this.generateVanillaBiomes) {
            MutableBiomeArray mutableBiomes = MutableBiomeArray.inject(chunk.getBiomeArray());
            ChunkPos pos = chunk.getPos();
            BlockState blockState;
            Biome biome;
            
            // Replace biomes in bodies of water at least four deep with ocean biomes
            for (int x = 0; x < 4; x++) {
                
                for (int z = 0; z < 4; z++) {
                    int absX = pos.getStartX() + (x << 2);
                    int absZ = pos.getStartZ() + (z << 2);

                    // Assign ocean biomes
                    //POS.set(absX, this.getSeaLevel() - 4, absZ);
                    //blockState = chunk.getBlockState(POS);
                    
                    int y = GenUtil.getSolidHeight(chunk, absX, absZ);

                    if (y < 60) {
                        //biome = biomeSource.getLayeredBiomeForNoiseGen(absX, 0, absZ, BiomeType.OCEAN);
                        biome = biomeSource.getOceanBiomeForNoiseGen(absX >> 2, absZ >> 2);
                        
                        mutableBiomes.setBiome(absX, 0, absZ, biome);
                    }
                    
                    /*
                    if (this.generateVanillaBiomes) {
                        // Assign beach biomes
                        int y = this.getHeight(absX, absZ, Type.OCEAN_FLOOR_WG) - 1;
                        biome = biomeSource.getLayeredBiomeForNoiseGen(absX, 0, absZ, BiomeType.LAND);
                        
                        POS.set(absX, y, absZ);
                        blockState = chunk.getBlockState(POS);
                        
                        if (y < 67 && (blockState.isOf(Blocks.SAND) || blockState.isOf(Blocks.GRAVEL)) && biome.getCategory() != Category.DESERT) {
                            biome = biomeSource.getLayeredBiomeForNoiseGen(absX, 0, absZ, BiomeType.BEACH);
                            
                            mutableBiomes.setBiome(absX, 0, absZ, biome);
                        }
                    }
                    */
                }   
            }
        }
        
        BetaFeature.OLD_FANCY_OAK.chunkReset();
    }
    
    // Modified to accommodate additional ocean biome replacements
    @Override
    public void generateFeatures(ChunkRegion chunkRegion, StructureAccessor structureAccessor) {
        int ctrX = chunkRegion.getCenterChunkX();
        int ctrZ = chunkRegion.getCenterChunkZ();
        int ctrAbsX = ctrX * 16;
        int ctrAbsZ = ctrZ * 16;

        Chunk ctrChunk = chunkRegion.getChunk(ctrX, ctrZ);
        
        Biome biome = GenUtil.getOceanBiome(ctrChunk, this, biomeSource);

        long popSeed = FEATURE_RAND.setPopulationSeed(chunkRegion.getSeed(), ctrAbsX, ctrAbsZ);
        
        try {
            biome.generateFeatureStep(structureAccessor, this, chunkRegion, popSeed, FEATURE_RAND, new BlockPos(ctrAbsX, 0, ctrAbsZ));
        } catch (Exception exception) {
            CrashReport report = CrashReport.create(exception, "Biome decoration");
            report.addElement("Generation").add("CenterX", ctrX).add("CenterZ", ctrZ).add("Seed", popSeed).add("Biome", biome);
            throw new CrashException(report);
        }
    }
    
 // Modified to accommodate additional ocean biome replacements
    @Override
    public void setStructureStarts(
        DynamicRegistryManager dynamicRegistryManager, 
        StructureAccessor structureAccessor,   
        Chunk chunk, 
        StructureManager structureManager, 
        long seed
    ) {
        ChunkPos chunkPos = chunk.getPos();
        
        Biome biome = GenUtil.getOceanBiome(chunk, this, biomeSource);

        this.setStructureStart(ConfiguredStructureFeatures.STRONGHOLD, dynamicRegistryManager, structureAccessor, chunk,
                structureManager, seed, chunkPos, biome);
        
        for (final Supplier<ConfiguredStructureFeature<?, ?>> supplier : biome.getGenerationSettings()
                .getStructureFeatures()) {
            this.setStructureStart(supplier.get(), dynamicRegistryManager, structureAccessor, chunk, structureManager,
                    seed, chunkPos, biome);
        }
        
    }

    
    // Modified to accommodate additional ocean biome replacements
    private void setStructureStart(
        ConfiguredStructureFeature<?, ?> configuredStructureFeature,
        DynamicRegistryManager dynamicRegistryManager, 
        StructureAccessor structureAccessor, 
        Chunk chunk,
        StructureManager structureManager, 
        long long7, 
        ChunkPos chunkPos, 
        Biome biome
    ) {
        StructureStart<?> structureStart = structureAccessor.getStructureStart(ChunkSectionPos.from(chunk.getPos(), 0),
                configuredStructureFeature.feature, chunk);
        int refs = (structureStart != null) ? structureStart.getReferences() : 0;

        // StructureConfig structureConfig13 =
        // this.structuresConfig.getForType(configuredStructureFeature.feature);
        StructureConfig structureConfig = this.settings.wrapped.getStructuresConfig()
                .getForType(configuredStructureFeature.feature);

        if (structureConfig != null) {
            StructureStart<?> gotStart = configuredStructureFeature.tryPlaceStart(dynamicRegistryManager, this,
                    this.biomeSource, structureManager, long7, chunkPos, biome, refs, structureConfig);
            structureAccessor.setStructureStart(ChunkSectionPos.from(chunk.getPos(), 0),
                    configuredStructureFeature.feature, gotStart, chunk);
        }
    }


    @Override
    public void carve(long seed, BiomeAccess biomeAccess, Chunk chunk, GenerationStep.Carver carver) {
        BiomeAccess biomeAcc = biomeAccess.withSource(this.biomeSource);
        ChunkPos chunkPos = chunk.getPos();

        int mainChunkX = chunkPos.x;
        int mainChunkZ = chunkPos.z;

        int biomeX = mainChunkX << 2;
        int biomeZ = mainChunkZ << 2;

        int absX = biomeX << 2;
        int absZ = biomeZ << 2;

        Biome biome = GenUtil.getOceanBiome(chunk, this, biomeSource);
        GenerationSettings genSettings = biome.getGenerationSettings();
        
        BitSet bitSet = ((ProtoChunk) chunk).getOrCreateCarvingMask(carver);
        
        RAND.setSeed(this.seed);
        long l = (RAND.nextLong() / 2L) * 2L + 1L;
        long l1 = (RAND.nextLong() / 2L) * 2L + 1L;
        
        for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
            for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
                List<Supplier<ConfiguredCarver<?>>> carverList = genSettings.getCarversForStep(carver);
                ListIterator<Supplier<ConfiguredCarver<?>>> carverIterator = carverList.listIterator();

                while (carverIterator.hasNext()) {
                    ConfiguredCarver<?> configuredCarver = carverIterator.next().get();

                    RAND.setSeed((long) chunkX * l + (long) chunkZ * l1 ^ seed);

                    if (configuredCarver.shouldCarve(RAND, chunkX, chunkZ)) {
                        configuredCarver.carve(chunk, biomeAcc::getBiome, RAND, this.getSeaLevel(), chunkX, chunkZ,
                                mainChunkX, mainChunkZ, bitSet);

                    }
                }
            }
        }
    }

    @Override
    public void buildSurface(ChunkRegion chunkRegion, Chunk chunk) {
        buildInfdevSurface(chunkRegion, chunk);
    }
     

    public void generateTerrain(Chunk chunk, StructureAccessor structureAccessor) {
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        Heightmap heightmapOCEAN = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmapSURFACE = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        
        GenUtil.collectStructures(chunk, structureAccessor, STRUCTURE_LIST, JIGSAW_LIST);
        
        ObjectListIterator<StructurePiece> structureListIterator = (ObjectListIterator<StructurePiece>) STRUCTURE_LIST.iterator();
        ObjectListIterator<JigsawJunction> jigsawListIterator = (ObjectListIterator<JigsawJunction>) JIGSAW_LIST.iterator();
        
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++ j) {
                int bX = (chunkX << 2) + i;
                int bZ = (chunkZ << 2) + j;
                
                for (int bY = 0; bY < HEIGHTMAP.length; ++bY) {
                    HEIGHTMAP[bY][0] = this.generateHeightmap(bX, bY, bZ);
                    HEIGHTMAP[bY][1] = this.generateHeightmap(bX, bY, bZ + 1);
                    HEIGHTMAP[bY][2] = this.generateHeightmap(bX + 1, bY, bZ);
                    HEIGHTMAP[bY][3] = this.generateHeightmap(bX + 1, bY, bZ + 1);
                }
                
                for (int bY = 0; bY < 32; ++bY) {
                    double n1 = HEIGHTMAP[bY][0];
                    double n2 = HEIGHTMAP[bY][1];
                    double n3 = HEIGHTMAP[bY][2];
                    double n4 = HEIGHTMAP[bY][3];
                    double n5 = HEIGHTMAP[bY + 1][0];
                    double n7 = HEIGHTMAP[bY + 1][1];
                    double n8 = HEIGHTMAP[bY + 1][2];
                    double n9 = HEIGHTMAP[bY + 1][3];
                    
                    for (int pY = 0; pY < 4; ++pY) {
                        double mixY = pY / 4.0;
                        
                        double nx1 = n1 + (n5 - n1) * mixY;
                        double nx2 = n2 + (n7 - n2) * mixY;
                        double nx3 = n3 + (n8 - n3) * mixY;
                        double nx4 = n4 + (n9 - n4) * mixY;
                        
                        int y = (bY << 2) + pY;
                        
                        for (int pX = 0; pX < 4; ++pX) {
                            double mixX = pX / 4.0;
                            
                            double nz1 = nx1 + (nx3 - nx1) * mixX;
                            double nz2 = nx2 + (nx4 - nx2) * mixX;
                            
                            int x = pX + (i << 2);
                            int z = 0 + (j << 2);
                            
                            for (int pZ = 0; pZ < 4; ++pZ) {
                                double mixZ = pZ / 4.0;
                                double density = nz1 + (nz2 - nz1) * mixZ;
                                
                                int absX = (chunk.getPos().x << 4) + x;
                                int absZ = (chunk.getPos().z << 4) + z;
                                
                                double clampedDensity = MathHelper.clamp(density / 200.0, -1.0, 1.0);
                                clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
                                
                                while (structureListIterator.hasNext()) {
                                    StructurePiece curStructurePiece = (StructurePiece) structureListIterator.next();
                                    BlockBox blockBox = curStructurePiece.getBoundingBox();

                                    int sX = Math.max(0, Math.max(blockBox.minX - absX, absX - blockBox.maxX));
                                    int sY = y - (blockBox.minY + ((curStructurePiece instanceof PoolStructurePiece)
                                            ? ((PoolStructurePiece) curStructurePiece).getGroundLevelDelta() : 0));
                                    int sZ = Math.max(0, Math.max(blockBox.minZ - absZ, absZ - blockBox.maxZ));

                                    clampedDensity += super.getNoiseWeight(sX, sY, sZ) * 0.8;
                                }
                                structureListIterator.back(STRUCTURE_LIST.size());

                                while (jigsawListIterator.hasNext()) {
                                    JigsawJunction curJigsawJunction = (JigsawJunction) jigsawListIterator.next();

                                    int jX = absX - curJigsawJunction.getSourceX();
                                    int jY = y - curJigsawJunction.getSourceGroundY();
                                    int jZ = absZ - curJigsawJunction.getSourceZ();

                                    clampedDensity += super.getNoiseWeight(jX, jY, jZ) * 0.4;
                                }
                                jigsawListIterator.back(JIGSAW_LIST.size());
                                
                                BlockState blockToSet = this.getBlockState(clampedDensity, y);
                                chunk.setBlockState(POS.set(x, y, z), blockToSet, false);
                                
                                heightmapOCEAN.trackUpdate(x, y, z, blockToSet);
                                heightmapSURFACE.trackUpdate(x, y, z, blockToSet);

                                ++z;
                            }
                        }
                    }
                }
            }
        }
    }

    private double generateHeightmap(double x, double y, double z) {
        double elevGrad;
        if ((elevGrad = y * 4.0 - 64.0) < 0.0) {
            elevGrad *= 3.0;
        }
        
        double noise;
        double res;
        
        if ((noise = this.noiseOctavesC.generateInfdevOctaves(x * 8.55515, y * 1.71103, z * 8.55515) / 2.0) < -1) {
            res = MathHelper.clamp(
                this.noiseOctavesA.generateInfdevOctaves(x * 684.412, y * 984.412, z * 684.412) / 512.0 - elevGrad, 
                -10.0, 
                10.0
            );
            
        } else if (noise > 1.0) {
            res = MathHelper.clamp(
                this.noiseOctavesB.generateInfdevOctaves(x * 684.412, y * 984.412, z * 684.412) / 512.0 - elevGrad, 
                -10.0, 
                10.0
            );
            
        } else {
            double noise2 = MathHelper.clamp(
                this.noiseOctavesA.generateInfdevOctaves(x * 684.412, y * 984.412, z * 684.412) / 512.0 - elevGrad, 
                -10.0, 
                10.0
            );
            
            double noise3 = MathHelper.clamp(
                this.noiseOctavesB.generateInfdevOctaves(x * 684.412, y * 984.412, z * 684.412) / 512.0 - elevGrad, 
                -10.0, 
                10.0
            );
            
            double mix = (noise + 1.0) / 2.0;
            
            res = noise2 + (noise3 - noise2) * mix;
        }
        
        return res;
    }

    private void buildInfdevSurface(ChunkRegion region, Chunk chunk) {
        double thirtysecond = 0.03125D; // eighth
        
        int chunkX = chunk.getPos().x;
        int chunkZ = chunk.getPos().z;
        
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int absX = (chunkX << 4) + x;
                int absZ = (chunkZ << 4) + z;
                
                boolean genSandBeach = this.beachNoiseOctaves.generateInfdevOctaves(
                    absX * thirtysecond, 
                    absZ * thirtysecond, 
                    0.0) + RAND.nextDouble() * 0.2 > 0.0;
                
                boolean genGravelBeach = this.beachNoiseOctaves.generateInfdevOctaves(
                    absZ * thirtysecond, 
                    109.0134,
                    absX * thirtysecond) + RAND.nextDouble() * 0.2 > 3.0;
                
                int genStone = (int)(this.stoneNoiseOctaves.generateInfdevOctaves(
                    absX * thirtysecond * 2.0, 
                    absZ * thirtysecond * 2.0) / 3.0 + 3.0 + this.random.nextDouble() * 0.25);
                
                int flag = -1;
                
                Biome curBiome = region.getBiome(POS.set(absX, 0, absZ));
                
                BlockState biomeTopBlock = curBiome.getGenerationSettings().getSurfaceConfig().getTopMaterial();
                BlockState biomeFillerBlock = curBiome.getGenerationSettings().getSurfaceConfig().getUnderMaterial();
                
                BlockState topBlock = biomeTopBlock;
                BlockState fillerBlock = biomeFillerBlock;
                
                for (int y = 127; y >= 0; --y) {
                    
                    // Randomly place bedrock from y=0 to y=5
                    if (y <= 0 + RAND.nextInt(5)) {
                        chunk.setBlockState(POS.set(x, y, z), Blocks.BEDROCK.getDefaultState(), false);
                        continue;
                    }
                    
                    POS.set(x, y, z);
                    Block someBlock = chunk.getBlockState(POS).getBlock();
                    
                    if (someBlock.equals(Blocks.AIR)) {
                        flag = -1;
                        
                    } else if (someBlock.equals(Blocks.STONE)) {
                        if (flag == -1) {
                            if (genStone <= 0) {
                                topBlock = BlockStates.AIR;
                                fillerBlock = BlockStates.STONE;
                            } else if (y >= 60 && y <= 65) {
                                topBlock = biomeTopBlock;
                                fillerBlock = biomeFillerBlock;
                                
                                if (genGravelBeach) {
                                    topBlock = BlockStates.AIR;
                                    fillerBlock = BlockStates.GRAVEL;
                                }
                                
                                if (genSandBeach) {
                                    topBlock = BlockStates.SAND;
                                    fillerBlock = BlockStates.SAND;
                                }
                            }
                            
                            if (y < this.getSeaLevel() && topBlock.equals(BlockStates.AIR)) {
                                topBlock = BlockStates.WATER; // Will this ever happen?
                            }
                            
                            flag = genStone;
                            
                            if (y >= this.getSeaLevel() - 1) {
                                chunk.setBlockState(POS, topBlock, false);
                            } else {
                                chunk.setBlockState(POS, fillerBlock, false);
                            }
                            
                        } else if (flag > 0) {
                            --flag;
                            chunk.setBlockState(POS, fillerBlock, false);
                            
                            // Gens layer of sandstone starting at lowest block of sand, of height 1 to 4.
                            // Beta backport.
                            if (flag == 0 && fillerBlock.equals(BlockStates.SAND)) {
                                flag = RAND.nextInt(4);
                                fillerBlock = BlockStates.SANDSTONE;
                            }
                        }
                    }
                }
            }
        }
    }

    
    protected BlockState getBlockState(double density, int y) {
        BlockState blockStateToSet = BlockStates.AIR;
        if (density > 0.0) {
            blockStateToSet = this.settings.wrapped.getDefaultBlock();
        } else if (y < this.getSeaLevel()) {
            blockStateToSet = this.settings.wrapped.getDefaultFluid();

        }
        return blockStateToSet;
    }

    
    // Called only when generating structures
    @Override
    public int getHeight(int x, int z, Heightmap.Type type) {
        BlockPos structPos = new BlockPos(x, 0, z);
        
        if (GROUND_CACHE_Y.get(structPos) == null) {
            sampleHeightmap(x, z);
        }

        int groundHeight = GROUND_CACHE_Y.get(structPos);

        // Not ideal
        if (type == Heightmap.Type.WORLD_SURFACE_WG && groundHeight < this.getSeaLevel())
            groundHeight = this.getSeaLevel();

        return groundHeight;
    }

    private void sampleHeightmap(int absX, int absZ) {
        
        int chunkX = absX >> 4;
        int chunkZ = absZ >> 4;
        
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++ j) {
                int bX = (chunkX << 2) + i;
                int bZ = (chunkZ << 2) + j;
                
                for (int bY = 0; bY < HEIGHTMAP.length; ++bY) {
                    HEIGHTMAP[bY][0] = this.generateHeightmap(bX, bY, bZ);
                    HEIGHTMAP[bY][1] = this.generateHeightmap(bX, bY, bZ + 1);
                    HEIGHTMAP[bY][2] = this.generateHeightmap(bX + 1, bY, bZ);
                    HEIGHTMAP[bY][3] = this.generateHeightmap(bX + 1, bY, bZ + 1);
                }
                
                for (int bY = 0; bY < 32; ++bY) {
                    double n1 = HEIGHTMAP[bY][0];
                    double n2 = HEIGHTMAP[bY][1];
                    double n3 = HEIGHTMAP[bY][2];
                    double n4 = HEIGHTMAP[bY][3];
                    double n5 = HEIGHTMAP[bY + 1][0];
                    double n7 = HEIGHTMAP[bY + 1][1];
                    double n8 = HEIGHTMAP[bY + 1][2];
                    double n9 = HEIGHTMAP[bY + 1][3];
                    
                    for (int pY = 0; pY < 4; ++pY) {
                        double mixY = pY / 4.0;
                        
                        double nx1 = n1 + (n5 - n1) * mixY;
                        double nx2 = n2 + (n7 - n2) * mixY;
                        double nx3 = n3 + (n8 - n3) * mixY;
                        double nx4 = n4 + (n9 - n4) * mixY;
                        
                        for (int pX = 0; pX < 4; ++pX) {
                            double mixX = pX / 4.0;
                            
                            double nz1 = nx1 + (nx3 - nx1) * mixX;
                            double nz2 = nx2 + (nx4 - nx2) * mixX;
                            
                            int x = pX + (i << 2);
                            int z = 0 + (j << 2);
                            int y = (bY << 2) + pY;
                            
                            for (int pZ = 0; pZ < 4; ++pZ) {
                                double mixZ = pZ / 4.0;
                                double noiseValue = nz1 + (nz2 - nz1) * mixZ;
                                if (noiseValue > 0.0D) {
                                    CHUNK_Y[x][z] = y;
                                }
                                
                                z++;
                            }
                        }
                    }
                }
            }
        }
        

        for (int pX = 0; pX < CHUNK_Y.length; pX++) {
            for (int pZ = 0; pZ < CHUNK_Y[pX].length; pZ++) {
                BlockPos structPos = new BlockPos((chunkX << 4) + pX, 0, (chunkZ << 4) + pZ);
                //POS.set((chunkX << 4) + pX, 0, (chunkZ << 4) + pZ);
                
                GROUND_CACHE_Y.put(structPos, CHUNK_Y[pX][pZ] + 1); // +1 because it is one above the ground
            }
        }
    }
    
    @Override
    public int getWorldHeight() {
        return 128;
    }

    @Override
    public int getSeaLevel() {
        return this.settings.wrapped.getSeaLevel();
    }

    @Override
    public ChunkGenerator withSeed(long seed) {
        return new InfdevChunkGenerator(this.biomeSource.withSeed(seed), seed, this.settings);
    }
}
