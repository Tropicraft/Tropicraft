package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.passive.ToucanEntity;

public class ToucanModel extends HierarchicalModel<ToucanEntity> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart beakBottom;
    private final ModelPart tailTop;
    private final ModelPart tailBottomLeft;
    private final ModelPart tailBottomRight;

    public ToucanModel(ModelPart root) {
        this.root = root;
        body = root.getChild("body_base");
        legLeft = body.getChild("leg_left");
        legRight = body.getChild("leg_right");
        wingLeft = body.getChild("wing_left");
        wingRight = body.getChild("wing_right");
        neck = body.getChild("neck_base");
        head = neck.getChild("head_base");
        beakBottom = head.getChild("beak_bottom");
        tailTop = body.getChild("tail_top");
        tailBottomLeft = body.getChild("tail_bottom_left");
        tailBottomRight = body.getChild("tail_bottom_right");
    }

    public static LayerDefinition create() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        PartDefinition body = root.addOrReplaceChild("body_base", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0f, -1.5f, -4.0f, 2.0f, 2.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, 23.0f, 2.0f, -0.4363f, 0.0f, 0.0f));

        body.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(5, 26).addBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.25f, 0.5f, -1.0f, 0.1309f, 0.0f, 0.0f));
        body.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(5, 24).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-0.25f, 0.5f, -1.0f, 0.1309f, 0.0f, 0.0f));

        body.addOrReplaceChild("wing_left", CubeListBuilder.create().texOffs(9, 12).addBox(0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(1.0f, -1.5f, -3.0f, 0.0f, 0.0436f, 0.0f));
        body.addOrReplaceChild("wing_right", CubeListBuilder.create().texOffs(0, 12).addBox(0.0f, 0.0f, 0.0f, 0.0f, 2.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-1.0f, -1.5f, -3.0f, 0.0f, -0.0436f, 0.0f));

        body.addOrReplaceChild("tail_top", CubeListBuilder.create().texOffs(13, 7).addBox(-1.0f, 0.0f, 0.0f, 2.0f, 0.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -1.0f, -0.5f, 0.7854f, 0.0f, 0.0f));
        body.addOrReplaceChild("tail_bottom_left", CubeListBuilder.create().texOffs(0, 7).addBox(0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(-1.0f, -0.5f, 0.0f, 0.9163f, 0.0f, 0.0f));
        body.addOrReplaceChild("tail_bottom_right", CubeListBuilder.create().texOffs(13, 0).addBox(-2.0f, 0.0f, 0.0f, 2.0f, 0.0f, 4.0f, new CubeDeformation(0.0f)), PartPose.offsetAndRotation(1.0f, -0.48f, 0.0f, 0.9163f, 0.0f, 0.0f));

        PartDefinition neck = body.addOrReplaceChild("neck_base", CubeListBuilder.create().texOffs(9, 19).addBox(-1.0f, -2.0f, -1.0f, 2.0f, 2.0f, 1.0f, new CubeDeformation(0.002f)), PartPose.offsetAndRotation(0.0f, 0.5f, -4.0f, -1.1345f, 0.0f, 0.0f));
        PartDefinition head = neck.addOrReplaceChild("head_base", CubeListBuilder.create().texOffs(0, 19).addBox(-1.0f, -2.0f, -2.0f, 2.0f, 2.0f, 2.0f, new CubeDeformation(0.004f)), PartPose.offset(0.0f, 0.0f, -1.0f));

        head.addOrReplaceChild("beak_top", CubeListBuilder.create().texOffs(0, 24).addBox(-0.5f, 0.0f, -1.0f, 1.0f, 3.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, 0.0f, -1.0f));
        head.addOrReplaceChild("beak_bottom", CubeListBuilder.create().texOffs(16, 19).addBox(-0.5f, 0.0f, 0.0f, 1.0f, 3.0f, 1.0f, new CubeDeformation(0.0f)), PartPose.offset(0.0f, 0.0f, -1.0f));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(ToucanEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        body.getAllParts().forEach(ModelPart::resetPose);

        neck.xRot = (headPitch - 65.0f) * ModelAnimator.DEG_TO_RAD;
        head.zRot = headYaw * ModelAnimator.DEG_TO_RAD;

        float partialTicks = ageInTicks - entity.tickCount;
        float flightAnimation = entity.getFlightAnimation(partialTicks);
        float walkAnimation = 1.0f - flightAnimation;

        if (flightAnimation > 0.0f) {
            body.xRot += 25.0f * Mth.DEG_TO_RAD * flightAnimation;
            head.xRot -= 25.0f * Mth.DEG_TO_RAD * flightAnimation;

            tailTop.xRot -= 35.0f * Mth.DEG_TO_RAD * flightAnimation;
            tailBottomLeft.xRot -= 40.0f * Mth.DEG_TO_RAD * flightAnimation;
            tailBottomRight.xRot -= 40.0f * Mth.DEG_TO_RAD * flightAnimation;

            tailBottomLeft.yRot += 35.0f * Mth.DEG_TO_RAD * flightAnimation;
            tailBottomRight.yRot -= 35.0f * Mth.DEG_TO_RAD * flightAnimation;

            legLeft.xRot += 40.0f * Mth.DEG_TO_RAD * flightAnimation;
            legRight.xRot += 40.0f * Mth.DEG_TO_RAD * flightAnimation;

            wingLeft.xRot += 90.0f * Mth.DEG_TO_RAD * flightAnimation;
            wingRight.xRot += 90.0f * Mth.DEG_TO_RAD * flightAnimation;

            try (ModelAnimator.Cycle fly = ModelAnimator.cycle(ageInTicks * 0.15f, flightAnimation)) {
                body.y += fly.eval(1.0f, 0.2f, 0.06f, 0.0f);
                body.xRot += fly.eval(1.0f, -0.04f, 0.06f, -0.04f);

                tailTop.xRot += fly.eval(1.0f, 0.1f, -0.2f, 0.0f);
                tailBottomLeft.xRot += fly.eval(1.0f, 0.1f, -0.1f, 0.0f);
                tailBottomRight.xRot += fly.eval(1.0f, 0.1f, -0.1f, 0.0f);

                wingLeft.zRot += fly.eval(1.0f, 1.3f, -0.1f, 1.4f);
                wingRight.zRot += fly.eval(1.0f, -1.3f, -0.1f, -1.4f);
            }
        }

        if (walkAnimation > 0.0f) {
            try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing, limbSwingAmount * walkAnimation)) {
                legLeft.xRot += walk.eval(1.0f, 1.0f);
                legRight.xRot += walk.eval(1.0f, -1.0f);
            }
        }
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
