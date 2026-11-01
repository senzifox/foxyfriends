package dev.senzifox.foxyfriends.taming;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.care.FoxCare;
import dev.senzifox.foxyfriends.mixin.FoxAccessor;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public final class FoxOwnership {
	public static final AttachmentType<EntityReference<LivingEntity>> OWNER = AttachmentRegistry.create(
		FoxyFriends.id("owner"),
		builder -> builder
			.persistent(EntityReference.<LivingEntity>codec())
			.syncWith(EntityReference.<LivingEntity>streamCodec(), AttachmentSyncPredicate.all())
	);

	private FoxOwnership() {
	}

	public static void init() {
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
			if (entity instanceof Fox fox && fox.level() instanceof ServerLevel level) {
				announceDeath(fox, level);
			}
		});
	}

	private static void announceDeath(Fox fox, ServerLevel level) {
		EntityReference<LivingEntity> reference = getOwnerReference(fox);
		if (reference == null) {
			return;
		}

		ServerPlayer owner = level.getServer().getPlayerList().getPlayer(reference.getUUID());
		if (owner != null) {
			owner.sendSystemMessage(fox.getCombatTracker().getDeathMessage());
		}
	}

	public static @Nullable EntityReference<LivingEntity> getOwnerReference(Fox fox) {
		return fox.getAttached(OWNER);
	}

	public static @Nullable LivingEntity getOwner(Fox fox) {
		EntityReference<LivingEntity> reference = getOwnerReference(fox);
		if (reference == null) {
			return null;
		}

		LivingEntity owner = reference.getEntity(fox.level()::getEntity, LivingEntity.class);
		return owner != null && isNearby(fox, owner) ? owner : null;
	}

	public static boolean isNearby(Fox fox, LivingEntity owner) {
		return !owner.isRemoved() && owner.level() == fox.level();
	}

	public static boolean isTame(Fox fox) {
		return fox.hasAttached(OWNER);
	}

	public static boolean isOwnedBy(Fox fox, LivingEntity entity) {
		EntityReference<LivingEntity> owner = getOwnerReference(fox);
		return owner != null && owner.matches(entity);
	}

	public static void tame(Fox fox, Player owner) {
		fox.setAttached(OWNER, EntityReference.of(owner));
		FoxAccessor trust = (FoxAccessor) fox;
		if (!trust.foxyfriends$trusts(owner)) {
			trust.foxyfriends$addTrustedEntity(owner);
		}

		FoxCare.onTamed(fox);
	}
}
