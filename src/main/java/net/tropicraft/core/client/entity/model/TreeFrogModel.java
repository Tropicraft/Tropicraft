package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.neutral.TreeFrogEntity;

public class TreeFrogModel extends HierarchicalModel<TreeFrogEntity> {
    private final ModelPart root;
    private final ModelPart frontLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart body;
    private final ModelPart rearRightLeg;
    private final ModelPart rearLeftLeg;
    private final ModelPart rightEye;
    private final ModelPart leftEye;

    public TreeFrogModel(ModelPart root) {
        this.root = root;
        frontLeftLeg = root.getChild("frontLeftLeg");
        frontRightLeg = root.getChild("frontRightLeg");
        body = root.getChild("body");
        rearRightLeg = root.getChild("rearRightLeg");
        rearLeftLeg = root.getChild("rearLeftLeg");
        rightEye = root.getChild("rightEye");
        leftEye = root.getChild("leftEye");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("frontLeftLeg",
                CubeListBuilder.create().texOffs(12, 19)
                        .addBox(-2F, 0.0F, -2F, 4, 1, 4),
                PartPose.offset(2.0F, 23F, -3F));

        root.addOrReplaceChild("frontRightLeg",
                CubeListBuilder.create().texOffs(12, 14)
                        .addBox(-2F, 0.0F, -2F, 4, 1, 4),
                PartPose.offset(-2F, 23F, -3F));

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(28, 8)
                        .addBox(-2F, -5F, -2F, 4, 9, 4),
                PartPose.offsetAndRotation(0.0F, 21F, 1.0F, 1.570796F, 0F, 0F));

        root.addOrReplaceChild("rearRightLeg",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-3F, 0.0F, -2F, 3, 5, 3),
                PartPose.offset(-2F, 19F, 4F));

        root.addOrReplaceChild("rearLeftLeg",
                CubeListBuilder.create().texOffs(0, 8)
                        .addBox(0.0F, 0.0F, -2F, 3, 5, 3),
                PartPose.offset(2.0F, 19F, 4F));

        root.addOrReplaceChild("rightEye",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2F, -1F, -1F, 2, 2, 2),
                PartPose.offset(-1F, 19F, -1F));

        root.addOrReplaceChild("leftEye",
                CubeListBuilder.create().texOffs(0, 4)
                        .addBox(0.0F, -1F, -1F, 2, 2, 2),
                PartPose.offset(1.0F, 19F, -1F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(TreeFrogEntity froog, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        frontLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        rearLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount;
        rearRightLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount;
        frontRightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
