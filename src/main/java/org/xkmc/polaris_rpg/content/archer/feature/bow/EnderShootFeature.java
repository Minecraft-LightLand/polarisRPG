package org.xkmc.polaris_rpg.content.archer.feature.bow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import org.xkmc.polaris_rpg.content.archer.entity.GenericArrowEntity;
import org.xkmc.polaris_rpg.content.archer.feature.types.OnPullFeature;
import org.xkmc.polaris_rpg.content.archer.feature.types.OnShootFeature;
import org.xkmc.polaris_rpg.content.archer.item.GenericBowItem;
import org.xkmc.polaris_rpg.util.GenericItemStack;
import org.xkmc.polaris_rpg.util.RayTraceUtil;

import java.util.function.Consumer;

public class EnderShootFeature implements OnShootFeature, OnPullFeature, IGlowFeature {

	public final int range;

	public EnderShootFeature(int range) {
		this.range = range;
	}

	@Override
	public boolean onShoot(PlayerEntity player, Consumer<Consumer<GenericArrowEntity>> consumer) {
		if (player == null)
			return false;
		Entity target = RayTraceUtil.serverGetTarget(player);
		if (target == null)
			return false;
		consumer.accept(entity -> {
			Vector3d pos = target.position().add(target.getEyePosition(1f)).scale(0.5f).add(entity.getDeltaMovement().scale(-1));
			entity.setPos(pos.x, pos.y, pos.z);
		});
		return true;
	}

	@Override
	public void onPull(PlayerEntity player, GenericItemStack<GenericBowItem> bow) {

	}

	@Override
	public void tickAim(PlayerEntity player, GenericItemStack<GenericBowItem> bow) {
		RayTraceUtil.clientUpdateTarget(player, range);
	}

	@Override
	public void stopAim(PlayerEntity player, GenericItemStack<GenericBowItem> bow) {
		RayTraceUtil.TARGET.updateTarget(null);
	}
}
