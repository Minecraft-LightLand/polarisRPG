package org.xkmc.polaris_rpg.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.xkmc.polaris_rpg.event.PolarisGeneralEventHandler;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

	@Shadow
	public int experienceLevel;

	@Inject(at = @At("HEAD"), method = "getXpNeededForNextLevel", cancellable = true)
	public void getXpNeededForNextLevel(CallbackInfoReturnable<Integer> cir) {
		int xp = (int) (700 * Math.exp(experienceLevel * Math.log(1.2)));
		cir.setReturnValue(xp);
	}

	@Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
	public void readAdditionalSaveData(CompoundNBT tag, CallbackInfo ci) {
		PlayerEntity player = (PlayerEntity) (Object) this;
		if (player instanceof ServerPlayerEntity) {
			PolarisGeneralEventHandler.serverPlayerUpdateLevel((ServerPlayerEntity) player);
		}
		if (tag.contains("Health", 99)) {
			player.setHealth(tag.getFloat("Health"));
		}
	}

	@Inject(at = @At("HEAD"), method = "getExperienceReward", cancellable = true)
	public void getExperienceReward(PlayerEntity entity, CallbackInfoReturnable<Integer> ci) {
		ci.setReturnValue(0);
	}

}
