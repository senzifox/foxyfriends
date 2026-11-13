package dev.senzifox.foxyfriends.mixin;

import dev.senzifox.foxyfriends.mode.FoxMode;
import dev.senzifox.foxyfriends.mode.FoxModes;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.world.entity.animal.fox.Fox;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.animal.fox.Fox$FoxFollowParentGoal")
public abstract class FoxFollowParentGoalMixin {
	@Shadow
	@Final
	private Fox fox;

	@Inject(method = {"canUse", "canContinueToUse"}, at = @At("HEAD"), cancellable = true)
	private void foxyfriends$followParentsOnlyWhileWandering(CallbackInfoReturnable<Boolean> cir) {
		if (FoxOwnership.isTame(fox) && FoxModes.getMode(fox) != FoxMode.WANDER) {
			cir.setReturnValue(false);
		}
	}
}
