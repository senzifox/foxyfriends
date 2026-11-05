package dev.senzifox.foxyfriends.pat;

import dev.senzifox.foxyfriends.FoxyFriends;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record FoxPatPayload(int entityId) implements CustomPacketPayload {
	public static final Type<FoxPatPayload> TYPE = new Type<>(FoxyFriends.id("pat"));
	public static final StreamCodec<ByteBuf, FoxPatPayload> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(FoxPatPayload::new, FoxPatPayload::entityId);

	@Override
	public Type<FoxPatPayload> type() {
		return TYPE;
	}
}
