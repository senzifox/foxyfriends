package dev.senzifox.foxyfriends.gametest;

import dev.senzifox.foxyfriends.mode.FoxWaiting;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;

public class FoxWaitingGameTests {
	private static final BlockPos FOX_POS = new BlockPos(1, 1, 1);
	private static final long LAST_VISIT = 123457L;

	@GameTest
	public void lastOwnerVisitSurvivesSaveAndLoad(GameTestHelper helper) {
		Fox fox = helper.spawn(EntityTypes.FOX, FOX_POS);
		fox.setAttached(FoxWaiting.LAST_OWNER_VISIT, LAST_VISIT);

		TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
		fox.saveWithoutId(output);
		Fox loaded = EntityTypes.FOX.create(helper.getLevel(), EntitySpawnReason.LOAD);
		loaded.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), output.buildResult()));

		Long restored = loaded.getAttached(FoxWaiting.LAST_OWNER_VISIT);
		helper.assertTrue(restored != null && restored == LAST_VISIT, "loaded fox should remember when its owner last visited");
		helper.succeed();
	}
}
