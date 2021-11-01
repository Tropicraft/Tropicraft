package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.tropicraft.core.common.entity.passive.monkey.SpiderMonkeyEntity;

public class SpiderMonkeyModel<T extends SpiderMonkeyEntity> extends EntityModel<T> {
    private final ModelPart body_base;
    private final ModelPart head_base;
    private final ModelPart monke;
    private final ModelPart tail_a;
    private final ModelPart tail_b;
    private final ModelPart arm_left_a;
    private final ModelPart leg_left_a;
    private final ModelPart leg_left_a_r1;
    private final ModelPart leg_right_a;
    private final ModelPart leg_right_a_r1;
    private final ModelPart arm_right_a;

    public SpiderMonkeyModel(ModelPart root) {
        body_base = root.getChild("body_base");
        head_base = body_base.getChild("head_base");
        monke = head_base.getChild("monke");

        tail_a = body_base.getChild("tail_a");
        tail_b = tail_a.getChild("tail_b");

        arm_left_a = body_base.getChild("arm_left_a");

        leg_left_a = body_base.getChild("leg_left_a");
        leg_left_a_r1 = leg_left_a.getChild("leg_left_a_r1");

        arm_right_a = body_base.getChild("arm_right_a");

        leg_right_a = body_base.getChild("leg_right_a");
        leg_right_a_r1 = leg_right_a.getChild("leg_right_a_r1");

//        texWidth = 64;
//        texHeight = 64;
//
//        body_base = new ModelPart(this);
//        body_base.setPos(0.0F, 15.0F, 4.0F);
//        setRotationAngle(body_base, 80.0F, 0.0F, 0.0F);
//        body_base.texOffs(0, 0).addBox(-2.5F, -9.0F, -2.0F, 5.0F, 10.0F, 3.0F, 0.0F, false);
//
//        head_base = new ModelPart(this);
//        head_base.setPos(0.0F, -9.0F, 0.0F);
//        setRotationAngle(head_base, -75.0F, 0.0F, 0.0F);
//        body_base.addChild(head_base);
//        head_base.texOffs(17, 0).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
//
//        monke = new ModelPart(this);
//        monke.setPos(0.0F, -1.5F, -2.5F);
//        head_base.addChild(monke);
//        monke.texOffs(9, 39).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
//
//        tail_a = new ModelPart(this);
//        tail_a.setPos(0.0F, 1.0F, 0.0F);
//        setRotationAngle(tail_a, 65.0F, 0.0F, 0.0F);
//        body_base.addChild(tail_a);
//        tail_a.texOffs(0, 28).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);
//
//        tail_b = new ModelPart(this);
//        tail_b.setPos(0.0F, 7.5F, 1.0F);
//        setRotationAngle(tail_b, -35.0F, 0.0F, 0.0F);
//        tail_a.addChild(tail_b);
//        tail_b.texOffs(0, 39).addBox(-0.99F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
//
//        arm_left_a = new ModelPart(this);
//        arm_left_a.setPos(-2.5F, -7.5F, -0.5F);
//        setRotationAngle(arm_left_a, -82.5F, 2.5F, 2.5F);
//        body_base.addChild(arm_left_a);
//        arm_left_a.texOffs(9, 14).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);
//
//        leg_left_a = new ModelPart(this);
//        leg_left_a.setPos(-2.0F, 1.0F, -1.5F);
//        body_base.addChild(leg_left_a);
//
//        leg_left_a_r1 = new ModelPart(this);
//        leg_left_a_r1.setPos(0.0F, 0.0F, 0.0F);
//        setRotationAngle(leg_left_a_r1, -75.0F, 5.0F, -2.5F);
//        leg_left_a.addChild(leg_left_a_r1);
//        leg_left_a_r1.texOffs(18, 28).addBox(-0.5F, -0.5F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);
//
//        leg_right_a = new ModelPart(this);
//        leg_right_a.setPos(2.0F, 1.0F, -1.5F);
//        body_base.addChild(leg_right_a);
//
//        leg_right_a_r1 = new ModelPart(this);
//        leg_right_a_r1.setPos(0.0F, 0.0F, 0.0F);
//        setRotationAngle(leg_right_a_r1, -75.0F, -5.0F, 2.5F);
//        leg_right_a.addChild(leg_right_a_r1);
//        leg_right_a_r1.texOffs(9, 28).addBox(-1.5F, -0.5F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);
//
//        arm_right_a = new ModelPart(this);
//        arm_right_a.setPos(2.5F, -7.5F, -0.5F);
//        setRotationAngle(arm_right_a, -82.5F, -2.5F, -2.5F);
//        body_base.addChild(arm_right_a);
//        arm_right_a.texOffs(0, 14).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);
//
//        this.setDefaultRotationAngles();
    }

    public static LayerDefinition create() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition modelPartBody = modelPartData.addOrReplaceChild("body_base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.5F, -9.0F, -2.0F, 5.0F, 10.0F, 3.0F, false),
                PartPose.offsetAndRotation(0.0F, 15.0F, 4.0F, 80.0F, 0.0F, 0.0F));

