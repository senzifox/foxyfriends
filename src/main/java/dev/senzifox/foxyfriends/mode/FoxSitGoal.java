package dev.senzifox.foxyfriends.mode;

import dev.senzifox.foxyfriends.mixin.FoxAccessor;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.fox.Fox;

public class FoxSitGoal extends Goal {
	private static final float DOZE_CHANCE = 0.005F;

	private final Fox fox;

	public FoxSitGoal(Fox fox) {
		this.fox = fox;
		setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		return FoxModes.isInMode(fox, FoxMode.SIT) && !fox.isInWater() && fox.onGround();
	}

	@Override
	public boolean canContinueToUse() {
		return FoxModes.isInMode(fox, FoxMode.SIT) && !fox.isInWater();
	}

	@Override
	public void start() {
		fox.getNavigation().stop();
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

	@Override
	public void tick() {
		boolean sleepTime = fox.level().isBrightOutside() && !fox.level().isThundering();
		if (fox.isSleeping()) {
			if (!sleepTime) {
				((FoxAccessor) fox).foxyfriends$setSleeping(false);
				fox.setSitting(true);
			}
		} else if (sleepTime && fox.getRandom().nextFloat() < DOZE_CHANCE) {
			fox.setSitting(false);
			((FoxAccessor) fox).foxyfriends$setSleeping(true);
		} else if (!fox.isSitting()) {
			fox.setSitting(true);
		}
	}
}
