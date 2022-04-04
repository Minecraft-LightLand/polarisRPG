package org.xkmc.polaris_rpg.content.archer.controller;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.world.World;
import org.xkmc.polaris_rpg.content.archer.entity.GenericArrowEntity;
import org.xkmc.polaris_rpg.content.archer.feature.FeatureList;
import org.xkmc.polaris_rpg.content.archer.feature.types.OnShootFeature;
import org.xkmc.polaris_rpg.content.archer.item.GenericArrowItem;
import org.xkmc.polaris_rpg.content.archer.item.GenericBowItem;
import org.xkmc.polaris_rpg.util.GenericItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ArrowFeatureController {

	public static class BowArrowUseContext {
		public final World level;
		public final PlayerEntity user;
		public final boolean no_consume;
		public final float power;

		public BowArrowUseContext(World level, PlayerEntity user, boolean no_consume, float power) {
			this.level = level;
			this.user = user;
			this.no_consume = no_consume;
			this.power = power;
		}
	}

	public static boolean canBowUseArrow(GenericBowItem bow, GenericItemStack<GenericArrowItem> arrow) {
		return FeatureList.canMerge(bow.config.feature, arrow.item.config.feature.get());
	}

	@Nullable
	public static AbstractArrowEntity createArrowEntity(BowArrowUseContext ctx,
														GenericItemStack<GenericBowItem> bow,
														GenericItemStack<GenericArrowItem> arrow) {
		FeatureList features = Objects.requireNonNull(FeatureList.merge(bow.item.config.feature, arrow.item.config.feature.get()));
		List<Consumer<GenericArrowEntity>> list = new ArrayList<>();
		for (OnShootFeature e : features.shot)
			if (!e.onShoot(ctx.user, list::add))
				return null;
		GenericArrowEntity ans = new GenericArrowEntity(ctx.level, ctx.user,
				new GenericArrowEntity.ArrowEntityData(bow, arrow, ctx.no_consume, ctx.power), features);
		list.forEach(e -> e.accept(ans));
		return ans;
	}

}
