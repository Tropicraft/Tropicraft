package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.neutral.IguanaEntity;

public class IguanaModel extends ListModel<IguanaEntity> {
    public ModelPart head;
    public ModelPart headTop1;
    public ModelPart headTop2;
    public ModelPart body;
    public ModelPart frontLeftLeg;
    public ModelPart rearLeftLeg;
    public ModelPart frontRightLeg;
    public ModelPart rearRightLeg;
    public ModelPart back1;
    public ModelPart back2;
    public ModelPart back3;
    public ModelPart jaw;
    public ModelPart dewLap;
    public ModelPart tailBase;
    public ModelPart tailMid;
    public ModelPart miscPart;

    public IguanaModel() {
        head = new ModelPart(this, 36, 23);
        head.addBox(-2.5F, -2F, -6F, 5, 3, 6);
        head.setPos(0F, 20F, -6F);

        body = new ModelPart(this, 0, 16);
        body.addBox(-2.5F, -1.5F, -7.5F, 5, 3, 13);
        body.setPos(0F, 21.5F, 1F);

        frontLeftLeg = new ModelPart(this, 24, 21);
        frontLeftLeg.addBox(0F, 0F, -1.5F, 2, 3, 3);
        frontLeftLeg.setPos(2.5F, 21F, -4F);

        rearLeftLeg = new ModelPart(this, 24, 21);
        rearLeftLeg.addBox(0F, 0F, -1.5F, 2, 3, 3);
        rearLeftLeg.setPos(2.5F, 21F, 4F);

        frontRightLeg = new ModelPart(this, 0, 21);
        frontRightLeg.addBox(-2F, 0F, -1.5F, 2, 3, 3);
        frontRightLeg.setPos(-2.5F, 21F, -4F);

        rearRightLeg = new ModelPart(this, 0, 21);
        rearRightLeg.addBox(-2F, 0F, -1.5F, 2, 3, 3);
        rearRightLeg.setPos(-2.5F, 21F, 4F);

        back1 = new ModelPart(this, 0, 0);
        back1.addBox(-1.5F, -1F, 0F, 3, 1, 10);
        back1.setPos(0F, 20F, -5F);

        back2 = new ModelPart(this, 32, 0);
        back2.addBox(-0.5F, -1F, -3F, 1, 1, 6);
        back2.setPos(0F, 19F, 0F);

        headTop2 = new ModelPart(this, 0, 0);
        headTop2.addBox(-0.5F, -4F, -4F, 1, 1, 2);
        headTop2.setPos(0F, 20F, -6F);

        headTop1 = new ModelPart(this, 32, 7);
        headTop1.addBox(-0.5F, -3F, -5F, 1, 1, 4);
        headTop1.setPos(0F, 20F, -6F);

        jaw = new ModelPart(this, 0, 11);
        jaw.addBox(-1F, 1F, -4F, 2, 1, 4);
        jaw.setPos(0F, 20F, -6F);

        back3 = new ModelPart(this, 32, 7);
        back3.addBox(-0.5F, 0F, -2F, 1, 1, 4);
        back3.setPos(0F, 17F, 0F);

        dewLap = new ModelPart(this, 0, 4);
        dewLap.addBox(-0.5F, 2F, -3F, 1, 1, 3);
        dewLap.setPos(0F, 20F, -6F);

        tailBase = new ModelPart(this, 46, 0);
        tailBase.addBox(-1.5F, -0.5F, 0F, 3, 1, 6);
        tailBase.setPos(0F, 21.5F, 6F);

        tailMid = new ModelPart(this, 48, 7);
        tailMid.addBox(-1F, -0.5F, 0F, 2, 1, 6);
        miscPart = new ModelPart(this, 52, 14);
        miscPart.addBox(-0.5F, -0.5F, 0F, 1, 1, 5);
    }

    @Override
    public void setupAnim(IguanaEntity iguana, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        final float magicHeadRotationAmt = 57.29578F;
        head.xRot = headPitch / magicHeadRotationAmt;
        head.yRot = netHeadYaw / magicHeadRotationAmt;
        jaw.xRot = head.xRot;
        jaw.yRot = head.yRot;
        headTop2.xRot = head.xRot;
        headTop2.yRot = head.yRot;
        headTop1.xRot = head.xRot;
        headTop1.yRot = head.yRot;
        dewLap.xRot = head.xRot;
        dewLap.yRot = head.yRot;
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(head, body, frontLeftLeg, rearLeftLeg, frontRightLeg, rearRightLeg, back1, back2, headTop2, headTop1, jaw,
                back3, dewLap, tailBase, tailMid, miscPart);
    }

    @Override
    public void prepareMobModel(final IguanaEntity iggy, float f, float f1, float f2) {
        frontRightLeg.xRot = Mth.cos(f * 0.6662F) * 1.75F * f1;
        frontLeftLeg.xRot = Mth.cos(f * 0.6662F + 3.141593F) * 1.75F * f1;
        rearRightLeg.xRot = Mth.cos(f * 0.6662F + 3.141593F) * 1.75F * f1;
        rearLeftLeg.xRot = Mth.cos(f * 0.6662F) * 1.75F * f1;
        tailBase.yRot = Mth.cos(f * 0.6662F) * .25F * f1;
        tailMid.setPos(0F - (Mth.cos(tailBase.yRot + 1.570796F) * 6), 21.5F, 12F + Mth.sin(tailBase.xRot + 3.14159F) * 6);
        tailMid.yRot = tailBase.yRot + Mth.cos(f * 0.6662F) * .50F * f1;
        miscPart.setPos(0F - (Mth.cos(tailMid.yRot + 1.570796F) * 6), 21.5F, 18F + Mth.sin(tailMid.xRot + 3.14159F) * 6);
        miscPart.yRot = tailMid.yRot + Mth.cos(f * 0.6662F) * .75F * f1;;
    }
}
