package org.xkmc.polaris_rpg.init.registry;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.item.Item;
import org.xkmc.polaris_rpg.init.PolarisRPG;

import java.util.Locale;

@SuppressWarnings({"rawtype", "unchecked", "unused"})
public class PolarisItems {

	public enum SimpleItem {
		;

		public final ItemEntry<Item> entry;

		SimpleItem() {
			entry = PolarisRPG.REGISTRATE.item(name().toLowerCase(Locale.ROOT), Item::new)
					.defaultModel().defaultLang().register();
		}

		public static void register() {
		}

	}

	static {
	}

	public static void register() {
	}

}
