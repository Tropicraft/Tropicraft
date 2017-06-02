package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.underdasea.EntityEchinodermEgg;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderEchinodermEgg extends Render<EntityEchinodermEgg> {
	
	protected RenderEchinodermEgg() {
		super(Minecraft.getMinecraft().getRenderManager());
	}

	@Override
	public void doRender(EntityEchinodermEgg entity, double x, double y, double z, float yaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x, (float)y, (float)z);
		GlStateManager.enableRescaleNormal();
		this.bindEntityTexture(entity);
		Tessellator tessellator = Tessellator.getInstance();

		GL11.glRotatef(180f - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

		GL11.glScalef(0.25f, 0.25f, 0.25f);

		float f = 0;
		float f1 = 1;
		float f2 = 0;
		float f3 = 1;
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		tessellator.addVertexWithUV((double)(0.0F - f5), (double)(0.0F - f6), 0.0D, (double)f, (double)f3);
		tessellator.addVertexWithUV((double)(f4 - f5), (double)(0.0F - f6), 0.0D, (double)f1, (double)f3);
		tessellator.addVertexWithUV((double)(f4 - f5), (double)(f4 - f6), 0.0D, (double)f1, (double)f2);
		tessellator.addVertexWithUV((double)(0.0F - f5), (double)(f4 - f6), 0.0D, (double)f, (double)f2);
		tessellator.draw();

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityEchinodermEgg entity) {
		return TropicraftRenderUtils.bindTextureEntity("seaurchinegg");
	}
}