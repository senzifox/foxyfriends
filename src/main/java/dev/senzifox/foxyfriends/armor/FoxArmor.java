package dev.senzifox.foxyfriends.armor;

import dev.senzifox.foxyfriends.FoxyFriends;
import dev.senzifox.foxyfriends.taming.FoxOwnership;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import org.jspecify.annotations.Nullable;

public final class FoxArmor {
	public static final Item LEATHER_FOX_ARMOR = register("leather_fox_armor", ArmorMaterials.LEATHER, new Item.Properties());
	public static final Item COPPER_FOX_ARMOR = register("copper_fox_armor", ArmorMaterials.COPPER, new Item.Properties());
	public static final Item IRON_FOX_ARMOR = register("iron_fox_armor", ArmorMaterials.IRON, new Item.Properties());
	public static final Item GOLDEN_FOX_ARMOR = register("golden_fox_armor", ArmorMaterials.GOLD, new Item.Properties());
	public static final Item DIAMOND_FOX_ARMOR = register("diamond_fox_armor", ArmorMaterials.DIAMOND, new Item.Properties());
	public static final Item NETHERITE_FOX_ARMOR = register("netherite_fox_armor", ArmorMaterials.NETHERITE, new Item.Properties().fireResistant());

	private FoxArmor() {
	}

	public static void init() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(output -> output.insertAfter(
			Items.WOLF_ARMOR,
			LEATHER_FOX_ARMOR,
			COPPER_FOX_ARMOR,
			IRON_FOX_ARMOR,
			GOLDEN_FOX_ARMOR,
			DIAMOND_FOX_ARMOR,
			NETHERITE_FOX_ARMOR
		));
	}

	public static @Nullable InteractionResult interact(Fox fox, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!fox.isEquippableInSlot(stack, EquipmentSlot.BODY)
			|| fox.isBaby()
			|| !fox.getBodyArmorItem().isEmpty()
			|| !FoxOwnership.isOwnedBy(fox, player)) {
			return null;
		}

		if (!fox.level().isClientSide()) {
			fox.setItemSlot(EquipmentSlot.BODY, stack.copyWithCount(1));
			fox.setGuaranteedDrop(EquipmentSlot.BODY);
			stack.consume(1, player);
		}

		return InteractionResult.SUCCESS;
	}

	public static boolean isFoxArmor(ItemStack stack) {
		Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
		return equippable != null
			&& equippable.slot() == EquipmentSlot.BODY
			&& equippable.canBeEquippedBy(EntityTypes.FOX.builtInRegistryHolder());
	}

	private static Item register(String name, ArmorMaterial material, Item.Properties properties) {
		ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, FoxyFriends.id(name));
		ResourceKey<EquipmentAsset> asset = ResourceKey.create(EquipmentAssets.ROOT_ID, FoxyFriends.id(material.assetId().identifier().getPath()));
		properties
			.attributes(material.createAttributes(ArmorType.BODY))
			.component(
				DataComponents.EQUIPPABLE,
				Equippable.builder(EquipmentSlot.BODY)
					.setEquipSound(material.equipSound())
					.setAsset(asset)
					.setAllowedEntities(EntityTypes.FOX)
					.setDamageOnHurt(false)
					.setCanBeSheared(true)
					.setShearingSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ARMOR_UNEQUIP_WOLF))
					.build()
			)
			.stacksTo(1);
		return Registry.register(BuiltInRegistries.ITEM, key, new Item(properties.setId(key)));
	}
}
