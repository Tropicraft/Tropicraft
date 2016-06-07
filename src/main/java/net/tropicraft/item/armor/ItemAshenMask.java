package net.tropicraft.item.armor;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.client.entity.render.RenderArmorMask;
import net.tropicraft.entity.placeable.EntityWallMask;
import net.tropicraft.entity.placeable.EntityWallShell;
import net.tropicraft.info.TCInfo;
import net.tropicraft.item.ItemTropicraftMulti;


public class ItemAshenMask extends ItemTropicraftArmor {

	@SideOnly(Side.CLIENT)
	private IIcon[] images;

	private final String[] displayNames;
	private final String[] imageNames;
	
	public ItemAshenMask(ArmorMaterial material, int renderIndex, int armorType, String[] displayNames, String[] imageNames) {
		super(material, renderIndex, armorType);
		this.imageNames = imageNames;
		this.displayNames = displayNames;
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
    
    @Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int damage) {
		// System.out.println("Twats");
		int j = MathHelper.clamp_int(damage, 0, 15);
		return this.images[j];
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	public void getSubItems(Item id, CreativeTabs creativeTabs, List list) {
		for (int meta = 0; meta < imageNames.length; meta++) {
			list.add(new ItemStack(id, 1, meta));
		}
	}

	/**
	 * Returns the unlocalized name of this item. This version accepts an
	 * ItemStack so different stacks can have different names based on their
	 * damage or NBT.
	 */
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		int i = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 15);
		return super.getUnlocalizedName() + "." + displayNames[i];
	}
	
	/**
	 * Registers all icons used in this item
	 * 
	 * @param iconRegistry
	 *            IconRegister instance used to register all icons for this item
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegistry) {
		images = new IIcon[displayNames.length];
	//	this.itemIcon = iconRegistry.registerIcon(ModInfo.ICONLOCATION + getImageName());
		
		for (int i = 0; i < displayNames.length; i++) {
			images[i] = iconRegistry.registerIcon(TCInfo.ICON_LOCATION + imageNames[i]);
		}
	}

	@Override
	public void damageArmor(EntityLivingBase player, ItemStack stack,
			DamageSource source, int damage, int slot) {

	}

	/**
	 * Returns a custom model for rendering the mask.
	 */
	@Override
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot){
		
		if (armorSlot == 0) // head
			return new RenderArmorMask (itemStack.getItemDamage());
		else
			return null;
	}

    
	/**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
	@Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player,
    		World world, int x, int y, int z,
    		int side, float local_px, float local_py, float local_pz)
    {		
        if (side == 0)
        {
            return false;
        }
        else if (side == 1)
        {
            return false;
        }
        else // It's a wall, place the mask on it.
        {        	
        	int type = itemStack.getItemDamage();
        	
            int direction = Direction.facingToDirection[side];

            // Must set the world coordinates here, or onValidSurface will be false.
            EntityHanging entityhanging = new EntityWallMask(world, x, y, z, direction, type);

            if (!player.canPlayerEdit(x, y, z, side, itemStack))
            {
                return false;
            }
            else
            {        		
                if (entityhanging != null && entityhanging.onValidSurface())
                {              		
                    if (!world.isRemote)
                    {                  		
                        world.spawnEntityInWorld(entityhanging);
                    }

                    --itemStack.stackSize;
                }
                
                return true;
            }
        }
    }
}
