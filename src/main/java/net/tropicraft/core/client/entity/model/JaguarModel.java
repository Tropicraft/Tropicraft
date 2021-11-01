package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class JaguarModel<T extends Entity> extends TropicraftAgeableModel<T> {
    private final ModelPart body_base;
    private final ModelPart tail_base;
    private final ModelPart tail_tip;
    private final ModelPart tail_tip_r1;
    private final ModelPart leg_back_left;
    private final ModelPart torso_main;
    private final ModelPart leg_front_left;
    private final ModelPart head_base;
    private final ModelPart ear_left;
    private final ModelPart ear_left_r1;
    private final ModelPart head_snout;
    private final ModelPart ear_right;
    private final ModelPart ear_right_r1;
    private final ModelPart leg_front_right;
    private final ModelPart leg_back_right;

    public JaguarModel(ModelPart root) {
        body_base = root.getChild("body_base");
        tail_base = body_base.getChild("tail_base");
        tail_tip = tail_base.getChild("tail_tip");
        tail_tip_r1 = tail_tip.getChild("tail_tip_r1");
        leg_back_left = body_base.getChild("leg_back_left");
        torso_main = body_base.getChild("torso_main");
        leg_front_left = torso_main.getChild("leg_front_left");

        head_base = root.getChild("head_base"); //???
        ear_left = head_base.getChild("ear_left");
        ear_left_r1 = ear_left.getChild("ear_left_r1");
        head_snout = head_base.getChild("head_snout");
        ear_right = head_base.getChild("ear_right");
        ear_right_r1 = ear_right.getChild("ear_right_r1");

        leg_front_right = torso_main.getChild("leg_front_right");
        leg_back_right = body_base.getChild("leg_back_right");

//        texWidth = 128;
//        texHeight = 128;
//
//        body_base = new ModelPart(this);
//        body_base.setPos(0.0F, 9.0F, 4.0F);
//        body_base.texOffs(37, 0).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 8.0F, 10.0F, 0.0F, false);
//
//        tail_base = new ModelPart(this);
//        tail_base.setPos(0.0F, -1.0F, 6.0F);
//        body_base.addChild(tail_base);
//        setRotationAngle(tail_base, -1.0472F, 0.0F, 0.0F);
//        tail_base.texOffs(54, 20).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 3.0F, 9.0F, 0.0F, false);
//
//        tail_tip = new ModelPart(this);
//        tail_tip.setPos(0.0F, 3.0F, 9.0F);
//        tail_base.addChild(tail_tip);
//        setRotationAngle(tail_tip, 0.4363F, 0.0F, 0.0F);
//
//        tail_tip_r1 = new ModelPart(this);
//        tail_tip_r1.setPos(0.0F, 0.0F, 0.0F);
//        tail_tip.addChild(tail_tip_r1);
//        setRotationAngle(tail_tip_r1, 0.1745F, 0.0F, 0.0F);
//        tail_tip_r1.texOffs(29, 20).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 9.0F, 0.001F, false);
//
//        leg_back_left = new ModelPart(this);
//        leg_back_left.setPos(4.5F, 4.0F, 3.0F);
//        body_base.addChild(leg_back_left);
//        leg_back_left.texOffs(0, 55).addBox(-3.0F, -2.0F, -2.0F, 3.0F, 13.0F, 4.0F, 0.0F, false);
//
//        torso_main = new ModelPart(this);
//        torso_main.setPos(0.0F, 0.0F, -4.0F);
//        body_base.addChild(torso_main);
//        torso_main.texOffs(0, 0).addBox(-4.0F, -1.0F, -10.0F, 8.0F, 9.0F, 10.0F, 0.0F, false);
//
//        leg_front_left = new ModelPart(this);
//        leg_front_left.setPos(4.0F, 2.0F, -8.0F);
//        torso_main.addChild(leg_front_left);
//        leg_front_left.texOffs(15, 35).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 15.0F, 4.0F, 0.0F, false);
//
//        head_base = new ModelPart(this);
//        head_base.setPos(0.0F, 9.0F, -10.0F);
//        setRotationAngle(head_base, 0.0436F, 0.0F, 0.0F);
//        head_base.texOffs(0, 20).addBox(-3.5F, -2.0F, -7.0F, 7.0F, 7.0F, 7.0F, 0.0F, false);
//
//        ear_left = new ModelPart(this);
//        ear_left.setPos(2.0F, -2.0F, -3.0F);
//        head_base.addChild(ear_left);
//        setRotationAngle(ear_left, 0.0F, -0.5672F, 0.3927F);
//
//        ear_left_r1 = new ModelPart(this);
//        ear_left_r1.setPos(0.0F, 0.0F, 0.0F);
//        ear_left.addChild(ear_left_r1);
//        setRotationAngle(ear_left_r1, 0.0F, 0.0F, 0.0F);
//        ear_left_r1.texOffs(34, 55).addBox(0.0F, -2.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
//
//        head_snout = new ModelPart(this);
//        head_snout.setPos(0.0F, -1.0F, -7.0F);
//        head_base.addChild(head_snout);
//        setRotationAngle(head_snout, 0.2618F, 0.0F, 0.0F);
//        head_snout.texOffs(15, 55).addBox(-2.5F, 0.0F, -4.0F, 5.0F, 6.0F, 4.0F, 0.0F, false);
//
//        ear_right = new ModelPart(this);
//        ear_right.setPos(-2.0F, -2.0F, -3.0F);
//        head_base.addChild(ear_right);
//        setRotationAngle(ear_right, 0.0F, 0.5672F, -0.3927F);
//
//        ear_right_r1 = new ModelPart(this);
//        ear_right_r1.setPos(0.0F, 0.0F, 0.0F);
//        ear_right.addChild(ear_right_r1);
//        setRotationAngle(ear_right_r1, 0.0F, 0.0F, 0.0F);
//        ear_right_r1.texOffs(15, 66).addBox(-3.0F, -2.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
//
//        leg_front_right = new ModelPart(this);
//        leg_front_right.setPos(-4.0F, 2.0F, -8.0F);
//        torso_main.addChild(leg_front_right);
//        leg_front_right.texOffs(0, 35).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 15.0F, 4.0F, 0.0F, false);
//
//        leg_back_right = new ModelPart(this);
//        leg_back_right.setPos(-4.5F, 4.0F, 3.0F);
//        body_base.addChild(leg_back_right);
//        leg_back_right.texOffs(30, 35).addBox(0.0F, -2.0F, -2.0F, 3.0F, 13.0F, 4.0F, 0.0F, false);
    }


    public static LayerDefinition create() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition modelPartBody = modelPartData.addOrReplaceChild("body_base",
                CubeListBuilder.create()
                        .texOffs(37, 0)
                        .addBox(-3.5F, -1.0F, -4.0F, 7.0F, 8.0F, 10.0F, false),
                PartPose.offset(0.0F, 9.0F, 4.0F));

        PartDefinition modelPartTail = modelPartBody.addOrReplaceChild("tail_base",
                CubeListBuilder.create()
                        .texOffs(54, 20)
                        .addBox(-1.5F, 0.0F, 0.0F, 3.0F, 3.0F, 9.0F, false),
                PartPose.offsetAndRotation(0.0F, -1.0F, 6.0F, -1.0472F, 0.0F, 0.0F));

        PartDefinition modelPartTailTip = modelPartTail.addOrReplaceChild("tail_tip",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 3.0F, 9.0F, 0.4363F, 0.0F, 0.0F));

        modelPartTailTip.addOrReplaceChild("tail_tip_r1",
                CubeListBuilder.create().mirror(false)
                        .texOffs(29, 20)
                        .addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        modelPartBody.addOrReplaceChild("leg_back_left",
                CubeListBuilder.create()
                        .texOffs(0, 55)
                        .addBox(-3.0F, -2.0F, -2.0F, 3.0F, 13.0F, 4.0F, false),
                PartPose.offset(4.5F, 4.0F, 3.0F));

        PartDefinition modelPartTorso = modelPartBody.addOrReplaceChild("torso_main",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -1.0F, -10.0F, 8.0F, 9.0F, 10.0F, false),
                PartPose.offset(0.0F, 0.0F, -4.0F));

        modelPartTorso.addOrReplaceChild("leg_front_left",
                CubeListBuilder.create()
                        .texOffs(15, 35)
                        .addBox(-2.0F, -2.0F, -1.0F, 3.0F, 15.0F, 4.0F, false),
                PartPose.offset(4.0F, 2.0F, -8.0F));

        PartDefinition modelPartHead = modelPartData.addOrReplaceChild("head_base",
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-3.5F, -2.0F, -7.0F, 7.0F, 7.0F, 7.0F, false),
                PartPose.offsetAndRotation(0.0F, 9.0F, -10.0F, 0.0436F, 0.0F, 0.0F));

        modelPartHead.addOrReplaceChild("head_snout",
                CubeListBuilder.create()
                        .texOffs(15, 55)
                        .addBox(-2.5F, 0.0F, -4.0F, 5.0F, 6.0F, 4.0F, false),
                PartPose.offsetAndRotation(0.0F, -1.0F, -7.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition modelPartEarLeft = modelPartHead.addOrReplaceChild("ear_left",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(2.0F, -2.0F, -3.0F, 0.0F, -0.5672F, 0.3927F));

        modelPartEarLeft.addOrReplaceChild("ear_left_r1",
                CubeListBuilder.create()
                        .texOffs(34, 55)
                        .addBox(0.0F, -2.0F, 0.0F, 3.0F, 3.0F, 1.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition modelPartEarRight = modelPartHead.addOrReplaceChild("ear_right",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-2.0F, -2.0F, -3.0F, 0.0F, 0.5672F, -0.3927F));

        modelPartEarRight.addOrReplaceChild("ear_right_r1",
                CubeListBuilder.create()
                        .texOffs(15, 66)
                        .addBox(-3.0F, -2.0F, 0.0F, 3.0F, 3.0F, 1.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        modelPartTorso.addOrReplaceChild("leg_front_right",
                CubeListBuilder.create()
                        .texOffs(0, 35)
                        .addBox(-1.0F, -2.0F, -1.0F, 3.0F, 15.0F, 4.0F, false),
                PartPose.offset(-4.0F, 2.0F, -8.0F));

        modelPartBody.addOrReplaceChild("leg_back_right",
                CubeListBuilder.create()
                        .texOffs(30, 35)
                        .addBox(0.0F, -2.0F, -2.0F, 3.0F, 13.0F, 4.0F, false),
                PartPose.offset(-4.5F, 4.0F, 3.0F));

        return LayerDefinition.create(modelData, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(head_base, headYaw, headPitch);

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.1F, limbSwingAmount)) {
            leg_front_left.xRot = walk.eval(1.0F, 0.8F);
            leg_front_right.xRot = walk.eval(-1.0F, 0.8F);
            leg_back_left.xRot = walk.eval(-1.0F, 0.8F);
            leg_back_right.xRot = walk.eval(1.0F, 0.8F);
        }
    }

    @Override
    protected ModelPart getHead() {
        return this.head_base;
    }

    @Override
    protected ModelPart getBody() {
        return this.body_base;
    }

    private void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
