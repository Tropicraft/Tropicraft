package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class JaguarModel<T extends Entity> extends TropicraftAgeableHierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart leg_back_left;
    private final ModelPart leg_front_left;
    private final ModelPart head_base;
    private final ModelPart leg_front_right;
    private final ModelPart leg_back_right;

    public JaguarModel(ModelPart root) {
        this.root = root;
        ModelPart body_base = root.getChild("body_base");
        leg_back_left = body_base.getChild("leg_back_left");
        ModelPart torso_main = body_base.getChild("torso_main");
        leg_front_left = torso_main.getChild("leg_front_left");
        head_base = root.getChild("head_base"); //???
        leg_front_right = torso_main.getChild("leg_front_right");
        leg_back_right = body_base.getChild("leg_back_right");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body_base",
                CubeListBuilder.create()
                        .texOffs(37, 0)
                        .addBox(-3.5f, -1.0f, -4.0f, 7.0f, 8.0f, 10.0f, false),
                PartPose.offset(0.0f, 9.0f, 4.0f));

        PartDefinition tail = body.addOrReplaceChild("tail_base",
                CubeListBuilder.create()
                        .texOffs(54, 20)
                        .addBox(-1.5f, 0.0f, 0.0f, 3.0f, 3.0f, 9.0f, false),
                PartPose.offsetAndRotation(0.0f, -1.0f, 6.0f, -1.0472f, 0.0f, 0.0f));

        PartDefinition tailTip = tail.addOrReplaceChild("tail_tip",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0f, 3.0f, 9.0f, 0.4363f, 0.0f, 0.0f));

        tailTip.addOrReplaceChild("tail_tip_r1",
                CubeListBuilder.create().mirror(false)
                        .texOffs(29, 20)
                        .addBox(-1.5f, -3.0f, 0.0f, 3.0f, 3.0f, 9.0f, new CubeDeformation(0.001f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.1745f, 0.0f, 0.0f));

        body.addOrReplaceChild("leg_back_left",
                CubeListBuilder.create()
                        .texOffs(0, 55)
                        .addBox(-3.0f, -2.0f, -2.0f, 3.0f, 13.0f, 4.0f, false),
                PartPose.offset(4.5f, 4.0f, 3.0f));

        PartDefinition torso = body.addOrReplaceChild("torso_main",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0f, -1.0f, -10.0f, 8.0f, 9.0f, 10.0f, false),
                PartPose.offset(0.0f, 0.0f, -4.0f));

        torso.addOrReplaceChild("leg_front_left",
                CubeListBuilder.create()
                        .texOffs(15, 35)
                        .addBox(-2.0f, -2.0f, -1.0f, 3.0f, 15.0f, 4.0f, false),
                PartPose.offset(4.0f, 2.0f, -8.0f));

        PartDefinition head = root.addOrReplaceChild("head_base",
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(-3.5f, -2.0f, -7.0f, 7.0f, 7.0f, 7.0f, false),
                PartPose.offsetAndRotation(0.0f, 9.0f, -10.0f, 0.0436f, 0.0f, 0.0f));

        head.addOrReplaceChild("head_snout",
                CubeListBuilder.create()
                        .texOffs(15, 55)
                        .addBox(-2.5f, 0.0f, -4.0f, 5.0f, 6.0f, 4.0f, false),
                PartPose.offsetAndRotation(0.0f, -1.0f, -7.0f, 0.2618f, 0.0f, 0.0f));

        PartDefinition earLeft = head.addOrReplaceChild("ear_left",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(2.0f, -2.0f, -3.0f, 0.0f, -0.5672f, 0.3927f));

        earLeft.addOrReplaceChild("ear_left_r1",
                CubeListBuilder.create()
                        .texOffs(34, 55)
                        .addBox(0.0f, -2.0f, 0.0f, 3.0f, 3.0f, 1.0f, false),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition earRight = head.addOrReplaceChild("ear_right",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-2.0f, -2.0f, -3.0f, 0.0f, 0.5672f, -0.3927f));

        earRight.addOrReplaceChild("ear_right_r1",
                CubeListBuilder.create()
                        .texOffs(15, 66)
                        .addBox(-3.0f, -2.0f, 0.0f, 3.0f, 3.0f, 1.0f, false),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        torso.addOrReplaceChild("leg_front_right",
                CubeListBuilder.create()
                        .texOffs(0, 35)
                        .addBox(-1.0f, -2.0f, -1.0f, 3.0f, 15.0f, 4.0f, false),
                PartPose.offset(-4.0f, 2.0f, -8.0f));

        body.addOrReplaceChild("leg_back_right",
                CubeListBuilder.create()
                        .texOffs(30, 35)
                        .addBox(0.0f, -2.0f, -2.0f, 3.0f, 13.0f, 4.0f, false),
                PartPose.offset(-4.5f, 4.0f, 3.0f));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(head_base, headYaw, headPitch);

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.1f, limbSwingAmount)) {
            leg_front_left.xRot = walk.eval(1.0f, 0.8f);
            leg_front_right.xRot = walk.eval(-1.0f, 0.8f);
            leg_back_left.xRot = walk.eval(-1.0f, 0.8f);
            leg_back_right.xRot = walk.eval(1.0f, 0.8f);
        }
    }

    @Override
    protected ModelPart root() {
        return root;
    }

    @Override
    protected ModelPart head() {
        return head_base;
    }
}
