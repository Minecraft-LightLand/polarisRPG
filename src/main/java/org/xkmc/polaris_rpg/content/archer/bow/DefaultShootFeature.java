package org.xkmc.polaris_rpg.content.archer.bow;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import org.xkmc.polaris_rpg.content.archer.GenericArrowEntity;
import org.xkmc.polaris_rpg.content.archer.feature.OnShootFeature;

import java.util.function.Consumer;

public class DefaultShootFeature implements OnShootFeature {

	public static final DefaultShootFeature INSTANCE = new DefaultShootFeature();

	@Override
	public boolean onShoot(PlayerEntity player, Consumer<Consumer<GenericArrowEntity>> consumer) {
		consumer.accept(entity -> {
			entity.shootFromRotation(player, player.xRot, player.yRot, 0.0F,
					entity.data.power * entity.data.bow.item.config.speed, 1.0F);
			if (entity.data.power == 1.0F) {
				entity.setCritArrow(true);
			}
			ItemStack bow = entity.data.bow.stack;
			int power = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bow);
			if (power > 0) {
				entity.setBaseDamage(entity.getBaseDamage() + (double) power * 0.5D + 0.5D);
			}
			int punch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bow);
			if (punch > 0) {
				entity.setKnockback(punch);
			}
			if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bow) > 0) {
				entity.setSecondsOnFire(100);
			}
			if (entity.data.no_consume) {
				entity.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
			}
			int knock = entity.knockback + entity.data.bow.item.config.punch + entity.data.arrow.item.config.punch;
			double damage = entity.getBaseDamage() + entity.data.bow.item.config.damage + entity.data.arrow.item.config.damage;
			entity.setKnockback(Math.max(0, knock));
			entity.setBaseDamage(Math.max(damage, 0.5));
		});
		return true;
	}
}
