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
                CubeListBuilder.create().texOffs(0, 22).mirror()
                        .addBox(-5.0f, -3.0f, -2.0f, 7, 6, 4),
                PartPose.offsetAndRotation(0.0f, 19.0f, 0.0f, 0.0f, -1.570796f, 0.0f));

        root.addOrReplaceChild("dorsalFin1",
                CubeListBuilder.create().texOffs(24, 20).mirror()
                        .addBox(-0.5f, -0.5f, -0.5f, 1, 2, 10),
                PartPose.offset(0.0f, 15.5f, -5.0f));

        root.addOrReplaceChild("leftFin",
                CubeListBuilder.create().texOffs(12, 10).mirror()
                        .addBox(0.0f, -0.5f, -2.0f, 4, 1, 2),
                PartPose.offset(2.0f, 21.0f, -3.0f));

        root.addOrReplaceChild("rightFin",
                CubeListBuilder.create().texOffs(12, 7).mirror()
                        .addBox(-4.0f, -0.5f, -2.0f, 4, 1, 2),
                PartPose.offset(-2.0f, 21.0f, -3.0f));

        root.addOrReplaceChild("bottomFin",
                CubeListBuilder.create().texOffs(52, 0).mirror()
                        .addBox(-0.5f, 2.0f, -2.5f, 1, 3, 2),
                PartPose.offsetAndRotation(0.0f, 19.0f, 0.0f, 0.6981317f, 0.0f, 0.0f));

        PartDefinition head = root.addOrReplaceChild("head",
                CubeListBuilder.create().mirror()
                        .texOffs(46, 24)
                        .addBox(-1.5f, -3.0f, -3.0f, 3, 5, 3)
                        .texOffs(28, 0)
                        .addBox(-1.0f, -1.5f, -4.0f, 2, 3, 1)
                        .texOffs(22, 0)
                        .addBox(-0.5f, -0.5f, -6.0f, 1, 2, 2)
                        .texOffs(23, 24)
                        .addBox(-0.5f, -6.0f, -2.5f, 1, 3, 2),
                PartPose.offset(0.0f, 20.0f, -5.0f));

        head.addOrReplaceChild("sword",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                        .addBox(4.0f, -1.5f, -0.5f, 10, 1, 1),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 1.5707f, 0.0f));

        PartDefinition tail1 = root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 13).mirror()
                        .addBox(-1.5f, -2.0f, 0.0f, 3, 5, 4),
                PartPose.offset(0.0f, 19.0f, 2.0f));

        PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(0, 5).mirror()
                        .addBox(-1.0f, -1.5f, 0.0f, 2, 4, 4),
                PartPose.offset(0.0f, 0.0f, 4.0f));

        PartDefinition tail3 = tail2.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(46, 0).mirror()
                        .addBox(-0.5f, -1.5f, 0.0f, 1, 3, 2),
                PartPose.offset(0.0f, 1.0f, 4.0f));

        tail3.addOrReplaceChild("tailEndB",
                CubeListBuilder.create().texOffs(40, 0).mirror()
                        .addBox(-0.5f, 1.0f, -1.0f, 1, 5, 2),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.593411f, 0.0f, 0.0f));

        tail3.addOrReplaceChild("tailEndT",
                CubeListBuilder.create().texOffs(34, 0).mirror()
                        .addBox(-0.5f, 1.0f, -1.0f, 1, 5, 2),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 2.548179f, 0.0f, 0.0f));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(MarlinEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float yAngleRot = Mth.sin(ageInTicks * 0.25f);
        float zWaveFloat = yAngleRot * 0.165f;
        if (!entity.isInWater()) {
            float yWaveRot = Mth.sin(ageInTicks * 0.55f) * 0.260f;
            head.yRot = yWaveRot;
            tail1.yRot = yWaveRot;
            tail3.yRot = yWaveRot;
            leftFin.zRot = zWaveFloat + 0.523598f;
            rightFin.zRot = -yAngleRot * 0.165f - 0.523598f;
            leftFin.yRot = -1.5f;
            rightFin.yRot = 1.5f - zWaveFloat - 0.523598f;
        } else {
            head.yRot = yAngleRot * 0.135f;
            tail1.yRot = Mth.sin(ageInTicks * 0.35f) * 0.15f;
            tail3.yRot = Mth.sin(ageInTicks * 0.45f) * 0.16f;
            leftFin.zRot = zWaveFloat + 0.523598f;
            rightFin.zRot = -yAngleRot * 0.165f - 0.523598f;
            leftFin.yRot = -0.392699f;
            rightFin.yRot = 0.392699f;
        }
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
