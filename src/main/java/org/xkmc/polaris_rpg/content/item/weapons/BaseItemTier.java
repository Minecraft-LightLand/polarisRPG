package org.xkmc.polaris_rpg.content.item.weapons;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class BaseItemTier implements IItemTier {

	public static class BaseTierStats {

		private final int uses;
		private final float speed;
		private final float damage;
		private final int enchantmentValue;

		private BaseTierStats(int durability, float speed, float damage, int enchant) {
			this.uses = durability;
			this.speed = speed;
			this.damage = damage;
			this.enchantmentValue = enchant;
		}

	}

	public static class ExtraTierStats {

		public boolean unbreakable;

		public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {

		}

		public void addAttribute(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder, ItemStack stack) {
		}

		public void inventoryTick(ItemStack stack, World level, Entity entity, int slot, boolean selected) {

		}
	}

	private final int level;
	private final BaseTierStats stats;
	private final LazyValue<Ingredient> repairIngredient;

	public final ExtraTierStats extra;

	private BaseItemTier(int level, BaseTierStats stats, ExtraTierStats extra, Supplier<Ingredient> repair) {
		this.level = level;
		this.stats = stats;
		this.extra = extra;
		this.repairIngredient = new LazyValue<>(repair);
	}

	public int getUses() {
		return this.stats.uses;
	}

	public float getSpeed() {
		return this.stats.speed;
	}

	public float getAttackDamageBonus() {
		return this.stats.damage;
	}

	public int getLevel() {
		return this.level;
	}

	public int getEnchantmentValue() {
		return this.stats.enchantmentValue;
	}

	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

}
