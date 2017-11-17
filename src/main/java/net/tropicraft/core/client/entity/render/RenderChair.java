package net.tropicraft.core.client.entity.render;

import java.nio.FloatBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.ColorHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelChair;
import net.tropicraft.core.client.entity.model.ModelEIH;
import net.tropicraft.core.common.entity.placeable.EntityChair;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class RenderChair extends Render<EntityChair> {

	protected ModelBase modelChair;
	FloatBuffer color;
	float red = 0.0F, green = 0.0F, blue = 0.0F, alpha = 1.0F;

	public RenderChair() {
		super(Minecraft.getMinecraft().getRenderManager());
		shadowSize = .5F;
		modelChair = new ModelChair();
	}

	public void renderChair(EntityChair entitychair, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();

		red = ColorHelper.getRed(entitychair.getColor());
		green = ColorHelper.getGreen(entitychair.getColor());
		blue = ColorHelper.getBlue(entitychair.getColor());

		GL11.glTranslatef((float) d, (float) d1 + .3125F, (float) d2);
		GL11.glRotatef(f + (180 - f)*2, 0.0F, 1.0F, 0.0F);

		// Draw arms of chair
		Minecraft.getMinecraft().renderEngine.bindTexture(TropicraftRenderUtils.getTextureEntity("chair_layer"));
		GL11.glScalef(-1F, -1F, 1.0F);
		modelChair.render(entitychair, 0.0F, 1.0F, 0.1F, 0.0F, 0.0F, 0.0625F);

		// Draw the colored part of the chair
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// Change the color mode to blending
		GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_BLEND);
		color = BufferUtils.createFloatBuffer(4).put(new float[]{red, green, blue, alpha});
		color.position(0);
		// Color it
		GL11.glTexEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_COLOR, color);
		Minecraft.getMinecraft().renderEngine.bindTexture(TropicraftRenderUtils.getTextureEntity("chair_color_layer"));
		modelChair.render(entitychair, 0.0F, 1.0F, 0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glDisable(GL11.GL_BLEND);
		// Change the color mode back to modulation
		GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);

		// loldisco
		/*if (red <= 1.0F)
			red += 0.005F;
		else
			if (green <= 1.0F)
				green += 0.005F;
			else
				if (blue <= 1.0F)
					blue += 0.005F;
				else {
					red = green = blue = 0.0F;
				}
		 */		
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(EntityChair entity, double d, double d1, double d2,
			float f, float f1) {
		renderChair((EntityChair) entity, d, d1, d2, f, f1);
	}

	@Override
	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityChair entity) {
		return TropicraftRenderUtils.getTextureEntity("chairBlue");
	}
}