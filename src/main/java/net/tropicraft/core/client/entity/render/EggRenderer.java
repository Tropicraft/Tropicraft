package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.EggModel;
import net.tropicraft.core.common.entity.egg.EggEntity;

public class EggRenderer extends LivingEntityRenderer<EggEntity, EggModel> {

    public EggRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation) {
        super(context, new EggModel(context.bakeLayer(modelLayerLocation)), 1.0f);
        shadowStrength = 0.5f;
    }

    @Override
    public void render(EggEntity egg, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        if (egg.shouldEggRenderFlat()) {
            shadowRadius = 0.0f;
            stack.translate(0, 0.05, 0);
            drawFlatEgg(egg, partialTicks, stack, bufferIn, packedLightIn);
        } else {
            shadowRadius = 0.2f;
            stack.scale(0.5f, 0.5f, 0.5f);
            super.render(egg, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        }
        stack.popPose();
    }

    public void drawFlatEgg(EggEntity ent, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();

        stack.mulPose(entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Axis.YP.rotationDegrees(180.0f));

        stack.scale(0.25f, 0.25f, 0.25f);

        VertexConsumer buffer = TropicraftRenderUtils.getEntityCutoutBuilder(bufferIn, getTextureLocation(ent));
        int overlay = getOverlayCoords(ent, getWhiteOverlayProgress(ent, partialTicks));

        PoseStack.Pose pose = stack.last();
        TropicraftSpecialRenderHelper.vertex(buffer, pose, -0.5, -0.25, 0, 1, 1, 1, 1, 0, 1, Direction.UP, packedLightIn, overlay);
        TropicraftSpecialRenderHelper.vertex(buffer, pose, 0.5, -0.25, 0, 1, 1, 1, 1, 1, 1, Direction.UP, packedLightIn, overlay);
        TropicraftSpecialRenderHelper.vertex(buffer, pose, 0.5, 0.75, 0, 1, 1, 1, 1, 1, 0, Direction.UP, packedLightIn, overlay);
        TropicraftSpecialRenderHelper.vertex(buffer, pose, -0.5, 0.75, 0, 1, 1, 1, 1, 0, 0, Direction.UP, packedLightIn, overlay);

        stack.popPose();
    }

    @Override
    protected boolean shouldShowName(EggEntity entity) {
        return entity.hasCustomName();
    }

    @Override
    public ResourceLocation getTextureLocation(EggEntity entity) {
        return TropicraftRenderUtils.bindTextureEntity(entity.getEggTexture());
    }
}
