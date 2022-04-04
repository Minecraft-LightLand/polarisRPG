package org.xkmc.polaris_rpg.content.archer.feature.types;

import net.minecraft.entity.player.PlayerEntity;
import org.xkmc.polaris_rpg.content.archer.entity.GenericArrowEntity;
import org.xkmc.polaris_rpg.content.archer.feature.BowArrowFeature;
import org.xkmc.polaris_rpg.util.ServerOnly;

import java.util.function.Consumer;

public interface OnShootFeature extends BowArrowFeature {

	@ServerOnly
	boolean onShoot(PlayerEntity player, Consumer<Consumer<GenericArrowEntity>> entity);

	default void onClientShoot(GenericArrowEntity entity) {
	}
}
