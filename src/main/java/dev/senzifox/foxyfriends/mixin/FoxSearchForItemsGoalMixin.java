package dev.senzifox.foxyfriends.mixin;

import dev.senzifox.foxyfriends.armor.FoxArmor;
import java.util.function.Predicate;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets = "net.minecraft.world.entity.animal.fox.Fox$FoxSearchForItemsGoal")
public abstract class FoxSearchForItemsGoalMixin {
	@ModifyArg(
		method = {"canUse", "tick", "start"},
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"
		),
		index = 2
	)
	private Predicate<ItemEntity> foxyfriends$ignoreArmor(Predicate<ItemEntity> selector) {
		return item -> selector.test(item) && !FoxArmor.isFoxArmor(item.getItem());
	}
}
