package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.common.entity.underdasea.EntityStarfish;
import net.tropicraft.core.common.entity.underdasea.StarfishType;

public class RenderStarfish extends Render<EntityStarfish> {

	public RenderStarfish() {
		super(Minecraft.getMinecraft().getRenderManager());
	}

	/**
	 * Amount freshly hatched starfish are scaled down while rendering.
	 */
	public static final float BABY_RENDER_SCALE = 0.25f;

	/**
	 * Amount mature starfish are scaled down while rendering.
	 */
	public static final float ADULT_RENDER_SCALE = 1f;

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityStarfish entity) {
		return null; //Using manual texture bind for loop below instead of bindEntityTexture method
	}

	@Override
	public void doRender(EntityStarfish entity, double d0, double d1, double d2, float yaw, float partialTicks) {
		EntityStarfish starfish = (EntityStarfish) entity;
		StarfishType type = starfish.getStarfishType();

		float f = 0f;
		float f1 = 1f;
		float f2 = 0f;
		float f3 = 1f;
		float f1shifted = 1;
		float f3shifted = 1;

		Tessellator tessellator = Tessellator.getInstance();

		GlStateManager.pushMatrix();
		GlStateManager.translate(d0-0.5f, d1, d2-0.5f);
		GlStateManager.rotate(90f, 1f, 0f, 0f);

		float growthProgress = starfish.getGrowthProgress();
		float scale = BABY_RENDER_SCALE + growthProgress*(ADULT_RENDER_SCALE-BABY_RENDER_SCALE);

		GlStateManager.scale(scale, scale, scale);

		GlStateManager.enableRescaleNormal();
		for (int i = 0; i < type.getLayerCount(); i++) {
			renderManager.renderEngine.bindTexture(new ResourceLocation(type.getTexturePaths().get(i)));
			if (entity.hurtTime > 0) {
				GlStateManager.color(1f, 0f, 0f, 1f);
			}
			popper(tessellator, f1, f2, f, f3, f1shifted, f3shifted, type.getLayerHeights()[i]);
			GlStateManager.translate(0f, 0f, -type.getLayerHeights()[i]);
		}

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}

	private void buf(BufferBuilder buffer, double x, double y, double z, double tex1, double tex2) {
		buffer.pos(x, y, z).tex(tex1, tex2).endVertex();
	}

	private void popper(Tessellator tessellator, float f, float f1, float f2, float f3, float f1shifted, float f3shifted, float layerHeight) {
		float f4 = 1.0F;
		float f5 = layerHeight;

		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.glNormal3f(0.0F, 0.0F, 1.0F);

		//vertexbuffer.pos(0D, 0D, 0D).tex((double)f, (double)f3shifted).endVertex();
		buf(vertexbuffer, 0.0D, 0.0D, 0.0D, f, f3shifted);
		buf(vertexbuffer, f4, 0.0D, 0.0D, f2, f3shifted);
		buf(vertexbuffer, f4, 1.0D, 0.0D, f2, f1shifted);
		buf(vertexbuffer, 0.0D, 1.0D, 0.0D, f, f1shifted);
		tessellator.draw();

		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.glNormal3f(0.0F, 0.0F, -1F);
		buf(vertexbuffer, 0.0D, 1.0D, 0.0F - f5, f, f1);
		buf(vertexbuffer, f4, 1.0D, 0.0F - f5, f2, f1);
		buf(vertexbuffer, f4, 0.0D, 0.0F - f5, f2, f3);
		buf(vertexbuffer, 0.0D, 0.0D, 0.0F - f5, f, f3);
		tessellator.draw();

		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.glNormal3f(-1F, 0.0F, 0.0F);
		for (int i = 0; i < 32; i++) {
			float f6 = (float) i / 32F;
			float f10 = (f + (f2 - f) * f6) - 0.001953125F;
			float f14 = f4 * f6;
			buf(vertexbuffer, f14, 0.0D, 0.0F - f5, f10, f3);
			buf(vertexbuffer, f14, 0.0D, 0.0D, f10, f3);
			buf(vertexbuffer, f14, 1.0D, 0.0D, f10, f1);
			buf(vertexbuffer, f14, 1.0D, 0.0F - f5, f10, f1);
		}
		tessellator.draw();

		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.glNormal3f(1.0F, 0.0F, 0.0F);
		for (int j = 0; j < 32; j++) {
			float f7 = (float) j / 32F;
			float f11 = (f + (f2 - f) * f7) - 0.001953125F;
			float f15 = f4 * f7 + 0.03125F;
			buf(vertexbuffer, f15, 1.0D, 0.0F - f5, f11, f1);
			buf(vertexbuffer, f15, 1.0D, 0.0D, f11, f1);
			buf(vertexbuffer, f15, 0.0D, 0.0D, f11, f3);
			buf(vertexbuffer, f15, 0.0D, 0.0F - f5, f11, f3);
		}
		tessellator.draw();

		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
		for (int k = 0; k < 32; k++) {
			float f8 = (float) k / 32F;
			float f12 = (f3 + (f1 - f3) * f8) - 0.001953125F;
			float f16 = f4 * f8 + 0.03125F;
			buf(vertexbuffer, 0.0D, f16, 0.0D, f, f12);
			buf(vertexbuffer, f4, f16, 0.0D, f2, f12);
			buf(vertexbuffer, f4, f16, 0.0F - f5, f2, f12);
			buf(vertexbuffer, 0.0D, f16, 0.0F - f5, f, f12);
		}
		tessellator.draw();

		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.glNormal3f(0.0F, -1F, 0.0F);
		for (int l = 0; l < 32; l++) {
			float f9 = (float) l / 32F;
			float f13 = (f3 + (f1 - f3) * f9) - 0.001953125F;
			float f17 = f4 * f9;
			buf(vertexbuffer, f4, f17, 0.0D, f2, f13);
			buf(vertexbuffer, 0.0D, f17, 0.0D, f, f13);
			buf(vertexbuffer, 0.0D, f17, 0.0F - f5, f, f13);
			buf(vertexbuffer, f4, f17, 0.0F - f5, f2, f13);
		}

		tessellator.draw();
	}
}