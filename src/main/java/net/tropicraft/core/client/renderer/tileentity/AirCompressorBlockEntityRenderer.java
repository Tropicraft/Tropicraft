package net.tropicraft.core.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.EIHMachineModel;
import net.tropicraft.core.common.block.tileentity.AirCompressorBlockEntity;

public class AirCompressorBlockEntityRenderer extends MachineBlockEntityRenderer<AirCompressorBlockEntity> {
    public static final Material MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, Tropicraft.location("block/te/drink_mixer"));

    private final Model.Simple tankModel;

    public AirCompressorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new EIHMachineModel(context.bakeLayer(TropicraftRenderLayers.AIRCOMPRESSOR_LAYER)));
        tankModel = new Model.Simple(context.bakeLayer(TropicraftRenderLayers.TANK_SCUBA_LAYER), RenderType::entitySolid);
    }

    @Override
    protected Material getMaterial() {
        return MATERIAL;
    }

    @Override
    protected void animationTransform(AirCompressorBlockEntity blockEntity, PoseStack poseStack, float partialTicks) {
        float progress = blockEntity.getBreatheProgress(partialTicks);
        float sin = 1 + Mth.cos(progress);
        float sc = 1 + 0.05f * sin;
        poseStack.translate(0, 1.5f, 0);
        poseStack.scale(sc, sc, sc);
        poseStack.translate(0, -1.5f, 0);
        if (progress < Math.PI) {
            float shake = Mth.sin(blockEntity.getBreatheProgress(partialTicks) * 10) * 8.0f;
            poseStack.mulPose(Axis.YP.rotationDegrees(shake));
        }
    }

    @Override
    protected void renderIngredients(AirCompressorBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, int lightCoords, int overlayCoords) {
        if (blockEntity.isActive()) {
            poseStack.pushPose();
            poseStack.translate(-0.5f, 0.5f, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(90));
            ResourceLocation texture = blockEntity.getTank().getScubaType().textureLocation();
            tankModel.renderToBuffer(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture)), lightCoords, overlayCoords);
            poseStack.popPose();
        }
    }
}
