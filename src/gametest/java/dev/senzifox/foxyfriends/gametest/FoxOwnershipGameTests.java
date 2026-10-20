package dev.senzifox.foxyfriends.gametest;

import dev.senzifox.foxyfriends.mixin.FoxAccessor;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;

public class FoxOwnershipGameTests {
	private static final BlockPos FOX_POS = new BlockPos(1, 1, 1);

	@GameTest
	public void ownerSurvivesSaveAndLoad(GameTestHelper helper) {
		Fox fox = helper.spawn(EntityTypes.FOX, FOX_POS);
		Player player = helper.makeMockPlayer(GameType.SURVIVAL);
		FoxOwnership.tame(fox, player);

		TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
		fox.saveWithoutId(output);
		Fox loaded = EntityTypes.FOX.create(helper.getLevel(), EntitySpawnReason.LOAD);
		loaded.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), output.buildResult()));

		helper.assertTrue(FoxOwnership.isOwnedBy(loaded, player), "loaded fox should keep its owner");
		helper.assertTrue(((FoxAccessor) loaded).foxyfriends$trusts(player), "loaded fox should still trust its owner");
		helper.succeed();
	}
}
