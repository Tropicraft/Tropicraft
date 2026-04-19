package net.tropicraft.core.common.attribute;

import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.data.TropicraftLangKeys;

@EventBusSubscriber(modid = Tropicraft.ID)
public class TropicraftAttributes {
    public static final DeferredRegister<Attribute> REGISTER = DeferredRegister.create(Registries.ATTRIBUTE, Tropicraft.ID);

    public static final DeferredHolder<Attribute, Attribute> UNDERWATER_VISIBILITY = REGISTER.register(
            "underwater_visibility",
            () -> new RangedAttribute(TropicraftLangKeys.SCUBA_VISIBILITY_STAT.key(), 1.0, 0.0, 10.0).setSyncable(true)
    );

    @SubscribeEvent
    public static void onRegisterAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, UNDERWATER_VISIBILITY);
    }

    @EventBusSubscriber(modid = Tropicraft.ID, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void renderWaterFog(ViewportEvent.RenderFog event) {
            if (event.getType() != FogType.WATER) {
                return;
            }

            Camera camera = event.getCamera();
            if (camera.entity() instanceof LocalPlayer player) {
                double visibility = player.getAttributeValue(TropicraftAttributes.UNDERWATER_VISIBILITY);
                if (visibility != 1.0) {
                    event.scaleFarPlaneDistance((float) visibility);
                }
            }
        }
    }
}
