package dev.senzifox.foxyfriends.client.mixin;

import dev.senzifox.foxyfriends.client.armor.FoxArmorLayer;
import dev.senzifox.foxyfriends.client.collar.FoxCollarLayer;
import dev.senzifox.foxyfriends.collar.FoxCollar;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.minecraft.client.renderer.entity.FoxRenderer;
import net.minecraft.client.renderer.entity.state.FoxRenderState;
import net.minecraft.world.entity.animal.fox.Fox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoxRenderer.class)
public abstract class FoxRendererMixin {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/fox/Fox;Lnet/minecraft/client/renderer/entity/state/FoxRenderState;F)V", at = @At("TAIL"))
	private void foxyfriends$extractGear(Fox fox, FoxRenderState state, float partialTicks, CallbackInfo ci) {
		state.setData(FoxCollarLayer.COLLAR_COLOR, FoxOwnership.isTame(fox) ? FoxCollar.getColor(fox) : null);
		state.setData(FoxArmorLayer.BODY_ARMOR, fox.getBodyArmorItem().copy());
	}
}
