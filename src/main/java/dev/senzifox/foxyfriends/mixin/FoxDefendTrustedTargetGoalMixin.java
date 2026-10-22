package dev.senzifox.foxyfriends.mixin;

import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.world.entity.animal.fox.Fox;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.animal.fox.Fox$DefendTrustedTargetGoal")
public abstract class FoxDefendTrustedTargetGoalMixin {
	@Shadow
	@Final
	Fox this$0;

	@Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
	private void foxyfriends$leaveDefenseToOwnerGoals(CallbackInfoReturnable<Boolean> cir) {
		if (FoxOwnership.isTame(this$0)) {
			cir.setReturnValue(false);
		}
	}
}
