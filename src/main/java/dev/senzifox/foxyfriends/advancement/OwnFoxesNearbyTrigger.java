package dev.senzifox.foxyfriends.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import java.util.Optional;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class OwnFoxesNearbyTrigger extends SimpleCriterionTrigger<OwnFoxesNearbyTrigger.TriggerInstance> {
	public static final double RADIUS = 16.0;

	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, int notYetSpawned) {
		int count = player.level().getEntitiesOfClass(Fox.class, player.getBoundingBox().inflate(RADIUS), fox -> FoxOwnership.isOwnedBy(fox, player)).size() + notYetSpawned;
		this.trigger(player, instance -> instance.matches(count));
	}

	public record TriggerInstance(
		Optional<Holder<LootItemCondition>> player,
		Optional<MinMaxBounds.Ints> count
	) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
			MinMaxBounds.Ints.CODEC.optionalFieldOf("count").forGetter(TriggerInstance::count)
		).apply(instance, TriggerInstance::new));

		public boolean matches(int count) {
			return this.count.map(bounds -> bounds.matches(count)).orElse(true);
		}
	}
}
