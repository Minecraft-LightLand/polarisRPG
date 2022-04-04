package org.xkmc.polaris_rpg.content.archer.feature;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import org.xkmc.polaris_rpg.content.archer.GenericArrowEntity;
import org.xkmc.polaris_rpg.content.archer.feature.BowArrowFeature;
import org.xkmc.polaris_rpg.util.ServerOnly;

public interface OnHitFeature extends BowArrowFeature {

	@ServerOnly
	void onHitEntity(GenericArrowEntity genericArrow, LivingEntity target);

	@ServerOnly
	void onHitBlock(GenericArrowEntity genericArrow, BlockRayTraceResult result);

}
