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
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;
import net.tropicraft.registry.TCBlockRegistry;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockFruitLeaves extends BlockLeaves {

    public static final String[][] imageNames = new String[][] {{"leafGrapefruit", "leafLemon", "leafLime", "leafOrange"}, {"leafGrapefruitFast", "leafLemonFast", "leafLimeFast", "leafOrangeFast"}};
    public static final String[] treeNames = new String[] {"grapefruit", "lemon", "lime", "orange"};
    
    @SideOnly(Side.CLIENT)
    protected IIcon[][] icons;
    
    public BlockFruitLeaves() {
        super();
        this.disableStats();
        setCreativeTab(TCCreativeTabRegistry.tabBlock);
        
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
        	icons = new IIcon[2][];
        }
    }
    
    @Override
    public void dropBlockAsItemWithChance(World par1World, int x, int y, int z, int meta, float par6, int par7) {
        if (!par1World.isRemote) {
            if (par1World.rand.nextInt(3) == 0) {
                Item item = this.getItemDropped(meta, par1World.rand, par7);
                if(item == Item.getItemFromBlock(TCBlockRegistry.saplings)) {
                    System.out.println("heh" + this.damageDropped(meta));
                    super.dropBlockAsItem(par1World, x, y, z, new ItemStack(item, 1, this.damageDropped(meta) + 2));
                } else {
                    super.dropBlockAsItem(par1World, x, y, z, new ItemStack(item, 1, this.damageDropped(meta)));
                }
            }
        }
    }
    
    @Override
    public int damageDropped(int meta)
    {
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
    
    /* Override default leaf color so it doesn't color over our awesome fruits */
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int p_149741_1_) {
        return 0xffffff;
    }

    /* Override default leaf color so it doesn't color over our awesome fruits */
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess p_149720_1_, int p_149720_2_, int p_149720_3_, int p_149720_4_)
    {
        return 0xffffff;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
    }
    
    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(3) != 0 ? 0 : 1;
    }

    @Override
    public Item getItemDropped(int metadata, Random random, int j) {
        int treeType = metadata & 3;
        if (random.nextFloat() < 0.8F) {
            if (treeType == 0) {
                return TCItemRegistry.grapefruit;
            } else if (treeType == 1) {
                return TCItemRegistry.lemon;
            } else if (treeType == 2) {
                return TCItemRegistry.lime;
            } else {
                return TCItemRegistry.orange;
            }
        }
        return Item.getItemFromBlock(TCBlockRegistry.saplings);
    }

    @Override
    public IIcon getIcon(int var1, int meta) {
        return (meta & 3) == 1 ? this.icons[this.field_150127_b][1] : ((meta & 3) == 3 ? this.icons[this.field_150127_b][3] : ((meta & 3) == 2 ? this.icons[this.field_150127_b][2] : this.icons[this.field_150127_b][0]));
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
