package dev.senzifox.foxyfriends.client.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.controller.DoubleFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.senzifox.foxyfriends.config.FoxConfig;
import dev.senzifox.foxyfriends.config.FoxyFriendsConfig;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class FoxConfigScreen {
	private static final FoxyFriendsConfig DEFAULT = FoxyFriendsConfig.DEFAULT;

	private boolean wanderHunting;
	private float wildTameChance;
	private double tamedMaxHealth;
	private double tamedAttackDamage;
	private float regenAmount;
	private int regenIntervalTicks;
	private int regenCalmTicks;

	private FoxConfigScreen(FoxyFriendsConfig config) {
		wanderHunting = config.wanderHunting();
		wildTameChance = config.wildTameChance();
		tamedMaxHealth = config.tamedMaxHealth();
		tamedAttackDamage = config.tamedAttackDamage();
		regenAmount = config.regenAmount();
		regenIntervalTicks = config.regenIntervalTicks();
		regenCalmTicks = config.regenCalmTicks();
	}

	public static Screen create(Screen parent) {
		return new FoxConfigScreen(FoxConfig.get()).build().generateScreen(parent);
	}

	private YetAnotherConfigLib build() {
		return YetAnotherConfigLib.createBuilder()
			.title(Component.translatable("config.foxyfriends.title"))
			.category(ConfigCategory.createBuilder()
				.name(Component.translatable("config.foxyfriends.category.foxes"))
				.group(OptionGroup.createBuilder()
					.name(Component.translatable("config.foxyfriends.group.behaviour"))
					.option(option("wander_hunting", DEFAULT.wanderHunting(), () -> wanderHunting, value -> wanderHunting = value, TickBoxControllerBuilder::create))
					.option(option("wild_tame_chance", DEFAULT.wildTameChance(), () -> wildTameChance, value -> wildTameChance = value,
						opt -> FloatSliderControllerBuilder.create(opt).range(0.0F, 1.0F).step(0.01F)))
					.build())
				.group(OptionGroup.createBuilder()
					.name(Component.translatable("config.foxyfriends.group.tamed"))
					.option(option("tamed_max_health", DEFAULT.tamedMaxHealth(), () -> tamedMaxHealth, value -> tamedMaxHealth = value,
						opt -> DoubleFieldControllerBuilder.create(opt).range(1.0, 1024.0)))
					.option(option("tamed_attack_damage", DEFAULT.tamedAttackDamage(), () -> tamedAttackDamage, value -> tamedAttackDamage = value,
						opt -> DoubleFieldControllerBuilder.create(opt).range(0.0, 2048.0)))
					.build())
				.group(OptionGroup.createBuilder()
					.name(Component.translatable("config.foxyfriends.group.regen"))
					.option(option("regen_amount", DEFAULT.regenAmount(), () -> regenAmount, value -> regenAmount = value,
						opt -> FloatFieldControllerBuilder.create(opt).range(0.0F, 1024.0F)))
					.option(option("regen_interval_ticks", DEFAULT.regenIntervalTicks(), () -> regenIntervalTicks, value -> regenIntervalTicks = value,
						opt -> IntegerFieldControllerBuilder.create(opt).range(1, 72000)))
					.option(option("regen_calm_ticks", DEFAULT.regenCalmTicks(), () -> regenCalmTicks, value -> regenCalmTicks = value,
						opt -> IntegerFieldControllerBuilder.create(opt).range(0, 72000)))
					.build())
				.build())
			.save(this::save)
			.build();
	}

	private void save() {
		FoxyFriendsConfig config = new FoxyFriendsConfig(wanderHunting, wildTameChance, tamedMaxHealth, tamedAttackDamage, regenAmount, regenIntervalTicks, regenCalmTicks);
		FoxConfig.update(config, Minecraft.getInstance().getSingleplayerServer());
	}

	private static <T> Option<T> option(
		String key,
		T fallback,
		Supplier<T> getter,
		Consumer<T> setter,
		Function<Option<T>, ? extends ControllerBuilder<T>> controller
	) {
		return Option.<T>createBuilder()
			.name(Component.translatable("config.foxyfriends." + key))
			.description(OptionDescription.of(Component.translatable("config.foxyfriends." + key + ".description")))
			.binding(fallback, getter, setter)
			.controller(controller::apply)
			.build();
	}
}
