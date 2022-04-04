package org.xkmc.polaris_rpg.content.damage;

import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

public interface BaseDamage<T extends DamageSource & BaseDamage<T>> {

	static DamageSource transform(DamageSource source) {
		if (source instanceof IndirectEntityDamageSource) {
			return new BaseIndirectDamage((IndirectEntityDamageSource) source);
		}
		if (source instanceof EntityDamageSource) {
			return new BaseDirectDamage((EntityDamageSource) source);
		}
		return new EnvironmentDamage(source);
	}

	static <T extends DamageSource & BaseDamage<T>> void copyProperties(DamageSource vanilla, T self) {
		if (vanilla.isProjectile()) self.setProjectile();
		if (vanilla.isFire()) self.setIsFire();
		if (vanilla.isMagic()) self.setMagic();
		if (vanilla.isExplosion()) self.setExplosion();
		if (vanilla.isBypassArmor()) self.bypassArmor();
		if (vanilla.isBypassMagic()) self.bypassMagic();
		if (vanilla.isBypassInvul()) self.bypassInvul();
		if (vanilla.scalesWithDifficulty()) self.scalesWithDifficulty();
	}

	@SuppressWarnings({"unchecked", "unsafe"})
	default T getThis() {
		return (T) this;
	}

	default float getArmorPiercing(float damage) {
		return damage / 2;
	}

}
