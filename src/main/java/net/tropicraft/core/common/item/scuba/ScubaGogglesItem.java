package net.tropicraft.core.common.item.scuba;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Camera;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;
import net.tropicraft.core.client.data.TropicraftLangKeys;

import java.util.UUID;

import net.minecraft.world.item.Item.Properties;

@EventBusSubscriber(modid = Constants.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ScubaGogglesItem extends ScubaArmorItem {

    private static final ResourceLocation GOGGLES_OVERLAY_TEX_PATH = new ResourceLocation(Constants.MODID, "textures/gui/goggles.png");

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
    
    // Taken from ForgeIngameGui#renderPumpkinOverlay
    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderHelmetOverlay(ItemStack stack, Player player, int width, int height, float partialTicks) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableAlphaTest();
        Minecraft mc = Minecraft.getInstance();
        double scaledWidth = mc.getWindow().getGuiScaledWidth();
        double scaledHeight = mc.getWindow().getGuiScaledHeight();
        mc.getTextureManager().bind(GOGGLES_OVERLAY_TEX_PATH);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0f, 1.0f).endVertex();
        bufferbuilder.vertex(scaledWidth, scaledHeight, -90.0D).uv(1.0f, 1.0f).endVertex();
        bufferbuilder.vertex(scaledWidth, 0.0D, -90.0D).uv(1.0f, 0.0f).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0f, 0.0f).endVertex();
        tessellator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableAlphaTest();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void renderWaterFog(FogDensity event) {
        Camera info = event.getInfo();
        FluidState fluid = info.getFluidInCamera();
        if (fluid.is(FluidTags.WATER) && info.getEntity() instanceof LocalPlayer) {
            LocalPlayer player = (LocalPlayer) info.getEntity();
            if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ScubaGogglesItem) {
                // Taken from FogRenderer#setupFog in the case where the player is in fluid
                RenderSystem.fogMode(GlStateManager.FogMode.EXP2);
                float f = 0.05F - player.getWaterVision() * player.getWaterVision() * 0.03F;

                // Reduce fog slightly
                f *= 0.75F;
    
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
