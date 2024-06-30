package mod.bespectacled.modernbeta.world.biome.provider;

import mod.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public class BiomeProviderSingle extends BiomeProvider {
	private final ResourceKey<Biome> biomeKey;

	public BiomeProviderSingle(CompoundTag settings, HolderGetter<Biome> biomeRegistry, long seed) {
		super(settings, biomeRegistry, seed);

		this.biomeKey = ResourceKey.create(Registries.BIOME, ResourceLocation.tryParse(this.settings.singleBiome));
	}

	@Override
	public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
		return this.biomeRegistry.getOrThrow(this.biomeKey);
	}

	@Override
	public List<Holder<Biome>> getBiomes() {
		return List.of(this.biomeRegistry.getOrThrow(this.biomeKey));
	}
}
