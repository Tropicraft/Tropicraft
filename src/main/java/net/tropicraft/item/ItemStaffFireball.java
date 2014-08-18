package net.tropicraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.entity.projectile.EntityFireBall;

/* extends to allow inner logic fixes to still happen */
public class ItemStaffFireball extends Item
{
	//public Icon particles[] = new Icon[8];
	
    public ItemStaffFireball()
    {
        super();
        maxStackSize = 1;
        setMaxDamage(100);
    }
    
    @Override
    public void registerIcons(IconRegister par1IconRegister) {
    	//particles[0] = par1IconRegister.registerIcon(ModInfo.ICONLOCATION + "particle_firetrail");
    	this.itemIcon = par1IconRegister.registerIcon(ModInfo.ICONLOCATION + "staff_fire");
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (!entityplayer.capabilities.isCreativeMode)
        {
        	itemstack.damageItem(1, entityplayer);
        }
        world.playSoundAtEntity(entityplayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        if (!world.isRemote)
        {
            world.spawnEntityInWorld(new EntityFireBall(world, entityplayer));
        }
        return itemstack;
    }
}
