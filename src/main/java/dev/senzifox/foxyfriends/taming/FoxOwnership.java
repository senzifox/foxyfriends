package dev.senzifox.foxyfriends.taming;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.mixin.FoxAccessor;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
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
	}

	public static @Nullable EntityReference<LivingEntity> getOwnerReference(Fox fox) {
		return fox.getAttached(OWNER);
	}

	public static @Nullable LivingEntity getOwner(Fox fox) {
		return EntityReference.getLivingEntity(getOwnerReference(fox), fox.level());
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
	}
}
