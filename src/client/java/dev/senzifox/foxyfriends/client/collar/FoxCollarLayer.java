package dev.senzifox.foxyfriends.client.collar;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.senzifox.foxyfriends.FoxyFriends;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.model.animal.fox.FoxModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.FoxRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.item.DyeColor;

public class FoxCollarLayer extends RenderLayer<FoxRenderState, FoxModel> {
	public static final RenderStateDataKey<DyeColor> COLLAR_COLOR = RenderStateDataKey.create(() -> "foxyfriends:collar_color");
	public static final RenderStateDataKey<Boolean> COLLAR_GLOWING = RenderStateDataKey.create(() -> "foxyfriends:collar_glowing");
	private static final Identifier COLLAR_LOCATION = FoxyFriends.id("textures/entity/fox/collar.png");
	private static final Identifier BABY_COLLAR_LOCATION = FoxyFriends.id("textures/entity/fox/collar_baby.png");

	public FoxCollarLayer(RenderLayerParent<FoxRenderState, FoxModel> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, FoxRenderState state, float yRot, float xRot) {
		DyeColor collarColor = state.getData(COLLAR_COLOR);
		if (collarColor != null && !state.isInvisible) {
			Identifier collarLocation = state.isBaby ? BABY_COLLAR_LOCATION : COLLAR_LOCATION;
			int collarLight = Boolean.TRUE.equals(state.getData(COLLAR_GLOWING)) ? LightCoordsUtil.FULL_BRIGHT : lightCoords;
			submitNodeCollector.order(1)
				.submitModel(
					this.getParentModel(),
					state,
					poseStack,
					RenderTypes.entityCutout(collarLocation),
					collarLight,
					OverlayTexture.NO_OVERLAY,
					collarColor.getTextureDiffuseColor(),
					null,
					state.outlineColor
				);
		}
	}
}
