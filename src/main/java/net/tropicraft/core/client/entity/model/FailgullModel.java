package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.passive.FailgullEntity;

public class FailgullModel extends ListModel<FailgullEntity> {
	final ModelPart baseFootLeft;
	final ModelPart baseFootRight;
	final ModelPart lowerLeg1;
	final ModelPart lowestBody;
	final ModelPart lowerLeg2;
	final ModelPart lowerBody1;
	final ModelPart lowerBody2;
	final ModelPart rightWing;
	final ModelPart leftWing;
	final ModelPart neck;
	final ModelPart beak;

	public FailgullModel(ModelPart root) {
		baseFootLeft = root.getChild("baseFootLeft");
		baseFootRight = root.getChild("baseFootRight");
		lowerLeg1 = root.getChild("lowerLeg1");
		lowestBody = root.getChild("lowestBody");
		lowerLeg2 = root.getChild("lowerLeg2");
		lowerBody1 = root.getChild("lowerBody1");
		lowerBody2 = root.getChild("lowerBody2");
		rightWing = root.getChild("rightWing");
		leftWing = root.getChild("leftWing");
		neck = root.getChild("neck");
		beak = root.getChild("beak");

		/*
		baseFootLeft = new ModelPart(this, 0, 0);
		baseFootLeft.addCuboid(0F, 0F, 0F, 1, 0, 1);
		baseFootLeft.setPivot(-1F, 23F, 0F);

		baseFootRight = new ModelPart(this, 0, 0);
		baseFootRight.addCuboid(0F, 0F, 0F, 1, 0, 1);
		baseFootRight.setPivot(1F, 23F, 0F);

		lowerLeg1 = new ModelPart(this, 0, 0);
		lowerLeg1.addCuboid(0F, -1F, 0F, 1, 2, 0);
		lowerLeg1.setPivot(1F, 22F, 1F);

		lowestBody = new ModelPart(this, 0, 0);
		lowestBody.addCuboid(0F, 0F, 0F, 3, 1, 4);
		lowestBody.setPivot(-1F, 20F, 0F);

		lowerLeg2 = new ModelPart(this, 0, 0);
		lowerLeg2.addCuboid(0F, 0F, 0F, 1, 2, 0);
		lowerLeg2.setPivot(-1F, 21F, 1F);

		lowerBody1 = new ModelPart(this, 0, 0);
		lowerBody1.addCuboid(0F, 0F, 0F, 3, 1, 8);
		lowerBody1.setPivot(-1F, 19F, -1F);

		lowerBody2 = new ModelPart(this, 0, 0);
		lowerBody2.addCuboid(0F, 0F, 0F, 3, 1, 7);
		lowerBody2.setPivot(-1F, 18F, -2F);

		rightWing = new ModelPart(this, 0, 0);
		rightWing.addCuboid(0F, 0F, 0F, 0, 2, 5);
		rightWing.setPivot(-1F, 18F, 0F);
		rightWing.pitch = -0.06981F;
		rightWing.yaw = -0.06981F;

		leftWing = new ModelPart(this, 0, 0);
		leftWing.addCuboid(0F, 0F, 0F, 0, 2, 5);
		leftWing.setPivot(2F, 18F, 0F);
		leftWing.pitch = -0.06981F;
		leftWing.yaw = 0.06981F;

		neck = new ModelPart(this, 0, 0);
		neck.addCuboid(0F, 0F, 0F, 3, 2, 2);
		neck.setPivot(-1F, 16F, -3F);

		head = new ModelPart(this, 14, 0);
		head.addCuboid(0F, 0F, 0F, 1, 1, 2);
		head.setPivot(0F, 17F, -5F);

		 */
	}

	public static LayerDefinition create() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();

		modelPartData.addOrReplaceChild("baseFootLeft",
				CubeListBuilder.create()
						.texOffs(0,0)
						.addBox(0F, 0F, 0F, 1, 0, 1),
				PartPose.offset(-1F, 23F, 0F));

		modelPartData.addOrReplaceChild("baseFootRight",
				CubeListBuilder.create()
						.texOffs(0,0)
						.addBox(0F, 0F, 0F, 1, 0, 1),
				PartPose.offset(1F, 23F, 0F));

		modelPartData.addOrReplaceChild("lowerLeg1",
				CubeListBuilder.create()
						.texOffs(0,0)
						.addBox(0F, -1F, 0F, 1, 2, 0),
				PartPose.offset(1F, 22F, 1F));

		modelPartData.addOrReplaceChild("lowestBody",
				CubeListBuilder.create()
						.texOffs(0,0)
						.addBox(0F, 0F, 0F, 3, 1, 4),
				PartPose.offset(-1F, 20F, 0F));

		modelPartData.addOrReplaceChild("lowerLeg2",
				CubeListBuilder.create()
						.texOffs(0,0)
						.addBox(0F, 0F, 0F, 1, 2, 0),
				PartPose.offset(-1F, 21F, 1F));

		modelPartData.addOrReplaceChild("lowerBody1",
				CubeListBuilder.create()
						.texOffs(0,0)
						.addBox(0F, 0F, 0F, 3, 1, 8),
				PartPose.offset(-1F, 19F, -1F));

		modelPartData.addOrReplaceChild("lowerBody2",
				CubeListBuilder.create()
						.texOffs(0,0)
						.addBox(0F, 0F, 0F, 3, 1, 7),
				PartPose.offset(-1F, 18F, -2F));

		modelPartData.addOrReplaceChild("rightWing",
				CubeListBuilder.create()
						.texOffs(0,0)
						.addBox(0F, 0F, 0F, 0, 2, 5),
				PartPose.offsetAndRotation(-1F, 18F, 0F, -0.06981F,-0.06981F,0));

		modelPartData.addOrReplaceChild("leftWing",
				CubeListBuilder.create()
						.texOffs(0,0)
						.addBox(0F, 0F, 0F, 0, 2, 5),
				PartPose.offsetAndRotation(2F, 18F, 0F, -0.06981F,0.06981F,0));

		modelPartData.addOrReplaceChild("neck",
				CubeListBuilder.create()
						.texOffs(0,0)
						.addBox(0F, 0F, 0F, 3, 2, 2),
				PartPose.offset(-1F, 16F, -3F));

		modelPartData.addOrReplaceChild("beak",
				CubeListBuilder.create()
						.texOffs(14,0)
						.addBox(0F, 0F, 0F, 1, 1, 2),
				PartPose.offset(0F, 17F, -5F));

		return LayerDefinition.create(modelData, 64, 32);
	}

	@Override
	public void setupAnim(FailgullEntity failgull, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		lowerLeg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		lowerLeg2.xRot = Mth.cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount;
		rightWing.zRot = ageInTicks;
		leftWing.zRot = -ageInTicks;		//left wing
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(baseFootLeft, baseFootRight, lowerLeg1, lowerLeg2, rightWing, leftWing, neck, beak, lowestBody, lowerBody1, lowerBody2);
	}
}
