package dev.senzifox.foxyfriends.defense;

import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.mode.FoxMode;
import dev.senzifox.foxyfriends.mode.FoxModes;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.fox.Fox;

public final class FoxRaids {
	public static final double ALLY_RADIUS = 32.0;

	private FoxRaids() {
	}

	public static void onRaidWon(ServerPlayer hero) {
		boolean foxAtSide = !hero.level().getEntitiesOfClass(Fox.class, hero.getBoundingBox().inflate(ALLY_RADIUS), fox -> isAlly(fox, hero)).isEmpty();
		if (foxAtSide) {
			FoxAdvancements.RAID_WON_WITH_FOX.trigger(hero);
		}
	}

	private static boolean isAlly(Fox fox, ServerPlayer hero) {
		return fox.isAlive() && FoxOwnership.isOwnedBy(fox, hero) && !FoxModes.isInMode(fox, FoxMode.SIT);
	}
}
