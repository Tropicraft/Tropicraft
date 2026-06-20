package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;

import java.util.function.Predicate;

public class SunglassesLayer<S extends EntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/sunglasses.png");

    private final TropicraftSpecialRenderHelper mask;
    private final M model;
    private final Predicate<S> predicate;
    private final Transform<S, M> transform;

    public SunglassesLayer(RenderLayerParent<S, M> parent, Predicate<S> predicate, Transform<S, M> transform) {
        super(parent);
        model = parent.getModel();
        mask = new TropicraftSpecialRenderHelper();
        this.predicate = predicate;
        this.transform = transform;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        if (!predicate.test(state)) {
            return;
        }

        poseStack.pushPose();
        model.setupAnim(state);
        transform.apply(poseStack, state, model);

        poseStack.mulPose(Axis.YP.rotation(Mth.PI));
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(TEXTURE), (pose, buffer) -> {
            PoseStack stack = new PoseStack();
            stack.mulPose(pose.pose());
            mask.renderMask(stack, buffer, 0, state.lightCoords, OverlayTexture.NO_OVERLAY);
        });
        poseStack.popPose();
    }

    public interface Transform<S, M> {
        void apply(PoseStack poseStack, S state, M model);
    }
}
