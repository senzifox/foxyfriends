package dev.senzifox.foxyfriends.pat;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.mixin.FoxAccessor;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import java.util.UUID;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public final class FoxPatting {
	private static final int COOLDOWN_TICKS = 5;
	private static final int CALM_TICKS = 100;
	private static final String FOX_AT_HEART_NAME = "senzifox";
	private static final UUID FOX_AT_HEART_ID = UUID.fromString("31351c6b-0fa4-46ae-8ae2-912bb3dab8ac");
	private static final AttachmentType<Long> LAST_PAT = AttachmentRegistry.create(FoxyFriends.id("last_pat"));
	private static final AttachmentType<Long> CALM_UNTIL = AttachmentRegistry.create(FoxyFriends.id("calm_until"));

	private FoxPatting() {
	}

	public static void init() {
		PayloadTypeRegistry.clientboundPlay().register(FoxPatPayload.TYPE, FoxPatPayload.STREAM_CODEC);
		UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) ->
			entity instanceof Player patted && isFoxAtHeart(patted) && isPatGesture(player, hand) ? pat(player, patted) : InteractionResult.PASS);
	}

	public static @Nullable InteractionResult interact(Fox fox, Player player, InteractionHand hand) {
		if (!isPatGesture(player, hand)) {
			return null;
		}

		if (FoxOwnership.isOwnedBy(fox, player) && !fox.level().isClientSide()) {
			calm(fox);
		}

		return pat(player, fox);
	}

	public static boolean isCalm(Fox fox) {
		Long until = fox.getAttached(CALM_UNTIL);
		return until != null && fox.level().getGameTime() < until;
	}

	private static boolean isPatGesture(Player player, InteractionHand hand) {
		return hand == InteractionHand.MAIN_HAND && player.isSecondaryUseActive() && player.getMainHandItem().isEmpty();
	}

	private static boolean isFoxAtHeart(Entity entity) {
		if (entity instanceof Player player) {
			return FOX_AT_HEART_NAME.equalsIgnoreCase(player.getGameProfile().name()) || FOX_AT_HEART_ID.equals(player.getUUID());
		}

		Component name = entity.getCustomName();
		return name != null && FOX_AT_HEART_NAME.equalsIgnoreCase(name.getString());
	}

	private static InteractionResult pat(Player patter, Entity patted) {
		if (patted.level() instanceof ServerLevel level && isCooledDown(patted, level)) {
			patted.setAttached(LAST_PAT, level.getGameTime());
			if (patter instanceof ServerPlayer serverPatter) {
				FoxAdvancements.PAT_FOX.trigger(serverPatter, patted, isFoxAtHeart(patted));
			}

			level.sendParticles(ParticleTypes.HEART, patted.getX(), patted.getY(0.8), patted.getZ(), 2, 0.25, 0.15, 0.25, 0.0);
			patted.playSound(SoundEvents.FOX_AMBIENT, 0.8F, 1.2F);
			FoxPatPayload payload = new FoxPatPayload(patted.getId());
			PlayerLookup.tracking(patted).forEach(tracking -> ServerPlayNetworking.send(tracking, payload));
			if (patted instanceof ServerPlayer self) {
				ServerPlayNetworking.send(self, payload);
			}
		}

		return InteractionResult.SUCCESS;
	}

	private static void calm(Fox fox) {
		if (fox.isSleeping()) {
			return;
		}

		fox.setAttached(CALM_UNTIL, fox.level().getGameTime() + CALM_TICKS);
		fox.getNavigation().stop();
		fox.setLastHurtByMob(null);
		fox.setTarget(null);
		fox.setIsCrouching(false);
		fox.setIsInterested(false);
		fox.setIsPouncing(false);
		((FoxAccessor) fox).foxyfriends$setDefending(false);
	}

	private static boolean isCooledDown(Entity patted, ServerLevel level) {
		Long last = patted.getAttached(LAST_PAT);
		return last == null || level.getGameTime() - last >= COOLDOWN_TICKS;
	}
}
