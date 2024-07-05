package net.tropicraft.core.common.item.scuba;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.tropicraft.core.client.data.TropicraftLangKeys;
import net.tropicraft.core.client.scuba.ScubaHUD;
import net.tropicraft.core.common.item.TropicraftDataComponents;

import java.util.List;

public class ScubaHarnessItem extends ScubaArmorItem {

    public ScubaHarnessItem(ScubaType type, Properties properties) {
        super(type, Type.CHESTPLATE, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, tooltip, flagIn);
        int airRemaining = getRemainingAir(stack);
        tooltip.add(TropicraftLangKeys.SCUBA_AIR_TIME
                .format(Component.literal(ScubaHUD.formatTime(airRemaining))
                        .withStyle(ScubaHUD.getAirTimeColor(airRemaining)))
                .copy()
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean providesAir() {
        return true;
    }

    @Override
    public void tickAir(Player player, EquipmentSlot slot, ItemStack stack) {
        if (player.level().isClientSide || player.getAbilities().instabuild) return;
        final int remainingAir = getRemainingAir(stack);
        if (remainingAir > 0) {
            stack.set(TropicraftDataComponents.SCUBA_AIR, remainingAir - 1);
            player.setAirSupply(player.getMaxAirSupply());
        }
    }

    @Override
    public int addAir(int air, ItemStack stack) {
        if (air > 0) {
            int current = getRemainingAir(stack);
            int max = getMaxAir(stack);
            int newAir = Math.min(current + air, max);
            stack.set(TropicraftDataComponents.SCUBA_AIR, newAir);
            return air - (newAir - current);
        }
        return 0;
    }

    @Override
    public int getRemainingAir(ItemStack stack) {
        return stack.getOrDefault(TropicraftDataComponents.SCUBA_AIR, 0);
    }

    @Override
    public int getMaxAir(ItemStack stack) {
        return 20 * 60 * 10; // 10 Minutes
    }
}
