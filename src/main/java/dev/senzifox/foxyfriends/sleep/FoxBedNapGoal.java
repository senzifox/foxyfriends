package dev.senzifox.foxyfriends.sleep;

import dev.senzifox.foxyfriends.mode.FoxMode;
import dev.senzifox.foxyfriends.mode.FoxModes;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.LevelReader;

public class FoxBedNapGoal extends MoveToBlockGoal {
	private static final double SPEED = 1.0;
	private static final int SEARCH_RANGE = 6;
	private static final int VERTICAL_SEARCH_RANGE = 2;
	private static final double ARRIVE_DISTANCE = 1.25;
	private static final int GIVE_UP_TICKS = 1200;

	private final Fox fox;
	private final FoxNaps naps;

	public FoxBedNapGoal(Fox fox) {
		super(fox, SPEED, SEARCH_RANGE, VERTICAL_SEARCH_RANGE);
		this.fox = fox;
		this.naps = new FoxNaps(fox);
		setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		return FoxModes.isInMode(fox, FoxMode.WANDER)
			&& !fox.isPassenger()
			&& !naps.inDanger()
			&& naps.isNapTime()
			&& super.canUse()
			&& naps.canDozeAt(getMoveToTarget());
	}

	@Override
	public boolean canContinueToUse() {
		if (!FoxModes.isInMode(fox, FoxMode.WANDER) || naps.inDanger() || tryTicks > GIVE_UP_TICKS || !isValidTarget(fox.level(), blockPos)) {
			return false;
		}

		return isReachedTarget() ? naps.canDoze() : naps.isNapTime();
	}

	@Override
	public void start() {
		naps.startSettling();
		naps.wakeUp();
		fox.setSitting(false);
		super.start();
	}

	@Override
	public void stop() {
		super.stop();
		naps.wakeUp();
	}

	@Override
	public void tick() {
		super.tick();
		if (isReachedTarget() && !fox.isSleeping() && naps.settleOn(blockPos)) {
			naps.fallAsleep();
		}
	}

	@Override
	public double acceptedDistance() {
		return ARRIVE_DISTANCE;
	}

	@Override
	protected boolean isValidTarget(LevelReader level, BlockPos pos) {
		return level.getBlockState(pos).is(BlockTags.BEDS)
			&& level.isEmptyBlock(pos.above())
			&& !naps.isSpotTaken(pos);
	}
}
