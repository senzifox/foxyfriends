package dev.senzifox.foxyfriends.mode;

import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.sleep.FoxNaps;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import java.util.EnumSet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.fox.Fox;

public class FoxSitGoal extends Goal {
	private static final float DOZE_CHANCE = 0.005F;
	private static final double NAP_WITNESS_RANGE = 8.0;

	private final Fox fox;
	private final FoxNaps naps;

	public FoxSitGoal(Fox fox) {
		this.fox = fox;
		this.naps = new FoxNaps(fox);
		setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		return FoxModes.isInMode(fox, FoxMode.SIT) && !naps.inDanger() && fox.onGround();
	}

	@Override
	public boolean canContinueToUse() {
		return FoxModes.isInMode(fox, FoxMode.SIT) && !naps.inDanger();
	}

	@Override
	public void start() {
		FoxModes.stopMoving(fox);
	}

	@Override
	public void stop() {
		fox.setSitting(false);
		naps.wakeUp();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		if (fox.isSleeping()) {
			if (!naps.canDoze()) {
				naps.wakeUp();
				fox.setSitting(true);
			}
		} else if (fox.getRandom().nextFloat() < DOZE_CHANCE && naps.canDoze()) {
			naps.fallAsleep();
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
}
