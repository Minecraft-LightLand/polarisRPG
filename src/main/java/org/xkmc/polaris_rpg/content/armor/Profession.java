package org.xkmc.polaris_rpg.content.armor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public enum Profession {
	NONE, CASTER, SHIELD, ARCHER;

	@Nullable
	public static Profession armorProfession(PlayerEntity player) {
		Profession prof = null;
		for (int i = 2; i < 6; i++) {
			EquipmentSlotType slot = EquipmentSlotType.values()[i];
			ItemStack stack = player.getItemBySlot(slot);
			Item item = stack.getItem();
			if (!(item instanceof BaseArmorItem)) {
				return null;
			}
			BaseArmorItem armor = (BaseArmorItem) item;
			Profession armor_prof = armor.armorMaterial.extra.profession;
			if (prof == null) {
				prof = armor_prof;
			} else if (prof != armor_prof) {
				return null;
			}
		}
		return prof;
	}


}
