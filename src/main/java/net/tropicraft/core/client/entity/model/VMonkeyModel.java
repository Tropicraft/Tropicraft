package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.HumanoidArm;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;

import javax.annotation.Nullable;

public class VMonkeyModel extends HierarchicalModel<VMonkeyEntity> implements ArmedModel {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart lLegUpper;
    private final ModelPart rLegUpper;
    private final ModelPart rArmUpper;
    private final ModelPart lArmUpper;
    private final ModelPart tailBase;
    private final ModelPart tailMid;
    private final ModelPart tailTop;
    private final ModelPart rArmLower;
    private final ModelPart lArmLower;
    private final ModelPart lLegLower;
    private final ModelPart rLegLower;
    private final ModelPart face;
    private final ModelPart head;
    @Nullable
    protected RandomSource rand;

    public VMonkeyModel(ModelPart root) {
        this.root = root;
        body = root.getChild("body");
        lLegUpper = root.getChild("lLegUpper");
        rLegUpper = root.getChild("rLegUpper");
        rArmUpper = root.getChild("rArmUpper");
        lArmUpper = root.getChild("lArmUpper");
        tailBase = root.getChild("tailBase");
        tailMid = root.getChild("tailMid");
        tailTop = root.getChild("tailTop");
        rArmLower = root.getChild("rArmLower");
        lArmLower = root.getChild("lArmLower");
        lLegLower = root.getChild("lLegLower");
        rLegLower = root.getChild("rLegLower");
        face = root.getChild("face");
        head = root.getChild("head");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 8)
                        .addBox(-1.0f, -2.0f, -4.0f, 2, 4, 9),
                PartPose.offsetAndRotation(0.0f, 16.0f, 0.0f, 0.0f, Mth.PI, 0.0f));

        root.addOrReplaceChild("lLegUpper",
                CubeListBuilder.create().texOffs(7, 0)
                        .addBox(-1.0f, 0.0f, -0.5f, 1, 5, 1),
                PartPose.offsetAndRotation(-1.0f, 14.0f, -3.5f, 0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("rLegUpper",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0f, 0.0f, -0.5f, 1, 5, 1),
                PartPose.offsetAndRotation(1.0f, 14.0f, -3.5f, 0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("rArmUpper",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0f, 0.0f, -0.5f, 1, 5, 1),
                PartPose.offsetAndRotation(1.0f, 14.0f, 3.5f, 0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("lArmUpper",
                CubeListBuilder.create().texOffs(7, 0)
                        .addBox(-1.0f, 0.0f, -0.5f, 1, 5, 1),
                PartPose.offsetAndRotation(-1.0f, 14.0f, 3.5f, 0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("tailBase",
                CubeListBuilder.create().texOffs(20, 27)
                        .addBox(-0.5f, -4.0f, -0.5f, 1, 3, 1),
                PartPose.offsetAndRotation(0.0f, 15.0f, 3.5f, 0.0f, Mth.PI, 0.0f));

        root.addOrReplaceChild("tailMid",
                CubeListBuilder.create().texOffs(20, 24)
                        .addBox(-0.5f, -2.0f, -0.5f, 1, 2, 1),
                PartPose.offsetAndRotation(0.0f, 11.0f, 3.5f, 0.0f, Mth.PI, 0.0f));

        root.addOrReplaceChild("tailTop",
                CubeListBuilder.create().texOffs(20, 21)
                        .addBox(-0.5f, -2.0f, -0.5f, 1, 2, 1),
                PartPose.offsetAndRotation(0.0f, 9.0f, 3.5f, 0.0f, Mth.PI, 0.0f));

        root.addOrReplaceChild("rArmLower",
                CubeListBuilder.create().texOffs(0, 7)
                        .addBox(0.0f, 0.0f, -0.5f, 1, 5, 1),
                PartPose.offsetAndRotation(1.0f, 19.0f, 3.5f, 0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("lArmLower",
                CubeListBuilder.create().texOffs(12, 0)
                        .addBox(-1.0f, 0.0f, -0.5f, 1, 5, 1),
                PartPose.offsetAndRotation(-1.0f, 19.0f, 3.5f, 0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("lLegLower",
                CubeListBuilder.create().texOffs(12, 0)
                        .addBox(-1.0f, 0.0f, -0.5f, 1, 5, 1),
                PartPose.offsetAndRotation(-1.0f, 19.0f, -3.5f, 0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("rLegLower",
                CubeListBuilder.create().texOffs(0, 7)
                        .addBox(0.0f, 0.0f, -0.5f, 1, 5, 1),
                PartPose.offsetAndRotation(1.0f, 19.0f, -3.5f, 0.0f, 0.0f, 0.0f));

        root.addOrReplaceChild("face",
                CubeListBuilder.create().texOffs(0, 25)
                        .addBox(-2.0f, -1.0f, 0.0f, 4, 4, 3),
                PartPose.offsetAndRotation(0.0f, 15.0f, -5.0f, 0.0f, Mth.PI, 0.0f));

        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(25, 25)
                        .addBox(-3.0f, -2.0f, 0.0f, 6, 5, 2),
                PartPose.offsetAndRotation(0.0f, 15.0f, -5.0f, 0.0f, Mth.PI, 0.0f));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(VMonkeyEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float partialTicks = ageInTicks - entity.tickCount;

        face.yRot = netHeadYaw * Mth.DEG_TO_RAD + Mth.PI;
        face.xRot = headPitch * Mth.DEG_TO_RAD;

        if (entity.isOrderedToSit()) {
            body.setPos(0.0f, 20.0f, 0.0f);
            body.xRot = 0.9320058f;
            body.yRot = Mth.PI;
            lLegUpper.setPos(-1.0f, 16.0f, -1.5f);
            lLegUpper.xRot = -0.2792527f;
            rLegUpper.setPos(1.0f, 16.0f, -1.5f);
            rLegUpper.xRot = -0.2792527f;
            rLegUpper.yRot = 0.005817764f;
            rArmUpper.setPos(1.0f, 22.0f, 3.5f);
            rArmUpper.xRot = -2.142101f;
            lArmUpper.setPos(-1.0f, 22.0f, 3.5f);
            lArmUpper.xRot = -2.142043f;
            tailBase.setPos(0.0f, 22.0f, 2.466667f);
            tailBase.xRot = 1.902409f;
            tailBase.yRot = Mth.PI;
            tailMid.setPos(0.0f, 23.3f, 5.966667f);
            tailMid.xRot = Mth.HALF_PI;
            tailMid.yRot = 2.111848f;
            tailMid.zRot = -0.2617994f;
            tailTop.setPos(-1.0f, 23.2f, 7.0f);
            tailTop.xRot = Mth.HALF_PI;
            tailTop.yRot = 0.8377581f;
            tailTop.zRot = Mth.DEG_TO_RAD;
            rArmLower.setPos(1.0f, 19.0f, -0.5f);
            rArmLower.xRot = -0.1489348f;
            lArmLower.setPos(-1.0f, 19.0f, -0.3f);
            lArmLower.xRot = -0.1492257f;
            lLegLower.setPos(-1.0f, 21.0f, -2.8f);
            lLegLower.xRot = -0.9599311f;
            rLegLower.setPos(1.0f, 21.0f, -2.833333f);
            rLegLower.xRot = -0.9599311f;
            face.setPos(0.0f, 15.0f, -3.0f);
            head.setPos(0.0f, 15.0f, -3.0f);
        } else if (entity.isClimbing()) {
            body.xRot = Mth.HALF_PI;
            body.setPos(0.0f, 16.0f, 0.0f);
            lLegUpper.setPos(-1.0f, 12.0f, 2.0f);
            //lLegUpper.rotateAngleX = -1.570796f;
            rLegUpper.setPos(1.0f, 12.0f, 2.0f);
            //rLegUpper.rotateAngleX = -1.570796f;
            rArmUpper.setPos(1.0f, 19.5f, 2.0f);
            //rArmUpper.rotateAngleX = -1.570796f;
            lArmUpper.setPos(-1.0f, 19.5f, 2.0f);
            //lArmUpper.rotateAngleX = -1.570796f;
            tailBase.setPos(0.0f, 19.5f, 0.5f);
            tailBase.xRot = Mth.HALF_PI;
            tailBase.yRot = Mth.PI;
            tailMid.setPos(0.0f, 19.5f, 4.5f);

            tailMid.xRot = Mth.HALF_PI;
            tailMid.yRot = Mth.PI;
            tailTop.setPos(0.0f, 19.5f, 6.5f);
            tailTop.xRot = Mth.HALF_PI;
            tailTop.yRot = Mth.PI;
            rArmLower.setPos(1.0f, 19.5f, -3.0f);
            //rArmLower.rotateAngleX = -0.6981317f;
            lArmLower.setPos(-1.0f, 19.5f, -3.0f);
            //lArmLower.rotateAngleX = -0.6981317f;
            lLegLower.setPos(-1.0f, 12.0f, -3.0f);
            //lLegLower.rotateAngleX = -2.443461f;
            rLegLower.setPos(1.0f, 12.0f, -3.0f);
            //rLegLower.rotateAngleX = -2.443461f;
            face.setPos(0.0f, 11.0f, 1.0f);
            face.xRot += Mth.HALF_PI;
            head.setPos(0.0f, 11.0f, 1.0f);

            rLegUpper.xRot = Mth.cos(limbSwing * 0.5f) * 0.75f * partialTicks - Mth.HALF_PI;
            rArmUpper.xRot = Mth.cos(limbSwing * 0.5f) * 0.75f * partialTicks - Mth.HALF_PI;
            lArmUpper.xRot = Mth.cos(limbSwing * 0.5f) * 0.75f * partialTicks - Mth.HALF_PI;
            lLegUpper.xRot = Mth.cos(limbSwing * 0.5f) * 0.75f * partialTicks - Mth.HALF_PI;
            rLegLower.setPos(1.0f, 12.0f + (Mth.cos(rLegUpper.xRot) * 5), -3.0f - (5 + Mth.sin(rLegUpper.xRot) * 5));
            rArmLower.setPos(1.0f, 19.5f + (Mth.cos(rArmUpper.xRot) * 5), -3.0f - (5 + Mth.sin(rArmUpper.xRot) * 5));
            lArmLower.setPos(-1.0f, 19.5f + (Mth.cos(lArmUpper.xRot) * 5), -3.0f - (5 + Mth.sin(lArmUpper.xRot) * 5));
            lLegLower.setPos(-1.0f, 12.0f + (Mth.cos(lLegUpper.xRot) * 5), -3.0f - (5 + Mth.sin(lLegUpper.xRot) * 5));
            rLegLower.xRot = rLegUpper.xRot - 0.6981317f;
            rArmLower.xRot = rArmUpper.xRot + 0.6981317f;
            lLegLower.xRot = lLegUpper.xRot - 0.6981317f;
            lArmLower.xRot = lArmUpper.xRot + 0.6981317f;
        } else {
            body.setPos(0.0f, 16.0f, 0.0f);
            body.yRot = Mth.PI;
            body.xRot = 0.0f;
            lLegUpper.setPos(-1.0f, 14.0f, -3.5f);
            rLegUpper.setPos(1.0f, 14.0f, -3.5f);
            rArmUpper.setPos(1.0f, 14.0f, 3.5f);
            lArmUpper.setPos(-1.0f, 14.0f, 3.5f);
            tailBase.setPos(0.0f, 15.0f, 3.5f);
            tailBase.xRot = 0.0f;
            tailBase.yRot = Mth.PI;
            tailBase.zRot = 0.0f;
            tailMid.setPos(0.0f, 11.0f, 3.5f);
            tailMid.xRot = 0.0f;
            tailMid.yRot = Mth.PI;
            tailMid.zRot = 0.0f;
            tailTop.setPos(0.0f, 9.0f, 3.5f);
            tailTop.xRot = 0.0f;
            tailTop.yRot = Mth.PI;
            tailTop.zRot = 0.0f;
            face.setPos(0.0f, 15.0f, -5.0f);
            head.setPos(0.0f, 15.0f, -5.0f);

            rLegUpper.xRot = Mth.cos(limbSwing * 0.6662f) * 0.75f * limbSwingAmount;
            rArmUpper.xRot = Mth.cos(limbSwing * 0.6662f + Mth.PI) * 0.75f * limbSwingAmount;
            lLegUpper.xRot = Mth.cos(limbSwing * 0.6662f + Mth.PI) * 0.75f * limbSwingAmount;
            lArmUpper.xRot = Mth.cos(limbSwing * 0.6662f) * 0.75f * limbSwingAmount;
            rLegLower.setPos(1.0f, 19.0f - (5 - Mth.sin(rLegUpper.xRot + 1.570796327f) * 5), -3.5f - (Mth.cos(rLegUpper.xRot + 1.570796327f) * 5));
            rArmLower.setPos(1.0f, 19.0f - (5 - Mth.sin(rArmUpper.xRot + 1.570796327f) * 5), 3.5f - (Mth.cos(rArmUpper.xRot + 1.570796327f) * 5));
            lArmLower.setPos(-1.0f, 19.0f - (5 - Mth.sin(lArmUpper.xRot + 1.570796327f) * 5), 3.5f - (Mth.cos(lArmUpper.xRot + 1.570796327f) * 5));
            lLegLower.setPos(-1.0f, 19.0f - (5 - Mth.sin(lLegUpper.xRot + 1.570796327f) * 5), -3.5f - (Mth.cos(lLegUpper.xRot + 1.570796327f) * 5));
            rLegLower.xRot = rLegUpper.xRot;
            rArmLower.xRot = rArmUpper.xRot;
            lLegLower.xRot = lLegUpper.xRot;
            lArmLower.xRot = lArmUpper.xRot;

            tailBase.xRot = Mth.cos(limbSwing * 0.6662f) * 0.50f * limbSwingAmount;
            tailBase.zRot = Mth.cos(limbSwing * 0.6662f) * 0.50f * limbSwingAmount;
            tailMid.setPos(0.0f - (Mth.cos(tailBase.zRot + Mth.PI / 2.0f) * 3), 11.0f + (3 - Mth.sin(tailBase.xRot + Mth.PI / 2.0f) * 3), 3.5f - (Mth.cos(tailBase.xRot + Mth.PI / 2.0f) * 3));
            tailMid.xRot = tailBase.xRot + Mth.cos(limbSwing * 0.6662f) * 0.75f * limbSwingAmount;
            tailMid.zRot = tailBase.zRot + Mth.cos(limbSwing * 0.6662f) * 0.75f * limbSwingAmount;
            tailTop.setPos(0.0f - (Mth.cos(tailMid.zRot + Mth.PI / 2.0f) * 2), 9.0f + (2 - Mth.sin(tailMid.xRot + Mth.PI / 2.0f) * 2), 3.5f - (Mth.cos(tailMid.xRot + Mth.PI / 2.0f) * 2));
            tailTop.xRot = tailMid.xRot + Mth.cos(limbSwing * 0.6662f) * 1.75f * limbSwingAmount;
            tailTop.zRot = tailMid.xRot + Mth.cos(limbSwing * 0.6662f) * 1.75f * limbSwingAmount;
        }

        head.xRot = face.xRot;
        head.yRot = face.yRot;
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void translateToHand(HumanoidArm side, PoseStack stack) {
        stack.translate(0.09375f, 0.1875f, 0.0f);
    }
}
