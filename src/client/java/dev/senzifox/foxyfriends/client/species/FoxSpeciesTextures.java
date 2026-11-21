package dev.senzifox.foxyfriends.client.species;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.species.FoxSpecies;
import java.util.EnumMap;
import java.util.Map;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.renderer.entity.state.FoxRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public final class FoxSpeciesTextures {
	public static final RenderStateDataKey<FoxSpecies> SPECIES = RenderStateDataKey.create(() -> "foxyfriends:species");
	private static final Map<FoxSpecies, Textures> TEXTURES = new EnumMap<>(FoxSpecies.class);

	static {
		for (FoxSpecies species : FoxSpecies.values()) {
			TEXTURES.put(species, Textures.of(species));
		}
	}

	private FoxSpeciesTextures() {
	}

	public static @Nullable Identifier get(FoxRenderState state) {
		FoxSpecies species = state.getData(SPECIES);
		return species == null ? null : TEXTURES.get(species).pick(state.isBaby, state.isSleeping);
	}

	private record Textures(Identifier adult, Identifier adultSleeping, Identifier baby, Identifier babySleeping) {
		static Textures of(FoxSpecies species) {
			String folder = "textures/entity/fox/" + species.getSerializedName() + "/";
			return new Textures(
				FoxyFriends.id(folder + "fox.png"),
				FoxyFriends.id(folder + "fox_sleep.png"),
				FoxyFriends.id(folder + "fox_baby.png"),
				FoxyFriends.id(folder + "fox_sleep_baby.png")
			);
		}

		Identifier pick(boolean isBaby, boolean isSleeping) {
			if (isBaby) {
				return isSleeping ? babySleeping : baby;
			}
			return isSleeping ? adultSleeping : adult;
		}
	}
}
