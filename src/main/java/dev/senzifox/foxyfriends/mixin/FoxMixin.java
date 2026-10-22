package dev.senzifox.foxyfriends.mixin;

import dev.senzifox.foxyfriends.defense.FoxOwnerHurtByTargetGoal;
import dev.senzifox.foxyfriends.defense.FoxOwnerHurtTargetGoal;
import dev.senzifox.foxyfriends.defense.FoxRetaliateGoal;
import dev.senzifox.foxyfriends.mode.FoxFollowOwnerGoal;
import dev.senzifox.foxyfriends.mode.FoxMode;
import dev.senzifox.foxyfriends.mode.FoxModes;
import dev.senzifox.foxyfriends.mode.FoxReturnHomeGoal;
import dev.senzifox.foxyfriends.mode.FoxSitGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Fox.class)
public abstract class FoxMixin extends Animal {
	protected FoxMixin(EntityType<? extends Animal> type, Level level) {
		super(type, level);
	}

	@Inject(method = "registerGoals", at = @At("TAIL"))
	private void foxyfriends$addModeGoals(CallbackInfo ci) {
		Fox fox = (Fox) (Object) this;
		goalSelector.addGoal(1, new FoxSitGoal(fox));
		goalSelector.addGoal(3, new FoxFollowOwnerGoal(fox));
		goalSelector.addGoal(8, new FoxReturnHomeGoal(fox));
		targetSelector.addGoal(1, new FoxOwnerHurtByTargetGoal(fox));
		targetSelector.addGoal(2, new FoxOwnerHurtTargetGoal(fox));
		targetSelector.addGoal(3, new FoxRetaliateGoal(fox));
	}

	@Override
	public void onLeashRemoved() {
		super.onLeashRemoved();
		Fox fox = (Fox) (Object) this;
		if (FoxModes.isInMode(fox, FoxMode.WANDER) && !hasHome()) {
			setHomeTo(blockPosition(), FoxModes.WANDER_RADIUS);
		}
	}

	@ModifyArg(
		method = "registerGoals",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/ai/goal/target/NearestAttackableTargetGoal;<init>(Lnet/minecraft/world/entity/Mob;Ljava/lang/Class;IZZLnet/minecraft/world/entity/ai/targeting/TargetingConditions$Selector;)V"
		),
		index = 5
	)
	private TargetingConditions.Selector foxyfriends$huntOnlyWhenAllowed(TargetingConditions.Selector selector) {
		Fox fox = (Fox) (Object) this;
		return (target, level) -> FoxModes.canHunt(fox) && selector.test(target, level);
	}
}
