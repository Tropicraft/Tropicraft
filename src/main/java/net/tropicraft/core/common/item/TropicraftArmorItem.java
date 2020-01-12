package net.tropicraft.core.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.tropicraft.Constants;

public class TropicraftArmorItem extends ArmorItem {
    public TropicraftArmorItem(IArmorMaterial armorMaterial, EquipmentSlotType slotType, Properties properties) {
        super(armorMaterial, slotType, properties);
    }

    protected String getTexturePath(String name) {
        return Constants.ARMOR_LOCATION + name;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return getTexturePath(String.format("%s_layer_" + (slot == EquipmentSlotType.LEGS ? 2 : 1) + ".png", getArmorMaterial().getName()));
    }
}
