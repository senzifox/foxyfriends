package dev.senzifox.foxyfriends.pat;

import dev.senzifox.foxyfriends.FoxyFriends;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.Nullable;

public final class FoxPatting {
	private static final int COOLDOWN_TICKS = 10;
	private static final AttachmentType<Long> LAST_PAT = AttachmentRegistry.create(FoxyFriends.id("last_pat"));

	private FoxPatting() {
	}

	public static @Nullable InteractionResult interact(Fox fox, Player player, InteractionHand hand) {
		if (hand != InteractionHand.MAIN_HAND || !player.isSecondaryUseActive() || !player.getMainHandItem().isEmpty()) {
			return null;
		}

		if (fox.level() instanceof ServerLevel level && isCooledDown(fox, level)) {
			fox.setAttached(LAST_PAT, level.getGameTime());
			level.sendParticles(ParticleTypes.HEART, fox.getX(), fox.getY(0.8), fox.getZ(), 2, 0.25, 0.15, 0.25, 0.0);
			fox.playSound(SoundEvents.FOX_AMBIENT, 0.8F, 1.2F);
		}

		return InteractionResult.SUCCESS;
	}

	private static boolean isCooledDown(Fox fox, ServerLevel level) {
		Long last = fox.getAttached(LAST_PAT);
		return last == null || level.getGameTime() - last >= COOLDOWN_TICKS;
	}
}
