package dev.senzifox.foxyfriends.mixin;

import dev.senzifox.foxyfriends.taming.FoxBreeding;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.fox.Fox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin {
	@Inject(method = "ageBoundaryReached", at = @At("TAIL"))
	private void foxyfriends$rewardRaisedKit(CallbackInfo ci) {
		if ((Object) this instanceof Fox fox && !fox.isBaby()) {
			FoxBreeding.onGrownUp(fox);
		}
	}
}
