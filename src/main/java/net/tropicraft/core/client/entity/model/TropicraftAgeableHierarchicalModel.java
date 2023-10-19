package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public abstract class TropicraftAgeableHierarchicalModel<T extends Entity> extends EntityModel<T> {
    private static final double MODEL_OFFSET = 1.5;
    private static final float CHILD_SCALE = 0.5f;

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        if (!young) {
            renderAdult(poseStack, buffer, packedLight, packedOverlay, color);
        } else {
            renderChild(poseStack, buffer, packedLight, packedOverlay, color);
        }
    }

    protected void renderAdult(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        root().render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    protected void renderChild(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();
        poseStack.translate(0.0, MODEL_OFFSET * (1.0f - CHILD_SCALE), 0.0);
        poseStack.scale(CHILD_SCALE, CHILD_SCALE, CHILD_SCALE);
        setScale(head(), 1.0f / CHILD_SCALE);
        root().render(poseStack, buffer, packedLight, packedOverlay, color);
        setScale(head(), 1.0f);
        poseStack.popPose();
    }

    private void setScale(ModelPart part, float scale) {
        part.xScale = scale;
        part.yScale = scale;
        part.zScale = scale;
    }

    protected abstract ModelPart root();

    protected abstract ModelPart head();
}
