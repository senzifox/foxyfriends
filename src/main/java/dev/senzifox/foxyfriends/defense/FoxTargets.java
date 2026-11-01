package dev.senzifox.foxyfriends.defense;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.mixin.FoxAccessor;
import dev.senzifox.foxyfriends.mode.FoxMode;
import dev.senzifox.foxyfriends.mode.FoxModes;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
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
	public static final double OWNER_LEASH_DISTANCE = 24.0;

	private FoxTargets() {
	}

	public static boolean canDefend(Fox fox) {
		return FoxOwnership.isTame(fox) && FoxModes.getMode(fox) != FoxMode.SIT && !isLeftBehind(fox);
	}

	public static boolean isLeftBehind(Fox fox) {
		if (FoxModes.isInMode(fox, FoxMode.FOLLOW)) {
			LivingEntity owner = FoxOwnership.getOwner(fox);
			return owner != null && fox.distanceToSqr(owner) > OWNER_LEASH_DISTANCE * OWNER_LEASH_DISTANCE;
		}
		return false;
	}

	public static boolean isFrightened(Fox fox) {
		DamageSource source = fox.getLastDamageSource();
		if (source == null) {
			return false;
		}
		return source.is(DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES) || source.getEntity() instanceof LivingEntity attacker && isProtected(attacker);
	}

	public static void standGround(Fox fox) {
		((FoxAccessor) fox).foxyfriends$setDefending(true);
	}

	public static @Nullable LivingEntity defendedOwner(Fox fox) {
		return canDefend(fox) ? FoxOwnership.getOwner(fox) : null;
	}

	public static boolean wantsToAttack(@Nullable LivingEntity target, LivingEntity owner) {
		if (target == null || target == owner || isProtected(target)) {
			return false;
		}

		return !(target instanceof Player player && owner instanceof Player ownerPlayer && !ownerPlayer.canHarmPlayer(player));
	}

	public static boolean isProtected(LivingEntity target) {
		if (target.is(WONT_ATTACK)) {
			return true;
		}

		if (target instanceof Fox fox && FoxOwnership.isTame(fox)) {
			return true;
		}

		if (target instanceof AbstractHorse horse && horse.isTamed()) {
			return true;
		}

		return target instanceof TamableAnimal animal && animal.isTame();
	}
}
