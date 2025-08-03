package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.client.entity.render.state.BirdRenderState;

public class WhiteCollaredOlivebackModel extends EntityModel<BirdRenderState> {
    private final ModelPart body;
    private final ModelPart butt;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart head;
    private final ModelPart beak;
    private final ModelPart legLeft;
    private final ModelPart legRight;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;

    public WhiteCollaredOlivebackModel(ModelPart root) {
        super(root);
        body = root.getChild("body_base");
        butt = body.getChild("birb_butt");
        tail2 = butt.getChild("tail2");
        tail1 = butt.getChild("tail1");
        head = body.getChild("lil_head");
        beak = head.getChild("Beak");
        legLeft = body.getChild("leg_left");
        wingLeft = body.getChild("wing_left");
        legRight = body.getChild("leg_right");
        wingRight = body.getChild("wing_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition body = mesh.getRoot().addOrReplaceChild("body_base", CubeListBuilder.create().texOffs(0, 0).addBox(-1.005F, -3.0F, -2.0F, 2.01F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 22.25F, -0.5F, -0.6545F, 0.0F, 0.0F));

        PartDefinition butt = body.addOrReplaceChild("birb_butt", CubeListBuilder.create().texOffs(7, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 1.0F, -0.3927F, 0.0F, 0.0F));

        butt.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(9, 14).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7418F, 0.0F, 0.0F));
        butt.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(8, 11).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.1345F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("lil_head", CubeListBuilder.create().texOffs(0, 11).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, -2.0F, 0.6545F, 0.0F, 0.0F));
        head.addOrReplaceChild("Beak", CubeListBuilder.create().texOffs(12, 3).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.75F, -2.0F, 0.6109F, 0.0F, 0.0F));

        body.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(4, 7).addBox(0.0F, 0.25F, 0.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, 0.75F, -1.2654F, -0.5236F, -0.4363F));
        body.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(6, 7).addBox(-1.0F, 0.25F, 0.0F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, 0.75F, -1.2654F, 0.5236F, 0.4363F));

        body.addOrReplaceChild("wing_left", CubeListBuilder.create().texOffs(0, 6).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.0F, -1.75F, 0.3491F, 0.0873F, -0.3054F));
        body.addOrReplaceChild("wing_right", CubeListBuilder.create().texOffs(8, 6).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, -1.75F, 0.3491F, -0.0873F, 0.3054F));

        return LayerDefinition.create(mesh, 16, 16);
    }

    @Override
    public void setupAnim(BirdRenderState state) {
        body.getAllParts().forEach(ModelPart::resetPose);

        head.xRot += state.xRot * ModelAnimator.DEG_TO_RAD;
        head.yRot += state.yRot * ModelAnimator.DEG_TO_RAD;

        float flightAnimation = state.flightAnimation;
        float groundAnimation = 1.0f - flightAnimation;

        if (flightAnimation > 0.0f) {
            legLeft.xRot += 70.0f * Mth.DEG_TO_RAD * flightAnimation;
            legRight.xRot += 70.0f * Mth.DEG_TO_RAD * flightAnimation;

            wingLeft.xRot += 90.0f * Mth.DEG_TO_RAD * flightAnimation;
            wingRight.xRot += 90.0f * Mth.DEG_TO_RAD * flightAnimation;

            try (ModelAnimator.Cycle fly = ModelAnimator.cycle(state.ageInTicks * 0.3f, flightAnimation)) {
                body.y += fly.eval(1.0f, 0.2f, -0.06f, 0.0f);
                body.xRot += fly.eval(0.5f, -0.1f, -0.06f, 0.2f);
                head.xRot += fly.eval(0.5f, 0.1f, -0.1f, 0.0f);

                wingLeft.zRot += fly.eval(1.0f, 1.3f, -0.1f, 1.4f);
                wingRight.zRot += fly.eval(1.0f, -1.3f, -0.1f, -1.4f);
            }
        }

        if (groundAnimation > 0.0f) {
            try (ModelAnimator.Cycle walk = ModelAnimator.cycle(state.walkAnimationPos * 0.5f, state.walkAnimationSpeed * groundAnimation)) {
                legLeft.xRot += walk.eval(1.0f, 1.0f, 0.0f, 0.5f);
                legRight.xRot += walk.eval(1.0f, 1.0f, 0.0f, 0.5f);

                body.y += walk.eval(1.0f, 1.2f, -0.06f, 0.0f);

                wingLeft.yRot += walk.eval(2.0f, 0.5f, 0.0f, 1.0f);
                wingRight.yRot += walk.eval(2.0f, -0.5f, 0.0f, -1.0f);
            }

            try (ModelAnimator.Cycle idle = ModelAnimator.cycle(state.ageInTicks, 1.0f)) {
                float wingTwitch = idle.twitchSymmetric(12.0f, 0.22f, 0.5f);
                wingLeft.yRot += wingTwitch;
                wingLeft.xRot += wingTwitch * 0.5f;
                wingRight.yRot -= wingTwitch;
                wingRight.xRot += wingTwitch * 0.5f;

                head.zRot += idle.twitchSymmetric(9.0f, 0.2f, 0.15f);
            }
        }
    }
}
