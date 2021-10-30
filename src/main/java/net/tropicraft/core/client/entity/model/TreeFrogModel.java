package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
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

    public TreeFrogModel() {
        frontLeftLeg = new ModelPart(this, 12, 19);
        frontLeftLeg.addBox(-2F, 0.0F, -2F, 4, 1, 4, 0.0F);
        frontLeftLeg.setPos(2.0F, 23F, -3F);
        frontLeftLeg.mirror = false;
        frontRightLeg = new ModelPart(this, 12, 14);
        frontRightLeg.addBox(-2F, 0.0F, -2F, 4, 1, 4, 0.0F);
        frontRightLeg.setPos(-2F, 23F, -3F);
        frontRightLeg.mirror = false;
        body = new ModelPart(this, 28, 8);
        body.addBox(-2F, -5F, -2F, 4, 9, 4, 0.0F);
        body.setPos(0.0F, 21F, 1.0F);
        body.xRot = 1.570796F;
        body.mirror = false;
        rearRightLeg = new ModelPart(this, 0, 16);
        rearRightLeg.addBox(-3F, 0.0F, -2F, 3, 5, 3, 0.0F);
        rearRightLeg.setPos(-2F, 19F, 4F);
        rearRightLeg.mirror = false;
        rearLeftLeg = new ModelPart(this, 0, 8);
        rearLeftLeg.addBox(0.0F, 0.0F, -2F, 3, 5, 3, 0.0F);
        rearLeftLeg.setPos(2.0F, 19F, 4F);
        rearLeftLeg.mirror = false;
        rightEye = new ModelPart(this, 0, 0);
        rightEye.addBox(-2F, -1F, -1F, 2, 2, 2, 0.0F);
        rightEye.setPos(-1F, 19F, -1F);
        rightEye.mirror = false;
        leftEye = new ModelPart(this, 0, 4);
        leftEye.addBox(0.0F, -1F, -1F, 2, 2, 2, 0.0F);
        leftEye.setPos(1.0F, 19F, -1F);
        leftEye.mirror = false;
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
