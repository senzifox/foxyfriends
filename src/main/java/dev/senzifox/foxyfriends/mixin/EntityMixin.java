package dev.senzifox.foxyfriends.mixin;

import dev.senzifox.foxyfriends.travel.FoxTravel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Inject(method = "addPassenger", at = @At("TAIL"))
	private void foxyfriends$rewardBoatingWithFox(Entity passenger, CallbackInfo ci) {
		if ((Object) this instanceof AbstractBoat boat) {
			FoxTravel.onBoatPassengerAdded(boat);
		}
	}
}
