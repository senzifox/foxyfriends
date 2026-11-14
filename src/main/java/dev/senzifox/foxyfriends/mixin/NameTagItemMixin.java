package dev.senzifox.foxyfriends.mixin;

import dev.senzifox.foxyfriends.name.FoxNaming;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.NameTagItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NameTagItem.class)
public abstract class NameTagItemMixin {
	@Inject(method = "interactLivingEntity", at = @At("HEAD"), cancellable = true)
	private void foxyfriends$nameOnlyOwnFox(ItemStack stack, Player player, LivingEntity target, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		if (!FoxNaming.mayName(player, target)) {
			cir.setReturnValue(InteractionResult.PASS);
		}
	}

	@Inject(method = "interactLivingEntity", at = @At("RETURN"))
	private void foxyfriends$rewardNaming(ItemStack stack, Player player, LivingEntity target, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		if (cir.getReturnValue() == InteractionResult.SUCCESS) {
			FoxNaming.onNamed(player, target);
		}
	}
}
