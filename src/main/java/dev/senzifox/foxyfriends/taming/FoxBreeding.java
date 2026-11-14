package dev.senzifox.foxyfriends.taming;

import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.advancement.OwnFoxesNearbyTrigger;
import dev.senzifox.foxyfriends.stats.FoxStats;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.fox.Fox;

public final class FoxBreeding {
	private FoxBreeding() {
	}

	public static void onBorn(Fox baby, Fox parent, Fox partner) {
		boolean parentTame = FoxOwnership.isTame(parent);
		boolean partnerTame = FoxOwnership.isTame(partner);
		boolean bornTame = parentTame && partnerTame || (parentTame || partnerTame) && baby.getRandom().nextBoolean();
		if (!bornTame) {
			return;
		}

		Fox firstFed = parent.getInLoveTime() <= partner.getInLoveTime() ? parent : partner;
		ServerPlayer breeder = firstFed.getLoveCause();
		if (breeder != null) {
			FoxOwnership.tame(baby, breeder);
			breeder.awardStat(FoxStats.KITS_BORN);
			FoxAdvancements.OWN_FOXES_NEARBY.trigger(breeder, parent.level() == breeder.level() && parent.distanceTo(breeder) <= OwnFoxesNearbyTrigger.RADIUS ? 1 : 0);
		}
	}
}
