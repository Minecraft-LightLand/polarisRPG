package org.xkmc.polaris_rpg.content.archer.feature.types;

import net.minecraft.entity.player.PlayerEntity;
import org.xkmc.polaris_rpg.content.archer.feature.BowArrowFeature;
import org.xkmc.polaris_rpg.content.archer.item.GenericBowItem;
import org.xkmc.polaris_rpg.util.GenericItemStack;

public interface OnPullFeature extends BowArrowFeature {

	void onPull(PlayerEntity player, GenericItemStack<GenericBowItem> bow);

	void tickAim(PlayerEntity player, GenericItemStack<GenericBowItem> bow);

	void stopAim(PlayerEntity player, GenericItemStack<GenericBowItem> bow);

}
