package net.tropicraft.core.client.entity.render;

import java.nio.FloatBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.ColorHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelUmbrella;
import net.tropicraft.core.common.entity.placeable.EntityUmbrella;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class RenderUmbrella extends Render {

	protected ModelBase modelUmbrella;
	FloatBuffer color;
	float red = 0.0F, green = 0.0F, blue = 0.0F, alpha = 1.0F;

	public RenderUmbrella() {
		super(Minecraft.getMinecraft().getRenderManager());
		shadowSize = 2F;
		modelUmbrella = new ModelUmbrella();
	}

	public void renderUmbrella(EntityUmbrella entityUmbrella, double d, double d1, double d2, float f, float f1) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d, (float) d1, (float) d2);
		GL11.glRotatef(180F - f, 0.0F, 1.0F, 0.0F);
		float f2 = (float) entityUmbrella.getTimeSinceHit() - f1;
		float f3 = (float) entityUmbrella.getDamage() - f1;
		if (f3 < 0.0F) {
			f3 = 0.0F;
		}
		if (f2 > 0.0F) {
			GL11.glRotatef(((MathHelper.sin(f2) * f2 * f3) / 10F) * (float) entityUmbrella.getForwardDirection(), 1.0F, 0.0F, 0.0F);
		}

		float f4 = 0.75F;
		GL11.glScalef(f4, f4, f4);
		GL11.glScalef(1.0F / f4, 1.0F / f4, 1.0F / f4);

		red = ColorHelper.getRed(entityUmbrella.getColor());
		green = ColorHelper.getGreen(entityUmbrella.getColor());
		blue = ColorHelper.getBlue(entityUmbrella.getColor());

		// Draw arms of umbrella
		Minecraft.getMinecraft().renderEngine.bindTexture(TropicraftRenderUtils.getTextureEntity("umbrella_layer"));
		GL11.glScalef(-1F, -1F, 1.0F);
		modelUmbrella.render(entityUmbrella, 0.0F, 1.0F, 0.1F, 0.0F, 0.0F, 0.25F);

		// Draw the colored part of the umbrella
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// Change the color mode to blending
		GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_BLEND);
		color = BufferUtils.createFloatBuffer(4).put(new float[]{red, green, blue, alpha});
		color.position(0);
		// Color it
		GL11.glTexEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_COLOR, color);
		Minecraft.getMinecraft().renderEngine.bindTexture(TropicraftRenderUtils.getTextureEntity("umbrella_color_layer"));
		modelUmbrella.render(entityUmbrella, 0.0F, 1.0F, 0.1F, 0.0F, 0.0F, 0.25F);
		GL11.glDisable(GL11.GL_BLEND);
		// Change the color mode back to modulation
		GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
		renderUmbrella((EntityUmbrella) entity, d, d1, d2, f, f1);
	}

	@Override
	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}
}