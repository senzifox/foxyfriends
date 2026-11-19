package dev.senzifox.foxyfriends.mixin;

import dev.senzifox.foxyfriends.care.FoxCare;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	@Inject(method = "checkTotemDeathProtection", at = @At("RETURN"))
	private void foxyfriends$rewardTotemSavingFox(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValueZ() && (Object) this instanceof Fox fox) {
			FoxCare.onSavedByTotem(fox);
		}
	}
}
