package dev.senzifox.foxyfriends.client.pat;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.senzifox.foxyfriends.pat.FoxPatPayload;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.Nullable;

public final class FoxPatAnimation {
	public static final RenderStateDataKey<Float> SQUASH = RenderStateDataKey.create(() -> "foxyfriends:pat_squash");

	private static final float DURATION_TICKS = 6.0F;
	private static final float SQUASH_DEPTH = 0.25F;
	private static final float SPREAD = 0.1F;
	private static final Int2LongMap STARTED_AT = new Int2LongOpenHashMap();

	private FoxPatAnimation() {
	}

	public static void init() {
		ClientPlayNetworking.registerGlobalReceiver(FoxPatPayload.TYPE, (payload, context) -> {
			if (context.client().level != null) {
				STARTED_AT.put(payload.entityId(), context.client().level.getGameTime());
			}
		});
		ClientPlayConnectionEvents.DISCONNECT.register((listener, client) -> STARTED_AT.clear());
	}

	public static @Nullable Float squash(Entity entity, float partialTicks) {
		if (!STARTED_AT.containsKey(entity.getId())) {
			return null;
		}

		float progress = (entity.level().getGameTime() - STARTED_AT.get(entity.getId()) + partialTicks) / DURATION_TICKS;
		if (progress < 0.0F || progress >= 1.0F) {
			STARTED_AT.remove(entity.getId());
			return null;
		}

		return Mth.sin(progress * Mth.PI);
	}

	public static void apply(EntityRenderState state, PoseStack poseStack) {
		Float squash = state.getData(SQUASH);
		if (squash != null) {
			float width = 1.0F + SPREAD * squash;
			poseStack.scale(width, 1.0F - SQUASH_DEPTH * squash, width);
		}
	}
}
