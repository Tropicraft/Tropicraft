package net.tropicraft.core.common.item.armor;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;


public class ItemScaleArmor extends ItemTropicraftArmor {

	public ItemScaleArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot equipmentSlotIn) {
		super(material, renderIndex, equipmentSlotIn);
	}

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        if (source == DamageSource.IN_FIRE || source == DamageSource.LAVA) {
            // Invincible to fire damage
            return new ArmorProperties(10, 1.0, Integer.MAX_VALUE);
        } else {
            return super.getProperties(player, armor, source, damage, slot);
        }
    }

}
