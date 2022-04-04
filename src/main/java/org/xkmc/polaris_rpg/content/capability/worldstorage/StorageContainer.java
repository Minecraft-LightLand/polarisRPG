package org.xkmc.polaris_rpg.content.capability.worldstorage;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.UUID;

public class StorageContainer implements IInventoryChangedListener {

	private final CompoundNBT tag;

	final long password;

	public final UUID id;
	public final Inventory container;

	StorageContainer(UUID id, CompoundNBT tag) {
		this.tag = tag;
		this.id = id;
		this.password = tag.getLong("password");
		this.container = new Inventory(27);
		if (tag.contains("container")) {
			ListNBT list = tag.getList("container", 10);
			for (int i = 0; i < list.size(); i++) {
				this.container.setItem(i, ItemStack.of((CompoundNBT) list.get(i)));
			}
		}
		container.addListener(this);
	}

	@Override
	public void containerChanged(IInventory cont) {
		ListNBT list = new ListNBT();
		for (int i = 0; i < container.getContainerSize(); i++) {
			list.add(i, container.getItem(i).save(new CompoundNBT()));
		}
		tag.put("container", list);
	}

	public boolean isValid() {
		return password == tag.getLong("password");
	}
}
