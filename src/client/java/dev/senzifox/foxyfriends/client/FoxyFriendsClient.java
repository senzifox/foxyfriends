package dev.senzifox.foxyfriends.client;

import dev.senzifox.foxyfriends.client.armor.FoxArmorLayer;
import dev.senzifox.foxyfriends.client.collar.FoxCollarLayer;
import dev.senzifox.foxyfriends.network.FoxVersionCheck;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.renderer.entity.FoxRenderer;

public class FoxyFriendsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientLoginNetworking.registerGlobalReceiver(FoxVersionCheck.CHANNEL, (client, listener, buf, callbacks) -> CompletableFuture.completedFuture(FoxVersionCheck.writeVersion()));
		LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
			if (entityRenderer instanceof FoxRenderer foxRenderer) {
				registrationHelper.register(new FoxCollarLayer(foxRenderer));
				registrationHelper.register(new FoxArmorLayer(foxRenderer));
			}
		});
	}
}
