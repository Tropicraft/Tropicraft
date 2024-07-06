package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.EIHMachineModel;
import net.tropicraft.core.client.scuba.ModelScubaGear;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.AirCompressorBlockEntity;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;

public class AirCompressorRenderer extends MachineRenderer<AirCompressorBlockEntity> {

    private final ModelScubaGear tankModel;
    //private final ModelScubaGear tankModel = new ModelScubaGear(0, EquipmentSlot.CHEST); // Can't reuse the main one with a different scale

    public AirCompressorRenderer(BlockEntityRendererProvider.Context context) {
        super(context, TropicraftBlocks.AIR_COMPRESSOR.get(), new EIHMachineModel(Minecraft.getInstance().getEntityModels().bakeLayer(TropicraftRenderLayers.AIRCOMPRESSOR_LAYER)));
        tankModel = new ModelScubaGear(context.bakeLayer(TropicraftRenderLayers.CHEST_SCUBA_LAYER), EquipmentSlot.CHEST);
    }

    @Override
    protected Material getMaterial() {
        return TropicraftRenderUtils.getTEMaterial("drink_mixer");
    }

    @Override
    protected void animationTransform(AirCompressorBlockEntity te, PoseStack stack, float partialTicks) {
        float progress = te.getBreatheProgress(partialTicks);
        float sin = 1 + Mth.cos(progress);
        float sc = 1 + 0.05f * sin;
        stack.translate(0, 1.5f, 0);
        stack.scale(sc, sc, sc);
        stack.translate(0, -1.5f, 0);
        if (progress < Math.PI) {
            float shake = Mth.sin(te.getBreatheProgress(partialTicks) * 10) * 8.0f;
            stack.mulPose(Axis.YP.rotationDegrees(shake));
        }
    }

    @Override
    protected void renderIngredients(AirCompressorBlockEntity te, PoseStack stack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        if (te.isActive()) {
            stack.pushPose();
            stack.translate(-0.5f, 0.5f, 0);
            stack.mulPose(Axis.YP.rotationDegrees(90));
            // TODO this is likely wrong
            VertexConsumer builder = ItemRenderer.getFoilBuffer(buffer, RenderType.entityCutoutNoCull(ScubaArmorItem.getArmorTexture(te.getTank().getScubaType())), true, false);
            tankModel.showChest = true;
            // TODO 1.17
            tankModel.renderScubaGear(stack, builder, combinedLightIn, combinedOverlayIn, false);
            stack.popPose();
        }
    }
}
