package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.hostile.EntityLostMask;

public class RenderLostMask extends Render<EntityLostMask> {

	protected TropicraftSpecialRenderHelper mask;

	public RenderLostMask() {
		super(Minecraft.getMinecraft().getRenderManager());
		shadowSize = 0.5F;
		this.shadowOpaque  = 0.5f;
		mask = new TropicraftSpecialRenderHelper();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLostMask entity) {
		return TropicraftRenderUtils.bindTextureEntity("ashen/mask");
	}

	public void renderLostMask(EntityLostMask entity, double d, double d1, double d2,
			float f, float f1) {

		
		GlStateManager.pushMatrix();
		//loadTexture("/mods/TropicraftMod/textures/entities/mask.png");
		this.bindEntityTexture(entity);

		GlStateManager.translate(d, d1, d2);
		//GL11.glTranslatef((float) d-0.25f, (float) d1, (float) d2+0.5f);
		GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);

		if (!entity.onGround && !entity.checkForWater(0) && !entity.checkForWater(-1) ) {
			int[] a = entity.getRotator();

			GlStateManager.rotate(a[0] + f1, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(a[1] + f1, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(a[2] + f1, 0.0F, 0.0F, 1.0F);
		}
		else{
			GlStateManager.rotate(270, 1, 0, 0);
			GlStateManager.rotate(180, 0, 0, 1);
		}

		mask.renderMask(entity.getType());

		GlStateManager.popMatrix();
	}

	@Override
	public void doRender(EntityLostMask entity, double d, double d1, double d2, float f, float f1) {
		renderLostMask((EntityLostMask) entity, d, d1, d2, f, f1);
	}

}
