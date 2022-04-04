package org.xkmc.polaris_rpg.content.archer.feature.types;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import org.xkmc.polaris_rpg.content.archer.entity.GenericArrowEntity;
import org.xkmc.polaris_rpg.content.archer.feature.BowArrowFeature;
import org.xkmc.polaris_rpg.util.ServerOnly;

public interface OnHitFeature extends BowArrowFeature {

	@ServerOnly
	void onHitEntity(GenericArrowEntity genericArrow, LivingEntity target);

	@ServerOnly
	void onHitBlock(GenericArrowEntity genericArrow, BlockRayTraceResult result);

}
