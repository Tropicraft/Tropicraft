package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3f;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;

public class PlayerHeadpieceRenderer extends BipedModel<LivingEntity> {
	private int textureIndex;
	private final double xOffset, yOffset;
	protected TropicraftSpecialRenderHelper renderer;
	
	public PlayerHeadpieceRenderer(final int textureIndex) {
		this(textureIndex, 0, 0);
	}

	public PlayerHeadpieceRenderer(final int textureIndex, final double xOffset, final double yOffset) {
		super(0);
		this.textureIndex = textureIndex;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		renderer = new TropicraftSpecialRenderHelper();
	}

	@Override
	public void renderToBuffer(MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		stack.pushPose();

		if (crouching) {
			stack.translate(0, 0.25f, 0);
		}

		// Set head rotation to mask
		stack.mulPose(Vector3f.YP.rotation(head.yRot));
		stack.mulPose(Vector3f.XP.rotation(head.xRot));

		// Flip mask to face away from the player
		stack.mulPose(Vector3f.YP.rotationDegrees(180));

		// put it in the middle in front of the face
		stack.translate(0.0F - xOffset, 0.112f + 0.0625f - yOffset, 0.2501F);

   		// renderMask handles the rendering of the mask model, but it doesn't set the texture.
		// Setting the texture is handled in the item class.
		renderer.renderMask(stack, bufferIn, this.textureIndex, packedLightIn, packedOverlayIn);
		
		stack.popPose();
	}
	
	
}
