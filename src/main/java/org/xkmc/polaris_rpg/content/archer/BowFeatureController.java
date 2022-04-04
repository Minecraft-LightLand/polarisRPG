package org.xkmc.polaris_rpg.content.archer;

import net.minecraft.entity.player.PlayerEntity;
import org.xkmc.polaris_rpg.util.GenericItemStack;

public class BowFeatureController {

	public static void startUsing(PlayerEntity player, GenericItemStack<GenericBowItem> bow) {
		bow.item.config.feature.pull.forEach(e -> e.onPull(player, bow));
	}

	public static void usingTick(PlayerEntity player, GenericItemStack<GenericBowItem> bow) {
		bow.item.config.feature.pull.forEach(e -> e.tickAim(player, bow));
	}

	public static void stopUsing(PlayerEntity player, GenericItemStack<GenericBowItem> bow) {
		bow.item.config.feature.pull.forEach(e -> e.stopAim(player, bow));

	}
}
