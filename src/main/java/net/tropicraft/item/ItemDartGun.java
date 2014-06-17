package net.tropicraft.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.tropicraft.entity.projectile.EntityDart;
import net.tropicraft.registry.TCCreativeTabRegistry;
import net.tropicraft.registry.TCItemRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDartGun extends ItemTropicraft {

    public ItemDartGun() {
        maxStackSize = 1;
        setCreativeTab(TCCreativeTabRegistry.tabCombat);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer ent, List list, boolean wat) {
        if (itemstack.getItemDamage() == 0)
            return;
        
        list.clear();
        list.add("Blow Gun of " + StatCollector.translateToLocal("dart.tropicraft:" + ItemCurare.effectNames[itemstack.getItemDamage() - 1] + ".name"));
    }
    
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List par3List) {
        par3List.add(new ItemStack(item, 1, 0));
        
        for (int i = 0; i < ItemDart.dartNames.length; ++i) {
            par3List.add(new ItemStack(item, 1, i + 1));
        }
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 25000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.bow;
    }
    
    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i) {
        System.out.println("henk");
        if (entityplayer.capabilities.isCreativeMode || itemstack.getItemDamage() > 0) {
            int j = getMaxItemUseDuration(itemstack) - i;
            float f = (float) j / 20F;
            f = (f * f + f * 2.0F) / 3F;

            if ((double) f < 0.10000000000000001D) {
                return;
            }

            if (f > 1.0F) {
                f = 1.0F;
            }

            EntityDart dart = new EntityDart(world, entityplayer, f * 2.0F, (short)(itemstack.getItemDamage() - 1));

            itemstack.damageItem(1, entityplayer);
            world.playSoundAtEntity(entityplayer, "dartblow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

            int slot = 0;

            for (ItemStack stack : entityplayer.inventory.mainInventory) {
                if (stack != null && stack.getItem() == TCItemRegistry.dart && stack.getItemDamage() == itemstack.getItemDamage() - 1) {
                    break;
                }
                slot++;
            }

            if (!world.isRemote) {
                entityplayer.inventory.mainInventory[slot].stackSize--;
                EntityDart entitydart = new EntityDart(world, entityplayer, f * 2.0F, (short)(itemstack.getItemDamage() - 1));
                world.spawnEntityInWorld(entitydart);
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        System.out.println("henk1");
        if (!world.isRemote && (entityplayer.capabilities.isCreativeMode || itemstack.getItemDamage() > 0)) {
            System.out.println("henk2");
            entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        }
        return itemstack;
    }
}
