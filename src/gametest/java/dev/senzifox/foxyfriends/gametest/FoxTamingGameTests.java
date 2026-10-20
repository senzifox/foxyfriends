package dev.senzifox.foxyfriends.gametest;

import dev.senzifox.foxyfriends.mixin.FoxAccessor;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;

public class FoxTamingGameTests {
	private static final BlockPos FOX_POS = new BlockPos(1, 1, 1);

	@GameTest
	public void tamedFoxKeepsItsOwner(GameTestHelper helper) {
		Fox fox = helper.spawn(EntityTypes.FOX, FOX_POS);
		Player owner = helper.makeMockServerPlayer(GameType.SURVIVAL);
		Player stranger = helper.makeMockServerPlayer(GameType.SURVIVAL);
		FoxOwnership.tame(fox, owner);
		((FoxAccessor) fox).foxyfriends$addTrustedEntity(stranger);

		ItemStack food = feed(fox, stranger, Items.RABBIT);

		helper.assertTrue(FoxOwnership.isOwnedBy(fox, owner), "fox should keep its first owner");
		helper.assertValueEqual(food.getCount(), 1, "food count");
		helper.succeed();
	}

	private static ItemStack feed(Fox fox, Player player, Item item) {
		player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(item));
		player.interactOn(fox, InteractionHand.MAIN_HAND, Vec3.ZERO);
		return player.getItemInHand(InteractionHand.MAIN_HAND);
	}
}
