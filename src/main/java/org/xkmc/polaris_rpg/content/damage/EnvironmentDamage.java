package org.xkmc.polaris_rpg.content.damage;

import net.minecraft.util.DamageSource;

public class EnvironmentDamage extends DamageSource implements BaseDamage<EnvironmentDamage> {

	public EnvironmentDamage(DamageSource vanilla) {
		super(vanilla.getMsgId());
		BaseDamage.copyProperties(vanilla, this);
	}

}
