package org.xkmc.polaris_rpg.content.archer.arrow;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import org.xkmc.polaris_rpg.content.archer.GenericArrowEntity;
import org.xkmc.polaris_rpg.content.archer.feature.OnHitFeature;
import org.xkmc.polaris_rpg.content.archer.feature.OnShootFeature;

import java.util.function.Consumer;

public class FireArrowFeature implements OnShootFeature, OnHitFeature {

	public final int time;

	public FireArrowFeature(int time) {
		this.time = time;
	}

	@Override
	public boolean onShoot(PlayerEntity player, Consumer<Consumer<GenericArrowEntity>> consumer) {
		consumer.accept((e) -> e.setRemainingFireTicks(time));
		return true;
	}

	@Override
	public void onHitEntity(GenericArrowEntity genericArrow, LivingEntity target) {
		target.setRemainingFireTicks(time);
	}

	@Override
	public void onHitBlock(GenericArrowEntity genericArrow, BlockRayTraceResult result) {

	}
}
