package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.SeaTurtleEntity;
import net.tropicraft.core.common.entity.hostile.TropiSpiderEntity;

public class TropiSpiderRenderer extends SpiderRenderer<TropiSpiderEntity> {
	public TropiSpiderRenderer(final EntityRendererManager manager) {
		super(manager);
		shadowOpaque = 0.5f;
	}

	@Override
	public void render(TropiSpiderEntity spider, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		stack.push();
		float scale = 1f;
		if (spider.getSpiderType() == TropiSpiderEntity.Type.CHILD) {
			scale = 0.5f;
		}
		if (spider.getSpiderType() == TropiSpiderEntity.Type.MOTHER) {
			scale = 1.2f;
		}
		shadowSize = scale;

		// TODO still needed 1.15?
		//GlStateManager.translated(x, y, z);
		stack.scale(scale, scale, scale);
		super.render(spider, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
		GlStateManager.popMatrix();
	}


	@Override
	public ResourceLocation getEntityTexture(TropiSpiderEntity entity) {
		if (entity.getSpiderType() == TropiSpiderEntity.Type.CHILD) {
			return TropicraftRenderUtils.bindTextureEntity("spiderchild");
		}
		if (entity.getSpiderType() == TropiSpiderEntity.Type.MOTHER) {
			return TropicraftRenderUtils.bindTextureEntity("spidermother");
		}
		return TropicraftRenderUtils.bindTextureEntity("spideradult");
	}
}