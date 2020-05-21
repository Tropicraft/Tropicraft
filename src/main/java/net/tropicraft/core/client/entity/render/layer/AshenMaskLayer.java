package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.AshenModel;
import net.tropicraft.core.client.entity.render.AshenRenderer;
import net.tropicraft.core.common.entity.hostile.AshenEntity;
import org.lwjgl.opengl.GL11;

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
           // modelAshen.head.postRender(.045F);

            stack.translate(-0.03125F, 0.40625F, .18F);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(TropicraftRenderUtils.getTextureEntity("ashen/mask")));
            mask.renderMask(stack, ivertexbuilder, ashen.getMaskType());
            stack.pop();
        }
    }
}
