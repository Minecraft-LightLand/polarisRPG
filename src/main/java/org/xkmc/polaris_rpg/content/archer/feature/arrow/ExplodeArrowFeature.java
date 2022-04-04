package org.xkmc.polaris_rpg.content.archer.feature.arrow;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import org.xkmc.polaris_rpg.content.archer.entity.GenericArrowEntity;
import org.xkmc.polaris_rpg.content.archer.feature.types.OnHitFeature;

public class ExplodeArrowFeature implements OnHitFeature {

	public final float radius;

	public ExplodeArrowFeature(float radius) {
		this.radius = radius;
	}

	@Override
	public void onHitEntity(GenericArrowEntity arrow, LivingEntity target) {
		arrow.level.explode(arrow, arrow.getX(), arrow.getY(), arrow.getZ(), radius, Explosion.Mode.NONE);
	}

	@Override
	public void onHitBlock(GenericArrowEntity arrow, BlockRayTraceResult result) {
		arrow.level.explode(arrow, result.getLocation().x, result.getLocation().y, result.getLocation().z, radius, Explosion.Mode.NONE);
		arrow.remove();
	}
}
