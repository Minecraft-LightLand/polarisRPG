package org.xkmc.polaris_rpg.content.archer.feature.arrow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import org.xkmc.polaris_rpg.content.archer.entity.GenericArrowEntity;
import org.xkmc.polaris_rpg.content.archer.feature.types.OnHitFeature;

public class EnderArrowFeature implements OnHitFeature {

	@Override
	public void onHitEntity(GenericArrowEntity genericArrow, LivingEntity target) {
		Entity owner = genericArrow.getOwner();
		if (owner != null) {
			Vector3d pos = owner.getPosition(1);
			Vector3d tpos = target.getPosition(1);
			owner.teleportTo(tpos.x, tpos.y, tpos.z);
			target.teleportTo(pos.x, pos.y, pos.z);
		}
	}

	@Override
	public void onHitBlock(GenericArrowEntity genericArrow, BlockRayTraceResult result) {
		Entity owner = genericArrow.getOwner();
		if (owner != null) {
			Vector3d pos = result.getLocation();
			owner.teleportTo(pos.x, pos.y, pos.z);
		}
		genericArrow.remove();
	}
}
