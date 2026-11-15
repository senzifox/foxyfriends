package dev.senzifox.foxyfriends.collar;

import com.mojang.serialization.Codec;
import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public final class FoxCollar {
	public static final DyeColor DEFAULT_COLOR = DyeColor.RED;
	private static final int GLOW_PARTICLE_CHANCE = 10;

	public static final AttachmentType<DyeColor> COLOR = AttachmentRegistry.create(
		FoxyFriends.id("collar"),
		builder -> builder
			.persistent(DyeColor.CODEC)
			.syncWith(DyeColor.STREAM_CODEC, AttachmentSyncPredicate.all())
	);

	public static final AttachmentType<Boolean> GLOW = AttachmentRegistry.create(
		FoxyFriends.id("collar_glow"),
		builder -> builder
			.persistent(Codec.BOOL)
			.syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all())
	);

	private FoxCollar() {
	}

	public static void init() {
	}

	public static void onTamed(Fox fox, Player owner) {
		if (owner instanceof ServerPlayer serverOwner) {
			FoxAdvancements.COLLAR_COLORED.trigger(serverOwner, getColor(fox), false);
		}
	}

	public static DyeColor getColor(Fox fox) {
		return fox.getAttachedOrElse(COLOR, DEFAULT_COLOR);
	}

	public static boolean isGlowing(Fox fox) {
		return fox.getAttachedOrElse(GLOW, false);
	}

	public static @Nullable InteractionResult interact(Fox fox, Player player, InteractionHand hand) {
		if (!FoxOwnership.isOwnedBy(fox, player)) {
			return null;
		}

		ItemStack stack = player.getItemInHand(hand);
		if (stack.is(Items.GLOW_INK_SAC)) {
			return setGlowing(fox, player, stack, true, SoundEvents.GLOW_INK_SAC_USE);
		}

		if (stack.is(Items.INK_SAC)) {
			return setGlowing(fox, player, stack, false, SoundEvents.INK_SAC_USE);
		}

		DyeColor color = stack.get(DataComponents.DYE);
		if (color == null || color == getColor(fox)) {
			return null;
		}

		if (!fox.level().isClientSide()) {
			fox.setAttached(COLOR, color);
			stack.consume(1, player);
			fox.level().playSound(null, fox, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
			if (player instanceof ServerPlayer serverPlayer) {
				FoxAdvancements.COLLAR_COLORED.trigger(serverPlayer, color, true);
			}
		}

		return InteractionResult.SUCCESS;
	}

	public static void tickGlow(Fox fox) {
		if (!fox.level().isClientSide() || fox.isInvisible() || fox.getRandom().nextInt(GLOW_PARTICLE_CHANCE) != 0 || !isGlowing(fox) || !FoxOwnership.isTame(fox)) {
			return;
		}

		Vec3 neck = fox.position()
			.add(Vec3.directionFromRotation(0.0F, fox.yBodyRot).scale(fox.getBbWidth() * 0.5))
			.add(0.0, fox.getBbHeight() * 0.6, 0.0);
		double spread = fox.getBbWidth() * 0.3;
		fox.level().addParticle(
			ParticleTypes.GLOW,
			neck.x + (fox.getRandom().nextDouble() - 0.5) * spread,
			neck.y + (fox.getRandom().nextDouble() - 0.5) * spread,
			neck.z + (fox.getRandom().nextDouble() - 0.5) * spread,
			0.0,
			0.0,
			0.0
		);
	}

	private static @Nullable InteractionResult setGlowing(Fox fox, Player player, ItemStack stack, boolean glowing, SoundEvent sound) {
		if (glowing == isGlowing(fox)) {
			return null;
		}

		if (!fox.level().isClientSide()) {
			if (glowing) {
				fox.setAttached(GLOW, true);
			} else {
				fox.removeAttached(GLOW);
			}
			stack.consume(1, player);
			fox.level().playSound(null, fox, sound, SoundSource.PLAYERS, 1.0F, 1.0F);
		}

		return InteractionResult.SUCCESS;
	}
}
