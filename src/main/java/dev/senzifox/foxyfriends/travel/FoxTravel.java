package dev.senzifox.foxyfriends.travel;

import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import java.util.List;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;

public final class FoxTravel {
	private FoxTravel() {
	}

	public static void init() {
		ServerEntityLevelChangeEvents.AFTER_ENTITY_CHANGE_LEVEL.register((originalEntity, newEntity, origin, destination) -> {
			if (newEntity instanceof Fox fox) {
				onFoxChangedLevel(fox, destination);
			}
		});
	}

	private static void onFoxChangedLevel(Fox fox, ServerLevel destination) {
		EntityReference<LivingEntity> reference = FoxOwnership.getOwnerReference(fox);
		if (reference == null) {
			return;
		}

		ServerPlayer owner = destination.getServer().getPlayerList().getPlayer(reference.getUUID());
		if (owner != null) {
			FoxAdvancements.FOX_CHANGED_DIMENSION.trigger(owner, destination.dimension());
		}
	}

	public static void onBoatPassengerAdded(Entity boat) {
		if (boat.level().isClientSide()) {
			return;
		}

		List<Entity> passengers = boat.getPassengers();
		for (Entity rider : passengers) {
			if (rider instanceof ServerPlayer player && passengers.stream().anyMatch(other -> other instanceof Fox fox && FoxOwnership.isOwnedBy(fox, player))) {
				FoxAdvancements.BOATED_WITH_FOX.trigger(player);
			}
		}
	}
}
