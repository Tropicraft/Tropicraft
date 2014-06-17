package net.tropicraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.tropicraft.info.TCInfo;
import net.tropicraft.info.TCNames;
import net.tropicraft.registry.TCCreativeTabRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDart extends ItemTropicraft {

    public static final String[] dartNames = new String[]{"paralyze", "poison", "moveSlowdown", "harm", "confusion", "hunger", "weakness"};
    private static final int[] colors = new int[]{0xFFB5FF, 0x258EA3, 0xCFD7D9, 0xDFE23A, 0x5137FB, 0xE12424, 0x6CC7FF};
    private static final String[] tooltipText = new String[]{"\u00a7d", "\u00a73", "\u00a77", "\u00a76", "\u00a71",
        "\u00a74", "\u00a79"};

    @SideOnly(Side.CLIENT)
    private IIcon overlay;

    @SideOnly(Side.CLIENT)
    private IIcon backImage;

    public ItemDart() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        setCreativeTab(TCCreativeTabRegistry.tabCombat);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List) {
        for (int i = 0; i < dartNames.length; ++i) {
            par3List.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
        return par2 > 0 ? this.overlay : getIconFromDamage(par1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        return colors[par1ItemStack.getItemDamage()];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer ent, List list, boolean wat) {
        list.clear();
        list.add(tooltipText[itemstack.getItemDamage()] + StatCollector.translateToLocal("dart.tropicraft:prefix.name") + " " + 
                StatCollector.translateToLocal("dart.tropicraft:" + dartNames[itemstack.getItemDamage()] + ".name"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        super.registerIcons(par1IconRegister);
        this.overlay = par1IconRegister.registerIcon(TCInfo.ICON_LOCATION + TCNames.dartOverlay);
        this.backImage = par1IconRegister.registerIcon(TCInfo.ICON_LOCATION + TCNames.dart);
    }
}
