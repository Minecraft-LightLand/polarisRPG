package org.xkmc.polaris_rpg.content.backpack;

import dev.lcy0x1.base.menu.BaseContainerMenu;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import org.xkmc.polaris_rpg.content.capability.worldstorage.StorageContainer;
import org.xkmc.polaris_rpg.init.registry.PolarisContainers;
import org.xkmc.polaris_rpg.util.ServerOnly;

import javax.annotation.Nullable;

public class WorldChestContainer extends BaseContainerMenu<WorldChestContainer> {

	public static WorldChestContainer fromNetwork(ContainerType<WorldChestContainer> type, int windowId, PlayerInventory inv) {
		return new WorldChestContainer(windowId, inv, new Inventory(27), null);
	}

	protected final PlayerEntity player;
	protected final StorageContainer storage;

	public WorldChestContainer(int windowId, PlayerInventory inventory, Inventory cont, @Nullable StorageContainer storage) {
		super(PolarisContainers.MT_WORLD_CHEST.get(), windowId, inventory, BackpackContainer.MANAGERS[2], menu -> cont, false);
		this.player = inventory.player;
		this.addSlot("grid", stack -> true);
		this.storage = storage;
	}

	@ServerOnly
	@Override
	public boolean stillValid(PlayerEntity player) {
		return storage == null || storage.isValid();
	}

}
