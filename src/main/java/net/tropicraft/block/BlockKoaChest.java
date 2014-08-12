package net.tropicraft.block;

import net.minecraft.block.BlockChest;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityKoaChest;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockKoaChest extends BlockChest implements ITileEntityProvider {

    public BlockKoaChest() {
        super(0);
        this.disableStats();
        setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }
    
    @Override
    public int getRenderType() {
    	return -1/*ModRenderIds.koaChestModelId*/;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityKoaChest();
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        super.onBlockAdded(world, i, j, k);
    }

    @Override
    public IIcon getIcon(int i, int j) {
        return this.blockIcon;
    }
  
    /**
     * Gets the hardness of block at the given coordinates in the given world, relative to the ability of the given
     * EntityPlayer.
     */
    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int i, int j, int k)
    {
        TileEntityKoaChest tile = (TileEntityKoaChest) world.getTileEntity(i, j, k);
        if (tile != null && tile.isUnbreakable()) {
            return 0.0F;
        }
        return super.getPlayerRelativeBlockHardness(player, world, i, j, k);
    }
    
    /*@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon(ModInfo.ICONLOCATION + "bamboochest");
    }*/
}
