package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.hostile.TropiSpiderEntity;

public class TropiSpiderRenderer extends SpiderRenderer<TropiSpiderEntity> {
	public TropiSpiderRenderer(final EntityRendererManager manager) {
		super(manager);
		shadowOpaque = 0.5f;
	}

	@Override
	public void doRender(TropiSpiderEntity entityliving, double x, double y, double z, float yaw, float pt) {
		GlStateManager.pushMatrix();
		float scale = 1f;
		if (entityliving.getSpiderType() == TropiSpiderEntity.Type.CHILD) {
			scale = 0.5f;
		}
		if (entityliving.getSpiderType() == TropiSpiderEntity.Type.MOTHER) {
			scale = 1.2f;
		}
		shadowSize = scale;

		GlStateManager.translated(x, y, z);
		GlStateManager.scalef(scale, scale, scale);
		super.doRender(entityliving, 0, 0, 0, yaw, pt);
		GlStateManager.popMatrix();
	}


	@Override
	protected ResourceLocation getEntityTexture(TropiSpiderEntity entity) {
		ResourceLocation l = TropicraftRenderUtils.bindTextureEntity("spideradult");
		if (entity.getSpiderType() == TropiSpiderEntity.Type.CHILD) {
			l = TropicraftRenderUtils.bindTextureEntity("spiderchild");
		}
		if (entity.getSpiderType() == TropiSpiderEntity.Type.MOTHER) {
			l = TropicraftRenderUtils.bindTextureEntity("spidermother");
		}
		return l;
	}
}