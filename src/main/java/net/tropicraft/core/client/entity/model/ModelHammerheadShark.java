package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelHammerheadShark extends ModelBase {
	ModelRenderer Head1;
	ModelRenderer Head3;
	ModelRenderer Body1Upper;
	ModelRenderer Body1Lower;
	ModelRenderer Body2Upper;
	ModelRenderer Body2Lower;
	ModelRenderer Body3UpperLeft;
	ModelRenderer Body3LowerLeft;
	ModelRenderer Body3LowerRight;
	ModelRenderer FinPectoralLeft;
	ModelRenderer FinPectoralRight;
	ModelRenderer FinDorsal;
	ModelRenderer FinPelvicLeft;
	ModelRenderer FinPelvicRight;
	ModelRenderer FinAdipose;
	ModelRenderer FinAnal;
	ModelRenderer FinCaudalUpper;
	ModelRenderer FinCaudalLower;
	ModelRenderer Body3Lower;
	ModelRenderer Body4Lower;
	ModelRenderer Head2;

	public ModelHammerheadShark() {
		textureWidth = 128;
		textureHeight = 64;

		Head1 = new ModelRenderer(this, 0, 24);
		Head1.addBox(-8F, -11.8F, -2.6F, 16, 6, 2);
		Head1.setRotationPoint(0F, 0.5F, -14F);
		Head1.setTextureSize(128, 64);
		Head1.mirror = true;
		setRotation(Head1, 1.527163F, 0F, 0F);
		Head3 = new ModelRenderer(this, 0, 46);
		Head3.addBox(-2.5F, -7F, -3.9F, 5, 14, 2);
		Head3.setRotationPoint(0F, 0.5F, -14F);
		Head3.setTextureSize(128, 64);
		Head3.mirror = true;
		setRotation(Head3, 1.48353F, 0F, 0F);
		Body1Upper = new ModelRenderer(this, 18, 0);
		Body1Upper.addBox(-2.5F, -17F, 0F, 5, 18, 6);
		Body1Upper.setRotationPoint(0F, 0F, 3F);
		Body1Upper.setTextureSize(128, 64);
		Body1Upper.mirror = true;
		setRotation(Body1Upper, 1.780236F, 0F, 0F);
		Body1Lower = new ModelRenderer(this, 28, 47);
		Body1Lower.addBox(-4F, -11F, -5F, 8, 12, 5);
		Body1Lower.setRotationPoint(0F, 0F, 3F);
		Body1Lower.setTextureSize(128, 64);
		Body1Lower.mirror = true;
		setRotation(Body1Lower, 1.570796F, 0F, 0F);
		
		Body2Upper = new ModelRenderer(this, 40, 0);
		Body2Upper.addBox(-2F, -0.8F, 0F, 4, 21, 6);
		Body2Upper.setRotationPoint(0F, 0F, 3F);
		Body2Upper.setTextureSize(128, 64);
		Body2Upper.mirror = true;
		setRotation(Body2Upper, 1.48353F, 0F, 0F);
		
		Body2Lower = new ModelRenderer(this, 52, 39);
		Body2Lower.addBox(-3F, 0F, -5F, 6, 20, 5);
		Body2Lower.setRotationPoint(0F, 0F, 3F);
		Body2Lower.setTextureSize(128, 64);
		Body2Lower.mirror = true;
		setRotation(Body2Lower, 1.623156F, 0F, 0F);
		
		Body3UpperLeft = new ModelRenderer(this, 60, 0);
		Body3UpperLeft.addBox(-1F, -0.3F, -1F, 2, 15, 5);
		Body3UpperLeft.setRotationPoint(0F, 0F, 22F);
		Body3UpperLeft.setTextureSize(128, 64);
		Body3UpperLeft.mirror = true;
		setRotation(Body3UpperLeft, 1.48353F, 0F, 0F);
		
		Body3LowerLeft = new ModelRenderer(this, 74, 45);
		Body3LowerLeft.addBox(0F, 0F, -4F, 2, 14, 5);
		Body3LowerLeft.setRotationPoint(0F, 0F, 22F);
		Body3LowerLeft.setTextureSize(128, 64);
		Body3LowerLeft.mirror = true;
		setRotation(Body3LowerLeft, 1.692969F, -0.0698132F, 0F);
		
		Body3LowerRight = new ModelRenderer(this, 74, 45);
		Body3LowerRight.mirror = true;
		Body3LowerRight.addBox(-2F, 0F, -4F, 2, 14, 5);
		Body3LowerRight.setRotationPoint(0F, 0F, 22F);
		Body3LowerRight.setTextureSize(128, 64);
		Body3LowerRight.mirror = true;
		setRotation(Body3LowerRight, 1.692969F, 0.0698132F, 0F);
		
		Body3LowerRight.mirror = false;
		FinPectoralLeft = new ModelRenderer(this, 88, 57);
		FinPectoralLeft.addBox(0F, 0F, 0F, 14, 7, 0);
		FinPectoralLeft.setRotationPoint(4F, 4F, -7F);
		FinPectoralLeft.setTextureSize(128, 64);
		FinPectoralLeft.mirror = true;
		setRotation(FinPectoralLeft, 2.007129F, -0.7853982F, 0.4363323F);
		
		FinPectoralRight = new ModelRenderer(this, 88, 57);
		FinPectoralRight.mirror = true;
		FinPectoralRight.addBox(-14F, 0F, 0F, 14, 7, 0);
		FinPectoralRight.setRotationPoint(-4F, 4F, -7F);
		FinPectoralRight.setTextureSize(128, 64);
		FinPectoralRight.mirror = true;
		setRotation(FinPectoralRight, 2.007129F, 0.7853982F, -0.4363323F);
		FinPectoralRight.mirror = false;
		
		FinDorsal = new ModelRenderer(this, 94, -7);
		FinDorsal.addBox(0F, -15F, -2F, 0, 14, 7);
		FinDorsal.setRotationPoint(0F, -4F, 4F);
		FinDorsal.setTextureSize(128, 64);
		FinDorsal.mirror = true;
		setRotation(FinDorsal, -0.5235988F, 0F, 0F);
		
		FinPelvicLeft = new ModelRenderer(this, 96, 52);
		FinPelvicLeft.addBox(0F, 0F, 0F, 5, 3, 0);
		FinPelvicLeft.setRotationPoint(3F, 4F, 17F);
		FinPelvicLeft.setTextureSize(128, 64);
		FinPelvicLeft.mirror = true;
		setRotation(FinPelvicLeft, 2.181662F, -0.7853982F, 0.6981317F);
		
		FinPelvicRight = new ModelRenderer(this, 96, 52);
		FinPelvicRight.mirror = true;
		FinPelvicRight.addBox(-5F, 0F, 0F, 5, 3, 0);
		FinPelvicRight.setRotationPoint(-3F, 4F, 17F);
		FinPelvicRight.setTextureSize(128, 64);
		FinPelvicRight.mirror = true;
		setRotation(FinPelvicRight, 2.181662F, 0.7853982F, -0.6981317F);
		FinPelvicRight.mirror = false;
		
		FinAdipose = new ModelRenderer(this, 109, -3);
		FinAdipose.addBox(0F, -5F, 0F, 0, 5, 3);
		FinAdipose.setRotationPoint(0F, -3.8F, 24F);
		FinAdipose.setTextureSize(128, 64);
		FinAdipose.mirror = true;
		setRotation(FinAdipose, -0.7853982F, 0F, 0F);
		
		FinAnal = new ModelRenderer(this, 108, 47);
		FinAnal.addBox(0F, 0F, 0F, 0, 5, 3);
		FinAnal.setRotationPoint(0F, 3.6F, 25F);
		FinAnal.setTextureSize(128, 64);
		FinAnal.mirror = true;
		setRotation(FinAnal, 0.8726646F, 0F, 0F);
		
		FinCaudalUpper = new ModelRenderer(this, 116, -6);
		FinCaudalUpper.addBox(0F, -20F, -2F, 0, 20, 6);
		FinCaudalUpper.setRotationPoint(0F, 0F, 35F);
		FinCaudalUpper.setTextureSize(128, 64);
		FinCaudalUpper.mirror = true;
		setRotation(FinCaudalUpper, -0.9599311F, 0F, 0F);
		
		FinCaudalLower = new ModelRenderer(this, 116, 46);
		FinCaudalLower.addBox(0F, -12.53333F, -4F, 0, 12, 6);
		FinCaudalLower.setRotationPoint(0F, 0F, 35F);
		FinCaudalLower.setTextureSize(128, 64);
		FinCaudalLower.mirror = true;
		setRotation(FinCaudalLower, -2.356194F, 0F, 0F);
		
		Body3Lower = new ModelRenderer(this, 14, 48);
		Body3Lower.addBox(3F, -21F, -5.6F, 2, 11, 5);
		Body3Lower.setRotationPoint(0F, 0F, 3F);
		Body3Lower.setTextureSize(128, 64);
		Body3Lower.mirror = true;
		setRotation(Body3Lower, 1.500983F, 0.0907571F, 0F);
		
		Body4Lower = new ModelRenderer(this, 14, 48);
		Body4Lower.mirror = true;
		Body4Lower.addBox(-5F, -21F, -5.6F, 2, 11, 5);
		Body4Lower.setRotationPoint(0F, 0F, 3F);
		Body4Lower.setTextureSize(128, 64);
		Body4Lower.mirror = true;
		setRotation(Body4Lower, 1.500983F, -0.0907571F, 0F);
		Body1Lower.mirror = false;

		Head2 = new ModelRenderer(this, 0, 0);		
		Head2.addBox(-3F, -8.8F, 0F, 6, 9, 3);
		Head2.setRotationPoint(0F, 0.5F, -14F);
		Head2.setTextureSize(128, 64);
		Head2.mirror = true;
		setRotation(Head2, 1.919862F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
			
		GlStateManager.enableCull();
		Head1.render(f5);
		Head3.render(f5);
		Head2.render(f5);

		
		
		Body1Upper.render(f5);
		Body1Lower.render(f5);
		Body2Upper.render(f5);
		Body2Lower.render(f5);
		
		Body3UpperLeft.render(f5);
		Body3LowerLeft.render(f5);
		Body3LowerRight.render(f5);
		Body3Lower.render(f5);

		FinCaudalUpper.render(f5);
		FinCaudalLower.render(f5);
	
		FinPectoralLeft.render(f5);
		FinPectoralRight.render(f5);
		FinDorsal.render(f5);
		FinPelvicLeft.render(f5);
		FinPelvicRight.render(f5);
		FinAdipose.render(f5);
		FinAnal.render(f5);
		Body4Lower.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
		float timeScale = 0.05f;
		
		if(!e.isInWater()) {
			timeScale = 0.2f;
		}
		
		FinPectoralLeft.rotateAngleZ = (0.4f -(float) Math.sin(f2*timeScale)*0.3f);
		FinPectoralRight.rotateAngleZ = (-0.4f -(float) Math.sin(f2*timeScale)*0.3f);
		

		Body3UpperLeft.rotateAngleY =  -(float) Math.sin(f2*timeScale)*0.2f;
		Body3LowerLeft.rotateAngleY =  -(float) Math.sin(f2*timeScale)*0.2f;
		Body3LowerRight.rotateAngleY =  -(float) Math.sin(f2*timeScale)*0.2f;

		
		FinCaudalUpper.offsetX = -(float) Math.sin(f2*timeScale)*0.175f;
		FinCaudalUpper.rotateAngleY =  -(float) Math.sin(f2*timeScale)*0.2f;

		FinCaudalLower.offsetX = -(float) Math.sin(f2*timeScale)*0.175f;
		FinCaudalLower.rotateAngleY =  -(float) Math.sin(f2*timeScale)*0.2f;

		FinAdipose.offsetX = -(float) Math.sin(f2*timeScale)*0.025f;
		FinAdipose.rotateAngleY =  -(float) Math.sin(f2*timeScale)*0.2f;
	
		FinAnal.offsetX = -(float) Math.sin(f2*timeScale)*0.025f;
		FinAnal.rotateAngleY =  -(float) Math.sin(f2*timeScale)*0.2f;

	}

}
