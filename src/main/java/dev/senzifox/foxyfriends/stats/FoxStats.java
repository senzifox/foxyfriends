package dev.senzifox.foxyfriends.stats;

import dev.senzifox.foxyfriends.FoxyFriends;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public final class FoxStats {
	public static final Identifier FOXES_PATTED = FoxyFriends.id("foxes_patted");
	public static final Identifier FOXES_TAMED = FoxyFriends.id("foxes_tamed");
	public static final Identifier TAMING_ATTEMPTS = FoxyFriends.id("taming_attempts");
	public static final Identifier KITS_BORN = FoxyFriends.id("kits_born");

	private FoxStats() {
	}

	public static void init() {
		register(FOXES_PATTED);
		register(FOXES_TAMED);
		register(TAMING_ATTEMPTS);
		register(KITS_BORN);
	}

	private static void register(Identifier stat) {
		Registry.register(BuiltInRegistries.CUSTOM_STAT, stat, stat);
		Stats.CUSTOM.get(stat, StatFormatter.DEFAULT);
	}
}
