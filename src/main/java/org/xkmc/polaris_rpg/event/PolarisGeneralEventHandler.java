package org.xkmc.polaris_rpg.event;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.xkmc.polaris_rpg.content.armor.BaseArmorItem;

public class PolarisGeneralEventHandler {

	@SubscribeEvent
	public static void serverPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.player.level.isClientSide()) return;
		ServerPlayerEntity player = (ServerPlayerEntity) event.player;
		ItemStack stack = player.getItemBySlot(EquipmentSlotType.CHEST);
		GameType type = player.gameMode.getGameModeForPlayer();
		boolean armorfly = type == GameType.CREATIVE || type == GameType.SPECTATOR;
		if (!armorfly && stack.getItem() instanceof BaseArmorItem) {
			BaseArmorItem armor = (BaseArmorItem) stack.getItem();
			armorfly = armor.canFly(player);
		}
		boolean oldfly = player.abilities.mayfly;
		if (oldfly != armorfly) {
			player.abilities.mayfly = armorfly;
			player.abilities.flying &= armorfly;
			player.onUpdateAbilities();
		}
		if (player.getHealth() > player.getMaxHealth()) {
			player.setHealth(player.getMaxHealth());
		}

	}

}
