package net.tropicraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockLeaves;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.tropicraft.info.TCInfo;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockRainforestLeaves extends BlockLeaves {

    public static final String[][] imageNames = new String[][] {{"leafKapok", "leafMahogany"}, {"leafKapok", "leafMahoganyFast"}};
    public static final String[] treeNames = new String[] {"kapok", "mahogany"};
    
    @SideOnly(Side.CLIENT)
    protected IIcon[][] icons;
    
    public BlockRainforestLeaves() {
        this.disableStats();
        setCreativeTab(TCCreativeTabRegistry.tabBlock);
        
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
        	icons = new IIcon[2][];
        }
    }
    
    @Override
    public int damageDropped(int meta) {
        return meta & 3;
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
    public boolean isOpaqueCube() {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_) {
        return 0xffffff;
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
    {
        return 0xffffff;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));    // Kapok
        list.add(new ItemStack(item, 1, 1));    // Mahogany
    }
    
    @Override
    public Item getItemDropped(int metadata, Random random, int j) {
        return Item.getItemFromBlock(TCBlockRegistry.saplings);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return (p_149691_2_ & 3) == 1 ? this.icons[this.field_150127_b][1] : this.icons[this.field_150127_b][0];
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        for (int i = 0; i < imageNames.length; ++i) {
            this.icons[i] = new IIcon[imageNames[i].length];

            for (int j = 0; j < imageNames[i].length; ++j) {
                this.icons[i][j] = iconRegister.registerIcon(TCInfo.ICON_LOCATION + imageNames[i][j]);
            }
        }
    }

    @Override
    public String[] func_150125_e() {
        return this.treeNames;
    }
}
