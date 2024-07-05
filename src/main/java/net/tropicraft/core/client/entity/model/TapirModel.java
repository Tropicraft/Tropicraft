package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
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
        head_base = root.getChild("head_base");
        tail_base = body_base.getChild("tail_base");
        trunk_base = head_base.getChild("trunk_base");
        trunk_tip = trunk_base.getChild("trunk_tip");
        ear_left = head_base.getChild("ear_left");
        ear_left_r1 = ear_left.getChild("ear_left_r1");
        ear_right = head_base.getChild("ear_right");
        ear_right_r1 = ear_right.getChild("ear_right_r1");
        leg_front_left = body_base.getChild("leg_front_left");
        leg_front_right = body_base.getChild("leg_front_right");
        leg_back_left = body_base.getChild("leg_back_left");
        leg_back_right = body_base.getChild("leg_back_right");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body_base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-5.0F, -3.0F, -11.0F, 10.0F, 10.0F, 18.0F, false),
                PartPose.offset(0.0F, 7.0F, 3.0F));

        PartDefinition head = root.addOrReplaceChild("head_base",
                CubeListBuilder.create()
                        .texOffs(0, 29)
                        .addBox(-4.0F, -3.0F, -10.0F, 8.0F, 8.0F, 10.0F, false),
                PartPose.offsetAndRotation(0.0F, 6.0F, -8.0F, 0.0873F, 0.0F, 0.0F));

        body.addOrReplaceChild("tail_base",
                CubeListBuilder.create()
                        .texOffs(4, 0)
                        .addBox(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 1.0F, false),
                PartPose.offsetAndRotation(0.0F, -2.0F, 7.0F, 0.3054F, 0.0F, 0.0F));

        PartDefinition trunk = head.addOrReplaceChild("trunk_base",
                CubeListBuilder.create()
                        .texOffs(41, 0)
                        .addBox(-2.0F, 0.0F, -4.0F, 4.0F, 4.0F, 4.0F, false),
                PartPose.offsetAndRotation(0.0F, -2.0F, -10.0F, 0.6109F, 0.0F, 0.0F));

        trunk.addOrReplaceChild("trunk_tip",
                CubeListBuilder.create().mirror(false)
                        .texOffs(40, 10)
                        .addBox(-2.0F, 0.0F, -3.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(.001F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition earLeft = head.addOrReplaceChild("ear_left",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(4.0F, -3.0F, -2.0F, 0.0F, 0.2618F, 0.3491F));

        earLeft.addOrReplaceChild("ear_left_r1",
                CubeListBuilder.create()
                        .texOffs(17, 70)
                        .addBox(0.0F, -2.0F, -2.0F, 1.0F, 3.0F, 3.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition earRight = head.addOrReplaceChild("ear_right",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-4.0F, -3.0F, -2.0F, 0.0F, -0.2618F, -0.3491F));

        earRight.addOrReplaceChild("ear_right_r1",
                CubeListBuilder.create()
                        .texOffs(17, 63)
                        .addBox(-1.0F, -2.0F, -2.0F, 1.0F, 3.0F, 3.0F, false),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        body.addOrReplaceChild("leg_front_left",
                CubeListBuilder.create()
                        .texOffs(0, 63)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, false),
                PartPose.offset(3.0F, 7.0F, -9.0F));

        body.addOrReplaceChild("leg_front_right",
                CubeListBuilder.create()
                        .texOffs(34, 48)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, false),
                PartPose.offset(-3.0F, 7.0F, -9.0F));

        body.addOrReplaceChild("leg_back_left",
                CubeListBuilder.create()
                        .texOffs(17, 48)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, false),
                PartPose.offset(3.0F, 7.0F, 4.0F));

        body.addOrReplaceChild("leg_back_right",
                CubeListBuilder.create()
                        .texOffs(0, 48)
                        .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, false),
                PartPose.offset(-3.0F, 7.0F, 4.0F));

        return LayerDefinition.create(mesh, 128, 128);
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
    public ModelPart getHead() {
        return head_base;
    }

    @Override
    protected ModelPart getBody() {
        return body_base;
    }
}
