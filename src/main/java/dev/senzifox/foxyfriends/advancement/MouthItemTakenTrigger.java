package dev.senzifox.foxyfriends.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class MouthItemTakenTrigger extends SimpleCriterionTrigger<MouthItemTakenTrigger.TriggerInstance> {
	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, ItemStack taken) {
		this.trigger(player, instance -> instance.matches(taken));
	}

	public record TriggerInstance(
		Optional<Holder<LootItemCondition>> player,
		Optional<ItemPredicate> item,
		Optional<Rarity> minRarity
	) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
			ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item),
			Rarity.CODEC.optionalFieldOf("min_rarity").forGetter(TriggerInstance::minRarity)
		).apply(instance, TriggerInstance::new));

		public boolean matches(ItemStack taken) {
			return this.item.map(predicate -> predicate.test(taken)).orElse(true)
				&& this.minRarity.map(rarity -> taken.getRarity().compareTo(rarity) >= 0).orElse(true);
		}
	}
}
