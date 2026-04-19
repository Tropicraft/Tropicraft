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
import net.tropicraft.core.client.entity.model.SharkModel;
import net.tropicraft.core.client.entity.render.state.SharkRenderState;
import net.tropicraft.core.common.entity.underdasea.SharkEntity;

public class SharkRenderer extends MobRenderer<SharkEntity, SharkRenderState, SharkModel> {
    public static final Identifier BASIC_SHARK_TEXTURE = Tropicraft.id("textures/entity/shark/hammerhead1.png");
    public static final Identifier BOSS_SHARK_TEXTURE = Tropicraft.id("textures/entity/shark/hammerhead4.png");

    public SharkRenderer(EntityRendererProvider.Context context) {
        super(context, new SharkModel(context.bakeLayer(TropicraftRenderLayers.HAMMERHEAD_LAYER)), 1);
    }

    @Override
    public SharkRenderState createRenderState() {
        return new SharkRenderState();
    }

    @Override
    public void extractRenderState(SharkEntity entity, SharkRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isBoss = entity.isBoss();
    }

    @Override
    public Identifier getTextureLocation(SharkRenderState state) {
        if (state.isBoss) {
            return BOSS_SHARK_TEXTURE;
        }
        return BASIC_SHARK_TEXTURE;
    }

    @Override
    public void submit(SharkRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0, -1, 0);
        super.submit(state, poseStack, submitNodeCollector, camera);
        poseStack.popPose();
    }

    @Override
    protected void scale(SharkRenderState state, PoseStack poseStack) {
        float scale = 1.0f;
        if (state.isBoss) {
            scale = 1.5f;
            poseStack.translate(0, 0.3f, 0);
        }
        poseStack.scale(scale, scale, scale);
    }
}
