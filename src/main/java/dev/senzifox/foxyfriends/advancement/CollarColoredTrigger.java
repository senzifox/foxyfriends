package dev.senzifox.foxyfriends.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class CollarColoredTrigger extends SimpleCriterionTrigger<CollarColoredTrigger.TriggerInstance> {
	@Override
	public Codec<TriggerInstance> codec() {
		return TriggerInstance.CODEC;
	}

	public void trigger(ServerPlayer player, DyeColor color, boolean dyed) {
		this.trigger(player, instance -> instance.matches(color, dyed));
	}

	public record TriggerInstance(
		Optional<Holder<LootItemCondition>> player,
		Optional<DyeColor> color,
		Optional<Boolean> dyed
	) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
			DyeColor.CODEC.optionalFieldOf("color").forGetter(TriggerInstance::color),
			Codec.BOOL.optionalFieldOf("dyed").forGetter(TriggerInstance::dyed)
		).apply(instance, TriggerInstance::new));

		public boolean matches(DyeColor color, boolean dyed) {
			return this.color.map(expected -> expected == color).orElse(true)
				&& this.dyed.map(expected -> expected == dyed).orElse(true);
		}
	}
}
