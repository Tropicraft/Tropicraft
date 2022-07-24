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

    public IguanaModel(ModelPart root) {
        head = root.getChild("head");
        headTop1 = root.getChild("headTop1");
        headTop2 = root.getChild("headTop2");
        body = root.getChild("body");
        frontLeftLeg = root.getChild("frontLeftLeg");
        rearLeftLeg = root.getChild("rearLeftLeg");
        frontRightLeg = root.getChild("frontRightLeg");
        rearRightLeg = root.getChild("rearRightLeg");
        back1 = root.getChild("back1");
        back2 = root.getChild("back2");
        back3 = root.getChild("back3");
        jaw = root.getChild("jaw");
        dewLap = root.getChild("dewLap");
        tailBase = root.getChild("tailBase");
        tailMid = root.getChild("tailMid");
        miscPart = root.getChild("miscPart");


        /*
        head = new ModelPart(this, 36, 23);
        head.addCuboid(-2.5F, -2F, -6F, 5, 3, 6);
        head.setPivot(0F, 20F, -6F);

        body = new ModelPart(this, 0, 16);
        body.addCuboid(-2.5F, -1.5F, -7.5F, 5, 3, 13);
        body.setPivot(0F, 21.5F, 1F);

        frontLeftLeg = new ModelPart(this, 24, 21);
        frontLeftLeg.addCuboid(0F, 0F, -1.5F, 2, 3, 3);
        frontLeftLeg.setPivot(2.5F, 21F, -4F);

        rearLeftLeg = new ModelPart(this, 24, 21);
        rearLeftLeg.addCuboid(0F, 0F, -1.5F, 2, 3, 3);
        rearLeftLeg.setPivot(2.5F, 21F, 4F);

        frontRightLeg = new ModelPart(this, 0, 21);
        frontRightLeg.addCuboid(-2F, 0F, -1.5F, 2, 3, 3);
        frontRightLeg.setPivot(-2.5F, 21F, -4F);

        rearRightLeg = new ModelPart(this, 0, 21);
        rearRightLeg.addCuboid(-2F, 0F, -1.5F, 2, 3, 3);
        rearRightLeg.setPivot(-2.5F, 21F, 4F);

        back1 = new ModelPart(this, 0, 0);
        back1.addCuboid(-1.5F, -1F, 0F, 3, 1, 10);
        back1.setPivot(0F, 20F, -5F);

        back2 = new ModelPart(this, 32, 0);
        back2.addCuboid(-0.5F, -1F, -3F, 1, 1, 6);
        back2.setPivot(0F, 19F, 0F);

        headTop2 = new ModelPart(this, 0, 0);
        headTop2.addCuboid(-0.5F, -4F, -4F, 1, 1, 2);
        headTop2.setPivot(0F, 20F, -6F);

        headTop1 = new ModelPart(this, 32, 7);
        headTop1.addCuboid(-0.5F, -3F, -5F, 1, 1, 4);
        headTop1.setPivot(0F, 20F, -6F);

        jaw = new ModelPart(this, 0, 11);
        jaw.addCuboid(-1F, 1F, -4F, 2, 1, 4);
        jaw.setPivot(0F, 20F, -6F);

        back3 = new ModelPart(this, 32, 7);
        back3.addCuboid(-0.5F, 0F, -2F, 1, 1, 4);
        back3.setPivot(0F, 17F, 0F);

        dewLap = new ModelPart(this, 0, 4);
        dewLap.addCuboid(-0.5F, 2F, -3F, 1, 1, 3);
        dewLap.setPivot(0F, 20F, -6F);

        tailBase = new ModelPart(this, 46, 0);
        tailBase.addCuboid(-1.5F, -0.5F, 0F, 3, 1, 6);
        tailBase.setPivot(0F, 21.5F, 6F);

        tailMid = new ModelPart(this, 48, 7);
        tailMid.addCuboid(-1F, -0.5F, 0F, 2, 1, 6);
        miscPart = new ModelPart(this, 52, 14);
        miscPart.addCuboid(-0.5F, -0.5F, 0F, 1, 1, 5);
         */
    }

    public static LayerDefinition create() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        modelPartData.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(36, 23)
                        .addBox(-2.5F, -2F, -6F, 5, 3, 6),
                PartPose.offset(0F, 20F, -6F));

        modelPartData.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-2.5F, -1.5F, -7.5F, 5, 3, 13),
                PartPose.offset(0F, 21.5F, 1F));

        modelPartData.addOrReplaceChild("frontLeftLeg",
                CubeListBuilder.create()
                        .texOffs(24, 21)
                        .addBox(0F, 0F, -1.5F, 2, 3, 3),
                PartPose.offset(2.5F, 21F, -4F));

        modelPartData.addOrReplaceChild("rearLeftLeg",
                CubeListBuilder.create()
                        .texOffs(24, 21)
                        .addBox(0F, 0F, -1.5F, 2, 3, 3),
                PartPose.offset(2.5F, 21F, 4F));

        modelPartData.addOrReplaceChild("frontRightLeg",
                CubeListBuilder.create()
                        .texOffs(0, 21)
                        .addBox(-2F, 0F, -1.5F, 2, 3, 3),
                PartPose.offset(-2.5F, 21F, -4F));

        modelPartData.addOrReplaceChild("rearRightLeg",
                CubeListBuilder.create()
                        .texOffs(0, 21)
                        .addBox(-2F, 0F, -1.5F, 2, 3, 3),
                PartPose.offset(-2.5F, 21F, 4F));

        modelPartData.addOrReplaceChild("back1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5F, -1F, 0F, 3, 1, 10),
                PartPose.offset(0F, 20F, -5F));

        modelPartData.addOrReplaceChild("back2",
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-0.5F, -1F, -3F, 1, 1, 6),
                PartPose.offset(0F, 19F, 0F));

        modelPartData.addOrReplaceChild("headTop2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4F, -4F, 1, 1, 2),
                PartPose.offset(0F, 20F, -6F));

        modelPartData.addOrReplaceChild("headTop1",
                CubeListBuilder.create()
                        .texOffs(32, 7)
                        .addBox(-0.5F, -3F, -5F, 1, 1, 4),
                PartPose.offset(0F, 20F, -6F));

        modelPartData.addOrReplaceChild("jaw",
                CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-1F, 1F, -4F, 2, 1, 4),
                PartPose.offset(0F, 20F, -6F));

        modelPartData.addOrReplaceChild("back3",
                CubeListBuilder.create()
                        .texOffs(32, 7)
                        .addBox(-0.5F, 0F, -2F, 1, 1, 4),
                PartPose.offset(0F, 17F, 0F));

        modelPartData.addOrReplaceChild("dewLap",
                CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(-0.5F, 2F, -3F, 1, 1, 3),
                PartPose.offset(0F, 20F, -6F));

        modelPartData.addOrReplaceChild("tailBase",
                CubeListBuilder.create()
                        .texOffs(46, 0)
                        .addBox(-1.5F, -0.5F, 0F, 3, 1, 6),
                PartPose.offset(0F, 21.5F, 6F));

        modelPartData.addOrReplaceChild("tailMid",
                CubeListBuilder.create()
                        .texOffs(48, 7)
                        .addBox(-1F, -0.5F, 0F, 2, 1, 6),
                PartPose.ZERO);

        modelPartData.addOrReplaceChild("miscPart",
                CubeListBuilder.create()
                        .texOffs(52, 14)
                        .addBox(-0.5F, -0.5F, 0F, 1, 1, 5),
                PartPose.ZERO);


        return LayerDefinition.create(modelData, 64, 32);
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

        // Animate iguana tail ambiently
        // The call in prepareMobModel animates it when swinging limbs
        try (ModelAnimator.Cycle idle = ModelAnimator.cycle(ageInTicks * 0.025F, 0.1F)) {
            tailBase.yRot += idle.eval(1.0F, 1.0F, 0.0F, 0.0F);

            // The positions need to be set to ensure the tail parts move in tandem
            tailMid.setPos(0F - (Mth.cos(tailBase.yRot + 1.570796F) * 6), 21.5F, 12F + Mth.sin(tailBase.xRot + 3.14159F) * 6);
            tailMid.yRot += idle.eval(1.0F, 1.0F, 0.05F, 0.0F);

            miscPart.setPos(0F - (Mth.cos(tailMid.yRot + 1.570796F) * 6), 21.5F, 18F + Mth.sin(tailMid.xRot + 3.14159F) * 6);
            miscPart.yRot += idle.eval(1.0F, 1.0F, 0.075F, 0.0F);
        }
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(head, body, frontLeftLeg, rearLeftLeg, frontRightLeg, rearRightLeg, back1, back2, headTop2, headTop1, jaw,
                back3, dewLap, tailBase, tailMid, miscPart);
    }

    @Override
    public void prepareMobModel(final IguanaEntity iggy, float limbSwing, float limbSwingAmount, float partialTicks) {
        frontRightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.75F * limbSwingAmount;
        frontLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.141593F) * 1.75F * limbSwingAmount;
        rearRightLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.141593F) * 1.75F * limbSwingAmount;
        rearLeftLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.75F * limbSwingAmount;
        tailBase.yRot = Mth.cos(limbSwing * 0.6662F) * .25F * limbSwingAmount;
        tailMid.setPos(0F - (Mth.cos(tailBase.yRot + 1.570796F) * 6), 21.5F, 12F + Mth.sin(tailBase.xRot + 3.14159F) * 6);
        tailMid.yRot = tailBase.yRot + Mth.cos(limbSwing * 0.6662F) * .50F * limbSwingAmount;
        miscPart.setPos(0F - (Mth.cos(tailMid.yRot + 1.570796F) * 6), 21.5F, 18F + Mth.sin(tailMid.xRot + 3.14159F) * 6);
        miscPart.yRot = tailMid.yRot + Mth.cos(limbSwing * 0.6662F) * .75F * limbSwingAmount;
    }
}
