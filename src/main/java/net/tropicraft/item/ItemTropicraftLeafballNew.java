package net.tropicraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.entity.projectile.EntityTropicraftLeafballNew;
import CoroUtil.entity.ItemTropicraftLeafball;

/* extends to allow inner logic fixes to still happen */
public class ItemTropicraftLeafballNew extends ItemTropicraftLeafball
{
    public ItemTropicraftLeafballNew()
    {
        super();
        maxStackSize = 16;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (!entityplayer.capabilities.isCreativeMode)
        {
            itemstack.stackSize--;
        }
        world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote)
        {
            world.spawnEntityInWorld(new EntityTropicraftLeafballNew(world, entityplayer));
        }
        return itemstack;
    }
}
