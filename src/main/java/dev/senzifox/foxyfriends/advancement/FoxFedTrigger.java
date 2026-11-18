package dev.senzifox.foxyfriends.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class FoxFedTrigger extends SimpleCriterionTrigger<FoxFedTrigger.TriggerInstance> {
	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, double healthFractionBefore) {
		this.trigger(player, instance -> instance.matches(healthFractionBefore));
	}

	public record TriggerInstance(
		Optional<Holder<LootItemCondition>> player,
		Optional<MinMaxBounds.Doubles> healthFraction
	) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
			MinMaxBounds.Doubles.CODEC.optionalFieldOf("health_fraction").forGetter(TriggerInstance::healthFraction)
		).apply(instance, TriggerInstance::new));

		public boolean matches(double healthFractionBefore) {
			return this.healthFraction.map(bounds -> bounds.matches(healthFractionBefore)).orElse(true);
		}
	}
}
