package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.SeaTurtleEntity;

public class SeaTurtleModel extends HierarchicalModel<SeaTurtleEntity> {
    private final ModelPart body;
    private final ModelPart frFlipper;
    private final ModelPart flFlipper;
    private final ModelPart head;
    private final ModelPart rlFlipper;
    private final ModelPart rrFlipper;

    public SeaTurtleModel(ModelPart root) {
        body = root.getChild("body");
        frFlipper = body.getChild("frFlipper");
        flFlipper = body.getChild("flFlipper");
        head = body.getChild("head");
        rlFlipper = body.getChild("rlFlipper");
        rrFlipper = body.getChild("rrFlipper");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create().mirror()
                        .addBox("bodypart1", -4.5f, -1.0f, -9.0f, 9, 2, 1, 0, 29)
                        .addBox("bodypart2", -3.0f, -2.0f, 1.0f, 6, 1, 4, 43, 40)
                        .addBox("bodypart3", -7.0f, -2.0f, -8.0f, 14, 4, 8, 0, 52)
                        .addBox("bodypart4", -5.0f, -1.0f, 0.0f, 10, 3, 8, 0, 41)
                        .addBox("bodypart5", -4.0f, -2.5f, -6.0f, 8, 2, 7, 0, 32)
                        .addBox("bodypart6", -6.0f, -0.5f, 0.0f, 1, 2, 7, 44, 55)
                        .addBox("bodypart7", 5.0f, -0.5f, 0.0f, 1, 2, 7, 44, 55)
                        .addBox("bodypart8", -4.0f, -0.5f, 8.0f, 8, 2, 2, 0, 25)
                , PartPose.offset(0.0f, 19.0f, 0.0f));

        body.addOrReplaceChild("frFlipper",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 20).addBox(-10.0f, 0.0f, -3.0f, 10, 1, 4),
                PartPose.offset(-7.0f, 2.0f, -6.0f));

        body.addOrReplaceChild("flFlipper",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 20).addBox(0.0f, 0.0f, -3.0f, 10, 1, 4),
                PartPose.offset(7.0f, 2.0f, -6.0f));

        body.addOrReplaceChild("head",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 0).addBox(-1.5f, -1.5f, -6.0f, 3, 3, 6),
                PartPose.offset(0.0f, 1.0f, -8.0f));

        body.addOrReplaceChild("rlFlipper",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 16).addBox(-7.0f, 0.0f, -1.0f, 7, 1, 3),
                PartPose.offset(-4.0f, 2.0f, 7.0f));

        body.addOrReplaceChild("rrFlipper",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 16).addBox(-1.0f, 0.0f, -1.0f, 7, 1, 3),
                PartPose.offset(4.0f, 2.0f, 7.0f));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(SeaTurtleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float defFront = 0.3927f;
        float defFront2 = 0.3f;
        float defRear = 0.5f;

        if (!entity.isInWater() && !entity.isVehicle()) {
            limbSwingAmount *= 3.0f;
            limbSwing *= 2.0f;

            body.xRot = -Math.abs(Mth.sin(limbSwing * 0.25f) * 1.25f * limbSwingAmount) - 0.10f;
            frFlipper.xRot = defFront2;
            frFlipper.yRot = swimRotate(limbSwing, limbSwingAmount, 0.5f, 5.0f, 0, defFront);
            frFlipper.zRot = swimRotate(limbSwing, limbSwingAmount, 0.5f, 1.25f, 0, -defFront2);
            flFlipper.xRot = defFront2;
            flFlipper.yRot = swimRotate(limbSwing, limbSwingAmount, 0.5f, 5.0f, (float) Math.PI, -defFront2);
            flFlipper.zRot = -swimRotate(limbSwing, limbSwingAmount, 0.5f, 1.25f, 0, -defFront2);
            rrFlipper.xRot = 0.0f;
            rrFlipper.yRot = -swimRotate(limbSwing, limbSwingAmount, 3.0f, 2.0f, 0, defRear);
            rrFlipper.zRot = 0.0f;
            rlFlipper.xRot = 0.0f;
            rlFlipper.yRot = -swimRotate(limbSwing, limbSwingAmount, 3.0f, 2.0f, 0, -defRear);
            rlFlipper.zRot = 0.0f;
        } else {
            limbSwingAmount *= 0.75f;
            limbSwing *= 0.1f;
            body.xRot = (float) Math.toRadians(headPitch);
            frFlipper.yRot = swimRotate(limbSwing, limbSwingAmount, 1.25f, 1.5f, 0, defFront);
            frFlipper.xRot = swimRotate(limbSwing, limbSwingAmount, 1.25f, 1.5f, Mth.PI / 4, defFront2 + 0.25f);
            frFlipper.zRot = 0;
            flFlipper.yRot = -swimRotate(limbSwing, limbSwingAmount, 1.25f, 1.5f, 0, defFront);
            flFlipper.zRot = 0;
            flFlipper.xRot = swimRotate(limbSwing, limbSwingAmount, 1.25f, 1.5f, Mth.PI / 4, defFront2 + 0.25f);
            rlFlipper.xRot = swimRotate(limbSwing, limbSwingAmount, 5.0f, 0.5f, Mth.PI / 4, 0);
            rrFlipper.xRot = swimRotate(limbSwing, limbSwingAmount, 5.0f, 0.5f, Mth.PI / 4, 0);
            rrFlipper.yRot = -0.5f;
            rlFlipper.yRot = 0.5f;
            rrFlipper.zRot = swimRotate(limbSwing, limbSwingAmount, 5.0f, 0.5f, 0, 0.5f);
            rlFlipper.zRot = swimRotate(limbSwing, limbSwingAmount, 5.0f, 0.5f, Mth.PI, -0.5f);
        }
    }

    @Override
    public ModelPart root() {
        return body;
    }

    private float swimRotate(float swing, float amount, float rot, float intensity, float rotOffset, float offset) {
        return Mth.cos(swing * rot + rotOffset) * amount * intensity + offset;
    }
}
