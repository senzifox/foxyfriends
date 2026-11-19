package dev.senzifox.foxyfriends.mode;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;

public final class FoxHunting {
	public static final TagKey<EntityType<?>> PREY = TagKey.create(Registries.ENTITY_TYPE, FoxyFriends.id("fox_prey"));

	private FoxHunting() {
	}

	public static void init() {
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
			if (source.getEntity() instanceof Fox fox && entity.is(PREY) && entity.level() instanceof ServerLevel level) {
				rewardOwner(fox, level);
			}
		});
	}

	private static void rewardOwner(Fox fox, ServerLevel level) {
		EntityReference<LivingEntity> reference = FoxOwnership.getOwnerReference(fox);
		if (reference == null || !FoxModes.isInMode(fox, FoxMode.WANDER)) {
			return;
		}

		ServerPlayer owner = level.getServer().getPlayerList().getPlayer(reference.getUUID());
		if (owner != null) {
			FoxAdvancements.WANDERING_FOX_HUNTED.trigger(owner);
		}
	}
}
