package dev.senzifox.foxyfriends.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

public final class ConfigFile<T> {
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final Path path;
	private final Codec<T> codec;
	private volatile T current;

	public ConfigFile(String fileName, Codec<T> codec, T fallback) {
		this.path = FabricLoader.getInstance().getConfigDir().resolve(fileName);
		this.codec = codec;
		this.current = fallback;
	}

	public static <T> MapCodec<T> field(Codec<T> codec, String name, T fallback) {
		return codec.optionalFieldOf(name).xmap(value -> value.orElse(fallback), Optional::of);
	}

	public T get() {
		return current;
	}

	public void update(T config) {
		current = config;
		write(config);
	}

	public DataResult<T> load() {
		DataResult<T> result = Files.exists(path) ? read() : DataResult.success(current);
		result.ifSuccess(this::update);
		return result;
	}

	public void loadOrLog() {
		load().ifError(error -> LOGGER.error("{} not loaded, keeping previous values: {}", path.getFileName(), error.message()));
	}

	public DataResult<T> parse(String json) {
		try {
			return codec.parse(JsonOps.INSTANCE, JsonParser.parseString(json));
		} catch (RuntimeException e) {
			return DataResult.error(e::getMessage);
		}
	}

	private DataResult<T> read() {
		try {
			return parse(Files.readString(path));
		} catch (IOException e) {
			return DataResult.error(e::getMessage);
		}
	}

	private void write(T config) {
		codec.encodeStart(JsonOps.INSTANCE, config).ifSuccess(json -> {
			try {
				Files.createDirectories(path.getParent());
				Files.writeString(path, GSON.toJson(json) + System.lineSeparator());
			} catch (IOException e) {
				LOGGER.error("{} not saved", path.getFileName(), e);
			}
		});
	}
}
