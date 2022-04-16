package org.xkmc.polaris_rpg.init.registry;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;

import java.util.Locale;

import static org.xkmc.polaris_rpg.init.PolarisRPG.REGISTRATE;

public enum SimpleItems {
	ROUGH_CLOTH(Rarity.COMMON),
	SMOOTH_CLOTH(Rarity.COMMON),
	FINE_CLOTH(Rarity.COMMON),

	SOUL_GEM(Rarity.UNCOMMON),
	ENDER_EMERALD(Rarity.UNCOMMON),
	SOUL_REMNANT(Rarity.UNCOMMON),
	ZOMBIE_BRAIN(Rarity.UNCOMMON),
	HARD_BONE(Rarity.UNCOMMON),
	WITHER_BONE(Rarity.UNCOMMON),
	HOGLIN_TEETH(Rarity.UNCOMMON),
	PIGLIN_TEETH(Rarity.UNCOMMON),
	GEL(Rarity.UNCOMMON),
	RUNE_GEM(Rarity.UNCOMMON),

	ORE_HEART(Rarity.UNCOMMON),
	DIM_0(Rarity.UNCOMMON),
	WITHER_BONEMEAL(Rarity.UNCOMMON),

	SOUL_INGOT(Rarity.RARE),
	SOUL_ESSENCE(Rarity.RARE),
	SOUL_FLAME(Rarity.RARE),
	SHULKER_HEART(Rarity.RARE),
	DEEP_OCEAN_SOUL(Rarity.RARE),
	GHAST_CRY(Rarity.RARE),
	DIM_1(Rarity.RARE),

	SOULGOLD_INGOT(Rarity.EPIC),
	TRIPLE_INGOT(Rarity.EPIC),
	END_SEED(Rarity.EPIC),
	SHINING_NETHER_STAR(Rarity.EPIC),
	DIM_2(Rarity.EPIC);

	public final ItemEntry<Item> entry;

	SimpleItems(Rarity rarity) {
		entry = REGISTRATE.item(getName(), Item::new).properties(e -> e.fireResistant().rarity(rarity)).defaultModel().defaultLang().register();
	}

	public Item get() {
		return entry.get();
	}

	public ItemStack getStack(int i) {
		return new ItemStack(get(), i);
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static void register() {

	}
}
