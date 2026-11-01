package dev.senzifox.foxyfriends.mixin;

import dev.senzifox.foxyfriends.armor.FoxArmor;
import dev.senzifox.foxyfriends.care.FoxCare;
import dev.senzifox.foxyfriends.defense.FoxOwnerHurtByTargetGoal;
import dev.senzifox.foxyfriends.defense.FoxOwnerHurtTargetGoal;
import dev.senzifox.foxyfriends.defense.FoxRetaliateGoal;
import dev.senzifox.foxyfriends.mode.FoxFollowOwnerGoal;
import dev.senzifox.foxyfriends.mode.FoxMode;
import dev.senzifox.foxyfriends.mode.FoxModes;
import dev.senzifox.foxyfriends.mode.FoxReturnHomeGoal;
import dev.senzifox.foxyfriends.mode.FoxSitGoal;
import dev.senzifox.foxyfriends.taming.FoxBreeding;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

	@Inject(method = "aiStep", at = @At("TAIL"))
	private void foxyfriends$care(CallbackInfo ci) {
		FoxCare.tick((Fox) (Object) this);
	}

	@Override
	public void onLeashRemoved() {
		super.onLeashRemoved();
		Fox fox = (Fox) (Object) this;
		if (FoxModes.isInMode(fox, FoxMode.WANDER) && !hasHome()) {
			setHomeTo(blockPosition(), FoxModes.WANDER_RADIUS);
		}
	}

	@Inject(method = "canHoldItem", at = @At("HEAD"), cancellable = true)
	private void foxyfriends$keepArmorOutOfMouth(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (FoxArmor.isFoxArmor(stack)) {
			cir.setReturnValue(false);
		}
	}

	@Override
	protected boolean canShearEquipment(Player player) {
		return FoxOwnership.isOwnedBy((Fox) (Object) this, player) && super.canShearEquipment(player);
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

	@ModifyArg(
		method = "registerGoals",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/entity/ai/goal/AvoidEntityGoal;<init>(Lnet/minecraft/world/entity/PathfinderMob;Ljava/lang/Class;FDDLjava/util/function/Predicate;)V",
			ordinal = 0
		),
		index = 5
	)
	private Predicate<LivingEntity> foxyfriends$fleePlayersOnlyWhenWild(Predicate<LivingEntity> predicate) {
		Fox fox = (Fox) (Object) this;
		return entity -> !FoxOwnership.isTame(fox) && predicate.test(entity);
	}

	@Inject(method = "getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/animal/fox/Fox;", at = @At("RETURN"))
	private void foxyfriends$inheritOwner(ServerLevel level, AgeableMob partner, CallbackInfoReturnable<Fox> cir) {
		if (cir.getReturnValue() instanceof Fox baby && partner instanceof Fox partnerFox) {
			FoxBreeding.onBorn(baby, (Fox) (Object) this, partnerFox);
		}
	}
}
