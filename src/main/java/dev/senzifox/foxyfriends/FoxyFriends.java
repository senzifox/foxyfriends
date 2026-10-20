package dev.senzifox.foxyfriends;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class FoxyFriends implements ModInitializer {
	public static final String MOD_ID = "foxyfriends";

	@Override
	public void onInitialize() {
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
