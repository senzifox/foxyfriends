package dev.senzifox.foxyfriends.gametest;

import dev.senzifox.foxyfriends.mode.FoxMode;
import dev.senzifox.foxyfriends.mode.FoxModes;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.Vec3;

public class FoxModeGameTests {
	private static final BlockPos FOX_POS = new BlockPos(1, 1, 1);

	@GameTest
	public void strangerCannotCommand(GameTestHelper helper) {
		Fox fox = helper.spawn(EntityTypes.FOX, FOX_POS);
		FoxOwnership.tame(fox, helper.makeMockPlayer(GameType.SURVIVAL));

		click(fox, helper.makeMockPlayer(GameType.SURVIVAL), ItemStack.EMPTY);

		helper.assertValueEqual(FoxModes.getMode(fox), FoxMode.FOLLOW, "mode");
		helper.succeed();
	}

	@GameTest
	public void clickWithItemDoesNotCommand(GameTestHelper helper) {
		Fox fox = helper.spawn(EntityTypes.FOX, FOX_POS);
		Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
		FoxOwnership.tame(fox, owner);

		click(fox, owner, new ItemStack(Items.STICK));

		helper.assertValueEqual(FoxModes.getMode(fox), FoxMode.FOLLOW, "mode");
		helper.succeed();
	}

	@GameTest
	public void modeSurvivesSaveAndLoad(GameTestHelper helper) {
		Fox fox = helper.spawn(EntityTypes.FOX, FOX_POS);
		FoxOwnership.tame(fox, helper.makeMockPlayer(GameType.SURVIVAL));
		FoxModes.setMode(fox, FoxMode.WANDER);

		TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
		fox.saveWithoutId(output);
		Fox loaded = EntityTypes.FOX.create(helper.getLevel(), EntitySpawnReason.LOAD);
		loaded.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), output.buildResult()));

		helper.assertValueEqual(FoxModes.getMode(loaded), FoxMode.WANDER, "loaded mode");
		helper.assertValueEqual(loaded.getHomePosition(), fox.getHomePosition(), "loaded wander point");
		helper.assertValueEqual(loaded.getHomeRadius(), FoxModes.WANDER_RADIUS, "loaded wander radius");
		helper.succeed();
	}

	private static void click(Fox fox, Player player, ItemStack stack) {
		player.setItemInHand(InteractionHand.MAIN_HAND, stack);
		player.interactOn(fox, InteractionHand.MAIN_HAND, Vec3.ZERO);
	}
}
