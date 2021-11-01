package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class CuberaModel<T extends Entity> extends EntityModel<T> {
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

    public CuberaModel(ModelPart root) {
        body_base = root.getChild("body_base");
        fin_anal = body_base.getChild("fin_anal");
        fin_pelvic_right = body_base.getChild("fin_pelvic_right");
        fin_pelvic_right_r1 = fin_pelvic_right.getChild("fin_pelvic_right_r1");
        fin_pelvic_left = body_base.getChild("fin_pelvic_left");
        fin_pelvic_left_r1 = fin_pelvic_left.getChild("fin_pelvic_left_r1");
        fin_pectoral_left = body_base.getChild("fin_pectoral_left");
        fin_pectoral_right = body_base.getChild("fin_pectoral_right");
        fin_dorsal = body_base.getChild("fin_dorsal");
        body_connection = body_base.getChild("body_connection");
        jaw_lower = body_connection.getChild("jaw_lower");
        head_base = body_base.getChild("head_base");
        head_snout = head_base.getChild("head_snout");
        head_snout_r1 = head_snout.getChild("head_snout_r1");
        tail_base = body_base.getChild("tail_base");
        tail_main = tail_base.getChild("tail_main");
        fin_tail = tail_main.getChild("fin_tail");

//        texWidth = 64;
//        texHeight = 64;
//
//        body_base = new ModelPart(this);
//        body_base.setPos(0.0F, 20.0F, 0.0F);
//        setRotationAngle(body_base, 0.0436F, 0.0F, 0.0F);
//        body_base.texOffs(0, 0).addBox(-2.0F, -5.0F, -3.0F, 4.0F, 6.0F, 8.0F, 0.0F, false);
//
//        fin_anal = new ModelPart(this);
//        fin_anal.setPos(0.0F, 1.0F, 4.0F);
//        body_base.addChild(fin_anal);
//        setRotationAngle(fin_anal, -0.3054F, 0.0F, 0.0F);
//        fin_anal.texOffs(11, 37).addBox(0.0F, -1.0F, -1.0F, 0.0F, 2.0F, 4.0F, 0.0F, false);
//
//        fin_pelvic_right = new ModelPart(this);
//        fin_pelvic_right.setPos(-1.5F, 1.0F, -2.0F);
//        body_base.addChild(fin_pelvic_right);
//
//        fin_pelvic_right_r1 = new ModelPart(this);
//        fin_pelvic_right_r1.setPos(0.0F, 0.0F, 0.0F);
//        fin_pelvic_right.addChild(fin_pelvic_right_r1);
//        setRotationAngle(fin_pelvic_right_r1, -1.3526F, -0.2182F, 0.0F);
//        fin_pelvic_right_r1.texOffs(20, 37).addBox(0.0F, -1.5F, -0.5F, 0.0F, 2.0F, 3.0F, 0.0F, false);
//
//        fin_pelvic_left = new ModelPart(this);
//        fin_pelvic_left.setPos(1.5F, 1.0F, -2.0F);
//        body_base.addChild(fin_pelvic_left);
//
//        fin_pelvic_left_r1 = new ModelPart(this);
//        fin_pelvic_left_r1.setPos(0.0F, 0.0F, 0.0F);
//        fin_pelvic_left.addChild(fin_pelvic_left_r1);
//        setRotationAngle(fin_pelvic_left_r1, -1.3526F, 0.2182F, 0.0F);
//        fin_pelvic_left_r1.texOffs(27, 37).addBox(0.0F, -1.5F, -0.5F, 0.0F, 2.0F, 3.0F, 0.0F, false);
//
//        fin_pectoral_left = new ModelPart(this);
//        fin_pectoral_left.setPos(2.0F, -1.0F, -2.0F);
//        body_base.addChild(fin_pectoral_left);
//        setRotationAngle(fin_pectoral_left, 0.4363F, 0.5672F, 0.0F);
//        fin_pectoral_left.texOffs(7, 45).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
//
//        fin_pectoral_right = new ModelPart(this);
//        fin_pectoral_right.setPos(-2.0F, -1.0F, -2.0F);
//        body_base.addChild(fin_pectoral_right);
//        setRotationAngle(fin_pectoral_right, 0.4363F, -0.5672F, 0.0F);
//        fin_pectoral_right.texOffs(0, 45).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
//
//        fin_dorsal = new ModelPart(this);
//        fin_dorsal.setPos(0.0F, -5.0F, -1.0F);
//        body_base.addChild(fin_dorsal);
//        setRotationAngle(fin_dorsal, -0.3054F, 0.0F, 0.0F);
//        fin_dorsal.texOffs(25, 0).addBox(0.0F, -3.0F, 0.0F, 0.0F, 3.0F, 7.0F, 0.0F, false);
//
//        body_connection = new ModelPart(this);
//        body_connection.setPos(0.0F, 1.0F, -3.0F);
//        body_base.addChild(body_connection);
//        body_connection.texOffs(28, 15).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);
//
//        jaw_lower = new ModelPart(this);
//        jaw_lower.setPos(0.0F, 0.0F, -4.0F);
//        body_connection.addChild(jaw_lower);
//        setRotationAngle(jaw_lower, -0.1309F, 0.0F, 0.0F);
//        jaw_lower.texOffs(15, 29).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 1.0F, 3.0F, 0.0F, false);
//
//        head_base = new ModelPart(this);
//        head_base.setPos(0.0F, -5.0F, -3.0F);
//        body_base.addChild(head_base);
//        setRotationAngle(head_base, 0.4363F, 0.0F, 0.0F);
//        head_base.texOffs(0, 15).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 4.0F, 4.0F, 0.01F, false);
//
//        head_snout = new ModelPart(this);
//        head_snout.setPos(0.0F, 0.0F, -4.0F);
//        head_base.addChild(head_snout);
//        setRotationAngle(head_snout, 0.3054F, 0.0F, 0.0F);
//
//        head_snout_r1 = new ModelPart(this);
//        head_snout_r1.setPos(0.0F, 0.0F, 0.0F);
//        head_snout.addChild(head_snout_r1);
//        setRotationAngle(head_snout_r1, -0.1309F, 0.0F, 0.0F);
//        head_snout_r1.texOffs(0, 29).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 3.0F, 3.0F, 0.005F, false);
//
//        tail_base = new ModelPart(this);
//        tail_base.setPos(0.0F, -4.5F, 5.0F);
//        body_base.addChild(tail_base);
//        setRotationAngle(tail_base, -0.0436F, 0.0F, 0.0F);
//        tail_base.texOffs(0, 37).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 5.0F, 2.0F, 0.0F, false);
//
//        tail_main = new ModelPart(this);
//        tail_main.setPos(0.0F, 0.5F, 2.0F);
//        tail_base.addChild(tail_main);
//        tail_main.texOffs(30, 29).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 3.0F, 0.0F, false);
//
//        fin_tail = new ModelPart(this);
//        fin_tail.setPos(0.0F, 0.0F, 3.0F);
//        tail_main.addChild(fin_tail);
//        fin_tail.texOffs(17, 15).addBox(0.0F, -2.0F, -1.0F, 0.0F, 8.0F, 5.0F, 0.0F, false);
    }

    public static LayerDefinition create() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition modelPartBody = modelPartData.addOrReplaceChild("body_base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-2.0F, -5.0F, -3.0F, 4.0F, 6.0F, 8.0F, false),
                PartPose.offsetAndRotation(0.0F, 20.0F, 0.0F, 0.0436F, 0.0F, 0.0F));

        modelPartBody.addOrReplaceChild("fin_anal",
                CubeListBuilder.create()
                        .texOffs(11, 37)
                        .addBox(0.0F, -1.0F, -1.0F, 0.0F, 2.0F, 4.0F, false),
                PartPose.offsetAndRotation(0.0F, 1.0F, 4.0F, -0.3054F, 0.0F, 0.0F));

        PartDefinition modelPartPelvicRight = modelPartBody.addOrReplaceChild("fin_pelvic_right",
                CubeListBuilder.create(),
                PartPose.offset(-1.5F, 1.0F, -2.0F));

        modelPartPelvicRight.addOrReplaceChild("fin_pelvic_right_r1",
                CubeListBuilder.create()
                        .texOffs(20, 37)
                        .addBox(0.0F, -1.5F, -0.5F, 0.0F, 2.0F, 3.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.3526F, -0.2182F, 0.0F));

        PartDefinition modelPartPelvicLeft = modelPartBody.addOrReplaceChild("fin_pelvic_left",
                CubeListBuilder.create(),
                PartPose.offset(1.5F, 1.0F, -2.0F));

        modelPartPelvicLeft.addOrReplaceChild("fin_pelvic_left_r1",
                CubeListBuilder.create()
                        .texOffs(20, 37)
                        .addBox(0.0F, -1.5F, -0.5F, 0.0F, 2.0F, 3.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.3526F, 0.2182F, 0.0F));

        modelPartBody.addOrReplaceChild("fin_pectoral_left",
                CubeListBuilder.create()
                        .texOffs(7, 45)
                        .addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 3.0F, false),
                PartPose.offsetAndRotation(2.0F, -1.0F, -2.0F, 0.4363F, 0.5672F, 0.0F));

        modelPartBody.addOrReplaceChild("fin_pectoral_right",
                CubeListBuilder.create()
                        .texOffs(0, 45)
                        .addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 3.0F, false),
                PartPose.offsetAndRotation(-2.0F, -1.0F, -2.0F, 0.4363F, -0.5672F, 0.0F));

        modelPartBody.addOrReplaceChild("fin_dorsal",
                CubeListBuilder.create()
                        .texOffs(25, 0)
                        .addBox(0.0F, -3.0F, 0.0F, 0.0F, 3.0F, 7.0F, false),
                PartPose.offsetAndRotation(0.0F, -5.0F, -1.0F, -0.3054F, 0.0F, 0.0F));

        PartDefinition modelPartBodyConnection = modelPartBody.addOrReplaceChild("body_connection",
                CubeListBuilder.create()
                        .texOffs(28, 15)
                        .addBox(-2.0F, -2.0F, -4.0F, 4.0F, 2.0F, 4.0F, false),
                PartPose.offset(0.0F, 1.0F, -3.0F));

        modelPartBodyConnection.addOrReplaceChild("jaw_lower",
                CubeListBuilder.create()
                        .texOffs(15, 29)
                        .addBox(-2.0F, -1.0F, -3.0F, 4.0F, 1.0F, 3.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition modelPartHead = modelPartBody.addOrReplaceChild("head_base",
                CubeListBuilder.create().mirror(false)
                        .texOffs(0, 15)
                        .addBox(-2.0F, 0.0F, -4.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.01F)),
                PartPose.offsetAndRotation(0.0F, -5.0F, -3.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition modelPartHeadSnout = modelPartHead.addOrReplaceChild("head_snout",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, 0.3054F, 0.0F, 0.0F));

        modelPartHeadSnout.addOrReplaceChild("head_snout_r1",
                CubeListBuilder.create().mirror(false)
                        .texOffs(0, 29)
                        .addBox(-2.0F, 0.0F, -3.0F, 4.0F, 3.0F, 3.0F, new CubeDeformation(0.005F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition modelPartTailBase = modelPartBody.addOrReplaceChild("tail_base",
                CubeListBuilder.create()
                        .texOffs(0, 37)
                        .addBox(-1.5F, 0.0F, 0.0F, 3.0F, 5.0F, 2.0F, false),
                PartPose.offsetAndRotation(0.0F, -4.5F, 5.0F, -0.0436F, 0.0F, 0.0F));

        PartDefinition modelPartTailMain = modelPartTailBase.addOrReplaceChild("tail_main",
                CubeListBuilder.create()
                        .texOffs(30, 29)
                        .addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 3.0F, false),
                PartPose.offset(0.0F, 0.5F, 2.0F));

        modelPartTailMain.addOrReplaceChild("fin_tail",
                CubeListBuilder.create()
                        .texOffs(17, 15)
                        .addBox(0.0F, -2.0F, -1.0F, 0.0F, 8.0F, 5.0F, false),
                PartPose.offset(0.0F, 0.0F, 3.0F));

        return LayerDefinition.create(modelData, 64, 64);
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
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body_base.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    private void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
