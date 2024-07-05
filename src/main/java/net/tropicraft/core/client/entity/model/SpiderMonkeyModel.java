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
                        .addBox(-2.5f, -9.0f, -2.0f, 5.0f, 10.0f, 3.0f, false),
                PartPose.offsetAndRotation(0.0f, 15.0f, 4.0f, 80.0f, 0.0f, 0.0f));

        PartDefinition head = body.addOrReplaceChild("head_base",
                CubeListBuilder.create()
                        .texOffs(17, 0)
                        .addBox(-2.0f, -4.0f, -3.0f, 4.0f, 4.0f, 4.0f, false),
                PartPose.offsetAndRotation(0.0f, -9.0f, 0.0f, -75.0f, 0.0f, 0.0f));

        head.addOrReplaceChild("monke",
                CubeListBuilder.create()
                        .texOffs(9, 39)
                        .addBox(-1.0f, 0.0f, -1.0f, 2.0f, 2.0f, 2.0f, false),
                PartPose.offset(0.0f, -1.5f, -2.5f));

        PartDefinition tail = body.addOrReplaceChild("tail_a",
                CubeListBuilder.create()
                        .texOffs(0, 28)
                        .addBox(-1.0f, -0.5f, -1.0f, 2.0f, 8.0f, 2.0f, false),
                PartPose.offsetAndRotation(0.0f, 1.0f, 0.0f, 65.0f, 0.0f, 0.0f));

        tail.addOrReplaceChild("tail_b",
                CubeListBuilder.create()
                        .texOffs(0, 39)
                        .addBox(-0.99f, 0.0f, -2.0f, 2.0f, 6.0f, 2.0f, false),
                PartPose.offsetAndRotation(0.0f, 7.5f, 1.0f, -35.0f, 0.0f, 0.0f));

        body.addOrReplaceChild("arm_left_a",
                CubeListBuilder.create()
                        .texOffs(9, 14)
                        .addBox(-1.0f, -1.0f, -1.0f, 2.0f, 11.0f, 2.0f, false),
                PartPose.offsetAndRotation(-2.5f, -7.5f, -0.5f, -82.5f, 2.5f, 2.5f));

        body.addOrReplaceChild("arm_right_a",
                CubeListBuilder.create()
                        .texOffs(0, 14)
                        .addBox(-1.0f, -1.0f, -1.0f, 2.0f, 11.0f, 2.0f, false),
                PartPose.offsetAndRotation(2.5f, -7.5f, -0.5f, -82.5f, -2.5f, -2.5f));

        PartDefinition legLeft = body.addOrReplaceChild("leg_left_a",
                CubeListBuilder.create(),
                PartPose.offset(-2.0f, 1.0f, -1.5f));

        legLeft.addOrReplaceChild("leg_left_a_r1",
                CubeListBuilder.create()
                        .texOffs(18, 28)
                        .addBox(-0.5f, -0.5f, -1.0f, 2.0f, 8.0f, 2.0f, false),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -75.0f, 5.0f, -2.5f));

        PartDefinition legRight = body.addOrReplaceChild("leg_right_a",
                CubeListBuilder.create(),
                PartPose.offset(2.0f, 1.0f, -1.5f));

        legRight.addOrReplaceChild("leg_right_a_r1",
                CubeListBuilder.create()
                        .texOffs(9, 28)
                        .addBox(-1.5f, -0.5f, -1.0f, 2.0f, 8.0f, 2.0f, false),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -75.0f, -5.0f, 2.5f));

        return LayerDefinition.create(mesh, 64, 64);
    }

    private void setDefaultRotationAngles() {
        setRotationAngle(body_base, 80.0f, 0.0f, 0.0f);
        setRotationAngle(head_base, -75.0f, 0.0f, 0.0f);
        setRotationAngle(tail_a, 65.0f, 0.0f, 0.0f);
        setRotationAngle(tail_b, -35.0f, 0.0f, 0.0f);
        setRotationAngle(arm_left_a, -82.5f, 2.5f, 2.5f);
        setRotationAngle(leg_left_a_r1, -75.0f, 5.0f, -2.5f);
        setRotationAngle(leg_right_a_r1, -75.0f, -5.0f, 2.5f);
        setRotationAngle(arm_right_a, -82.5f, -2.5f, -2.5f);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        head_base.xRot += headPitch * ModelAnimator.DEG_TO_RAD;
        head_base.zRot -= headYaw * ModelAnimator.DEG_TO_RAD;

        try (ModelAnimator.Cycle idle = ModelAnimator.cycle(age * 0.025f, 0.05f)) {
            tail_a.xRot += idle.eval(1.0f, 1.0f, 0.0f, 0.0f);
            tail_b.xRot += idle.eval(1.0f, 1.0f, 0.2f, 0.0f);
        }
    }

    @Override
    public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
        setDefaultRotationAngles();

        float standAnimation = entity.getStandAnimation(partialTicks);
        float standAngle = standAnimation * 70.0f * ModelAnimator.DEG_TO_RAD;

        body_base.xRot -= standAngle;
        head_base.xRot += standAngle;
        arm_right_a.xRot += standAngle;
        arm_left_a.xRot += standAngle;
        leg_left_a_r1.xRot += standAngle;
        leg_right_a_r1.xRot += standAngle;

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.2f, limbSwingAmount)) {
            arm_left_a.xRot += walk.eval(1.0f, 1.0f);
            arm_right_a.xRot += walk.eval(-1.0f, 1.0f);
            leg_left_a_r1.xRot += walk.eval(-1.0f, 1.0f);
            leg_right_a_r1.xRot += walk.eval(1.0f, 1.0f);
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
