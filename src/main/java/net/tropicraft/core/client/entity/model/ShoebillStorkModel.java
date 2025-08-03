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
import net.tropicraft.core.common.entity.IkWalker;
import net.tropicraft.core.common.entity.passive.ShoebillStorkEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ShoebillStorkModel extends HierarchicalModel<ShoebillStorkEntity> {
    private final ModelPart body;
    private final ModelPart wingLeft1a;
    private final ModelPart wingLeft1b;
    private final ModelPart wingLeft1c;
    private final ModelPart wingLeftTip;
    private final ModelPart buttocks;
    private final ModelPart tailTop;
    private final ModelPart tailBottom;
    private final ModelPart tailMidLeft;
    private final ModelPart tailMidRight;
    private final ModelPart neckBase;
    private final ModelPart neckTop;
    private final ModelPart head;
    private final ModelPart beakTop;
    private final ModelPart beakBottom;
    private final ModelPart fancyLad;
    private final ModelPart legLeft1a;
    private final ModelPart legLeft1b;
    private final ModelPart clawLeft;
    private final ModelPart legRight1a;
    private final ModelPart legRight1b;
    private final ModelPart clawRight;
    private final ModelPart wingRight1a;
    private final ModelPart wingRight1b;
    private final ModelPart wingRight1c;
    private final ModelPart wingRightTip;

    private final ModelPart[] neckChain;

    private final TwoJointSolver leftLegSolver;
    private final TwoJointSolver rightLegSolver;
    private final TwoJointSolver headSolver;

    public ShoebillStorkModel(ModelPart root) {
        body = root.getChild("body_main");
        wingLeft1a = body.getChild("wing_left1a");
        wingLeft1b = wingLeft1a.getChild("wing_left1b");
        wingLeft1c = wingLeft1b.getChild("wing_left1c");
        wingLeftTip = wingLeft1c.getChild("wing_left_tip");
        buttocks = body.getChild("bird_buttocks");
        tailTop = buttocks.getChild("tail_top");
        tailBottom = buttocks.getChild("tail_bottom");
        tailMidLeft = buttocks.getChild("tail_mid_left");
        tailMidRight = buttocks.getChild("tail_mid_right");
        neckBase = body.getChild("neck_base");
        neckTop = neckBase.getChild("neck_top");
        head = neckTop.getChild("head_main");
        beakTop = head.getChild("beak_top");
        beakBottom = head.getChild("beak_bottom");
        fancyLad = head.getChild("fancy_lad");
        legLeft1a = body.getChild("leg_left1a");
        legLeft1b = legLeft1a.getChild("leg_left1b");
        clawLeft = legLeft1b.getChild("claw_left");
        legRight1a = body.getChild("leg_right1a");
        legRight1b = legRight1a.getChild("leg_right1b");
        clawRight = legRight1b.getChild("claw_right");
        wingRight1a = body.getChild("wing_right1a");
        wingRight1b = wingRight1a.getChild("wing_right1b");
        wingRight1c = wingRight1b.getChild("wing_right1c");
        wingRightTip = wingRight1c.getChild("wing_right_tip");

        neckChain = new ModelPart[]{body, neckBase, neckTop};

        leftLegSolver = new TwoJointSolver(new ModelPart[]{body}, legLeft1a, legLeft1b, clawLeft);
        rightLegSolver = new TwoJointSolver(new ModelPart[]{body}, legRight1a, legRight1b, clawRight);
        headSolver = new TwoJointSolver(new ModelPart[]{body}, neckBase, neckTop, head);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition body = mesh.getRoot().addOrReplaceChild("body_main", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -4.0F, -8.0F, 5.0F, 4.0F, 10.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 12.9F, 1.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition wingLeft1a = body.addOrReplaceChild("wing_left1a", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, -2.4F, -5.45F, 0.0F, -0.48F, 0.0873F));
        wingLeft1a.addOrReplaceChild("wing_left1a_r1", CubeListBuilder.create().texOffs(0, 34).addBox(0.0F, -0.9F, -1.0F, 1.0F, 4.0F, 3.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -0.6F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition wingLeft1b = wingLeft1a.addOrReplaceChild("wing_left1b", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, -0.6F, 2.0F, 0.0F, -2.5744F, 0.0F));
        wingLeft1b.addOrReplaceChild("wing_left1b_r1", CubeListBuilder.create().texOffs(8, 33).addBox(-1.0F, -0.9F, -1.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition wingLeft1c = wingLeft1b.addOrReplaceChild("wing_left1c", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, 0.0F, 4.0F, 0.0F, 2.9671F, 0.0F));
        wingLeft1c.addOrReplaceChild("wing_left1c_r1", CubeListBuilder.create().texOffs(18, 29).addBox(0.0F, -0.9F, -1.0F, 1.0F, 4.0F, 8.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition wingLeftTip = wingLeft1c.addOrReplaceChild("wing_left_tip", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 8.0F));
        wingLeftTip.addOrReplaceChild("wing_left_tip_r1", CubeListBuilder.create().texOffs(36, 36).addBox(0.0F, -0.9F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition buttocks = body.addOrReplaceChild("bird_buttocks", CubeListBuilder.create().texOffs(30, 8).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 3.0F, 2.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -4.0F, 2.0F, -0.6109F, 0.0F, 0.0F));
        buttocks.addOrReplaceChild("tail_top", CubeListBuilder.create().texOffs(12, 0).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 8.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3491F, 0.0F, 0.0F));
        buttocks.addOrReplaceChild("tail_bottom", CubeListBuilder.create().texOffs(30, 0).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 0.0F, 6.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 1.5F, 2.0F, 0.6545F, 0.0F, 0.0F));
        buttocks.addOrReplaceChild("tail_mid_left", CubeListBuilder.create().texOffs(22, 0).addBox(-2.5F, 0.0F, 0.0F, 4.0F, 0.0F, 6.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.5F, 0.0F, 2.0F, 0.4363F, 0.0F, 0.0F));
        buttocks.addOrReplaceChild("tail_mid_right", CubeListBuilder.create().texOffs(22, 0).mirror().addBox(-1.5F, 0.0F, 0.0F, 4.0F, 0.0F, 6.0F, CubeDeformation.NONE).mirror(false), PartPose.offsetAndRotation(-0.5F, 0.05F, 2.0F, 0.4363F, 0.0F, 0.0F));

        PartDefinition neckBase = body.addOrReplaceChild("neck_base", CubeListBuilder.create().texOffs(0, 14).addBox(-1.5F, -3.0F, -4.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.005F)), PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, -0.6545F, 0.0F, 0.0F));
        PartDefinition neckTop = neckBase.addOrReplaceChild("neck_top", CubeListBuilder.create().texOffs(0, 21).addBox(-1.5F, -2.0F, -4.0F, 3.0F, 2.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.829F, 0.0F, 0.0F));

        PartDefinition head = neckTop.addOrReplaceChild("head_main", CubeListBuilder.create().texOffs(14, 14).addBox(-2.0F, -1.0F, -1.8F, 4.0F, 4.0F, 3.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -0.5F, -4.0F, 0.9163F, 0.0F, 0.0F));
		head.addOrReplaceChild("beak_top", CubeListBuilder.create().texOffs(14, 21).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 3.0F, -1.8F, 0.5672F, 0.0F, 0.0F));
		head.addOrReplaceChild("beak_bottom", CubeListBuilder.create().texOffs(16, 29).addBox(-1.5F, -0.25F, 0.0F, 3.0F, 4.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 3.0F, 0.2F, 0.4363F, 0.0F, 0.0F));
		head.addOrReplaceChild("fancy_lad", CubeListBuilder.create().texOffs(0, 4).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 2.0F, 0.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -1.0F, -1.8F, 0.3927F, 0.0F, 0.0F));

		PartDefinition legLeft1a = body.addOrReplaceChild("leg_left1a", CubeListBuilder.create().texOffs(52, 0).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 5.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(1.5F, 0.0F, -1.0F, 0.8727F, -0.0436F, -0.0873F));
		PartDefinition legLeft1b = legLeft1a.addOrReplaceChild("leg_left1b", CubeListBuilder.create().texOffs(53, 6).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 0.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 4.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		legLeft1b.addOrReplaceChild("claw_left", CubeListBuilder.create().texOffs(48, 14).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 0.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.1745F, -0.1745F, 0.0F));

		PartDefinition legRight1a = body.addOrReplaceChild("leg_right1a", CubeListBuilder.create().texOffs(44, 0).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 5.0F, 1.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(-1.5F, 0.0F, -1.0F, 0.8727F, 0.0436F, 0.0873F));
		PartDefinition legRight1b = legRight1a.addOrReplaceChild("leg_right1b", CubeListBuilder.create().texOffs(45, 6).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 8.0F, 0.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 4.0F, 1.0F, -0.2618F, 0.0F, 0.0F));
		legRight1b.addOrReplaceChild("claw_right", CubeListBuilder.create().texOffs(40, 14).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 0.0F, 4.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.1745F, 0.1745F, 0.0F));

		PartDefinition wingRight1a = body.addOrReplaceChild("wing_right1a", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, -2.4F, -5.45F, 0.0F, 0.48F, -0.0873F));
		wingRight1a.addOrReplaceChild("wing_right1a_r1", CubeListBuilder.create().texOffs(0, 46).addBox(-1.0F, -0.9F, -1.0F, 1.0F, 4.0F, 3.0F, CubeDeformation.NONE), PartPose.offsetAndRotation(0.0F, -0.6F, 0.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition wingRight1b = wingRight1a.addOrReplaceChild("wing_right1b", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, -0.6F, 2.0F, 0.0F, 2.5744F, 0.0F));
		wingRight1b.addOrReplaceChild("wing_right1b_r1", CubeListBuilder.create().texOffs(8, 45).addBox(0.0F, -0.9F, -1.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition wingRight1c = wingRight1b.addOrReplaceChild("wing_right1c", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 0.0F, 4.0F, 0.0F, -2.9671F, 0.0F));
		wingRight1c.addOrReplaceChild("wing_right1c_r1", CubeListBuilder.create().texOffs(18, 41).addBox(-1.0F, -0.9F, -1.0F, 1.0F, 4.0F, 8.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));

		PartDefinition wingRightTip = wingRight1c.addOrReplaceChild("wing_right_tip", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 8.0F));
		wingRightTip.addOrReplaceChild("wing_right_tip_r1", CubeListBuilder.create().texOffs(36, 48).addBox(-1.0F, -0.9F, -1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.02F)), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public ModelPart root() {
		return body;
	}

	@Override
	public void setupAnim(ShoebillStorkEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
		body.getAllParts().forEach(ModelPart::resetPose);

		float partialTicks = ageInTicks - entity.tickCount;
		float flightAnimation = entity.getFlightAnimation(partialTicks);
		float groundAnimation = 1.0f - flightAnimation;

		float headRoll = 0.0f;

		if (groundAnimation > 0.0f) {
			try (ModelAnimator.Cycle idle = ModelAnimator.cycle(ageInTicks, 1.0f)) {
				headRoll += idle.periodic(400.0f, 5.0f, 20.0f, 20.0f);
				headRoll += idle.periodic(500.0f, 5.0f, 20.0f, -20.0f);

				body.y += idle.eval(0.01f, 0.075f);

				float wingTwitch = idle.twitchAsymmetric(100.0f, 0.1f, 0.5f);
				wingLeft1a.yRot += wingTwitch;
				wingRight1a.yRot -= wingTwitch;
			}

			IkWalker.EntitySpace entitySpace = IkWalker.EntitySpace.from(entity, partialTicks);
			Vector3f leftFootPos = entity.leftFoot().solveModelPosition(entitySpace, partialTicks);
			Vector3f rightFootPos = entity.rightFoot().solveModelPosition(entitySpace, partialTicks);

			float footDeltaX = (leftFootPos.x + rightFootPos.x) / 2.0f;
			float footDeltaZ = (leftFootPos.z + rightFootPos.z) / 2.0f - ShoebillStorkEntity.BASE_FOOT_Z;
			body.xRot += footDeltaZ * 0.6f * groundAnimation;
			body.zRot += footDeltaX * 0.6f * groundAnimation;

			leftLegSolver.apply(leftFootPos, groundAnimation);
			rightLegSolver.apply(rightFootPos, groundAnimation);
		}

		// Try to keep the head stable in the same position - very Shoebill-like
		headSolver.applyRelativeToBase(0.35f, 0.0f, 0.0f, 0.0f);

		Quaternionf neckRotation = new Quaternionf().rotationZYX(
				0.0f,
				headYaw * Mth.DEG_TO_RAD * 0.75f,
				headPitch * Mth.DEG_TO_RAD * 0.75f
		);
		Quaternionf headRotation = new Quaternionf().rotationZYX(
				headRoll * Mth.DEG_TO_RAD,
				headYaw * Mth.DEG_TO_RAD * 0.25f,
				headPitch * Mth.DEG_TO_RAD * 0.25f
		);
		ModelAnimator.rotateByInModelSpace(new ModelPart[]{body}, neckBase, neckRotation);
		ModelAnimator.rotateByInModelSpace(neckChain, head, headRotation);
	}
}
