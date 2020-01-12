package net.tropicraft.core.common.item;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.tropicraft.Constants;

public class ScaleArmorItem extends TropicraftArmorItem {
    public ScaleArmorItem(EquipmentSlotType slotType, Properties properties) {
        super(ArmorMaterials.SCALE_ARMOR, slotType, properties);
    }

    // TODO waiting on forge???
//    @Override
//    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
//        if (source == DamageSource.IN_FIRE || source == DamageSource.LAVA) {
//            // Invincible to fire damage
//            return new ArmorProperties(10, 1.0, Integer.MAX_VALUE);
//        } else {
//            return super.getProperties(player, armor, source, damage, slot);
//        }
//    }
}
