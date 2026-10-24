package dev.senzifox.foxyfriends.gametest;

import dev.senzifox.foxyfriends.armor.FoxArmor;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.Vec3;

public class FoxArmorGameTests {
	private static final BlockPos FOX_POS = new BlockPos(1, 0, 1);

	@GameTest
	public void strangerCannotPutArmorOnFox(GameTestHelper helper) {
		Fox fox = spawnTame(helper, helper.makeMockPlayer(GameType.SURVIVAL));

		ItemStack armor = use(fox, helper.makeMockPlayer(GameType.SURVIVAL), FoxArmor.IRON_FOX_ARMOR);

		assertBare(helper, fox);
		helper.assertValueEqual(armor.getCount(), 1, "armor count");
		helper.succeed();
	}

	@GameTest
	public void dressedFoxKeepsItsArmor(GameTestHelper helper) {
		Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
		Fox fox = spawnTame(helper, owner);
		use(fox, owner, FoxArmor.IRON_FOX_ARMOR);

		ItemStack armor = use(fox, owner, FoxArmor.DIAMOND_FOX_ARMOR);

		helper.assertTrue(fox.getBodyArmorItem().is(FoxArmor.IRON_FOX_ARMOR), "fox should keep iron armor");
		helper.assertValueEqual(armor.getCount(), 1, "armor count");
		helper.succeed();
	}

	@GameTest
	public void strangerCannotShearArmorOff(GameTestHelper helper) {
		Player owner = helper.makeMockServerPlayer(GameType.SURVIVAL);
		Fox fox = spawnTame(helper, owner);
		use(fox, owner, FoxArmor.GOLDEN_FOX_ARMOR);

		use(fox, helper.makeMockServerPlayer(GameType.SURVIVAL), Items.SHEARS);

		helper.assertTrue(fox.getBodyArmorItem().is(FoxArmor.GOLDEN_FOX_ARMOR), "fox should keep golden armor");
		helper.succeed();
	}

	@GameTest
	public void armorSurvivesReload(GameTestHelper helper) {
		Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
		Fox fox = spawnTame(helper, owner);
		use(fox, owner, FoxArmor.NETHERITE_FOX_ARMOR);

		TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
		fox.saveWithoutId(output);
		Fox loaded = EntityTypes.FOX.create(helper.getLevel(), EntitySpawnReason.LOAD);
		loaded.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), output.buildResult()));

		helper.assertTrue(loaded.getBodyArmorItem().is(FoxArmor.NETHERITE_FOX_ARMOR), "armor after reload");
		helper.assertTrue(loaded.getDropChances().isPreserved(EquipmentSlot.BODY), "armor should still drop on death");
		helper.succeed();
	}

	private static Fox spawnAdult(GameTestHelper helper) {
		Fox fox = helper.spawn(EntityTypes.FOX, FOX_POS);
		fox.setAge(0);
		return fox;
	}

	private static Fox spawnTame(GameTestHelper helper, Player owner) {
		Fox fox = spawnAdult(helper);
		FoxOwnership.tame(fox, owner);
		return fox;
	}

	private static void assertBare(GameTestHelper helper, Fox fox) {
		helper.assertTrue(fox.getBodyArmorItem().isEmpty(), "fox should wear no armor");
	}

	private static ItemStack use(Fox fox, Player player, Item item) {
		player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(item));
		player.interactOn(fox, InteractionHand.MAIN_HAND, Vec3.ZERO);
		return player.getItemInHand(InteractionHand.MAIN_HAND);
	}
}
