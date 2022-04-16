package org.xkmc.polaris_rpg.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.CombatEntry;
import net.minecraft.util.CombatTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CombatTracker.class)
public class CombatTrackerMixin {

	@Shadow
	private boolean inCombat;

	@Shadow
	private boolean takingDamage;

	@Shadow
	@Final
	private LivingEntity mob;

	@Shadow
	private int lastDamageTime;

	@Shadow
	private int combatEndTime;

	@Shadow
	@Final
	public List<CombatEntry> entries;

	@Inject(at = @At("HEAD"), method = "recheckStatus", cancellable = true)
	public void recheckStatus(CallbackInfo ci) {
		int i = this.inCombat ? 300 : 100;
		if (this.takingDamage && (!this.mob.isAlive() || this.mob.tickCount - this.lastDamageTime > i)) {
			boolean flag = this.inCombat;
			this.takingDamage = false;
			this.inCombat = false;
			this.combatEndTime = this.mob.tickCount;
			if (flag) {
				this.mob.onLeaveCombat();
			}
			if (this.mob.isAlive())
				this.entries.clear();
		}
		ci.cancel();
	}

}
