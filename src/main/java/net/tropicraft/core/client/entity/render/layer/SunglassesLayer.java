package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.TropiBeeModel;
import net.tropicraft.core.client.entity.render.TropiBeeRenderer;
import net.tropicraft.core.common.entity.TropiBeeEntity;

public class SunglassesLayer extends LayerRenderer<TropiBeeEntity, TropiBeeModel> {

    private TropicraftSpecialRenderHelper mask;
    private TropiBeeModel beeModel;

    public SunglassesLayer(TropiBeeRenderer renderer) {
        super(renderer);
        beeModel = new TropiBeeModel();
        mask = new TropicraftSpecialRenderHelper();
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn, TropiBeeEntity bee, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        stack.push();
        beeModel.setRotationAngles(bee, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        beeModel.getBody().translateRotate(stack);

        if (!bee.isChild()) {
            stack.translate(0.03125F, 0.175, -.313F);
        } else {
            stack.translate(0.03125F, 0.295, -.163F);
        }
        stack.rotate(Vector3f.YP.rotationDegrees(180));
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(TropicraftRenderUtils.getTextureEntity("sunglasses")));
        mask.renderMask(stack, ivertexbuilder, 0, packedLightIn, OverlayTexture.NO_OVERLAY);
        stack.pop();
    }
}
