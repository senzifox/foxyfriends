package dev.senzifox.foxyfriends.advancement;

import dev.senzifox.foxyfriends.FoxyFriends;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;

public final class FoxAdvancements {
	public static final FoxPatTrigger PAT_FOX = new FoxPatTrigger();
	public static final FoxTameTrigger TAME_FOX = new FoxTameTrigger();
	public static final OwnFoxesNearbyTrigger OWN_FOXES_NEARBY = new OwnFoxesNearbyTrigger();
	public static final PlayerTrigger TAMED_KIT_BORN = new PlayerTrigger();
	public static final PlayerTrigger KIT_RAISED = new PlayerTrigger();
	public static final CollarColoredTrigger COLLAR_COLORED = new CollarColoredTrigger();

	private FoxAdvancements() {
	}

	public static void init() {
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("pat_fox"), PAT_FOX);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("tame_fox"), TAME_FOX);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("own_foxes_nearby"), OWN_FOXES_NEARBY);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("tamed_kit_born"), TAMED_KIT_BORN);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("kit_raised"), KIT_RAISED);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("collar_colored"), COLLAR_COLORED);
		ResourceLoader.registerBuiltinPack(
			FoxyFriends.id("advancement_tab"),
			FabricLoader.getInstance().getModContainer(FoxyFriends.MOD_ID).orElseThrow(),
			Component.translatableWithFallback("pack.foxyfriends.advancement_tab", "FoxyFriends: Advancement Tab"),
			PackActivationType.DEFAULT_ENABLED
		);
	}
}
