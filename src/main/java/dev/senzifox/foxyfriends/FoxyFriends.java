package dev.senzifox.foxyfriends;

import dev.senzifox.foxyfriends.armor.FoxArmor;
import dev.senzifox.foxyfriends.care.FoxCare;
import dev.senzifox.foxyfriends.collar.FoxCollar;
import dev.senzifox.foxyfriends.mode.FoxModes;
import dev.senzifox.foxyfriends.network.FoxVersionCheck;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class FoxyFriends implements ModInitializer {
	public static final String MOD_ID = "foxyfriends";

	@Override
	public void onInitialize() {
		FoxOwnership.init();
		FoxModes.init();
		FoxCare.init();
		FoxCollar.init();
		FoxArmor.init();
		FoxVersionCheck.init();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
