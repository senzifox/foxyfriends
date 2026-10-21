package dev.senzifox.foxyfriends.mixin;

import dev.senzifox.foxyfriends.mode.FoxMode;
import dev.senzifox.foxyfriends.mode.FoxModes;
import net.minecraft.world.entity.animal.fox.Fox;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.animal.fox.Fox$SleepGoal")
public abstract class FoxSleepGoalMixin {
	@Shadow
	@Final
	Fox this$0;

	@Inject(method = {"canUse", "canContinueToUse"}, at = @At("HEAD"), cancellable = true)
	private void foxyfriends$stayAwakeWhileFollowing(CallbackInfoReturnable<Boolean> cir) {
		if (FoxModes.isInMode(this$0, FoxMode.FOLLOW)) {
			cir.setReturnValue(false);
		}
	}
}
