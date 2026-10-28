package dev.senzifox.foxyfriends.gametest;

import com.mojang.authlib.GameProfile;
import dev.senzifox.foxyfriends.mode.FoxMode;
import dev.senzifox.foxyfriends.mode.FoxModes;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import io.netty.channel.embedded.EmbeddedChannel;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

public class FoxFleeGameTests {
	private static final BlockPos CORRIDOR_START = new BlockPos(1, 0, 1);
	private static final int CORRIDOR_LENGTH = 16;
	private static final BlockPos FOX_POS = new BlockPos(1, 0, 9);
	private static final BlockPos STRANGER_POS = new BlockPos(1, 0, 6);
	private static final int FLEE_TICKS = 60;
	private static final int FLEE_PADDING = 32;
	private static final long MIDNIGHT = 18000L;

	@GameTest(maxTicks = FLEE_TICKS + 20, padding = FLEE_PADDING)
	public void tamedFoxDoesNotFleeFromStrangers(GameTestHelper helper) {
		Fox fox = spawnFox(helper);
		FoxOwnership.tame(fox, helper.makeMockPlayer(GameType.SURVIVAL));
		FoxModes.setMode(fox, FoxMode.WANDER);
		ServerPlayer stranger = placeStranger(helper);
		AtomicBoolean fled = watchFleeing(helper, fox);

		helper.runAfterDelay(FLEE_TICKS, () -> {
			helper.getLevel().getServer().getPlayerList().remove(stranger);
			helper.assertFalse(fled.get(), "tamed fox should not flee from stranger");
			helper.succeed();
		});
	}

	private static Fox spawnFox(GameTestHelper helper) {
		helper.setTime(MIDNIGHT);
		GameTestArena.buildCorridor(helper, CORRIDOR_START, CORRIDOR_LENGTH);
		Fox fox = helper.spawn(EntityTypes.FOX, FOX_POS);
		fox.setAge(0);
		return fox;
	}

	private static ServerPlayer placeStranger(GameTestHelper helper) {
		CommonListenerCookie cookie = CommonListenerCookie.createInitial(new GameProfile(UUID.randomUUID(), "stranger"), false);
		ServerPlayer stranger = new ServerPlayer(helper.getLevel().getServer(), helper.getLevel(), cookie.gameProfile(), cookie.clientInformation());
		Connection connection = new Connection(PacketFlow.SERVERBOUND);
		new EmbeddedChannel(connection);
		helper.getLevel().getServer().getPlayerList().placeNewPlayer(connection, stranger, cookie);
		stranger.setGameMode(GameType.SURVIVAL);
		Vec3 pos = helper.absoluteVec(Vec3.atBottomCenterOf(STRANGER_POS));
		stranger.snapTo(pos.x, pos.y, pos.z, 0.0F, 0.0F);
		return stranger;
	}

	private static AtomicBoolean watchFleeing(GameTestHelper helper, Fox fox) {
		AtomicBoolean fled = new AtomicBoolean();
		helper.onEachTick(() -> {
			if (fox.getGoalSelector().getAvailableGoals().stream().anyMatch(goal -> goal.isRunning() && goal.getGoal() instanceof AvoidEntityGoal)) {
				fled.set(true);
			}
		});
		return fled;
	}
}
