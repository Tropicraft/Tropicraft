package net.tropicraft.item;

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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemFlippers extends ItemArmor {

    private boolean hasFlippers = false;
    
    /** Name of the armor, eg "scale" or "fire", used in getArmorTexture */
    private String modArmorName;

    public ItemFlippers(ArmorMaterial material, int renderIndex, int armorType) {
        super(material, renderIndex, armorType);
        this.setCreativeTab(TCCreativeTabRegistry.tabMisc);
        setMaxDamage(0);
        maxStackSize = 1;
        this.modArmorName = material.name();
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
     * Called to tick armor in the armor slot. Override to do something
     *
     * @param world
     * @param player
     * @param itemStack
     */
    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        ItemStack item0 = player.inventory.armorInventory[0];

        // flippers stuff
        if (item0 != null && item0.getItem() != null && item0.getItem() == TCItemRegistry.flippers) {
            if (player.isInsideOfMaterial(Material.water)) {
                player.capabilities.isFlying = true;
                player.setAIMoveSpeed(player.capabilities.getWalkSpeed());
                if (item0.isItemEnchanted()) {
                    player.moveFlying(1E-4F, 1E-4F, 0.00000000001f);
                    player.motionX /= 1.06999999;
                    player.motionZ /= 1.06999999;
                    player.moveEntityWithHeading(-1E-4F, -1E-4F);
                } else {
                    player.moveFlying(1E-4F, 1E-4F, 0.00000000001f);
                    player.motionX /= 1.26999999;
                    player.motionZ /= 1.26999999;
                    player.moveEntityWithHeading(-1E-4F, -1E-4F);
                }

                player.moveEntityWithHeading(-1E-4F, -1E-4F);
            } else {
                player.setAIMoveSpeed((float) (player.getAIMoveSpeed() / 1.33333));
                player.capabilities.isFlying = false;
            }

            hasFlippers = true;
        }

        if ((item0 == null || item0.getItem() != TCItemRegistry.flippers) && hasFlippers) {
            if (!player.capabilities.isCreativeMode && player.capabilities.isFlying) {
                player.capabilities.isFlying = false;
            }

            hasFlippers = false;
        }       
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
