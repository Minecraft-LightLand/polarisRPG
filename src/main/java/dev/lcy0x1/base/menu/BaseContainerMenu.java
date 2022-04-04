package dev.lcy0x1.base.menu;

import dev.lcy0x1.core.util.SpriteManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class BaseContainerMenu<T extends BaseContainerMenu<T>> extends Container {

	public static class BaseContainer<T extends BaseContainerMenu<T>> extends Inventory {

		protected final T parent;
		private boolean updating = false;

		public BaseContainer(int size, T menu) {
			super(size);
			parent = menu;
		}

		@Override
		public void setChanged() {
			super.setChanged();
			if (!updating) {
				updating = true;
				parent.slotsChanged(this);
				updating = false;
			}
		}

	}

	public final PlayerInventory inventory;
	public final Inventory container;
	public final SpriteManager sprite;
	private int added = 0;
	private final boolean isVirtual;

	@SuppressWarnings("unchecked")
	protected BaseContainerMenu(ContainerType<?> type, int wid, PlayerInventory plInv, SpriteManager manager, Function<T, Inventory> factory, boolean isVirtual) {
		super(type, wid);
		this.inventory = plInv;
		container = factory.apply((T) this);
		sprite = manager;
		int x = manager.getPlInvX();
		int y = manager.getPlInvY();
		this.bindPlayerInventory(plInv, x, y);
		this.isVirtual = isVirtual;
	}

	protected void bindPlayerInventory(PlayerInventory plInv, int x, int y) {
		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				addSlot(createSlot(plInv, j + i * 9 + 9, x + j * 18, y + i * 18));
		for (int k = 0; k < 9; ++k)
			addSlot(createSlot(plInv, k, x + k * 18, y + 58));
	}

	protected Slot createSlot(IInventory inv, int slot, int x, int y) {
		return shouldLock(inv, slot) ? new SlotLocked(inv, slot, x, y) : new Slot(inv, slot, x, y);
	}

	protected boolean shouldLock(IInventory inv, int slot) {
		return false;
	}

	protected void addSlot(String name, Predicate<ItemStack> pred) {
		sprite.getSlot(name, (x, y) -> new PredSlot(container, added++, x, y, pred), this::addSlot);
	}

	protected void addSlot(String name, Predicate<ItemStack> pred, Consumer<PredSlot> modifier) {
		sprite.getSlot(name, (x, y) -> {
			PredSlot s = new PredSlot(container, added++, x, y, pred);
			modifier.accept(s);
			return s;
		}, this::addSlot);
	}

	@Override
	public ItemStack quickMoveStack(PlayerEntity pl, int id) {
		ItemStack stack = slots.get(id).getItem();
		int n = container.getContainerSize();
		if (id >= 36) {
			moveItemStackTo(stack, 0, 36, true);
		} else {
			moveItemStackTo(stack, 36, 36 + n, false);
		}
		container.setChanged();
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(PlayerEntity player) {
		return player.isAlive();
	}

	@Override
	public void removed(PlayerEntity player) {
		if (isVirtual && !player.level.isClientSide())
			clearContainer(player, player.level, container);
		super.removed(player);
	}
}
