package mod.bespectacled.modernbeta.world.chunk;

import net.minecraft.world.level.levelgen.NoiseSettings;

public class ModernBetaShapeConfigs {
	@Deprecated
	public static final NoiseSettings MODERN_BETA;
	public static final NoiseSettings BETA;
	public static final NoiseSettings ALPHA;
	public static final NoiseSettings SKYLANDS;
	public static final NoiseSettings INFDEV_611;
	public static final NoiseSettings INFDEV_420;
	public static final NoiseSettings INFDEV_415;
	public static final NoiseSettings INFDEV_227;
	public static final NoiseSettings INDEV;
	public static final NoiseSettings CLASSIC_0_30;
	public static final NoiseSettings PE;

	static {
		MODERN_BETA = NoiseSettings.create(-64, 192, 1, 2);
		BETA = NoiseSettings.create(-64, 192, 1, 2);
		ALPHA = NoiseSettings.create(-64, 192, 1, 2);
		SKYLANDS = NoiseSettings.create(0, 128, 2, 1);
		INFDEV_611 = NoiseSettings.create(-64, 192, 1, 2);
		INFDEV_420 = NoiseSettings.create(-64, 192, 1, 2);
		INFDEV_415 = NoiseSettings.create(-64, 192, 1, 1);
		INFDEV_227 = NoiseSettings.create(-64, 192, 1, 2);
		INDEV = NoiseSettings.create(0, 256, 1, 2);
		CLASSIC_0_30 = NoiseSettings.create(0, 256, 1, 2);
		PE = NoiseSettings.create(-64, 192, 1, 2);
	}
}
