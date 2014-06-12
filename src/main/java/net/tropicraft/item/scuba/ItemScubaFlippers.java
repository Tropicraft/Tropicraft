package net.tropicraft.item.scuba;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemScubaFlippers extends ItemScubaGear {

	public ItemScubaFlippers(ArmorMaterial material,
			ScubaMaterial scubaMaterial, int renderIndex, int armorType) {
		super(material, scubaMaterial, renderIndex, armorType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player,
			ItemStack armor, DamageSource source, double damage, int slot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack,
			DamageSource source, int damage, int slot) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (player.isInWater() || isFullyUnderwater(world, player)) {
			player.capabilities.isFlying = true;
			player.setAIMoveSpeed(player.capabilities.getWalkSpeed());
			player.moveFlying(1E-4F, 1E-4F, 0.00000000001f);
			player.motionX /= 1.06999999;
			player.motionZ /= 1.06999999;
			player.moveEntityWithHeading(-1E-4F, -1E-4F);
		} else {
			player.setAIMoveSpeed((float) (player.getAIMoveSpeed() / 1.33333));
			player.capabilities.isFlying = false;
		}

	}
	
	private boolean isFullyUnderwater(World world, EntityPlayer player) {
		int x = MathHelper.ceiling_double_int(player.posX);
		int y = MathHelper.ceiling_double_int(player.posY + player.height);
		int z = MathHelper.ceiling_double_int(player.posZ);
		
		return world.getBlock(x, y, z).getMaterial().isLiquid();
	}

}
