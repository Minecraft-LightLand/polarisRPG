package dev.lcy0x1.base.menu;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PredSlot extends Slot {

	private final Predicate<ItemStack> pred;
	private BooleanSupplier pickup;
	private Consumer<ItemStack> take;

	public PredSlot(IInventory inv, int ind, int x, int y, Predicate<ItemStack> pred) {
		super(inv, ind, x, y);
		this.pred = pred;
	}

	public PredSlot setPickup(BooleanSupplier pickup) {
		this.pickup = pickup;
		return this;
	}

	public PredSlot setTake(Consumer<ItemStack> take) {
		this.take = take;
		return this;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return pred.test(stack);
	}

	@Override
	public boolean mayPickup(PlayerEntity player) {
		return pickup == null ? super.mayPickup(player) : pickup.getAsBoolean();
	}

	@Override
	public ItemStack onTake(PlayerEntity player, ItemStack stack) {
		if (take != null) {
			take.accept(stack);
		}
		return super.onTake(player, stack);
	}

}
