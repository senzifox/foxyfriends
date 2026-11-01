package dev.senzifox.foxyfriends.defense;

import dev.senzifox.foxyfriends.taming.FoxOwnership;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.fox.Fox;
import org.jspecify.annotations.Nullable;

public class FoxOwnerHurtTargetGoal extends TargetGoal {
	private final Fox fox;
	private @Nullable LivingEntity victim;
	private int timestamp;

	public FoxOwnerHurtTargetGoal(Fox fox) {
		super(fox, false);
		this.fox = fox;
		setFlags(EnumSet.of(Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		LivingEntity owner = FoxTargets.defendedOwner(fox);
		if (owner == null) {
			return false;
		}

		victim = owner.getLastHurtMob();
		return owner.getLastHurtMobTimestamp() != timestamp
			&& canAttack(victim, TargetingConditions.DEFAULT)
			&& FoxTargets.wantsToAttack(victim, owner);
	}

	@Override
	public boolean canContinueToUse() {
		return FoxTargets.canDefend(fox) && super.canContinueToUse();
	}

	@Override
	public void start() {
		mob.setTarget(victim);
		LivingEntity owner = FoxOwnership.getOwner(fox);
		if (owner != null) {
			timestamp = owner.getLastHurtMobTimestamp();
		}

		super.start();
		FoxTargets.standGround(fox);
	}

	@Override
	protected double getFollowDistance() {
		return FoxTargets.CHASE_DISTANCE;
	}
}
