package net.tropicraft.core.common.item.scuba;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.item.ItemTropicraft;
import net.tropicraft.core.common.item.scuba.api.AirTypeRegistry;
import net.tropicraft.core.common.item.scuba.api.IAirType;
import net.tropicraft.core.common.item.scuba.api.IAirType.AirType;
import net.tropicraft.core.common.item.scuba.api.IScubaTank;

public class ItemScubaTank extends ItemTropicraft {

	public ItemScubaTank() {
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.maxStackSize = 1;
	}

	/**
	 * Called when item is crafted/smelted. Used only by maps so far.
	 */
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {

	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return ScubaCapabilities.getProvider(ScubaCapabilities.getTankCapability(), () -> {
            IScubaTank ret = new IScubaTank.ScubaTank() {

                @Override
                public void setPressure(float pressure) {
                    super.setPressure(pressure);
                    stack.setTagCompound(serializeNBT());
                }

                @Override
                public void setAirType(IAirType type) {
                    super.setAirType(type);
                    stack.setTagCompound(serializeNBT());
                }
            };
            if (stack.hasTagCompound()) {
                ret.deserializeNBT(stack.getTagCompound());
            }
            return ret;
        });
    }

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List<String> list, boolean par4) {
	    IScubaTank cap = itemstack.getCapability(ScubaCapabilities.getTankCapability(), null);
		IAirType airType = cap.getAirType();

		list.add(TextFormatting.BLUE + I18n.format("tropicraft.gui.air.type", TextFormatting.GRAY + airType.getDisplayName()));
		list.add(TextFormatting.BLUE + I18n.format("tropicraft.gui.air.capacity", TextFormatting.GRAY.toString() + airType.getMaxCapacity()));
		list.add(TextFormatting.BLUE + I18n.format("tropicraft.gui.air.remaining", TextFormatting.GRAY.toString() + cap.getPressure()));
		list.add(TextFormatting.BLUE + I18n.format("tropicraft.gui.air.efficiency", TextFormatting.GRAY, (airType.getUsageRate() * 20)));
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
	    ItemStack stack = new ItemStack(item);
	    IScubaTank tank = stack.getCapability(ScubaCapabilities.getTankCapability(), null);
	    
	    for (IAirType airType : AirTypeRegistry.INSTANCE.getTypes()) {
	        tank.setPressure(0);
	        tank.setAirType(airType);
	        list.add(stack.copy());
	        
	        tank.setPressure(airType.getMaxCapacity());
	        list.add(stack.copy());
	    }
	}

	/**
	 * Retrives an existing nbt tag compound or creates a new one if it is null
	 * @param stack
	 */
	public NBTTagCompound getTagCompound(ItemStack stack) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		return stack.getTagCompound();
	}

}
