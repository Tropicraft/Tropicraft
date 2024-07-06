package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;

public class TapirModel<T extends Entity> extends TropicraftAgeableHierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart head_base;
    private final ModelPart leg_front_left;
    private final ModelPart leg_front_right;
    private final ModelPart leg_back_left;
    private final ModelPart leg_back_right;

    public TapirModel(ModelPart root) {
        this.root = root;
        ModelPart body_base = root.getChild("body_base");
        head_base = root.getChild("head_base");
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
                        .addBox(-5.0f, -3.0f, -11.0f, 10.0f, 10.0f, 18.0f, false),
                PartPose.offset(0.0f, 7.0f, 3.0f));

        PartDefinition head = root.addOrReplaceChild("head_base",
                CubeListBuilder.create()
                        .texOffs(0, 29)
                        .addBox(-4.0f, -3.0f, -10.0f, 8.0f, 8.0f, 10.0f, false),
                PartPose.offsetAndRotation(0.0f, 6.0f, -8.0f, 0.0873f, 0.0f, 0.0f));

        body.addOrReplaceChild("tail_base",
                CubeListBuilder.create()
                        .texOffs(4, 0)
                        .addBox(-1.5f, 0.0f, -1.0f, 3.0f, 5.0f, 1.0f, false),
                PartPose.offsetAndRotation(0.0f, -2.0f, 7.0f, 0.3054f, 0.0f, 0.0f));

        PartDefinition trunk = head.addOrReplaceChild("trunk_base",
                CubeListBuilder.create()
                        .texOffs(41, 0)
                        .addBox(-2.0f, 0.0f, -4.0f, 4.0f, 4.0f, 4.0f, false),
                PartPose.offsetAndRotation(0.0f, -2.0f, -10.0f, 0.6109f, 0.0f, 0.0f));

        trunk.addOrReplaceChild("trunk_tip",
                CubeListBuilder.create().mirror(false)
                        .texOffs(40, 10)
                        .addBox(-2.0f, 0.0f, -3.0f, 4.0f, 4.0f, 3.0f, new CubeDeformation(0.001f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, -4.0f, 0.2618f, 0.0f, 0.0f));

        PartDefinition earLeft = head.addOrReplaceChild("ear_left",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(4.0f, -3.0f, -2.0f, 0.0f, 0.2618f, 0.3491f));

        earLeft.addOrReplaceChild("ear_left_r1",
                CubeListBuilder.create()
                        .texOffs(17, 70)
                        .addBox(0.0f, -2.0f, -2.0f, 1.0f, 3.0f, 3.0f, false),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition earRight = head.addOrReplaceChild("ear_right",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-4.0f, -3.0f, -2.0f, 0.0f, -0.2618f, -0.3491f));

        earRight.addOrReplaceChild("ear_right_r1",
                CubeListBuilder.create()
                        .texOffs(17, 63)
                        .addBox(-1.0f, -2.0f, -2.0f, 1.0f, 3.0f, 3.0f, false),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        body.addOrReplaceChild("leg_front_left",
                CubeListBuilder.create()
                        .texOffs(0, 63)
                        .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 4.0f, false),
                PartPose.offset(3.0f, 7.0f, -9.0f));

        body.addOrReplaceChild("leg_front_right",
                CubeListBuilder.create()
                        .texOffs(34, 48)
                        .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 4.0f, false),
                PartPose.offset(-3.0f, 7.0f, -9.0f));

        body.addOrReplaceChild("leg_back_left",
                CubeListBuilder.create()
                        .texOffs(17, 48)
                        .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 4.0f, false),
                PartPose.offset(3.0f, 7.0f, 4.0f));

        body.addOrReplaceChild("leg_back_right",
                CubeListBuilder.create()
                        .texOffs(0, 48)
                        .addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 4.0f, false),
                PartPose.offset(-3.0f, 7.0f, 4.0f));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(head_base, headYaw, headPitch);

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.1f, limbSwingAmount)) {
            leg_front_left.xRot = walk.eval(1.0f, 1.0f);
            leg_front_right.xRot = walk.eval(-1.0f, 1.0f);
            leg_back_left.xRot = walk.eval(-1.0f, 1.0f);
            leg_back_right.xRot = walk.eval(1.0f, 1.0f);
        }
    }

    @Override
    protected ModelPart root() {
        return root;
    }

    @Override
    public ModelPart head() {
        return head_base;
    }
}
