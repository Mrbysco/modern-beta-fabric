package mod.bespectacled.modernbeta.settings;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.util.NbtReader;
import net.minecraft.nbt.CompoundTag;
import org.slf4j.event.Level;

public interface ModernBetaSettings {
	CompoundTag toCompound();

	public static void datafix(String tag, NbtReader reader, Runnable datafixer) {
		if (reader.contains(tag)) {
			ModernBeta.log(Level.INFO, String.format("Found old setting '%s', fixing..", tag));
			datafixer.run();
		}
	}
}
