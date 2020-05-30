package net.tropicraft.core.client.entity.render;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.common.entity.underdasea.StarfishEntity;
import net.tropicraft.core.common.entity.underdasea.StarfishType;

public class StarfishRenderer extends EntityRenderer<StarfishEntity> {

	/**
	 * Amount freshly hatched starfish are scaled down while rendering.
	 */
	public static final float BABY_RENDER_SCALE = 0.25f;

	/**
	 * Amount mature starfish are scaled down while rendering.
	 */
	public static final float ADULT_RENDER_SCALE = 1f;

	public StarfishRenderer(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void render(StarfishEntity starfish, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packedLightIn) {
		StarfishType type = starfish.getStarfishType();

		float f = 0f;
		float f1 = 1f;
		float f2 = 0f;
		float f3 = 1f;
		float f1shifted = 1;
		float f3shifted = 1;

		Tessellator tessellator = Tessellator.getInstance();

		stack.push();
		stack.translate(-0.5, 0, -0.5);
		stack.rotate(Vector3f.XP.rotationDegrees(90));

		float growthProgress = starfish.getGrowthProgress();
		float scale = BABY_RENDER_SCALE + growthProgress*(ADULT_RENDER_SCALE-BABY_RENDER_SCALE);

		stack.scale(scale, scale, scale);


		for (int i = 0; i < type.getLayerCount(); i++) {
			// TODO static-ify to prevent allocation every render tick
			IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEntityCutout(new ResourceLocation(type.getTexturePaths().get(i))));
			if (starfish.hurtTime > 0) {
				GlStateManager.color4f(1f, 0f, 0f, 1f);
			}
			popper(tessellator, f1, f2, f, f3, f1shifted, f3shifted, type.getLayerHeights()[i], stack, ivertexbuilder);
			GlStateManager.translatef(0f, 0f, -type.getLayerHeights()[i]);
		}

		stack.pop();
	}

	@Nullable
	@Override
	public ResourceLocation getEntityTexture(StarfishEntity starfishEntity) {
		return null; // Custom setting this in the render loop
	}

	private void buf(IVertexBuilder buffer, double x, double y, double z, float u, float v) {
		buffer.pos(x, y, z).tex(u, v).endVertex();
	}

	private void popper(Tessellator tessellator, float f, float f1, float f2, float f3, float f1shifted, float f3shifted, float layerHeight, MatrixStack stack, IVertexBuilder buffer) {
		float f4 = 1.0F;
		float f5 = layerHeight;

//		BufferBuilder vertexbuffer = tessellator.getBuffer();
//		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.normal3f(0.0F, 0.0F, 1.0F);

		//vertexbuffer.pos(0D, 0D, 0D).tex((double)f, (double)f3shifted).endVertex();
		buf(buffer, 0.0D, 0.0D, 0.0D, f, f3shifted);
		buf(buffer, f4, 0.0D, 0.0D, f2, f3shifted);
		buf(buffer, f4, 1.0D, 0.0D, f2, f1shifted);
		buf(buffer, 0.0D, 1.0D, 0.0D, f, f1shifted);
		//tessellator.draw();

	//	vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.normal3f(0.0F, 0.0F, -1F);
		buf(buffer, 0.0D, 1.0D, 0.0F - f5, f, f1);
		buf(buffer, f4, 1.0D, 0.0F - f5, f2, f1);
		buf(buffer, f4, 0.0D, 0.0F - f5, f2, f3);
		buf(buffer, 0.0D, 0.0D, 0.0F - f5, f, f3);
	//	tessellator.draw();

	//	vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.normal3f(-1F, 0.0F, 0.0F);
		for (int i = 0; i < 32; i++) {
			float f6 = (float) i / 32F;
			float f10 = (f + (f2 - f) * f6) - 0.001953125F;
			float f14 = f4 * f6;
			buf(buffer, f14, 0.0D, 0.0F - f5, f10, f3);
			buf(buffer, f14, 0.0D, 0.0D, f10, f3);
			buf(buffer, f14, 1.0D, 0.0D, f10, f1);
			buf(buffer, f14, 1.0D, 0.0F - f5, f10, f1);
		}
	//	tessellator.draw();

	//	vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.normal3f(1.0F, 0.0F, 0.0F);
		for (int j = 0; j < 32; j++) {
			float f7 = (float) j / 32F;
			float f11 = (f + (f2 - f) * f7) - 0.001953125F;
			float f15 = f4 * f7 + 0.03125F;
			buf(buffer, f15, 1.0D, 0.0F - f5, f11, f1);
			buf(buffer, f15, 1.0D, 0.0D, f11, f1);
			buf(buffer, f15, 0.0D, 0.0D, f11, f3);
			buf(buffer, f15, 0.0D, 0.0F - f5, f11, f3);
		}
	//	tessellator.draw();

	//	vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.normal3f(0.0F, 1.0F, 0.0F);
		for (int k = 0; k < 32; k++) {
			float f8 = (float) k / 32F;
			float f12 = (f3 + (f1 - f3) * f8) - 0.001953125F;
			float f16 = f4 * f8 + 0.03125F;
			buf(buffer, 0.0D, f16, 0.0D, f, f12);
			buf(buffer, f4, f16, 0.0D, f2, f12);
			buf(buffer, f4, f16, 0.0F - f5, f2, f12);
			buf(buffer, 0.0D, f16, 0.0F - f5, f, f12);
		}
	//	tessellator.draw();

	//	vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.normal3f(0.0F, -1F, 0.0F);
		for (int l = 0; l < 32; l++) {
			float f9 = (float) l / 32F;
			float f13 = (f3 + (f1 - f3) * f9) - 0.001953125F;
			float f17 = f4 * f9;
			buf(buffer, f4, f17, 0.0D, f2, f13);
			buf(buffer, 0.0D, f17, 0.0D, f, f13);
			buf(buffer, 0.0D, f17, 0.0F - f5, f, f13);
			buf(buffer, f4, f17, 0.0F - f5, f2, f13);
		}

	//	tessellator.draw();
	}
}