package net.tropicraft.item.armor;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.tropicraft.info.TCInfo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTropicraftArmor extends ItemArmor implements ISpecialArmor {

	/** Name of the armor, eg "scale" or "fire", used in getArmorTexture */
	private String modArmorName;
	
	@SideOnly(Side.CLIENT)
    public static List[] fxLayers;
	
	public ItemTropicraftArmor(ArmorMaterial material, int renderIndex, int armorType) {
		super(material, renderIndex, armorType);
		this.modArmorName = material.name();
	}

	/**
	 * @return The unlocalized item name
	 */
	@Override
	public String getUnlocalizedName() {
		return String.format("item.%s%s", TCInfo.ICON_LOCATION, getActualName(super.getUnlocalizedName()));
	}

	/**
	 * @param itemStack ItemStack instance of this item
	 * @return The unlocalized item name
	 */
	@Override
	public String getUnlocalizedName(ItemStack itemStack) {
		return String.format("item.%s%s", TCInfo.ICON_LOCATION, getActualName(super.getUnlocalizedName()));
	}

	/**
	 * Get the actual name of the block
	 * @param unlocalizedName Unlocalized name of the block
	 * @return Actual name of the block, without the "tile." prefix
	 */
	protected String getActualName(String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
	
	protected String getTexturePath(String name) {
		return TCInfo.ARMOR_LOCATION + name;
	}

	/**
	 * Register all icons here
	 * @param iconRegister Icon registry
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1));
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return getTexturePath(String.format("%s_layer_" + (slot == 2 ? 2 : 1) + ".png", modArmorName));
	}

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        return new ArmorProperties(10, source == DamageSource.inFire ? 1.0 : 0.3, Integer.MAX_VALUE);
    }

    /**
     * Get the displayed effective armor.
     *
     * @param player The player wearing the armor.
     * @param armor The ItemStack of the armor item itself.
     * @param slot The armor slot the item is in.
     * @return The number of armor points for display, 2 per shield.
     */
    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 3;
    }

    /**
     * Applies damage to the ItemStack. The mod is responsible for reducing the
     * item durability and stack size. If the stack is depleted it will be cleaned
     * up automatically.
     *
     * @param entity The entity wearing the armor
     * @param armor The ItemStack of the armor item itself.
     * @param source The source of the damage, which can be used to alter armor
     *     properties based on the type or source of damage.
     * @param damage The amount of damage being applied to the armor
     * @param slot The armor slot the item is in.
     */
    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        stack.damageItem(damage, entity);        
    }

}
