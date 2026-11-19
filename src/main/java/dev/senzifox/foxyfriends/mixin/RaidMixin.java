package dev.senzifox.foxyfriends.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.senzifox.foxyfriends.defense.FoxRaids;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Raid.class)
public abstract class RaidMixin {
	@WrapOperation(
		method = "tick",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/advancements/triggers/PlayerTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;)V"
		)
	)
	private void foxyfriends$rewardRaidWithFox(PlayerTrigger trigger, ServerPlayer hero, Operation<Void> original) {
		original.call(trigger, hero);
		FoxRaids.onRaidWon(hero);
	}
}
