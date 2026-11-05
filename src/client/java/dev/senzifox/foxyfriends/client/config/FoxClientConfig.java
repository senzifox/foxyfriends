package dev.senzifox.foxyfriends.client.config;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.config.ConfigFile;

public final class FoxClientConfig {
	private static final ConfigFile<FoxyFriendsClientConfig> FILE = new ConfigFile<>(FoxyFriends.MOD_ID + "-client.json", FoxyFriendsClientConfig.CODEC, FoxyFriendsClientConfig.DEFAULT);

	private FoxClientConfig() {
	}

	public static void init() {
		FILE.loadOrLog();
	}

	public static FoxyFriendsClientConfig get() {
		return FILE.get();
	}

	public static void update(FoxyFriendsClientConfig config) {
		FILE.update(config);
	}
}
