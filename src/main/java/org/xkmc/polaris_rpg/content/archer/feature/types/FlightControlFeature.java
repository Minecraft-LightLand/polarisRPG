package org.xkmc.polaris_rpg.content.archer.feature.types;

import net.minecraft.util.math.vector.Vector3d;
import org.xkmc.polaris_rpg.content.archer.entity.GenericArrowEntity;
import org.xkmc.polaris_rpg.content.archer.feature.BowArrowFeature;

public class FlightControlFeature implements BowArrowFeature {

	public static final FlightControlFeature INSTANCE = new FlightControlFeature();

	public float gravity = 0.05f;
	public float inertia = 0.99f;
	public float water_inertia = 0.6f;
	public int life = -1;
	public int ground_life = 1200;

	public void tickMotion(GenericArrowEntity entity, Vector3d velocity) {
		float inertia = entity.isInWater() ? water_inertia : this.inertia;
		velocity = velocity.scale(inertia);
		float grav = !entity.isNoGravity() && !entity.isNoPhysics() ? gravity : 0;
		entity.setDeltaMovement(velocity.x, velocity.y - grav, velocity.z);
	}

}
