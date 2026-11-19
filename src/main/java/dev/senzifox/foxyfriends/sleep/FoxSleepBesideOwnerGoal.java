package dev.senzifox.foxyfriends.sleep;

import dev.senzifox.foxyfriends.mode.FoxMode;
import dev.senzifox.foxyfriends.mode.FoxModes;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class FoxSleepBesideOwnerGoal extends Goal {
	private static final double RANGE = 10.0;
	private static final double SPEED = 1.2;
	private static final double ARRIVE_DISTANCE = 1.0;
	private static final int WAKE_DELAY_MIN = 20;
	private static final int WAKE_DELAY_SPREAD = 41;

	private final Fox fox;
	private final FoxNaps naps;
	private @Nullable Player owner;
	private @Nullable BlockPos bedFoot;
	private int wakeDelay;

	public FoxSleepBesideOwnerGoal(Fox fox) {
		this.fox = fox;
		this.naps = new FoxNaps(fox);
		setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		if (!(FoxModes.isInMode(fox, FoxMode.FOLLOW) || FoxModes.isInMode(fox, FoxMode.WANDER))
			|| !(FoxOwnership.getOwner(fox) instanceof Player candidate)
			|| !candidate.isSleeping()
			|| fox.distanceToSqr(candidate) > RANGE * RANGE
			|| fox.isPassenger()
			|| fox.isLeashed()
			|| naps.inDanger()) {
			return false;
		}

		BlockPos foot = candidate.getSleepingPos().map(this::footOf).orElse(null);
		if (foot == null || naps.isSpotTaken(foot)) {
			return false;
		}

		owner = candidate;
		bedFoot = foot;
		return true;
	}

	@Override
	public boolean canContinueToUse() {
		if (owner == null || bedFoot == null || naps.inDanger() || naps.isSpotTaken(bedFoot)) {
			return false;
		}

		return ownerAsleep() || fox.isSleeping() && wakeDelay > 0;
	}

	@Override
	public void start() {
		wakeDelay = WAKE_DELAY_MIN + fox.getRandom().nextInt(WAKE_DELAY_SPREAD);
		naps.startSettling();
		naps.wakeUp();
		fox.setSitting(false);
	}

	@Override
	public void stop() {
		owner = null;
		bedFoot = null;
		naps.wakeUp();
		fox.setSitting(false);
		fox.getNavigation().stop();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		if (owner == null || bedFoot == null) {
			return;
		}

		if (!ownerAsleep()) {
			wakeDelay--;
			return;
		}

		Vec3 spot = Vec3.atBottomCenterOf(bedFoot);
		if (fox.distanceToSqr(spot) > ARRIVE_DISTANCE * ARRIVE_DISTANCE) {
			fox.getNavigation().moveTo(spot.x, spot.y, spot.z, SPEED);
			return;
		}

		if (!fox.isSleeping() && !naps.settleOn(bedFoot)) {
			return;
		}

		if (fox.level().isThundering()) {
			fox.setSitting(true);
		} else if (!fox.isSleeping()) {
			naps.fallAsleep();
		}
	}

	private boolean ownerAsleep() {
		return owner != null && owner.isSleeping() && !owner.isRemoved();
	}

	private BlockPos footOf(BlockPos sleepingPos) {
		BlockState state = fox.level().getBlockState(sleepingPos);
		return state.getOptionalValue(BedBlock.FACING)
			.map(facing -> sleepingPos.relative(facing.getOpposite()))
			.orElse(sleepingPos);
	}
}
