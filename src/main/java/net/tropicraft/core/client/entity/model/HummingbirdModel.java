package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.Entity;

public class HummingbirdModel<T extends Entity> extends EntityModel<T> {
    private final ModelPart body_base;
    private final ModelPart tail_base;
    private final ModelPart wing_left;
    private final ModelPart head_base;
    private final ModelPart beak_base;
    private final ModelPart wing_right;

    public HummingbirdModel(final ModelPart root) {
        body_base = root.getChild(PartNames.BODY);
        tail_base = root.getChild(PartNames.TAIL);
        wing_left = root.getChild(PartNames.LEFT_WING);
        wing_right = root.getChild(PartNames.RIGHT_WING);
        head_base = root.getChild(PartNames.HEAD);
        beak_base = root.getChild(PartNames.BEAK);

        texWidth = 32;
        texHeight = 32;

        body_base = new ModelPart(this);
        body_base.setPos(0.0F, 20.0F, 0.0F);
        setRotationAngle(body_base, 0.4363F, 0.0F, 0.0F);
        body_base.texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        tail_base = new ModelPart(this);
        tail_base.setPos(0.0F, 1.0F, 1.0F);
        body_base.addChild(tail_base);
        setRotationAngle(tail_base, 0.2618F, 0.0F, 0.0F);
        tail_base.texOffs(0, 6).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 4.0F, 0.0F, 0.0F, false);

        wing_left = new ModelPart(this);
        wing_left.setPos(1.0F, -2.0F, 1.0F);
        body_base.addChild(wing_left);
        wing_left.texOffs(9, 11).addBox(0.0F, 0.0F, 0.0F, 4.0F, 2.0F, 0.0F, 0.0F, false);

        head_base = new ModelPart(this);
        head_base.setPos(0.0F, -2.0F, 0.0F);
        body_base.addChild(head_base);
        setRotationAngle(head_base, -0.2618F, 0.0F, 0.0F);
        head_base.texOffs(9, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.001F, false);

        beak_base = new ModelPart(this);
        beak_base.setPos(0.0F, -2.0F, -1.0F);
        head_base.addChild(beak_base);
        setRotationAngle(beak_base, 0.3927F, 0.0F, 0.0F);
        beak_base.texOffs(7, 6).addBox(0.0F, 0.0F, -3.0F, 0.0F, 1.0F, 3.0F, 0.0F, false);

        wing_right = new ModelPart(this);
        wing_right.setPos(-1.0F, -2.0F, 1.0F);
        body_base.addChild(wing_right);
        wing_right.texOffs(0, 11).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 0.0F, 0.0F, false);
    }

    public static LayerDefinition create() {
        // TODO 1.17
        return null;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(head_base, headYaw, headPitch);

        try (ModelAnimator.Cycle fly = ModelAnimator.cycle(age * 0.25F, 1.0F)) {
            body_base.y = 20.0F + fly.eval(1.0F, 0.1F);

            wing_right.yRot = fly.eval(1.0F, 1.0F, 0.0F, 0.0F);
            wing_left.yRot = fly.eval(1.0F, -1.0F, 0.0F, 0.0F);
            wing_right.zRot = fly.eval(1.0F, 0.4F, 0.0F, 0.3F);
            wing_left.zRot = fly.eval(1.0F, -0.4F, 0.0F, -0.3F);
            wing_right.xRot = fly.eval(1.0F, 0.4F, 0.1F, 0.2F);
            wing_left.xRot = fly.eval(1.0F, 0.4F, 0.1F, 0.2F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body_base.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    private void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
