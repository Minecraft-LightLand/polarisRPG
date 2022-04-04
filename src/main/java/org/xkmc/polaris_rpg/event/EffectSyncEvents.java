package org.xkmc.polaris_rpg.event;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.xkmc.polaris_rpg.network.packets.EffectToClient;

import java.util.*;

public class EffectSyncEvents {

	public static final Map<UUID, Map<Effect, Integer>> EFFECT_MAP = new HashMap<>();
	public static final Set<Effect> TRACKED = new HashSet<>();

	public static void init() {
	}

	@OnlyIn(Dist.CLIENT)
	public static void sync(EffectToClient eff) {
		Map<Effect, Integer> set = EFFECT_MAP.get(eff.entity);
		if (eff.exist) {
			if (set == null) {
				EFFECT_MAP.put(eff.entity, set = new HashMap<>());
			}
			set.put(eff.effect, eff.level);
		} else if (set != null) {
			set.remove(eff.effect);
		}
	}

	@SubscribeEvent
	public static void onPotionAddedEvent(PotionEvent.PotionAddedEvent event) {
		if (TRACKED.contains(event.getPotionEffect().getEffect())) {
			onEffectAppear(event.getPotionEffect().getEffect(), event.getEntityLiving(), event.getPotionEffect().getAmplifier());
		}
	}

	@SubscribeEvent
	public static void onPotionExpiryEvent(PotionEvent.PotionExpiryEvent event) {
		if (event.getPotionEffect() != null && TRACKED.contains(event.getPotionEffect().getEffect())) {
			onEffectDisappear(event.getPotionEffect().getEffect(), event.getEntityLiving());
		}
	}

	@SubscribeEvent
	public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
		if (!(event.getTarget() instanceof LivingEntity))
			return;
		LivingEntity le = (LivingEntity) event.getTarget();
		for (Effect eff : le.getActiveEffectsMap().keySet()) {
			if (TRACKED.contains(eff)) {
				onEffectAppear(eff, le, le.getActiveEffectsMap().get(eff).getAmplifier());
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerStopTracking(PlayerEvent.StopTracking event) {
		if (!(event.getTarget() instanceof LivingEntity))
			return;
		LivingEntity le = (LivingEntity) event.getTarget();
		for (Effect eff : le.getActiveEffectsMap().keySet()) {
			if (TRACKED.contains(eff)) {
				onEffectDisappear(eff, le);
			}
		}
	}

	@SubscribeEvent
	public static void onServerPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayerEntity e = (ServerPlayerEntity) event.getPlayer();
		if (e != null) {
			for (Effect eff : e.getActiveEffectsMap().keySet()) {
				if (TRACKED.contains(eff)) {
					onEffectAppear(eff, e, e.getActiveEffectsMap().get(eff).getAmplifier());
				}
			}
		}
	}

	@SubscribeEvent
	public static void onServerPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
		ServerPlayerEntity e = (ServerPlayerEntity) event.getPlayer();
		if (e != null) {
			for (Effect eff : e.getActiveEffectsMap().keySet()) {
				if (TRACKED.contains(eff)) {
					onEffectDisappear(eff, e);
				}
			}
		}
	}

	private static void onEffectAppear(Effect eff, LivingEntity e, int lv) {
		new EffectToClient(e.getUUID(), eff, true, lv).toTrackingPlayers(e);
	}

	private static void onEffectDisappear(Effect eff, LivingEntity e) {
		new EffectToClient(e.getUUID(), eff, false, 0).toTrackingPlayers(e);
	}

}
