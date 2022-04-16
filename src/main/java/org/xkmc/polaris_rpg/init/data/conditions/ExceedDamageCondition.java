package org.xkmc.polaris_rpg.init.data.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.CombatEntry;
import org.xkmc.polaris_rpg.init.data.PolarisLootModifier;

import java.util.List;

public class ExceedDamageCondition extends BaseCondition.Config<ExceedDamageCondition> {

	public static Codec<ExceedDamageCondition> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.FLOAT.fieldOf("exceed").forGetter(e -> e.exceed)
	).apply(i, ExceedDamageCondition::new));

	public final float exceed;

	public ExceedDamageCondition(float exceed) {
		super(PolarisLootModifier.EXCEED_DAMAGE);
		this.exceed = exceed;
	}

	@Override
	public boolean test(LootContext context) {
		Entity self = context.getParamOrNull(LootParameters.THIS_ENTITY);
		if (!(self instanceof LivingEntity)) return false;
		LivingEntity le = (LivingEntity) self;
		List<CombatEntry> list = le.getCombatTracker().entries;
		if (list.isEmpty()) {
			return false;
		}
		CombatEntry last = list.get(list.size() - 1);
		return last.getDamage() - last.health > exceed;
	}

}
