package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.SeaTurtleModel;
import net.tropicraft.core.client.entity.render.state.SeaTurtleRenderState;
import net.tropicraft.core.common.entity.SeaTurtleEntity;

public class SeaTurtleRenderer extends MobRenderer<SeaTurtleEntity, SeaTurtleRenderState, SeaTurtleModel> {

    public SeaTurtleRenderer(EntityRendererProvider.Context context) {
        super(context, new SeaTurtleModel(context.bakeLayer(TropicraftRenderLayers.SEA_TURTLE_LAYER)), 0.7f);
        shadowRadius = 0.5f;
        shadowStrength = 0.5f;
    }

    @Override
    public void submit(SeaTurtleRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        float scale = 0.3f;
        float existingTime = state.ageInTicks / 4000.0f;
        if (state.ageInTicks < 30) {
            shadowStrength = 0.5f;
            shadowRadius = Math.min(0.2f + existingTime, 0.5f);
        } else {
            scale = Math.min(0.3f + existingTime, 1.0f);
        }
        if (state.isMature) {
            scale = 1.0f;
        }
        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);

        super.submit(state, poseStack, submitNodeCollector, camera);

        poseStack.popPose();
    }

    @Override
    public SeaTurtleRenderState createRenderState() {
        return new SeaTurtleRenderState();
    }

    @Override
    public void extractRenderState(SeaTurtleEntity entity, SeaTurtleRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isVehicle = entity.isVehicle();
        state.isMature = entity.isMature();
        state.type = entity.getTurtleType();
    }

    @Override
    public Identifier getTextureLocation(SeaTurtleRenderState state) {
        return Tropicraft.id("textures/entity/turtle/sea_turtle" + state.type + ".png");
    }
}
