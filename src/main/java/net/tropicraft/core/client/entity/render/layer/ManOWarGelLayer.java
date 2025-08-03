package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.tropicraft.core.client.entity.model.ManOWarModel;
import net.tropicraft.core.client.entity.render.ManOWarRenderer;

public class ManOWarGelLayer extends RenderLayer<LivingEntityRenderState, ManOWarModel> {
    private final ManOWarModel model;

    public ManOWarGelLayer(ManOWarRenderer renderer, ManOWarModel model) {
        super(renderer);
        this.model = model;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, LivingEntityRenderState state, float yRot, float xRot) {
        if (!state.isInvisible) {
            model.setupAnim(state);
            VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityTranslucent(ManOWarRenderer.TEXTURE_LOCATION));
            model.renderToBuffer(poseStack, buffer, packedLight, LivingEntityRenderer.getOverlayCoords(state, 0.0f));
        }
    }
}
