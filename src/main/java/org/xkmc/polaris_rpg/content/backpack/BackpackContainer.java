package org.xkmc.polaris_rpg.content.backpack;

import dev.lcy0x1.base.menu.BaseContainerMenu;
import dev.lcy0x1.core.util.SpriteManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import org.xkmc.polaris_rpg.init.PolarisRPG;
import org.xkmc.polaris_rpg.init.registry.PolarisContainers;
import org.xkmc.polaris_rpg.util.ServerOnly;

import java.util.UUID;

public class BackpackContainer extends BaseContainerMenu<BackpackContainer> {

	public static final SpriteManager[] MANAGERS = new SpriteManager[6];

	static {
		for (int i = 0; i < 6; i++) {
			MANAGERS[i] = new SpriteManager(PolarisRPG.MODID, "backpack_" + (i + 1));
		}
	}

	public static BackpackContainer fromNetwork(ContainerType<BackpackContainer> type, int windowId, PlayerInventory inv, PacketBuffer buf) {
		int slot = buf.readInt();
		UUID id = buf.readUUID();
		int row = buf.readInt();
		return new BackpackContainer(windowId, inv, slot, id, row);
	}

	protected final PlayerEntity player;
	protected final int item_slot;
	protected final UUID uuid;

	public BackpackContainer(int windowId, PlayerInventory inventory, int hand, UUID uuid, int row) {
		super(PolarisContainers.MT_BACKPACK.get(), windowId, inventory, MANAGERS[row - 1], menu -> new BaseContainer<>(row * 9, menu), false);
		this.player = inventory.player;
		this.item_slot = hand;
		this.uuid = uuid;
		this.addSlot("grid", stack -> true);//TODO
		if (!this.player.level.isClientSide()) {
			ItemStack stack = getStack();
			if (!stack.isEmpty()) {
				ListNBT tag = BackpackItem.getListTag(stack);
				for (int i = 0; i < tag.size(); i++) {
					this.container.setItem(i, ItemStack.of((CompoundNBT) tag.get(i)));
				}
			}
		}
	}

	@ServerOnly
	@Override
	public boolean stillValid(PlayerEntity player) {
		return !getStack().isEmpty();
	}

	@ServerOnly
	public ItemStack getStack() {
		ItemStack stack = player.inventory.getItem(item_slot);
		CompoundNBT tag = stack.getTag();
		if (tag == null) return ItemStack.EMPTY;
		if (!tag.contains("container_id")) return ItemStack.EMPTY;
		if (!tag.getUUID("container_id").equals(uuid)) return ItemStack.EMPTY;
		return stack;
	}


	@Override
	public void removed(PlayerEntity player) {
		if (!player.level.isClientSide) {
			ItemStack stack = getStack();
			if (!stack.isEmpty()) {
				ListNBT list = new ListNBT();
				for (int i = 0; i < this.container.getContainerSize(); i++) {
					list.add(i, this.container.getItem(i).save(new CompoundNBT()));
				}
				BackpackItem.setListTag(stack, list);
			}
		}
		super.removed(player);
	}

}
