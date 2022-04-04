package org.xkmc.polaris_rpg.event;

import dev.lcy0x1.base.Proxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.xkmc.polaris_rpg.content.archer.feature.bow.GlowTargetAimFeature;
import org.xkmc.polaris_rpg.content.archer.feature.bow.IGlowFeature;
import org.xkmc.polaris_rpg.content.archer.item.GenericBowItem;
import org.xkmc.polaris_rpg.content.item.IGlowingTarget;
import org.xkmc.polaris_rpg.init.registry.PolarisMagic;
import org.xkmc.polaris_rpg.util.RayTraceUtil;

import java.util.ArrayList;

public class ClientEntityEffectRenderEvents {

	public static class EntityTarget {

		public final double max_distance, max_angle;
		public final int max_time;
		public int time;
		public Entity target;

		public EntityTarget(double max_distance, double max_angle, int max_time) {
			this.max_distance = max_distance;
			this.max_angle = max_angle;
			this.max_time = max_time;
		}

		public void updateTarget(Entity entity) {
			if (target != entity) {
				onChange(entity);
			}
			target = entity;
			time = 0;
		}

		public void onChange(Entity entity) {

		}

		@OnlyIn(Dist.CLIENT)
		public void tickRender() {
			if (target == null) {
				return;
			}
			PlayerEntity player = Proxy.getClientPlayer();
			if (player == null) {
				updateTarget(null);
				return;
			}
			ItemStack stack = player.getMainHandItem();
			if (stack.getItem() instanceof GenericBowItem) {
				GenericBowItem bow = (GenericBowItem) stack.getItem();
				if (bow.config.feature.pull.stream().noneMatch(e -> e instanceof IGlowFeature)) {
					updateTarget(null);
					return;
				}
			} else if (!(stack.getItem() instanceof IGlowingTarget)) {
				updateTarget(null);
				return;
			}
			Vector3d pos_a = player.getEyePosition(1f);
			Vector3d vec = player.getViewVector(1);
			Vector3d pos_b = target.getPosition(1);
			Vector3d diff = pos_b.subtract(pos_a);
			double dot = diff.dot(vec);
			double len_d = diff.length();
			double len_v = vec.length();
			double angle = Math.acos(dot / len_d / len_v);
			double dist = Math.sin(angle) * len_d;
			if (angle > max_angle && dist > max_distance) {
				updateTarget(null);
			}
			time++;
			if (time >= max_time) {
				updateTarget(null);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void clientTick(TickEvent.ClientTickEvent event) {
		GlowTargetAimFeature.TARGET.tickRender();
		RayTraceUtil.TARGET.tickRender();
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void fov(FOVUpdateEvent event) {
		PlayerEntity player = Proxy.getClientPlayer();
		if (player == null)
			return;
		if (player.getMainHandItem().getItem() instanceof GenericBowItem) {
			GenericBowItem bow = (GenericBowItem) player.getMainHandItem().getItem();
			float f = event.getFov();
			float i = player.getTicksUsingItem();
			EffectInstance ins = player.getEffect(PolarisMagic.QUICK_PULL.get());
			if (ins != null) {
				i *= 1.5 + 0.5 * ins.getAmplifier();
			}
			float p = bow.config.fov_time;
			event.setNewfov(f * (1 - Math.min(1, i / p) * bow.config.fov));
		}
	}

	private static class DelayedEntityRender {
		public final LivingEntity entity;
		public final ResourceLocation rl;

		private DelayedEntityRender(LivingEntity entity, ResourceLocation rl) {
			this.entity = entity;
			this.rl = rl;
		}
	}

	private static final ArrayList<DelayedEntityRender> ICONS = new ArrayList<>();

}
