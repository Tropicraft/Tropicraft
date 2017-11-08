package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.egg.EntityEchinodermEgg;

public class RenderEchinodermEgg extends Render<EntityEchinodermEgg> {

	public RenderEchinodermEgg() {
		super(Minecraft.getMinecraft().getRenderManager());
	}

	private void buf(VertexBuffer buffer, double x, double y, double z, double tex1, double tex2) {
		buffer.pos(x, y, z).tex(tex1, tex2).endVertex();
	}

	@Override
	public void doRender(EntityEchinodermEgg entity, double x, double y, double z, float yaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x, (float)y, (float)z);
		GlStateManager.enableRescaleNormal();
		this.bindEntityTexture(entity);
		Tessellator tessellator = Tessellator.getInstance();

		GlStateManager.rotate(180f - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

		GlStateManager.scale(0.25f, 0.25f, 0.25f);

		float f = 0;
		float f1 = 1;
		float f2 = 0;
		float f3 = 1;
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		VertexBuffer buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
		buf(buffer, (double)(0.0F - f5), (double)(0.0F - f6), 0.0D, (double)f, (double)f3);
		buf(buffer, (double)(f4 - f5), (double)(0.0F - f6), 0.0D, (double)f1, (double)f3);
		buf(buffer, (double)(f4 - f5), (double)(f4 - f6), 0.0D, (double)f1, (double)f2);
		buf(buffer, (double)(0.0F - f5), (double)(f4 - f6), 0.0D, (double)f, (double)f2);
		tessellator.draw();

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityEchinodermEgg entity) {
		return TropicraftRenderUtils.bindTextureEntity("seaurchinegg");
	}
}