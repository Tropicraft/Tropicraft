package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityShark;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public class RenderShark extends RenderTropicraftWaterMob {

	public RenderShark(ModelBase modelbase, float f) {
		super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
	}
    
    @Override
	protected ResourceLocation getEntityTexture(EntityTropicraftWaterBase entity) {
    		return TropicraftRenderUtils.getTextureEntity(String.format("shark/%s", entity.getTexture()));
    	}

	@Override
	public void doRender(EntityTropicraftWaterBase entityliving, double d, double d1, double d2, float f, float f1) {
		this.shadowOpaque = 0.5f;
		this.shadowSize = 1f;
		GlStateManager.translate(0, -1.3D, 0);
		String t = entityliving.getCustomNameTag();
		entityliving.setCustomNameTag("");
		this.renderWaterMob((EntityTropicraftWaterBase) entityliving, d, d1, d2, f1);
		GlStateManager.translate(0, 1.3D, 0);
		entityliving.setCustomNameTag(t);

	}

	@Override
	protected void preRenderCallback(EntityTropicraftWaterBase entityliving, float f) {
		if(entityliving instanceof EntityShark) {
			EntityShark s = (EntityShark)entityliving;
			
			float scale = 1f;
			if(s.isBoss()) {
				scale = 1.5f;
				GlStateManager.translate(0, 0.3f, 0f);
			}
			
			GlStateManager.scale(scale, scale, scale);
		}
	}
}