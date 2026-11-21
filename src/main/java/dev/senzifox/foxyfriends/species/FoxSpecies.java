package dev.senzifox.foxyfriends.species;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public enum FoxSpecies implements StringRepresentable {
	CORSAC("corsac");

	public static final Codec<FoxSpecies> CODEC = StringRepresentable.fromEnum(FoxSpecies::values);
	public static final StreamCodec<ByteBuf, FoxSpecies> STREAM_CODEC = ByteBufCodecs.idMapper(index -> values()[index], FoxSpecies::ordinal);

	private final String name;

	FoxSpecies(String name) {
		this.name = name;
	}

	@Override
	public String getSerializedName() {
		return name;
	}
}
