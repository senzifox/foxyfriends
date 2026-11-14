package dev.senzifox.foxyfriends.name;

import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;

public final class FoxNaming {
	private FoxNaming() {
	}

	public static boolean mayName(Player player, LivingEntity target) {
		return !(target instanceof Fox fox) || !FoxOwnership.isTame(fox) || FoxOwnership.isOwnedBy(fox, player);
	}
}
