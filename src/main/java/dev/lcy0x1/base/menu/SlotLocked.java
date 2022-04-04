package dev.lcy0x1.base.menu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class SlotLocked extends Slot {

	public SlotLocked(IInventory inventory, int index, int x, int y) {
		super(inventory, index, x, y);
	}

	@Override
	public boolean mayPickup(PlayerEntity player) {
		return false;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

}
