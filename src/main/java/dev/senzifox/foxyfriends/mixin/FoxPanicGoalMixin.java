package dev.senzifox.foxyfriends.mixin;

import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.animal.fox.Fox;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.animal.fox.Fox$FoxPanicGoal")
public abstract class FoxPanicGoalMixin {
	@Shadow
	@Final
	Fox this$0;

	@Inject(method = "shouldPanic", at = @At("HEAD"), cancellable = true)
	private void foxyfriends$panicOnlyFromEnvironment(CallbackInfoReturnable<Boolean> cir) {
		if (FoxOwnership.isTame(this$0)) {
			DamageSource source = this$0.getLastDamageSource();
			cir.setReturnValue(source != null && source.is(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
		}
	}
}
