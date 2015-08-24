package net.tropicraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCFluidRegistry;
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
}
