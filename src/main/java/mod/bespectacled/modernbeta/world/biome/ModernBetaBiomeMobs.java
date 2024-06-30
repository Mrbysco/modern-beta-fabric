package mod.bespectacled.modernbeta.world.biome;

import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;

public class ModernBetaBiomeMobs {
	public static void addCommonMobs(MobSpawnSettings.Builder spawnSettings) {
		BiomeDefaultFeatures.farmAnimals(spawnSettings);
		BiomeDefaultFeatures.commonSpawns(spawnSettings);
	}

	public static void addSquid(MobSpawnSettings.Builder spawnSettings) {
		spawnSettings.addSpawn(MobCategory.WATER_CREATURE, new SpawnerData(EntityType.SQUID, 10, 1, 4));
	}

	public static void addTurtles(MobSpawnSettings.Builder spawnSettings) {
		spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.TURTLE, 5, 2, 5));
	}

	public static void addWolves(MobSpawnSettings.Builder spawnSettings) {
		spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.WOLF, 5, 4, 4));
	}

	public static void addColdOceanMobs(MobSpawnSettings.Builder spawnSettings) {
		BiomeDefaultFeatures.oceanSpawns(spawnSettings, 3, 4, 15);

		spawnSettings.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(EntityType.SALMON, 15, 1, 5));
	}

	public static void addFrozenOceanMobs(MobSpawnSettings.Builder spawnSettings) {
		spawnSettings.addSpawn(MobCategory.WATER_CREATURE, new SpawnerData(EntityType.SQUID, 1, 1, 4));
		spawnSettings.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(EntityType.SALMON, 15, 1, 5));
		spawnSettings.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 1, 1, 2));

		BiomeDefaultFeatures.commonSpawns(spawnSettings);
		spawnSettings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.DROWNED, 5, 1, 1));
	}

	public static void addOceanMobs(MobSpawnSettings.Builder spawnSettings) {
		BiomeDefaultFeatures.oceanSpawns(spawnSettings, 10, 4, 10);

		spawnSettings.addSpawn(MobCategory.WATER_CREATURE, new SpawnerData(EntityType.DOLPHIN, 1, 1, 2));
	}

	public static void addWarmOceanMobs(MobSpawnSettings.Builder spawnSettings) {
		BiomeDefaultFeatures.warmOceanSpawns(spawnSettings, 10, 4);

		spawnSettings.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(EntityType.PUFFERFISH, 15, 1, 3));
	}

	public static void addLukewarmOceanMobs(MobSpawnSettings.Builder spawnSettings) {
		BiomeDefaultFeatures.oceanSpawns(spawnSettings, 10, 2, 15);

		spawnSettings.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(EntityType.PUFFERFISH, 5, 1, 3));
		spawnSettings.addSpawn(MobCategory.WATER_AMBIENT, new SpawnerData(EntityType.TROPICAL_FISH, 25, 8, 8));
		spawnSettings.addSpawn(MobCategory.WATER_CREATURE, new SpawnerData(EntityType.DOLPHIN, 2, 1, 2));
	}

	public static void addDesertMobs(MobSpawnSettings.Builder spawnSettings) {
		BiomeDefaultFeatures.desertSpawns(spawnSettings);
	}

	public static void addPlainsMobs(MobSpawnSettings.Builder spawnSettings) {
		BiomeDefaultFeatures.plainsSpawns(spawnSettings);
	}

	public static void addRainforestMobs(MobSpawnSettings.Builder spawnSettings) {
		spawnSettings.addSpawn(MobCategory.MONSTER, new SpawnerData(EntityType.OCELOT, 2, 1, 3));
		spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.PANDA, 2, 1, 2));
		spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.PARROT, 40, 1, 2));
	}

	public static void addSwamplandMobs(MobSpawnSettings.Builder spawnSettings) {
		spawnSettings.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 1, 1, 1));
		spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.FROG, 10, 2, 5));
	}

	public static void addTaigaMobs(MobSpawnSettings.Builder spawnSettings) {
		spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.WOLF, 5, 4, 4));
		spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.RABBIT, 4, 2, 3));
		spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.LLAMA, 4, 4, 6));
		spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.FOX, 8, 2, 4));
	}

	public static void addTundraMobs(MobSpawnSettings.Builder spawnSettings) {
		BiomeDefaultFeatures.snowySpawns(spawnSettings);

		// TODO: Move maybe later
		spawnSettings.addSpawn(MobCategory.CREATURE, new SpawnerData(EntityType.GOAT, 5, 4, 6));
	}

	public static void addSkyMobs(MobSpawnSettings.Builder spawnSettings) {
		BiomeDefaultFeatures.monsters(spawnSettings, 95, 5, 20, false);
		spawnSettings.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 10, 4, 4));
	}
}
