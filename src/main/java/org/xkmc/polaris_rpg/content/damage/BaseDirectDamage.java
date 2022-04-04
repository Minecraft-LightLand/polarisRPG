package org.xkmc.polaris_rpg.content.damage;

import net.minecraft.util.EntityDamageSource;

public class BaseDirectDamage extends EntityDamageSource implements BaseDamage<BaseDirectDamage> {

	public BaseDirectDamage(EntityDamageSource vanilla) {
		super(vanilla.getMsgId(), vanilla.getEntity());
		BaseDamage.copyProperties(vanilla, this);
		if (vanilla.isThorns()) this.setThorns();
	}

}
