package net.tropicraft.core.common.item;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class ScaleArmorItem extends TropicraftArmorItem {
    public ScaleArmorItem(EquipmentSlot slotType, Properties properties) {
        super(ArmorMaterials.SCALE_ARMOR, slotType, properties);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        var source = event.getSource();
        if (isInvincibleToDamage(source)) {
            for (var armor : event.getEntity().getArmorSlots()) {
                if (armor.getItem() instanceof ScaleArmorItem) {
                    event.setCanceled(true);
                    break;
                }
            }
        }
    }

    private static boolean isInvincibleToDamage(DamageSource source) {
        return source == DamageSource.IN_FIRE || source == DamageSource.LAVA;
    }
}
