package net.tropicraft.core.client.entity.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelSeahorse;
import net.tropicraft.core.common.entity.underdasea.EntitySeahorse;

public class RenderSeahorse extends RenderLiving<EntitySeahorse> {

	private ModelSeahorse modelSeahorse;

	public RenderSeahorse(ModelBase model, float shadowSize) {
		super(Minecraft.getMinecraft().getRenderManager(), model, shadowSize);
		modelSeahorse = (ModelSeahorse)model;
	}
	
//	@Override
//    public void doRender(EntitySeahorse par1EntityLiving, double x, double y, double z, float yaw, float partialTicks) {
//        renderSeahorse((EntitySeahorse)par1EntityLiving, x, y, z, yaw, partialTicks);
//    }
//	
//	private void renderSeahorse(EntitySeahorse seahorse, double x, double y, double z, float yaw, float partialTicks) {
//		//GL11.glPushMatrix();
//		//GL11.glTranslated(x, y - 2.2, z);
//		//GL11.glRotatef(90F, 0, 1, 0);
//		super.doRender(seahorse, x, y, z, yaw, partialTicks);
//		//GL11.glPopMatrix();
//	}

	@Override
	protected ResourceLocation getEntityTexture(EntitySeahorse entity) {
		EntitySeahorse seahorse = null;
		
		if (entity instanceof EntitySeahorse)
			seahorse = (EntitySeahorse)entity;
		
		if (seahorse == null)
			return TropicraftRenderUtils.getTextureEntity("seahorse/razz");
		else
			return TropicraftRenderUtils.getTextureEntity(String.format("seahorse/%s", seahorse.getColorName()));
	}
}