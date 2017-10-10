package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelEagleRay;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public class RenderEagleRay extends RenderTropicraftWaterMob {

	public RenderEagleRay() {
		super(Minecraft.getMinecraft().getRenderManager(), new ModelEagleRay(), 1f);
	}


	@Override
	public void doRender(EntityTropicraftWaterBase entity, double x, double y, double z, float entityYaw, float partialTicks) {
		
		GlStateManager.pushMatrix();

		GlStateManager.disableCull();
		
		GlStateManager.translate(4, 0f, 4f);

	//	this.mainModel = new ModelEagleRay();
		this.renderWaterMob(entity, x-4, y-1, z-4, partialTicks);
		
		GlStateManager.popMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTropicraftWaterBase entity) {
		return TropicraftRenderUtils.bindTextureEntity("ray/eagleray");
	}

}