package net.tropicraft.item;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.ItemFluidContainer;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.registry.TCFluidRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTropicsWaterBucket extends ItemFluidContainer {

	public ItemTropicsWaterBucket() {
		super(0, FluidContainerRegistry.BUCKET_VOLUME);
		this.maxStackSize = 1;
		this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
		ItemStack fluid = new ItemStack(item);

		this.fill(fluid, new FluidStack(TCFluidRegistry.tropicsWater, FluidContainerRegistry.BUCKET_VOLUME), true);
		list.add(fluid);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, false);

		if (movingobjectposition != null) {
			ForgeDirection direction = ForgeDirection.getOrientation(movingobjectposition.sideHit);
            int i = movingobjectposition.blockX;
            int j = movingobjectposition.blockY;
            int k = movingobjectposition.blockZ;
            
        	TileEntity tile = world.getTileEntity(i, j, k);
        	
        	if(tile != null && tile instanceof IFluidHandler) {
        		IFluidHandler tank = (IFluidHandler)tile;
        		if(tank.fill(direction,this.getFluid(itemStack), false) == this.getCapacity(itemStack)) {
        			tank.fill(direction,this.getFluid(itemStack), true);
        			if(!player.capabilities.isCreativeMode)
        				return new ItemStack(Items.bucket);
        		}
        		return itemStack;
        	}

            if (movingobjectposition.sideHit == 0) {
                --j;
            }

            if (movingobjectposition.sideHit == 1) {
                ++j;
            }

            if (movingobjectposition.sideHit == 2) {
                --k;
            }

            if (movingobjectposition.sideHit == 3) {
                ++k;
            }

            if (movingobjectposition.sideHit == 4) {
                --i;
            }

            if (movingobjectposition.sideHit == 5) {
                ++i;
            }

            if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, itemStack)) {
                return itemStack;
            }

            if (this.tryPlaceContainedLiquid(itemStack, world, i, j, k) && !player.capabilities.isCreativeMode) {
                return new ItemStack(Items.bucket);
            }
		}

		return itemStack;
	}

    public boolean tryPlaceContainedLiquid(ItemStack itemStack, World world, int x, int y, int z) {
    	FluidStack fluid = this.getFluid(itemStack);
    	
        if (fluid == null || fluid.amount == 0) {
            return false;
        }
        else {
            Material material = world.getBlock(x, y, z).getMaterial();
            boolean isSolid = material.isSolid();
            
            if (!world.isAirBlock(x, y, z) && isSolid) {
                return false;
            }
            else {
            	if (!world.isRemote && !isSolid && !material.isLiquid()) {
            		world.func_147480_a(x, y, z, true);
            	}
            	
            	world.setBlock(x, y, z, fluid.getFluid().getBlock(), 0, 3);
            }

            return true;
        }
    }
	
	@Override
    public IIcon getIcon(ItemStack itemStack, int renderPass) {
        FluidStack fluid = this.getFluid(itemStack);
        
        if (fluid != null && fluid.amount != 0)  {
        	if (this.itemIcon != null) 
        		return this.itemIcon;
        }
        
        return Items.bucket.getIconFromDamage(0);
    }
	
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + TCNames.bucketTropicsWater);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
	public String getItemStackDisplayName(ItemStack itemStack) {
    	FluidStack fluid = this.getFluid(itemStack);
    	
    	if (fluid != null && fluid.amount != 0) {
    		return StatCollector.translateToLocal(fluid.getFluid().getUnlocalizedName().replace("fluid.", String.format("item.%s:", TCInfo.MODID)).split(":")[0] + ":" 
    				+ TCNames.bucketTropicsWater + ".name");
    	}
        
        return Items.bucket.getUnlocalizedName() + ".name";
    }

}
