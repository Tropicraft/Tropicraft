package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.SeahorseModel;
import net.tropicraft.core.common.entity.underdasea.SeahorseEntity;

import javax.annotation.Nullable;

public class SeahorseRenderer extends MobRenderer<SeahorseEntity, SeahorseModel> {
	public SeahorseRenderer(EntityRendererManager renderManager) {
		super(renderManager, new SeahorseModel(), 0.5F);
		shadowOpaque = 0.5f;
	}

	@Override
	public void doRender(SeahorseEntity seahorse, double x, double y, double z, float yaw, float partialTicks) {
		GlStateManager.pushMatrix();

		GlStateManager.translated(x, y - 1f, z);
		GlStateManager.scalef(0.5f, 0.5f, 0.5f);

	//	GlStateManager.rotatef(90, 0f, 1f, 0f);

		super.doRender(seahorse, x, y, z, yaw, partialTicks);
		GlStateManager.popMatrix();
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(SeahorseEntity seahorseEntity) {
		return TropicraftRenderUtils.getTextureEntity(String.format("seahorse/%s", seahorseEntity.getTexture()));
	}
}