package mod.bespectacled.modernbeta.compat;

import mod.bespectacled.modernbeta.ModernBeta;
import net.neoforged.fml.ModList;
import org.slf4j.event.Level;

public class Compat {
	public static void setupCompat() {
		try {
			if (isLoaded("colormatic")) {
				ModernBeta.log(Level.WARN, "Colormatic found. Colormatic is currently not supported.");
			}

		} catch (Exception e) {
			ModernBeta.log(Level.ERROR, "Something went wrong when attempting to add mod compatibility!");
			e.printStackTrace();
		}
	}

	public static boolean isLoaded(String modid) {
		return ModList.get().isLoaded(modid);
	}
}