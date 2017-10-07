package net.tropicraft.core.common.item.scuba;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemScubaFlippers extends ItemScubaGear {

	public ItemScubaFlippers(ArmorMaterial material, ScubaMaterial scubaMaterial, int renderIndex, EntityEquipmentSlot slot) {
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
		if (player.isInWater() || isFullyUnderwater(world, player)) {
			player.capabilities.isFlying = true;
			player.setAIMoveSpeed(player.capabilities.getWalkSpeed());
			player.moveRelative(1E-4F, 1E-4F, 0.00000000001f);
			player.motionX /= 1.06999999;
			player.motionZ /= 1.06999999;
			player.moveEntityWithHeading(-1E-4F, -1E-4F);
		} else {
			player.setAIMoveSpeed((float) (player.getAIMoveSpeed() / 1.33333));
			player.capabilities.isFlying = false;
		}

	}

	private boolean isFullyUnderwater(World world, EntityPlayer player) {
		int x = MathHelper.ceil(player.posX);
		int y = MathHelper.ceil(player.posY + player.height - 0.5F);
		int z = MathHelper.ceil(player.posZ);
		
		BlockPos pos = new BlockPos(x, y, z);

		return world.getBlockState(pos).getMaterial().isLiquid();
	}

}
