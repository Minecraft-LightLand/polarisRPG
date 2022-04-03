package org.xkmc.polaris_rpg.event;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.xkmc.polaris_rpg.content.armor.BaseArmorItem;
import org.xkmc.polaris_rpg.util.UUIDUtil;

import java.util.Objects;
import java.util.UUID;

public class PolarisGeneralEventHandler {

	private static final String LEVEL_HEALTH_STR = "polaris_rpg:max_health_from_level";
	private static final UUID LEVEL_HEALTH_ID = UUIDUtil.getUUIDfromString(LEVEL_HEALTH_STR);

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
		serverPlayerUpdateLevel(player);
	}

	public static void serverPlayerUpdateLevel(ServerPlayerEntity player){
		int health_level = (player.experienceLevel - 12) & -2;
		ModifiableAttributeInstance instance = Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH));
		AttributeModifier mod = instance.getModifier(LEVEL_HEALTH_ID);
		if (mod == null || Math.abs(mod.getAmount() - health_level) > 1e-3) {
			if (mod != null) {
				instance.removeModifier(LEVEL_HEALTH_ID);
			}
			instance.addPermanentModifier(new AttributeModifier(LEVEL_HEALTH_ID, LEVEL_HEALTH_STR,
					health_level, AttributeModifier.Operation.ADDITION));
		}
		if (player.getHealth() > player.getMaxHealth()) {
			player.setHealth(player.getMaxHealth());
		}
	}

}
