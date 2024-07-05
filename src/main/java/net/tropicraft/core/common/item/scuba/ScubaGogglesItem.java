package net.tropicraft.core.common.item.scuba;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.tropicraft.Constants;
import net.tropicraft.core.client.data.TropicraftLangKeys;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ScubaGogglesItem extends ScubaArmorItem {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, Constants.MODID);

    // This is never registered to any entities, so it's not used in any logic
    // Just here for the nice tooltip
    private static final DeferredHolder<Attribute, Attribute> UNDERWATER_VISIBILITY = ATTRIBUTES.register(
            "underwater_visibility",
            () -> new RangedAttribute(TropicraftLangKeys.SCUBA_VISIBILITY_STAT.key(), 0, -1, 1)
    );
    private static final AttributeModifier VISIBILITY_BOOST = new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Constants.MODID, "underwater.visibility"), 0.25, Operation.ADD_MULTIPLIED_BASE);

    private final Supplier<ItemAttributeModifiers> boostedModifiers;

    public ScubaGogglesItem(ScubaType type, Properties builder) {
        super(type, Type.HELMET, builder);

        // lazily initialize because attributes are registered after items
        this.boostedModifiers = Suppliers.memoize(() -> {
            ItemAttributeModifiers inheritedModifiers = super.getDefaultAttributeModifiers();
            return inheritedModifiers.withModifierAdded(
                    UNDERWATER_VISIBILITY, VISIBILITY_BOOST, EquipmentSlotGroup.HEAD
            );
        });
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void renderWaterFog(ViewportEvent.RenderFog event) {
        if (event.getFogShape() == FogShape.CYLINDER) {
            // Fog is already matching render distance, we cannot reduce it at this point
            return;
        }

        Camera camera = event.getCamera();
        if (camera.getFluidInCamera() == FogType.WATER && camera.getEntity() instanceof LocalPlayer player) {
            if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ScubaGogglesItem) {
                event.setFarPlaneDistance(event.getFarPlaneDistance() * 1.25f);
                event.setCanceled(true);
            }
        }
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return boostedModifiers.get();
    }
}
