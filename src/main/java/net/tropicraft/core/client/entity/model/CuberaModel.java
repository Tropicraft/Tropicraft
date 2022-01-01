package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.underdasea.CuberaEntity;

public class CuberaModel<T extends CuberaEntity> extends EntityModel<T> {
    private final ModelPart body_base;
    private final ModelPart fin_anal;
    private final ModelPart fin_pelvic_right;
    private final ModelPart fin_pelvic_right_r1;
    private final ModelPart fin_pelvic_left;
    private final ModelPart fin_pelvic_left_r1;
    private final ModelPart fin_pectoral_left;
    private final ModelPart fin_pectoral_right;
    private final ModelPart fin_dorsal;
    private final ModelPart body_connection;
    private final ModelPart jaw_lower;
    private final ModelPart head_base;
    private final ModelPart head_snout;
    private final ModelPart head_snout_r1;
    private final ModelPart tail_base;
    private final ModelPart tail_main;
    private final ModelPart fin_tail;

    public boolean inWater;

    public CuberaModel(ModelPart model) {
        this.body_base = model;
        this.fin_anal = model.getChild("fin_anal");
        this.fin_pelvic_right = model.getChild("fin_pelvic_right");
        this.fin_pelvic_right_r1 = this.fin_pelvic_right.getChild("fin_pelvic_right_r1");
        this.fin_pelvic_left = model.getChild("fin_pelvic_left");
        this.fin_pelvic_left_r1 = this.fin_pelvic_left.getChild("fin_pelvic_left_r1");
        this.fin_pectoral_left = model.getChild("fin_pectoral_left");
        this.fin_pectoral_right = model.getChild("fin_pectoral_right");
        this.fin_dorsal = model.getChild("fin_dorsal");
        this.body_connection = model.getChild("body_connection");
        this.jaw_lower = this.body_connection.getChild("jaw_lower");
        this.head_base = model.getChild("head_base");
        this.head_snout = this.head_base.getChild("head_snout");
        this.head_snout_r1 = this.head_snout.getChild("head_snout_r1");
        this.tail_base = model.getChild("tail_base");
        this.tail_main = this.tail_base.getChild("tail_main");
        this.fin_tail = this.tail_main.getChild("fin_tail");
    }

