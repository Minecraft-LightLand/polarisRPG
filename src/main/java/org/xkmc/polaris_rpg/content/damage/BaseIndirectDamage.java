package org.xkmc.polaris_rpg.content.damage;

import net.minecraft.util.IndirectEntityDamageSource;

public class BaseIndirectDamage extends IndirectEntityDamageSource implements BaseDamage<BaseIndirectDamage> {

	private float piercing = 0;

	@SuppressWarnings({"ConstantConditions"})
	public BaseIndirectDamage(IndirectEntityDamageSource vanilla) {
		super(vanilla.getMsgId(), vanilla.getDirectEntity(), vanilla.getEntity());
		BaseDamage.copyProperties(vanilla, this);
		if (vanilla.isThorns()) this.setThorns();

	}



}
