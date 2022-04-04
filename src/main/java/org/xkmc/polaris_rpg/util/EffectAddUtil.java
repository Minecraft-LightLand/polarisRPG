package org.xkmc.polaris_rpg.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.FakeAccessor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent;
import org.xkmc.polaris_rpg.content.magic.ForceEffect;

public class EffectAddUtil {

	public enum AddReason {
		NONE, PROF, FORCE, SKILL, SELF
	}

	private static final ThreadLocal<AddReason> REASON = new ThreadLocal<>();

	/**
	 * force add effect, make boss not override
	 * for icon use only, such as Arcane Mark on Wither and Ender Dragon
	 */
	private static void forceAddEffect(LivingEntity e, EffectInstance ins) {
		EffectInstance effectinstance = e.getActiveEffectsMap().get(ins.getEffect());
		MinecraftForge.EVENT_BUS.post(new PotionEvent.PotionAddedEvent(e, effectinstance, ins));
		if (effectinstance == null) {
			e.getActiveEffectsMap().put(ins.getEffect(), ins);
			FakeAccessor.onEffectAdded(e, ins);
		} else if (effectinstance.update(ins)) {
			FakeAccessor.onEffectUpdated(e,effectinstance, true);
		}
	}

	public static void addEffect(LivingEntity entity, EffectInstance ins, AddReason reason, Entity source) {
		if (entity == source)
			reason = AddReason.SELF;
		if (ins.getEffect() instanceof ForceEffect)
			reason = AddReason.FORCE;
		ins = new EffectInstance(ins.getEffect(), ins.getDuration(), ins.getAmplifier(),
				ins.isAmbient(), reason != AddReason.FORCE && ins.isVisible(), ins.showIcon());
		REASON.set(reason);
		if (ins.getEffect() instanceof ForceEffect)
			forceAddEffect(entity, ins);
		else if (ins.getEffect().isInstantenous())
			ins.getEffect().applyInstantenousEffect(null, null, entity, ins.getAmplifier(), 1);
		else entity.addEffect(ins);
		REASON.set(AddReason.NONE);
	}

	public static void refreshEffect(LivingEntity entity, EffectInstance ins, AddReason reason, Entity source) {
		EffectInstance cur = entity.getEffect(ins.getEffect());
		if (cur == null || cur.getAmplifier() < ins.getAmplifier())
			addEffect(entity, ins, reason, source);
	}

	public static AddReason getReason() {
		AddReason ans = REASON.get();
		return ans == null ? AddReason.NONE : ans;
	}

}
