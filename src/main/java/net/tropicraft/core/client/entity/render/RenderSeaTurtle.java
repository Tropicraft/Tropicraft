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
			
			float scale = 0.3f;
			if(entity.ticksExisted < 30) {
				this.shadowOpaque = 0.5f;
				this.shadowSize = 0.2f+(((float)entity.ticksExisted/4000));
				if(this.shadowSize > 0.5f) {
					this.shadowSize = 0.5f;
				}
			}else {
				scale = 0.3f+(((float)entity.ticksExisted/4000));
				if(scale > 1f) {
					scale = 1f;
				}
			}
			if(turtle.isMature()) {
				scale = 1f;
			}
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			GlStateManager.scale(scale, scale, scale);

			if(turtle.isInWater() || turtle.getPassengers().size() > 0) {
				this.renderWaterMob(entity, 0, 0, 0, partialTicks, 0f, 0f);
			}else {
				super.doRender(entity, 0, 0, 0, 0, partialTicks);

			}
			GlStateManager.popMatrix();


		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTropicraftWaterBase entity) {
		return TropicraftRenderUtils.getTextureEntity(String.format("turtle/%s", entity.getTexture()));
	}

}
