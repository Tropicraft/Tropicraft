package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.EagleRayModel;
import net.tropicraft.core.client.entity.render.layer.EagleRayWingsLayer;
import net.tropicraft.core.common.entity.underdasea.EagleRayEntity;

public class EagleRayRenderer extends MobRenderer<EagleRayEntity, LivingEntityRenderState, EagleRayModel> {
    public static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/ray/eagleray.png");

    public EagleRayRenderer(EntityRendererProvider.Context context) {
        super(context, new EagleRayModel(context.bakeLayer(TropicraftRenderLayers.EAGLE_RAY_LAYER)), 0.8f);
        addLayer(new EagleRayWingsLayer(this));
    }

    @Override
    public void render(LivingEntityRenderState state, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, -1.25, 0);
        super.render(state, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }
}
