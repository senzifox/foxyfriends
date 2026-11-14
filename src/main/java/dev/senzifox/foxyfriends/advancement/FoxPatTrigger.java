package dev.senzifox.foxyfriends.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import dev.senzifox.foxyfriends.stats.FoxStats;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.fox.Fox;
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
		boolean foxSleeping = patted instanceof Fox fox && fox.isSleeping();
		boolean foxTamed = patted instanceof Fox fox && FoxOwnership.isTame(fox);
		int total = player.getStats().getValue(Stats.CUSTOM.get(FoxStats.FOXES_PATTED));
		this.trigger(player, instance -> instance.matches(pattedContext, foxAtHeart, foxSleeping, foxTamed, total));
	}

	public record TriggerInstance(
		Optional<Holder<LootItemCondition>> player,
		Optional<Holder<LootItemCondition>> entity,
		Optional<Boolean> foxAtHeart,
		Optional<Boolean> foxSleeping,
		Optional<Boolean> foxTamed,
		Optional<MinMaxBounds.Ints> total
	) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
			LootItemCondition.CODEC.optionalFieldOf("entity").forGetter(TriggerInstance::entity),
			Codec.BOOL.optionalFieldOf("fox_at_heart").forGetter(TriggerInstance::foxAtHeart),
			Codec.BOOL.optionalFieldOf("fox_sleeping").forGetter(TriggerInstance::foxSleeping),
			Codec.BOOL.optionalFieldOf("fox_tamed").forGetter(TriggerInstance::foxTamed),
			MinMaxBounds.Ints.CODEC.optionalFieldOf("total").forGetter(TriggerInstance::total)
		).apply(instance, TriggerInstance::new));

		public boolean matches(LootContext patted, boolean foxAtHeart, boolean foxSleeping, boolean foxTamed, int total) {
			return this.foxAtHeart.map(expected -> expected == foxAtHeart).orElse(true)
				&& this.foxSleeping.map(expected -> expected == foxSleeping).orElse(true)
				&& this.foxTamed.map(expected -> expected == foxTamed).orElse(true)
				&& this.total.map(bounds -> bounds.matches(total)).orElse(true)
				&& this.entity.map(condition -> condition.value().test(patted)).orElse(true);
		}

		@Override
		public void validate(ValidationContextSource validator) {
			SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
			Validatable.validateHolder(validator.entityContext(), "entity", this.entity);
		}
	}
}
