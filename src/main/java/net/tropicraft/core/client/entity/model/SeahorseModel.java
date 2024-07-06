package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.underdasea.SeahorseEntity;

public class SeahorseModel extends HierarchicalModel<SeahorseEntity> {
    private final ModelPart root;
    private final ModelPart head1;
    private final ModelPart snout1;
    private final ModelPart snout2;
    private final ModelPart snout3;
    private final ModelPart eye1;
    private final ModelPart eye2;
    private final ModelPart fin2;
    private final ModelPart fin4;
    private final ModelPart fin3;
    private final ModelPart neck1;
    private final ModelPart neck2;
    private final ModelPart belly;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart tail3;
    private final ModelPart tail4;
    private final ModelPart tail5;
    private final ModelPart tail6;
    private final ModelPart tail7;
    private final ModelPart tail8;
    private final ModelPart tail9;
    private final ModelPart tail10;
    private final ModelPart tail11;
    private final ModelPart fin1;

    public SeahorseModel(ModelPart root) {
        this.root = root;
        head1 = root.getChild("head1");
        snout1 = root.getChild("snout1");
        snout2 = root.getChild("snout2");
        snout3 = root.getChild("snout3");
        eye1 = root.getChild("eye1");
        eye2 = root.getChild("eye2");
        fin2 = root.getChild("fin2");
        fin4 = root.getChild("fin4");
        fin3 = root.getChild("fin3");
        neck1 = root.getChild("neck1");
        neck2 = root.getChild("neck2");
        belly = root.getChild("belly");
        tail1 = root.getChild("tail1");
        tail2 = root.getChild("tail2");
        tail3 = root.getChild("tail3");
        tail4 = root.getChild("tail4");
        tail5 = root.getChild("tail5");
        tail6 = root.getChild("tail6");
        tail7 = root.getChild("tail7");
        tail8 = root.getChild("tail8");
        tail9 = root.getChild("tail9");
        tail10 = root.getChild("tail10");
        tail11 = root.getChild("tail11");
        fin1 = root.getChild("fin1");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head1",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-2.5f, -2.5f, -2.5f, 5, 5, 5),
                PartPose.offsetAndRotation(1.0f, -36.0f, 0.5f, 0.0f, 0.0f, -0.7060349f));

        root.addOrReplaceChild("snout1",
                CubeListBuilder.create().texOffs(20, 0)
                        .addBox(-1.5f, -1.0f, -1.5f, 3, 3, 4),
                PartPose.offsetAndRotation(-2.448189f, -33.97269f, 2.980232E-08F, 0.0f, 0.0f, -0.7060349f));

        root.addOrReplaceChild("snout2",
                CubeListBuilder.create().texOffs(34, 0)
                        .addBox(-2.5f, -0.5f, -0.5f, 5, 2, 2),
                PartPose.offsetAndRotation(-5.491952f, -31.3774f, 2.980232E-08F, 0.0f, 0.0f, -0.7060349f));

        root.addOrReplaceChild("snout3",
                CubeListBuilder.create().texOffs(23, 7)
                        .addBox(-0.5f, -1.0f, -1.0f, 1, 3, 3),
                PartPose.offsetAndRotation(-7.54649f, -29.62558f, 0.0f, 0.0f, 0.0f, -1.055101f));

        root.addOrReplaceChild("eye1",
                CubeListBuilder.create().texOffs(40, 4)
                        .addBox(-1.0f, -1.0f, -0.5f, 2, 2, 1),
                PartPose.offsetAndRotation(-2.955017f, -34.83473f, -2.0f, -0.1802033f, 0.1073159f, -0.7155942f));

        root.addOrReplaceChild("eye2",
                CubeListBuilder.create().texOffs(40, 4)
                        .addBox(-1.0f, -1.0f, -0.5f, 2, 2, 1),
                PartPose.offsetAndRotation(-2.958766f, -34.83232f, 3.0f, -0.1327665f, 2.978997f, -2.432569f));

        root.addOrReplaceChild("fin2",
                CubeListBuilder.create().texOffs(39, 15)
                        .addBox(-3.0f, -2.5f, 0.0f, 6, 5, 0),
                PartPose.offsetAndRotation(1.222835f, -38.81833f, 0.5f, 0.0f, 0.0f, -0.1043443f));

        root.addOrReplaceChild("fin4",
                CubeListBuilder.create().texOffs(36, 9)
                        .addBox(-4.0f, -2.5f, 0.0f, 4, 5, 0),
                PartPose.offsetAndRotation(1.000001f, -36.0f, -2.0f, -0.2562083f, -2.679784f, 0.4709548f));

