package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.TropiBeeModel;
import net.tropicraft.core.common.entity.TropiBeeEntity;

public class SunglassesLayer extends RenderLayer<TropiBeeEntity, TropiBeeModel> {
    private static final ResourceLocation TEXTURE = TropicraftRenderUtils.getTextureEntity("sunglasses");

    private final TropicraftSpecialRenderHelper mask;
    private final TropiBeeModel beeModel;

    public SunglassesLayer(RenderLayerParent<TropiBeeEntity, TropiBeeModel> featureRendererContext) {
        super(featureRendererContext);
        beeModel = featureRendererContext.getModel();
        mask = new TropicraftSpecialRenderHelper();
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferIn, int packedLightIn, TropiBeeEntity bee, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        stack.pushPose();
        beeModel.setupAnim(bee, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        beeModel.body().translateAndRotate(stack);

        if (!bee.isBaby()) {
            stack.translate(0.03125F, 1.350F, -.313F);
        } else {
            stack.translate(0.025F, 1.450F, -.163F);
            stack.scale(0.55F, 0.55F, 0.55F);
        }
        stack.mulPose(Axis.YP.rotationDegrees(180));
        VertexConsumer consumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        mask.renderMask(stack, consumer, 0, packedLightIn, OverlayTexture.NO_OVERLAY);
        stack.popPose();
    }
}
