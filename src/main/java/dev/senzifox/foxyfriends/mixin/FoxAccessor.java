package dev.senzifox.foxyfriends.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.fox.Fox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Fox.class)
public interface FoxAccessor {
	@Invoker("trusts")
	boolean foxyfriends$trusts(LivingEntity entity);

	@Invoker("addTrustedEntity")
	void foxyfriends$addTrustedEntity(LivingEntity entity);

	@Invoker("clearStates")
	void foxyfriends$clearStates();

	@Invoker("setSleeping")
	void foxyfriends$setSleeping(boolean sleeping);
}
