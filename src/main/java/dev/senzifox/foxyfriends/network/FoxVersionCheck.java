package dev.senzifox.foxyfriends.network;

import dev.senzifox.foxyfriends.FoxyFriends;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public final class FoxVersionCheck {
	public static final Identifier CHANNEL = FoxyFriends.id("version");
	public static final String VERSION = FabricLoader.getInstance()
		.getModContainer(FoxyFriends.MOD_ID)
		.orElseThrow()
		.getMetadata()
		.getVersion()
		.getFriendlyString();

	private FoxVersionCheck() {
	}

	public static void init() {
		ServerLoginConnectionEvents.QUERY_START.register((listener, server, sender, synchronizer) -> sender.sendPacket(CHANNEL, writeVersion()));
		ServerLoginNetworking.registerGlobalReceiver(CHANNEL, (server, listener, understood, buf, synchronizer, responseSender) -> {
			String clientVersion = understood ? buf.readUtf() : null;
			if (!VERSION.equals(clientVersion)) {
				listener.disconnect(mismatchMessage(clientVersion));
			}
		});
	}

	public static FriendlyByteBuf writeVersion() {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeUtf(VERSION);
		return buf;
	}

	private static Component mismatchMessage(@Nullable String clientVersion) {
		if (clientVersion == null) {
			return Component.translatableWithFallback("multiplayer.foxyfriends.missing", "This server requires FoxyFriends %s", VERSION);
		}

		return Component.translatableWithFallback("multiplayer.foxyfriends.version_mismatch", "This server requires FoxyFriends %s, you have %s", VERSION, clientVersion);
	}
}
