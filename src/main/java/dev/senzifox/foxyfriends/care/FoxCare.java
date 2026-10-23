package dev.senzifox.foxyfriends.care;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public final class FoxCare {
	public static final double TAMED_MAX_HEALTH = 25.0;
	public static final double TAMED_ATTACK_DAMAGE = 3.0;
	public static final TagKey<Item> HEALING_FOOD = TagKey.create(Registries.ITEM, FoxyFriends.id("fox_healing_food"));
	public static final int REGEN_INTERVAL_TICKS = 600;
	public static final int CALM_TICKS = 200;
	private static final float REGEN_AMOUNT = 1.0F;
	private static final float FALLBACK_HEAL = 1.0F;

	private FoxCare() {
	}

	public static void onTamed(Fox fox) {
		applyTamedStats(fox);
		fox.setHealth(fox.getMaxHealth());
	}

	public static void tick(Fox fox) {
		if (fox.level().isClientSide() || !FoxOwnership.isTame(fox)) {
			return;
		}

		applyTamedStats(fox);
		if (fox.tickCount % REGEN_INTERVAL_TICKS == 0 && fox.isAlive() && isWounded(fox) && fox.getLastDamageSource(CALM_TICKS) == null) {
			fox.heal(REGEN_AMOUNT);
		}
	}

	public static @Nullable InteractionResult interact(Fox fox, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!stack.is(HEALING_FOOD) || !FoxOwnership.isTame(fox) || !isWounded(fox)) {
			return null;
		}

		if (!fox.level().isClientSide()) {
			FoodProperties food = stack.get(DataComponents.FOOD);
			fox.heal(food != null ? food.nutrition() : FALLBACK_HEAL);
			stack.consume(1, player);
			fox.playSound(SoundEvents.FOX_EAT);
		}

		return InteractionResult.SUCCESS;
	}

	private static boolean isWounded(Fox fox) {
		return fox.getHealth() < fox.getMaxHealth();
	}

	private static void applyTamedStats(Fox fox) {
		raiseBase(fox, Attributes.MAX_HEALTH, TAMED_MAX_HEALTH);
		raiseBase(fox, Attributes.ATTACK_DAMAGE, TAMED_ATTACK_DAMAGE);
	}

	private static void raiseBase(Fox fox, Holder<Attribute> attribute, double value) {
		AttributeInstance instance = fox.getAttribute(attribute);
		if (instance != null && instance.getBaseValue() < value) {
			instance.setBaseValue(value);
		}
	}
}
