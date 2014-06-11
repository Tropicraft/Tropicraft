package net.tropicraft.item.scuba;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.tropicraft.item.ItemTropicraft;
import net.tropicraft.item.scuba.ItemScubaGear.AirType;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.ColorHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemScubaTank extends ItemTropicraft {

	public ItemScubaTank() {
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.maxStackSize = 1;
		this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
	}

	/**
	 * Called when item is crafted/smelted. Used only by maps so far.
	 */
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {

	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {
		AirType airType = itemstack.getItemDamage() == 1 ? AirType.TRIMIX : AirType.REGULAR;
		String airRemaining = getTagCompound(itemstack).getInteger("AirContained") + " psi";

		list.add(ColorHelper.color(9) + StatCollector.translateToLocal("gui.tropicraft:airType") + ": " + ColorHelper.color(7) + airType.getDisplayName());
		list.add(ColorHelper.color(9) + StatCollector.translateToLocal("gui.tropicraft:maxAirCapacity") + ": " + ColorHelper.color(7) + airType.getMaxCapacity() + " psi");
		list.add(ColorHelper.color(9) + StatCollector.translateToLocal("gui.tropicraft:airRemaining") + ": " + ColorHelper.color(7) + airRemaining);
		list.add(ColorHelper.color(9) + String.format("%s: %s%.3f psi/sec", StatCollector.translateToLocal("gui.tropicraft:useEfficiency"),  ColorHelper.color(7), (airType.getUsageRate() * 20)));
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack singleTankRegular = new ItemStack(item, 1, 0);
		getTagCompound(singleTankRegular).setInteger("AirContained", ItemScubaGear.AirType.REGULAR.getMaxCapacity());
		list.add(singleTankRegular);

		ItemStack singleTankTrimix = new ItemStack(item, 1, 1);
		getTagCompound(singleTankTrimix).setInteger("AirContained", ItemScubaGear.AirType.TRIMIX.getMaxCapacity());
		list.add(singleTankTrimix);
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
