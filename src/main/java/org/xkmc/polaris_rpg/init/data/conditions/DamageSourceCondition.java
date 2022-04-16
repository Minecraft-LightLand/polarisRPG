package org.xkmc.polaris_rpg.init.data.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.CombatEntry;
import net.minecraft.util.DamageSource;
import org.xkmc.polaris_rpg.init.data.PolarisLootModifier;

import java.util.List;

public class DamageSourceCondition extends BaseCondition.Config<DamageSourceCondition> {

	public static Codec<DamageSourceCondition> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.STRING.fieldOf("msg").forGetter(e -> e.msg),
			Codec.FLOAT.fieldOf("min_damage").forGetter(e -> e.min_damage)
	).apply(i, DamageSourceCondition::new));

	public final String msg;
	public final float min_damage;

	public DamageSourceCondition(DamageSource source, float min_damage) {
		this(source.getMsgId(), min_damage);
	}

	public DamageSourceCondition(String msg, float min_damage) {
		super(PolarisLootModifier.DAMAGE_SOURCE);
		this.msg = msg;
		this.min_damage = min_damage;
	}

	@Override
	public boolean test(LootContext context) {
		DamageSource source = context.getParamOrNull(LootParameters.DAMAGE_SOURCE);
		if (source == null)
			return false;
		if (!source.getMsgId().equals(msg))
			return false;
		Entity self = context.getParamOrNull(LootParameters.THIS_ENTITY);
		if (!(self instanceof LivingEntity)) return false;
		LivingEntity le = (LivingEntity) self;
		List<CombatEntry> list = le.getCombatTracker().entries;
		if (list.isEmpty()) {
			return false;
		}
		CombatEntry last = list.get(list.size() - 1);
		return last.getDamage() > min_damage;
	}

}
