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

        root.addOrReplaceChild("seat", CubeListBuilder.create().texOffs(0, 0).addBox(-7F, 0F, -8F, 16, 1, 16), PartPose.offset(-1F, 0F, 0F));
        root.addOrReplaceChild("back", CubeListBuilder.create().texOffs(0, 0).addBox(-7F, 0F, 0F, 16, 1, 16), PartPose.offsetAndRotation(-1F, 0F, 8F, 1.169371F, 0F, 0F));
        root.addOrReplaceChild("backRightLeg", CubeListBuilder.create().texOffs(0, 0).addBox(-1F, -1F, 0F, 1, 10, 1), PartPose.offsetAndRotation(-8F, -3F, 6F, 0.4537856F, 0F, 0F));
        root.addOrReplaceChild("backLeftLeg", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F, 0F, 1, 10, 1), PartPose.offsetAndRotation(8F, -4F, 5F, 0.4537856F, 0F, 0F));
        root.addOrReplaceChild("frontLeftLeg", CubeListBuilder.create().texOffs(0, 0).addBox(0F, 0F, -1F, 1, 10, 1), PartPose.offsetAndRotation(8F, -4F, 0F, -0.4537856F, 0F, 0F));
        root.addOrReplaceChild("frontRightLeg", CubeListBuilder.create().texOffs(0, 0).addBox(-1F, 0F, -1F, 1, 10, 1), PartPose.offsetAndRotation(-8F, -4F, 0F, -0.4537856F, 0F, 0F));
        root.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(0, 29).addBox(0F, -1F, 0F, 14, 1, 2), PartPose.offsetAndRotation(-10F, -4F, 11F, 0F, 1.570796F, 0F));
        root.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(0, 29).addBox(0F, 0F, 0F, 14, 1, 2), PartPose.offsetAndRotation(8F, -5F, 11F, 0F, 1.570796F, 0F));

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
