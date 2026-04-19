package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.tropicraft.core.client.entity.model.ManOWarModel;
import net.tropicraft.core.client.entity.render.ManOWarRenderer;

public class ManOWarGelLayer extends RenderLayer<LivingEntityRenderState, ManOWarModel> {
    private final ManOWarModel model;

    public ManOWarGelLayer(ManOWarRenderer renderer, ManOWarModel model) {
        super(renderer);
        this.model = model;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, LivingEntityRenderState state, float yRot, float xRot) {
        if (!state.isInvisible) {
            int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0f);
            submitNodeCollector.submitModel(model, state, poseStack, RenderTypes.entityTranslucent(ManOWarRenderer.TEXTURE_LOCATION), lightCoords, overlayCoords, state.outlineColor, null);
        }
    }
}
