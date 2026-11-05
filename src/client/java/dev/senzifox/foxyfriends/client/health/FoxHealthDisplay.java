package dev.senzifox.foxyfriends.client.health;

import dev.senzifox.foxyfriends.client.config.FoxClientConfig;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import java.util.Comparator;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.fox.Fox;
import org.jspecify.annotations.Nullable;

public final class FoxHealthDisplay {
	private static final double HURT_RANGE = 32.0;

	private FoxHealthDisplay() {
	}

	public static void init() {
		ClientTickEvents.END_CLIENT_TICK.register(FoxHealthDisplay::tick);
	}

	private static void tick(Minecraft minecraft) {
		LocalPlayer player = minecraft.player;
		if (player == null || !FoxClientConfig.get().healthActionbar()) {
			return;
		}

		Fox fox = foxToShow(minecraft, player);
		if (fox != null) {
			minecraft.gui.hud.setOverlayMessage(healthLine(fox), false);
		}
	}

	private static @Nullable Fox foxToShow(Minecraft minecraft, LocalPlayer player) {
		if (minecraft.crosshairPickEntity instanceof Fox fox && FoxOwnership.isOwnedBy(fox, player)) {
			return fox;
		}

		return player.level().getEntitiesOfClass(Fox.class, player.getBoundingBox().inflate(HURT_RANGE),
				candidate -> candidate.hurtTime > 0 && FoxOwnership.isOwnedBy(candidate, player))
			.stream()
			.max(Comparator.comparingInt(candidate -> candidate.hurtTime))
			.orElse(null);
	}

	private static Component healthLine(Fox fox) {
		return fox.getDisplayName().copy()
			.append("  ")
			.append(Component.literal("❤ ").withStyle(ChatFormatting.RED))
			.append(Component.literal(Mth.ceil(fox.getHealth()) + "/" + Mth.ceil(fox.getMaxHealth())));
	}
}
