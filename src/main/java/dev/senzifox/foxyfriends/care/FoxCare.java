package dev.senzifox.foxyfriends.care;

import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.fox.Fox;

public final class FoxCare {
	public static final double TAMED_MAX_HEALTH = 25.0;
	public static final double TAMED_ATTACK_DAMAGE = 3.0;

	private FoxCare() {
	}

	public static void onTamed(Fox fox) {
		applyTamedStats(fox);
		fox.setHealth(fox.getMaxHealth());
	}

	public static void tick(Fox fox) {
		if (!fox.level().isClientSide() && FoxOwnership.isTame(fox)) {
			applyTamedStats(fox);
		}
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
