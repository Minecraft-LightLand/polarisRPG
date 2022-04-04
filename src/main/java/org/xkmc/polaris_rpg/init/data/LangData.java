package org.xkmc.polaris_rpg.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.util.text.TranslationTextComponent;
import org.xkmc.polaris_rpg.init.PolarisRPG;

public enum LangData {
	ARMOR_FULL_SUIT("tooltip.armor.full_suit", "When equip full set:"),
	ARMOR_ELYTRA_FLY("tooltip.armor.elytra_fly", "Enable elytra fly"),
	ARMOR_CREATIVE_FLY("tooltip.armor.creative_fly", "Enable creative fly"),
	ARMOR_HOME("tooltip.armor.home", "Return home when dropped in the void"),
	STORAGE_OWNER("tooltip.storage.owner", "Owner: %s"),
	BACKPACK_SLOT("tooltip.storage.slot", "Upgrade: %s/%s");

	private final String key, def;

	LangData(String key, String def) {
		this.key = PolarisRPG.MODID + "." + key;
		this.def = def;
	}

	public TranslationTextComponent get(Object... args) {
		return new TranslationTextComponent(key, args);
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (LangData data : LangData.values()) {
			pvd.add(data.key, data.def);
		}
		pvd.add("itemGroup.polaris_rpg.material", "Tools and Materials");
		pvd.add("itemGroup.polaris_rpg.profession", "Professional Equipments");
		pvd.add("itemGroup.polaris_rpg.generated", "Common Equipments");
	}
}