    public static LayerDefinition create() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        partDefinition.addOrReplaceChild("body_base", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-2.0f, -5.0f, -3.0f, 4.0f, 6.0f, 8.0f),
                PartPose.offsetAndRotation(0.0f, 20.0f, 0.0f, 0.0436f, 0.0f, 0.0f));

        partDefinition.addOrReplaceChild("fin_anal", CubeListBuilder.create()
                        .texOffs(11, 37).addBox(0.0f, -1.0f, -1.0f, 0.0f, 2.0f, 4.0f),
                PartPose.offsetAndRotation(0.0f, 21.0f, 4.0f, -0.3054f, 0.0f, 0.0f));

        PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("fin_pelvic_right", CubeListBuilder.create(),
                PartPose.offsetAndRotation(-1.5f, 21.0f, -2.0f, 0.0f, 0.0f, 0.0f));

        partDefinition2.addOrReplaceChild("fin_pelvic_right_r1", CubeListBuilder.create()
                        .texOffs(20, 37).addBox(0.0f, -1.5f, -0.5f, 0.0f, 2.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -1.3526f, -0.2182f, 0.0f));

        PartDefinition partDefinition4 = partDefinition.addOrReplaceChild("fin_pelvic_left", CubeListBuilder.create(),
                PartPose.offsetAndRotation(1.5f, 21.0f, -2.0f, 0.0f, 0.0f, 0.0f));

        partDefinition4.addOrReplaceChild("fin_pelvic_left_r1", CubeListBuilder.create()
                        .texOffs(27, 37).addBox(0.0f, -1.5f, -0.5f, 0.0f, 2.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -1.3526f, 0.2182f, 0.0f));

        partDefinition.addOrReplaceChild("fin_pectoral_left", CubeListBuilder.create()
                        .texOffs(7, 45).addBox(0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 3.0f),
                PartPose.offsetAndRotation(2.0f, 19.0f, -2.0f, 0.4363f, 0.5672f, 0.0f));

        partDefinition.addOrReplaceChild("fin_pectoral_right", CubeListBuilder.create()
                        .texOffs(0, 45).addBox(0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 3.0f),
                PartPose.offsetAndRotation(-2.0f, 19.0f, -2.0f, 0.4363f, -0.5672f, 0.0f));

        partDefinition.addOrReplaceChild("fin_dorsal", CubeListBuilder.create()
                        .texOffs(25, 0).addBox(0.0f, -3.0f, 0.0f, 0.0f, 3.0f, 7.0f),
                PartPose.offsetAndRotation(0.0f, 15.0f, -1.0f, -0.3054f, 0.0f, 0.0f));

        PartDefinition partDefinition9 = partDefinition.addOrReplaceChild("body_connection", CubeListBuilder.create()
                        .texOffs(28, 15).addBox(-2.0f, -2.0f, -4.0f, 4.0f, 2.0f, 4.0f),
                PartPose.offsetAndRotation(0.0f, 21.0f, -3.0f, 0.0f, 0.0f, 0.0f));

        partDefinition9.addOrReplaceChild("jaw_lower", CubeListBuilder.create()
                        .texOffs(15, 29).addBox(-2.0f, -1.0f, -3.0f, 4.0f, 1.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, -4.0f, -0.1309f, 0.0f, 0.0f));

        PartDefinition partDefinition11 = partDefinition.addOrReplaceChild("head_base", CubeListBuilder.create()
                        .texOffs(0, 15).addBox(-2.0f, 0.0f, -4.0f, 4.0f, 4.0f, 4.0f, new CubeDeformation(0.01f)),
                PartPose.offsetAndRotation(0.0f, 15.0f, -3.0f, 0.4363f, 0.0f, 0.0f));

        PartDefinition partDefinition12 = partDefinition11.addOrReplaceChild("head_snout", CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0f, 0.0f, -4.0f, 0.3054f, 0.0f, 0.0f));

        partDefinition12.addOrReplaceChild("head_snout_r1", CubeListBuilder.create()
                        .texOffs(0, 29).addBox(-2.0f, 0.0f, -3.0f, 4.0f, 3.0f, 3.0f, new CubeDeformation(0.01f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.1309f, 0.0f, 0.0f));

        PartDefinition partDefinition14 = partDefinition.addOrReplaceChild("tail_base", CubeListBuilder.create()
                        .texOffs(0, 37).addBox(-1.5f, 0.0f, 0.0f, 3.0f, 5.0f, 2.0f),
                PartPose.offsetAndRotation(0.0f, 15.5f, 5.0f, -0.0436f, 0.0f, 0.0f));

        PartDefinition partDefinition15 = partDefinition14.addOrReplaceChild("tail_main", CubeListBuilder.create()
                        .texOffs(30, 29).addBox(-1.0f, 0.0f, 0.0f, 2.0f, 4.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, 0.5f, 2.0f, 0.0f, 0.0f, 0.0f));

        partDefinition15.addOrReplaceChild("fin_tail", CubeListBuilder.create()
                        .texOffs(17, 15).addBox(0.0f, -2.0f, -1.0f, 0.0f, 8.0f, 5.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 3.0f, 0.0f, 0.0f, 0.0f));

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        if (inWater) {
            body_base.zRot = 0.0F;
            body_base.y = 20.0F;
        } else {
            body_base.zRot = 90.0F * ModelAnimator.DEG_TO_RAD;
            body_base.y = 22.0F;
        }

        try (ModelAnimator.Cycle swim = ModelAnimator.cycle(limbSwing * 0.4F, limbSwingAmount)) {
            tail_base.yRot = swim.eval(1.0F, 1.0F, 0.0F, 0.0F);
            tail_main.yRot = swim.eval(1.0F, 1.0F, 0.25F, 0.0F);
            fin_tail.yRot = swim.eval(1.0F, 1.0F, 0.5F, 0.0F);

            fin_dorsal.yRot = swim.eval(1.0F, 0.125F);
            fin_anal.yRot = swim.eval(1.0F, 0.125F);
        }

        try (ModelAnimator.Cycle idle = ModelAnimator.cycle(age * 0.05F, 0.1F)) {
            body_base.y += idle.eval(0.5F, 2.0F);

            jaw_lower.xRot = -7.5F * ModelAnimator.DEG_TO_RAD - idle.eval(1.0F, 0.5F, 0.0F, 1.0F);

            fin_pectoral_left.yRot = 32.5F * ModelAnimator.DEG_TO_RAD + idle.eval(0.5F, 1.0F);
            fin_pectoral_right.yRot = -32.5F * ModelAnimator.DEG_TO_RAD + idle.eval(0.5F, -1.0F);

            fin_pelvic_left_r1.zRot = idle.eval(0.5F, -1.0F, 0.2F, 0.0F);
            fin_pelvic_right_r1.zRot = idle.eval(0.5F, 1.0F, 0.2F, 0.0F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body_base.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
