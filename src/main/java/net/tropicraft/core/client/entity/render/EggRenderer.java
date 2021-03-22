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
		shadowOpaque = 0.5f;
	}

	@Override
	public void render(EggEntity egg, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		stack.push();
		if (egg.shouldEggRenderFlat()) {
			shadowSize = 0.0f;
			stack.translate(0, 0.05, 0);
			drawFlatEgg(egg, partialTicks, stack, bufferIn, packedLightIn);
		} else {
			shadowSize = 0.2f;
			stack.scale(0.5f, 0.5f, 0.5f);
			super.render(egg, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
		}
		stack.pop();
	}

	public void drawFlatEgg(EggEntity ent, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		stack.push();

		stack.rotate(this.renderManager.getCameraOrientation());
		stack.rotate(Vector3f.YP.rotationDegrees(180.0F));

		stack.scale(0.25f, 0.25f, 0.25f);

		final IVertexBuilder buffer = TropicraftRenderUtils.getEntityCutoutBuilder(bufferIn, getEntityTexture(ent));
		int overlay = getPackedOverlay(ent, getOverlayProgress(ent, partialTicks));
		
		Matrix4f mat = stack.getLast().getMatrix();
		Matrix3f normal = new Matrix3f();
		normal.setIdentity();
		TropicraftSpecialRenderHelper.vertex(buffer, mat, normal, -.5, -.25, 0, 1, 1, 1, 1, 0, 1, Direction.UP, packedLightIn, overlay);
		TropicraftSpecialRenderHelper.vertex(buffer, mat, normal,  .5, -.25, 0, 1, 1, 1, 1, 1, 1, Direction.UP, packedLightIn, overlay);
		TropicraftSpecialRenderHelper.vertex(buffer, mat, normal,  .5,  .75, 0, 1, 1, 1, 1, 1, 0, Direction.UP, packedLightIn, overlay);
		TropicraftSpecialRenderHelper.vertex(buffer, mat, normal, -.5,  .75, 0, 1, 1, 1, 1, 0, 0, Direction.UP, packedLightIn, overlay);

		stack.pop();
	}
	
	@Override
	protected boolean canRenderName(EggEntity entity) {
		return entity.hasCustomName();
	}
	
	@Override
	public ResourceLocation getEntityTexture(EggEntity entity) {
		return TropicraftRenderUtils.bindTextureEntity(entity.getEggTexture());
	}
}
