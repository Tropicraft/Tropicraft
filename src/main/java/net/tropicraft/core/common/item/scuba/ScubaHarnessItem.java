package net.tropicraft.core.common.item.scuba;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.tropicraft.Constants;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.client.scuba.ScubaHUD;

import javax.annotation.Nullable;
import java.util.List;

@EventBusSubscriber(modid = Constants.MODID, bus = Bus.FORGE)
public class ScubaHarnessItem extends ScubaArmorItem {

    public ScubaHarnessItem(ScubaType type, Properties properties) {
        super(type, EquipmentSlot.CHEST, properties);
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        int airRemaining = getRemainingAir(stack);
        tooltip.add(TropicraftLangKeys.SCUBA_AIR_TIME
                .format(new TextComponent(ScubaHUD.formatTime(airRemaining))
                        .withStyle(ScubaHUD.getAirTimeColor(airRemaining, worldIn)))
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean providesAir() {
        return true;
    }

    @Override
    public void tickAir(Player player, EquipmentSlot slot, ItemStack stack) {
        if (player.level.isClientSide || player.getAbilities().instabuild) return;
        CompoundTag scubaData = stack.getOrCreateTagElement("scuba");
        int remaining = getRemainingAir(stack);
        if (remaining > 0) {
            scubaData.putInt("air", remaining - 1);
            player.setAirSupply(player.getMaxAirSupply());
        }
    }
    
    @Override
    public int addAir(int air, ItemStack stack) {
        if (air > 0) {
            int current = getRemainingAir(stack);
            int max = getMaxAir(stack);
            int newAir = Math.min(current + air, max);
            stack.getOrCreateTagElement("scuba").putInt("air", newAir);
            return air - (newAir - current);
        }
        return 0;
    }

    @Override
    public int getRemainingAir(ItemStack stack) {
        return stack.getOrCreateTagElement("scuba").getInt("air");
    }
    
    @Override
    public int getMaxAir(ItemStack stack) {
        return 20 * 60 * 10; // 10 Minutes
    }
    
    public static void handleUnderwaterBreathing(PlayerTickEvent event) {
        
    }
}
