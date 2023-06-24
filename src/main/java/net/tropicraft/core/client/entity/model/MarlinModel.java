package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.underdasea.MarlinEntity;

public class MarlinModel extends HierarchicalModel<MarlinEntity> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart dorsalFin1;
    private final ModelPart leftFin;
    private final ModelPart rightFin;
    private final ModelPart bottomFin;
    private final ModelPart head;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart sword;
    private final ModelPart tail3;
    private final ModelPart tailEndB;
    private final ModelPart tailEndT;

    public MarlinModel(ModelPart root) {
        this.root = root;
        head = root.getChild("head");
        body = root.getChild("body");
        sword = head.getChild("sword");
        tail1 = root.getChild("tail1");
        tail2 = tail1.getChild("tail2");
        tail3 = tail2.getChild("tail3");
        tailEndB = tail3.getChild("tailEndB");
        tailEndT = tail3.getChild("tailEndT");
        dorsalFin1 = root.getChild("dorsalFin1");
        leftFin = root.getChild("leftFin");
        rightFin = root.getChild("rightFin");
        bottomFin = root.getChild("bottomFin");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0,22).mirror()
                        .addBox(-5F, -3F, -2F, 7, 6, 4),
                PartPose.offsetAndRotation(0F, 19F, 0F, 0F, -1.570796F, 0F));

        root.addOrReplaceChild("dorsalFin1",
                CubeListBuilder.create().texOffs(24,20).mirror()
                        .addBox(-0.5F, -0.5F, -0.5F, 1, 2, 10),
                PartPose.offset(0F, 15.5F, -5F));

        root.addOrReplaceChild("leftFin",
                CubeListBuilder.create().texOffs(12, 10).mirror()
                        .addBox(0F, -0.5F, -2F, 4, 1, 2),
                PartPose.offset(2F, 21F, -3F));

        root.addOrReplaceChild("rightFin",
                CubeListBuilder.create().texOffs(12,7).mirror()
                        .addBox(-4F, -0.5F, -2F, 4, 1, 2),
                PartPose.offset(-2F, 21F, -3F));

        root.addOrReplaceChild("bottomFin",
                CubeListBuilder.create().texOffs(52, 0).mirror()
                        .addBox(-0.5F, 2F, -2.5F, 1, 3, 2),
                PartPose.offsetAndRotation(0F, 19F, 0F, 0.6981317F, 0F, 0F));

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create().mirror()
                        .texOffs(46, 24)
                        .addBox(-1.5F, -3F, -3F, 3, 5, 3)
                        .texOffs(28, 0)
                        .addBox(-1F, -1.5F, -4F, 2, 3, 1)
                        .texOffs(22, 0)
                        .addBox(-0.5F, -0.5F, -6F, 1, 2, 2)
                        .texOffs(23, 24)
                        .addBox(-0.5F, -6F, -2.5F, 1, 3, 2),
                PartPose.offset(0F, 20F, -5F));

        head.addOrReplaceChild("sword",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                        .addBox(4F, -1.5F, -0.5F, 10, 1, 1),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 1.5707F, 0F));

        PartDefinition tail1 = root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 13).mirror()
                        .addBox(-1.5F, -2F, 0F, 3, 5, 4),
                PartPose.offset(0F, 19F, 2F));

        PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(0, 5).mirror()
                        .addBox(-1F, -1.5F, 0F, 2, 4, 4),
                PartPose.offset(0F, 0F, 4F));

        PartDefinition tail3 = tail2.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(46, 0).mirror()
                        .addBox(-0.5F, -1.5F, 0F, 1, 3, 2),
                PartPose.offset(0F, 1F, 4F));

        tail3.addOrReplaceChild("tailEndB",
                CubeListBuilder.create().texOffs(40, 0).mirror()
                        .addBox(-0.5F, 1F, -1F, 1, 5, 2),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0.593411F, 0F, 0F));

        tail3.addOrReplaceChild("tailEndT",
                CubeListBuilder.create().texOffs(34, 0).mirror()
                        .addBox(-0.5F, 1F, -1F, 1, 5, 2),
                PartPose.offsetAndRotation(0F, 0F, 0F, 2.548179F, 0F, 0F));


        return LayerDefinition.create(mesh,64,32);
    }

    @Override
    public void setupAnim(MarlinEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float yAngleRot = Mth.sin(ageInTicks * .25F);
        float zWaveFloat = yAngleRot * .165F;
        if (!entity.isInWater()) {
            float yWaveRot = Mth.sin(ageInTicks * .55F) * .260F;
            head.yRot = yWaveRot;
            tail1.yRot = yWaveRot;
            tail3.yRot = yWaveRot;
            leftFin.zRot = zWaveFloat + 0.523598F;
            rightFin.zRot = -yAngleRot * .165F - 0.523598F;
            leftFin.yRot = -1.5F;
            rightFin.yRot = 1.5F - zWaveFloat - 0.523598F;
        } else {
            head.yRot = yAngleRot * .135F;
            tail1.yRot = Mth.sin(ageInTicks * .35F) * .150F;
            tail3.yRot = Mth.sin(ageInTicks * .45F) * .160F;
            leftFin.zRot = zWaveFloat + 0.523598F;
            rightFin.zRot = -yAngleRot * .165F - 0.523598F;
            leftFin.yRot = -0.392699F;
            rightFin.yRot = 0.392699F;
        }
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
