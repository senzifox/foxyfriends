package dev.senzifox.foxyfriends.mixin;

import dev.senzifox.foxyfriends.mode.FoxSitGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Fox.class)
public abstract class FoxMixin extends Animal {
	protected FoxMixin(EntityType<? extends Animal> type, Level level) {
		super(type, level);
	}

	@Inject(method = "registerGoals", at = @At("TAIL"))
	private void foxyfriends$addModeGoals(CallbackInfo ci) {
		goalSelector.addGoal(1, new FoxSitGoal((Fox) (Object) this));
	}
}
