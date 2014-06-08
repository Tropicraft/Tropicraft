package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class BlockCoconut extends BlockTropicraft {

	public BlockCoconut() {
		super(Material.gourd);
		
		float f = 0.225F;

        setBlockBounds(0.5F - f, f, 0.5F - f, 0.5F + f, 1.0F - f, 0.5F + f);
        this.setCreativeTab(TCCreativeTabRegistry.tabFood);
	}
	
	 /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
	@Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public int getRenderType() {
        return 1;
    }
    
    /** NOTE: Behavior overridden in TCBlockEvents */
	@Override
	public Item getItemDropped(int meta, Random rand, int unused) {
		return null;
	}

}
