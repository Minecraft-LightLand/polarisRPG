package org.xkmc.polaris_rpg.content.item.armor;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.xkmc.polaris_rpg.content.profession.Profession;
import org.xkmc.polaris_rpg.init.PolarisRPG;

import java.util.Arrays;
import java.util.function.Supplier;

public class BaseArmorMaterial implements IArmorMaterial {

	public static class BaseArmorStats {

		public final int durability;
		private final int[] protection;
		private final int enchant;
		private final SoundEvent sound;
		private final float tough;
		private final float kb;

		public BaseArmorStats(int durability, int[] protection, int enchant,
							  SoundEvent sound, float tough, float kb) {
			this.durability = durability;
			this.protection = protection;
			this.enchant = enchant;
			this.sound = sound;
			this.tough = tough;
			this.kb = kb;
		}

	}

	public static class ExtraArmorStats {

		public final boolean creativeFly, home, elytraFly;
		public final LazyValue<EffectInstance[]> ins;
		public final Profession profession;
		public double magicImmune;

		@SafeVarargs
		public ExtraArmorStats(Profession profession, double magicImmune, boolean elytraFly, boolean creativeFly, boolean home, Supplier<EffectInstance>... ins) {
			this.profession = profession;
			this.magicImmune = magicImmune;
			this.elytraFly = elytraFly;
			this.creativeFly = creativeFly;
			this.home = home;
			this.ins = new LazyValue<>(() -> Arrays.stream(ins).map(Supplier::get).toArray(EffectInstance[]::new));
		}
	}

	private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
	private final String name;
	private final BaseArmorStats stats;
	private final LazyValue<Ingredient> repairIngredient;

	public final ExtraArmorStats extra;

	private BaseArmorMaterial(String name, BaseArmorStats stats, Supplier<Ingredient> repair, ExtraArmorStats extra) {
		this.name = name;
		this.stats = stats;
		this.repairIngredient = new LazyValue<>(repair);
		this.extra = extra;
	}

	public int getDurabilityForSlot(EquipmentSlotType slotType) {
		return HEALTH_PER_SLOT[slotType.getIndex()] * this.stats.durability;
	}

	public int getDefenseForSlot(EquipmentSlotType slotType) {
		return this.stats.protection[slotType.getIndex()];
	}

	public int getEnchantmentValue() {
		return this.stats.enchant;
	}

	public SoundEvent getEquipSound() {
		return this.stats.sound;
	}

	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

	@OnlyIn(Dist.CLIENT)
	public String getName() {
		return PolarisRPG.MODID + ":" + this.name;
	}

	public float getToughness() {
		return this.stats.tough;
	}

	public float getKnockbackResistance() {
		return this.stats.kb;
	}
}
