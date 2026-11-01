package dev.senzifox.foxyfriends.gametest;

import dev.senzifox.foxyfriends.defense.FoxTargets;
import dev.senzifox.foxyfriends.mode.FoxMode;
import dev.senzifox.foxyfriends.mode.FoxModes;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class FoxDefenseGameTests {
	private static final BlockPos FOX_POS = new BlockPos(1, 0, 1);
	private static final BlockPos OWNER_POS = new BlockPos(3, 0, 1);
	private static final BlockPos ATTACKER_POS = new BlockPos(1, 0, 3);
	private static final int SETTLE_TICKS = 5;
	private static final int IGNORE_TICKS = 20;

	@GameTest
	public void targetFilterSkipsProtectedEntities(GameTestHelper helper) {
		Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
		Fox tamedFox = create(helper, EntityTypes.FOX);
		FoxOwnership.tame(tamedFox, owner);

		helper.assertTrue(FoxTargets.wantsToAttack(create(helper, EntityTypes.ZOMBIE), owner), "zombie should be a valid target");
		helper.assertFalse(FoxTargets.wantsToAttack(create(helper, EntityTypes.CREEPER), owner), "creeper should be skipped");
		helper.assertFalse(FoxTargets.wantsToAttack(create(helper, EntityTypes.IRON_GOLEM), owner), "iron golem should be skipped");
		helper.assertFalse(FoxTargets.wantsToAttack(create(helper, EntityTypes.WARDEN), owner), "warden should be skipped");
		helper.assertFalse(FoxTargets.wantsToAttack(tamedFox, owner), "tamed fox should be skipped");
		helper.succeed();
	}

	@GameTest(maxTicks = SETTLE_TICKS + IGNORE_TICKS + 20)
	public void foxForgivesItsOwner(GameTestHelper helper) {
		Villager owner = helper.spawnWithNoFreeWill(EntityTypes.VILLAGER, OWNER_POS);
		Fox fox = spawnGuard(helper, owner);

		helper.startSequence()
			.thenIdle(SETTLE_TICKS)
			.thenExecute(() -> fox.hurtServer(helper.getLevel(), helper.getLevel().damageSources().mobAttack(owner), 1.0F))
			.thenIdle(IGNORE_TICKS)
			.thenExecute(() -> helper.assertTrue(fox.getTarget() == null, "fox should not attack its owner"))
			.thenSucceed();
	}

	@GameTest(maxTicks = SETTLE_TICKS * 3 + IGNORE_TICKS + 20)
	public void modeChangeEndsRetaliation(GameTestHelper helper) {
		Villager owner = helper.spawnWithNoFreeWill(EntityTypes.VILLAGER, OWNER_POS);
		Fox fox = spawnGuard(helper, owner);
		FoxModes.setMode(fox, FoxMode.WANDER);
		Zombie zombie = helper.spawnWithNoFreeWill(EntityTypes.ZOMBIE, ATTACKER_POS);

		helper.startSequence()
			.thenIdle(SETTLE_TICKS)
			.thenExecute(() -> fox.hurtServer(helper.getLevel(), helper.getLevel().damageSources().mobAttack(zombie), 1.0F))
			.thenIdle(SETTLE_TICKS)
			.thenExecute(() -> helper.assertTrue(fox.getTarget() == zombie, "fox should strike back before the mode change"))
			.thenExecute(() -> FoxModes.setMode(fox, FoxMode.FOLLOW))
			.thenIdle(IGNORE_TICKS)
			.thenExecute(() -> helper.assertTrue(fox.getTarget() == null, "mode change should end retaliation"))
			.thenSucceed();
	}

	private static Fox spawnGuard(GameTestHelper helper, LivingEntity owner) {
		Fox fox = helper.spawn(EntityTypes.FOX, FOX_POS);
		fox.setAge(0);
		fox.setAttached(FoxOwnership.OWNER, EntityReference.of(owner));
		return fox;
	}

	private static <T extends LivingEntity> T create(GameTestHelper helper, EntityType<T> type) {
		return type.create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
	}
}
