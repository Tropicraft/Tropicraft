package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.EagleRayModel;
import net.tropicraft.core.client.entity.render.layer.EagleRayWingsLayer;
import net.tropicraft.core.common.entity.underdasea.EagleRayEntity;

public class EagleRayRenderer extends MobRenderer<EagleRayEntity, LivingEntityRenderState, EagleRayModel> {
    public static final Identifier TEXTURE = Tropicraft.id("textures/entity/ray/eagleray.png");

    public EagleRayRenderer(EntityRendererProvider.Context context) {
        super(context, new EagleRayModel(context.bakeLayer(TropicraftRenderLayers.EAGLE_RAY_LAYER)), 0.8f);
        addLayer(new EagleRayWingsLayer(this));
    }

    @Override
    public void submit(LivingEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0, -1.25, 0);
        super.submit(state, poseStack, submitNodeCollector, camera);
        poseStack.popPose();
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }
}
