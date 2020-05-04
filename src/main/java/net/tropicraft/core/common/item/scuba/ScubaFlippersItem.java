package net.tropicraft.core.common.item.scuba;

import java.util.UUID;

import com.google.common.collect.Multimap;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.tropicraft.Constants;

public class ScubaFlippersItem extends ScubaArmorItem {
    
    private static final AttributeModifier SWIM_SPEED_BOOST = new AttributeModifier(UUID.fromString("d0b3c58b-ff33-41f2-beaa-3ffa15e8342b"), Constants.MODID + ".scuba", 0.25, Operation.MULTIPLY_TOTAL);

    public ScubaFlippersItem(ScubaType type, Properties properties) {
        super(type, EquipmentSlotType.FEET, properties);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<String, AttributeModifier> mods = super.getAttributeModifiers(slot, stack);
        if (slot == EquipmentSlotType.FEET && EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack) == 0) {
            mods.put(LivingEntity.SWIM_SPEED.getName(), SWIM_SPEED_BOOST);
        }
        return mods;
    }
}
