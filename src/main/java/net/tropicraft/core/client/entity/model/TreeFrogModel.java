package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.entity.neutral.TreeFrogEntity;

public class TreeFrogModel extends SegmentedModel<TreeFrogEntity> {
    public ModelRenderer frontLeftLeg;
    public ModelRenderer frontRightLeg;
    public ModelRenderer body;
    public ModelRenderer rearRightLeg;
    public ModelRenderer rearLeftLeg;
    public ModelRenderer rightEye;
    public ModelRenderer leftEye;

    public TreeFrogModel() {
        frontLeftLeg = new ModelRenderer(this, 12, 19);
        frontLeftLeg.addBox(-2F, 0.0F, -2F, 4, 1, 4, 0.0F);
        frontLeftLeg.setRotationPoint(2.0F, 23F, -3F);
        frontLeftLeg.mirror = false;
        frontRightLeg = new ModelRenderer(this, 12, 14);
        frontRightLeg.addBox(-2F, 0.0F, -2F, 4, 1, 4, 0.0F);
        frontRightLeg.setRotationPoint(-2F, 23F, -3F);
        frontRightLeg.mirror = false;
        body = new ModelRenderer(this, 28, 8);
        body.addBox(-2F, -5F, -2F, 4, 9, 4, 0.0F);
        body.setRotationPoint(0.0F, 21F, 1.0F);
        body.rotateAngleX = 1.570796F;
        body.mirror = false;
        rearRightLeg = new ModelRenderer(this, 0, 16);
        rearRightLeg.addBox(-3F, 0.0F, -2F, 3, 5, 3, 0.0F);
        rearRightLeg.setRotationPoint(-2F, 19F, 4F);
        rearRightLeg.mirror = false;
        rearLeftLeg = new ModelRenderer(this, 0, 8);
        rearLeftLeg.addBox(0.0F, 0.0F, -2F, 3, 5, 3, 0.0F);
        rearLeftLeg.setRotationPoint(2.0F, 19F, 4F);
        rearLeftLeg.mirror = false;
        rightEye = new ModelRenderer(this, 0, 0);
        rightEye.addBox(-2F, -1F, -1F, 2, 2, 2, 0.0F);
        rightEye.setRotationPoint(-1F, 19F, -1F);
        rightEye.mirror = false;
        leftEye = new ModelRenderer(this, 0, 4);
        leftEye.addBox(0.0F, -1F, -1F, 2, 2, 2, 0.0F);
        leftEye.setRotationPoint(1.0F, 19F, -1F);
        leftEye.mirror = false;
    }

    @Override
    public void setRotationAngles(TreeFrogEntity froog, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        frontLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        rearLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount;
        rearRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount;
        frontRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(
            frontLeftLeg, frontRightLeg, body, rearRightLeg, rearLeftLeg, rightEye, leftEye
        );
    }
}
