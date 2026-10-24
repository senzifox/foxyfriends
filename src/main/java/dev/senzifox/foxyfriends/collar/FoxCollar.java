package dev.senzifox.foxyfriends.collar;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public final class FoxCollar {
	public static final DyeColor DEFAULT_COLOR = DyeColor.RED;

	public static final AttachmentType<DyeColor> COLOR = AttachmentRegistry.create(
		FoxyFriends.id("collar"),
		builder -> builder
			.persistent(DyeColor.CODEC)
			.syncWith(DyeColor.STREAM_CODEC, AttachmentSyncPredicate.all())
	);

	private FoxCollar() {
	}

	public static void init() {
	}

	public static DyeColor getColor(Fox fox) {
		return fox.getAttachedOrElse(COLOR, DEFAULT_COLOR);
	}

	public static @Nullable InteractionResult interact(Fox fox, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		DyeColor color = stack.get(DataComponents.DYE);
		if (color == null || color == getColor(fox) || !FoxOwnership.isOwnedBy(fox, player)) {
			return null;
		}

		if (!fox.level().isClientSide()) {
			fox.setAttached(COLOR, color);
			stack.consume(1, player);
			fox.level().playSound(null, fox, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
		}

		return InteractionResult.SUCCESS;
	}
}
