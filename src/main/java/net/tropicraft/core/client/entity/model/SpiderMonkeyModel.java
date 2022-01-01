package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
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

    public SpiderMonkeyModel(ModelPart model) {
        this.body_base = model;
        this.head_base = model.getChild("head_base");
        this.monke = this.head_base.getChild("monke");
        this.tail_a = model.getChild("tail_a");
        this.tail_b = this.tail_a.getChild("tail_b");
        this.arm_left_a = model.getChild("arm_left_a");
        this.leg_left_a = model.getChild("leg_left_a");
        this.leg_left_a_r1 = this.leg_left_a.getChild("leg_left_a_r1");
        this.leg_right_a = model.getChild("leg_right_a");
        this.leg_right_a_r1 = this.leg_right_a.getChild("leg_right_a_r1");
        this.arm_right_a = model.getChild("arm_right_a");
    }

    public static LayerDefinition create() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("body_base", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.5f, -9.0f, -2.0f, 5.0f, 10.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, 15.0f, 4.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition1 = partDefinition.addOrReplaceChild("head_base", CubeListBuilder.create()
                        .texOffs(17, 0).addBox(-2.0f, -4.0f, -3.0f, 4.0f, 4.0f, 4.0f),
                PartPose.offsetAndRotation(0.0f, 6.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        partDefinition1.addOrReplaceChild("monke", CubeListBuilder.create()
                        .texOffs(9, 39).addBox(-1.0f, 0.0f, -1.0f, 2.0f, 2.0f, 2.0f),
                PartPose.offsetAndRotation(0.0f, -1.5f, -2.5f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition3 = partDefinition.addOrReplaceChild("tail_a", CubeListBuilder.create()
                        .texOffs(0, 28).addBox(-1.0f, -0.5f, -1.0f, 2.0f, 8.0f, 2.0f),
                PartPose.offsetAndRotation(0.0f, 16.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        partDefinition3.addOrReplaceChild("tail_b", CubeListBuilder.create()
                        .texOffs(0, 39).addBox(-0.99f, 0.0f, -2.0f, 2.0f, 6.0f, 2.0f),
                PartPose.offsetAndRotation(0.0f, 7.5f, 1.0f, 0.0f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("arm_left_a", CubeListBuilder.create()
                        .texOffs(9, 14).addBox(-1.0f, -1.0f, -1.0f, 2.0f, 11.0f, 2.0f),
                PartPose.offsetAndRotation(-2.5f, 7.5f, -0.5f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition6 = partDefinition.addOrReplaceChild("leg_left_a", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-2.0f, 16.0f, -1.5f, 0.0f, 0.0f, 0.0f));

        partDefinition6.addOrReplaceChild("leg_left_a_r1", CubeListBuilder.create()
                        .texOffs(18, 28).addBox(-0.5f, -0.5f, -1.0f, 2.0f, 8.0f, 2.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition partDefinition8 = partDefinition.addOrReplaceChild("leg_right_a", CubeListBuilder.create(),
                PartPose.offsetAndRotation(2.0f, 16.0f, -1.5f, 0.0f, 0.0f, 0.0f));

        partDefinition8.addOrReplaceChild("leg_right_a_r1", CubeListBuilder.create()
                        .texOffs(9, 28).addBox(-1.5f, -0.5f, -1.0f, 2.0f, 8.0f, 2.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("arm_right_a", CubeListBuilder.create()
                        .texOffs(0, 14).addBox(-1.0f, -1.0f, -1.0f, 2.0f, 11.0f, 2.0f),
                PartPose.offsetAndRotation(2.5f, 7.5f, -0.5f, 0.0f, 0.0f, 0.0f));

        return LayerDefinition.create(meshDefinition, 64, 64);
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
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body_base.render(poseStack, buffer, packedLight, packedOverlay);
    }

    private void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