        root.addOrReplaceChild("fin3",
                CubeListBuilder.create().texOffs(45, 9)
                        .addBox(-4.0f, -2.5f, 0.0f, 4, 5, 0),
                PartPose.offsetAndRotation(1.000001f, -36.0f, 3.0f, 0.2562083f, 2.679784f, 0.4709548f));

        root.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(0, 10)
                        .addBox(-2.0f, -2.0f, -2.0f, 4, 4, 4),
                PartPose.offsetAndRotation(3.5f, -33.5f, 0.5f, 0.0f, 0.0f, -0.7853982f));

        root.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(0, 18)
                        .addBox(-2.5f, -2.0f, -2.5f, 5, 4, 5),
                PartPose.offsetAndRotation(4.999997f, -31.0f, 0.5f, 0.0f, 0.0f, -0.349066f));

        root.addOrReplaceChild("belly",
                CubeListBuilder.create().texOffs(0, 27)
                        .addBox(-3.5f, 0.0f, -3.0f, 7, 8, 6),
                PartPose.offset(5.0f, -30.0f, 0.5f));

        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 18)
                        .addBox(-2.5f, 0.0f, -2.5f, 5, 4, 5),
                PartPose.offsetAndRotation(5.5f, -22.5f, 0.5f, 0.0f, 0.0f, 0.08726645f));

        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(0, 41)
                        .addBox(-2.0f, 0.0f, -2.0f, 4, 4, 4),
                PartPose.offsetAndRotation(5.0f, -19.0f, 0.5f, 0.0f, 0.0f, 0.3490658f));

        root.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(0, 49)
                        .addBox(-2.0f, 0.0f, -1.5f, 3, 4, 3),
                PartPose.offsetAndRotation(4.5f, -15.5f, 0.5f, 0.0f, 0.0f, 0.6981316f));

        root.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(0, 56)
                        .addBox(-1.0f, 0.0f, -1.0f, 2, 4, 2),
                PartPose.offsetAndRotation(2.652397f, -12.89918f, 0.5f, 0.0f, 0.0f, 1.466756f));

        root.addOrReplaceChild("tail5",
                CubeListBuilder.create().texOffs(8, 56)
                        .addBox(-0.5f, 0.0f, -0.5f, 1, 2, 1),
                PartPose.offsetAndRotation(-0.8942064f, -12.51931f, 0.5f, 0.0f, 0.0f, 1.947916f));

        root.addOrReplaceChild("tail6",
                CubeListBuilder.create().texOffs(12, 56)
                        .addBox(-0.5f, 0.0f, -0.5f, 1, 2, 1),
                PartPose.offsetAndRotation(-2.551666f, -13.06961f, 0.5f, 0.0f, 0.0f, 2.471515f));

        root.addOrReplaceChild("tail7",
                CubeListBuilder.create().texOffs(12, 56)
                        .addBox(-0.5f, 0.0f, -0.5f, 1, 2, 1),
                PartPose.offsetAndRotation(-3.685031f, -14.47157f, 0.5f, 0.0f, 0.0f, -3.113539f));

        root.addOrReplaceChild("tail8",
                CubeListBuilder.create().texOffs(12, 56)
                        .addBox(-0.5f, 0.0f, -0.5f, 1, 2, 1),
                PartPose.offsetAndRotation(-3.770199f, -16.05041f, 0.5f, 0.0f, 0.0f, -2.415407f));

        root.addOrReplaceChild("tail9",
                CubeListBuilder.create().texOffs(12, 56)
                        .addBox(-0.5f, 0.0f, -0.5f, 1, 2, 1),
                PartPose.offsetAndRotation(-2.846481f, -17.36065f, 0.5f, 0.0f, 0.0f, -1.542743f));

        root.addOrReplaceChild("tail10",
                CubeListBuilder.create().texOffs(12, 56)
                        .addBox(-0.5f, 0.0f, -0.5f, 1, 2, 1),
                PartPose.offsetAndRotation(-0.2576861f, -15.77428f, 0.5f, 0.0f, 0.0f, 2.659437f));

        root.addOrReplaceChild("tail11",
                CubeListBuilder.create().texOffs(12, 56)
                        .addBox(-0.5f, -1.0f, -0.5f, 1, 2, 1),
                PartPose.offsetAndRotation(-0.856306f, -15.47153f, 0.5f, 0.0f, 0.0f, -2.415407f));

        root.addOrReplaceChild("fin1",
                CubeListBuilder.create().texOffs(40, 22)
                        .addBox(-2.5f, -4.0f, 0.0f, 5, 8, 0),
                PartPose.offsetAndRotation(8.5f, -20.0f, 0.5f, 0.0f, 0.0f, 0.2188137f));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(SeahorseEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        fin2.resetPose();
        fin2.zRot = Mth.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
