package org.xkmc.polaris_rpg.network.packets;

import dev.lcy0x1.core.util.SerialClass;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;
import org.xkmc.polaris_rpg.network.SerialPacketBase;

@SerialClass
public class SlotClickToServer extends SerialPacketBase {

	/**
	 * slot click for backpack
	 */
	@SerialClass.SerialField
	private int index, slot, wid;

	@Deprecated
	public SlotClickToServer() {

	}

	public SlotClickToServer(int index, int slot, int wid) {
		this.index = index;
		this.slot = slot;
		this.wid = wid;
	}

	@Override
	public void handle(NetworkEvent.Context ctx) {
		ServerPlayerEntity player = ctx.getSender();
		if (player == null) return;
		ItemStack stack;
		if (slot >= 0) {
			stack = ctx.getSender().inventory.getItem(slot);
		} else {
			Container menu = ctx.getSender().containerMenu;
			if (wid == 0 || menu.containerId == 0 || wid != menu.containerId) return;
			stack = ctx.getSender().containerMenu.getSlot(index).getItem();
		}
		/* TODO backpack
		if (slot >= 0 && stack.getItem() instanceof BackpackItem) {
			new BackpackItem.MenuPvd(player, slot, stack).open();
		} else if (stack.getItem() instanceof EnderBackpackItem) {
			NetworkHooks.openGui(player, new SimpleMenuProvider((id, inv, pl) ->
					ChestMenu.threeRows(id, inv, pl.getEnderChestInventory()), stack.getDisplayName()));
		} else if (stack.getItem() instanceof WorldChestItem chest) {
			new WorldChestItem.MenuPvd(player, stack, chest).open();
		}*/
	}
}
