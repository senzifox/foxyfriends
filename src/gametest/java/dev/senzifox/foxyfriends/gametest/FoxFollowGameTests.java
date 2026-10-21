package dev.senzifox.foxyfriends.gametest;

import dev.senzifox.foxyfriends.mode.FoxMode;
import dev.senzifox.foxyfriends.mode.FoxModes;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.GameType;

public class FoxFollowGameTests {
	private static final BlockPos FOX_POS = new BlockPos(1, 0, 1);
	private static final BlockPos PREY_POS = new BlockPos(3, 0, 1);
	private static final int HUNT_TICKS = 100;

	@GameTest(maxTicks = HUNT_TICKS + 20)
	public void followingFoxDoesNotHunt(GameTestHelper helper) {
		assertFoxIgnoresPrey(helper, FoxMode.FOLLOW);
	}

	private static void assertFoxIgnoresPrey(GameTestHelper helper, FoxMode mode) {
		Fox fox = spawnHunter(helper, mode);
		Chicken chicken = helper.spawn(EntityTypes.CHICKEN, PREY_POS);

		helper.runAfterDelay(HUNT_TICKS, () -> {
			helper.assertTrue(fox.getTarget() == null, "fox should not target prey in mode " + mode.getSerializedName());
			helper.assertTrue(chicken.isAlive(), "prey should survive");
			helper.succeed();
		});
	}

	private static Fox spawnHunter(GameTestHelper helper, FoxMode mode) {
		Fox fox = helper.spawn(EntityTypes.FOX, FOX_POS, EntitySpawnReason.SPAWN_ITEM_USE);
		fox.setAge(0);
		FoxOwnership.tame(fox, helper.makeMockPlayer(GameType.SURVIVAL));
		FoxModes.setMode(fox, mode);
		return fox;
	}
}
