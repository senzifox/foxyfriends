package dev.senzifox.foxyfriends.client.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.senzifox.foxyfriends.config.ConfigFile;

public record FoxyFriendsClientConfig(boolean healthActionbar) {
	public static final FoxyFriendsClientConfig DEFAULT = new FoxyFriendsClientConfig(true);

	public static final Codec<FoxyFriendsClientConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ConfigFile.field(Codec.BOOL, "health_actionbar", DEFAULT.healthActionbar).forGetter(FoxyFriendsClientConfig::healthActionbar)
	).apply(instance, FoxyFriendsClientConfig::new));
}
