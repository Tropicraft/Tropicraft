package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelSeahorse;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public class RenderSeahorse extends RenderTropicraftWaterMob {

	private ModelSeahorse modelSeahorse;

	public RenderSeahorse(ModelBase model, float shadowSize) {
		super(Minecraft.getMinecraft().getRenderManager(), model, shadowSize);
		modelSeahorse = (ModelSeahorse) model;
	}

	@Override
	public void doRender(EntityTropicraftWaterBase par1EntityLiving, double x, double y, double z, float yaw,
			float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y - 2.5f, z);
		GlStateManager.rotate(90, 0f, 1f, 0f);

		float swimYaw = (par1EntityLiving.prevSwimYaw)
				+ ((par1EntityLiving.swimYaw) - (par1EntityLiving.prevSwimYaw)) * partialTicks;
		String n = par1EntityLiving.getCustomNameTag();
		par1EntityLiving.setCustomNameTag("");
		super.doRender(par1EntityLiving, 0, 0, 0, swimYaw, partialTicks);
		GlStateManager.popMatrix();
		par1EntityLiving.setCustomNameTag(n);
		if (Minecraft.getMinecraft().pointedEntity != null && par1EntityLiving.getCustomNameTag().length() > 0) {
			if (Minecraft.getMinecraft().pointedEntity.equals(par1EntityLiving)) {
				this.renderEntityName(par1EntityLiving, x, y, z, par1EntityLiving.getCustomNameTag(), 7d);
			}
		}

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTropicraftWaterBase entity) {
		return TropicraftRenderUtils.getTextureEntity(String.format("seahorse/%s", entity.getTexture()));
	}
}