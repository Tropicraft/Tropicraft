package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.neutral.TreeFrogEntity;

public class TreeFrogModel extends ListModel<TreeFrogEntity> {
    public ModelPart frontLeftLeg;
    public ModelPart frontRightLeg;
    public ModelPart body;
    public ModelPart rearRightLeg;
    public ModelPart rearLeftLeg;
    public ModelPart rightEye;
    public ModelPart leftEye;

    public TreeFrogModel(ModelPart root) {

        frontLeftLeg = root.getChild("frontLeftLeg");
        frontRightLeg = root.getChild("frontRightLeg");
        body = root.getChild("body");
        rearRightLeg = root.getChild("rearRightLeg");
        rearLeftLeg = root.getChild("rearLeftLeg");
        rightEye = root.getChild("rightEye");
        leftEye = root.getChild("leftEye");


        /*
        frontLeftLeg = new ModelPart(this, 12, 19);
        frontLeftLeg.addCuboid(-2F, 0.0F, -2F, 4, 1, 4, 0.0F);
        frontLeftLeg.setPivot(2.0F, 23F, -3F);
        frontLeftLeg.mirror = false;

        frontRightLeg = new ModelPart(this, 12, 14);
        frontRightLeg.addCuboid(-2F, 0.0F, -2F, 4, 1, 4, 0.0F);
        frontRightLeg.setPivot(-2F, 23F, -3F);
        frontRightLeg.mirror = false;

        body = new ModelPart(this, 28, 8);
        body.addCuboid(-2F, -5F, -2F, 4, 9, 4, 0.0F);
        body.setPivot(0.0F, 21F, 1.0F);
        body.pitch = 1.570796F;
        body.mirror = false;

        rearRightLeg = new ModelPart(this, 0, 16);
        rearRightLeg.addCuboid(-3F, 0.0F, -2F, 3, 5, 3, 0.0F);
        rearRightLeg.setPivot(-2F, 19F, 4F);
        rearRightLeg.mirror = false;

        rearLeftLeg = new ModelPart(this, 0, 8);
        rearLeftLeg.addCuboid(0.0F, 0.0F, -2F, 3, 5, 3, 0.0F);
        rearLeftLeg.setPivot(2.0F, 19F, 4F);
        rearLeftLeg.mirror = false;

        rightEye = new ModelPart(this, 0, 0);
        rightEye.addCuboid(-2F, -1F, -1F, 2, 2, 2, 0.0F);
        rightEye.setPivot(-1F, 19F, -1F);
        rightEye.mirror = false;

        leftEye = new ModelPart(this, 0, 4);
        leftEye.addCuboid(0.0F, -1F, -1F, 2, 2, 2, 0.0F);
        leftEye.setPivot(1.0F, 19F, -1F);
        leftEye.mirror = false;
        */
    }

    public static LayerDefinition create() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        modelPartData.addOrReplaceChild("frontLeftLeg",
                CubeListBuilder.create().texOffs(12, 19)
                        .addBox(-2F, 0.0F, -2F, 4, 1, 4),
                PartPose.offset(2.0F, 23F, -3F));

        modelPartData.addOrReplaceChild("frontRightLeg",
                CubeListBuilder.create().texOffs(12, 14)
                        .addBox(-2F, 0.0F, -2F, 4, 1, 4),
                PartPose.offset(-2F, 23F, -3F));

        modelPartData.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(28, 8)
                        .addBox(-2F, -5F, -2F, 4, 9, 4),
                PartPose.offsetAndRotation(0.0F, 21F, 1.0F, 1.570796F, 0F ,0F));

        modelPartData.addOrReplaceChild("rearRightLeg",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-3F, 0.0F, -2F, 3, 5, 3),
                PartPose.offset(-2F, 19F, 4F));

        modelPartData.addOrReplaceChild("rearLeftLeg",
                CubeListBuilder.create().texOffs(0, 8)
                        .addBox(0.0F, 0.0F, -2F, 3, 5, 3),
                PartPose.offset(2.0F, 19F, 4F));

        modelPartData.addOrReplaceChild("rightEye",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2F, -1F, -1F, 2, 2, 2),
                PartPose.offset(-1F, 19F, -1F));

        modelPartData.addOrReplaceChild("leftEye",
                CubeListBuilder.create().texOffs(0, 4)
                        .addBox(0.0F, -1F, -1F, 2, 2, 2),
                PartPose.offset(1.0F, 19F, -1F));

        return LayerDefinition.create(modelData, 64, 32);
    }

    @Override
    public void setupAnim(TreeFrogEntity froog, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        frontLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        rearLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount;
        rearRightLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount;
        frontRightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(
            frontLeftLeg, frontRightLeg, body, rearRightLeg, rearLeftLeg, rightEye, leftEye
        );
    }
}
