package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.hostile.TropiSpiderEntity;

public class TropiSpiderRenderer extends SpiderRenderer<TropiSpiderEntity> {
	public TropiSpiderRenderer(final EntityRendererManager manager) {
		super(manager);
		shadowStrength = 0.5f;
	}

	@Override
	public void render(TropiSpiderEntity spider, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		stack.pushPose();
		final float scale = getScale(spider);
		shadowRadius = scale;
		stack.scale(scale, scale, scale);
		super.render(spider, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
		stack.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(TropiSpiderEntity entity) {
		if (entity.getSpiderType() == TropiSpiderEntity.Type.CHILD) {
			return TropicraftRenderUtils.bindTextureEntity("spiderchild");
		}
		if (entity.getSpiderType() == TropiSpiderEntity.Type.MOTHER) {
			return TropicraftRenderUtils.bindTextureEntity("spidermother");
		}
		return TropicraftRenderUtils.bindTextureEntity("spideradult");
	}

	private float getScale(final TropiSpiderEntity spider) {
		float scale = 1.0f;
		if (spider.getSpiderType() == TropiSpiderEntity.Type.CHILD) {
			scale = 0.5f;
		}
		if (spider.getSpiderType() == TropiSpiderEntity.Type.MOTHER) {
			scale = 1.2f;
		}
		return scale;
	}
}