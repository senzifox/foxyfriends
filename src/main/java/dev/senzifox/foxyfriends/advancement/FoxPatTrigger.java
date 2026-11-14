package dev.senzifox.foxyfriends.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Validatable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class FoxPatTrigger extends SimpleCriterionTrigger<FoxPatTrigger.TriggerInstance> {
	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, Entity patted, boolean foxAtHeart) {
		LootContext pattedContext = EntityPredicate.createContext(player, patted);
		this.trigger(player, instance -> instance.matches(pattedContext, foxAtHeart));
	}

	public record TriggerInstance(
		Optional<Holder<LootItemCondition>> player,
		Optional<Holder<LootItemCondition>> entity,
		Optional<Boolean> foxAtHeart
	) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
			LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entity),
			Codec.BOOL.optionalFieldOf("fox_at_heart").forGetter(TriggerInstance::foxAtHeart)
		).apply(instance, TriggerInstance::new));

		public boolean matches(LootContext patted, boolean foxAtHeart) {
			return this.foxAtHeart.map(expected -> expected == foxAtHeart).orElse(true)
				&& this.entity.map(condition -> condition.value().test(patted)).orElse(true);
		}

		@Override
		public void validate(ValidationContextSource validator) {
			SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
			Validatable.validateHolder(validator.entityContext(), "entity", this.entity);
		}
	}
}
