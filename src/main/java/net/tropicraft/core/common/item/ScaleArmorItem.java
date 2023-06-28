package net.tropicraft.core.common.item;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class ScaleArmorItem extends TropicraftArmorItem {
    public ScaleArmorItem(ArmorItem.Type slotType, Properties properties) {
        super(ArmorMaterials.SCALE_ARMOR, slotType, properties);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
            for (var armor : event.getEntity().getArmorSlots()) {
                if (armor.getItem() instanceof ScaleArmorItem) {
                    event.setCanceled(true);
                    break;
                }
            }
        }
    }
}
