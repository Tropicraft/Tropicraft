package net.tropicraft.core.common.item.scuba;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class ItemScubaLeggings extends ItemScubaGear {

	public ItemScubaLeggings(ArmorMaterial material, ScubaMaterial scubaMaterial, int renderIndex, EntityEquipmentSlot slot) {
		super(material, scubaMaterial, renderIndex, slot);
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
		return null;
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {

	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		
	}

}
