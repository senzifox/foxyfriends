package dev.senzifox.foxyfriends.mode;

import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.animal.fox.Fox;

public class FoxReturnHomeGoal extends MoveTowardsRestrictionGoal {
	private static final double SPEED = 1.0;

	private final Fox fox;

	public FoxReturnHomeGoal(Fox fox) {
		super(fox, SPEED);
		this.fox = fox;
	}

	@Override
	public boolean canUse() {
		return FoxModes.isInMode(fox, FoxMode.WANDER) && super.canUse();
	}

	@Override
	public boolean canContinueToUse() {
		return FoxModes.isInMode(fox, FoxMode.WANDER) && super.canContinueToUse();
	}
}
