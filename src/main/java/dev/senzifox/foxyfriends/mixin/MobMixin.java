package dev.senzifox.foxyfriends.mixin;

import dev.senzifox.foxyfriends.mode.FoxMode;
import dev.senzifox.foxyfriends.mode.FoxModes;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin {
	@Inject(method = "canShearEquipment", at = @At("HEAD"), cancellable = true)
	private void foxyfriends$shearOnlyOwnFox(Player player, CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this instanceof Fox fox && !FoxOwnership.isOwnedBy(fox, player)) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "onLeashRemoved", at = @At("TAIL"))
	private void foxyfriends$rehomeWanderingFox(CallbackInfo ci) {
		if ((Object) this instanceof Fox fox && FoxModes.isInMode(fox, FoxMode.WANDER) && !fox.hasHome()) {
			fox.setHomeTo(fox.blockPosition(), FoxModes.WANDER_RADIUS);
		}
	}
}
