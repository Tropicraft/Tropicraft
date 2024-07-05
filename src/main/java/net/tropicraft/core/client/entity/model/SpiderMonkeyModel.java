package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.passive.monkey.SpiderMonkeyEntity;

public class SpiderMonkeyModel<T extends SpiderMonkeyEntity> extends HierarchicalModel<T> {
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
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body_base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.5F, -9.0F, -2.0F, 5.0F, 10.0F, 3.0F, false),
                PartPose.offsetAndRotation(0.0F, 15.0F, 4.0F, 80.0F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head_base",
                CubeListBuilder.create()
                        .texOffs(17, 0)
                        .addBox(-2.0F, -4.0F, -3.0F, 4.0F, 4.0F, 4.0F, false),
                PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, -75.0F, 0.0F, 0.0F));

        head.addOrReplaceChild("monke",
                CubeListBuilder.create()
                        .texOffs(9, 39)
                        .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, false),
                PartPose.offset(0.0F, -1.5F, -2.5F));

        PartDefinition tail = body.addOrReplaceChild("tail_a",
                CubeListBuilder.create()
                        .texOffs(0, 28)
                        .addBox(-1.0F, -0.5F, -1.0F, 2.0F, 8.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 65.0F, 0.0F, 0.0F));

        tail.addOrReplaceChild("tail_b",
                CubeListBuilder.create()
                        .texOffs(0, 39)
                        .addBox(-0.99F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, 7.5F, 1.0F, -35.0F, 0.0F, 0.0F));

        body.addOrReplaceChild("arm_left_a",
                CubeListBuilder.create()
                        .texOffs(9, 14)
                        .addBox(-1.0F, -1.0F, -1.0F, 2.0F, 11.0F, 2.0F, false),
                PartPose.offsetAndRotation(-2.5F, -7.5F, -0.5F, -82.5F, 2.5F, 2.5F));

        body.addOrReplaceChild("arm_right_a",
                CubeListBuilder.create()
                        .texOffs(0, 14)
                        .addBox(-1.0F, -1.0F, -1.0F, 2.0F, 11.0F, 2.0F, false),
                PartPose.offsetAndRotation(2.5F, -7.5F, -0.5F, -82.5F, -2.5F, -2.5F));

        PartDefinition legLeft = body.addOrReplaceChild("leg_left_a",
                CubeListBuilder.create(),
                PartPose.offset(-2.0F, 1.0F, -1.5F));

        legLeft.addOrReplaceChild("leg_left_a_r1",
                CubeListBuilder.create()
                        .texOffs(18, 28)
                        .addBox(-0.5F, -0.5F, -1.0F, 2.0F, 8.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -75.0F, 5.0F, -2.5F));

        PartDefinition legRight = body.addOrReplaceChild("leg_right_a",
                CubeListBuilder.create(),
                PartPose.offset(2.0F, 1.0F, -1.5F));

        legRight.addOrReplaceChild("leg_right_a_r1",
                CubeListBuilder.create()
                        .texOffs(9, 28)
                        .addBox(-1.5F, -0.5F, -1.0F, 2.0F, 8.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -75.0F, -5.0F, 2.5F));

        return LayerDefinition.create(mesh, 64, 64);
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
        setDefaultRotationAngles();

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

    private void setRotationAngle(ModelPart part, float x, float y, float z) {
        part.xRot = x * ModelAnimator.DEG_TO_RAD;
        part.yRot = y * ModelAnimator.DEG_TO_RAD;
        part.zRot = z * ModelAnimator.DEG_TO_RAD;
    }

    @Override
    public ModelPart root() {
        return body_base;
    }
}
