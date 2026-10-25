package dev.senzifox.foxyfriends.client.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.model.animal.fox.FoxModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.FoxRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.equipment.Equippable;

public class FoxArmorLayer extends RenderLayer<FoxRenderState, FoxModel> {
	public static final RenderStateDataKey<ItemStack> BODY_ARMOR = RenderStateDataKey.create(() -> "foxyfriends:body_armor");
	private static final String DYEABLE_ASSET = "leather";
	private static final String OVERLAY_SUFFIX = "_overlay";
	private static final int UNDYED_COLOR = -1;
	private static final int BASE_ORDER = 2;
	private static final int OVERLAY_ORDER = 3;

	public FoxArmorLayer(RenderLayerParent<FoxRenderState, FoxModel> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, FoxRenderState state, float yRot, float xRot) {
		ItemStack armor = state.getData(BODY_ARMOR);
		if (armor == null || armor.isEmpty() || state.isBaby || state.isInvisible) {
			return;
		}

		Equippable equippable = armor.get(DataComponents.EQUIPPABLE);
		if (equippable == null || equippable.assetId().isEmpty()) {
			return;
		}

		Identifier asset = equippable.assetId().get().identifier();
		if (asset.getPath().equals(DYEABLE_ASSET)) {
			int color = DyedItemColor.getOrDefault(armor, DyedItemColor.LEATHER_COLOR);
			submitLayer(poseStack, submitNodeCollector, lightCoords, state, texture(asset, ""), color, BASE_ORDER);
			submitLayer(poseStack, submitNodeCollector, lightCoords, state, texture(asset, OVERLAY_SUFFIX), UNDYED_COLOR, OVERLAY_ORDER);
		} else {
			submitLayer(poseStack, submitNodeCollector, lightCoords, state, texture(asset, ""), UNDYED_COLOR, BASE_ORDER);
		}
	}

	private static Identifier texture(Identifier asset, String suffix) {
		return asset.withPath(path -> "textures/entity/fox/armor/" + path + suffix + ".png");
	}

	private void submitLayer(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, FoxRenderState state, Identifier texture, int color, int order) {
		submitNodeCollector.order(order)
			.submitModel(
				this.getParentModel(),
				state,
				poseStack,
				RenderTypes.entityCutout(texture),
				lightCoords,
				OverlayTexture.NO_OVERLAY,
				color,
				null,
				state.outlineColor
			);
	}
}
