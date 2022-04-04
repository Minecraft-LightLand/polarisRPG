package org.xkmc.polaris_rpg.init.registry;

import com.tterrag.registrate.builders.NoConfigBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import org.xkmc.polaris_rpg.content.magic.QuickPullEffect;
import org.xkmc.polaris_rpg.content.magic.RunBowEffect;

import static org.xkmc.polaris_rpg.init.PolarisRPG.REGISTRATE;

public class PolarisMagic {

	public static final RegistryEntry<RunBowEffect> RUN_BOW = genEffect("run_bow", () -> new RunBowEffect(EffectType.BENEFICIAL, 0xffffff));
	public static final RegistryEntry<QuickPullEffect> QUICK_PULL = genEffect("quick_pull", () -> new QuickPullEffect(EffectType.BENEFICIAL, 0xFFFFFF));

	public static <T extends Effect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup) {
		return REGISTRATE.entry(name, cb -> new NoConfigBuilder<>(REGISTRATE, REGISTRATE, name, cb, Effect.class, sup))
				.lang(Effect::getDescriptionId).register();
	}

	public static void register() {
	}

}
