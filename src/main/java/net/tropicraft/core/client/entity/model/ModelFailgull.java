package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelFailgull extends ModelBase {
	ModelRenderer BaseFootLeft;
	ModelRenderer BaseFootRight;
	ModelRenderer LowerLeg1;
	ModelRenderer LowestBody;
	ModelRenderer LowerLeg11;
	ModelRenderer LowerBody1;
	ModelRenderer LowerBody2;
	ModelRenderer RightWing;
	ModelRenderer RightWing1;
	ModelRenderer Neck;
	ModelRenderer New_Shape1;
	
	public ModelFailgull() {
		BaseFootLeft = new ModelRenderer(this, 0, 0);
		BaseFootLeft.addBox(0F, 0F, 0F, 1, 0, 1);
		BaseFootLeft.setRotationPoint(-1F, 23F, 0F);
		BaseFootLeft.rotateAngleX = 0F;
		BaseFootLeft.rotateAngleY = 0F;
		BaseFootLeft.rotateAngleZ = 0F;
		BaseFootLeft.mirror = false;
		BaseFootRight = new ModelRenderer(this, 0, 0);
		BaseFootRight.addBox(0F, 0F, 0F, 1, 0, 1);
		BaseFootRight.setRotationPoint(1F, 23F, 0F);
		BaseFootRight.rotateAngleX = 0F;
		BaseFootRight.rotateAngleY = 0F;
		BaseFootRight.rotateAngleZ = 0F;
		BaseFootRight.mirror = false;
		LowerLeg1 = new ModelRenderer(this, 0, 0);
		LowerLeg1.addBox(0F, -1F, 0F, 1, 2, 0);
		LowerLeg1.setRotationPoint(1F, 22F, 1F);
		LowerLeg1.rotateAngleX = 0F;
		LowerLeg1.rotateAngleY = 0F;
		LowerLeg1.rotateAngleZ = 0F;
		LowerLeg1.mirror = false;
		LowestBody = new ModelRenderer(this, 0, 0);
		LowestBody.addBox(0F, 0F, 0F, 3, 1, 4);
		LowestBody.setRotationPoint(-1F, 20F, 0F);
		LowestBody.rotateAngleX = 0F;
		LowestBody.rotateAngleY = 0F;
		LowestBody.rotateAngleZ = 0F;
		LowestBody.mirror = false;
		LowerLeg11 = new ModelRenderer(this, 0, 0);
		LowerLeg11.addBox(0F, 0F, 0F, 1, 2, 0);
		LowerLeg11.setRotationPoint(-1F, 21F, 1F);
		LowerLeg11.rotateAngleX = 0F;
		LowerLeg11.rotateAngleY = 0F;
		LowerLeg11.rotateAngleZ = 0F;
		LowerLeg11.mirror = false;
		LowerBody1 = new ModelRenderer(this, 0, 0);
		LowerBody1.addBox(0F, 0F, 0F, 3, 1, 8);
		LowerBody1.setRotationPoint(-1F, 19F, -1F);
		LowerBody1.rotateAngleX = 0F;
		LowerBody1.rotateAngleY = 0F;
		LowerBody1.rotateAngleZ = 0F;
		LowerBody1.mirror = false;
		LowerBody2 = new ModelRenderer(this, 0, 0);
		LowerBody2.addBox(0F, 0F, 0F, 3, 1, 7);
		LowerBody2.setRotationPoint(-1F, 18F, -2F);
		LowerBody2.rotateAngleX = 0F;
		LowerBody2.rotateAngleY = 0F;
		LowerBody2.rotateAngleZ = 0F;
		LowerBody2.mirror = false;
		RightWing = new ModelRenderer(this, 0, 0);
		RightWing.addBox(0F, 0F, 0F, 0, 2, 5);
		RightWing.setRotationPoint(-1F, 18F, 0F);
		RightWing.rotateAngleX = -0.06981F;
		RightWing.rotateAngleY = -0.06981F;
		RightWing.rotateAngleZ = 0F;
		RightWing.mirror = false;
		RightWing1 = new ModelRenderer(this, 0, 0);
		RightWing1.addBox(0F, 0F, 0F, 0, 2, 5);
		RightWing1.setRotationPoint(2F, 18F, 0F);
		RightWing1.rotateAngleX = -0.06981F;
		RightWing1.rotateAngleY = 0.06981F;
		RightWing1.rotateAngleZ = 0F;
		RightWing1.mirror = false;
		Neck = new ModelRenderer(this, 0, 0);
		Neck.addBox(0F, 0F, 0F, 3, 2, 2);
		Neck.setRotationPoint(-1F, 16F, -3F);
		Neck.rotateAngleX = 0F;
		Neck.rotateAngleY = 0F;
		Neck.rotateAngleZ = 0F;
		Neck.mirror = false;
		New_Shape1 = new ModelRenderer(this, 14, 0);
		New_Shape1.addBox(0F, 0F, 0F, 1, 1, 2);
		New_Shape1.setRotationPoint(0F, 17F, -5F);
		New_Shape1.rotateAngleX = 0F;
		New_Shape1.rotateAngleY = 0F;
		New_Shape1.rotateAngleZ = 0F;
		New_Shape1.mirror = false;
	}

	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		BaseFootLeft.render(f5);
		BaseFootRight.render(f5);
		LowerLeg1.render(f5);
		LowestBody.render(f5);
		LowerLeg11.render(f5);
		LowerBody1.render(f5);
		LowerBody2.render(f5);
		RightWing.render(f5);
		RightWing1.render(f5);
		Neck.render(f5);
		New_Shape1.render(f5);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
		LowerLeg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		LowerLeg11.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
		RightWing.rotateAngleZ = f2;
		RightWing1.rotateAngleZ = -f2;		//left wing
	}

}