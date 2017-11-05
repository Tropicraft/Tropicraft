package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.underdasea.EntityTurtleEgg;

public class RenderTurtleEgg extends RenderLiving<EntityTurtleEgg> {

	public RenderTurtleEgg(ModelBase modelbase, float f) {
		super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
		this.shadowOpaque = 0.5f;
		this.shadowSize = 0.2f;

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTurtleEgg entity) {
		return TropicraftRenderUtils.bindTextureEntity("turtle/eggText");
	}

	@Override
	public void doRender(EntityTurtleEgg entityliving, double d, double d1, double d2, float f, float f1) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(d, d1, d2);
		GlStateManager.scale(0.5f, 0.65f, 0.5f);
		super.doRender(entityliving, 0, 0, 0, f, f1);
		GlStateManager.popMatrix();

	}

}