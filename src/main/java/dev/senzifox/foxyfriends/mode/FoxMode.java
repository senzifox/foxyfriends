package dev.senzifox.foxyfriends.mode;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public enum FoxMode implements StringRepresentable {
	FOLLOW("follow"),
	SIT("sit"),
	WANDER("wander");

	public static final Codec<FoxMode> CODEC = StringRepresentable.fromEnum(FoxMode::values);
	public static final StreamCodec<ByteBuf, FoxMode> STREAM_CODEC = ByteBufCodecs.idMapper(index -> values()[index], FoxMode::ordinal);

	private final String name;

	FoxMode(String name) {
		this.name = name;
	}

	public FoxMode next() {
		return values()[(ordinal() + 1) % values().length];
	}

	@Override
	public String getSerializedName() {
		return name;
	}
}
