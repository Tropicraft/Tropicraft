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
import net.tropicraft.core.common.entity.passive.SmallBirdEntity;

public class PapyrusGonolekModel extends HierarchicalModel<SmallBirdEntity> {
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart beak;
    private final ModelPart butt;
    private final ModelPart legRight;
    private final ModelPart legLeft;
    private final ModelPart tail1;
    private final ModelPart tail2;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;

    public PapyrusGonolekModel(ModelPart root) {
        body = root.getChild("body_main");
        head = body.getChild("head");
        beak = head.getChild("cute_lil_beak");
        butt = body.getChild("gonolek_butt");
        tail1 = butt.getChild("tail1");
        tail2 = butt.getChild("tail2");
        legRight = body.getChild("leg_right");
        legLeft = body.getChild("leg_left");
        wingLeft = body.getChild("wing_left");
        wingRight = body.getChild("wing_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();

        PartDefinition body = mesh.getRoot().addOrReplaceChild("body_main", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 20.75F, 1.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(6, 12).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -1.0F, -3.0F, 0.4363F, 0.0F, 0.0F));
        head.addOrReplaceChild("cute_lil_beak", CubeListBuilder.create().texOffs(5, 7).addBox(-0.5F, 0.0F, -1.5F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.25F, -2.0F, 0.3054F, 0.0F, 0.0F));

        PartDefinition butt = body.addOrReplaceChild("gonolek_butt", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, -1.0F, 1.0F, -0.48F, 0.0F, 0.0F));

        butt.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(-3, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6981F, 0.0F, 0.0F));
        butt.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(6, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 1.0036F, 0.0F, 0.0F));

        body.addOrReplaceChild("wing_left", CubeListBuilder.create().texOffs(8, 7).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, -2.5F, 0.1745F, 0.0873F, -0.1745F));
        body.addOrReplaceChild("wing_right", CubeListBuilder.create().texOffs(0, 7).addBox(-0.5F, -0.5F, 0.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, -2.5F, 0.1745F, -0.0873F, 0.1745F));

        body.addOrReplaceChild("leg_left", CubeListBuilder.create().texOffs(10, 2).addBox(-0.5F, -0.25F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6F, 2.0F, 0.5F, -0.3491F, -0.3927F, -0.1745F));
        body.addOrReplaceChild("leg_right", CubeListBuilder.create().texOffs(8, 2).addBox(-0.5F, -0.25F, 0.0F, 1.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.6F, 2.0F, 0.5F, -0.3491F, 0.3927F, 0.1745F));

        return LayerDefinition.create(mesh, 16, 16);
    }

    @Override
    public void setupAnim(SmallBirdEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        body.getAllParts().forEach(ModelPart::resetPose);

        head.xRot += headPitch * ModelAnimator.DEG_TO_RAD;
        head.yRot += headYaw * ModelAnimator.DEG_TO_RAD;

        float partialTicks = ageInTicks - entity.tickCount;
        float flightAnimation = entity.getFlightAnimation(partialTicks);
        float groundAnimation = 1.0f - flightAnimation;

        if (flightAnimation > 0.0f) {
            legLeft.xRot += 70.0f * Mth.DEG_TO_RAD * flightAnimation;
            legRight.xRot += 70.0f * Mth.DEG_TO_RAD * flightAnimation;

            wingLeft.xRot += 90.0f * Mth.DEG_TO_RAD * flightAnimation;
            wingRight.xRot += 90.0f * Mth.DEG_TO_RAD * flightAnimation;

            try (ModelAnimator.Cycle fly = ModelAnimator.cycle(ageInTicks * 0.3f, flightAnimation)) {
                body.y += fly.eval(1.0f, 0.2f, -0.06f, 0.0f);
                body.xRot += fly.eval(0.5f, -0.1f, -0.06f, 0.2f);
            	head.xRot += fly.eval(0.5f, 0.1f, -0.1f, 0.0f);

				wingLeft.zRot += fly.eval(1.0f, 1.3f, -0.1f, 1.4f);
				wingRight.zRot += fly.eval(1.0f, -1.3f, -0.1f, -1.4f);
			}
		}

		if (groundAnimation > 0.0f) {
			try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.5f, limbSwingAmount * groundAnimation)) {
				legLeft.xRot += walk.eval(1.0f, 1.0f, 0.0f, 0.5f);
				legRight.xRot += walk.eval(1.0f, 1.0f, 0.0f, 0.5f);

				body.y += walk.eval(1.0f, 1.2f, -0.06f, 0.0f);

				wingLeft.yRot += walk.eval(2.0f, 0.5f, 0.0f, 1.0f);
				wingRight.yRot += walk.eval(2.0f, -0.5f, 0.0f, -1.0f);
			}

			try (ModelAnimator.Cycle idle = ModelAnimator.cycle(ageInTicks, 1.0f)) {
				float wingTwitch = idle.twitchSymmetric(12.0f, 0.22f, 0.5f);
				wingLeft.yRot += wingTwitch;
				wingLeft.xRot += wingTwitch * 0.5f;
				wingRight.yRot -= wingTwitch;
				wingRight.xRot += wingTwitch * 0.5f;

				head.zRot += idle.twitchSymmetric(9.0f, 0.2f, 0.15f);
			}
		}
	}

	@Override
	public ModelPart root() {
		return body;
	}
}
