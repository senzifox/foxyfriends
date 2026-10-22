package dev.senzifox.foxyfriends.defense;

import dev.senzifox.foxyfriends.taming.FoxOwnership;
import java.util.EnumSet;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.fox.Fox;
import org.jspecify.annotations.Nullable;

public class FoxOwnerHurtByTargetGoal extends TargetGoal {
	private static final int DAMAGE_MEMORY_TICKS = 100;

	private final Fox fox;
	private @Nullable LivingEntity attacker;
	private int timestamp;

	public FoxOwnerHurtByTargetGoal(Fox fox) {
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

		DamageSource source = owner.getLastDamageSource(DAMAGE_MEMORY_TICKS);
		if (source == null || source.is(DamageTypeTags.NO_WOLF_RETALIATION)) {
			return false;
		}

		attacker = owner.getLastHurtByMob();
		return owner.getLastHurtByMobTimestamp() != timestamp
			&& canAttack(attacker, TargetingConditions.DEFAULT)
			&& FoxTargets.wantsToAttack(attacker, owner);
	}

	@Override
	public boolean canContinueToUse() {
		return FoxTargets.canDefend(fox) && super.canContinueToUse();
	}

	@Override
	public void start() {
		mob.setTarget(attacker);
		LivingEntity owner = FoxOwnership.getOwner(fox);
		if (owner != null) {
			timestamp = owner.getLastHurtByMobTimestamp();
		}

		super.start();
	}

	@Override
	protected double getFollowDistance() {
		return FoxTargets.CHASE_DISTANCE;
	}
}
