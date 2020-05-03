package net.tropicraft.core.common.item.scuba;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.tropicraft.Constants;
import net.tropicraft.core.client.scuba.ModelScubaGear;
import net.tropicraft.core.common.item.ArmorMaterials;
import net.tropicraft.core.common.item.TropicraftArmorItem;

@EventBusSubscriber(modid = Constants.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ScubaGogglesItem extends TropicraftArmorItem {

    private static final ResourceLocation GOGGLES_OVERLAY_TEX_PATH = new ResourceLocation(Constants.MODID, "textures/gui/goggles.png");

    private final ScubaType type;

    public ScubaGogglesItem(ScubaType type, Properties builder) {
        super(ArmorMaterials.SCUBA_GOGGLES, EquipmentSlotType.HEAD, builder);
        this.type = type;
    }
    
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return getArmorTexture(this.type).toString();
    }
    
    public static ResourceLocation getArmorTexture(ScubaType material) {
        return new ResourceLocation(Constants.ARMOR_LOCATION + "scuba_gear_" + material.getTextureName() + ".png");   
    }

    @Override
    @Nullable
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemstack, EquipmentSlotType armorSlot, A _default) {
        if (itemstack.isEmpty()) {
            return null;
        }

        BipedModel<?> armorModel;
        switch (armorSlot) {
        case HEAD:
            armorModel = ModelScubaGear.HEAD;
            break;
        case CHEST:
            armorModel = ModelScubaGear.CHEST;
            break;
        case FEET:
            armorModel = ModelScubaGear.FEET;
            break;  
        default:
            return null;
        }

        armorModel.isSneak = entityLiving.isSneaking();
        armorModel.isChild = entityLiving.isChild();
        armorModel.rightArmPose = entityLiving.getHeldItemMainhand() != null ? BipedModel.ArmPose.BLOCK : BipedModel.ArmPose.EMPTY;
        return (A) armorModel;
    }
    
    // Taken from ForgeIngameGui#renderPumpkinOverlay
    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderHelmetOverlay(ItemStack stack, PlayerEntity player, int width, int height, float partialTicks) {
        GlStateManager.disableDepthTest();
        GlStateManager.depthMask(false);
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlphaTest();
        Minecraft mc = Minecraft.getInstance();
        double scaledWidth = mc.mainWindow.getScaledWidth();
        double scaledHeight = mc.mainWindow.getScaledHeight();
        mc.getTextureManager().bindTexture(GOGGLES_OVERLAY_TEX_PATH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, scaledHeight, -90.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos(scaledWidth, scaledHeight, -90.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos(scaledWidth, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepthTest();
        GlStateManager.enableAlphaTest();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void renderWaterFog(FogDensity event) {
        ActiveRenderInfo info = event.getInfo();
        IFluidState ifluidstate = info.getFluidState();
        if (ifluidstate.isTagged(FluidTags.WATER) && info.getRenderViewEntity() instanceof ClientPlayerEntity) {
            ClientPlayerEntity clientplayerentity = (ClientPlayerEntity) info.getRenderViewEntity();
            if (clientplayerentity.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() instanceof ScubaGogglesItem) {
                // Taken from FogRenderer#setupFog in the case where the player is in fluid
                GlStateManager.fogMode(GlStateManager.FogMode.EXP2);
                float f = 0.05F - clientplayerentity.getWaterBrightness() * clientplayerentity.getWaterBrightness() * 0.03F;
                Biome biome = clientplayerentity.world.getBiome(new BlockPos(clientplayerentity));
                if (biome == Biomes.SWAMP || biome == Biomes.SWAMP_HILLS) {
                    f += 0.005F;
                }
                
                // Reduce fog slightly
                f *= 0.75F;
    
                event.setDensity(f);
                event.setCanceled(true);
            }
        }
    }
}
