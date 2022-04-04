package org.xkmc.polaris_rpg.content.archer.arrow;

import org.xkmc.polaris_rpg.content.archer.feature.FlightControlFeature;

public class NoFallArrowFeature extends FlightControlFeature {

	public NoFallArrowFeature(int life) {
		this.gravity = 0;
		this.inertia = 1;
		this.water_inertia = 1;
		this.life = life;
	}

}
