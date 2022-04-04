package org.xkmc.polaris_rpg.content.archer.bow;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import org.xkmc.polaris_rpg.content.archer.feature.OnPullFeature;
import org.xkmc.polaris_rpg.content.archer.GenericBowItem;
import org.xkmc.polaris_rpg.event.ClientEntityEffectRenderEvents.EntityTarget;
import org.xkmc.polaris_rpg.util.GenericItemStack;

import java.util.function.Predicate;

public class GlowTargetAimFeature implements OnPullFeature, IGlowFeature {

	public final int range;

	public static final EntityTarget TARGET = new EntityTarget(3, Math.PI / 180 * 5, 2);

	public GlowTargetAimFeature(int range) {
		this.range = range;
	}

	@Override
	public void onPull(PlayerEntity player, GenericItemStack<GenericBowItem> bow) {

	}

	@Override
	public void tickAim(PlayerEntity player, GenericItemStack<GenericBowItem> bow) {
		if (player.level.isClientSide()) {
			Vector3d vec3 = player.getEyePosition(1f);
			Vector3d vec31 = player.getViewVector(1.0F).scale(range);
			Vector3d vec32 = vec3.add(vec31);
			AxisAlignedBB aabb = player.getBoundingBox().expandTowards(vec31).inflate(1.0D);
			int sq = range * range;
			Predicate<Entity> predicate = (e) -> e instanceof LivingEntity && !e.isSpectator();
			EntityRayTraceResult result = ProjectileHelper.getEntityHitResult(player, vec3, vec32, aabb, predicate, sq);
			if (result != null && vec3.distanceToSqr(result.getLocation()) < sq) {
				TARGET.updateTarget(result.getEntity());
			}
		}
	}

	@Override
	public void stopAim(PlayerEntity player, GenericItemStack<GenericBowItem> bow) {
		TARGET.updateTarget(null);
	}

}
