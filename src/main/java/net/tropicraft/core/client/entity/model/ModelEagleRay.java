package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.tropicraft.core.common.entity.underdasea.EntityEagleRay;

public class ModelEagleRay extends ModelBase {
	/**
	 * Wing joint amplitudes, linearly interpolated between previous tick and this tick using partialTicks.
	 */
	private float[] interpolatedWingAmplitudes = new float[EntityEagleRay.WING_JOINTS];

	private ModelRenderer body;

	public ModelEagleRay() {
		textureWidth = 128;
		textureHeight = 64;

		body = new ModelRenderer(this, 32, 0);
		body.addBox(-2F, 0F, 0F, 5, 3, 32);
		body.setRotationPoint(0F, 0F, -8F);
		body.setTextureSize(128, 64);
		body.mirror = true;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		body.render(f5);
		renderWings();
		renderTailSimple();
	}

	private void renderTailSimple() {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buffer = tessellator.getBuffer();

		float minU = 0.75f;
		float maxU = 1.0f;
		float minV = 0.0f;
		float maxV = 0.5f;

		GlStateManager.pushMatrix();
		GlStateManager.translate(0.55f, 0f, 1.5f);
		GlStateManager.rotate(-90f, 0f, 1f, 0f);
		GlStateManager.scale(1.5f, 1f, 1f);

		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(0, 0, 0).tex(minU, minV).endVertex();
		buffer.pos(0, 0, 1).tex(minU, maxV).endVertex();
		buffer.pos(1, 0, 1).tex(maxU, maxV).endVertex();
		buffer.pos(1, 0, 0).tex(maxU, minV).endVertex();
		tessellator.draw();

		GlStateManager.popMatrix();
	}

	private void renderWings() {
		GlStateManager.disableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.scale(2f, 0.5f, 2f);
		GlStateManager.translate(0.1f, 0f, -0.25f);
		renderWing(false);
		GlStateManager.rotate(180f, 0f, 1f, 0f);
		GlStateManager.translate(0.10f, 0f, -1f);
		renderWing(true);
		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
	}

	private void buf(VertexBuffer buffer, double x, double y, double z, double tex1, double tex2) {
		buffer.pos(x, y, z).tex(tex1, tex2).endVertex();
	}

	private void renderWing(boolean reverse) {
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buffer = tessellator.getBuffer();

		float minUFront = 0f;
		float maxUFront = 0.25f;
		float minVFront = 0f;
		float maxVFront = 0.5f;

		float minUBack = 0f;
		float maxUBack = 0.25f;
		float minVBack = 0.5f;
		float maxVBack = 1f;

		for (int i = 1; i < EntityEagleRay.WING_JOINTS; i++) {
			float prevAmplitude = interpolatedWingAmplitudes[i-1];
			float amplitude = interpolatedWingAmplitudes[i];

			float prevX = (i-1)/(EntityEagleRay.WING_JOINTS-1f);
			float x = i/(EntityEagleRay.WING_JOINTS-1f);

			float prevUFront = minUFront + (maxUFront-minUFront)*prevX;
			float uFront = minUFront + (maxUFront-minUFront)*x;
			float prevUBack = minUBack + (maxUBack-minUBack)*prevX;
			float uBack = minUBack + (maxUBack-minUBack)*x;

			float offset = -0.001f;
			// Bottom
			buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
			buf(buffer, x, amplitude-offset, 0.0D, uBack, reverse ? maxVBack : minVBack);
			buf(buffer, x, amplitude-offset, 1.0D, uBack, reverse ? minVBack : maxVBack);
			buf(buffer, prevX, prevAmplitude-offset, 1.0D, prevUBack, reverse ? minVBack : maxVBack);
			buf(buffer, prevX, prevAmplitude-offset, 0.0D, prevUBack, reverse ? maxVBack : minVBack);
			tessellator.draw();

			// Top
			buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
			buf(buffer, prevX, prevAmplitude, 0.0D, prevUFront, reverse ? maxVFront : minVFront);
			buf(buffer, prevX, prevAmplitude, 1.0D, prevUFront, reverse ? minVFront : maxVFront);
			buf(buffer, x, amplitude, 1.0D, uFront, reverse ? minVFront : maxVFront);
			buf(buffer, x, amplitude, 0.0D, uFront, reverse ? maxVFront : minVFront);
			tessellator.draw();
		}
	}

	@Override
	public void setLivingAnimations(EntityLivingBase entityLiving, float par2, float par3, float partialTicks) {
		EntityEagleRay ray = (EntityEagleRay) entityLiving;
		float[] prevWingAmplitudes = ray.getPrevWingAmplitudes();
		float[] wingAmplitudes = ray.getWingAmplitudes();

		for (int i = 1; i < EntityEagleRay.WING_JOINTS; i++) {
			float prevWingAmplitude = prevWingAmplitudes[i];
			float wingAmplitude = wingAmplitudes[i];
			interpolatedWingAmplitudes[i] = prevWingAmplitude + partialTicks*(wingAmplitude - prevWingAmplitude);
		}
	}
}
