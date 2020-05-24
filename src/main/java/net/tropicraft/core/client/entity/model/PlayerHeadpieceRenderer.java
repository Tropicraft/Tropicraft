package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
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
	
	private float rotationYaw;
	private float rotationPitch;
	private boolean sneaking;

	@Override
	public void setRotationAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.rotationYaw = netHeadYaw;
		this.rotationPitch = headPitch;
		this.sneaking = entityIn.isCrouching();
	}

	@Override
	public void render(MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		stack.push();

		if (sneaking) {
			stack.translate(0, 0.25f, 0);
		}

		// Set head rotation to mask
		stack.rotate(Vector3f.YP.rotationDegrees(rotationYaw));
		stack.rotate(Vector3f.XP.rotationDegrees(rotationPitch));

		// Flip mask to face away from the player
		stack.rotate(Vector3f.YP.rotationDegrees(180));

		// put it in the middle in front of the face
		stack.translate(0.0F - xOffset, 0.112f + 0.0625f - yOffset, 0.2501F);

   		// renderMask handles the rendering of the mask model, but it doesn't set the texture.
		// Setting the texture is handled in the item class.
		renderer.renderMask(stack, bufferIn, this.textureIndex, packedLightIn, packedOverlayIn);
		
		stack.pop();
	}
	
	
}