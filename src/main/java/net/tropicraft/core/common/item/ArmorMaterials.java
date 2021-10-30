package net.tropicraft.core.common.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.tropicraft.core.common.TropicraftTags;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class ArmorMaterials {
    private static final Ingredient NO_INGREDIENT = new Ingredient(Stream.empty()) {
        @Override
        public boolean test(@Nullable ItemStack stack) {
            return false;
        }
    };

    public static final ArmorMaterial ASHEN_MASK = new AshenMask();
    public static final ArmorMaterial NIGEL_STACHE = new NigelStache();
    public static final ArmorMaterial SCALE_ARMOR = createArmorMaterial(
            18,
            new int[] {2, 5, 6, 2},
            9,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            Ingredient.of(TropicraftItems.SCALE.get()),
            "scale",
            0.5f,
            0.0F
    );
    public static final ArmorMaterial FIRE_ARMOR = createArmorMaterial(
            12,
            new int[] {2, 4, 5, 2},
            9,
            SoundEvents.ARMOR_EQUIP_IRON,
            NO_INGREDIENT,
            "fire",
            0.1f,
            0.0F
    );
    public static final ArmorMaterial SCUBA = createArmorMaterial(
            10, 
            new int[] {0, 0, 0, 0},
            0,
            SoundEvents.ARMOR_EQUIP_GENERIC,
            NO_INGREDIENT,
            "scuba_goggles",
            0,
            0.0F
    );

    private static class AshenMask implements ArmorMaterial {
        @Override
        public int getDurabilityForSlot(EquipmentSlot slotIn) {
            return 10;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slotIn) {
            return slotIn == EquipmentSlot.HEAD ? 1 : 0;
        }

        @Override
        public int getEnchantmentValue() {
            return 15;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(TropicraftTags.Items.ASHEN_MASKS);
        }

        @Override
        public String getName() {
            return "mask";
        }

        @Override
        public float getToughness() {
            return 0;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    }

    private static class NigelStache implements ArmorMaterial {

        @Override
        public int getDurabilityForSlot(EquipmentSlot slotIn) {
            return 10;
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot slotIn) {
            return slotIn == EquipmentSlot.HEAD ? 1 : 0;
        }

        @Override
        public int getEnchantmentValue() {
            return 15;
        }

        @Override
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_LEATHER;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(TropicraftItems.NIGEL_STACHE.get());
        }

        @Override
        public String getName() {
            return "nigel";
        }

        @Override
        public float getToughness() {
            return 0;
        }

        @Override
        public float getKnockbackResistance() {
            return 0;
        }
    }

    public static ArmorMaterial createArmorMaterial(final int durability, final int[] dmgReduction, final int enchantability, final SoundEvent soundEvent,
                                                     final Ingredient repairMaterial, final String name, final float toughness, float knockbackResistance) {
        return new ArmorMaterial() {
            @Override
            public int getDurabilityForSlot(EquipmentSlot equipmentSlotType) {
                return durability;
            }

            @Override
            public int getDefenseForSlot(EquipmentSlot equipmentSlotType) {
                return dmgReduction[equipmentSlotType.getIndex()];
            }

            @Override
            public int getEnchantmentValue() {
                return enchantability;
            }

            @Override
            public SoundEvent getEquipSound() {
                return soundEvent;
            }

            @Override
            public Ingredient getRepairIngredient() {
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

            @Override
            public float getKnockbackResistance() {
                return knockbackResistance;
            }
        };
    }
}
