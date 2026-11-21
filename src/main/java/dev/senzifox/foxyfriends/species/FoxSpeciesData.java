package dev.senzifox.foxyfriends.species;

import dev.senzifox.foxyfriends.FoxyFriends;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.world.entity.animal.fox.Fox;
import org.jspecify.annotations.Nullable;

public final class FoxSpeciesData {
	public static final AttachmentType<FoxSpecies> SPECIES = AttachmentRegistry.create(
		FoxyFriends.id("species"),
		builder -> builder
			.persistent(FoxSpecies.CODEC)
			.syncWith(FoxSpecies.STREAM_CODEC, AttachmentSyncPredicate.all())
	);

	private FoxSpeciesData() {
	}

	public static void init() {
	}

	public static @Nullable FoxSpecies get(Fox fox) {
		return fox.getAttached(SPECIES);
	}
}
