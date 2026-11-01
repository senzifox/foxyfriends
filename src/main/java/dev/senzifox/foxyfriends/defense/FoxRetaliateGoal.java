package dev.senzifox.foxyfriends.defense;

import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.fox.Fox;

public class FoxRetaliateGoal extends HurtByTargetGoal {
	private final Fox fox;

	public FoxRetaliateGoal(Fox fox) {
		super(fox);
		this.fox = fox;
	}

	@Override
	public boolean canUse() {
		LivingEntity attacker = fox.getLastHurtByMob();
		return FoxOwnership.isTame(fox)
			&& !FoxTargets.isLeftBehind(fox)
			&& attacker != null
			&& !FoxOwnership.isOwnedBy(fox, attacker)
			&& !FoxTargets.isProtected(attacker)
			&& super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return FoxOwnership.isTame(fox) && !FoxTargets.isLeftBehind(fox) && fox.getTarget() != null && super.canContinueToUse();
	}

	@Override
	public void start() {
		super.start();
		FoxTargets.standGround(fox);
	}

	@Override
	protected double getFollowDistance() {
		return FoxTargets.CHASE_DISTANCE;
	}
}
