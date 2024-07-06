package net.tropicraft.core.common.item;

import net.minecraft.core.Holder;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.tropicraft.Tropicraft;

@EventBusSubscriber(modid = Tropicraft.ID)
public class ScaleArmorItem extends ArmorItem {
    public ScaleArmorItem(ArmorItem.Type slotType, Properties properties) {
        super((Holder<ArmorMaterial>) TropicraftArmorMaterials.SCALE_ARMOR, slotType, properties);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent.Pre event) {
        if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
            for (var armor : event.getEntity().getArmorSlots()) {
                if (armor.getItem() instanceof ScaleArmorItem) {
                    event.setNewDamage(0.0f);
                    break;
                }
            }
        }
    }
}
