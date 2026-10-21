package dev.senzifox.foxyfriends.mode;

import dev.senzifox.foxyfriends.mixin.FoxAccessor;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jspecify.annotations.Nullable;

public class FoxFollowOwnerGoal extends Goal {
	private static final double SPEED = 1.2;
	private static final double START_DISTANCE = 10.0;
	private static final double STOP_DISTANCE = 2.0;
	private static final double TELEPORT_DISTANCE = 12.0;
	private static final int TELEPORT_ATTEMPTS = 10;

	private final Fox fox;
	private @Nullable LivingEntity owner;
	private int timeToRecalcPath;
	private float oldWaterCost;

	public FoxFollowOwnerGoal(Fox fox) {
		this.fox = fox;
		setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		LivingEntity candidate = FoxOwnership.getOwner(fox);
		if (candidate == null || !canFollow(candidate) || fox.distanceToSqr(candidate) < START_DISTANCE * START_DISTANCE) {
			return false;
		}

		owner = candidate;
		return true;
	}

	@Override
	public boolean canContinueToUse() {
		return owner != null
			&& canFollow(owner)
			&& !fox.getNavigation().isDone()
			&& fox.distanceToSqr(owner) > STOP_DISTANCE * STOP_DISTANCE;
	}

	@Override
	public void start() {
		((FoxAccessor) fox).foxyfriends$clearStates();
		timeToRecalcPath = 0;
		oldWaterCost = fox.getPathfindingMalus(PathType.WATER);
		fox.setPathfindingMalus(PathType.WATER, 0.0F);
	}

	@Override
	public void stop() {
		owner = null;
		fox.getNavigation().stop();
		fox.setPathfindingMalus(PathType.WATER, oldWaterCost);
	}

	@Override
	public void tick() {
		if (owner == null) {
			return;
		}

		boolean farAway = fox.distanceToSqr(owner) >= TELEPORT_DISTANCE * TELEPORT_DISTANCE;
		if (!farAway) {
			fox.getLookControl().setLookAt(owner, 10.0F, fox.getMaxHeadXRot());
		}

		if (--timeToRecalcPath <= 0) {
			timeToRecalcPath = adjustedTickDelay(10);
			if (farAway) {
				teleportNear(owner.blockPosition());
			} else {
				fox.getNavigation().moveTo(owner, SPEED);
			}
		}
	}

	private boolean canFollow(LivingEntity target) {
		return FoxModes.isInMode(fox, FoxMode.FOLLOW) && !target.isSpectator() && !fox.isPassenger() && !fox.isLeashed();
	}

	private void teleportNear(BlockPos target) {
		for (int attempt = 0; attempt < TELEPORT_ATTEMPTS; attempt++) {
			int dx = fox.getRandom().nextIntBetweenInclusive(-3, 3);
			int dz = fox.getRandom().nextIntBetweenInclusive(-3, 3);
			if (Math.abs(dx) < 2 && Math.abs(dz) < 2) {
				continue;
			}

			BlockPos pos = target.offset(dx, fox.getRandom().nextIntBetweenInclusive(-1, 1), dz);
			if (canTeleportTo(pos)) {
				fox.snapTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, fox.getYRot(), fox.getXRot());
				fox.getNavigation().stop();
				return;
			}
		}
	}

	private boolean canTeleportTo(BlockPos pos) {
		return WalkNodeEvaluator.getPathTypeStatic(fox, pos) == PathType.WALKABLE
			&& !(fox.level().getBlockState(pos.below()).getBlock() instanceof LeavesBlock)
			&& fox.level().noCollision(fox, fox.getBoundingBox().move(pos.subtract(fox.blockPosition())));
	}
}
