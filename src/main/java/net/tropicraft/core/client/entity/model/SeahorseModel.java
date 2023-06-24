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
                        .addBox(-2.5F, -2.5F, -2.5F, 5, 5, 5),
                PartPose.offset(1F, -36F, 0.5F));

        root.addOrReplaceChild("snout1",
                CubeListBuilder.create().texOffs(20, 0)
                        .addBox(-1.5F, -1F, -1.5F, 3, 3, 4),
                PartPose.offset(-2.448189F, -33.97269F, 2.980232E-08F));

        root.addOrReplaceChild("snout2",
                CubeListBuilder.create().texOffs(34, 0)
                        .addBox(-2.5F, -0.5F, -0.5F, 5, 2, 2),
                PartPose.offset(-5.491952F, -31.3774F, 2.980232E-08F));

        root.addOrReplaceChild("snout3",
                CubeListBuilder.create().texOffs(23, 7)
                        .addBox(-0.5F, -1F, -1F, 1, 3, 3),
                PartPose.offset(-7.54649F, -29.62558F, 0F));

        root.addOrReplaceChild("eye1",
                CubeListBuilder.create().texOffs(40, 4)
                        .addBox(-1F, -1F, -0.5F, 2, 2, 1),
                PartPose.offset(-2.955017F, -34.83473F, -2F));

        root.addOrReplaceChild("eye2",
                CubeListBuilder.create().texOffs(40, 4)
                        .addBox(-1F, -1F, -0.5F, 2, 2, 1),
                PartPose.offset(-2.958766F, -34.83232F, 3F));

        root.addOrReplaceChild("fin2",
                CubeListBuilder.create().texOffs(39, 15)
                        .addBox(-3F, -2.5F, 0F, 6, 5, 0),
                PartPose.offset(1.222835F, -38.81833F, 0.5F));

        root.addOrReplaceChild("fin4",
                CubeListBuilder.create().texOffs(36, 9)
                        .addBox(-4F, -2.5F, 0F, 4, 5, 0),
                PartPose.offset(1.000001F, -36F, -2F));

        root.addOrReplaceChild("fin3",
                CubeListBuilder.create().texOffs(45, 9)
                        .addBox(-4F, -2.5F, 0F, 4, 5, 0),
                PartPose.offset(1.000001F, -36F, 3F));

        root.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(0, 10)
                        .addBox(-2F, -2F, -2F, 4, 4, 4),
                PartPose.offset(3.5F, -33.5F, 0.5F));

        root.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(0, 18)
                        .addBox(-2.5F, -2F, -2.5F, 5, 4, 5),
                PartPose.offset(4.999997F, -31F, 0.5F));

        root.addOrReplaceChild("belly",
                CubeListBuilder.create().texOffs(0, 27)
                        .addBox(-3.5F, 0F, -3F, 7, 8, 6),
                PartPose.offset(5F, -30F, 0.5F));

        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 18)
                        .addBox(-2.5F, 0F, -2.5F, 5, 4, 5),
                PartPose.offset(5.5F, -22.5F, 0.5F));

        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(0, 41)
                        .addBox(-2F, 0F, -2F, 4, 4, 4),
                PartPose.offset(5F, -19F, 0.5F));

        root.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(0, 49)
                        .addBox(-2F, 0F, -1.5F, 3, 4, 3),
                PartPose.offset(4.5F, -15.5F, 0.5F));

        root.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(0, 56)
                        .addBox(-1F, 0F, -1F, 2, 4, 2),
                PartPose.offset(2.652397F, -12.89918F, 0.5F));

        root.addOrReplaceChild("tail5",
                CubeListBuilder.create().texOffs(8, 56)
                        .addBox(-0.5F, 0F, -0.5F, 1, 2, 1),
                PartPose.offset(-0.8942064F, -12.51931F, 0.5F));

        root.addOrReplaceChild("tail6",
                CubeListBuilder.create().texOffs(12, 56)
                        .addBox(-0.5F, 0F, -0.5F, 1, 2, 1),
                PartPose.offset(-2.551666F, -13.06961F, 0.5F));

        root.addOrReplaceChild("tail7",
                CubeListBuilder.create().texOffs(12, 56)
                        .addBox(-0.5F, 0F, -0.5F, 1, 2, 1),
                PartPose.offset(-3.685031F, -14.47157F, 0.5F));

        root.addOrReplaceChild("tail8",
                CubeListBuilder.create().texOffs(12, 56)
                        .addBox(-0.5F, 0F, -0.5F, 1, 2, 1),
                PartPose.offset(-3.770199F, -16.05041F, 0.5F));

        root.addOrReplaceChild("tail9",
                CubeListBuilder.create().texOffs(12, 56)
                        .addBox(-0.5F, 0F, -0.5F, 1, 2, 1),
                PartPose.offset(-2.846481F, -17.36065F, 0.5F));

        root.addOrReplaceChild("tail10",
                CubeListBuilder.create().texOffs(12, 56)
                        .addBox(-0.5F, 0F, -0.5F, 1, 2, 1),
                PartPose.offset(-0.2576861F, -15.77428F, 0.5F));

        root.addOrReplaceChild("tail11",
                CubeListBuilder.create().texOffs(12, 56)
                        .addBox(-0.5F, -1F, -0.5F, 1, 2, 1),
                PartPose.offset(-0.856306F, -15.47153F, 0.5F));

        root.addOrReplaceChild("fin1",
                CubeListBuilder.create().texOffs(40, 22)
                        .addBox(-2.5F, -4F, 0F, 5, 8, 0),
                PartPose.offset(8.5F, -20F, 0.5F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void prepareMobModel(SeahorseEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        head1.xRot = 0F;
        head1.yRot = 0F;
        head1.zRot = -0.7060349F;

        snout1.xRot = 0F;
        snout1.yRot = 0F;
        snout1.zRot = -0.7060349F;

        snout2.xRot = 0F;
        snout2.yRot = 0F;
        snout2.zRot = -0.7060349F;

        snout3.xRot = 0F;
        snout3.yRot = 0F;
        snout3.zRot = -1.055101F;

        eye1.xRot = -0.1802033F;
        eye1.yRot = 0.1073159F;
        eye1.zRot = -0.7155942F;

        eye2.xRot = -0.1327665F;
        eye2.yRot = 2.978997F;
        eye2.zRot = -2.432569F;

        fin2.xRot = 0F;
        fin2.yRot = 0F;
        fin2.zRot = -0.1043443F;

        fin4.xRot = -0.2562083F;
        fin4.yRot = -2.679784F;
        fin4.zRot = 0.4709548F;

        fin3.xRot = 0.2562083F;
        fin3.yRot = 2.679784F;
        fin3.zRot = 0.4709548F;

        neck1.xRot = 0F;
        neck1.yRot = 0F;
        neck1.zRot = -0.7853982F;

        neck2.xRot = 0F;
        neck2.yRot = 0F;
        neck2.zRot = -0.349066F;

        belly.xRot = 0F;
        belly.yRot = 0F;
        belly.zRot = 0F;

        tail1.xRot = 0F;
        tail1.yRot = 0F;
        tail1.zRot = 0.08726645F;

        tail2.xRot = 0F;
        tail2.yRot = 0F;
        tail2.zRot = 0.3490658F;

        tail3.xRot = 0F;
        tail3.yRot = 0F;
        tail3.zRot = 0.6981316F;

        tail4.xRot = 0F;
        tail4.yRot = 0F;
        tail4.zRot = 1.466756F;

        tail5.xRot = 0F;
        tail5.yRot = 0F;
        tail5.zRot = 1.947916F;

        tail6.xRot = 0F;
        tail6.yRot = 0F;
        tail6.zRot = 2.471515F;

        tail7.xRot = 0F;
        tail7.yRot = 0F;
        tail7.zRot = -3.113539F;

        tail8.xRot = 0F;
        tail8.yRot = 0F;
        tail8.zRot = -2.415407F;

        tail9.xRot = 0F;
        tail9.yRot = 0F;
        tail9.zRot = -1.542743F;

        tail10.xRot = 0F;
        tail10.yRot = 0F;
        tail10.zRot = 2.659437F;

        tail11.xRot = 0F;
        tail11.yRot = 0F;
        tail11.zRot = -2.415407F;

        fin1.xRot = 0F;
        fin1.yRot = 0F;
        fin1.zRot = 0.2188137F;
    }

    @Override
    public void setupAnim(SeahorseEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        fin2.zRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
