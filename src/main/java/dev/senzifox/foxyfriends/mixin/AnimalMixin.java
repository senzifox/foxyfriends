package dev.senzifox.foxyfriends.mixin;

import dev.senzifox.foxyfriends.care.FoxCare;
import dev.senzifox.foxyfriends.collar.FoxCollar;
import dev.senzifox.foxyfriends.mode.FoxModes;
import dev.senzifox.foxyfriends.taming.FoxTaming;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public abstract class AnimalMixin {
	@Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
	private void foxyfriends$interactWithFox(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		if ((Object) this instanceof Fox fox) {
			InteractionResult result = FoxTaming.interact(fox, player, hand);
			if (result == null) {
				result = FoxCare.interact(fox, player, hand);
			}

			if (result == null) {
				result = FoxCollar.interact(fox, player, hand);
			}

			if (result == null) {
				result = FoxModes.interact(fox, player, hand);
			}

			if (result != null) {
				cir.setReturnValue(result);
			}
		}
	}
}
