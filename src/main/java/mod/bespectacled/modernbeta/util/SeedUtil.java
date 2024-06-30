package mod.bespectacled.modernbeta.util;

public class SeedUtil {
	public static long parseSeed(String stringSeed) {
		long seed = 0L;

		if (!stringSeed.isEmpty()) {
			try {
				seed = Long.parseLong(stringSeed);
			} catch (NumberFormatException e) {
				seed = stringSeed.hashCode();
			}
		}

		return seed;
	}
}
