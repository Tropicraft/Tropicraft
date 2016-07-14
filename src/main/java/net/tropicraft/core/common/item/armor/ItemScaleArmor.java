package net.tropicraft.core.common.item.armor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class ItemScaleArmor extends ItemTropicraftArmor {

	public ItemScaleArmor(ArmorMaterial material, int renderIndex, EntityEquipmentSlot equipmentSlotIn) {
		super(material, renderIndex, equipmentSlotIn);
	}

	   /**
     * Called to tick armor in the armor slot. Override to do something
     *
     * @param world
     * @param player
     * @param itemStack
     */
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        
    }

}
