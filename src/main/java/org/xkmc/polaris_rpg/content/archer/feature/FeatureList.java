package org.xkmc.polaris_rpg.content.archer.feature;

import org.xkmc.polaris_rpg.content.archer.feature.types.FlightControlFeature;
import org.xkmc.polaris_rpg.content.archer.feature.types.OnHitFeature;
import org.xkmc.polaris_rpg.content.archer.feature.types.OnPullFeature;
import org.xkmc.polaris_rpg.content.archer.feature.types.OnShootFeature;

import java.util.ArrayList;
import java.util.List;

public class FeatureList {

	public static boolean canMerge(FeatureList a, FeatureList b) {
		return a.flight == null || b.flight == null;
	}

	public static FeatureList merge(FeatureList a, FeatureList b) {
		if (a.flight != null && b.flight != null) {
			return null;
		}
		FeatureList ans = new FeatureList();
		ans.pull.addAll(a.pull);
		ans.pull.addAll(b.pull);
		ans.shot.addAll(a.shot);
		ans.shot.addAll(b.shot);
		ans.hit.addAll(a.hit);
		ans.hit.addAll(b.hit);
		ans.flight = a.flight != null ? a.flight : b.flight;
		return ans;
	}

	public List<OnPullFeature> pull = new ArrayList<>();
	public List<OnShootFeature> shot = new ArrayList<>();
	public FlightControlFeature flight = null;
	public List<OnHitFeature> hit = new ArrayList<>();

	public FlightControlFeature getFlightControl() {
		return flight == null ? FlightControlFeature.INSTANCE : flight;
	}

	public FeatureList add(BowArrowFeature feature) {
		if (feature instanceof OnPullFeature) pull.add((OnPullFeature) feature);
		if (feature instanceof OnShootFeature) shot.add((OnShootFeature) feature);
		if (feature instanceof FlightControlFeature) flight = (FlightControlFeature) feature;
		if (feature instanceof OnHitFeature) hit.add((OnHitFeature) feature);
		return this;
	}

	public void end() {

	}
}
