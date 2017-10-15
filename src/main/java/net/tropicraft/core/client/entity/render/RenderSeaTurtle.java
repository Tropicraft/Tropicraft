package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelSeaTurtle;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public class RenderSeaTurtle extends RenderTropicraftWaterMob {

    public ModelSeaTurtle turtle;
    
    public RenderSeaTurtle(ModelBase modelbase, float f) {
        super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
        turtle = (ModelSeaTurtle) modelbase;
    }

	@Override
	public void doRender(EntityTropicraftWaterBase entity, double x, double y, double z, float entityYaw,	float partialTicks) {
		this.renderWaterMob(entity, x, y, z, partialTicks, 0f, -25f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTropicraftWaterBase entity) {
		return TropicraftRenderUtils.bindTextureEntity("turtle/seaTurtle");
	}

}
