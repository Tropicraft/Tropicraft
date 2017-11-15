package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelDolphin;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityDolphin;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public class RenderDolphin extends RenderTropicraftWaterMob {

	public RenderDolphin(ModelBase modelbase, float f) {
		super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
	}
    
    @Override
	protected ResourceLocation getEntityTexture(EntityTropicraftWaterBase entity) {
		return TropicraftRenderUtils.bindTextureEntity(entity.getTexture());
	}

	@Override
	public void doRender(EntityTropicraftWaterBase entityliving, double d, double d1, double d2, float f, float f1) {
		entityliving.outOfWaterTime = 0;
		this.renderWaterMob((EntityTropicraftWaterBase) entityliving, d, d1, d2, f1);
	}

	@Override
	protected void preRenderCallback(EntityTropicraftWaterBase entityliving, float f) {
		preRenderScale((EntityDolphin) entityliving, f);
	}

	protected void preRenderScale(EntityDolphin entitymarlin, float f) {
		GlStateManager.scale(1.2f, 1.2f, 1.2f);
	}
}