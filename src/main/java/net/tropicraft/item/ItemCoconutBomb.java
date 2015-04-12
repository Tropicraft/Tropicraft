package net.tropicraft.item;

import tv.twitch.chat.ChatMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.tropicraft.config.ConfigMisc;
import net.tropicraft.entity.projectile.EntityCoconutGrenade;
import net.tropicraft.registry.TCCreativeTabRegistry;

public class ItemCoconutBomb extends ItemTropicraft {

    public ItemCoconutBomb() {
        super();
        this.setCreativeTab(TCCreativeTabRegistry.tabCombat);
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
            itemstack.stackSize--;
            world.playSoundAtEntity(player, "random.bow", 0.5f, 0.4F/ (itemRand.nextFloat() * 0.4F + 0.8F));
           if (!world.isRemote && ConfigMisc.coconutBombWhitelistedUsers.contains(player.getGameProfile().getName())) {
                world.spawnEntityInWorld(new EntityCoconutGrenade(world, player));
            } else {
                if (!world.isRemote && !ConfigMisc.coconutBombWhitelistedUsers.contains(player.getGameProfile().getName()))
                	player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("tropicraft.coconutBombWarning")));
            }
        

        return itemstack;
    }
    
    private String getName(EntityPlayer player) {
        return player.getCommandSenderName();
    }
}
