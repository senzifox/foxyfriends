package dev.senzifox.foxyfriends.taming;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.mixin.FoxAccessor;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
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
	public static final float WILD_TAME_CHANCE = 0.1F;

	private FoxTaming() {
	}

	public static @Nullable InteractionResult interact(Fox fox, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.is(TAMING_FOOD) || FoxOwnership.isTame(fox)) {
			return null;
		}

		if (fox.level() instanceof ServerLevel level) {
			stack.consume(1, player);
			boolean tamed = ((FoxAccessor) fox).foxyfriends$trusts(player) || fox.getRandom().nextFloat() < WILD_TAME_CHANCE;
			if (tamed) {
				FoxOwnership.tame(fox, player);
				fox.getNavigation().stop();
				fox.setTarget(null);
			}

			level.sendParticles(tamed ? ParticleTypes.HEART : ParticleTypes.SMOKE, fox.getX(), fox.getY(0.5) + 0.5, fox.getZ(), 7, 0.4, 0.3, 0.4, 0.02);
			fox.playSound(SoundEvents.FOX_EAT);
		}

		return InteractionResult.SUCCESS;
	}
}
