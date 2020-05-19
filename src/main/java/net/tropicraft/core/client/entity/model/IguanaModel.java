package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.entity.neutral.IguanaEntity;

public class IguanaModel extends SegmentedModel<IguanaEntity> {
    public ModelRenderer head;
    public ModelRenderer headTop1;
    public ModelRenderer headTop2;
    public ModelRenderer body;
    public ModelRenderer frontLeftLeg;
    public ModelRenderer rearLeftLeg;
    public ModelRenderer frontRightLeg;
    public ModelRenderer rearRightLeg;
    public ModelRenderer back1;
    public ModelRenderer back2;
    public ModelRenderer back3;
    public ModelRenderer jaw;
    public ModelRenderer dewLap;
    public ModelRenderer tailBase;
    public ModelRenderer tailMid;
    public ModelRenderer miscPart;

    public IguanaModel() {
        head = new ModelRenderer(this, 36, 23);
        head.addBox(-2.5F, -2F, -6F, 5, 3, 6);
        head.setRotationPoint(0F, 20F, -6F);

        body = new ModelRenderer(this, 0, 16);
        body.addBox(-2.5F, -1.5F, -7.5F, 5, 3, 13);
        body.setRotationPoint(0F, 21.5F, 1F);

        frontLeftLeg = new ModelRenderer(this, 24, 21);
        frontLeftLeg.addBox(0F, 0F, -1.5F, 2, 3, 3);
        frontLeftLeg.setRotationPoint(2.5F, 21F, -4F);

        rearLeftLeg = new ModelRenderer(this, 24, 21);
        rearLeftLeg.addBox(0F, 0F, -1.5F, 2, 3, 3);
        rearLeftLeg.setRotationPoint(2.5F, 21F, 4F);

        frontRightLeg = new ModelRenderer(this, 0, 21);
        frontRightLeg.addBox(-2F, 0F, -1.5F, 2, 3, 3);
        frontRightLeg.setRotationPoint(-2.5F, 21F, -4F);

        rearRightLeg = new ModelRenderer(this, 0, 21);
        rearRightLeg.addBox(-2F, 0F, -1.5F, 2, 3, 3);
        rearRightLeg.setRotationPoint(-2.5F, 21F, 4F);

        back1 = new ModelRenderer(this, 0, 0);
        back1.addBox(-1.5F, -1F, 0F, 3, 1, 10);
        back1.setRotationPoint(0F, 20F, -5F);

        back2 = new ModelRenderer(this, 32, 0);
        back2.addBox(-0.5F, -1F, -3F, 1, 1, 6);
        back2.setRotationPoint(0F, 19F, 0F);

        headTop2 = new ModelRenderer(this, 0, 0);
        headTop2.addBox(-0.5F, -4F, -4F, 1, 1, 2);
        headTop2.setRotationPoint(0F, 20F, -6F);

        headTop1 = new ModelRenderer(this, 32, 7);
        headTop1.addBox(-0.5F, -3F, -5F, 1, 1, 4);
        headTop1.setRotationPoint(0F, 20F, -6F);

        jaw = new ModelRenderer(this, 0, 11);
        jaw.addBox(-1F, 1F, -4F, 2, 1, 4);
        jaw.setRotationPoint(0F, 20F, -6F);

        back3 = new ModelRenderer(this, 32, 7);
        back3.addBox(-0.5F, 0F, -2F, 1, 1, 4);
        back3.setRotationPoint(0F, 17F, 0F);

        dewLap = new ModelRenderer(this, 0, 4);
        dewLap.addBox(-0.5F, 2F, -3F, 1, 1, 3);
        dewLap.setRotationPoint(0F, 20F, -6F);

        tailBase = new ModelRenderer(this, 46, 0);
        tailBase.addBox(-1.5F, -0.5F, 0F, 3, 1, 6);
        tailBase.setRotationPoint(0F, 21.5F, 6F);

        tailMid = new ModelRenderer(this, 48, 7);
        tailMid.addBox(-1F, -0.5F, 0F, 2, 1, 6);
        miscPart = new ModelRenderer(this, 52, 14);
        miscPart.addBox(-0.5F, -0.5F, 0F, 1, 1, 5);
    }

    @Override
    public void setRotationAngles(IguanaEntity iguana, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        final float magicHeadRotationAmt = 57.29578F;
        head.rotateAngleX = headPitch / magicHeadRotationAmt;
        head.rotateAngleY = netHeadYaw / magicHeadRotationAmt;
        jaw.rotateAngleX = head.rotateAngleX;
        jaw.rotateAngleY = head.rotateAngleY;
        headTop2.rotateAngleX = head.rotateAngleX;
        headTop2.rotateAngleY = head.rotateAngleY;
        headTop1.rotateAngleX = head.rotateAngleX;
        headTop1.rotateAngleY = head.rotateAngleY;
        dewLap.rotateAngleX = head.rotateAngleX;
        dewLap.rotateAngleY = head.rotateAngleY;
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(head, body, frontLeftLeg, rearLeftLeg, frontRightLeg, rearRightLeg, back1, back2, headTop2, headTop1, jaw,
                back3, dewLap, tailBase, tailMid, miscPart);
    }

    @Override
    public void setLivingAnimations(final IguanaEntity iggy, float f, float f1, float f2) {
        frontRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.75F * f1;
        frontLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.75F * f1;
        rearRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.75F * f1;
        rearLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.75F * f1;
        tailBase.rotateAngleY = MathHelper.cos(f * 0.6662F) * .25F * f1;
        tailMid.setRotationPoint(0F - (MathHelper.cos(tailBase.rotateAngleY + 1.570796F) * 6), 21.5F, 12F + MathHelper.sin(tailBase.rotateAngleX + 3.14159F) * 6);
        tailMid.rotateAngleY = tailBase.rotateAngleY + MathHelper.cos(f * 0.6662F) * .50F * f1;
        miscPart.setRotationPoint(0F - (MathHelper.cos(tailMid.rotateAngleY + 1.570796F) * 6), 21.5F, 18F + MathHelper.sin(tailMid.rotateAngleX + 3.14159F) * 6);
        miscPart.rotateAngleY = tailMid.rotateAngleY + MathHelper.cos(f * 0.6662F) * .75F * f1;;
    }
}
