package dev.senzifox.foxyfriends.travel;

import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.fox.Fox;

public final class FoxTravel {
	private FoxTravel() {
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
