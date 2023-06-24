package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.underdasea.SharkEntity;

public class SharkModel extends HierarchicalModel<SharkEntity> {
    private final ModelPart root;
    private final ModelPart Head1;
    private final ModelPart Head3;
    private final ModelPart Body1Upper;
    private final ModelPart Body1Lower;
    private final ModelPart Body2Upper;
    private final ModelPart Body2Lower;
    private final ModelPart Body3UpperLeft;
    private final ModelPart Body3LowerLeft;
    private final ModelPart Body3LowerRight;
    private final ModelPart FinPectoralLeft;
    private final ModelPart FinPectoralRight;
    private final ModelPart FinDorsal;
    private final ModelPart FinPelvicLeft;
    private final ModelPart FinPelvicRight;
    private final ModelPart FinAdipose;
    private final ModelPart FinAnal;
    private final ModelPart FinCaudalUpper;
    private final ModelPart FinCaudalLower;
    private final ModelPart Body3Lower;
    private final ModelPart Body4Lower;
    private final ModelPart Head2;

    public SharkModel(ModelPart root) {
        this.root = root;
        Head1 = root.getChild("Head1");
        Head3 = root.getChild("Head3");
        Body1Upper = root.getChild("Body1Upper");
        Body1Lower = root.getChild("Body1Lower");
        Body2Upper = root.getChild("Body2Upper");
        Body2Lower = root.getChild("Body2Lower");
        Body3UpperLeft = root.getChild("Body3UpperLeft");
        Body3LowerLeft = root.getChild("Body3LowerLeft");
        Body3LowerRight = root.getChild("Body3LowerRight");
        FinPectoralLeft = root.getChild("FinPectoralLeft");
        FinPectoralRight = root.getChild("FinPectoralRight");
        FinDorsal = root.getChild("FinDorsal");
        FinPelvicLeft = root.getChild("FinPelvicLeft");
        FinPelvicRight = root.getChild("FinPelvicRight");
        FinAdipose = root.getChild("FinAdipose");
        FinAnal = root.getChild("FinAnal");
        FinCaudalUpper = root.getChild("FinCaudalUpper");
        FinCaudalLower = root.getChild("FinCaudalLower");
        Body3Lower = root.getChild("Body3Lower");
        Body4Lower = root.getChild("Body4Lower");
        Head2 = root.getChild("Head2");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("Head1",
                CubeListBuilder.create().texOffs(0, 24).mirror()
                        .addBox(-8F, -11.8F, -2.6F, 16, 6, 2),
                PartPose.offsetAndRotation(0F, 0.5F, -14F, 1.527163F, 0F, 0F));

        root.addOrReplaceChild("Head3",
                CubeListBuilder.create().texOffs(0, 46).mirror()
                        .addBox(-2.5F, -7F, -3.9F, 5, 14, 2),
                PartPose.offsetAndRotation(0F, 0.5F, -14F, 1.48353F, 0F, 0F));

        root.addOrReplaceChild("Body1Upper",
                CubeListBuilder.create().texOffs(18, 0).mirror()
                        .addBox(-2.5F, -17F, 0F, 5, 18, 6),
                PartPose.offsetAndRotation(0F, 0F, 3F, 1.780236F, 0F, 0F));

        root.addOrReplaceChild("Body1Lower",
                CubeListBuilder.create().texOffs(28, 47).mirror()
                        .addBox(-4F, -11F, -5F, 8, 12, 5),
                PartPose.offsetAndRotation(0F, 0F, 3F, 1.570796F, 0F, 0F));

        root.addOrReplaceChild("Body2Upper",
                CubeListBuilder.create().texOffs(40, 0).mirror()
                        .addBox(-2F, -0.8F, 0F, 4, 21, 6),
                PartPose.offsetAndRotation(0F, 0F, 3F, 1.48353F, 0F, 0F));

        root.addOrReplaceChild("Body2Lower",
                CubeListBuilder.create().texOffs(52, 39).mirror()
                        .addBox(-3F, 0F, -5F, 6, 20, 5),
                PartPose.offsetAndRotation(0F, 0F, 3F, 1.623156F, 0F, 0F));

        root.addOrReplaceChild("Body3UpperLeft",
                CubeListBuilder.create().texOffs(60, 0).mirror()
                        .addBox(-1F, -0.3F, -1F, 2, 15, 5),
                PartPose.offsetAndRotation(0F, 0F, 22F, 1.48353F, 0F, 0F));

        root.addOrReplaceChild("Body3LowerLeft",
                CubeListBuilder.create().texOffs(74, 45).mirror()
                        .addBox(0F, 0F, -4F, 2, 14, 5),
                PartPose.offsetAndRotation(0F, 0F, 22F, 1.692969F, -0.0698132F, 0F));

        root.addOrReplaceChild("Body3LowerRight",
                CubeListBuilder.create().texOffs(74, 45).mirror()
                        .addBox(-2F, 0F, -4F, 2, 14, 5),
                PartPose.offsetAndRotation(0F, 0F, 22F, 1.692969F, 0.0698132F, 0F));

        root.addOrReplaceChild("FinPectoralLeft",
                CubeListBuilder.create().texOffs(88, 57).mirror()
                        .addBox(0F, 0F, 0F, 14, 7, 0),
                PartPose.offsetAndRotation(4F, 4F, -7F, 2.007129F, -0.7853982F, 0.4363323F));

        root.addOrReplaceChild("FinPectoralRight",
                CubeListBuilder.create().texOffs(88, 57).mirror()
                        .addBox(-14F, 0F, 0F, 14, 7, 0),
                PartPose.offsetAndRotation(-4F, 4F, -7F, 2.007129F, 0.7853982F, -0.4363323F));

        root.addOrReplaceChild("FinDorsal",
                CubeListBuilder.create().texOffs(94, -7).mirror()
                        .addBox(0F, -15F, -2F, 0, 14, 7),
                PartPose.offsetAndRotation(0F, -4F, 4F, -0.5235988F, 0F, 0F));

        root.addOrReplaceChild("FinPelvicLeft",
                CubeListBuilder.create().texOffs(96, 52).mirror()
                        .addBox(0F, 0F, 0F, 5, 3, 0),
                PartPose.offsetAndRotation(3F, 4F, 17F, 2.181662F, -0.7853982F, 0.6981317F));

        root.addOrReplaceChild("FinPelvicRight",
                CubeListBuilder.create().texOffs(96, 52).mirror()
                        .addBox(-5F, 0F, 0F, 5, 3, 0),
                PartPose.offsetAndRotation(-3F, 4F, 17F, 2.181662F, 0.7853982F, -0.6981317F));

        root.addOrReplaceChild("FinAdipose",
                CubeListBuilder.create().texOffs(109, -3).mirror()
                        .addBox(0F, -5F, 0F, 0, 5, 3),
                PartPose.offsetAndRotation(0F, -3.8F, 24F, -0.7853982F, 0F, 0F));

        root.addOrReplaceChild("FinAnal",
                CubeListBuilder.create().texOffs(108, 47).mirror()
                        .addBox(0F, 0F, 0F, 0, 5, 3),
                PartPose.offsetAndRotation(0F, 3.6F, 25F, 0.8726646F, 0F, 0F));

        root.addOrReplaceChild("FinCaudalUpper",
                CubeListBuilder.create().texOffs(116, -6).mirror()
                        .addBox(0F, -20F, -2F, 0, 20, 6),
                PartPose.offsetAndRotation(0F, 0F, 35F, -0.9599311F, 0F, 0F));

        root.addOrReplaceChild("FinCaudalLower",
                CubeListBuilder.create().texOffs(116, 46).mirror()
                        .addBox(0F, -12.53333F, -4F, 0, 12, 6),
                PartPose.offsetAndRotation(0F, 0F, 35F, -2.356194F, 0F, 0F));

        root.addOrReplaceChild("Body3Lower",
                CubeListBuilder.create().texOffs(14, 48).mirror()
                        .addBox(3F, -21F, -5.6F, 2, 11, 5),
                PartPose.offsetAndRotation(0F, 0F, 3F, 1.500983F, 0.0907571F, 0F));

        root.addOrReplaceChild("Body4Lower",
                CubeListBuilder.create().texOffs(14, 48).mirror()
                        .addBox(-5F, -21F, -5.6F, 2, 11, 5),
                PartPose.offsetAndRotation(0F, 0F, 3F, 1.500983F, -0.0907571F, 0F));

        root.addOrReplaceChild("Head2",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                        .addBox(-3F, -8.8F, 0F, 6, 9, 3),
                PartPose.offsetAndRotation(0F, 0.5F, -14F, 1.919862F, 0F, 0F));

        return LayerDefinition.create(mesh, 128, 64);
    }

    @Override
    public void setupAnim(SharkEntity shark, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float timeScale = shark.isInWater() ? 0.05f : 0.2f;

        FinPectoralLeft.zRot = (0.4f - Mth.sin(ageInTicks * timeScale) * 0.3f);
        FinPectoralRight.zRot = (-0.4f - Mth.sin(ageInTicks * timeScale) * 0.3f);

        Body3UpperLeft.yRot = -Mth.sin(ageInTicks * timeScale) * 0.2f;
        Body3LowerLeft.yRot = -Mth.sin(ageInTicks * timeScale) * 0.2f;
        Body3LowerRight.yRot = -Mth.sin(ageInTicks * timeScale) * 0.2f;

        FinCaudalUpper.yRot = -Mth.sin(ageInTicks * timeScale) * 0.2f;
        FinCaudalLower.yRot = -Mth.sin(ageInTicks * timeScale) * 0.2f;
        FinAdipose.yRot = -Mth.sin(ageInTicks * timeScale) * 0.2f;
        FinAnal.yRot = -Mth.sin(ageInTicks * timeScale) * 0.2f;
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
