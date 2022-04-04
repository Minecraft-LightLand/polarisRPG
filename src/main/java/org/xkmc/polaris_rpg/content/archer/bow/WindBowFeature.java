package org.xkmc.polaris_rpg.content.archer.bow;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import org.xkmc.polaris_rpg.content.archer.feature.OnPullFeature;
import org.xkmc.polaris_rpg.content.archer.GenericBowItem;
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
