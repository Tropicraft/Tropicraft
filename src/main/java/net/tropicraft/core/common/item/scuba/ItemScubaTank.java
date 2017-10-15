package net.tropicraft.core.common.item.scuba;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.ColorHelper;
import net.tropicraft.core.common.item.ItemTropicraft;
import net.tropicraft.core.common.item.scuba.ItemScubaGear.AirType;

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

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {
		AirType airType = itemstack.getItemDamage() == 1 ? AirType.TRIMIX : AirType.REGULAR;
		String airRemaining = getTagCompound(itemstack).getInteger("AirContained") + " psi";

		list.add(ColorHelper.color(9) + I18n.translateToLocal("gui.tropicraft:airType") + ": " + ColorHelper.color(7) + airType.getDisplayName());
		list.add(ColorHelper.color(9) + I18n.translateToLocal("gui.tropicraft:maxAirCapacity") + ": " + ColorHelper.color(7) + airType.getMaxCapacity() + " psi");
		list.add(ColorHelper.color(9) + I18n.translateToLocal("gui.tropicraft:airRemaining") + ": " + ColorHelper.color(7) + airRemaining);
		list.add(ColorHelper.color(9) + String.format("%s: %s%.3f psi/sec", I18n.translateToLocal("gui.tropicraft:useEfficiency"),  ColorHelper.color(7), (airType.getUsageRate() * 20)));
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack singleTankRegular = new ItemStack(item, 1, 0);
		getTagCompound(singleTankRegular).setInteger("AirContained", ItemScubaGear.AirType.REGULAR.getMaxCapacity());
		list.add(singleTankRegular);

		ItemStack singleTankTrimix = new ItemStack(item, 1, 1);
		getTagCompound(singleTankTrimix).setInteger("AirContained", ItemScubaGear.AirType.TRIMIX.getMaxCapacity());
		list.add(singleTankTrimix);

		ItemStack singleTankTrimix2 = new ItemStack(item, 1, 2);
		getTagCompound(singleTankTrimix2).setInteger("AirContained", 0);
		list.add(singleTankTrimix2);
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
