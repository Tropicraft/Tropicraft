package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.common.entity.underdasea.StarfishEntity;
import net.tropicraft.core.common.entity.underdasea.StarfishType;

import javax.annotation.Nullable;

public class StarfishRenderer extends EntityRenderer<StarfishEntity> {

	/**
	 * Amount freshly hatched starfish are scaled down while rendering.
	 */
	public static final float BABY_RENDER_SCALE = 0.25f;

	/**
	 * Amount mature starfish are scaled down while rendering.
	 */
	public static final float ADULT_RENDER_SCALE = 1f;

	public StarfishRenderer(final EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(StarfishEntity starfish, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLightIn) {
		StarfishType type = starfish.getStarfishType();

		float f = 0f;
		float f1 = 1f;
		float f2 = 0f;
		float f3 = 1f;
		float f1shifted = 1;
		float f3shifted = 1;

		stack.pushPose();
		stack.translate(-0.5, 0, -0.5);
		stack.mulPose(Vector3f.XP.rotationDegrees(90));

		final float scale = BABY_RENDER_SCALE + starfish.getGrowthProgress() * (ADULT_RENDER_SCALE - BABY_RENDER_SCALE);
		stack.scale(scale, scale, scale);

		for (int i = 0; i < type.getLayerCount(); i++) {
			final VertexConsumer ivertexbuilder = buffer.getBuffer(RenderType.entityCutout(TropicraftRenderUtils.getTextureEntity(type.getTexturePaths().get(i))));
			final float red = 1;
			final float green = starfish.hurtTime > 0 ? 0 : 1;
			final float blue = starfish.hurtTime > 0 ? 0 : 1;
			final float alpha = 1;
			final float layerHeight = type.getLayerHeights()[i];
			TropicraftSpecialRenderHelper.popper(f1, f2, f, f3, f1shifted, f3shifted, layerHeight, stack, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(starfish, 0), red, green, blue, alpha);
			stack.translate(0f, 0f, -layerHeight);
		}

		stack.popPose();
	}

	@Nullable
	@Override
	public ResourceLocation getTextureLocation(StarfishEntity starfishEntity) {
		return null; // Custom setting this in the render loop
	}
}
