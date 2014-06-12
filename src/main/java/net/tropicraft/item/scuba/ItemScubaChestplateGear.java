package net.tropicraft.item.scuba;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.tropicraft.util.ColorHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemScubaChestplateGear extends ItemScubaGear {

	public ItemScubaChestplateGear(ArmorMaterial material, ScubaMaterial scubaMaterial, int renderIndex, int armorType) {
		super(material, scubaMaterial, renderIndex, armorType);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {
		AirType airType = itemstack.getItemDamage() >= 2 ? AirType.TRIMIX : AirType.REGULAR;
		String airRemaining = getTagCompound(itemstack).getFloat("AirContained") + " psi";
		String numTanks = String.valueOf(itemstack.getItemDamage() % 2 != 0 ? 2 : 1);
		String suitType = this.scubaMaterial.getDisplayName();

		list.add(ColorHelper.color(9) + StatCollector.translateToLocal("gui.tropicraft:suitType") + ": " + ColorHelper.color(7) + suitType);
		list.add(ColorHelper.color(9) + StatCollector.translateToLocal("gui.tropicraft:airType") + ": " + ColorHelper.color(7) + airType.getDisplayName());
		list.add(ColorHelper.color(9) + StatCollector.translateToLocal("gui.tropicraft:numTanks") + ": " + ColorHelper.color(7) + numTanks);
		list.add(ColorHelper.color(9) + StatCollector.translateToLocal("gui.tropicraft:maxAirCapacity") + ": " + ColorHelper.color(7) + airType.getMaxCapacity() + " psi");
		list.add(ColorHelper.color(9) + StatCollector.translateToLocal("gui.tropicraft:airRemaining") + ": " + ColorHelper.color(7) + airRemaining);
		list.add(ColorHelper.color(9) + String.format("%s: %s%.3f psi/sec", StatCollector.translateToLocal("gui.tropicraft:useEfficiency"),  ColorHelper.color(7), (airType.getUsageRate() * 20)));
	}
	
	/**
	 * Gets the type of air this gear uses
	 * @param itemstack An ItemStack containing this item
	 * @return
	 */
	public AirType getAirType(ItemStack itemstack) {
		return itemstack.getItemDamage() >= 2 ? AirType.TRIMIX : AirType.REGULAR;
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		ItemStack singleTankRegular = new ItemStack(item, 1, 0);
		getTagCompound(singleTankRegular).setFloat("AirContained", ItemScubaGear.AirType.REGULAR.getMaxCapacity());
		list.add(singleTankRegular);

		ItemStack doubleTankRegular = new ItemStack(item, 1, 1);
		getTagCompound(doubleTankRegular).setFloat("AirContained", ItemScubaGear.AirType.REGULAR.getMaxCapacity() * 2);
		list.add(doubleTankRegular);

		ItemStack singleTankTrimix = new ItemStack(item, 1, 2);
		getTagCompound(singleTankTrimix).setFloat("AirContained", ItemScubaGear.AirType.TRIMIX.getMaxCapacity());
		list.add(singleTankTrimix);

		ItemStack doubleTankTrimix = new ItemStack(item, 1, 3);
		getTagCompound(doubleTankTrimix).setFloat("AirContained", ItemScubaGear.AirType.TRIMIX.getMaxCapacity() * 2);
		list.add(doubleTankTrimix);
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase player,
			ItemStack armor, DamageSource source, double damage, int slot) {
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
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemstack) {

	}

}
