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

public class FoxKilledMobTrigger extends SimpleCriterionTrigger<FoxKilledMobTrigger.TriggerInstance> {
	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer owner, Fox fox, boolean ownersFight, boolean attackedOwner) {
		LootContext foxContext = EntityPredicate.createContext(owner, fox);
		double ownerHealth = owner.getHealth();
		this.trigger(owner, instance -> instance.matches(foxContext, ownersFight, attackedOwner, ownerHealth));
	}

	public record TriggerInstance(
		Optional<Holder<LootItemCondition>> player,
		Optional<Holder<LootItemCondition>> fox,
		Optional<Boolean> ownersFight,
		Optional<Boolean> attackedOwner,
		Optional<MinMaxBounds.Doubles> ownerHealth
	) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
			LootItemCondition.CODEC.optionalFieldOf("fox").forGetter(TriggerInstance::fox),
			Codec.BOOL.optionalFieldOf("owners_fight").forGetter(TriggerInstance::ownersFight),
			Codec.BOOL.optionalFieldOf("attacked_owner").forGetter(TriggerInstance::attackedOwner),
			MinMaxBounds.Doubles.CODEC.optionalFieldOf("owner_health").forGetter(TriggerInstance::ownerHealth)
		).apply(instance, TriggerInstance::new));

		public boolean matches(LootContext fox, boolean ownersFight, boolean attackedOwner, double ownerHealth) {
			return this.ownersFight.map(expected -> expected == ownersFight).orElse(true)
				&& this.attackedOwner.map(expected -> expected == attackedOwner).orElse(true)
				&& this.ownerHealth.map(bounds -> bounds.matches(ownerHealth)).orElse(true)
				&& this.fox.map(condition -> condition.value().test(fox)).orElse(true);
		}

		@Override
		public void validate(ValidationContextSource validator) {
			SimpleCriterionTrigger.SimpleInstance.super.validate(validator);
			Validatable.validateHolder(validator.entityContext(), "fox", this.fox);
		}
	}
}
