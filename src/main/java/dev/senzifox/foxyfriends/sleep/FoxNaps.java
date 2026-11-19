package dev.senzifox.foxyfriends.sleep;

import dev.senzifox.foxyfriends.defense.FoxTargets;
import dev.senzifox.foxyfriends.mixin.FoxAccessor;
import dev.senzifox.foxyfriends.mode.FoxModes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class FoxNaps {
	private static final double ALERT_RANGE = 12.0;
	private static final double ALERT_HEIGHT = 6.0;
	private static final double TAKEN_RADIUS = 1.0;
	private static final double SETTLE_SPEED = 0.6;
	private static final double SETTLED_DISTANCE = 0.1;
	private static final int SETTLE_TICKS = 40;

	private final Fox fox;
	private final TargetingConditions alertTargeting;
	private int settleTicks;

	public FoxNaps(Fox fox) {
		this.fox = fox;
		this.alertTargeting = TargetingConditions.forCombat()
			.range(ALERT_RANGE)
			.ignoreLineOfSight()
			.selector(fox.new FoxAlertableEntitiesSelector());
	}

	public boolean inDanger() {
		return fox.isInWater() || fox.isOnFire() || fox.isFreezing() || fox.getTarget() != null || FoxTargets.isFrightened(fox);
	}

	public boolean isNapTime() {
		return fox.level().isBrightOutside() && !fox.level().isThundering();
	}

	public boolean canDoze() {
		return !fox.isInPowderSnow && canDozeAt(BlockPos.containing(fox.getX(), fox.getBoundingBox().maxY, fox.getZ()));
	}

	public boolean canDozeAt(BlockPos head) {
		return isNapTime() && hasShelter(head) && !alerted();
	}

	public boolean isSpotTaken(BlockPos pos) {
		return !fox.level().getEntitiesOfClass(Fox.class, new AABB(pos).inflate(TAKEN_RADIUS), other -> other != fox && other.isSleeping()).isEmpty();
	}

	public void startSettling() {
		settleTicks = 0;
	}

	public boolean settleOn(BlockPos bed) {
		Vec3 center = Vec3.atBottomCenterOf(bed);
		double dx = center.x - fox.getX();
		double dz = center.z - fox.getZ();
		fox.getNavigation().stop();
		if (dx * dx + dz * dz <= SETTLED_DISTANCE * SETTLED_DISTANCE || ++settleTicks > SETTLE_TICKS) {
			FoxModes.stopMoving(fox);
			return true;
		}

		fox.getMoveControl().setWantedPosition(center.x, fox.getY(), center.z, SETTLE_SPEED);
		return false;
	}

	public void fallAsleep() {
		fox.setSitting(false);
		((FoxAccessor) fox).foxyfriends$setSleeping(true);
	}

	public void wakeUp() {
		((FoxAccessor) fox).foxyfriends$setSleeping(false);
	}

	private boolean hasShelter(BlockPos head) {
		return !fox.level().canSeeSky(head) && fox.getWalkTargetValue(head) >= 0.0F;
	}

	private boolean alerted() {
		return fox.level() instanceof ServerLevel level
			&& !level.getNearbyEntities(LivingEntity.class, alertTargeting, fox, fox.getBoundingBox().inflate(ALERT_RANGE, ALERT_HEIGHT, ALERT_RANGE)).isEmpty();
	}
}
