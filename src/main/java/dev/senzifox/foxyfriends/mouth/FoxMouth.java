package dev.senzifox.foxyfriends.mouth;

import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public final class FoxMouth {
	private FoxMouth() {
	}

	public static @Nullable InteractionResult interact(Fox fox, Player player, InteractionHand hand) {
		ItemStack held = fox.getItemBySlot(EquipmentSlot.MAINHAND);
		if (hand != InteractionHand.MAIN_HAND
			|| !player.isSecondaryUseActive()
			|| !player.getItemInHand(hand).isEmpty()
			|| held.isEmpty()
			|| !FoxOwnership.isOwnedBy(fox, player)) {
			return null;
		}

		if (!fox.level().isClientSide()) {
			player.setItemInHand(hand, held);
			fox.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
			fox.playSound(SoundEvents.ITEM_PICKUP);
		}

		return InteractionResult.SUCCESS;
	}
}
