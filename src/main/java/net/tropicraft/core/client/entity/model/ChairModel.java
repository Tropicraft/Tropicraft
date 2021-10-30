package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.tropicraft.core.common.entity.placeable.ChairEntity;

public class ChairModel extends ListModel<ChairEntity> {
    public ModelPart seat;
    public ModelPart back;
    public ModelPart backRightLeg;
    public ModelPart backLeftLeg;
    public ModelPart frontLeftLeg;
    public ModelPart frontRightLeg;
    public ModelPart rightArm;
    public ModelPart leftArm;

    public ChairModel() {
        seat = new ModelPart(this, 0, 0);
        seat.addBox(-7F, 0F, -8F, 16, 1, 16, 0F);
        seat.setPos(-1F, 0F, 0F);

        back = new ModelPart(this, 0, 0);
        back.addBox(-7F, 0F, 0F, 16, 1, 16, 0F);
        back.setPos(-1F, 0F, 8F);
        back.xRot = 1.169371F;

        backRightLeg = new ModelPart(this, 0, 0);
        backRightLeg.addBox(-1F, -1F, 0F, 1, 10, 1, 0F);
        backRightLeg.setPos(-8F, -3F, 6F);
        backRightLeg.xRot = 0.4537856F;

        backLeftLeg = new ModelPart(this, 0, 0);
        backLeftLeg.addBox(0F, 0F, 0F, 1, 10, 1, 0F);
        backLeftLeg.setPos(8F, -4F, 5F);
        backLeftLeg.xRot = 0.4537856F;

        frontLeftLeg = new ModelPart(this, 0, 0);
        frontLeftLeg.addBox(0F, 0F, -1F, 1, 10, 1, 0F);
        frontLeftLeg.setPos(8F, -4F, 0F);
        frontLeftLeg.xRot = -0.4537856F;

        frontRightLeg = new ModelPart(this, 0, 0);
        frontRightLeg.addBox(-1F, 0F, -1F, 1, 10, 1, 0F);
        frontRightLeg.setPos(-8F, -4F, 0F);
        frontRightLeg.xRot = -0.4537856F;

        rightArm = new ModelPart(this, 0, 29);
        rightArm.addBox(0F, -1F, 0F, 14, 1, 2, 0F);
        rightArm.setPos(-10F, -4F, 11F);
        rightArm.yRot = 1.570796F;

        leftArm = new ModelPart(this, 0, 29);
        leftArm.addBox(0F, 0F, 0F, 14, 1, 2, 0F);
        leftArm.setPos(8F, -5F, 11F);
        leftArm.yRot = 1.570796F;
    }

    @Override
    public void setupAnim(ChairEntity chair, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(
            seat, back, backRightLeg, backLeftLeg, frontLeftLeg, frontRightLeg, rightArm, leftArm
        );
    }
}
