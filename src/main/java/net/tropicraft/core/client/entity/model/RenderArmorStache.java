package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.tropicraft.core.client.entity.render.TropicraftSpecialRenderHelper;

public class RenderArmorStache extends ModelBiped {

	private int stacheIndex;
	protected TropicraftSpecialRenderHelper stacheRenderer;

	public RenderArmorStache(int maskType){
		super();
		this.stacheIndex = maskType;
		stacheRenderer = new TropicraftSpecialRenderHelper();
	}

	@Override
	public void render(Entity entity, float f0, float f1, float f2, float f3, float f4, float f5)
	{	
		EntityLivingBase livingEntity = (EntityLivingBase)entity;

		float rotationYaw = f3;

		if (livingEntity instanceof EntityArmorStand) {
			rotationYaw = livingEntity.rotationYawHead;
		}

		/*
			Don't call super class' render method, let the renderMask function handle it!
			SetRotationAngles might me necessary, but I'm not sure.
		 */
		this.setRotationAngles(f0, f1, f2, rotationYaw, f4, f5, entity);

		GlStateManager.pushMatrix();

		if (entity.isSneaking()) {
			GlStateManager.translate(0, 0.25f, 0);
		}

		// Set head rotation to mask
		GlStateManager.rotate(rotationYaw, 0, 1, 0);
		GlStateManager.rotate(f4, 1, 0, 0);

		// Flip mask to face away from the player
		GlStateManager.rotate(180, 0, 1, 0);

		// put it in the middle in front of the face, eyeholes at (Steve's) eye height
		GlStateManager.translate(0.0F, 0.16F, 0.3F);

		/*
    		renderMask handles the rendering of the mask model, but it doesn't set the texture.
    		Setting the texture is handled in the item class.
		 */
		stacheRenderer.renderMask(this.stacheIndex);

		GlStateManager.popMatrix();
	}
}