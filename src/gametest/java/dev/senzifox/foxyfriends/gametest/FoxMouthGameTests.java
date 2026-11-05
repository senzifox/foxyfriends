package dev.senzifox.foxyfriends.gametest;

import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

public class FoxMouthGameTests {
	private static final BlockPos FOX_POS = new BlockPos(1, 1, 1);

	@GameTest
	public void ownerTakesItemFromMouth(GameTestHelper helper) {
		Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
		Fox fox = spawnHolding(helper, owner);

		sneakClick(fox, owner);

		helper.assertTrue(fox.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty(), "fox mouth should be empty");
		helper.assertTrue(owner.getMainHandItem().is(Items.STICK), "owner should hold the stick");
		helper.succeed();
	}

	@GameTest
	public void strangerCannotTakeItemFromMouth(GameTestHelper helper) {
		Fox fox = spawnHolding(helper, helper.makeMockPlayer(GameType.SURVIVAL));
		Player stranger = helper.makeMockPlayer(GameType.SURVIVAL);

		sneakClick(fox, stranger);

		helper.assertTrue(fox.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.STICK), "fox should keep the stick");
		helper.assertTrue(stranger.getMainHandItem().isEmpty(), "stranger hand should stay empty");
		helper.succeed();
	}

	private static Fox spawnHolding(GameTestHelper helper, Player owner) {
		Fox fox = helper.spawn(EntityTypes.FOX, FOX_POS);
		fox.setNoAi(true);
		FoxOwnership.tame(fox, owner);
		fox.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STICK));
		return fox;
	}

	private static void sneakClick(Fox fox, Player player) {
		player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		player.setShiftKeyDown(true);
		player.interactOn(fox, InteractionHand.MAIN_HAND, Vec3.ZERO);
	}
}
