package org.xkmc.polaris_rpg.content.archer.feature.bow;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import org.xkmc.polaris_rpg.content.archer.feature.types.OnPullFeature;
import org.xkmc.polaris_rpg.content.archer.item.GenericBowItem;
import org.xkmc.polaris_rpg.util.GenericItemStack;

public class WindBowFeature implements OnPullFeature {

	@Override
	public void onPull(PlayerEntity player, GenericItemStack<GenericBowItem> bow) {
		if (player instanceof ServerPlayerEntity) {
			player.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 100, 1));
		}
	}

	@Override
	public void tickAim(PlayerEntity player, GenericItemStack<GenericBowItem> bow) {

	}

	@Override
	public void stopAim(PlayerEntity player, GenericItemStack<GenericBowItem> bow) {

	}

}
