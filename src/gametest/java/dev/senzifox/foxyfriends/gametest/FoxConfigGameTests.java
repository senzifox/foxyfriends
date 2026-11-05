package dev.senzifox.foxyfriends.gametest;

import dev.senzifox.foxyfriends.config.FoxConfig;
import dev.senzifox.foxyfriends.config.FoxyFriendsConfig;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

public class FoxConfigGameTests {
	@GameTest
	public void missingFieldsFallBackToDefaults(GameTestHelper helper) {
		FoxyFriendsConfig config = FoxConfig.parse("{\"wander_hunting\": false}").getOrThrow();

		helper.assertValueEqual(config, new FoxyFriendsConfig(
			false,
			FoxyFriendsConfig.DEFAULT.wildTameChance(),
			FoxyFriendsConfig.DEFAULT.tamedMaxHealth(),
			FoxyFriendsConfig.DEFAULT.tamedAttackDamage(),
			FoxyFriendsConfig.DEFAULT.regenAmount(),
			FoxyFriendsConfig.DEFAULT.regenIntervalTicks(),
			FoxyFriendsConfig.DEFAULT.regenCalmTicks()
		), "config");
		helper.succeed();
	}

	@GameTest
	public void brokenConfigIsRejected(GameTestHelper helper) {
		helper.assertTrue(FoxConfig.parse("{\"wild_tame_chance\": 2}").isError(), "out of range chance should be rejected");
		helper.assertTrue(FoxConfig.parse("{\"regen_interval_ticks\": 0}").isError(), "zero interval should be rejected");
		helper.assertTrue(FoxConfig.parse("{ not json").isError(), "malformed json should be rejected");
		helper.succeed();
	}
}
