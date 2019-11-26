package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.EggModel;
import net.tropicraft.core.common.entity.egg.EggEntity;

public class EggRenderer extends LivingRenderer<EggEntity, EggModel> {

	public EggRenderer(final EntityRendererManager rendererManager) {
		super(rendererManager, new EggModel(), 1f);
		shadowOpaque = 0.5f;
	}

	@Override
	public void doRender(EggEntity egg, double x, double y, double z, float yaw, float ticks) {
		GlStateManager.pushMatrix();
		GlStateManager.translated(x, y, z);
	
		if (egg.shouldEggRenderFlat()) {
			shadowSize = 0.0f;
			drawFlatEgg(egg, 0, 0.05f, 0, yaw, ticks);
		} else {
			shadowSize = 0.2f;
			GlStateManager.scalef(0.5f, 0.65f, 0.5f);
			super.doRender(egg, 0, 0, 0, yaw, ticks);
		}
		GlStateManager.popMatrix();
	}
	
	public void drawFlatEgg(EggEntity ent, double x, double y, double z, float yaw, float ticks) {
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		this.bindEntityTexture(ent);
		Tessellator tessellator = Tessellator.getInstance();
		GlStateManager.translated(x, y, z);

		GlStateManager.rotatef(180f - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

		GlStateManager.scalef(0.25f, 0.25f, 0.25f);

		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.normal3f(0.0F, 1.0F, 0.0F);
		buffer.pos(-.5, -.25, 0).tex(0, 1).endVertex();
		buffer.pos( .5, -.25, 0).tex(1, 1).endVertex();
		buffer.pos( .5,  .75, 0).tex(1, 0).endVertex();
		buffer.pos(-.5,  .75, 0).tex(0, 0).endVertex();
		tessellator.draw();

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EggEntity entity) {
		return TropicraftRenderUtils.bindTextureEntity(entity.getEggTexture());
	}
}