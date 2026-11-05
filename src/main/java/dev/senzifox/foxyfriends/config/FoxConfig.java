package dev.senzifox.foxyfriends.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.care.FoxCare;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public final class FoxConfig {
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(FoxyFriends.MOD_ID + ".json");

	private static volatile FoxyFriendsConfig current = FoxyFriendsConfig.DEFAULT;

	private FoxConfig() {
	}

	public static void init() {
		loadOrLog();
		ServerLifecycleEvents.SERVER_STARTING.register(server -> loadOrLog());
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
			Commands.literal(FoxyFriends.MOD_ID)
				.requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
				.then(Commands.literal("reload").executes(FoxConfig::reload))
		));
	}

	public static FoxyFriendsConfig get() {
		return current;
	}

	public static void update(FoxyFriendsConfig config, @Nullable MinecraftServer server) {
		current = config;
		write(config);
		if (server != null) {
			server.execute(() -> applyToLoadedFoxes(server));
		}
	}

	public static DataResult<FoxyFriendsConfig> parse(String json) {
		try {
			return FoxyFriendsConfig.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(json));
		} catch (RuntimeException e) {
			return DataResult.error(e::getMessage);
		}
	}

	private static int reload(CommandContext<CommandSourceStack> context) {
		CommandSourceStack source = context.getSource();
		DataResult<FoxyFriendsConfig> result = load();
		if (result.isError()) {
			String message = result.error().map(DataResult.Error::message).orElse("");
			source.sendFailure(Component.translatable("commands.foxyfriends.reload.failure", message));
			return 0;
		}

		applyToLoadedFoxes(source.getServer());
		source.sendSuccess(() -> Component.translatable("commands.foxyfriends.reload.success"), true);
		return 1;
	}

	private static void loadOrLog() {
		load().ifError(error -> LOGGER.error("FoxyFriends config not loaded, keeping previous values: {}", error.message()));
	}

	private static DataResult<FoxyFriendsConfig> load() {
		DataResult<FoxyFriendsConfig> result = Files.exists(PATH) ? read() : DataResult.success(FoxyFriendsConfig.DEFAULT);
		result.ifSuccess(config -> {
			current = config;
			write(config);
		});
		return result;
	}

	private static DataResult<FoxyFriendsConfig> read() {
		try {
			return parse(Files.readString(PATH));
		} catch (IOException e) {
			return DataResult.error(e::getMessage);
		}
	}

	private static void write(FoxyFriendsConfig config) {
		FoxyFriendsConfig.CODEC.encodeStart(JsonOps.INSTANCE, config).ifSuccess(json -> {
			try {
				Files.createDirectories(PATH.getParent());
				Files.writeString(PATH, GSON.toJson(json) + System.lineSeparator());
			} catch (IOException e) {
				LOGGER.error("FoxyFriends config not saved", e);
			}
		});
	}

	private static void applyToLoadedFoxes(MinecraftServer server) {
		server.getAllLevels().forEach(level -> level.getAllEntities().forEach(FoxCare::refreshTamedStats));
	}
}
