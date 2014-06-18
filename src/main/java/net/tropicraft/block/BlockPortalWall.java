package net.tropicraft.block;

import net.minecraft.block.BlockSandStone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPortalWall extends BlockSandStone {

    public BlockPortalWall() {
        this.setBlockUnbreakable();
        this.setResistance(6000000.0F);
        this.setCreativeTab(null);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return Blocks.sandstone.getIcon(p_149691_1_, p_149691_2_);
    }
    
    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int doop, float doo, float doe, float doa) {
        if (par5EntityPlayer.getCurrentEquippedItem() != null &&
                par5EntityPlayer.getCurrentEquippedItem().getItem() instanceof ItemBucket) {
            return true;
        }

        return false;
    }

}
