package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class TapirModel<T extends Entity> extends TropicraftAgeableModel<T> {
    private final ModelPart body_base;
    private final ModelPart head_base;
    private final ModelPart tail_base;
    private final ModelPart trunk_base;
    private final ModelPart trunk_tip;
    private final ModelPart ear_left;
    private final ModelPart ear_left_r1;
    private final ModelPart ear_right;
    private final ModelPart ear_right_r1;
    private final ModelPart leg_front_left;
    private final ModelPart leg_front_right;
    private final ModelPart leg_back_left;
    private final ModelPart leg_back_right;

    public TapirModel(ModelPart root) {
        body_base = root.getChild("body_base");
        head_base = root.getChild("body_base");
        tail_base = body_base.getChild("body_base");
        trunk_base = head_base.getChild("body_base");
        trunk_tip = trunk_base.getChild("body_base");
        ear_left = head_base.getChild("body_base");
        ear_left_r1 = ear_left.getChild("body_base");
        ear_right = head_base.getChild("body_base");
        ear_right_r1 = ear_right.getChild("body_base");
        leg_front_left = body_base.getChild("body_base");
        leg_front_right = body_base.getChild("body_base");
        leg_back_left = body_base.getChild("body_base");
        leg_back_right = body_base.getChild("body_base");

//        texWidth = 128;
//        texHeight = 128;
//
//        body_base = new ModelPart(this);
//        body_base.setPos(0.0F, 7.0F, 3.0F);
//        body_base.texOffs(0, 0).addBox(-5.0F, -3.0F, -11.0F, 10.0F, 10.0F, 18.0F, 0.0F, false);
//
//        head_base = new ModelPart(this);
//        head_base.setPos(0.0F, 6.0F, -8.0F);
//        setRotationAngle(head_base, 0.0873F, 0.0F, 0.0F);
//        head_base.texOffs(0, 29).addBox(-4.0F, -3.0F, -10.0F, 8.0F, 8.0F, 10.0F, 0.0F, false);
//
//        tail_base = new ModelPart(this);
//        tail_base.setPos(0.0F, -2.0F, 7.0F);
//        body_base.addChild(tail_base);
//        setRotationAngle(tail_base, 0.3054F, 0.0F, 0.0F);
//        tail_base.texOffs(4, 0).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 1.0F, 0.0F, false);
//
//        trunk_base = new ModelPart(this);
//        trunk_base.setPos(0.0F, -2.0F, -10.0F);
//        head_base.addChild(trunk_base);
//        setRotationAngle(trunk_base, 0.6109F, 0.0F, 0.0F);
//        trunk_base.texOffs(41, 0).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
//
//        trunk_tip = new ModelPart(this);
//        trunk_tip.setPos(0.0F, 0.0F, -4.0F);
//        trunk_base.addChild(trunk_tip);
//        setRotationAngle(trunk_tip, 0.2618F, 0.0F, 0.0F);
//        trunk_tip.texOffs(40, 10).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 4.0F, 3.0F, 0.001F, false);
//
//        ear_left = new ModelPart(this);
//        ear_left.setPos(4.0F, -3.0F, -2.0F);
//        head_base.addChild(ear_left);
//        setRotationAngle(ear_left, 0.0F, 0.2618F, 0.3491F);
//
//        ear_left_r1 = new ModelPart(this);
//        ear_left_r1.setPos(0.0F, 0.0F, 0.0F);
//        ear_left.addChild(ear_left_r1);
//        setRotationAngle(ear_left_r1, 0.0F, 0.0F, 0.0F);
//        ear_left_r1.texOffs(17, 70).addBox(0.0F, -2.0F, -2.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
//
//        ear_right = new ModelPart(this);
//        ear_right.setPos(-4.0F, -3.0F, -2.0F);
//        head_base.addChild(ear_right);
//        setRotationAngle(ear_right, 0.0F, -0.2618F, -0.3491F);
//
//        ear_right_r1 = new ModelPart(this);
//        ear_right_r1.setPos(0.0F, 0.0F, 0.0F);
//        ear_right.addChild(ear_right_r1);
//        setRotationAngle(ear_right_r1, 0.0F, 0.0F, 0.0F);
//        ear_right_r1.texOffs(17, 63).addBox(-1.0F, -2.0F, -2.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
//
//        leg_front_left = new ModelPart(this);
//        leg_front_left.setPos(3.0F, 7.0F, -9.0F);
//        body_base.addChild(leg_front_left);
//        leg_front_left.texOffs(0, 63).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
//
//        leg_front_right = new ModelPart(this);
//        leg_front_right.setPos(-3.0F, 7.0F, -9.0F);
//        body_base.addChild(leg_front_right);
//        leg_front_right.texOffs(34, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
//
//        leg_back_left = new ModelPart(this);
//        leg_back_left.setPos(3.0F, 7.0F, 4.0F);
//        body_base.addChild(leg_back_left);
//        leg_back_left.texOffs(17, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
//
//        leg_back_right = new ModelPart(this);
//        leg_back_right.setPos(-3.0F, 7.0F, 4.0F);
//        body_base.addChild(leg_back_right);
//        leg_back_right.texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
    }

    public static LayerDefinition create() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition modelPartBody = modelPartData.addOrReplaceChild("body_base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-5.0F, -3.0F, -11.0F, 10.0F, 10.0F, 18.0F, false),
                PartPose.offset(0.0F, 7.0F, 3.0F));

        PartDefinition modelPartHead = modelPartData.addOrReplaceChild("head_base",
                CubeListBuilder.create()
                        .texOffs(0, 29)
                        .addBox(-4.0F, -3.0F, -10.0F, 8.0F, 8.0F, 10.0F, false),
                PartPose.offsetAndRotation(0.0F, 6.0F, -8.0F, 0.0873F, 0.0F, 0.0F));

        modelPartBody.addOrReplaceChild("tail_base",
                CubeListBuilder.create()
                        .texOffs(4, 0)
                        .addBox(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 1.0F, false),
                PartPose.offsetAndRotation(0.0F, -2.0F, 7.0F, 0.3054F, 0.0F, 0.0F));

        PartDefinition modelPartTrunk = modelPartHead.addOrReplaceChild("trunk_base",
                CubeListBuilder.create()
                        .texOffs(41, 0)
                        .addBox(-2.0F, 0.0F, -4.0F, 4.0F, 4.0F, 4.0F, false),
                PartPose.offsetAndRotation(0.0F, -2.0F, -10.0F, 0.6109F, 0.0F, 0.0F));

        modelPartTrunk.addOrReplaceChild("trunk_tip",
                CubeListBuilder.create().mirror(false)
                        .texOffs(40, 10)
                        .addBox(-2.0F, 0.0F, -3.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(.001F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition modelPartEarLeft = modelPartHead.addOrReplaceChild("ear_left",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(4.0F, -3.0F, -2.0F, 0.0F, 0.2618F, 0.3491F));

        modelPartEarLeft.addOrReplaceChild("ear_left_r1",
                CubeListBuilder.create()
                        .texOffs(17, 70)
                        .addBox(0.0F, -2.0F, -2.0F, 1.0F, 3.0F, 3.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition modelPartEarRight = modelPartHead.addOrReplaceChild("ear_right",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-4.0F, -3.0F, -2.0F, 0.0F, -0.2618F, -0.3491F));

        modelPartEarRight.addOrReplaceChild("ear_right_r1",
                CubeListBuilder.create()
                        .texOffs(17, 63)
                        .addBox(-1.0F, -2.0F, -2.0F, 1.0F, 3.0F, 3.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        modelPartBody.addOrReplaceChild("leg_front_left",
                CubeListBuilder.create()
                        .texOffs(0, 63)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, false),
                PartPose.offset(3.0F, 7.0F, -9.0F));

        modelPartBody.addOrReplaceChild("leg_front_right",
                CubeListBuilder.create()
                        .texOffs(34, 48)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, false),
                PartPose.offset(-3.0F, 7.0F, -9.0F));

        modelPartBody.addOrReplaceChild("leg_back_left",
                CubeListBuilder.create()
                        .texOffs(17, 48)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, false),
                PartPose.offset(3.0F, 7.0F, 4.0F));

        modelPartBody.addOrReplaceChild("leg_back_left",
                CubeListBuilder.create()
                        .texOffs(0, 48)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, false),
                PartPose.offset(-3.0F, 7.0F, 4.0F));

        return LayerDefinition.create(modelData, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(head_base, headYaw, headPitch);

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.1F, limbSwingAmount)) {
            leg_front_left.xRot = walk.eval(1.0F, 1.0F);
            leg_front_right.xRot = walk.eval(-1.0F, 1.0F);
            leg_back_left.xRot = walk.eval(-1.0F, 1.0F);
            leg_back_right.xRot = walk.eval(1.0F, 1.0F);
        }
    }

    @Override
    protected ModelPart getHead() {
        return head_base;
    }

    @Override
    protected ModelPart getBody() {
        return body_base;
    }

    private void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
