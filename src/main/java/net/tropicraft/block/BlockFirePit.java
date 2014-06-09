package net.tropicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityFirePit;
import net.tropicraft.block.tileentity.TileEntityTropicraftFlowerPot;
import net.tropicraft.info.TCNames;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFirePit extends BlockTropicraft implements ITileEntityProvider {

	public BlockFirePit() {
		super(Material.circuits);
		this.setBlockTextureName(TCNames.firePit);
		this.setBlockBoundsForItemRender();
	}
	
    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender() {
    	setBlockBounds(0.05F, 0F, 0.05F, 0.95F, 0.1F, 0.95F);
    }
    
    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube() {
        return false;
    }
    
    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock() {
        return false;
    }

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityFirePit();
	}

}
