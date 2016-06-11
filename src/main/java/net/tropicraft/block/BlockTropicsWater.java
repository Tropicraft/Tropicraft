package net.tropicraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCFluidRegistry;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTropicsWater extends BlockFluidClassic {

	@SideOnly(Side.CLIENT)
	public static IIcon stillIcon, flowingIcon;
	
	public BlockTropicsWater(Fluid fluid, Material material) {
		super(fluid, material);
		this.lightOpacity = 0;
		this.setCreativeTab(null);
		this.displacements.put(TCBlockRegistry.coral, Boolean.valueOf(false));
		this.displacements.put(TCBlockRegistry.bambooFence, Boolean.valueOf(false));
		setRenderPass(1);
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		stillIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + TCNames.stillWater);
		flowingIcon = iconRegister.registerIcon(TCInfo.ICON_LOCATION + TCNames.flowingWater);
		TCFluidRegistry.tropicsWater.setIcons(stillIcon, flowingIcon);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side != 0 && side != 1 ? flowingIcon : stillIcon;
	}
	
    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        Material material = world.getBlock(x, y, z).getMaterial();
        return material == this.blockMaterial ? false : (side == 1 ? true : super.shouldSideBeRendered(world, x, y, z, side));
    }
    
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
    	/*
    	 * Fix so that tropics water can form infinite water sources again.
    	 * Turns blocks into source blocks if they are between two other source blocks.
    	 */
    	
    	int currentMeta = world.getBlockMetadata(x, y, z);
    	if (currentMeta > 0 &&
    			world.getBlock(x, y - 1,  z).getMaterial() != Material.air)
    	{
    		int neighbourSources = 0;
    		if (IsNeighbourSource (world, x + 1, y, z))
    			neighbourSources ++;
    		if (IsNeighbourSource (world, x - 1, y, z))
    			neighbourSources ++;
    		if (IsNeighbourSource (world, x, y, z + 1))
    			neighbourSources ++;
    		if (IsNeighbourSource (world, x, y, z - 1))
    			neighbourSources ++;
    		
    		if (neighbourSources >= 2)
                world.setBlock(x, y, z, this, 0, 3); // set meta to 0
    	}
    	
    	// Need to do this for the water to flow !!
    	super.updateTick(world, x, y, z, rand);
    }

	private boolean IsNeighbourSource(World world, int x, int y, int z)
	{
		if (world.getBlock(x, y, z) == this &&
				world.getBlockMetadata(x,  y, z) == 0)
			return true;
		
		return false;
	}
}
