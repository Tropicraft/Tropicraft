package net.tropicraft.block;

import java.util.List;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.CoralColors;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMineralSands extends BlockFalling {

    public BlockMineralSands() {
        super(Material.sand);
        this.setCreativeTab(TCCreativeTabRegistry.tabBlock);
    }
    
    @Override
    public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
        int metadata = world.getBlockMetadata(x, y, z);
        
        // If not black sands
        if (metadata != 2) return;
        
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            
            ItemStack stack = player.getEquipmentInSlot(1);
            
            if (stack == null)
                player.attackEntityFrom(DamageSource.lava, 0.5F);
        } else {
            entity.attackEntityFrom(DamageSource.lava, 0.5F);
        }
    }
    
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < TCNames.mineralSandNames.length; i++)
            list.add(new ItemStack(item, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
        int color = CoralColors.getColor(meta);
        
        // Don't colorize black
        return meta == 2 ? color : color | ColorizerGrass.getGrassColor(0.1, 1.0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        int metadata = world.getBlockMetadata(x, y, z);
        return getRenderColor(metadata);
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
