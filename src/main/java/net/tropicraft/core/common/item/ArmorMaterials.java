package net.tropicraft.core.common.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class ArmorMaterials {
    public static IArmorMaterial ASHEN_MASK = new AshenMask();
    public static IArmorMaterial NIGEL_STACHE = new NigelStache();

    private static class AshenMask implements IArmorMaterial {
        @Override
        public int getDurability(EquipmentSlotType slotIn) {
            return 10;
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slotIn) {
            return slotIn == EquipmentSlotType.HEAD ? 1 : 0;
        }

        @Override
        public int getEnchantability() {
            return 15;
        }

        @Override
        public SoundEvent getSoundEvent() {
            return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.EMPTY;//TODO Ingredient.fromItems(TropicraftItems.ASHEN_MASKS);
        }

        @Override
        public String getName() {
            return "mask";
        }

        @Override
        public float getToughness() {
            return 0;
        }
    }

    private static class NigelStache implements IArmorMaterial {

        @Override
        public int getDurability(EquipmentSlotType slotIn) {
            return 10;
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slotIn) {
            return slotIn == EquipmentSlotType.HEAD ? 1 : 0;
        }

        @Override
        public int getEnchantability() {
            return 15;
        }

        @Override
        public SoundEvent getSoundEvent() {
            return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairMaterial() {
            return Ingredient.EMPTY; // TODO Ingredient.fromItems(TropicraftItems.NIGEL_STACHE);
        }

        @Override
        public String getName() {
            return "nigel";
        }

        @Override
        public float getToughness() {
            return 0;
        }
    }
}
