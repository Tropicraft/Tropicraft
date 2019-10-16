package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.entity.passive.FailgullEntity;

public class FailgullModel extends EntityModel<FailgullEntity> {

	RendererModel baseFootLeft;
	RendererModel baseFootRight;
	RendererModel lowerLeg1;
	RendererModel lowestBody;
	RendererModel lowerLeg11;
	RendererModel lowerBody1;
	RendererModel lowerBody2;
	RendererModel rightWing;
	RendererModel rightWing1;
	RendererModel neck;
	RendererModel newShape1;

	public FailgullModel() {
		baseFootLeft = new RendererModel(this, 0, 0);
		baseFootLeft.addBox(0F, 0F, 0F, 1, 0, 1);
		baseFootLeft.setRotationPoint(-1F, 23F, 0F);

		baseFootRight = new RendererModel(this, 0, 0);
		baseFootRight.addBox(0F, 0F, 0F, 1, 0, 1);
		baseFootRight.setRotationPoint(1F, 23F, 0F);

		lowerLeg1 = new RendererModel(this, 0, 0);
		lowerLeg1.addBox(0F, -1F, 0F, 1, 2, 0);
		lowerLeg1.setRotationPoint(1F, 22F, 1F);

		lowestBody = new RendererModel(this, 0, 0);
		lowestBody.addBox(0F, 0F, 0F, 3, 1, 4);
		lowestBody.setRotationPoint(-1F, 20F, 0F);

		lowerLeg11 = new RendererModel(this, 0, 0);
		lowerLeg11.addBox(0F, 0F, 0F, 1, 2, 0);
		lowerLeg11.setRotationPoint(-1F, 21F, 1F);

		lowerBody1 = new RendererModel(this, 0, 0);
		lowerBody1.addBox(0F, 0F, 0F, 3, 1, 8);
		lowerBody1.setRotationPoint(-1F, 19F, -1F);

		lowerBody2 = new RendererModel(this, 0, 0);
		lowerBody2.addBox(0F, 0F, 0F, 3, 1, 7);
		lowerBody2.setRotationPoint(-1F, 18F, -2F);

		rightWing = new RendererModel(this, 0, 0);
		rightWing.addBox(0F, 0F, 0F, 0, 2, 5);
		rightWing.setRotationPoint(-1F, 18F, 0F);
		rightWing.rotateAngleX = -0.06981F;
		rightWing.rotateAngleY = -0.06981F;

		rightWing1 = new RendererModel(this, 0, 0);
		rightWing1.addBox(0F, 0F, 0F, 0, 2, 5);
		rightWing1.setRotationPoint(2F, 18F, 0F);
		rightWing1.rotateAngleX = -0.06981F;
		rightWing1.rotateAngleY = 0.06981F;

		neck = new RendererModel(this, 0, 0);
		neck.addBox(0F, 0F, 0F, 3, 2, 2);
		neck.setRotationPoint(-1F, 16F, -3F);

		newShape1 = new RendererModel(this, 14, 0);
		newShape1.addBox(0F, 0F, 0F, 1, 1, 2);
		newShape1.setRotationPoint(0F, 17F, -5F);
	}

	@Override
    public void render(FailgullEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(entity, f, f1, f2, f3, f4, f5);
		baseFootLeft.render(f5);
		baseFootRight.render(f5);
		lowerLeg1.render(f5);
		lowestBody.render(f5);
		lowerLeg11.render(f5);
		lowerBody1.render(f5);
		lowerBody2.render(f5);
		rightWing.render(f5);
		rightWing1.render(f5);
		neck.render(f5);
		newShape1.render(f5);
	}

	@Override
	public void setRotationAngles(FailgullEntity e, float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(e, f, f1, f2, f3, f4, f5);
		lowerLeg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		lowerLeg11.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
		rightWing.rotateAngleZ = f2;
		rightWing1.rotateAngleZ = -f2;		//left wing
	}

}