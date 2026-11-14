package dev.senzifox.foxyfriends.gametest;

import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

public class FoxNamingGameTests {
	private static final BlockPos FOX_POS = new BlockPos(1, 0, 1);
	private static final String NAME = "Rusty";

	@GameTest
	public void strangerCannotNameTamedFox(GameTestHelper helper) {
		Fox fox = spawnTame(helper, helper.makeMockPlayer(GameType.SURVIVAL));

		ItemStack tag = name(fox, helper.makeMockPlayer(GameType.SURVIVAL));

		helper.assertTrue(fox.getCustomName() == null, "stranger should not name a tamed fox");
		helper.assertValueEqual(tag.getCount(), 1, "name tag count");
		helper.succeed();
	}

	@GameTest
	public void ownerCanNameTamedFox(GameTestHelper helper) {
		Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
		Fox fox = spawnTame(helper, owner);

		ItemStack tag = name(fox, owner);

		helper.assertValueEqual(fox.getCustomName() == null ? null : fox.getCustomName().getString(), NAME, "fox name");
		helper.assertTrue(tag.isEmpty(), "name tag should be used up");
		helper.succeed();
	}

	private static Fox spawnTame(GameTestHelper helper, Player owner) {
		Fox fox = helper.spawn(EntityTypes.FOX, FOX_POS);
		FoxOwnership.tame(fox, owner);
		return fox;
	}

	private static ItemStack name(Fox fox, Player player) {
		ItemStack tag = new ItemStack(Items.NAME_TAG);
		tag.set(DataComponents.CUSTOM_NAME, Component.literal(NAME));
		player.setItemInHand(InteractionHand.MAIN_HAND, tag);
		player.interactOn(fox, InteractionHand.MAIN_HAND, Vec3.ZERO);
		return player.getItemInHand(InteractionHand.MAIN_HAND);
	}
}
