package net.tropicraft.block;

import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.tropicraft.block.tileentity.TileEntityBambooChest;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCRenderIDs;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBambooChest extends BlockChest {

    public BlockBambooChest() {
        super(0);
        this.disableStats();
        setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }
    
    @Override
    public int getRenderType() {
        return TCRenderIDs.bambooChest;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityBambooChest();
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
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int i, int j, int k) {
        TileEntityBambooChest tile = (TileEntityBambooChest) world.getTileEntity(i, j, k);
        if (tile != null && tile.isUnbreakable()) {
            return 0.0F;
        }
        return super.getPlayerRelativeBlockHardness(player, world, i, j, k);
    }

    /**
     * @return The unlocalized block name
     */
    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", TCInfo.ICON_LOCATION, getActualName(super.getUnlocalizedName()));
    }
    
    /**
     * Get the true name of the block
     * @param unlocalizedName tile.%truename%
     * @return The actual name of the block, rather than tile.%truename%
     */
    protected String getActualName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf('.') + 1);
    }
    
    /**
     * Register all the icons of the block
     * @param iconRegister Icon registry
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(String.format("%s", getActualName(this.getUnlocalizedName())));
    }
}
