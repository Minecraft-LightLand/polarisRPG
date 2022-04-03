package org.xkmc.polaris_rpg.util;

import java.util.Random;
import java.util.UUID;

public class UUIDUtil {

	public static UUID getUUIDfromString(String str) {
		int hash = str.hashCode();
		Random r = new Random(hash);
		long l0 = r.nextLong();
		long l1 = r.nextLong();
		return new UUID(l0, l1);
	}

}
