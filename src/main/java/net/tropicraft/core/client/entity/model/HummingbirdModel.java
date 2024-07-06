package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.passive.HummingbirdEntity;

public class HummingbirdModel<T extends HummingbirdEntity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart wing_left;
    private final ModelPart head_base;
    private final ModelPart wing_right;

    public HummingbirdModel(ModelPart root) {
        this.root = root;
        wing_left = root.getChild("wing_left");
        head_base = root.getChild("head_base");
        wing_right = root.getChild("wing_right");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body_base", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-1.0f, -2.0f, -1.0f, 2.0f, 3.0f, 2.0f),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.4363f, 0.0f, 0.0f));

        root.addOrReplaceChild("tail_base", CubeListBuilder.create()
                        .texOffs(0, 6).addBox(-1.5f, 0.0f, 0.0f, 3.0f, 4.0f, 0.0f),
                PartPose.offsetAndRotation(0.0f, 1.0f, 1.0f, 0.2618f, 0.0f, 0.0f));

        root.addOrReplaceChild("wing_left", CubeListBuilder.create()
                        .texOffs(9, 11).addBox(0.0f, 0.0f, 0.0f, 4.0f, 2.0f, 0.0f),
                PartPose.offsetAndRotation(1.0f, -2.0f, 1.0f, 0.0f, 0.0f, 0.0f));

        PartDefinition headBase = root.addOrReplaceChild("head_base", CubeListBuilder.create()
                        .texOffs(9, 0).addBox(-1.0f, -2.0f, -1.0f, 2.0f, 2.0f, 2.0f, new CubeDeformation(0.00f)),
                PartPose.offsetAndRotation(0.0f, -2.0f, 0.0f, -0.2618f, 0.0f, 0.0f));

        headBase.addOrReplaceChild("beak_base", CubeListBuilder.create()
                        .texOffs(7, 6).addBox(0.0f, 0.0f, -3.0f, 0.0f, 1.0f, 3.0f),
                PartPose.offsetAndRotation(0.0f, -2.0f, -1.0f, 0.3927f, 0.0f, 0.0f));

        root.addOrReplaceChild("wing_right", CubeListBuilder.create()
                        .texOffs(0, 11).addBox(-4.0f, 0.0f, 0.0f, 4.0f, 2.0f, 0.0f),
                PartPose.offsetAndRotation(-1.0f, -2.0f, 1.0f, 0.0f, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(head_base, headYaw, headPitch);

        try (ModelAnimator.Cycle fly = ModelAnimator.cycle(age * 0.25f, 1.0f)) {
            root.y = 20.0f + fly.eval(1.0f, 0.1f);

            wing_right.yRot = fly.eval(1.0f, 1.0f, 0.0f, 0.0f);
            wing_left.yRot = fly.eval(1.0f, -1.0f, 0.0f, 0.0f);
            wing_right.zRot = fly.eval(1.0f, 0.4f, 0.0f, 0.3f);
            wing_left.zRot = fly.eval(1.0f, -0.4f, 0.0f, -0.3f);
            wing_right.xRot = fly.eval(1.0f, 0.4f, 0.1f, 0.2f);
            wing_left.xRot = fly.eval(1.0f, 0.4f, 0.1f, 0.2f);
        }
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
