package net.tropicraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.util.ColorHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTropicraftOre extends ItemTropicraftMulti {

    @SideOnly(Side.CLIENT)
    private IIcon[] unrefinedIcons;
    
    public ItemTropicraftOre(String[] names) {
        super(names);
        this.setCreativeTab(TCCreativeTabRegistry.tabMaterials);
    }
    
    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int i = MathHelper.clamp_int(stack.getItemDamage(), 0, names.length - 1);
        return super.getUnlocalizedName() + "_" + names[i];
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean par4) {
        int damage = itemstack.getItemDamage();
        
        if (damage == 5 || damage == 6) {
            float refinedPercentage = getTagCompound(itemstack).getFloat("AmtRefined");
            
            list.add(ColorHelper.color(3) + StatCollector.translateToLocal("gui.tropicraft:amtRefined") + ": " + 
            refinedPercentage + "%");
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        // Add original ores
        for (int i = 0; i < 5; i++) {
            list.add(new ItemStack(item, 1, i));
        }
        
        ItemStack u1 = new ItemStack(item, 1, 5);
        getTagCompound(u1).setFloat("AmtRefined", 0F);
        
        ItemStack u2 = new ItemStack(item, 1, 5);
        getTagCompound(u2).setFloat("AmtRefined", 33.333F);
        
        ItemStack u3 = new ItemStack(item, 1, 5);
        getTagCompound(u3).setFloat("AmtRefined", 66.667F);
        
        ItemStack refined = new ItemStack(item, 1, 6);
        getTagCompound(refined).setFloat("AmtRefined", 100.000F);
        
        // Add unrefined 1, 2, 3, then refined 'raftous' ore
        list.add(u1);
        list.add(u2);
        list.add(u3);
        list.add(refined);
        
        // Souzium
        list.add(new ItemStack(item, 1, 7));
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack) {
        int damage = stack.getItemDamage();
        
        float refinedAmt = getTagCompound(stack).getFloat("AmtRefined");
        
        // 33%
        if (refinedAmt > 32 && refinedAmt < 34) {
            return unrefinedIcons[0];
        }
        
        // 66%
        if (refinedAmt > 65 && refinedAmt < 67)
            return unrefinedIcons[1];
        
        return this.getIconFromDamage(damage);
    }
    
    /**
     * Returns which percentage of unrefined this itemstack is
     * @param stack
     * @return
     */
    private int getUnrefinedIndex(ItemStack stack) {
        float refinedAmt = getTagCompound(stack).getFloat("AmtRefined");
        
        // 33%
        if (refinedAmt > 32 && refinedAmt < 34)
            return 1;
        
        // 66%
        if (refinedAmt > 65 && refinedAmt < 67)
            return 2;
        
        return 0;
    }
    
    /**
     * Register all icons here
     * @param iconRegister Icon registry
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        icons = new IIcon[names.length];
        
        for (int i = 0 ; i < names.length ; i++) {
            icons[i] = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + names[i]);
        }
        
        unrefinedIcons = new IIcon[2];
        
        for (int i = 0; i < 2; i++) {
            unrefinedIcons[i] = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1) + "_" + names[5] + "" + (i + 1));
        }
    }
}
