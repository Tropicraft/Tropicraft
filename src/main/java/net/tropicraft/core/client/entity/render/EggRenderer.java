package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.EggModel;
import net.tropicraft.core.client.entity.render.state.EggRenderState;
import net.tropicraft.core.common.entity.egg.EggEntity;

public class EggRenderer extends LivingEntityRenderer<EggEntity, EggRenderState, EggModel> {

    public EggRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelLayerLocation) {
        super(context, new EggModel(context.bakeLayer(modelLayerLocation)), 1.0f);
        shadowStrength = 0.5f;
    }

    @Override
    public void submit(EggRenderState state, PoseStack stack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        stack.pushPose();
        if (state.shouldRenderFlat) {
            shadowRadius = 0.0f;
            stack.translate(0, 0.05, 0);
            drawFlatEgg(state, stack, submitNodeCollector, camera, state.lightCoords);
        } else {
            shadowRadius = 0.2f;
            stack.scale(0.5f, 0.5f, 0.5f);
            super.submit(state, stack, submitNodeCollector, camera);
        }
        stack.popPose();
    }

    public void drawFlatEgg(EggRenderState state, PoseStack stack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, int packedLightIn) {
        stack.pushPose();

        stack.mulPose(cameraRenderState.orientation);
        stack.mulPose(Axis.YP.rotationDegrees(180.0f));

        stack.scale(0.25f, 0.25f, 0.25f);

        Identifier texture = getTextureLocation(state);
        submitNodeCollector.submitCustomGeometry(stack, RenderTypes.entityCutout(texture), (pose, buffer) -> {
            int overlay = OverlayTexture.NO_OVERLAY;
            TropicraftSpecialRenderHelper.vertex(buffer, pose, -0.5, -0.25, 0, 1, 1, 1, 1, 0, 1, Direction.UP, packedLightIn, overlay);
            TropicraftSpecialRenderHelper.vertex(buffer, pose, 0.5, -0.25, 0, 1, 1, 1, 1, 1, 1, Direction.UP, packedLightIn, overlay);
            TropicraftSpecialRenderHelper.vertex(buffer, pose, 0.5, 0.75, 0, 1, 1, 1, 1, 1, 0, Direction.UP, packedLightIn, overlay);
            TropicraftSpecialRenderHelper.vertex(buffer, pose, -0.5, 0.75, 0, 1, 1, 1, 1, 0, 0, Direction.UP, packedLightIn, overlay);
        });

        stack.popPose();
    }

    @Override
    public EggRenderState createRenderState() {
        return new EggRenderState();
    }

    @Override
    public void extractRenderState(EggEntity entity, EggRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.hatching = entity.isNearHatching();
        state.randRotater = (float) entity.rotationRand;
        state.texture = entity.getEggTexture();
        state.shouldRenderFlat = entity.shouldEggRenderFlat();
    }

    @Override
    protected boolean shouldShowName(EggEntity entity, double distanceToCameraSq) {
        return entity.hasCustomName();
    }

    @Override
    protected boolean affectedByCulling(EggEntity display) {
        return false;
    }

    @Override
    public Identifier getTextureLocation(EggRenderState state) {
        return Tropicraft.id("textures/entity/" + state.texture + ".png");
    }
}
