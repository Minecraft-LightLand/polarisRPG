package org.xkmc.polaris_rpg.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import org.xkmc.polaris_rpg.content.item.armor.BaseArmorItem;

public class DamageUtil {

	public static void dealDamage(LivingEntity target, DamageSource source, float damage) {
		if (source.isFire() && target.fireImmune())
			return;
		target.hurt(source, damage);
	}

	public static double getMagicReduced(LivingEntity target, DamageSource source, double damage) {
		if (source.isBypassMagic() || source.isBypassInvul())
			return damage;
		double level = 0;
		EffectInstance ins = target.getEffect(Effects.DAMAGE_RESISTANCE);
		if (ins != null) level += (ins.getAmplifier() + 1) * 20;
		level += EnchantmentHelper.getDamageProtection(target.getArmorSlots(), source) * 4;
		if (source.isMagic()) {
			for (ItemStack stack : target.getArmorSlots()) {
				if (stack.getItem() instanceof BaseArmorItem) {
					level += ((BaseArmorItem) stack.getItem()).armorMaterial.extra.magicImmune;
				}
			}
		}
		return damage * Math.exp(-0.01 * level);
	}

}
