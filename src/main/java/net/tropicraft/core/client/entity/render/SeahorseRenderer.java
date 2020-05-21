package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.SeahorseModel;
import net.tropicraft.core.common.entity.underdasea.MarlinEntity;
import net.tropicraft.core.common.entity.underdasea.SeahorseEntity;

import javax.annotation.Nullable;

public class SeahorseRenderer extends MobRenderer<SeahorseEntity, SeahorseModel> {
	public SeahorseRenderer(EntityRendererManager renderManager) {
		super(renderManager, new SeahorseModel(), 0.5F);
		shadowOpaque = 0.5f;
	}

	@Override
	public void render(SeahorseEntity seahorse, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();

		matrixStackIn.translate(0, -1f, 0);
		matrixStackIn.scale(0.5f, 0.5f, 0.5f);

		super.render(seahorse, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		matrixStackIn.pop();
	}

	@Nullable
	@Override
	public ResourceLocation getEntityTexture(SeahorseEntity seahorseEntity) {
		return TropicraftRenderUtils.getTextureEntity(String.format("seahorse/%s", seahorseEntity.getTexture()));
	}
}