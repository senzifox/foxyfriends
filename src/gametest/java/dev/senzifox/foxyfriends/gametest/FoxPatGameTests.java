package dev.senzifox.foxyfriends.gametest;

import dev.senzifox.foxyfriends.pat.FoxPatting;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

public class FoxPatGameTests {
	private static final BlockPos FOX_POS = new BlockPos(1, 1, 1);

	@GameTest
	public void ownerPatCalmsFox(GameTestHelper helper) {
		Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
		Fox fox = spawnTame(helper, owner);

		pat(fox, owner);

		helper.assertTrue(FoxPatting.isCalm(fox), "fox should be calm");
		helper.succeed();
	}

	@GameTest
	public void strangerPatDoesNotCalmFox(GameTestHelper helper) {
		Fox fox = spawnTame(helper, helper.makeMockPlayer(GameType.SURVIVAL));

		pat(fox, helper.makeMockPlayer(GameType.SURVIVAL));

		helper.assertFalse(FoxPatting.isCalm(fox), "fox should not be calm");
		helper.succeed();
	}

	private static Fox spawnTame(GameTestHelper helper, Player owner) {
		Fox fox = helper.spawn(EntityTypes.FOX, FOX_POS);
		FoxOwnership.tame(fox, owner);
		return fox;
	}

	private static void pat(Fox fox, Player player) {
		player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		player.setShiftKeyDown(true);
		player.interactOn(fox, InteractionHand.MAIN_HAND, Vec3.ZERO);
	}
}
