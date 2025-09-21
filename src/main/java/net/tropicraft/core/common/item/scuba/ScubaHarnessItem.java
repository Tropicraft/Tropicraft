package net.tropicraft.core.common.item.scuba;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;
import net.tropicraft.core.common.item.TropicraftArmorMaterials;
import net.tropicraft.core.common.item.component.TropicraftDataComponents;

public class ScubaHarnessItem extends ScubaArmorItem {
    public ScubaHarnessItem(ScubaType type, Properties properties) {
        super(type, TropicraftArmorMaterials.applySafe(properties, type.material(), ArmorType.CHESTPLATE));
    }

    @Override
    public boolean providesAir() {
        return true;
    }

    @Override
    public void tickAir(Player player, EquipmentSlot slot, ItemStack stack) {
        if (player.level().isClientSide || player.getAbilities().instabuild) {
            return;
        }
        int remainingAir = getRemainingAir(stack);
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
