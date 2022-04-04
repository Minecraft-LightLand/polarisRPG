package org.xkmc.polaris_rpg.mixin;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.xkmc.polaris_rpg.content.item.FastItem;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientLocalPlayerMixin {

	@Shadow
	private boolean startedUsingItem;

	private static boolean in_ai_step = false;

	@Inject(at = @At("HEAD"), method = "aiStep")
	public void aiStep(CallbackInfo ci) {
		in_ai_step = true;
	}

	@Inject(at = @At("HEAD"), method = "isUsingItem", cancellable = true)
	public void isUsingItem(CallbackInfoReturnable<Boolean> cir) {
		if (in_ai_step) {
			in_ai_step = false;
			PlayerEntity player = (PlayerEntity) (Object) this;
			if (this.startedUsingItem) {
				ItemStack stack = player.getUseItem();
				if (stack.getItem() instanceof FastItem) {
					if (((FastItem) stack.getItem()).isFast(stack)) {
						cir.setReturnValue(false);
					}
				}
			}
		}
	}


}