        PartDefinition modelPartHead = modelPartBody.addOrReplaceChild("head_base",
                CubeListBuilder.create()
                        .texOffs(17, 0)
                        .addBox(-2.0F, -4.0F, -3.0F, 4.0F, 4.0F, 4.0F, false),
                PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, -75.0F, 0.0F, 0.0F));

        modelPartHead.addOrReplaceChild("monke",
                CubeListBuilder.create()
                        .texOffs(9, 39)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, false),
                PartPose.offset(0.0F, -1.5F, -2.5F));

        PartDefinition modelPartTail = modelPartBody.addOrReplaceChild("tail_a",
                CubeListBuilder.create()
                        .texOffs(0, 28)
                        .addBox(-1.0F, -0.5F, -1.0F, 2.0F, 8.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 65.0F, 0.0F, 0.0F));

        modelPartTail.addOrReplaceChild("tail_b",
                CubeListBuilder.create()
                        .texOffs(0, 39)
                        .addBox(-0.99F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, 7.5F, 1.0F, -35.0F, 0.0F, 0.0F));

        modelPartBody.addOrReplaceChild("arm_left_a",
                CubeListBuilder.create()
                        .texOffs(9, 14)
                        .addBox(-1.0F, -1.0F, -1.0F, 2.0F, 11.0F, 2.0F, false),
                PartPose.offsetAndRotation(-2.5F, -7.5F, -0.5F, -82.5F, 2.5F, 2.5F));

        modelPartBody.addOrReplaceChild("arm_right_a",
                CubeListBuilder.create()
                        .texOffs(0, 14)
                        .addBox(-1.0F, -1.0F, -1.0F, 2.0F, 11.0F, 2.0F, false),
                PartPose.offsetAndRotation(2.5F, -7.5F, -0.5F, -82.5F, -2.5F, -2.5F));

        PartDefinition modelPartLegLeft = modelPartBody.addOrReplaceChild("leg_left_a",
                CubeListBuilder.create(),
                PartPose.offset(-2.0F, 1.0F, -1.5F));

        modelPartLegLeft.addOrReplaceChild("leg_left_a_r1",
                CubeListBuilder.create()
                        .texOffs(18, 28)
                        .addBox(-0.5F, -0.5F, -1.0F, 2.0F, 8.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -75.0F, 5.0F, -2.5F));

        PartDefinition modelPartLegRight = modelPartBody.addOrReplaceChild("leg_right_a",
                CubeListBuilder.create(),
                PartPose.offset(2.0F, 1.0F, -1.5F));

        modelPartLegRight.addOrReplaceChild("leg_right_a_r1",
                CubeListBuilder.create()
                        .texOffs(9, 28)
                        .addBox(-1.5F, -0.5F, -1.0F, 2.0F, 8.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -75.0F, -5.0F, 2.5F));

        return LayerDefinition.create(modelData, 64, 64);
    }

    private void setDefaultRotationAngles() {
        setRotationAngle(body_base, 80.0F, 0.0F, 0.0F);
        setRotationAngle(head_base, -75.0F, 0.0F, 0.0F);
        setRotationAngle(tail_a, 65.0F, 0.0F, 0.0F);
        setRotationAngle(tail_b, -35.0F, 0.0F, 0.0F);
        setRotationAngle(arm_left_a, -82.5F, 2.5F, 2.5F);
        setRotationAngle(leg_left_a_r1, -75.0F, 5.0F, -2.5F);
        setRotationAngle(leg_right_a_r1, -75.0F, -5.0F, 2.5F);
        setRotationAngle(arm_right_a, -82.5F, -2.5F, -2.5F);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        head_base.xRot += headPitch * ModelAnimator.DEG_TO_RAD;
        head_base.zRot -= headYaw * ModelAnimator.DEG_TO_RAD;

        try (ModelAnimator.Cycle idle = ModelAnimator.cycle(age * 0.025F, 0.05F)) {
            tail_a.xRot += idle.eval(1.0F, 1.0F, 0.0F, 0.0F);
            tail_b.xRot += idle.eval(1.0F, 1.0F, 0.2F, 0.0F);
        }
    }

    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
        this.setDefaultRotationAngles();

        float standAnimation = entity.getStandAnimation(partialTicks);
        float standAngle = standAnimation * 70.0F * ModelAnimator.DEG_TO_RAD;

        body_base.xRot -= standAngle;
        head_base.xRot += standAngle;
        arm_right_a.xRot += standAngle;
        arm_left_a.xRot += standAngle;
        leg_left_a_r1.xRot += standAngle;
        leg_right_a_r1.xRot += standAngle;

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.2F, limbSwingAmount)) {
            arm_left_a.xRot += walk.eval(1.0F, 1.0F);
            arm_right_a.xRot += walk.eval(-1.0F, 1.0F);
            leg_left_a_r1.xRot += walk.eval(-1.0F, 1.0F);
            leg_right_a_r1.xRot += walk.eval(1.0F, 1.0F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body_base.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    private void setRotationAngle(ModelPart part, float x, float y, float z) {
        part.xRot = x * ModelAnimator.DEG_TO_RAD;
        part.yRot = y * ModelAnimator.DEG_TO_RAD;
        part.zRot = z * ModelAnimator.DEG_TO_RAD;
    }
}
