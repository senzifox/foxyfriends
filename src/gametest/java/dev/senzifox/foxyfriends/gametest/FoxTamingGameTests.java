package dev.senzifox.foxyfriends.gametest;

import dev.senzifox.foxyfriends.mixin.FoxAccessor;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
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

	@GameTest
	public void babyGoesToWhoeverFedTheFirstParent(GameTestHelper helper) {
		ServerPlayer owner = (ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
		ServerPlayer stranger = (ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
		Fox parent = helper.spawn(EntityTypes.FOX, FOX_POS);
		Fox partner = helper.spawn(EntityTypes.FOX, FOX_POS);
		FoxOwnership.tame(parent, owner);
		FoxOwnership.tame(partner, owner);

		parent.setInLove(stranger);
		parent.setInLoveTime(parent.getInLoveTime() - 100);
		partner.setInLove(owner);

		helper.assertTrue(FoxOwnership.isOwnedBy(parent.getBreedOffspring(helper.getLevel(), partner), stranger), "baby should go to the first breeder");
		helper.assertTrue(FoxOwnership.isOwnedBy(partner.getBreedOffspring(helper.getLevel(), parent), stranger), "baby should go to the first breeder either way");

		Fox wild = helper.spawn(EntityTypes.FOX, FOX_POS);
		Fox wildPartner = helper.spawn(EntityTypes.FOX, FOX_POS);
		wild.setInLove(owner);
		wildPartner.setInLove(owner);
		helper.assertFalse(FoxOwnership.isTame(wild.getBreedOffspring(helper.getLevel(), wildPartner)), "baby of wild foxes should stay wild");
		helper.succeed();
	}
}
