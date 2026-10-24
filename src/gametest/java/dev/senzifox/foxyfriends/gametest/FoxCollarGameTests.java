package dev.senzifox.foxyfriends.gametest;

import dev.senzifox.foxyfriends.collar.FoxCollar;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.Vec3;

public class FoxCollarGameTests {
	private static final BlockPos FOX_POS = new BlockPos(1, 0, 1);

	@GameTest
	public void strangerCannotDyeCollar(GameTestHelper helper) {
		Fox fox = spawnTame(helper, helper.makeMockPlayer(GameType.SURVIVAL));

		ItemStack dye = dye(fox, helper.makeMockPlayer(GameType.SURVIVAL), DyeColor.BLUE);

		helper.assertValueEqual(FoxCollar.getColor(fox), DyeColor.RED, "collar color");
		helper.assertValueEqual(dye.getCount(), 1, "dye count");
		helper.succeed();
	}

	@GameTest
	public void collarColorSurvivesReload(GameTestHelper helper) {
		Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
		Fox fox = spawnTame(helper, owner);
		dye(fox, owner, DyeColor.LIME);

		TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
		fox.saveWithoutId(output);
		Fox loaded = EntityTypes.FOX.create(helper.getLevel(), EntitySpawnReason.LOAD);
		loaded.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), output.buildResult()));

		helper.assertValueEqual(FoxCollar.getColor(loaded), DyeColor.LIME, "collar color after reload");
		helper.succeed();
	}

	private static Fox spawnTame(GameTestHelper helper, Player owner) {
		Fox fox = helper.spawn(EntityTypes.FOX, FOX_POS);
		FoxOwnership.tame(fox, owner);
		return fox;
	}

	private static ItemStack dye(Fox fox, Player player, DyeColor color) {
		player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DYE.pick(color)));
		player.interactOn(fox, InteractionHand.MAIN_HAND, Vec3.ZERO);
		return player.getItemInHand(InteractionHand.MAIN_HAND);
	}
}
