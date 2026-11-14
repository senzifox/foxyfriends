package dev.senzifox.foxyfriends.advancement;

import dev.senzifox.foxyfriends.FoxyFriends;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public final class FoxAdvancements {
	public static final FoxPatTrigger PAT_FOX = new FoxPatTrigger();

	private FoxAdvancements() {
	}

	public static void init() {
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("pat_fox"), PAT_FOX);
	}
}
