package dev.senzifox.foxyfriends.taming;

import com.mojang.serialization.Codec;
import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.advancement.FoxAdvancements;
import dev.senzifox.foxyfriends.config.FoxConfig;
import dev.senzifox.foxyfriends.mixin.FoxAccessor;
import dev.senzifox.foxyfriends.stats.FoxStats;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public final class FoxTaming {
	public static final TagKey<Item> TAMING_FOOD = TagKey.create(Registries.ITEM, FoxyFriends.id("fox_taming_food"));
	private static final AttachmentType<Map<UUID, Integer>> FAILED_FEEDINGS = AttachmentRegistry.create(
		FoxyFriends.id("failed_feedings"),
		builder -> builder.persistent(Codec.unboundedMap(UUIDUtil.STRING_CODEC, Codec.INT))
	);

	private FoxTaming() {
	}

	public static @Nullable InteractionResult interact(Fox fox, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.is(TAMING_FOOD) || FoxOwnership.isTame(fox)) {
			return null;
		}

		if (fox.level() instanceof ServerLevel level) {
			stack.consume(1, player);
			boolean trusted = ((FoxAccessor) fox).foxyfriends$trusts(player);
			boolean tamed = trusted || fox.getRandom().nextFloat() < FoxConfig.get().wildTameChance();
			int feedings = failedFeedings(fox, player) + 1;
			if (tamed) {
				fox.removeAttached(FAILED_FEEDINGS);
				FoxOwnership.tame(fox, player);
				fox.getNavigation().stop();
				fox.setTarget(null);
			} else {
				recordFailedFeeding(fox, player);
			}

			if (player instanceof ServerPlayer serverPlayer) {
				rewardFeeding(serverPlayer, fox, tamed, feedings, trusted);
			}

			level.sendParticles(tamed ? ParticleTypes.HEART : ParticleTypes.SMOKE, fox.getX(), fox.getY(0.5) + 0.5, fox.getZ(), 7, 0.4, 0.3, 0.4, 0.02);
			fox.playSound(SoundEvents.FOX_EAT);
		}

		return InteractionResult.SUCCESS;
	}

	private static void rewardFeeding(ServerPlayer player, Fox fox, boolean tamed, int feedings, boolean trusted) {
		player.awardStat(FoxStats.TAMING_ATTEMPTS);
		if (!tamed) {
			return;
		}

		player.awardStat(FoxStats.FOXES_TAMED);
		CriteriaTriggers.TAME_ANIMAL.trigger(player, fox);
		FoxAdvancements.TAME_FOX.trigger(player, fox, feedings, trusted);
		FoxAdvancements.OWN_FOXES_NEARBY.trigger(player, 0);
	}

	private static int failedFeedings(Fox fox, Player player) {
		Map<UUID, Integer> failed = fox.getAttached(FAILED_FEEDINGS);
		return failed == null ? 0 : failed.getOrDefault(player.getUUID(), 0);
	}

	private static void recordFailedFeeding(Fox fox, Player player) {
		Map<UUID, Integer> failed = new HashMap<>(fox.getAttachedOrElse(FAILED_FEEDINGS, Map.of()));
		failed.merge(player.getUUID(), 1, Integer::sum);
		fox.setAttached(FAILED_FEEDINGS, Map.copyOf(failed));
	}
}
