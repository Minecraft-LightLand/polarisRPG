package org.xkmc.polaris_rpg.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.util.text.TranslationTextComponent;
import org.xkmc.polaris_rpg.init.PolarisRPG;

public enum LangData {
	;

	private final String key, def;

	LangData(String key, String def) {
		this.key = PolarisRPG.MODID + "." + key;
		this.def = def;
	}

	public TranslationTextComponent get() {
		return new TranslationTextComponent(key);
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (LangData data : LangData.values()) {
			pvd.add(data.key, data.def);
		}
	}
}
