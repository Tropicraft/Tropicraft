package net.tropicraft.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.tropicraft.core.common.item.scuba.ItemScubaChestplateGear;
import net.tropicraft.core.common.item.scuba.ScubaCapabilities;

public class TropicraftGuiHandler implements IGuiHandler {

    public TropicraftGuiHandler() {

    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == 0) {
            EnumHand hand = EnumHand.values()[x];
            ItemStack held = player.getHeldItem(hand);
            
            if (held != null && held.getItem() instanceof ItemScubaChestplateGear) {
                return new ContainerScubaHarness(player.inventory, held.getCapability(ScubaCapabilities.getGearCapability(), null), hand);
            }
        }
        
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == 0) {
            EnumHand hand = EnumHand.values()[x];
            ItemStack held = player.getHeldItem(hand);
            
            if (held != null && held.getItem() instanceof ItemScubaChestplateGear) {
                return new GuiScubaHarness(new ContainerScubaHarness(player.inventory, held.getCapability(ScubaCapabilities.getGearCapability(), null), hand));
            }
        }
        
        return null;
    }

}
