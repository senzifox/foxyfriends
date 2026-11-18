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
	public static final PlayerTrigger NAMED_FOX = new PlayerTrigger();
	public static final PlayerTrigger COMMANDED_FOX = new PlayerTrigger();
	public static final MouthItemTakenTrigger MOUTH_ITEM_TAKEN = new MouthItemTakenTrigger();
	public static final FoxFedTrigger FED_FOX = new FoxFedTrigger();
	public static final PlayerTrigger CALMED_FOX = new PlayerTrigger();
	public static final PlayerTrigger BOATED_WITH_FOX = new PlayerTrigger();
	public static final FoxChangedDimensionTrigger FOX_CHANGED_DIMENSION = new FoxChangedDimensionTrigger();

	private FoxAdvancements() {
	}

	public static void init() {
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("pat_fox"), PAT_FOX);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("tame_fox"), TAME_FOX);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("own_foxes_nearby"), OWN_FOXES_NEARBY);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("tamed_kit_born"), TAMED_KIT_BORN);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("kit_raised"), KIT_RAISED);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("collar_colored"), COLLAR_COLORED);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("named_fox"), NAMED_FOX);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("commanded_fox"), COMMANDED_FOX);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("mouth_item_taken"), MOUTH_ITEM_TAKEN);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("fed_fox"), FED_FOX);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("calmed_fox"), CALMED_FOX);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("boated_with_fox"), BOATED_WITH_FOX);
		Registry.register(BuiltInRegistries.TRIGGER_TYPES, FoxyFriends.id("fox_changed_dimension"), FOX_CHANGED_DIMENSION);
		ResourceLoader.registerBuiltinPack(
			FoxyFriends.id("advancement_tab"),
			FabricLoader.getInstance().getModContainer(FoxyFriends.MOD_ID).orElseThrow(),
			Component.translatableWithFallback("pack.foxyfriends.advancement_tab", "FoxyFriends: Advancement Tab"),
			PackActivationType.DEFAULT_ENABLED
		);
	}
}
