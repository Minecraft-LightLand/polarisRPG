package org.xkmc.polaris_rpg.content.archer.feature.arrow;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import org.xkmc.polaris_rpg.content.archer.entity.GenericArrowEntity;
import org.xkmc.polaris_rpg.content.archer.feature.types.OnHitFeature;

import java.util.function.Function;

public class DamageArrowFeature implements OnHitFeature {

	private final Function<GenericArrowEntity, DamageSource> source;
	private final Function<GenericArrowEntity, Float> damage;

	public DamageArrowFeature(Function<GenericArrowEntity, DamageSource> source, Function<GenericArrowEntity, Float> damage) {
		this.source = source;
		this.damage = damage;
	}

	@Override
	public void onHitEntity(GenericArrowEntity arrow, LivingEntity target) {
		DamageSource source = this.source.apply(arrow);
		float damage = this.damage.apply(arrow);
		target.hurt(source, damage);
	}

	@Override
	public void onHitBlock(GenericArrowEntity genericArrow, BlockRayTraceResult result) {

	}
}
