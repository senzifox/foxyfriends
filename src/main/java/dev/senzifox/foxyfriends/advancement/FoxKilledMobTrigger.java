package dev.senzifox.foxyfriends.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class FoxKilledMobTrigger extends SimpleCriterionTrigger<FoxKilledMobTrigger.TriggerInstance> {
	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer owner, boolean ownersFight) {
		this.trigger(owner, instance -> instance.matches(ownersFight));
	}

	public record TriggerInstance(
		Optional<Holder<LootItemCondition>> player,
		Optional<Boolean> ownersFight
	) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
			Codec.BOOL.optionalFieldOf("owners_fight").forGetter(TriggerInstance::ownersFight)
		).apply(instance, TriggerInstance::new));

		public boolean matches(boolean ownersFight) {
			return this.ownersFight.map(expected -> expected == ownersFight).orElse(true);
		}
	}
}
