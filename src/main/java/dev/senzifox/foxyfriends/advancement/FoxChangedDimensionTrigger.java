package dev.senzifox.foxyfriends.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class FoxChangedDimensionTrigger extends SimpleCriterionTrigger<FoxChangedDimensionTrigger.TriggerInstance> {
	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, ResourceKey<Level> destination) {
		this.trigger(player, instance -> instance.matches(destination));
	}

	public record TriggerInstance(
		Optional<Holder<LootItemCondition>> player,
		Optional<ResourceKey<Level>> to
	) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
			ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("to").forGetter(TriggerInstance::to)
		).apply(instance, TriggerInstance::new));

		public boolean matches(ResourceKey<Level> destination) {
			return this.to.map(destination::equals).orElse(true);
		}
	}
}
