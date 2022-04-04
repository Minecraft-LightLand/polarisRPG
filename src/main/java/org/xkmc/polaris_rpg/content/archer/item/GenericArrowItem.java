package org.xkmc.polaris_rpg.content.archer.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.NonNullLazy;
import org.xkmc.polaris_rpg.content.archer.feature.FeatureList;

public class GenericArrowItem extends ArrowItem {

	public static class ArrowConfig {
		public final float damage;
		public final int punch;
		public final boolean is_inf;
		public final NonNullLazy<FeatureList> feature;

		public ArrowConfig(float damage, int punch, boolean is_inf, NonNullLazy<FeatureList> feature) {
			this.damage = damage;
			this.punch = punch;
			this.is_inf = is_inf;
			this.feature = feature;
		}
	}

	public final ArrowConfig config;

	public GenericArrowItem(Properties properties, ArrowConfig config) {
		super(properties);
		this.config = config;
	}

	public AbstractArrowEntity createArrow(World level, ItemStack stack, LivingEntity user) {
		ArrowEntity arrow = new ArrowEntity(level, user);
		arrow.setEffectsFromItem(stack);
		return arrow;
	}

	public boolean isInfinite(ItemStack stack, ItemStack bow, PlayerEntity player) {
		int enchant = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow);
		return enchant > 0 && config.is_inf;
	}

}
