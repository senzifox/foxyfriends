package dev.senzifox.foxyfriends.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.senzifox.foxyfriends.client.pat.FoxPatAnimation;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
	private void foxyfriends$extractPat(Avatar avatar, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
		state.setData(FoxPatAnimation.SQUASH, FoxPatAnimation.squash(avatar, partialTicks));
	}

	@Inject(method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V", at = @At("TAIL"))
	private void foxyfriends$squashWhenPatted(AvatarRenderState state, PoseStack poseStack, float bodyRot, float scale, CallbackInfo ci) {
		FoxPatAnimation.apply(state, poseStack);
	}
}
