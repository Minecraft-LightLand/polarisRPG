package net.minecraft.entity;

import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.entity.living.LivingEvent;

public class FakeAccessor {

	public static void onEffectAdded(LivingEntity entity, EffectInstance ins){
		entity.onEffectAdded(ins);
	}


	public static void onEffectUpdated(LivingEntity entity, EffectInstance ins, boolean update){
		entity.onEffectUpdated(ins, update);
	}

}
