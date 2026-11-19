package dev.senzifox.foxyfriends.mode;

import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.mixin.FoxAccessor;
import dev.senzifox.foxyfriends.defense.FoxTargets;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.fox.Fox;

public class FoxSitGoal extends Goal {
	private static final float DOZE_CHANCE = 0.005F;
	private static final double ALERT_RANGE = 12.0;
	private static final double ALERT_HEIGHT = 6.0;
	private static final double NAP_WITNESS_RANGE = 8.0;

	private final Fox fox;
	private final TargetingConditions alertTargeting;

	public FoxSitGoal(Fox fox) {
		this.fox = fox;
		this.alertTargeting = TargetingConditions.forCombat()
			.range(ALERT_RANGE)
			.ignoreLineOfSight()
			.selector(fox.new FoxAlertableEntitiesSelector());
		setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		return FoxModes.isInMode(fox, FoxMode.SIT) && !inDanger() && fox.onGround();
	}

	@Override
	public boolean canContinueToUse() {
		return FoxModes.isInMode(fox, FoxMode.SIT) && !inDanger();
	}

	@Override
	public void start() {
		FoxModes.stopMoving(fox);
	}

	@Override
	public void stop() {
		fox.setSitting(false);
		((FoxAccessor) fox).foxyfriends$setSleeping(false);
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	private boolean inDanger() {
		return fox.isInWater() || fox.isOnFire() || fox.isFreezing() || fox.getTarget() != null || FoxTargets.isFrightened(fox);
	}

	@Override
	public void tick() {
		if (fox.isSleeping()) {
			if (!canDoze()) {
				((FoxAccessor) fox).foxyfriends$setSleeping(false);
				fox.setSitting(true);
			}
		} else if (fox.getRandom().nextFloat() < DOZE_CHANCE && canDoze()) {
			fox.setSitting(false);
			((FoxAccessor) fox).foxyfriends$setSleeping(true);
			rewardOwnerNearby();
		} else if (!fox.isSitting()) {
			fox.setSitting(true);
		}
	}

	private void rewardOwnerNearby() {
		if (FoxOwnership.getOwner(fox) instanceof ServerPlayer owner && fox.closerThan(owner, NAP_WITNESS_RANGE)) {
			FoxAdvancements.NAPPED_BESIDE_FOX.trigger(owner);
		}
	}

	private boolean canDoze() {
		return fox.level().isBrightOutside()
			&& !fox.level().isThundering()
			&& !fox.isInPowderSnow
			&& hasShelter()
			&& !alerted();
	}

	private boolean hasShelter() {
		BlockPos head = BlockPos.containing(fox.getX(), fox.getBoundingBox().maxY, fox.getZ());
		return !fox.level().canSeeSky(head) && fox.getWalkTargetValue(head) >= 0.0F;
	}

	private boolean alerted() {
		return fox.level() instanceof ServerLevel level
			&& !level.getNearbyEntities(LivingEntity.class, alertTargeting, fox, fox.getBoundingBox().inflate(ALERT_RANGE, ALERT_HEIGHT, ALERT_RANGE)).isEmpty();
	}
}
