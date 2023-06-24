package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.passive.FailgullEntity;

public class FailgullModel extends HierarchicalModel<FailgullEntity> {
    private final ModelPart root;
    private final ModelPart lowerLeg1;
    private final ModelPart lowerLeg2;
    private final ModelPart rightWing;
    private final ModelPart leftWing;

    public FailgullModel(ModelPart root) {
        this.root = root;
        lowerLeg1 = root.getChild("lowerLeg1");
        lowerLeg2 = root.getChild("lowerLeg2");
        rightWing = root.getChild("rightWing");
        leftWing = root.getChild("leftWing");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("baseFootLeft",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0F, 0F, 0F, 1, 0, 1),
                PartPose.offset(-1F, 23F, 0F));

        root.addOrReplaceChild("baseFootRight",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0F, 0F, 0F, 1, 0, 1),
                PartPose.offset(1F, 23F, 0F));

        root.addOrReplaceChild("lowerLeg1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0F, -1F, 0F, 1, 2, 0),
                PartPose.offset(1F, 22F, 1F));

        root.addOrReplaceChild("lowestBody",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0F, 0F, 0F, 3, 1, 4),
                PartPose.offset(-1F, 20F, 0F));

        root.addOrReplaceChild("lowerLeg2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0F, 0F, 0F, 1, 2, 0),
                PartPose.offset(-1F, 21F, 1F));

        root.addOrReplaceChild("lowerBody1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0F, 0F, 0F, 3, 1, 8),
                PartPose.offset(-1F, 19F, -1F));

        root.addOrReplaceChild("lowerBody2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0F, 0F, 0F, 3, 1, 7),
                PartPose.offset(-1F, 18F, -2F));

        root.addOrReplaceChild("rightWing",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0F, 0F, 0F, 0, 2, 5),
                PartPose.offsetAndRotation(-1F, 18F, 0F, -0.06981F, -0.06981F, 0));

        root.addOrReplaceChild("leftWing",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0F, 0F, 0F, 0, 2, 5),
                PartPose.offsetAndRotation(2F, 18F, 0F, -0.06981F, 0.06981F, 0));

        root.addOrReplaceChild("neck",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0F, 0F, 0F, 3, 2, 2),
                PartPose.offset(-1F, 16F, -3F));

        root.addOrReplaceChild("beak",
                CubeListBuilder.create()
                        .texOffs(14, 0)
                        .addBox(0F, 0F, 0F, 1, 1, 2),
                PartPose.offset(0F, 17F, -5F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(FailgullEntity failgull, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        lowerLeg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        lowerLeg2.xRot = Mth.cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount;
        rightWing.zRot = ageInTicks;
        leftWing.zRot = -ageInTicks;
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
