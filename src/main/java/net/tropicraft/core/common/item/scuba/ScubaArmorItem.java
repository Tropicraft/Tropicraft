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
import net.minecraftforge.client.IItemRenderProperties;
import net.tropicraft.Constants;
import net.tropicraft.core.client.scuba.ModelScubaGear;
import net.tropicraft.core.common.item.ArmorMaterials;
import net.tropicraft.core.common.item.TropicraftArmorItem;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ScubaArmorItem extends TropicraftArmorItem {

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
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {

            @Nullable
            @Override
            public HumanoidModel<?> getArmorModel(LivingEntity entity, ItemStack item, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                if (item.isEmpty()) {
                    return null;
                }

                HumanoidModel<?> armorModel = getArmorModel(armorSlot);
                if (armorModel != null) {
                    prepareModel(armorModel, entity);
                    return armorModel;
                } else {
                    return null;
                }
            }

            @Nullable
            private HumanoidModel<?> getArmorModel(EquipmentSlot armorSlot) {
                return switch (armorSlot) {
                    case HEAD -> ModelScubaGear.HEAD;
                    case CHEST -> ModelScubaGear.CHEST;
                    case FEET -> ModelScubaGear.FEET;
                    default -> null;
                };
            }

            @SuppressWarnings("unchecked")
            private <E extends LivingEntity> void prepareModel(HumanoidModel<E> armorModel, LivingEntity entity) {
                armorModel.prepareMobModel((E) entity, 0.0F, 0.0F, 1.0F);
                armorModel.crouching = entity.isShiftKeyDown();
                armorModel.young = entity.isBaby();
                armorModel.rightArmPose = entity.getMainHandItem() != null ? HumanoidModel.ArmPose.BLOCK : HumanoidModel.ArmPose.EMPTY;
            }

            @Override
            public void renderHelmetOverlay(ItemStack stack, Player player, int width, int height, float partialTicks) {
                if (stack.getItem() instanceof ScubaGogglesItem) {
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
                    Tesselator tesselator = Tesselator.getInstance();
                    BufferBuilder builder = tesselator.getBuilder();
                    builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                    builder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0f, 1.0f).endVertex();
                    builder.vertex(scaledWidth, scaledHeight, -90.0D).uv(1.0f, 1.0f).endVertex();
                    builder.vertex(scaledWidth, 0.0D, -90.0D).uv(1.0f, 0.0f).endVertex();
                    builder.vertex(0.0D, 0.0D, -90.0D).uv(0.0f, 0.0f).endVertex();
                    tesselator.end();
                    RenderSystem.depthMask(true);
                    RenderSystem.enableDepthTest();
                    //RenderSystem.enableAlphaTest();
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        });
    }
}
