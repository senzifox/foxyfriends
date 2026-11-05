package dev.senzifox.foxyfriends.config;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.DataResult;
import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.care.FoxCare;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.jspecify.annotations.Nullable;

public final class FoxConfig {
	private static final ConfigFile<FoxyFriendsConfig> FILE = new ConfigFile<>(FoxyFriends.MOD_ID + ".json", FoxyFriendsConfig.CODEC, FoxyFriendsConfig.DEFAULT);

	private FoxConfig() {
	}

	public static void init() {
		FILE.loadOrLog();
		ServerLifecycleEvents.SERVER_STARTING.register(server -> FILE.loadOrLog());
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
			Commands.literal(FoxyFriends.MOD_ID)
				.requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
				.then(Commands.literal("reload").executes(FoxConfig::reload))
		));
	}

	public static FoxyFriendsConfig get() {
		return FILE.get();
	}

	public static void update(FoxyFriendsConfig config, @Nullable MinecraftServer server) {
		FILE.update(config);
		if (server != null) {
			server.execute(() -> applyToLoadedFoxes(server));
		}
	}

	public static DataResult<FoxyFriendsConfig> parse(String json) {
		return FILE.parse(json);
	}

	private static int reload(CommandContext<CommandSourceStack> context) {
		CommandSourceStack source = context.getSource();
		DataResult<FoxyFriendsConfig> result = FILE.load();
		if (result.isError()) {
			String message = result.error().map(DataResult.Error::message).orElse("");
			source.sendFailure(Component.translatable("commands.foxyfriends.reload.failure", message));
			return 0;
		}

		applyToLoadedFoxes(source.getServer());
		source.sendSuccess(() -> Component.translatable("commands.foxyfriends.reload.success"), true);
		return 1;
	}

	private static void applyToLoadedFoxes(MinecraftServer server) {
		server.getAllLevels().forEach(level -> level.getAllEntities().forEach(FoxCare::refreshTamedStats));
	}
}
