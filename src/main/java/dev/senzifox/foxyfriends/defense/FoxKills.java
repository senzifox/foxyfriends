package dev.senzifox.foxyfriends.defense;

import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;

public final class FoxKills {
	private FoxKills() {
	}

	public static void init() {
		ServerLivingEntityEvents.AFTER_DEATH.register((victim, source) -> {
			if (source.getEntity() instanceof Fox fox && FoxOwnership.getOwner(fox) instanceof ServerPlayer owner) {
				FoxAdvancements.FOX_KILLED_MOB.trigger(owner, isOwnersFight(owner, victim));
			}
		});
	}

	private static boolean isOwnersFight(ServerPlayer owner, LivingEntity victim) {
		return victim == owner.getLastHurtMob() || victim == owner.getLastHurtByMob();
	}
}
