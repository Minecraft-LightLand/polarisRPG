package org.xkmc.polaris_rpg.mixin;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

	@Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
	public void hurt(DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
		if (source == DamageSource.LIGHTNING_BOLT)
			cir.setReturnValue(false);
	}

}

