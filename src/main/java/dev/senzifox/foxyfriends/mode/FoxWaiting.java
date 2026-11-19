package dev.senzifox.foxyfriends.mode;

import com.mojang.serialization.Codec;
import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.fox.Fox;

public final class FoxWaiting {
	public static final AttachmentType<Long> LAST_OWNER_VISIT = AttachmentRegistry.create(
		FoxyFriends.id("last_owner_visit"),
		builder -> builder.persistent(Codec.LONG)
	);

	private static final int CHECK_INTERVAL = 20;
	private static final double OWNER_RANGE = 16.0;
	private static final long LONG_ABSENCE = 24000L;

	private FoxWaiting() {
	}

	public static void tick(Fox fox) {
		if (fox.tickCount % CHECK_INTERVAL != 0 || !(fox.level() instanceof ServerLevel level)) {
			return;
		}

		if (!(FoxOwnership.getOwner(fox) instanceof ServerPlayer owner) || !fox.closerThan(owner, OWNER_RANGE)) {
			return;
		}

		long now = level.getGameTime();
		Long lastVisit = fox.getAttached(LAST_OWNER_VISIT);
		if (lastVisit != null && now - lastVisit >= LONG_ABSENCE && isWaiting(fox)) {
			FoxAdvancements.CAME_BACK_TO_FOX.trigger(owner);
		}
		fox.setAttached(LAST_OWNER_VISIT, now);
	}

	private static boolean isWaiting(Fox fox) {
		return FoxModes.isInMode(fox, FoxMode.SIT) || FoxModes.isInMode(fox, FoxMode.WANDER);
	}
}
