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
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body_base",
                CubeListBuilder.create()
                        .texOffs(37, 0)
                        .addBox(-3.5F, -1.0F, -4.0F, 7.0F, 8.0F, 10.0F, false),
                PartPose.offset(0.0F, 9.0F, 4.0F));

        PartDefinition tail = body.addOrReplaceChild("tail_base",
                CubeListBuilder.create()
                        .texOffs(54, 20)
                        .addBox(-1.5F, 0.0F, 0.0F, 3.0F, 3.0F, 9.0F, false),
                PartPose.offsetAndRotation(0.0F, -1.0F, 6.0F, -1.0472F, 0.0F, 0.0F));

        PartDefinition tailTip = tail.addOrReplaceChild("tail_tip",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, 3.0F, 9.0F, 0.4363F, 0.0F, 0.0F));

        tailTip.addOrReplaceChild("tail_tip_r1",
                CubeListBuilder.create().mirror(false)
                        .texOffs(29, 20)
                        .addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 9.0F, new CubeDeformation(0.001F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        body.addOrReplaceChild("leg_back_left",
                CubeListBuilder.create()
                        .texOffs(0, 55)
                        .addBox(-3.0F, -2.0F, -2.0F, 3.0F, 13.0F, 4.0F, false),
                PartPose.offset(4.5F, 4.0F, 3.0F));

        PartDefinition torso = body.addOrReplaceChild("torso_main",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -1.0F, -10.0F, 8.0F, 9.0F, 10.0F, false),
                PartPose.offset(0.0F, 0.0F, -4.0F));

        torso.addOrReplaceChild("leg_front_left",
                CubeListBuilder.create()
                        .texOffs(15, 35)
                        .addBox(-2.0F, -2.0F, -1.0F, 3.0F, 15.0F, 4.0F, false),
                PartPose.offset(4.0F, 2.0F, -8.0F));

        PartDefinition head = root.addOrReplaceChild("head_base",
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-3.5F, -2.0F, -7.0F, 7.0F, 7.0F, 7.0F, false),
                PartPose.offsetAndRotation(0.0F, 9.0F, -10.0F, 0.0436F, 0.0F, 0.0F));

        head.addOrReplaceChild("head_snout",
                CubeListBuilder.create()
                        .texOffs(15, 55)
                        .addBox(-2.5F, 0.0F, -4.0F, 5.0F, 6.0F, 4.0F, false),
                PartPose.offsetAndRotation(0.0F, -1.0F, -7.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition earLeft = head.addOrReplaceChild("ear_left",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(2.0F, -2.0F, -3.0F, 0.0F, -0.5672F, 0.3927F));

        earLeft.addOrReplaceChild("ear_left_r1",
                CubeListBuilder.create()
                        .texOffs(34, 55)
                        .addBox(0.0F, -2.0F, 0.0F, 3.0F, 3.0F, 1.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition earRight = head.addOrReplaceChild("ear_right",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-2.0F, -2.0F, -3.0F, 0.0F, 0.5672F, -0.3927F));

        earRight.addOrReplaceChild("ear_right_r1",
                CubeListBuilder.create()
                        .texOffs(15, 66)
                        .addBox(-3.0F, -2.0F, 0.0F, 3.0F, 3.0F, 1.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        torso.addOrReplaceChild("leg_front_right",
                CubeListBuilder.create()
                        .texOffs(0, 35)
                        .addBox(-1.0F, -2.0F, -1.0F, 3.0F, 15.0F, 4.0F, false),
                PartPose.offset(-4.0F, 2.0F, -8.0F));

        body.addOrReplaceChild("leg_back_right",
                CubeListBuilder.create()
                        .texOffs(30, 35)
                        .addBox(0.0F, -2.0F, -2.0F, 3.0F, 13.0F, 4.0F, false),
                PartPose.offset(-4.5F, 4.0F, 3.0F));

        return LayerDefinition.create(mesh, 128, 128);
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
}