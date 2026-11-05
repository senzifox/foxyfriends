package dev.senzifox.foxyfriends.care;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.config.FoxConfig;
import dev.senzifox.foxyfriends.config.FoxyFriendsConfig;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
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
	public static final TagKey<Item> HEALING_FOOD = TagKey.create(Registries.ITEM, FoxyFriends.id("fox_healing_food"));
	private static final float FALLBACK_HEAL = 1.0F;

	private FoxCare() {
	}

	public static void init() {
		ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> refreshTamedStats(entity));
	}

	public static void refreshTamedStats(Entity entity) {
		if (entity instanceof Fox fox && FoxOwnership.isTame(fox)) {
			applyTamedStats(fox);
		}
	}

	public static void onTamed(Fox fox) {
		applyTamedStats(fox);
		fox.setHealth(fox.getMaxHealth());
	}

	public static void tick(Fox fox) {
		if (fox.level().isClientSide() || !FoxOwnership.isTame(fox)) {
			return;
		}

		FoxyFriendsConfig config = FoxConfig.get();
		if (fox.tickCount % config.regenIntervalTicks() == 0 && fox.isAlive() && isWounded(fox) && fox.getLastDamageSource(config.regenCalmTicks()) == null) {
			fox.heal(config.regenAmount());
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
		FoxyFriendsConfig config = FoxConfig.get();
		setBase(fox, Attributes.MAX_HEALTH, config.tamedMaxHealth());
		setBase(fox, Attributes.ATTACK_DAMAGE, config.tamedAttackDamage());
	}

	private static void setBase(Fox fox, Holder<Attribute> attribute, double value) {
		AttributeInstance instance = fox.getAttribute(attribute);
		if (instance != null && instance.getBaseValue() != value) {
			instance.setBaseValue(value);
		}
	}
}
