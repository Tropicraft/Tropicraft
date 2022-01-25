package net.tropicraft.core.common.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.tropicraft.Constants;

public class TropicraftArmorItem extends ArmorItem {
    public TropicraftArmorItem(ArmorMaterial armorMaterial, EquipmentSlot slotType, Properties properties) {
        super(armorMaterial, slotType, properties);
    }

    protected String getTexturePath(String name) {
        return Constants.ARMOR_LOCATION + name;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return getTexturePath(String.format("%s_layer_" + (slot == EquipmentSlot.LEGS ? 2 : 1) + ".png", getMaterial().getName()));
    }
}
