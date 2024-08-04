package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.passive.FailgullEntity;

public class FailgullModel extends HierarchicalModel<FailgullEntity> {
    private static final float WING_ROT_BASE = 90 * Mth.DEG_TO_RAD;
    private static final float LEFT_WING_ZROT = 93 * Mth.DEG_TO_RAD;
    private static final float RIGHT_WING_ZROT = 87 * Mth.DEG_TO_RAD;

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
                        .addBox(0.0f, 0.0f, 0.0f, 1, 0, 1, new CubeDeformation(0.001f)),
                PartPose.offset(-1.0f, 23.0f, 0.0f));

        root.addOrReplaceChild("baseFootRight",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0f, 0.0f, 0.0f, 1, 0, 1, new CubeDeformation(0.001f)),
                PartPose.offset(1.0f, 23.0f, 0.0f));

        root.addOrReplaceChild("lowerLeg1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0f, -1.0f, 0.0f, 1, 2, 0),
                PartPose.offset(1.0f, 22.0f, 1.0f));

        root.addOrReplaceChild("lowestBody",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0f, 0.0f, 0.0f, 3, 1, 4),
                PartPose.offset(-1.0f, 20.0f, 0.0f));

        root.addOrReplaceChild("lowerLeg2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0f, 0.0f, 0.0f, 1, 2, 0),
                PartPose.offset(-1.0f, 21.0f, 1.0f));

        root.addOrReplaceChild("lowerBody1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0f, 0.0f, 0.0f, 3, 1, 8),
                PartPose.offset(-1.0f, 19.0f, -1.0f));

        root.addOrReplaceChild("lowerBody2",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0f, 0.0f, 0.0f, 3, 1, 7),
                PartPose.offset(-1.0f, 18.0f, -2.0f));

        root.addOrReplaceChild("rightWing",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0f, 0.0f, 0.0f, 0, 2, 5, new CubeDeformation(0.001f)),
                PartPose.offsetAndRotation(-1.0f, 18.0f, 0.0f, -WING_ROT_BASE, 0, RIGHT_WING_ZROT));

        root.addOrReplaceChild("leftWing",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0f, 0.0f, 0.0f, 0, 2, 5, new CubeDeformation(0.001f)),
                PartPose.offsetAndRotation(2.0f, 18.0f, -2.0f, WING_ROT_BASE, 0, LEFT_WING_ZROT));

        root.addOrReplaceChild("neck",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0.0f, 0.0f, 0.0f, 3, 2, 2),
                PartPose.offset(-1.0f, 16.0f, -3.0f));

        root.addOrReplaceChild("beak",
                CubeListBuilder.create()
                        .texOffs(14, 0)
                        .addBox(0.0f, 0.0f, 0.0f, 1, 1, 2),
                PartPose.offset(0.0f, 17.0f, -5.0f));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(FailgullEntity failgull, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        lowerLeg1.xRot = Mth.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount;
        lowerLeg2.xRot = Mth.cos(limbSwing * 0.6662f + Mth.PI) * 1.4f * limbSwingAmount;

        rightWing.zRot = RIGHT_WING_ZROT + Mth.sin(ageInTicks * 0.4f) / 2;
        leftWing.zRot = LEFT_WING_ZROT - Mth.sin(ageInTicks * 0.4f) / 2;
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
