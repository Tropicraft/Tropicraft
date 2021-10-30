package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.EggModel;
import net.tropicraft.core.common.entity.egg.EggEntity;

public class EggRenderer extends LivingRenderer<EggEntity, EggModel> {

	public EggRenderer(final EntityRendererManager rendererManager) {
		super(rendererManager, new EggModel(), 1f);
		shadowStrength = 0.5f;
	}

	@Override
	public void render(EggEntity egg, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		stack.pushPose();
		if (egg.shouldEggRenderFlat()) {
			shadowRadius = 0.0f;
			stack.translate(0, 0.05, 0);
			drawFlatEgg(egg, partialTicks, stack, bufferIn, packedLightIn);
		} else {
			shadowRadius = 0.2f;
			stack.scale(0.5f, 0.5f, 0.5f);
			super.render(egg, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
		}
		stack.popPose();
	}

	public void drawFlatEgg(EggEntity ent, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		stack.pushPose();

		stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));

		stack.scale(0.25f, 0.25f, 0.25f);

		final IVertexBuilder buffer = TropicraftRenderUtils.getEntityCutoutBuilder(bufferIn, getTextureLocation(ent));
		int overlay = getOverlayCoords(ent, getWhiteOverlayProgress(ent, partialTicks));
		
		Matrix4f mat = stack.last().pose();
		Matrix3f normal = new Matrix3f();
		normal.setIdentity();
		TropicraftSpecialRenderHelper.vertex(buffer, mat, normal, -.5, -.25, 0, 1, 1, 1, 1, 0, 1, Direction.UP, packedLightIn, overlay);
		TropicraftSpecialRenderHelper.vertex(buffer, mat, normal,  .5, -.25, 0, 1, 1, 1, 1, 1, 1, Direction.UP, packedLightIn, overlay);
		TropicraftSpecialRenderHelper.vertex(buffer, mat, normal,  .5,  .75, 0, 1, 1, 1, 1, 1, 0, Direction.UP, packedLightIn, overlay);
		TropicraftSpecialRenderHelper.vertex(buffer, mat, normal, -.5,  .75, 0, 1, 1, 1, 1, 0, 0, Direction.UP, packedLightIn, overlay);

		stack.popPose();
	}
	
	@Override
	protected boolean shouldShowName(EggEntity entity) {
		return entity.hasCustomName();
	}
	
	@Override
	public ResourceLocation getTextureLocation(EggEntity entity) {
		return TropicraftRenderUtils.bindTextureEntity(entity.getEggTexture());
	}
}
