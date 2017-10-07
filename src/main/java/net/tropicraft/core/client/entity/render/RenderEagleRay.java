package net.tropicraft.core.client.entity.render;

import static org.lwjgl.opengl.GL11.glTranslatef;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
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
		
		GL11.glPushMatrix();
		//glTranslatef(0f, 1.25f, -1f);
		
		GL11.glDisable(2884 /*GL_CULL_FACE*/);
		
		

		this.renderWaterMob(entity, x, y-1, z, partialTicks);
		
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTropicraftWaterBase entity) {
		// TODO Auto-generated method stub
		return TropicraftRenderUtils.bindTextureEntity("ray/eagleray");
	}

}