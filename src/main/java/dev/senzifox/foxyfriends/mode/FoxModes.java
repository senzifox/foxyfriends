package dev.senzifox.foxyfriends.mode;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.mixin.FoxAccessor;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public final class FoxModes {
	public static final AttachmentType<FoxMode> MODE = AttachmentRegistry.create(
		FoxyFriends.id("mode"),
		builder -> builder
			.persistent(FoxMode.CODEC)
			.syncWith(FoxMode.STREAM_CODEC, AttachmentSyncPredicate.all())
	);

	private FoxModes() {
	}

	public static void init() {
	}

	public static FoxMode getMode(Fox fox) {
		return fox.getAttachedOrElse(MODE, FoxMode.FOLLOW);
	}

	public static boolean isInMode(Fox fox, FoxMode mode) {
		return FoxOwnership.isTame(fox) && getMode(fox) == mode;
	}

	public static void setMode(Fox fox, FoxMode mode) {
		fox.setAttached(MODE, mode);
		((FoxAccessor) fox).foxyfriends$clearStates();
		fox.getNavigation().stop();
		fox.setTarget(null);
		if (mode == FoxMode.SIT) {
			fox.setSitting(true);
		}
	}

	public static @Nullable InteractionResult interact(Fox fox, Player player, InteractionHand hand) {
		if (hand != InteractionHand.MAIN_HAND || !player.getItemInHand(hand).isEmpty() || !FoxOwnership.isOwnedBy(fox, player)) {
			return null;
		}

		if (!fox.level().isClientSide()) {
			FoxMode mode = getMode(fox).next();
			setMode(fox, mode);
			player.sendOverlayMessage(Component.translatable("message.foxyfriends.mode." + mode.getSerializedName(), fox.getDisplayName()));
		}

		return InteractionResult.SUCCESS;
	}
}
