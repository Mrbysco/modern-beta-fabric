package mod.bespectacled.modernbeta.world.cavebiome.provider;

import mod.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.biome.Biome;

public class CaveBiomeProviderNone extends CaveBiomeProvider {
	public CaveBiomeProviderNone(CompoundTag settings, HolderGetter<Biome> biomeRegistry, long seed) {
		super(settings, biomeRegistry, seed);
	}

	@Override
	public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
		return null;
	}
}
