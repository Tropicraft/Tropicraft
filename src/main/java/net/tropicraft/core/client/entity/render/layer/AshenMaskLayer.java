package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.AshenModel;
import net.tropicraft.core.client.entity.render.AshenRenderer;
import net.tropicraft.core.common.entity.hostile.AshenEntity;

public class AshenMaskLayer extends LayerRenderer<AshenEntity, AshenModel> {

    private TropicraftSpecialRenderHelper mask;
    private AshenModel modelAshen;

    public AshenMaskLayer(AshenRenderer renderer) {
        super(renderer);
        modelAshen = new AshenModel();
        mask = new TropicraftSpecialRenderHelper();
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn, AshenEntity ashen, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (ashen.hasMask()) {
            stack.push();
            modelAshen.setRotationAngles(ashen, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            modelAshen.head.translateRotate(stack);

            stack.translate(-0.03125F, 0.0625f * 3, .18F);
            stack.scale(0.75f, 0.75f, 0.75f);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(TropicraftRenderUtils.getTextureEntity("ashen/mask")));
            mask.renderMask(stack, ivertexbuilder, ashen.getMaskType(), packedLightIn, OverlayTexture.NO_OVERLAY);
            stack.pop();
        }
    }
}
