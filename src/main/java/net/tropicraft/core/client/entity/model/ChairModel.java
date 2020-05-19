package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.tropicraft.core.common.entity.placeable.ChairEntity;

public class ChairModel extends SegmentedModel<ChairEntity> {
    public ModelRenderer seat;
    public ModelRenderer back;
    public ModelRenderer backRightLeg;
    public ModelRenderer backLeftLeg;
    public ModelRenderer frontLeftLeg;
    public ModelRenderer frontRightLeg;
    public ModelRenderer rightArm;
    public ModelRenderer leftArm;

    public ChairModel() {
        seat = new ModelRenderer(this, 0, 0);
        seat.addBox(-7F, 0F, -8F, 16, 1, 16, 0F);
        seat.setRotationPoint(-1F, 0F, 0F);

        back = new ModelRenderer(this, 0, 0);
        back.addBox(-7F, 0F, 0F, 16, 1, 16, 0F);
        back.setRotationPoint(-1F, 0F, 8F);
        back.rotateAngleX = 1.169371F;

        backRightLeg = new ModelRenderer(this, 0, 0);
        backRightLeg.addBox(-1F, -1F, 0F, 1, 10, 1, 0F);
        backRightLeg.setRotationPoint(-8F, -3F, 6F);
        backRightLeg.rotateAngleX = 0.4537856F;

        backLeftLeg = new ModelRenderer(this, 0, 0);
        backLeftLeg.addBox(0F, 0F, 0F, 1, 10, 1, 0F);
        backLeftLeg.setRotationPoint(8F, -4F, 5F);
        backLeftLeg.rotateAngleX = 0.4537856F;

        frontLeftLeg = new ModelRenderer(this, 0, 0);
        frontLeftLeg.addBox(0F, 0F, -1F, 1, 10, 1, 0F);
        frontLeftLeg.setRotationPoint(8F, -4F, 0F);
        frontLeftLeg.rotateAngleX = -0.4537856F;

        frontRightLeg = new ModelRenderer(this, 0, 0);
        frontRightLeg.addBox(-1F, 0F, -1F, 1, 10, 1, 0F);
        frontRightLeg.setRotationPoint(-8F, -4F, 0F);
        frontRightLeg.rotateAngleX = -0.4537856F;

        rightArm = new ModelRenderer(this, 0, 29);
        rightArm.addBox(0F, -1F, 0F, 14, 1, 2, 0F);
        rightArm.setRotationPoint(-10F, -4F, 11F);
        rightArm.rotateAngleY = 1.570796F;

        leftArm = new ModelRenderer(this, 0, 29);
        leftArm.addBox(0F, 0F, 0F, 14, 1, 2, 0F);
        leftArm.setRotationPoint(8F, -5F, 11F);
        leftArm.rotateAngleY = 1.570796F;
    }

    @Override
    public void setRotationAngles(ChairEntity chair, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(
            seat, back, backRightLeg, backLeftLeg, frontLeftLeg, frontRightLeg, rightArm, leftArm
        );
    }
}
