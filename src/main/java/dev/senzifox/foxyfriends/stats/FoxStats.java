package dev.senzifox.foxyfriends.stats;

import dev.senzifox.foxyfriends.FoxyFriends;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public final class FoxStats {
	public static final Identifier FOXES_PATTED = FoxyFriends.id("foxes_patted");

	private FoxStats() {
	}

	public static void init() {
		Registry.register(BuiltInRegistries.CUSTOM_STAT, FOXES_PATTED, FOXES_PATTED);
		Stats.CUSTOM.get(FOXES_PATTED, StatFormatter.DEFAULT);
	}
}
