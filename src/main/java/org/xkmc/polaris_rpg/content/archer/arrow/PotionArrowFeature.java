package org.xkmc.polaris_rpg.content.archer.arrow;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockRayTraceResult;
import org.xkmc.polaris_rpg.content.archer.GenericArrowEntity;
import org.xkmc.polaris_rpg.content.archer.feature.OnHitFeature;
import org.xkmc.polaris_rpg.util.EffectAddUtil;

public class PotionArrowFeature implements OnHitFeature {

	public final EffectInstance[] instances;

	public PotionArrowFeature(EffectInstance... instances) {
		this.instances = instances;
	}

	@Override
	public void onHitEntity(GenericArrowEntity arrow, LivingEntity target) {
		for (EffectInstance instance : instances) {
			EffectAddUtil.addEffect(target, instance, EffectAddUtil.AddReason.PROF, arrow.getOwner());
		}
	}

	@Override
	public void onHitBlock(GenericArrowEntity arrow, BlockRayTraceResult result) {
	}
}
