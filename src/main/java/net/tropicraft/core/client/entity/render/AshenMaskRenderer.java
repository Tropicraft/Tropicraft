package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.common.entity.placeable.AshenMaskEntity;

public class AshenMaskRenderer extends EntityRenderer<AshenMaskEntity> {
	protected TropicraftSpecialRenderHelper mask;

	public AshenMaskRenderer(final EntityRendererManager manager) {
		super(manager);
		shadowRadius = 0.5F;
		shadowStrength  = 0.5f;
		mask = new TropicraftSpecialRenderHelper();
	}

	@Override
	public ResourceLocation getTextureLocation(final AshenMaskEntity entity) {
		return TropicraftRenderUtils.getTextureEntity("ashen/mask");
	}

	@Override
	public void render(AshenMaskEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packedLightIn) {
		stack.pushPose();

		IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.entityCutout(getTextureLocation(entity)));
	//	mask.render(stack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
//		GlStateManager.pushMatrix();
	//	bindEntityTexture(entity);

		stack.mulPose(Vector3f.XN.rotationDegrees(90));

		mask.renderMask(stack, ivertexbuilder, entity.getMaskType(), packedLightIn, OverlayTexture.NO_OVERLAY);

		stack.popPose();
	}

}
