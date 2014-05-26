package net.tropicraft.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.tropicraft.registry.TCBlockRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCoffeeBean extends ItemTropicraft implements IPlantable {

	@SideOnly(Side.CLIENT)
    private IIcon[] icons;
	
	public ItemCoffeeBean() {
		setHasSubtypes(true);
        setMaxDamage(0);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 1));
        list.add(new ItemStack(this, 1, 2));
    }
	
	@SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegistry) {
        this.icons = new IIcon[] {
            iconRegistry.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_Raw"),
            iconRegistry.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_Roasted"),
            iconRegistry.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_Berry")
        };
    }
	
	@SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int dmg) {
        dmg = dmg & 3; // last two bits
        
        return this.icons[dmg];
    }

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
		return EnumPlantType.Crop;
	}

	@Override
	public Block getPlant(IBlockAccess world, int x, int y, int z) {
		return TCBlockRegistry.coffeePlant;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
		return 0;
	}
	
	@Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float offsetX, float offsetY, float offsetZ) {
        if (stack.getItemDamage() != 0) { // 0 = raw, 1 = roasted, 2 = cherry
            return false;
        }

        if (side != 1) {
            return false;
        }

        if (!player.canPlayerEdit(x, y, z, side, stack) || !player.canPlayerEdit(x, y + 1, z, side, stack)) {
            return false;
        }

        if (world.getBlock(x, y, z) != Blocks.farmland) {
            return false;
        }

        if (!Blocks.farmland.canSustainPlant(world, x, y, z, ForgeDirection.UP, this)) {
            return false;
        }

        if (!world.isAirBlock(x, y+1, z)) {
            return false;
        }

        world.setBlock(x, y + 1, z, TCBlockRegistry.coffeePlant, 0, 2);
        --stack.stackSize;
        return true;
    }

}
