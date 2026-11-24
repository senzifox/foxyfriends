package dev.senzifox.foxyfriends.species;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.config.FoxConfig;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public final class FoxSpeciesSpawns {
	public static final TagKey<Biome> SPAWNS_CORSAC_FOXES = TagKey.create(Registries.BIOME, FoxyFriends.id("spawns_corsac_foxes"));

	private static final int DESERT_WEIGHT = 1;
	private static final int SAVANNA_WEIGHT = 2;
	private static final int MIN_GROUP = 1;
	private static final int MAX_GROUP = 2;

	private FoxSpeciesSpawns() {
	}

	public static void init() {
		BiomeModifications.create(FoxyFriends.id("corsac_foxes"))
			.add(ModificationPhase.ADDITIONS, whenEnabled(BiomeSelectors.includeByKey(Biomes.DESERT)), context -> addFoxes(context, DESERT_WEIGHT))
			.add(ModificationPhase.ADDITIONS, whenEnabled(BiomeSelectors.tag(BiomeTags.IS_SAVANNA)), context -> addFoxes(context, SAVANNA_WEIGHT));
	}

	public static void pickByBiome(Fox fox, Holder<Biome> biome) {
		if (FoxConfig.get().newSpecies() && biome.is(SPAWNS_CORSAC_FOXES)) {
			fox.setAttached(FoxSpeciesData.SPECIES, FoxSpecies.CORSAC);
		}
	}

	private static Predicate<BiomeSelectionContext> whenEnabled(Predicate<BiomeSelectionContext> selector) {
		return context -> FoxConfig.get().newSpecies() && selector.test(context);
	}

	private static void addFoxes(BiomeModificationContext context, int weight) {
		context.getMobSpawnSettings().addSpawn(EntityTypes.FOX, weight, MIN_GROUP, MAX_GROUP);
	}
}
