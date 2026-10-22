package dev.senzifox.foxyfriends.gametest;

import dev.senzifox.foxyfriends.defense.FoxTargets;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

public class FoxDefenseGameTests {
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

	private static <T extends LivingEntity> T create(GameTestHelper helper, EntityType<T> type) {
		return type.create(helper.getLevel(), EntitySpawnReason.TRIGGERED);
	}
}
