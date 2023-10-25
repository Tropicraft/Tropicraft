package net.tropicraft.core.client.entity.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;

public class SunglassesLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation TEXTURE = TropicraftRenderUtils.getTextureEntity("sunglasses");

    private final TropicraftSpecialRenderHelper mask;
    private final M model;
    private final Transform<T, M> transform;

    public SunglassesLayer(RenderLayerParent<T, M> parent, Transform<T, M> transform) {
        super(parent);
        model = parent.getModel();
        mask = new TropicraftSpecialRenderHelper();
        this.transform = transform;
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        stack.pushPose();
        model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        transform.apply(stack, entity, model);

        stack.mulPose(Axis.YP.rotation(Mth.PI));
        VertexConsumer consumer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        mask.renderMask(stack, consumer, 0, packedLightIn, OverlayTexture.NO_OVERLAY);
        stack.popPose();
    }

    public interface Transform<T, M> {
        void apply(PoseStack poseStack, T entity, M model);
    }
}
