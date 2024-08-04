package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.underdasea.SharkEntity;

public class SharkModel extends HierarchicalModel<SharkEntity> {
    private final ModelPart root;
    private final ModelPart Body3UpperLeft;
    private final ModelPart Body3LowerLeft;
    private final ModelPart Body3LowerRight;
    private final ModelPart FinPectoralLeft;
    private final ModelPart FinPectoralRight;
    private final ModelPart FinAdipose;
    private final ModelPart FinAnal;
    private final ModelPart FinCaudalUpper;
    private final ModelPart FinCaudalLower;

    public SharkModel(ModelPart root) {
        this.root = root;
        Body3UpperLeft = root.getChild("Body3UpperLeft");
        Body3LowerLeft = root.getChild("Body3LowerLeft");
        Body3LowerRight = root.getChild("Body3LowerRight");
        FinPectoralLeft = root.getChild("FinPectoralLeft");
        FinPectoralRight = root.getChild("FinPectoralRight");
        FinAdipose = root.getChild("FinAdipose");
        FinAnal = root.getChild("FinAnal");
        FinCaudalUpper = root.getChild("FinCaudalUpper");
        FinCaudalLower = root.getChild("FinCaudalLower");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("Head1",
                CubeListBuilder.create().texOffs(0, 24).mirror()
                        .addBox(-8.0f, -11.8f, -2.6f, 16, 6, 2),
                PartPose.offsetAndRotation(0.0f, 0.5f, -14.0f, 1.527163f, 0.0f, 0.0f));

        root.addOrReplaceChild("Head3",
                CubeListBuilder.create().texOffs(0, 46).mirror()
                        .addBox(-2.5f, -7.0f, -3.9f, 5, 14, 2),
                PartPose.offsetAndRotation(0.0f, 0.5f, -14.0f, 1.48353f, 0.0f, 0.0f));

        root.addOrReplaceChild("Body1Upper",
                CubeListBuilder.create().texOffs(18, 0).mirror()
                        .addBox(-2.5f, -17.0f, 0.0f, 5, 18, 6),
                PartPose.offsetAndRotation(0.0f, 0.0f, 3.0f, 1.780236f, 0.0f, 0.0f));

        root.addOrReplaceChild("Body1Lower",
                CubeListBuilder.create().texOffs(28, 47).mirror()
                        .addBox(-4.0f, -11.0f, -5.0f, 8, 12, 5),
                PartPose.offsetAndRotation(0.0f, 0.0f, 3.0f, Mth.HALF_PI, 0.0f, 0.0f));

        root.addOrReplaceChild("Body2Upper",
                CubeListBuilder.create().texOffs(40, 0).mirror()
                        .addBox(-2.0f, -0.8f, 0.0f, 4, 21, 6),
                PartPose.offsetAndRotation(0.0f, 0.0f, 3.0f, 1.48353f, 0.0f, 0.0f));

        root.addOrReplaceChild("Body2Lower",
                CubeListBuilder.create().texOffs(52, 39).mirror()
                        .addBox(-3.0f, 0.0f, -5.0f, 6, 20, 5),
                PartPose.offsetAndRotation(0.0f, 0.0f, 3.0f, 1.623156f, 0.0f, 0.0f));

        root.addOrReplaceChild("Body3UpperLeft",
                CubeListBuilder.create().texOffs(60, 0).mirror()
                        .addBox(-1.0f, -0.3f, -1.0f, 2, 15, 5),
                PartPose.offsetAndRotation(0.0f, 0.0f, 22.0f, 1.48353f, 0.0f, 0.0f));

        root.addOrReplaceChild("Body3LowerLeft",
                CubeListBuilder.create().texOffs(74, 45).mirror()
                        .addBox(0.0f, 0.0f, -4.0f, 2, 14, 5),
                PartPose.offsetAndRotation(0.0f, 0.0f, 22.0f, 1.692969f, -0.0698132f, 0.0f));

        root.addOrReplaceChild("Body3LowerRight",
                CubeListBuilder.create().texOffs(74, 45).mirror()
                        .addBox(-2.0f, 0.0f, -4.0f, 2, 14, 5),
                PartPose.offsetAndRotation(0.0f, 0.0f, 22.0f, 1.692969f, 0.0698132f, 0.0f));

        root.addOrReplaceChild("FinPectoralLeft",
                CubeListBuilder.create().texOffs(88, 57)
                        .addBox(0.0f, 0.0f, 0.0f, 14, 7, 0, new CubeDeformation(0.001f)),
                PartPose.offsetAndRotation(4.0f, 4.0f, -7.0f, 2.007129f, -0.7853982f, 0.4363323f));

        root.addOrReplaceChild("FinPectoralRight",
                CubeListBuilder.create().texOffs(88, 57).mirror()
                        .addBox(-14.0f, 0.0f, 0.0f, 14, 7, 0, new CubeDeformation(0.001f)),
                PartPose.offsetAndRotation(-4.0f, 4.0f, -7.0f, 2.007129f, 0.7853982f, -0.4363323f));

        root.addOrReplaceChild("FinDorsal",
                CubeListBuilder.create().texOffs(94, -7).mirror()
                        .addBox(0.0f, -15.0f, -2.0f, 0, 14, 7),
                PartPose.offsetAndRotation(0.0f, -4.0f, 4.0f, -0.5235988f, 0.0f, 0.0f));

        root.addOrReplaceChild("FinPelvicLeft",
                CubeListBuilder.create().texOffs(96, 52)
                        .addBox(0.0f, 0.0f, 0.0f, 5, 3, 0, new CubeDeformation(0.001f)),
                PartPose.offsetAndRotation(3.0f, 4.0f, 17.0f, 2.181662f, -0.7853982f, 0.6981317f));

        root.addOrReplaceChild("FinPelvicRight",
                CubeListBuilder.create().texOffs(96, 52).mirror()
                        .addBox(-5.0f, 0.0f, 0.0f, 5, 3, 0, new CubeDeformation(0.001f)),
                PartPose.offsetAndRotation(-3.0f, 4.0f, 17.0f, 2.181662f, 0.7853982f, -0.6981317f));

        root.addOrReplaceChild("FinAdipose",
                CubeListBuilder.create().texOffs(109, -3).mirror()
                        .addBox(0.0f, -5.0f, 0.0f, 0, 5, 3),
                PartPose.offsetAndRotation(0.0f, -3.8f, 24.0f, -0.7853982f, 0.0f, 0.0f));

        root.addOrReplaceChild("FinAnal",
                CubeListBuilder.create().texOffs(108, 47).mirror()
                        .addBox(0.0f, 0.0f, 0.0f, 0, 5, 3),
                PartPose.offsetAndRotation(0.0f, 3.6f, 25.0f, 0.8726646f, 0.0f, 0.0f));

        root.addOrReplaceChild("FinCaudalUpper",
                CubeListBuilder.create().texOffs(116, -6).mirror()
                        .addBox(0.0f, -20.0f, -2.0f, 0, 20, 6),
                PartPose.offsetAndRotation(0.0f, 0.0f, 35.0f, -0.9599311f, 0.0f, 0.0f));

        root.addOrReplaceChild("FinCaudalLower",
                CubeListBuilder.create().texOffs(116, 46).mirror()
                        .addBox(0.0f, -12.53333f, -4.0f, 0, 12, 6),
                PartPose.offsetAndRotation(0.0f, 0.0f, 35.0f, -2.356194f, 0.0f, 0.0f));

        root.addOrReplaceChild("Body3Lower",
                CubeListBuilder.create().texOffs(14, 48).mirror()
                        .addBox(3.0f, -21.0f, -5.6f, 2, 11, 5),
                PartPose.offsetAndRotation(0.0f, 0.0f, 3.0f, 1.500983f, 0.0907571f, 0.0f));

        root.addOrReplaceChild("Body4Lower",
                CubeListBuilder.create().texOffs(14, 48).mirror()
                        .addBox(-5.0f, -21.0f, -5.6f, 2, 11, 5),
                PartPose.offsetAndRotation(0.0f, 0.0f, 3.0f, 1.500983f, -0.0907571f, 0.0f));

        root.addOrReplaceChild("Head2",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                        .addBox(-3.0f, -8.8f, 0.0f, 6, 9, 3),
                PartPose.offsetAndRotation(0.0f, 0.5f, -14.0f, 1.919862f, 0.0f, 0.0f));

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
