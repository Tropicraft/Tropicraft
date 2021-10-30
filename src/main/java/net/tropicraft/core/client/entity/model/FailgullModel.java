package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.passive.FailgullEntity;

public class FailgullModel extends ListModel<FailgullEntity> {
	final ModelPart baseFootLeft;
	final ModelPart baseFootRight;
	final ModelPart lowerLeg1;
	final ModelPart lowestBody;
	final ModelPart lowerLeg11;
	final ModelPart lowerBody1;
	final ModelPart lowerBody2;
	final ModelPart rightWing;
	final ModelPart leftWing;
	final ModelPart neck;
	final ModelPart newShape1;

	public FailgullModel() {
		baseFootLeft = new ModelPart(this, 0, 0);
		baseFootLeft.addBox(0F, 0F, 0F, 1, 0, 1);
		baseFootLeft.setPos(-1F, 23F, 0F);

		baseFootRight = new ModelPart(this, 0, 0);
		baseFootRight.addBox(0F, 0F, 0F, 1, 0, 1);
		baseFootRight.setPos(1F, 23F, 0F);

		lowerLeg1 = new ModelPart(this, 0, 0);
		lowerLeg1.addBox(0F, -1F, 0F, 1, 2, 0);
		lowerLeg1.setPos(1F, 22F, 1F);

		lowestBody = new ModelPart(this, 0, 0);
		lowestBody.addBox(0F, 0F, 0F, 3, 1, 4);
		lowestBody.setPos(-1F, 20F, 0F);

		lowerLeg11 = new ModelPart(this, 0, 0);
		lowerLeg11.addBox(0F, 0F, 0F, 1, 2, 0);
		lowerLeg11.setPos(-1F, 21F, 1F);

		lowerBody1 = new ModelPart(this, 0, 0);
		lowerBody1.addBox(0F, 0F, 0F, 3, 1, 8);
		lowerBody1.setPos(-1F, 19F, -1F);

		lowerBody2 = new ModelPart(this, 0, 0);
		lowerBody2.addBox(0F, 0F, 0F, 3, 1, 7);
		lowerBody2.setPos(-1F, 18F, -2F);

		rightWing = new ModelPart(this, 0, 0);
		rightWing.addBox(0F, 0F, 0F, 0, 2, 5);
		rightWing.setPos(-1F, 18F, 0F);
		rightWing.xRot = -0.06981F;
		rightWing.yRot = -0.06981F;

		leftWing = new ModelPart(this, 0, 0);
		leftWing.addBox(0F, 0F, 0F, 0, 2, 5);
		leftWing.setPos(2F, 18F, 0F);
		leftWing.xRot = -0.06981F;
		leftWing.yRot = 0.06981F;

		neck = new ModelPart(this, 0, 0);
		neck.addBox(0F, 0F, 0F, 3, 2, 2);
		neck.setPos(-1F, 16F, -3F);

		newShape1 = new ModelPart(this, 14, 0);
		newShape1.addBox(0F, 0F, 0F, 1, 1, 2);
		newShape1.setPos(0F, 17F, -5F);
	}

	@Override
	public void setupAnim(FailgullEntity failgull, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		lowerLeg1.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		lowerLeg11.xRot = Mth.cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount;
		rightWing.zRot = ageInTicks;
		leftWing.zRot = -ageInTicks;		//left wing
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(baseFootLeft, baseFootRight, lowerLeg1, lowerLeg11, rightWing, leftWing, neck, newShape1, lowestBody, lowerBody1, lowerBody2);
	}
}
