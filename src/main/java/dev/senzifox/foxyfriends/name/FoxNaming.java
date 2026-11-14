package dev.senzifox.foxyfriends.name;

import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;

public final class FoxNaming {
	private FoxNaming() {
	}

	public static boolean mayName(Player player, LivingEntity target) {
		return !(target instanceof Fox fox) || !FoxOwnership.isTame(fox) || FoxOwnership.isOwnedBy(fox, player);
	}

	public static void onNamed(Player player, LivingEntity target) {
		if (player instanceof ServerPlayer serverPlayer && target instanceof Fox fox && FoxOwnership.isOwnedBy(fox, serverPlayer)) {
			FoxAdvancements.NAMED_FOX.trigger(serverPlayer);
		}
	}
}
