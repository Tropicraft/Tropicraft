package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.math.Vector3f;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.TropiBeeModel;
import net.tropicraft.core.client.entity.render.TropiBeeRenderer;
import net.tropicraft.core.common.entity.TropiBeeEntity;

public class SunglassesLayer extends RenderLayer<TropiBeeEntity, TropiBeeModel> {

    private TropicraftSpecialRenderHelper mask;
    private TropiBeeModel beeModel;

    public SunglassesLayer(RenderLayerParent<TropiBeeEntity, TropiBeeModel> featureRendererContext) {
        super(featureRendererContext);
        beeModel = featureRendererContext.getModel();
        mask = new TropicraftSpecialRenderHelper();
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferIn, int packedLightIn, TropiBeeEntity bee, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        stack.pushPose();
        beeModel.setupAnim(bee, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        beeModel.getBody().translateAndRotate(stack);

        if (!bee.isBaby()) {
            stack.translate(0.03125F, 1.350F, -.313F); 
        } else {
            stack.translate(0.025F, 1.450F, -.163F);
            stack.scale(0.55F, 0.55F, 0.55F);
        }
        stack.mulPose(Vector3f.YP.rotationDegrees(180));
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TropicraftRenderUtils.getTextureEntity("sunglasses")));
        mask.renderMask(stack, ivertexbuilder, 0, packedLightIn, OverlayTexture.NO_OVERLAY);
        stack.popPose();
    }
}
