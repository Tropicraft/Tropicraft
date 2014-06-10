package net.tropicraft.item.scuba;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemScubaHelmet extends ItemScubaGear {

	public ItemScubaHelmet(ArmorMaterial material, ScubaMaterial scubaMaterial, int renderIndex, int armorType) {
		super(material, scubaMaterial, renderIndex, armorType);
		
	}

	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		//TODO client side only: <Corosus> spawn some bubbles near them once in a while to simulating using the rebreather
		
		ItemStack chestplate = player.getEquipmentInSlot(3);
		
		if (chestplate != null && chestplate.getItem() instanceof ItemScubaChestplateGear) {
			getTagCompound(chestplate).getInteger("AirRemaining");
		}
	}
	
	/**
	 * Retrives an existing nbt tag compound or creates a new one if it is null
	 * @param stack
	 * @return
	 */
	public NBTTagCompound getTagCompound(ItemStack stack) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		
		return stack.getTagCompound();
	}
	
    /**
     * Called when the client starts rendering the HUD, for whatever item the player currently has as a helmet. 
     * This is where pumpkins would render there overlay.
     * 
     * @param stack The ItemStack that is equipped
     * @param player Reference to the current client entity
     * @param resolution Resolution information about the current viewport and configured GUI Scale
     * @param partialTicks Partial ticks for the renderer, useful for interpolation
     * @param hasScreen If the player has a screen up, which will be rendered after this.
     * @param mouseX Mouse's X position on screen
     * @param mouseY Mouse's Y position on screen
     */
    @SideOnly(Side.CLIENT)
    public void renderHelmetOverlay(ItemStack stack, EntityPlayer player, ScaledResolution resolution, float partialTicks, boolean hasScreen, int mouseX, int mouseY) {
    	// Check to see if player inventory contains bcd
    	
    	// TODO http://www.dansdiveshop.ca/dstore/images/cobalt.jpg
    	// TODO: only update most of these vars every second, for performance purposes
    	short currentDepth, maxDepthReached;
    	
    	int airRemaining, timeRemaining;
    	
    	float airTemp;
    	
    	
		ItemStack chestplate = player.getEquipmentInSlot(3);
		
		if (chestplate != null && chestplate.getItem() instanceof ItemScubaChestplateGear) {
			airRemaining = getTagCompound(chestplate).getInteger("AirRemaining");
		}
		
		airTemp = player.worldObj.getBiomeGenForCoords(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posZ)).temperature;
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

}
