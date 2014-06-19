package net.tropicraft.block;

import java.util.Random;

import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPalmLeaves extends BlockLeaves {

    public BlockPalmLeaves() {
        this.disableStats();
        setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderColor(int i) {
        return 0x48b518;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
        return 0x48b518;
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
    
    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(20) != 0 ? 0 : 1;
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int j) {
        return Item.getItemFromBlock(TCBlockRegistry.saplings);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int var1, int var2) {
        return this.blockIcon;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        this.blockIcon = register.registerIcon(TCInfo.ICON_LOCATION + TCNames.palmLeaves);
    }

    @Override
    public String[] func_150125_e() {
        return new String[] {"palm"};
    }

}
