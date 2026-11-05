package dev.senzifox.foxyfriends.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;

public record FoxyFriendsConfig(
	boolean wanderHunting,
	float wildTameChance,
	double tamedMaxHealth,
	double tamedAttackDamage,
	float regenAmount,
	int regenIntervalTicks,
	int regenCalmTicks
) {
	public static final FoxyFriendsConfig DEFAULT = new FoxyFriendsConfig(true, 0.1F, 25.0, 3.0, 1.0F, 600, 200);

	public static final Codec<FoxyFriendsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		field(Codec.BOOL, "wander_hunting", DEFAULT.wanderHunting).forGetter(FoxyFriendsConfig::wanderHunting),
		field(Codec.floatRange(0.0F, 1.0F), "wild_tame_chance", DEFAULT.wildTameChance).forGetter(FoxyFriendsConfig::wildTameChance),
		field(Codec.doubleRange(1.0, 1024.0), "tamed_max_health", DEFAULT.tamedMaxHealth).forGetter(FoxyFriendsConfig::tamedMaxHealth),
		field(Codec.doubleRange(0.0, 2048.0), "tamed_attack_damage", DEFAULT.tamedAttackDamage).forGetter(FoxyFriendsConfig::tamedAttackDamage),
		field(Codec.floatRange(0.0F, 1024.0F), "regen_amount", DEFAULT.regenAmount).forGetter(FoxyFriendsConfig::regenAmount),
		field(Codec.intRange(1, 72000), "regen_interval_ticks", DEFAULT.regenIntervalTicks).forGetter(FoxyFriendsConfig::regenIntervalTicks),
		field(Codec.intRange(0, 72000), "regen_calm_ticks", DEFAULT.regenCalmTicks).forGetter(FoxyFriendsConfig::regenCalmTicks)
	).apply(instance, FoxyFriendsConfig::new));

	private static <T> MapCodec<T> field(Codec<T> codec, String name, T fallback) {
		return codec.optionalFieldOf(name).xmap(value -> value.orElse(fallback), Optional::of);
	}
}
