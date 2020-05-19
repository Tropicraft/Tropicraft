package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.tropicraft.core.common.entity.underdasea.EagleRayEntity;

public class EagleRayModel extends SegmentedModel<EagleRayEntity> {
	/**
	 * Wing joint amplitudes, linearly interpolated between previous tick and this tick using partialTicks.
	 */
	private float[] interpolatedWingAmplitudes = new float[EagleRayEntity.WING_JOINTS];

	private ModelRenderer body;

	public EagleRayModel() {
		textureWidth = 128;
		textureHeight = 64;

		body = new ModelRenderer(this, 32, 0);
		body.addBox(-2F, 0F, 0F, 5, 3, 32);
		body.setRotationPoint(0F, 0F, -8F);
		body.setTextureSize(128, 64);
		body.mirror = true;
	}

	@Override
	public void setRotationAngles(EagleRayEntity eagleRay, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(body);
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		// TODO use bufferIn
		renderWings();
		renderTailSimple();
	}

	private void renderTailSimple() {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		float minU = 0.75f;
		float maxU = 1.0f;
		float minV = 0.0f;
		float maxV = 0.5f;

		RenderSystem.pushMatrix();
		RenderSystem.translatef(0.55f, 0f, 1.5f);
		RenderSystem.rotatef(-90f, 0f, 1f, 0f);
		RenderSystem.scalef(1.5f, 1f, 1f);

		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(0, 0, 0).tex(minU, minV).endVertex();
		buffer.pos(0, 0, 1).tex(minU, maxV).endVertex();
		buffer.pos(1, 0, 1).tex(maxU, maxV).endVertex();
		buffer.pos(1, 0, 0).tex(maxU, minV).endVertex();
		tessellator.draw();

		RenderSystem.popMatrix();
	}

	private void renderWings() {
		RenderSystem.disableLighting();
		RenderSystem.pushMatrix();
		RenderSystem.scalef(2f, 0.5f, 2f);
		RenderSystem.translatef(0.1f, 0f, -0.25f);
		renderWing(false);
		RenderSystem.rotatef(180f, 0f, 1f, 0f);
		RenderSystem.translatef(0.10f, 0f, -1f);
		renderWing(true);
		RenderSystem.popMatrix();
		RenderSystem.enableLighting();
	}

	private void buf(BufferBuilder buffer, double x, double y, double z, float u, float v) {
		buffer.pos(x, y, z).tex(u, v).endVertex();
	}

	private void renderWing(boolean reverse) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		float minUFront = 0f;
		float maxUFront = 0.25f;
		float minVFront = 0f;
		float maxVFront = 0.5f;

		float minUBack = 0f;
		float maxUBack = 0.25f;
		float minVBack = 0.5f;
		float maxVBack = 1f;

		for (int i = 1; i < EagleRayEntity.WING_JOINTS; i++) {
			float prevAmplitude = interpolatedWingAmplitudes[i - 1];
			float amplitude = interpolatedWingAmplitudes[i];

			float prevX = (i-1)/(EagleRayEntity.WING_JOINTS - 1f);
			float x = i/(EagleRayEntity.WING_JOINTS - 1f);

			float prevUFront = minUFront + (maxUFront-minUFront) * prevX;
			float uFront = minUFront + (maxUFront-minUFront) * x;
			float prevUBack = minUBack + (maxUBack-minUBack) * prevX;
			float uBack = minUBack + (maxUBack-minUBack) * x;

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
	public void setLivingAnimations(EagleRayEntity ray, float par2, float par3, float partialTicks) {
		final float[] prevWingAmplitudes = ray.getPrevWingAmplitudes();
		final float[] wingAmplitudes = ray.getWingAmplitudes();

		for (int i = 1; i < EagleRayEntity.WING_JOINTS; i++) {
			final float prevWingAmplitude = prevWingAmplitudes[i];
			final float wingAmplitude = wingAmplitudes[i];
			interpolatedWingAmplitudes[i] = prevWingAmplitude + partialTicks * (wingAmplitude - prevWingAmplitude);
		}
	}
}
