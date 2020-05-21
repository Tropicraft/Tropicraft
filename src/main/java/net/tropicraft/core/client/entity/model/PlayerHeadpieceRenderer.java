package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;

public class PlayerHeadpieceRenderer extends BipedModel<LivingEntity> {
	private int textureIndex;
	private ResourceLocation texPath;
	protected TropicraftSpecialRenderHelper renderer;

	public PlayerHeadpieceRenderer(final int modelSize, final int textureIndex, final ResourceLocation texPath) {
		super(modelSize);
		this.texPath = texPath;
		this.textureIndex = textureIndex;
		renderer = new TropicraftSpecialRenderHelper();
	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		// TODO
//		float rotationYaw = f3;
//
//		if (entity instanceof ArmorStandEntity) {
//			rotationYaw = entity.rotationYawHead;
//		}
//
//		setRotationAngles(entity, f0, f1, f2, rotationYaw, f4, f5);
//
//		GlStateManager.pushMatrix();
//		TropicraftRenderUtils.bindTexture(texPath);
//
//		if (entity.isSneaking()) {
//			GlStateManager.translatef(0, 0.25f, 0);
//		}
//
//		// Set head rotation to mask
//		GlStateManager.rotatef(rotationYaw, 0, 1, 0);
//		GlStateManager.rotatef(f4, 1, 0, 0);
//
//		// Flip mask to face away from the player
//		GlStateManager.rotatef(180, 0, 1, 0);
//
//		// put it in the middle in front of the face, eyeholes at (Steve's) eye height
//		GlStateManager.translatef(0.0F, 0.16F, 0.3F);
//
//   		// renderMask handles the rendering of the mask model, but it doesn't set the texture.
//		// Setting the texture is handled in the item class.
//		renderer.renderMask(this.textureIndex);
//
//		GlStateManager.popMatrix();
	}
}