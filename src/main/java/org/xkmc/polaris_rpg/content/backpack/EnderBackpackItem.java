package org.xkmc.polaris_rpg.content.backpack;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class EnderBackpackItem extends Item {

	public EnderBackpackItem(Properties props) {
		super(props);
	}

	@Override
	public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!level.isClientSide()) {
			player.openMenu(new SimpleNamedContainerProvider((id, inv, pl) -> ChestContainer.threeRows(id, inv, player.getEnderChestInventory()), stack.getDisplayName()));
		} else {
			player.playSound(SoundEvents.ENDER_CHEST_OPEN, 1, 1);
		}
		return ActionResult.success(stack);
	}

}
