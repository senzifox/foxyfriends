package dev.senzifox.foxyfriends.taming;

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
		}
	}
}
