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
    protected RandomSource rand;
    public float herps;

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
                        .addBox(-1F, -2F, -4F, 2, 4, 9),
                PartPose.offsetAndRotation(0F, 16F, 0F, 0F, 3.141593F, 0F));

        root.addOrReplaceChild("lLegUpper",
                CubeListBuilder.create().texOffs(7, 0)
                        .addBox(-1F, 0F, -0.5F, 1, 5, 1),
                PartPose.offsetAndRotation(-1F, 14F, -3.5F, 0F, 0F, 0F));

        root.addOrReplaceChild("rLegUpper",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0F, 0F, -0.5F, 1, 5, 1),
                PartPose.offsetAndRotation(1F, 14F, -3.5F, 0F, 0F, 0F));

        root.addOrReplaceChild("rArmUpper",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0F, 0F, -0.5F, 1, 5, 1),
                PartPose.offsetAndRotation(1F, 14F, 3.5F, 0F, 0F, 0F));

        root.addOrReplaceChild("lArmUpper",
                CubeListBuilder.create().texOffs(7, 0)
                        .addBox(-1F, 0F, -0.5F, 1, 5, 1),
                PartPose.offsetAndRotation(-1F, 14F, 3.5F, 0F, 0F, 0F));

        root.addOrReplaceChild("tailBase",
                CubeListBuilder.create().texOffs(20, 27)
                        .addBox(-0.5F, -4F, -0.5F, 1, 3, 1),
                PartPose.offsetAndRotation(0F, 15F, 3.5F, 0F, 3.141593F, 0F));

        root.addOrReplaceChild("tailMid",
                CubeListBuilder.create().texOffs(20, 24)
                        .addBox(-0.5F, -2F, -0.5F, 1, 2, 1),
                PartPose.offsetAndRotation(0F, 11F, 3.5F, 0F, 3.141593F, 0F));

        root.addOrReplaceChild("tailTop",
                CubeListBuilder.create().texOffs(20, 21)
                        .addBox(-0.5F, -2F, -0.5F, 1, 2, 1),
                PartPose.offsetAndRotation(0F, 9F, 3.5F, 0F, 3.141593F, 0F));

        root.addOrReplaceChild("rArmLower",
                CubeListBuilder.create().texOffs(0, 7)
                        .addBox(0F, 0F, -0.5F, 1, 5, 1),
                PartPose.offsetAndRotation(1F, 19F, 3.5F, 0F, 0F, 0F));

        root.addOrReplaceChild("lArmLower",
                CubeListBuilder.create().texOffs(12, 0)
                        .addBox(-1F, 0F, -0.5F, 1, 5, 1),
                PartPose.offsetAndRotation(-1F, 19F, 3.5F, 0F, 0F, 0F));

        root.addOrReplaceChild("lLegLower",
                CubeListBuilder.create().texOffs(12, 0)
                        .addBox(-1F, 0F, -0.5F, 1, 5, 1),
                PartPose.offsetAndRotation(-1F, 19F, -3.5F, 0F, 0F, 0F));

        root.addOrReplaceChild("rLegLower",
                CubeListBuilder.create().texOffs(0, 7)
                        .addBox(0F, 0F, -0.5F, 1, 5, 1),
                PartPose.offsetAndRotation(1F, 19F, -3.5F, 0F, 0F, 0F));

        root.addOrReplaceChild("face",
                CubeListBuilder.create().texOffs(0, 25)
                        .addBox(-2F, -1F, 0F, 4, 4, 3),
                PartPose.offsetAndRotation(0F, 15F, -5F, 0F, 3.141593F, 0F));

        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(25, 25)
                        .addBox(-3F, -2F, 0F, 6, 5, 2),
                PartPose.offsetAndRotation(0F, 15F, -5F, 0F, 3.141593F, 0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(VMonkeyEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        face.xRot = headPitch * Mth.DEG_TO_RAD + herps;
        face.yRot = netHeadYaw * Mth.DEG_TO_RAD + Mth.PI;
        head.xRot = face.xRot;
        head.yRot = face.yRot;
    }

    @Override
    public ModelPart root() {
        return root;
    }

    @Override
    public void prepareMobModel(VMonkeyEntity entity, float f, float f1, float f2) {
        if (entity.isOrderedToSit()) {
            body.setPos(0F, 20F, 0F);
            body.xRot = 0.9320058F;
            body.yRot = 3.141593F;
            lLegUpper.setPos(-1F, 16F, -1.5F);
            lLegUpper.xRot = -0.2792527F;
            rLegUpper.setPos(1F, 16F, -1.5F);
            rLegUpper.xRot = -0.2792527F;
            rLegUpper.yRot = 0.005817764F;
            rArmUpper.setPos(1F, 22F, 3.5F);
            rArmUpper.xRot = -2.142101F;
            lArmUpper.setPos(-1F, 22F, 3.5F);
            lArmUpper.xRot = -2.142043F;
            tailBase.setPos(0F, 22F, 2.466667F);
            tailBase.xRot = 1.902409F;
            tailBase.yRot = 3.141593F;
            tailMid.setPos(0F, 23.3F, 5.966667F);
            tailMid.xRot = 1.570796F;
            tailMid.yRot = 2.111848F;
            tailMid.zRot = -0.2617994F;
            tailTop.setPos(-1F, 23.2F, 7F);
            tailTop.xRot = 1.570796F;
            tailTop.yRot = 0.8377581F;
            tailTop.zRot = 0.01745329F;
            rArmLower.setPos(1F, 19F, -0.5F);
            rArmLower.xRot = -0.1489348F;
            lArmLower.setPos(-1F, 19F, -0.3F);
            lArmLower.xRot = -0.1492257F;
            lLegLower.setPos(-1F, 21F, -2.8F);
            lLegLower.xRot = -0.9599311F;
            rLegLower.setPos(1F, 21F, -2.833333F);
            rLegLower.xRot = -0.9599311F;
            face.setPos(0F, 15F, -3F);
            head.setPos(0F, 15F, -3F);
            herps = 0;
        } else if (entity.isClimbing()) {
            body.xRot = 1.570796F;
            body.setPos(0F, 16F, 0F);
            lLegUpper.setPos(-1F, 12F, 2F);
            //lLegUpper.rotateAngleX = -1.570796F;    
            rLegUpper.setPos(1F, 12F, 2F);
            //rLegUpper.rotateAngleX = -1.570796F;        
            rArmUpper.setPos(1F, 19.5F, 2F);
            //rArmUpper.rotateAngleX = -1.570796F;        
            lArmUpper.setPos(-1F, 19.5F, 2F);
            //lArmUpper.rotateAngleX = -1.570796F;        
            tailBase.setPos(0F, 19.5F, 0.5F);
            tailBase.xRot = 1.570796F;
            tailBase.yRot = 3.141593F;
            tailMid.setPos(0F, 19.5F, 4.5F);

            tailMid.xRot = 1.570796F;
            tailMid.yRot = 3.141593F;
            tailTop.setPos(0F, 19.5F, 6.5F);
            tailTop.xRot = 1.570796F;
            tailTop.yRot = 3.141593F;
            rArmLower.setPos(1F, 19.5F, -3F);
            //rArmLower.rotateAngleX = -0.6981317F;
            lArmLower.setPos(-1F, 19.5F, -3F);
            //lArmLower.rotateAngleX = -0.6981317F;        
            lLegLower.setPos(-1F, 12F, -3F);
            //lLegLower.rotateAngleX = -2.443461F;    
            rLegLower.setPos(1F, 12F, -3F);
            //rLegLower.rotateAngleX = -2.443461F;        
            face.setPos(0F, 11F, 1F);
            herps = 1.570796F;
            head.setPos(0F, 11F, 1F);
            head.xRot = 1.570796F;

            rLegUpper.xRot = Mth.cos(f * .5F) * .75F * f2 - 1.570796F;
            rArmUpper.xRot = Mth.cos(f * .5F) * .75F * f2 - 1.570796F;
            lArmUpper.xRot = Mth.cos(f * .5F) * .75F * f2 - 1.570796F;
            lLegUpper.xRot = Mth.cos(f * .5F) * .75F * f2 - 1.570796F;
            rLegLower.setPos(1F, 12F + (Mth.cos(rLegUpper.xRot) * 5), -3F - (5 + Mth.sin(rLegUpper.xRot) * 5));
            rArmLower.setPos(1F, 19.5F + (Mth.cos(rArmUpper.xRot) * 5), -3F - (5 + Mth.sin(rArmUpper.xRot) * 5));
            lArmLower.setPos(-1F, 19.5F + (Mth.cos(lArmUpper.xRot) * 5), -3F - (5 + Mth.sin(lArmUpper.xRot) * 5));
            lLegLower.setPos(-1F, 12F + (Mth.cos(lLegUpper.xRot) * 5), -3F - (5 + Mth.sin(lLegUpper.xRot) * 5));
            rLegLower.xRot = rLegUpper.xRot - 0.6981317F;
            rArmLower.xRot = rArmUpper.xRot + 0.6981317F;
            lLegLower.xRot = lLegUpper.xRot - 0.6981317F;
            lArmLower.xRot = lArmUpper.xRot + 0.6981317F;
        } else {
            body.setPos(0F, 16F, 0F);
            body.yRot = 3.141593F;
            body.xRot = 0F;
            lLegUpper.setPos(-1F, 14F, -3.5F);
            rLegUpper.setPos(1F, 14F, -3.5F);
            rArmUpper.setPos(1F, 14F, 3.5F);
            lArmUpper.setPos(-1F, 14F, 3.5F);
            tailBase.setPos(0F, 15F, 3.5F);
            tailBase.xRot = 0F;
            tailBase.yRot = 3.141593F;
            tailBase.zRot = 0F;
            tailMid.setPos(0F, 11F, 3.5F);
            tailMid.xRot = 0F;
            tailMid.yRot = 3.141593F;
            tailMid.zRot = 0F;
            tailTop.setPos(0F, 9F, 3.5F);
            tailTop.xRot = 0F;
            tailTop.yRot = 3.141593F;
            tailTop.zRot = 0F;
            face.setPos(0F, 15F, -5F);
            head.setPos(0F, 15F, -5F);

            rLegUpper.xRot = Mth.cos(f * 0.6662F) * .75F * f1;
            rArmUpper.xRot = Mth.cos(f * 0.6662F + 3.141593F) * .75F * f1;
            lLegUpper.xRot = Mth.cos(f * 0.6662F + 3.141593F) * .75F * f1;
            lArmUpper.xRot = Mth.cos(f * 0.6662F) * .75F * f1;
            rLegLower.setPos(1F, 19F - (5 - Mth.sin(rLegUpper.xRot + 1.570796327F) * 5), -3.5F - (Mth.cos(rLegUpper.xRot + 1.570796327F) * 5));
            rArmLower.setPos(1F, 19F - (5 - Mth.sin(rArmUpper.xRot + 1.570796327F) * 5), 3.5F - (Mth.cos(rArmUpper.xRot + 1.570796327F) * 5));
            lArmLower.setPos(-1F, 19F - (5 - Mth.sin(lArmUpper.xRot + 1.570796327F) * 5), 3.5F - (Mth.cos(lArmUpper.xRot + 1.570796327F) * 5));
            lLegLower.setPos(-1F, 19F - (5 - Mth.sin(lLegUpper.xRot + 1.570796327F) * 5), -3.5F - (Mth.cos(lLegUpper.xRot + 1.570796327F) * 5));
            rLegLower.xRot = rLegUpper.xRot;
            rArmLower.xRot = rArmUpper.xRot;
            lLegLower.xRot = lLegUpper.xRot;
            lArmLower.xRot = lArmUpper.xRot;

            tailBase.xRot = Mth.cos(f * 0.6662F) * .50F * f1;
            tailBase.zRot = Mth.cos(f * 0.6662F) * .50F * f1;
            tailMid.setPos(0F - (Mth.cos(tailBase.zRot + Mth.PI / 2F) * 3), 11F + (3 - Mth.sin(tailBase.xRot + Mth.PI / 2F) * 3), 3.5F - (Mth.cos(tailBase.xRot + Mth.PI / 2F) * 3));
            tailMid.xRot = tailBase.xRot + Mth.cos(f * 0.6662F) * .75F * f1;
            tailMid.zRot = tailBase.zRot + Mth.cos(f * 0.6662F) * .75F * f1;
            tailTop.setPos(0F - (Mth.cos(tailMid.zRot + Mth.PI / 2F) * 2), 9F + (2 - Mth.sin(tailMid.xRot + Mth.PI / 2F) * 2), 3.5F - (Mth.cos(tailMid.xRot + Mth.PI / 2F) * 2));
            tailTop.xRot = tailMid.xRot + Mth.cos(f * 0.6662F) * 1.75F * f1;
            tailTop.zRot = tailMid.xRot + Mth.cos(f * 0.6662F) * 1.75F * f1;
            herps = 0;
        }
    }

    @Override
    public void translateToHand(HumanoidArm side, PoseStack stack) {
        stack.translate(0.09375F, 0.1875F, 0.0F);
    }
}
