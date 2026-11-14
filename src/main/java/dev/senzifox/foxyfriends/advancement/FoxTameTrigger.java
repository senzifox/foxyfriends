package dev.senzifox.foxyfriends.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class FoxTameTrigger extends SimpleCriterionTrigger<FoxTameTrigger.TriggerInstance> {
	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, Fox fox, int feedings, boolean trusted) {
		LootContext foxContext = EntityPredicate.createContext(player, fox);
		this.trigger(player, instance -> instance.matches(foxContext, feedings, trusted));
	}

	public record TriggerInstance(
		Optional<Holder<LootItemCondition>> player,
		Optional<Holder<LootItemCondition>> entity,
		Optional<MinMaxBounds.Ints> feedings,
		Optional<Boolean> trusted
	) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
			LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entity),
			MinMaxBounds.Ints.CODEC.optionalFieldOf("feedings").forGetter(TriggerInstance::feedings),
			Codec.BOOL.optionalFieldOf("trusted").forGetter(TriggerInstance::trusted)
		).apply(instance, TriggerInstance::new));

		public boolean matches(LootContext fox, int feedings, boolean trusted) {
			return this.feedings.map(bounds -> bounds.matches(feedings)).orElse(true)
				&& this.trusted.map(expected -> expected == trusted).orElse(true)
				&& this.entity.map(condition -> condition.value().test(fox)).orElse(true);
		}

		@Override
		public void validate(ValidationContextSource validator) {
			SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
			Validatable.validateHolder(validator.entityContext(), "entity", this.entity);
		}
	}
}
