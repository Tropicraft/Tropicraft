package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelSeaTurtle;
import net.tropicraft.core.common.entity.underdasea.EntitySeaTurtle;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public class RenderSeaTurtle extends RenderTropicraftWaterMob {

    public ModelSeaTurtle turtle;
    
    public RenderSeaTurtle(ModelBase modelbase, float f) {
        super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
        turtle = (ModelSeaTurtle) modelbase;
        this.shadowSize = 0.5f;
		this.shadowOpaque = 0.5f;
    }

	@Override
	public void doRender(EntityTropicraftWaterBase entity, double x, double y, double z, float entityYaw,	float partialTicks) {
		if(entity instanceof EntitySeaTurtle) {
			EntitySeaTurtle turtle = (EntitySeaTurtle) entity;
			
			float scale = 0.5f;
			GlStateManager.pushMatrix();
		//	this.prepareScale(entity, partialTicks);

			//GlStateManager.scale(scale, scale, scale);
			GlStateManager.translate(x, y, z);
			if(turtle.isInWater()) {
				this.renderWaterMob(entity, 0, 0, 0, partialTicks, 0f, 0f);
			}else {
				GlStateManager.rotate(105f, 0f, 1f, 0f);

				super.doRender(entity, 0, 0, 0, 0, partialTicks);
			}
			GlStateManager.popMatrix();


		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTropicraftWaterBase entity) {
		return TropicraftRenderUtils.bindTextureEntity("turtle/seaTurtle");
	}

}
