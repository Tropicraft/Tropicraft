package net.tropicraft.core.common.item.scuba;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.tropicraft.Constants;
import net.tropicraft.core.client.scuba.ModelScubaGear;
import net.tropicraft.core.common.item.ArmorMaterials;
import net.tropicraft.core.common.item.TropicraftArmorItem;

import javax.annotation.Nullable;

public class ScubaArmorItem extends TropicraftArmorItem implements IItemRenderProperties {

    private static final ResourceLocation GOGGLES_OVERLAY_TEX_PATH = new ResourceLocation(Constants.MODID, "textures/gui/goggles.png");
    
    private final ScubaType type;

    public ScubaArmorItem(ScubaType type, EquipmentSlot slotType, Item.Properties properties) {
        super(ArmorMaterials.SCUBA, slotType, properties);
        this.type = type;
    }
    
    public ScubaType getType() {
        return type;
    }
    
    public boolean providesAir() {
        return false;
    }
    
    public void tickAir(Player player, EquipmentSlot slot, ItemStack stack) {
    }

    public int addAir(int air, ItemStack stack) {
        return 0;
    }
    
    public int getRemainingAir(ItemStack stack) {
        return 0;
    }
    
    public int getMaxAir(ItemStack stack) {
        return 0;
    }
    
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return getArmorTexture(this.type).toString();
    }
    
    public static ResourceLocation getArmorTexture(ScubaType material) {
        return new ResourceLocation(Constants.ARMOR_LOCATION + "scuba_gear_" + material.getTextureName() + ".png");   
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties()
        {
            @Override
            @Nullable
            @OnlyIn(Dist.CLIENT)
            public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemstack, EquipmentSlot armorSlot, A _default) {
                if (itemstack.isEmpty()) {
                    return null;
                }

                HumanoidModel<?> armorModel;
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

                ((HumanoidModel) armorModel).prepareMobModel(entityLiving, 0.0F, 0.0F, 1.0F);

                armorModel.crouching = entityLiving.isShiftKeyDown();
                armorModel.young = entityLiving.isBaby();
                armorModel.rightArmPose = entityLiving.getMainHandItem() != null ? HumanoidModel.ArmPose.BLOCK : HumanoidModel.ArmPose.EMPTY;
                return (A) armorModel;
            }
            @Override
            @OnlyIn(Dist.CLIENT)
            public void renderHelmetOverlay(ItemStack stack, Player player, int width, int height, float partialTicks) {
                if(stack.getItem() instanceof ScubaGogglesItem) {
                    //Taken from ForgeIngameGui#renderPumpkinOverlay
                    RenderSystem.disableDepthTest();
                    RenderSystem.depthMask(false);
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    //RenderSystem.disableAlphaTest();
                    Minecraft mc = Minecraft.getInstance();
                    double scaledWidth = mc.getWindow().getGuiScaledWidth();
                    double scaledHeight = mc.getWindow().getGuiScaledHeight();
                    RenderSystem.setShaderTexture(0, GOGGLES_OVERLAY_TEX_PATH); //mc.getTextureManager().bind(GOGGLES_OVERLAY_TEX_PATH);
                    Tesselator tessellator = Tesselator.getInstance();
                    BufferBuilder bufferbuilder = tessellator.getBuilder();
                    bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                    bufferbuilder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0f, 1.0f).endVertex();
                    bufferbuilder.vertex(scaledWidth, scaledHeight, -90.0D).uv(1.0f, 1.0f).endVertex();
                    bufferbuilder.vertex(scaledWidth, 0.0D, -90.0D).uv(1.0f, 0.0f).endVertex();
                    bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0f, 0.0f).endVertex();
                    tessellator.end();
                    RenderSystem.depthMask(true);
                    RenderSystem.enableDepthTest();
                    //RenderSystem.enableAlphaTest();
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        });
    }

}
