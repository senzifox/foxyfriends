package dev.senzifox.foxyfriends.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public class FoxyFriendsModMenu implements ModMenuApi {
	private static final String YACL_ID = "yet_another_config_lib_v3";

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return FabricLoader.getInstance().isModLoaded(YACL_ID) ? FoxConfigScreen::create : parent -> null;
	}
}
