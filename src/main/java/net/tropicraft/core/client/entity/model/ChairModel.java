package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.placeable.ChairEntity;

public class ChairModel extends HierarchicalModel<ChairEntity> {
    private final ModelPart root;

    public ChairModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0f, 0.0f, -8.0f, 16, 1, 16), PartPose.offset(-1.0f, 0.0f, 0.0f));
        root.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0f, 0.0f, 0.0f, 16, 1, 16), PartPose.offsetAndRotation(-1.0f, 0.0f, 8.0f, 1.169371f, 0.0f, 0.0f));
        root.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0f, -1.0f, 0.0f, 1, 10, 1), PartPose.offsetAndRotation(-8.0f, -3.0f, 6.0f, 0.4537856f, 0.0f, 0.0f));
        root.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, 0.0f, 0.0f, 1, 10, 1), PartPose.offsetAndRotation(8.0f, -4.0f, 5.0f, 0.4537856f, 0.0f, 0.0f));
        root.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(0, 0).addBox(0.0f, 0.0f, -1.0f, 1, 10, 1), PartPose.offsetAndRotation(8.0f, -4.0f, 0.0f, -0.4537856f, 0.0f, 0.0f));
        root.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0f, 0.0f, -1.0f, 1, 10, 1), PartPose.offsetAndRotation(-8.0f, -4.0f, 0.0f, -0.4537856f, 0.0f, 0.0f));
        root.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 29).addBox(0.0f, -1.0f, 0.0f, 14, 1, 2), PartPose.offsetAndRotation(-10.0f, -4.0f, 11.0f, 0.0f, 1.570796f, 0.0f));
        root.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(0, 29).addBox(0.0f, 0.0f, 0.0f, 14, 1, 2), PartPose.offsetAndRotation(8.0f, -5.0f, 11.0f, 0.0f, 1.570796f, 0.0f));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(ChairEntity chair, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
