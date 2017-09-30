package net.tropicraft.core.client.entity.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelAshen;
import net.tropicraft.core.common.entity.hostile.EntityAshen;

public class LayerMaskAshen implements LayerRenderer<EntityAshen> {

	private TropicraftSpecialRenderHelper mask;
	private ModelAshen modelAshen;

	public LayerMaskAshen(ModelAshen modelAshen) {
		super();
        mask = new TropicraftSpecialRenderHelper();
        this.modelAshen = modelAshen;
	}

	@Override
	public void doRenderLayer(EntityAshen ashen, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();
        modelAshen.head.postRender(.045F);
        TropicraftRenderUtils.bindTextureEntity("ashen/mask");
        GL11.glTranslatef(-0.03125F, 0.40625F + MathHelper.sin(ashen.bobber), .18F);
        mask.renderMask(ashen.getMaskType());
        GlStateManager.popMatrix();
	}

	@Override
	public boolean shouldCombineTextures() {
		return true;
	}

}
