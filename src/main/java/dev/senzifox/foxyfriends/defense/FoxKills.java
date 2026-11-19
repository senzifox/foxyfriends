package dev.senzifox.foxyfriends.defense;

import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.stats.FoxStats;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.monster.Enemy;

public final class FoxKills {
	private FoxKills() {
	}

	public static void init() {
		ServerLivingEntityEvents.AFTER_DEATH.register((victim, source) -> {
			if (source.getEntity() instanceof Fox fox && fox.level() instanceof ServerLevel level) {
				onFoxKilled(fox, victim, level);
			}
		});
	}

	private static void onFoxKilled(Fox fox, LivingEntity victim, ServerLevel level) {
		EntityReference<LivingEntity> reference = FoxOwnership.getOwnerReference(fox);
		ServerPlayer owner = reference == null ? null : level.getServer().getPlayerList().getPlayer(reference.getUUID());
		if (owner == null) {
			return;
		}

		if (victim instanceof Enemy) {
			owner.awardStat(FoxStats.FOX_KILLS);
		}

		boolean attackedOwner = victim == owner.getLastHurtByMob();
		boolean ownersFight = attackedOwner || victim == owner.getLastHurtMob();
		FoxAdvancements.FOX_KILLED_MOB.trigger(owner, fox, ownersFight, attackedOwner);
	}
}
