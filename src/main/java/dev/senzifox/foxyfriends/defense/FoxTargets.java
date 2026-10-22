package dev.senzifox.foxyfriends.defense;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.mode.FoxMode;
import dev.senzifox.foxyfriends.mode.FoxModes;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public final class FoxTargets {
	public static final TagKey<EntityType<?>> WONT_ATTACK = TagKey.create(Registries.ENTITY_TYPE, FoxyFriends.id("fox_wont_attack"));
	public static final double CHASE_DISTANCE = 16.0;

	private FoxTargets() {
	}

	public static boolean canDefend(Fox fox) {
		return FoxOwnership.isTame(fox) && FoxModes.getMode(fox) != FoxMode.SIT;
	}

	public static @Nullable LivingEntity defendedOwner(Fox fox) {
		return canDefend(fox) ? FoxOwnership.getOwner(fox) : null;
	}

	public static boolean wantsToAttack(@Nullable LivingEntity target, LivingEntity owner) {
		if (target == null || target.is(WONT_ATTACK)) {
			return false;
		}

		if (target instanceof Player player && owner instanceof Player ownerPlayer && !ownerPlayer.canHarmPlayer(player)) {
			return false;
		}

		if (target instanceof Fox fox && FoxOwnership.isTame(fox)) {
			return false;
		}

		if (target instanceof AbstractHorse horse && horse.isTamed()) {
			return false;
		}

		return !(target instanceof TamableAnimal animal && animal.isTame());
	}
}
