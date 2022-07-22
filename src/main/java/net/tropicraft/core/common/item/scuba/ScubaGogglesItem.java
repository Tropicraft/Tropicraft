package net.tropicraft.core.common.item.scuba;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.FogType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.client.data.TropicraftLangKeys;

import java.util.UUID;

@EventBusSubscriber(modid = Constants.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ScubaGogglesItem extends ScubaArmorItem {
    
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Constants.MODID);

    // This is never registered to any entities, so it's not used in any logic
    // Just here for the nice tooltip
    private static final RegistryObject<Attribute> UNDERWATER_VISIBILITY = ATTRIBUTES.register(
            "underwater_visibility",
            () -> new RangedAttribute(TropicraftLangKeys.SCUBA_VISIBILITY_STAT.getKey(), 0, -1, 1)
    );
    private static final AttributeModifier VISIBILITY_BOOST = new AttributeModifier(UUID.fromString("b09a907f-8264-455b-af81-997c06aa2268"), Constants.MODID + ".underwater.visibility", 0.25, Operation.MULTIPLY_BASE);

    private final LazyLoadedValue<Multimap<Attribute, AttributeModifier>> boostedModifiers;

    public ScubaGogglesItem(ScubaType type, Properties builder) {
        super(type, EquipmentSlot.HEAD, builder);

        // lazily initialize because attributes are registered after items
        this.boostedModifiers = new LazyLoadedValue<>(() ->
                ImmutableMultimap.<Attribute, AttributeModifier>builder()
                        .putAll(super.getAttributeModifiers(EquipmentSlot.HEAD, new ItemStack(this)))
                        .put(UNDERWATER_VISIBILITY.get(), VISIBILITY_BOOST)
                        .build()
        );
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void renderWaterFog(FogDensity event) {
        Camera info = event.getCamera();
        //FluidState fluid = info.getFluidInCamera();
        FogType fogType = info.getFluidInCamera();
        if (/*fluid.is(FluidTags.WATER) &&*/fogType == FogType.WATER && info.getEntity() instanceof LocalPlayer player) {
            if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ScubaGogglesItem) {
                float f = 192.0F;
                f *= Math.max(0.25F, player.getWaterVision());

                f *= 1.25;

                event.setDensity(f);
                event.setCanceled(true);
            }
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.HEAD) {
            return boostedModifiers.get();
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }
}
