package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
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
    public void render(SeaTurtleRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int lightCoords) {
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

        super.render(state, poseStack, bufferSource, lightCoords);

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
    public ResourceLocation getTextureLocation(SeaTurtleRenderState state) {
        return Tropicraft.location("textures/entity/turtle/sea_turtle" + state.type + ".png");
    }
}
