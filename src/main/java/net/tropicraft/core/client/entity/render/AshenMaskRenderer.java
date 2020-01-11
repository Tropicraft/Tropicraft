package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.common.entity.placeable.AshenMaskEntity;

public class AshenMaskRenderer extends EntityRenderer<AshenMaskEntity> {
	protected TropicraftSpecialRenderHelper mask;

	public AshenMaskRenderer(final EntityRendererManager manager) {
		super(manager);
		shadowSize = 0.5F;
		shadowOpaque  = 0.5f;
		mask = new TropicraftSpecialRenderHelper();
	}

	@Override
	protected ResourceLocation getEntityTexture(AshenMaskEntity entity) {
		return TropicraftRenderUtils.bindTextureEntity("ashen/mask");
	}

	@Override
	public void doRender(AshenMaskEntity entity, double x, double y, double z, float yaw, float pitch) {
		GlStateManager.pushMatrix();
		bindEntityTexture(entity);

		GlStateManager.translated(x, y, z);
		//GL11.glTranslatef((float) d-0.25f, (float) y, (float) z+0.5f);
		GlStateManager.rotatef(yaw, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotatef(-90, 1.0F, 0.0F, 0.0F);

//		if (!entity.onGround && !entity.isInWater() ) {
//			int[] a = entity.getRotator();
//
//			GlStateManager.rotatef(a[0] + pitch, 1.0F, 0.0F, 0.0F);
//			GlStateManager.rotatef(a[1] + pitch, 0.0F, 1.0F, 0.0F);
//			GlStateManager.rotatef(a[2] + pitch, 0.0F, 0.0F, 1.0F);
//		}
//		else{
//			GlStateManager.rotatef(270, 1, 0, 0);
//			GlStateManager.rotatef(180, 0, 0, 1);
//		}

		mask.renderMask(entity.getMaskType());

		GlStateManager.popMatrix();
	}

}
