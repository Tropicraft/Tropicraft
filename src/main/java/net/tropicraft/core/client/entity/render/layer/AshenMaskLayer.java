package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.platform.GlStateManager;
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
    public void render(AshenEntity ashen, float v, float v1, float v2, float v3, float v4, float v5, float v6) {
        if (ashen.hasMask()) {
            GlStateManager.pushMatrix();
            modelAshen.head.postRender(.045F);
            TropicraftRenderUtils.bindTextureEntity("ashen/mask");
            GL11.glTranslatef(-0.03125F, 0.40625F, .18F);
            mask.renderMask(ashen.getMaskType());
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
