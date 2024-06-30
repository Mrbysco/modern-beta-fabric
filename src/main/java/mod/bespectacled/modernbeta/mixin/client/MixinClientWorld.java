package mod.bespectacled.modernbeta.mixin.client;

import mod.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import mod.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import mod.bespectacled.modernbeta.api.world.biome.climate.ClimateSamplerSky;
import mod.bespectacled.modernbeta.client.color.BlockColorSampler;
import mod.bespectacled.modernbeta.client.world.ModernBetaClientWorld;
import mod.bespectacled.modernbeta.config.ModernBetaConfig;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsBiome;
import mod.bespectacled.modernbeta.util.SeedUtil;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.biome.provider.BiomeProviderBeta;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(value = ClientLevel.class, priority = 1)
public abstract class MixinClientWorld implements ModernBetaClientWorld {
	@Shadow
	private Minecraft minecraft;

	@Unique
	private Vec3 modernBeta_pos;
	@Unique
	private ClimateSampler modernBeta_climateSampler;
	@Unique
	private ClimateSamplerSky modernBeta_climateSamplerSky;
	@Unique
	private boolean modernBeta_isModernBetaWorld;

	@Override
	public boolean isModernBetaWorld() {
		return this.modernBeta_isModernBetaWorld;
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	private void init(
			ClientPacketListener networkHandler,
			ClientLevel.ClientLevelData properties,
			ResourceKey<Level> registryKeyWorld,
			Holder<DimensionType> registryEntryDimensionType,
			int loadDistance,
			int simulationDistance,
			Supplier<ProfilerFiller> profiler,
			LevelRenderer renderer,
			boolean debugWorld,
			long seed,
			CallbackInfo info
	) {
		long worldSeed = SeedUtil.parseSeed(ModernBetaConfig.COMMON.fixedSeed.get());
		boolean useFixedSeed = ModernBetaConfig.COMMON.useFixedSeed.get();

		// Init with default values
		BiomeProviderBeta biomeProviderBeta = new BiomeProviderBeta(new ModernBetaSettingsBiome().toCompound(), null, worldSeed);
		this.modernBeta_climateSampler = useFixedSeed ? biomeProviderBeta : null;
		this.modernBeta_climateSamplerSky = useFixedSeed ? biomeProviderBeta : null;
		this.modernBeta_isModernBetaWorld = false;

		// Server check
		if (this.minecraft.getSingleplayerServer() != null && registryKeyWorld != null) {
			ServerLevel serverWorld = this.minecraft.getSingleplayerServer().getLevel(registryKeyWorld);

			ChunkGenerator chunkGenerator = serverWorld.getChunkSource().getGenerator();
			BiomeSource biomeSource = chunkGenerator.getBiomeSource();

			if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
				BiomeProvider biomeProvider = modernBetaBiomeSource.getBiomeProvider();

				if (biomeProvider instanceof ClimateSampler climateSampler)
					this.modernBeta_climateSampler = climateSampler;

				if (biomeProvider instanceof ClimateSamplerSky skyClimateSampler)
					this.modernBeta_climateSamplerSky = skyClimateSampler;
			}

			this.modernBeta_isModernBetaWorld = chunkGenerator instanceof ModernBetaChunkGenerator || biomeSource instanceof ModernBetaBiomeSource;
		}

		// Set climate sampler
		BlockColorSampler.INSTANCE.setClimateSampler(this.modernBeta_climateSampler);
	}

	@Inject(method = "getSkyColor", at = @At("HEAD"))
	private void capturePos(Vec3 cameraPos, float tickDelta, CallbackInfoReturnable<Vec3> info) {
		this.modernBeta_pos = cameraPos;
	}

	@ModifyVariable(
			method = "getSkyColor",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/util/CubicSampler;gaussianSampleVec3(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/util/CubicSampler$Vec3Fetcher;)Lnet/minecraft/world/phys/Vec3;"
			),
			index = 6
	)
	private Vec3 injectSkyColor(Vec3 skyColorVec) {
		if (this.modernBeta_climateSamplerSky != null && this.modernBeta_climateSamplerSky.useSkyColor()) {
			int x = (int) modernBeta_pos.x();
			int z = (int) modernBeta_pos.z();

			float temp = (float) this.modernBeta_climateSamplerSky.sampleSky(x, z);
			temp /= 3F;
			temp = Mth.clamp(temp, -1F, 1F);

			skyColorVec = Vec3.fromRGB24(Mth.hsvToRgb(0.6222222F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F));
		}

		return skyColorVec;
	}
}

