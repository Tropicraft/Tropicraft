package net.tropicraft.core.common.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;
import net.tropicraft.core.client.TropicraftEquipmentAssets;
import net.tropicraft.core.common.TropicraftTags;

import java.util.Map;

public class TropicraftArmorMaterials {
    public static final ArmorMaterial ASHEN_MASK = new ArmorMaterial(
            0,
            Map.of(
                    ArmorType.HELMET, 1
            ),
            15,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0,
            0,
            TropicraftTags.Items.ASHEN_MASKS,
            TropicraftEquipmentAssets.NONE
    );
    public static final ArmorMaterial NIGEL_STACHE = new ArmorMaterial(
            0,
            Map.of(
                    ArmorType.HELMET, 1
            ),
            15,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0,
            0,
            TropicraftTags.Items.REPAIRS_NIGEL_STACHE,
            TropicraftEquipmentAssets.NONE
    );
    public static final ArmorMaterial SCALE_ARMOR = new ArmorMaterial(
            18,
            Map.of(
                    ArmorType.BOOTS, 2,
                    ArmorType.LEGGINGS, 5,
                    ArmorType.CHESTPLATE, 6,
                    ArmorType.HELMET, 2
            ),
            9,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            0.5f,
            0.0f,
            TropicraftTags.Items.REPAIRS_SCALE_ARMOR,
            TropicraftEquipmentAssets.SCALE
    );
    public static final ArmorMaterial FIRE_ARMOR = new ArmorMaterial(
            12,
            Map.of(
                    ArmorType.BOOTS, 2,
                    ArmorType.LEGGINGS, 4,
                    ArmorType.CHESTPLATE, 5,
                    ArmorType.HELMET, 2
            ),
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            0.1f,
            0.0f,
            TropicraftTags.Items.REPAIRS_FIRE_ARMOR,
            TropicraftEquipmentAssets.FIRE
    );
    public static final ArmorMaterial SCUBA_PINK = createScuba(TropicraftEquipmentAssets.PINK_SCUBA);
    public static final ArmorMaterial SCUBA_YELLOW = createScuba(TropicraftEquipmentAssets.YELLOW_SCUBA);

    private static ArmorMaterial createScuba(ResourceKey<EquipmentAsset> asset) {
        return new ArmorMaterial(
                10,
                Map.of(
                        ArmorType.BOOTS, 0,
                        ArmorType.LEGGINGS, 0,
                        ArmorType.CHESTPLATE, 0,
                        ArmorType.HELMET, 0
                ),
                0,
                SoundEvents.ARMOR_EQUIP_GENERIC,
                0,
                0.0f,
                TropicraftTags.Items.REPAIRS_SCUBA_GEAR,
                asset
        );
    }

    public static Item.Properties applyNoDurability(Item.Properties properties, ArmorMaterial material, ArmorType armorType) {
        Equippable.Builder equippable = Equippable.builder(armorType.getSlot())
                .setEquipSound(material.equipSound());
        if (material.assetId() != TropicraftEquipmentAssets.NONE) {
            equippable.setAsset(material.assetId());
        }
        if (material.enchantmentValue() != 0) {
            properties.enchantable(material.enchantmentValue());
        }
        return properties
                .attributes(material.createAttributes(armorType))
                .component(DataComponents.EQUIPPABLE, equippable.build())
                .repairable(material.repairIngredient());
    }

    public static Item.Properties applySafe(Item.Properties properties, ArmorMaterial material, ArmorType armorType) {
        return applyNoDurability(properties, material, armorType)
                .durability(armorType.getDurability(material.durability()));
    }
}
