package net.tropicraft.core.common.item.scuba;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.tropicraft.Constants;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.client.scuba.ScubaHUD;

@EventBusSubscriber(modid = Constants.MODID, bus = Bus.FORGE)
public class ScubaHarnessItem extends ScubaArmorItem {

    public ScubaHarnessItem(ScubaType type, Properties properties) {
        super(type, EquipmentSlotType.CHEST, properties);
    }
    
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        int airRemaining = getRemainingAir(stack);
        tooltip.add(TropicraftLangKeys.SCUBA_AIR_TIME
                .format(new StringTextComponent(ScubaHUD.formatTime(airRemaining))
                        .applyTextStyle(ScubaHUD.getAirTimeColor(airRemaining, worldIn)))
                .applyTextStyle(TextFormatting.GRAY));
    }

    @Override
    public boolean providesAir() {
        return true;
    }

    @Override
    public void tickAir(PlayerEntity player, EquipmentSlotType slot, ItemStack stack) {
        if (player.world.isRemote || player.abilities.isCreativeMode) return;
        CompoundNBT scubaData = stack.getOrCreateChildTag("scuba");
        int remaining = getRemainingAir(stack);
        if (remaining > 0) {
            scubaData.putInt("air", remaining - 1);
            player.setAir(player.getMaxAir());
        }
    }
    
    @Override
    public int addAir(int air, ItemStack stack) {
        if (air > 0) {
            int current = getRemainingAir(stack);
            int max = getMaxAir(stack);
            int newAir = Math.min(current + air, max);
            stack.getOrCreateChildTag("scuba").putInt("air", newAir);
            return air - (newAir - current);
        }
        return 0;
    }

    @Override
    public int getRemainingAir(ItemStack stack) {
        return stack.getOrCreateChildTag("scuba").getInt("air");
    }
    
    @Override
    public int getMaxAir(ItemStack stack) {
        return 20 * 60 * 10; // 10 Minutes
    }
    
    public static void handleUnderwaterBreathing(PlayerTickEvent event) {
        
    }
}
