package org.xkmc.polaris_rpg.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.xkmc.polaris_rpg.content.archer.feature.bow.GlowTargetAimFeature;
import org.xkmc.polaris_rpg.util.RayTraceUtil;

@Mixin(Entity.class)
public class EntityMixin {

	@Inject(at = @At("HEAD"), method = "isGlowing", cancellable = true)
	public void isCurrentlyGlowing(CallbackInfoReturnable<Boolean> cir) {
		if (GlowTargetAimFeature.TARGET.target == (Object) this) {
			cir.setReturnValue(true);
		}
		if (RayTraceUtil.TARGET.target == (Object) this) {
			cir.setReturnValue(true);
		}
	}

}
