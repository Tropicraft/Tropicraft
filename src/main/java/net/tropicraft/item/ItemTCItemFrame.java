package net.tropicraft.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHangingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityTCItemFrame;
import net.tropicraft.info.TCInfo;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class ItemTCItemFrame extends ItemHangingEntity {

    private final Class hangingEntityClass;
    private boolean shouldDropContents;
    
    public ItemTCItemFrame(Class clazz, boolean shouldDropContents) {
        super(clazz);
        this.setCreativeTab(TCCreativeTabRegistry.tabDecorations);
        this.hangingEntityClass = clazz;
        this.shouldDropContents = shouldDropContents;
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        if (par7 == 0) {
            return false;
        } else if (par7 == 1) {
            return false;
        } else {
            int i1 = Direction.facingToDirection[par7];
            EntityTCItemFrame entityhanging = this.createHangingEntity(par3World, par4, par5, par6, i1);

            if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
                return false;
            } else {
                if (entityhanging != null && entityhanging.onValidSurface()) {
                    if (!par3World.isRemote) {
                        par3World.spawnEntityInWorld(entityhanging);
                    }

                    --par1ItemStack.stackSize;
                }

                return true;
            }
        }
    }

    /**
     * Create the hanging entity associated to this item.
     */
    private EntityTCItemFrame createHangingEntity(World par1World, int par2, int par3, int par4, int par5) {
        return new EntityTCItemFrame(par1World, par2, par3, par4, par5, shouldDropContents);
    }
    
    @Override
    public void registerIcons(IIconRegister iconRegistry) {
        this.itemIcon = iconRegistry.registerIcon(TCInfo.ICON_LOCATION + "itemframe");
    }
}
