package net.tropicraft.registry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.entity.projectile.EntityCoconutGrenade;
import net.tropicraft.item.ItemTropicraft;

public class ItemCoconutBomb extends ItemTropicraft {

    public ItemCoconutBomb() {
        super();
        this.setCreativeTab(TCCreativeTabRegistry.tabCombat);
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
            itemstack.stackSize--;
            world.playSoundAtEntity(player, "random.bow", 0.5f, 0.4F/ (itemRand.nextFloat() * 0.4F + 0.8F));
           //TODO if (!world.isRemote && Tropicraft.coconutBombWhitelistedUsers.contains(player.getUniqueID())) {
                world.spawnEntityInWorld(new EntityCoconutGrenade(world, player));
   /*         } else {
                if (world.isRemote && !Tropicraft.coconutBombWhitelistedUsers.contains(player.getUniqueID()))
                    player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tropicraft.coconutBombWarning")));
            }*/
        

        return itemstack;
    }
    
    private String getName(EntityPlayer player) {
        return player.getCommandSenderName();
    }
}
