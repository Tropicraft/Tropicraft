package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix3f;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.render.EagleRayRenderer;
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
		renderWings(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		renderTailSimple(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}

	private void renderTailSimple(MatrixStack stack, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		float minU = 0.75f;
		float maxU = 1.0f;
		float minV = 0.0f;
		float maxV = 0.5f;

		stack.push();
		stack.translate(0.55f, 0f, 1.5f);
		stack.rotate(Vector3f.YP.rotationDegrees(-90f));
		stack.scale(1.5f, 1f, 1f);
		vertex(buffer, stack.getLast().getMatrix(), stack.getLast().getNormal(), 0, 0, 255, 255, 255, minU, minV, packedLightIn);
		vertex(buffer, stack.getLast().getMatrix(), stack.getLast().getNormal(), 0, 0, 255, 255, 255, minU, maxV, packedLightIn);
		vertex(buffer, stack.getLast().getMatrix(), stack.getLast().getNormal(), 0, 0, 255, 255, 255, maxU, maxV, packedLightIn);
		vertex(buffer, stack.getLast().getMatrix(), stack.getLast().getNormal(), 0, 0, 255, 255, 255, maxU, minV, packedLightIn);
		stack.pop();
	}

	private static void vertex(IVertexBuilder bufferIn, Matrix4f matrixIn, Matrix3f matrixNormalIn, float x, float y, int red, int green, int blue, float texU, float texV, int packedLight) {
		bufferIn.pos(matrixIn, x, y, 0.0F).color(red, green, blue, 128).tex(texU, texV).overlay(OverlayTexture.NO_OVERLAY).lightmap(packedLight).normal(matrixNormalIn, 0.0F, 1.0F, 0.0F).endVertex();
	}

	private void renderWings(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		//RenderSystem.disableLighting();
		matrixStackIn.push();
		matrixStackIn.scale(2f, 0.5f, 2f);
		matrixStackIn.translate(0.1f, 0f, -0.25f);
		renderWing(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha, false);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180f));
		matrixStackIn.translate(0.10f, 0f, -1f);
		renderWing(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha, true);
		matrixStackIn.pop();
		//RenderSystem.enableLighting();
	}

	private void renderWing(MatrixStack stack, IVertexBuilder buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, boolean reverse) {
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
			final Matrix4f matrix = stack.getLast().getMatrix();
			final Matrix3f normal = stack.getLast().getNormal();

			vertex(buffer, matrix, normal, x, amplitude-offset, 255, 255, 255, uBack, reverse ? maxVBack : minVBack, packedLightIn);
			vertex(buffer, matrix, normal, x, amplitude-offset, 255, 255, 255, uBack, reverse ? minVBack : maxVBack, packedLightIn);
			vertex(buffer, matrix, normal, prevX, prevAmplitude-offset, 255, 255, 255, prevUBack, reverse ? minVBack : maxVBack, packedLightIn);
			vertex(buffer, matrix, normal, prevX, prevAmplitude-offset, 255, 255, 255, prevUBack, reverse ? maxVBack : minVBack, packedLightIn);

			// Top
			vertex(buffer, matrix, normal, prevX, prevAmplitude, 255, 255, 255, prevUFront, reverse ? maxVFront : minVFront, packedLightIn);
			vertex(buffer, matrix, normal,x, amplitude, 255, 255, 255, prevUFront, reverse ? minVFront : maxVFront, packedLightIn);
			vertex(buffer, matrix, normal, x, amplitude, 255, 255, 255, prevUFront, reverse ? minVFront : maxVFront, packedLightIn);
			vertex(buffer, matrix, normal, x, amplitude, 255, 255, 255, uFront, reverse ? maxVFront : minVFront, packedLightIn);
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
