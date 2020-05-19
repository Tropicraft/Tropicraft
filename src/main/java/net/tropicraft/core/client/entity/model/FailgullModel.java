package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.entity.passive.FailgullEntity;

public class FailgullModel extends SegmentedModel<FailgullEntity> {
	final ModelRenderer baseFootLeft;
	final ModelRenderer baseFootRight;
	final ModelRenderer lowerLeg1;
	final ModelRenderer lowestBody;
	final ModelRenderer lowerLeg11;
	final ModelRenderer lowerBody1;
	final ModelRenderer lowerBody2;
	final ModelRenderer rightWing;
	final ModelRenderer leftWing;
	final ModelRenderer neck;
	final ModelRenderer newShape1;

	public FailgullModel() {
		baseFootLeft = new ModelRenderer(this, 0, 0);
		baseFootLeft.addBox(0F, 0F, 0F, 1, 0, 1);
		baseFootLeft.setRotationPoint(-1F, 23F, 0F);

		baseFootRight = new ModelRenderer(this, 0, 0);
		baseFootRight.addBox(0F, 0F, 0F, 1, 0, 1);
		baseFootRight.setRotationPoint(1F, 23F, 0F);

		lowerLeg1 = new ModelRenderer(this, 0, 0);
		lowerLeg1.addBox(0F, -1F, 0F, 1, 2, 0);
		lowerLeg1.setRotationPoint(1F, 22F, 1F);

		lowestBody = new ModelRenderer(this, 0, 0);
		lowestBody.addBox(0F, 0F, 0F, 3, 1, 4);
		lowestBody.setRotationPoint(-1F, 20F, 0F);

		lowerLeg11 = new ModelRenderer(this, 0, 0);
		lowerLeg11.addBox(0F, 0F, 0F, 1, 2, 0);
		lowerLeg11.setRotationPoint(-1F, 21F, 1F);

		lowerBody1 = new ModelRenderer(this, 0, 0);
		lowerBody1.addBox(0F, 0F, 0F, 3, 1, 8);
		lowerBody1.setRotationPoint(-1F, 19F, -1F);

		lowerBody2 = new ModelRenderer(this, 0, 0);
		lowerBody2.addBox(0F, 0F, 0F, 3, 1, 7);
		lowerBody2.setRotationPoint(-1F, 18F, -2F);

		rightWing = new ModelRenderer(this, 0, 0);
		rightWing.addBox(0F, 0F, 0F, 0, 2, 5);
		rightWing.setRotationPoint(-1F, 18F, 0F);
		rightWing.rotateAngleX = -0.06981F;
		rightWing.rotateAngleY = -0.06981F;

		leftWing = new ModelRenderer(this, 0, 0);
		leftWing.addBox(0F, 0F, 0F, 0, 2, 5);
		leftWing.setRotationPoint(2F, 18F, 0F);
		leftWing.rotateAngleX = -0.06981F;
		leftWing.rotateAngleY = 0.06981F;

		neck = new ModelRenderer(this, 0, 0);
		neck.addBox(0F, 0F, 0F, 3, 2, 2);
		neck.setRotationPoint(-1F, 16F, -3F);

		newShape1 = new ModelRenderer(this, 14, 0);
		newShape1.addBox(0F, 0F, 0F, 1, 1, 2);
		newShape1.setRotationPoint(0F, 17F, -5F);
	}

	@Override
	public void setRotationAngles(FailgullEntity failgull, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		lowerLeg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		lowerLeg11.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + 3.141593F) * 1.4F * limbSwingAmount;
		rightWing.rotateAngleZ = ageInTicks;
		leftWing.rotateAngleZ = -ageInTicks;		//left wing
	}

	@Override
	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(baseFootLeft, baseFootRight, lowerLeg1, lowerLeg11, rightWing, leftWing, neck, newShape1, lowestBody, lowerBody1, lowerBody2);
	}
}
