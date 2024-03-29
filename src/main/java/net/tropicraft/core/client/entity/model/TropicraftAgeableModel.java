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
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!this.young) {
            this.renderAdult(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        } else {
            this.renderChild(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    protected void renderAdult(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.getBody().render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        this.getHead().render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    protected void renderChild(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float reciprocalScale = 1.0F - CHILD_SCALE;

        ModelPart head = this.getHead();
        matrixStack.pushPose();
        matrixStack.translate(0.0, MODEL_OFFSET * reciprocalScale, 0.0);
        matrixStack.translate(
                (-head.x * reciprocalScale) / 16.0,
                (-head.y * reciprocalScale) / 16.0,
                (-head.z * reciprocalScale) / 16.0
        );
        head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.popPose();

        matrixStack.pushPose();
        matrixStack.translate(0.0, MODEL_OFFSET * reciprocalScale, 0.0);
        matrixStack.scale(CHILD_SCALE, CHILD_SCALE, CHILD_SCALE);
        this.getBody().render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.popPose();
    }

    protected abstract ModelPart getHead();

    protected abstract ModelPart getBody();
}
