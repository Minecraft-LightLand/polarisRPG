package org.xkmc.polaris_rpg.event;

import dev.lcy0x1.base.Proxy;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.xkmc.polaris_rpg.content.item.armor.BaseArmorItem;
import org.xkmc.polaris_rpg.util.UUIDUtil;

import java.util.Objects;
import java.util.UUID;

public class PolarisGeneralEventHandler {

	private static final String LEVEL_HEALTH_STR = "polaris_rpg:max_health_from_level";
	private static final UUID LEVEL_HEALTH_ID = UUIDUtil.getUUIDfromString(LEVEL_HEALTH_STR);

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onLivingAttack(LivingAttackEvent event) {
	}

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

	public static void serverPlayerUpdateLevel(ServerPlayerEntity player) {
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


	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onScreenClick(GuiScreenEvent.MouseClickedEvent event) {
		Screen screen = event.getGui();
		if (event.getButton() == 1 &&
				screen instanceof ContainerScreen<?>) {
			ContainerScreen<?> cont = (ContainerScreen<?>) screen;
			Slot slot = cont.findSlot(event.getMouseX(), event.getMouseY());
			boolean b0 = slot != null;
			boolean b1 = b0 && slot.container == Proxy.getClientPlayer().inventory;
			boolean b2 = b0 && cont.getMenu().containerId > 0;
			if (b1 || b2) {
				int inv = b1 ? slot.getSlotIndex() : -1;
				int ind = inv == -1 ? slot.index : -1;
				int wid = cont.getMenu().containerId;
				/* TODO backpack
				if ((inv >= 0 || ind >= 0) && (slot.getItem().getItem() instanceof EnderBackpackItem ||
						slot.getItem().getItem() instanceof WorldChestItem ||
						inv >= 0 && slot.getItem().getItem() instanceof BackpackItem)) {
					new SlotClickToServer(ind, inv, wid).toServer();
					event.setCanceled(true);
				}*/
			}
		}

	}

}
