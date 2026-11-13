package dev.senzifox.foxyfriends.client;

import dev.senzifox.foxyfriends.client.armor.FoxArmorLayer;
import dev.senzifox.foxyfriends.client.collar.FoxCollarLayer;
import dev.senzifox.foxyfriends.client.config.FoxClientConfig;
import dev.senzifox.foxyfriends.client.health.FoxHealthDisplay;
import dev.senzifox.foxyfriends.client.pat.FoxPatAnimation;
import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.network.FoxVersionCheck;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.pack.PackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.entity.FoxRenderer;
import net.minecraft.network.chat.Component;

public class FoxyFriendsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		FoxClientConfig.init();
		FoxHealthDisplay.init();
		FoxPatAnimation.init();
		ResourceLoader.registerBuiltinPack(FoxyFriends.id("he_fox"), FabricLoader.getInstance().getModContainer(FoxyFriends.MOD_ID).orElseThrow(), Component.translatable("pack.foxyfriends.he_fox"), PackActivationType.DEFAULT_ENABLED);
		ClientLoginNetworking.registerGlobalReceiver(FoxVersionCheck.CHANNEL, (client, listener, buf, callbacks) -> CompletableFuture.completedFuture(FoxVersionCheck.writeVersion()));
		LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
			if (entityRenderer instanceof FoxRenderer foxRenderer) {
				registrationHelper.register(new FoxCollarLayer(foxRenderer));
				registrationHelper.register(new FoxArmorLayer(foxRenderer));
			}
		});
	}
}
