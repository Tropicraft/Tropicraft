package net.tropicraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBundle extends BlockTropicraft {

	/** Icon associated with the top & bottom of the block */
	private IIcon end;
	
	/** Icon for the side of the block */
	private IIcon side;
	
	public BlockBundle(String name) {
		super(Material.plants); // closest to thatch
		this.setBlockTextureName(name);
		this.setBlockName(name);
	}
	
    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
	@Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        int j1 = metadata & 1;
        byte b0 = 0;

        switch (side) {
            case 0:
            case 1:
                b0 = 0;
                break;
            case 2:
            case 3:
                b0 = 8;
                break;
            case 4:
            case 5:
                b0 = 4;
        }

        return j1 | b0;
    }
	
    /**
     * Gets the block's texture.
     * @param b_side Side of the block
     * @param meta Metadata value of the block
     */
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int b_side, int meta) {
        return b_side == 0 || b_side == 1 ? end : side;
    }
    
	/**
	 * @param iconRegister Icon registry
	 */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.side = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + "Side");
        this.end = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + "End");
        this.blockIcon = this.side;
    }
}
