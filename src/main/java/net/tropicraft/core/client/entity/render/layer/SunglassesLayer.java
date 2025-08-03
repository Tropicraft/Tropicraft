package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;

import java.util.function.Predicate;

public class SunglassesLayer<S extends EntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
    private static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/sunglasses.png");

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
    public void render(PoseStack stack, MultiBufferSource bufferSource, int packedLight, S renderState, float yRot, float xRot) {
        if (!predicate.test(renderState)) {
            return;
        }

        stack.pushPose();
        model.setupAnim(renderState);
        transform.apply(stack, renderState, model);

        stack.mulPose(Axis.YP.rotation(Mth.PI));
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        mask.renderMask(stack, consumer, 0, packedLight, OverlayTexture.NO_OVERLAY);
        stack.popPose();
    }

    public interface Transform<S, M> {
        void apply(PoseStack poseStack, S state, M model);
    }
}
