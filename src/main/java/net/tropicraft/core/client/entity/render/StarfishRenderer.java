package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.render.state.StarfishRenderState;
import net.tropicraft.core.common.entity.underdasea.StarfishEntity;
import net.tropicraft.core.common.entity.underdasea.StarfishType;

public class StarfishRenderer extends EntityRenderer<StarfishEntity, StarfishRenderState> {

    /**
     * Amount freshly hatched starfish are scaled down while rendering.
     */
    public static final float BABY_RENDER_SCALE = 0.25f;

    /**
     * Amount mature starfish are scaled down while rendering.
     */
    public static final float ADULT_RENDER_SCALE = 1.0f;

    public StarfishRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public StarfishRenderState createRenderState() {
        return new StarfishRenderState();
    }

    @Override
    public void extractRenderState(StarfishEntity entity, StarfishRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.type = entity.getStarfishType();
        state.growthProgress = entity.getGrowthProgress();
        state.hasRedOverlay = entity.hurtTime > 0;
    }

    @Override
    public void render(StarfishRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        StarfishType type = state.type;

        float f = 0.0f;
        float f1 = 1.0f;
        float f2 = 0.0f;
        float f3 = 1.0f;
        float f1shifted = 1;
        float f3shifted = 1;

        poseStack.pushPose();
        poseStack.translate(-0.5, 0, -0.5);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        float scale = Mth.lerp(state.growthProgress, BABY_RENDER_SCALE, ADULT_RENDER_SCALE);
        poseStack.scale(scale, scale, scale);

        int packedOverlay = OverlayTexture.pack(OverlayTexture.u(0.0f), OverlayTexture.v(state.hasRedOverlay));
        for (int i = 0; i < type.getLayerCount(); i++) {
            ResourceLocation texture = type.getTextures().get(i);
            VertexConsumer ivertexbuilder = bufferSource.getBuffer(RenderType.entityCutout(texture));
            final float red = 1;
            float green = state.hasRedOverlay ? 0 : 1;
            float blue = state.hasRedOverlay ? 0 : 1;
            final float alpha = 1;
            float layerHeight = type.getLayerHeights()[i];
            TropicraftSpecialRenderHelper.popper(f1, f2, f, f3, f1shifted, f3shifted, layerHeight, poseStack, ivertexbuilder, packedLight, packedOverlay, red, green, blue, alpha);
            poseStack.translate(0.0f, 0.0f, -layerHeight);
        }

        poseStack.popPose();
    }
}
