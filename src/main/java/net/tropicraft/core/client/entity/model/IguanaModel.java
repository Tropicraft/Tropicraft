package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.neutral.IguanaEntity;

public class IguanaModel extends HierarchicalModel<IguanaEntity> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart headTop1;
    private final ModelPart headTop2;
    private final ModelPart body;
    private final ModelPart frontLeftLeg;
    private final ModelPart rearLeftLeg;
    private final ModelPart frontRightLeg;
    private final ModelPart rearRightLeg;
    private final ModelPart back1;
    private final ModelPart back2;
    private final ModelPart back3;
    private final ModelPart jaw;
    private final ModelPart dewLap;
    private final ModelPart tailBase;
    private final ModelPart tailMid;
    private final ModelPart miscPart;

    public IguanaModel(ModelPart root) {
        this.root = root;
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
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(36, 23)
                        .addBox(-2.5f, -2.0f, -6.0f, 5, 3, 6),
                PartPose.offset(0.0f, 20.0f, -6.0f));

        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-2.5f, -1.5f, -7.5f, 5, 3, 13),
                PartPose.offset(0.0f, 21.5f, 1.0f));

        root.addOrReplaceChild("frontLeftLeg",
                CubeListBuilder.create()
                        .texOffs(24, 21)
                        .addBox(0.0f, 0.0f, -1.5f, 2, 3, 3),
                PartPose.offset(2.5f, 21.0f, -4.0f));

        root.addOrReplaceChild("rearLeftLeg",
                CubeListBuilder.create()
                        .texOffs(24, 21)
                        .addBox(0.0f, 0.0f, -1.5f, 2, 3, 3),
                PartPose.offset(2.5f, 21.0f, 4.0f));

        root.addOrReplaceChild("frontRightLeg",
                CubeListBuilder.create()
                        .texOffs(0, 21)
                        .addBox(-2.0f, 0.0f, -1.5f, 2, 3, 3),
                PartPose.offset(-2.5f, 21.0f, -4.0f));

        root.addOrReplaceChild("rearRightLeg",
                CubeListBuilder.create()
                        .texOffs(0, 21)
                        .addBox(-2.0f, 0.0f, -1.5f, 2, 3, 3),
                PartPose.offset(-2.5f, 21.0f, 4.0f));

        root.addOrReplaceChild("back1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.5f, -1.0f, 0.0f, 3, 1, 10),
                PartPose.offset(0.0f, 20.0f, -5.0f));

        root.addOrReplaceChild("back2",
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-0.5f, -1.0f, -3.0f, 1, 1, 6),
                PartPose.offset(0.0f, 19.0f, 0.0f));

        root.addOrReplaceChild("headTop2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.5f, -4.0f, -4.0f, 1, 1, 2),
                PartPose.offset(0.0f, 20.0f, -6.0f));

        root.addOrReplaceChild("headTop1",
                CubeListBuilder.create()
                        .texOffs(32, 7)
                        .addBox(-0.5f, -3.0f, -5.0f, 1, 1, 4),
                PartPose.offset(0.0f, 20.0f, -6.0f));

        root.addOrReplaceChild("jaw",
                CubeListBuilder.create()
                        .texOffs(0, 11)
                        .addBox(-1.0f, 1.0f, -4.0f, 2, 1, 4),
                PartPose.offset(0.0f, 20.0f, -6.0f));

        root.addOrReplaceChild("back3",
                CubeListBuilder.create()
                        .texOffs(32, 7)
                        .addBox(-0.5f, 0.0f, -2.0f, 1, 1, 4),
                PartPose.offset(0.0f, 17.0f, 0.0f));

        root.addOrReplaceChild("dewLap",
                CubeListBuilder.create()
                        .texOffs(0, 4)
                        .addBox(-0.5f, 2.0f, -3.0f, 1, 1, 3),
                PartPose.offset(0.0f, 20.0f, -6.0f));

        root.addOrReplaceChild("tailBase",
                CubeListBuilder.create()
                        .texOffs(46, 0)
                        .addBox(-1.5f, -0.5f, 0.0f, 3, 1, 6),
                PartPose.offset(0.0f, 21.5f, 6.0f));

        root.addOrReplaceChild("tailMid",
                CubeListBuilder.create()
                        .texOffs(48, 7)
                        .addBox(-1.0f, -0.5f, 0.0f, 2, 1, 6),
                PartPose.ZERO);

        root.addOrReplaceChild("miscPart",
                CubeListBuilder.create()
                        .texOffs(52, 14)
                        .addBox(-0.5f, -0.5f, 0.0f, 1, 1, 5),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(IguanaEntity iguana, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        final float magicHeadRotationAmt = 57.29578f;
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
        try (ModelAnimator.Cycle idle = ModelAnimator.cycle(ageInTicks * 0.025f, 0.1f)) {
            tailBase.yRot += idle.eval(1.0f, 1.0f, 0.0f, 0.0f);

            // The positions need to be set to ensure the tail parts move in tandem
            tailMid.setPos(0.0f - (Mth.cos(tailBase.yRot + Mth.HALF_PI) * 6), 21.5f, 12.0f + Mth.sin(tailBase.xRot + 3.14159f) * 6);
            tailMid.yRot += idle.eval(1.0f, 1.0f, 0.05f, 0.0f);

            miscPart.setPos(0.0f - (Mth.cos(tailMid.yRot + Mth.HALF_PI) * 6), 21.5f, 18.0f + Mth.sin(tailMid.xRot + 3.14159f) * 6);
            miscPart.yRot += idle.eval(1.0f, 1.0f, 0.075f, 0.0f);
        }
    }

    @Override
    public void prepareMobModel(IguanaEntity iggy, float limbSwing, float limbSwingAmount, float partialTicks) {
        frontRightLeg.xRot = Mth.cos(limbSwing * 0.6662f) * 1.75f * limbSwingAmount;
        frontLeftLeg.xRot = Mth.cos(limbSwing * 0.6662f + Mth.PI) * 1.75f * limbSwingAmount;
        rearRightLeg.xRot = Mth.cos(limbSwing * 0.6662f + Mth.PI) * 1.75f * limbSwingAmount;
        rearLeftLeg.xRot = Mth.cos(limbSwing * 0.6662f) * 1.75f * limbSwingAmount;
        tailBase.yRot = Mth.cos(limbSwing * 0.6662f) * 0.25f * limbSwingAmount;
        tailMid.setPos(0.0f - (Mth.cos(tailBase.yRot + Mth.HALF_PI) * 6), 21.5f, 12.0f + Mth.sin(tailBase.xRot + 3.14159f) * 6);
        tailMid.yRot = tailBase.yRot + Mth.cos(limbSwing * 0.6662f) * 0.50f * limbSwingAmount;
        miscPart.setPos(0.0f - (Mth.cos(tailMid.yRot + Mth.HALF_PI) * 6), 21.5f, 18.0f + Mth.sin(tailMid.xRot + 3.14159f) * 6);
        miscPart.yRot = tailMid.yRot + Mth.cos(limbSwing * 0.6662f) * 0.75f * limbSwingAmount;
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
