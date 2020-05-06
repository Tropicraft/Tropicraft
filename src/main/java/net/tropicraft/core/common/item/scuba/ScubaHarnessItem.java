package net.tropicraft.core.common.item.scuba;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.tropicraft.Constants;

@EventBusSubscriber(modid = Constants.MODID, bus = Bus.FORGE)
public class ScubaHarnessItem extends ScubaArmorItem {

    public ScubaHarnessItem(ScubaType type, Properties properties) {
        super(type, EquipmentSlotType.CHEST, properties);
    }

    @Override
    public boolean providesAir() {
        return true;
    }

    @Override
    public void tickAir(ServerPlayerEntity player, EquipmentSlotType slot, ItemStack stack) {
        CompoundNBT scubaData = stack.getOrCreateChildTag("scuba");
        int remaining = getRemainingAir(stack);
        if (remaining > 0) {
            scubaData.putInt("air", remaining - 1);
            player.setAir(player.getMaxAir());
        }
    }

    @Override
    public int getRemainingAir(ItemStack stack) {
        return stack.getOrCreateChildTag("scuba").getInt("air");
    }
    
    public static void handleUnderwaterBreathing(PlayerTickEvent event) {
        
    }
}
