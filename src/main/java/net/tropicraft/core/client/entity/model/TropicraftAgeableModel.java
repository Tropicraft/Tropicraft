package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public abstract class TropicraftAgeableModel<T extends Entity> extends EntityModel<T> {
    private static final double MODEL_OFFSET = 1.501;

    static final float CHILD_SCALE = 0.5F;

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (!this.young) {
            this.renderAdult(matrixStack, buffer, packedLight, packedOverlay, color);
        } else {
            this.renderChild(matrixStack, buffer, packedLight, packedOverlay, color);
        }
    }

    protected void renderAdult(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.getBody().render(matrixStack, buffer, packedLight, packedOverlay, color);
        this.getHead().render(matrixStack, buffer, packedLight, packedOverlay, color);
    }

    protected void renderChild(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        float reciprocalScale = 1.0F - CHILD_SCALE;

        ModelPart head = this.getHead();
        matrixStack.pushPose();
        matrixStack.translate(0.0, MODEL_OFFSET * reciprocalScale, 0.0);
        matrixStack.translate(
                (-head.x * reciprocalScale) / 16.0,
                (-head.y * reciprocalScale) / 16.0,
                (-head.z * reciprocalScale) / 16.0
        );
        head.render(matrixStack, buffer, packedLight, packedOverlay, color);
        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate(0.0, MODEL_OFFSET * reciprocalScale, 0.0);
        matrixStack.scale(CHILD_SCALE, CHILD_SCALE, CHILD_SCALE);
        this.getBody().render(matrixStack, buffer, packedLight, packedOverlay, color);
        matrixStack.popPose();
    }

    protected abstract ModelPart getHead();

    protected abstract ModelPart getBody();
}
