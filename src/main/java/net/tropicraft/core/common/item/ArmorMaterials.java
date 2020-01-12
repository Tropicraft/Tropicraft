package net.tropicraft.core.common.item;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.tropicraft.core.common.TropicraftTags;

public class ArmorMaterials {
    public static IArmorMaterial ASHEN_MASK = new AshenMask();
    public static IArmorMaterial NIGEL_STACHE = new NigelStache();
    public static IArmorMaterial SCALE_ARMOR = createArmorMaterial(
            18,
            new int[] {2, 5, 6, 2},
            9,
            SoundEvents.ITEM_ARMOR_EQUIP_CHAIN,
            Ingredient.fromItems(TropicraftItems.SCALE.get()),
            "scale",
            0.5f
    );
    public static IArmorMaterial FIRE_ARMOR = createArmorMaterial(
            12,
            new int[] {2, 4, 5, 2},
            9,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            null,
            "fire",
            0.1f
    );

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
            return Ingredient.fromTag(TropicraftTags.Items.ASHEN_MASKS);
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
            return Ingredient.fromItems(TropicraftItems.NIGEL_STACHE.get());
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

    public static IArmorMaterial createArmorMaterial(final int durability, final int[] dmgReduction, final int enchantability, final SoundEvent soundEvent,
                                                     final Ingredient repairMaterial, final String name, final float toughness) {
        return new IArmorMaterial() {
            @Override
            public int getDurability(EquipmentSlotType equipmentSlotType) {
                return durability;
            }

            @Override
            public int getDamageReductionAmount(EquipmentSlotType equipmentSlotType) {
                return dmgReduction[equipmentSlotType.getIndex()];
            }

            @Override
            public int getEnchantability() {
                return enchantability;
            }

            @Override
            public SoundEvent getSoundEvent() {
                return soundEvent;
            }

            @Override
            public Ingredient getRepairMaterial() {
                return repairMaterial;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public float getToughness() {
                return toughness;
            }
        };
    }
}
