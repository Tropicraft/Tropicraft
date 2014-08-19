package net.tropicraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.tropicraft.info.TCInfo;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.registry.TCItemRegistry;

public class ItemSnorkel extends ItemArmor {

    private boolean outOfWater = true;
    private boolean outOfWaterLast = true;

    /** Name of the armor, eg "scale" or "fire", used in getArmorTexture */
    private String modArmorName;

    public ItemSnorkel(ArmorMaterial material, int renderIndex, int armorType) {
        super(material, renderIndex, armorType);
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
        setMaxDamage(0);
        maxStackSize = 1;
        this.modArmorName = material.name();
    }

    /**
     * Called to tick armor in the armor slot. Override to do something
     *
     * @param world
     * @param player
     * @param itemStack
     */
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        ItemStack item3 = player.inventory.armorInventory[3];

        // snorkel stuff
        if (item3 != null && item3.getItem() != null && item3.getItem() == TCItemRegistry.snorkel) {
            outOfWater = !player.isInsideOfMaterial(Material.water) || !player.isInWater();

            if (outOfWaterLast && outOfWater) {
                player.setAir(300);
            }

            if (outOfWaterLast && !outOfWater) {
                player.setAir(1200);
                outOfWaterLast = false;
            }

            if (outOfWater) {
                outOfWaterLast = true;
            }
        } else
            if (player.getAir() > 300 && (item3 == null || item3.getItem() != TCItemRegistry.snorkel)) {
                player.setAir(300);
            } else {
                outOfWater = true;
                outOfWaterLast = true;
            }
    }

    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
        return this.itemIcon;
    }

    /**
     * @return The unlocalized item name
     */
    @Override
    public String getUnlocalizedName() {
        return String.format("item.%s%s", TCInfo.ICON_LOCATION, getActualName(super.getUnlocalizedName()));
    }

    /**
     * @param itemStack ItemStack instance of this item
     * @return The unlocalized item name
     */
    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s%s", TCInfo.ICON_LOCATION, getActualName(super.getUnlocalizedName()));
    }

    /**
     * Get the actual name of the block
     * @param unlocalizedName Unlocalized name of the block
     * @return Actual name of the block, without the "tile." prefix
     */
    protected String getActualName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    protected String getTexturePath(String name) {
        return TCInfo.ARMOR_LOCATION + name;
    }

    /**
     * Register all icons here
     * @param iconRegister Icon registry
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        return getTexturePath(String.format("%s_layer_" + (slot == 2 ? 2 : 1) + ".png", modArmorName));
    }
}