package dev.senzifox.foxyfriends.taming;

import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.advancement.OwnFoxesNearbyTrigger;
import dev.senzifox.foxyfriends.stats.FoxStats;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
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
			FoxAdvancements.TAMED_KIT_BORN.trigger(breeder);
			FoxAdvancements.OWN_FOXES_NEARBY.trigger(breeder, parent.level() == breeder.level() && parent.distanceTo(breeder) <= OwnFoxesNearbyTrigger.RADIUS ? 1 : 0);
		}
	}

	public static void onGrownUp(Fox fox) {
		EntityReference<LivingEntity> owner = FoxOwnership.getOwnerReference(fox);
		if (owner == null || !(fox.level() instanceof ServerLevel level)) {
			return;
		}

		ServerPlayer player = level.getServer().getPlayerList().getPlayer(owner.getUUID());
		if (player != null) {
			player.awardStat(FoxStats.KITS_RAISED);
			FoxAdvancements.KIT_RAISED.trigger(player);
		}
	}
}
