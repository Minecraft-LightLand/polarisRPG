package org.xkmc.polaris_rpg.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.xkmc.polaris_rpg.util.DamageUtil;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Inject(at = @At("HEAD"), method = "getDamageAfterMagicAbsorb", cancellable = true)
	protected void getDamageAfterMagicAbsorb(DamageSource source, float damage, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue((float) DamageUtil.getMagicReduced((LivingEntity) (Object) this, source, damage));
	}

}
