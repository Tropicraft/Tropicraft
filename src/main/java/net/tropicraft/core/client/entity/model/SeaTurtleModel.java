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
                        .addBox("bodypart1", -4.5F, -1F, -9F, 9, 2, 1, 0, 29)
                        .addBox("bodypart2", -3F, -2F, 1F, 6, 1, 4, 43, 40)
                        .addBox("bodypart3", -7F, -2F, -8F, 14, 4, 8, 0, 52)
                        .addBox("bodypart4", -5F, -1F, 0F, 10, 3, 8, 0, 41)
                        .addBox("bodypart5", -4F, -2.5F, -6F, 8, 2, 7, 0, 32)
                        .addBox("bodypart6", -6F, -0.5F, 0F, 1, 2, 7, 44, 55)
                        .addBox("bodypart7", 5F, -0.5F, 0F, 1, 2, 7, 44, 55)
                        .addBox("bodypart8", -4F, -0.5F, 8F, 8, 2, 2, 0, 25)
                , PartPose.offset(0F, 19F, 0F));

        body.addOrReplaceChild("frFlipper",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 20).addBox(-10F, 0F, -3F, 10, 1, 4),
                PartPose.offset(-7F, 2F, -6F));

        body.addOrReplaceChild("flFlipper",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 20).addBox(0F, 0F, -3F, 10, 1, 4),
                PartPose.offset(7F, 2F, -6F));

        body.addOrReplaceChild("head",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 0).addBox(-1.5F, -1.5F, -6F, 3, 3, 6),
                PartPose.offset(0F, 1F, -8F));

        body.addOrReplaceChild("rlFlipper",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 16).addBox(-7F, 0F, -1F, 7, 1, 3),
                PartPose.offset(-4F, 2F, 7F));

        body.addOrReplaceChild("rrFlipper",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 16).addBox(-1F, 0F, -1F, 7, 1, 3),
                PartPose.offset(4F, 2F, 7F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    public void setupAnim(SeaTurtleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float defFront = 0.3927F;
        float defFront2 = 0.3F;
        float defRear = .5F;

        if (!entity.isInWater() && !entity.isVehicle()) {
            limbSwingAmount *= 3f;
            limbSwing *= 2f;

            body.xRot = -Math.abs(Mth.sin(limbSwing * 0.25F) * 1.25F * limbSwingAmount) - .10F;
            frFlipper.xRot = defFront2;
            frFlipper.yRot = swimRotate(limbSwing, limbSwingAmount, 0.5F, 5F, 0, defFront);
            frFlipper.zRot = swimRotate(limbSwing, limbSwingAmount, 0.5f, 1.25f, 0, -defFront2);
            flFlipper.xRot = defFront2;
            flFlipper.yRot = swimRotate(limbSwing, limbSwingAmount, 0.5f, 5f, (float) Math.PI, -defFront2);
            flFlipper.zRot = -swimRotate(limbSwing, limbSwingAmount, 0.5f, 1.25f, 0, -defFront2);
            rrFlipper.xRot = 0F;
            rrFlipper.yRot = -swimRotate(limbSwing, limbSwingAmount, 3f, 2f, 0, defRear);
            rrFlipper.zRot = 0F;
            rlFlipper.xRot = 0F;
            rlFlipper.yRot = -swimRotate(limbSwing, limbSwingAmount, 3f, 2f, 0, -defRear);
            rlFlipper.zRot = 0F;
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
            rlFlipper.xRot = swimRotate(limbSwing, limbSwingAmount, 5f, 0.5f, Mth.PI / 4, 0);
            rrFlipper.xRot = swimRotate(limbSwing, limbSwingAmount, 5f, 0.5f, Mth.PI / 4, 0);
            rrFlipper.yRot = -0.5f;
            rlFlipper.yRot = 0.5f;
            rrFlipper.zRot = swimRotate(limbSwing, limbSwingAmount, 5f, 0.5f, 0, 0.5f);
            rlFlipper.zRot = swimRotate(limbSwing, limbSwingAmount, 5f, 0.5f, Mth.PI, -0.5f);
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
