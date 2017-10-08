package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityDolphin;

public class ModelDolphin extends ModelBase {
	ModelRenderer Body1;
	ModelRenderer Body2;
	ModelRenderer Head1;
	ModelRenderer LowerJaw1;
	ModelRenderer LowerJaw2;
	ModelRenderer LowerJaw3;
	ModelRenderer LowerJaw4;
	ModelRenderer LowerJaw5;
	ModelRenderer UpperJaw1;
	ModelRenderer UpperJaw2;
	ModelRenderer UpperJaw3;
	ModelRenderer UpperJaw4;
	ModelRenderer UpperJaw5;
	ModelRenderer Head2;
	ModelRenderer Head3;
	ModelRenderer Head4;
	ModelRenderer Head5;
	ModelRenderer Body3;
	ModelRenderer RightPectoralFin1;
	ModelRenderer RightPectoralFin2;
	ModelRenderer RightPectoralFin3;
	ModelRenderer LeftPectoralFin1;
	ModelRenderer LeftPectoralFin2;
	ModelRenderer LeftPectoralFin3;
	ModelRenderer Tail1;
	ModelRenderer Tail2;
	ModelRenderer Tail3;
	ModelRenderer Tail4;
	ModelRenderer Fluke1;
	ModelRenderer Fluke2;
	ModelRenderer Fluke3;
	ModelRenderer Fluke4;
	ModelRenderer Fluke5;
	ModelRenderer Fluke6;
	ModelRenderer Fluke7;
	ModelRenderer Fluke8;
	ModelRenderer DorsalFin1;
	ModelRenderer DorsalFin2;
	ModelRenderer DorsalFin3;
	ModelRenderer DorsalFin4;
	ModelRenderer DorsalFin5;

	public ModelDolphin() {
		this(0.0f);
	}

	public ModelDolphin(float par1) {
		Body1 = new ModelRenderer(this, 0, 0);
		Body1.setTextureSize(64, 64);
		Body1.addBox(-3F, -3F, -3F, 6, 6, 8);
		Body1.setRotationPoint(0F, 20F, 0F);
		Body2 = new ModelRenderer(this, 0, 14);
		Body2.setTextureSize(64, 64);
		Body2.addBox(-3F, -2F, -5F, 6, 5, 4);
		Body2.setRotationPoint(0F, 19.8F, -2F);
		Head1 = new ModelRenderer(this, 0, 57);
		Head1.setTextureSize(64, 64);
		Head1.addBox(-2.5F, -3F, -3F, 5, 4, 3);
		Head1.setRotationPoint(0F, 21.4F, -6.3F);
		LowerJaw1 = new ModelRenderer(this, 16, 61);
		LowerJaw1.setTextureSize(64, 64);
		LowerJaw1.addBox(-2.5F, 0F, -1F, 5, 2, 1);
		LowerJaw1.setRotationPoint(0F, 20.4F, -9.3F);
		LowerJaw2 = new ModelRenderer(this, 29, 60);
		LowerJaw2.setTextureSize(64, 64);
		LowerJaw2.addBox(-2F, 0F, -3F, 4, 1, 3);
		LowerJaw2.setRotationPoint(0F, 21.4F, -10.3F);
		LowerJaw3 = new ModelRenderer(this, 29, 54);
		LowerJaw3.setTextureSize(64, 64);
		LowerJaw3.addBox(-2F, 0F, -3F, 4, 1, 3);
		LowerJaw3.setRotationPoint(0F, 20.4F, -10.3F);
		LowerJaw4 = new ModelRenderer(this, 44, 61);
		LowerJaw4.setTextureSize(64, 64);
		LowerJaw4.addBox(-1.5F, 0F, -2F, 3, 1, 2);
		LowerJaw4.setRotationPoint(0F, 21.4F, -13.3F);
		LowerJaw5 = new ModelRenderer(this, 45, 56);
		LowerJaw5.setTextureSize(64, 64);
		LowerJaw5.addBox(-1.5F, -1F, -1F, 3, 1, 1);
		LowerJaw5.setRotationPoint(0F, 22.4F, -15.3F);
		UpperJaw1 = new ModelRenderer(this, 52, 0);
		UpperJaw1.setTextureSize(64, 64);
		UpperJaw1.addBox(-2.5F, 0F, -1F, 5, 1, 1);
		UpperJaw1.setRotationPoint(0F, 19.4F, -9.3F);
		UpperJaw2 = new ModelRenderer(this, 50, 3);
		UpperJaw2.setTextureSize(64, 64);
		UpperJaw2.addBox(-2F, 0F, -3F, 4, 1, 3);
		UpperJaw2.setRotationPoint(0F, 19.4F, -10.3F);
		UpperJaw3 = new ModelRenderer(this, 54, 8);
		UpperJaw3.setTextureSize(64, 64);
		UpperJaw3.addBox(-1.5F, -1F, -2F, 3, 1, 2);
		UpperJaw3.setRotationPoint(0F, 21.36575F, -12.77706F);
		UpperJaw4 = new ModelRenderer(this, 58, 12);
		UpperJaw4.setTextureSize(64, 64);
		UpperJaw4.addBox(-1F, -1F, -1F, 2, 1, 1);
		UpperJaw4.setRotationPoint(0F, 21.36575F, -14.77706F);
		UpperJaw5 = new ModelRenderer(this, 52, 15);
		UpperJaw5.setTextureSize(64, 64);
		UpperJaw5.addBox(-1F, 0F, -4F, 2, 1, 4);
		UpperJaw5.setRotationPoint(0F, 19.74202F, -11.23969F);
		Head2 = new ModelRenderer(this, 0, 49);
		Head2.setTextureSize(64, 64);
		Head2.addBox(-2F, -1F, -4F, 4, 2, 4);
		Head2.setRotationPoint(0F, 18.4F, -6.3F);
		Head3 = new ModelRenderer(this, 14, 49);
		Head3.setTextureSize(64, 64);
		Head3.addBox(-1.5F, 0F, -1F, 3, 2, 1);
		Head3.setRotationPoint(0F, 17.99005F, -10.40267F);
		Head4 = new ModelRenderer(this, 24, 49);
		Head4.setTextureSize(64, 64);
		Head4.addBox(-1.5F, 0F, -1F, 3, 2, 1);
		Head4.setRotationPoint(0F, 18.43765F, -11.29691F);
		Head5 = new ModelRenderer(this, 34, 49);
		Head5.setTextureSize(64, 64);
		Head5.addBox(-1.5F, 0F, -1F, 3, 1, 1);
		Head5.setRotationPoint(0F, 19.10989F, -12.03724F);
		Body3 = new ModelRenderer(this, 20, 14);
		Body3.setTextureSize(64, 64);
		Body3.addBox(-2.5F, 0F, -4.3F, 5, 1, 5);
		Body3.setRotationPoint(0F, 17.1F, -2.5F);
		RightPectoralFin1 = new ModelRenderer(this, 0, 37);
		RightPectoralFin1.setTextureSize(64, 64);
		RightPectoralFin1.addBox(-3F, 0F, 0F, 3, 1, 3);
		RightPectoralFin1.setRotationPoint(-3F, 21.3F, -5F);
		RightPectoralFin2 = new ModelRenderer(this, 0, 41);
		RightPectoralFin2.setTextureSize(64, 64);
		RightPectoralFin2.addBox(-1F, 0F, 0F, 1, 1, 2);
		RightPectoralFin2.setRotationPoint(-5.104775F, 22.85859F, -3.227792F);
		RightPectoralFin3 = new ModelRenderer(this, 8, 42);
		RightPectoralFin3.setTextureSize(64, 64);
		RightPectoralFin3.addBox(-1F, 0F, 0F, 1, 1, 1);
		RightPectoralFin3.setRotationPoint(-5.521684F, 23.16731F, -1.912163F);

		LeftPectoralFin1 = new ModelRenderer(this, 0, 37);
		LeftPectoralFin1.setTextureSize(64, 64);
		LeftPectoralFin1.addBox(0F, 0F, 0F, 3, 1, 3);
		LeftPectoralFin1.setRotationPoint(3F, 21.3F, -5F);

		LeftPectoralFin2 = new ModelRenderer(this, 0, 41);
		LeftPectoralFin2.setTextureSize(64, 64);
		LeftPectoralFin2.addBox(3, -0.1f, 0.5f, 1, 1, 2);
		LeftPectoralFin2.setRotationPoint(3F, 21.3F, -5F);

		LeftPectoralFin3 = new ModelRenderer(this, 8, 42);
		LeftPectoralFin3.setTextureSize(64, 64);
		LeftPectoralFin3.addBox(4, -0.15F, 0.5f, 1, 1, 1);
		LeftPectoralFin3.setRotationPoint(3F, 21.3F, -5F);

		Tail1 = new ModelRenderer(this, 0, 24);
		Tail1.setTextureSize(64, 64);
		Tail1.addBox(-2.5F, -2.5F, -1F, 5, 5, 7);
		Tail1.setRotationPoint(0F, 19.8F, 5.1F);
		Tail2 = new ModelRenderer(this, 24, 27);
		Tail2.setTextureSize(64, 64);
		Tail2.addBox(-2F, -2F, -1F, 4, 4, 5);
		Tail2.setRotationPoint(0F, 20.07322F, 11.09378F);
		// Tail1.addChild(Tail2);
		Tail3 = new ModelRenderer(this, 40, 24);
		Tail3.setTextureSize(64, 64);
		Tail3.addBox(-1.5F, -1.5F, -1F, 3, 3, 4);
		Tail3.setRotationPoint(0F, 20.8163F, 15.02924F);
		Tail4 = new ModelRenderer(this, 27, 30);
		Tail4.setTextureSize(64, 64);
		Tail4.addBox(-1F, -1F, 0F, 2, 2, 3);
		Tail4.setRotationPoint(0F, 21.49112F, 17.43644F);
		Fluke1 = new ModelRenderer(this, 44, 34);
		Fluke1.setTextureSize(64, 64);
		Fluke1.addBox(-3F, 0F, 0F, 6, 1, 1);
		Fluke1.setRotationPoint(0F, 22.1683F, 19.21166F);
		Fluke2 = new ModelRenderer(this, 43, 38);
		Fluke2.setTextureSize(64, 64);
		Fluke2.addBox(-4.5F, 0F, 0F, 9, 1, 1);
		Fluke2.setRotationPoint(0F, 22.25945F, 20.2075F);
		Fluke3 = new ModelRenderer(this, 30, 38);
		Fluke3.setTextureSize(64, 64);
		Fluke3.addBox(-5F, 0F, -1F, 5, 1, 1);
		Fluke3.setRotationPoint(4.9F, 22.44176F, 22.19917F);
		Fluke4 = new ModelRenderer(this, 14, 38);
		Fluke4.setTextureSize(64, 64);
		Fluke4.addBox(-5F, 0F, 0F, 6, 1, 1);
		Fluke4.setRotationPoint(4.9F, 22.44176F, 22.19917F);
		Fluke5 = new ModelRenderer(this, 30, 38);
		Fluke5.setTextureSize(64, 64);
		Fluke5.addBox(0F, 0F, -1F, 5, 1, 1);
		Fluke5.setRotationPoint(-4.9F, 22.44176F, 22.19917F);
		Fluke6 = new ModelRenderer(this, 14, 38);
		Fluke6.setTextureSize(64, 64);
		Fluke6.addBox(-1F, 0F, 0F, 6, 1, 1);
		Fluke6.setRotationPoint(-4.9F, 22.44176F, 22.19917F);
		Fluke7 = new ModelRenderer(this, 55, 30);
		Fluke7.setTextureSize(64, 64);
		Fluke7.addBox(-3F, 0F, 0F, 3, 1, 1);
		Fluke7.setRotationPoint(0F, 22.43265F, 22.09959F);
		Fluke8 = new ModelRenderer(this, 55, 30);
		Fluke8.setTextureSize(64, 64);
		Fluke8.addBox(0F, 0F, 0F, 3, 1, 1);
		Fluke8.setRotationPoint(0F, 22.43265F, 22.09959F);
		DorsalFin1 = new ModelRenderer(this, 21, 0);
		DorsalFin1.setTextureSize(64, 64);
		DorsalFin1.addBox(-0.5F, -1F, -0.7F, 1, 1, 5);
		DorsalFin1.setRotationPoint(0F, 17.1F, 0F);
		DorsalFin2 = new ModelRenderer(this, 35, 0);
		DorsalFin2.setTextureSize(64, 64);
		DorsalFin2.addBox(-0.5F, -1F, 0F, 1, 1, 4);
		DorsalFin2.setRotationPoint(0F, 16.10415F, 0.09098025F);
		DorsalFin3 = new ModelRenderer(this, 30, 7);
		DorsalFin3.setTextureSize(64, 64);
		DorsalFin3.addBox(-0.5F, -1F, 0F, 1, 1, 3);
		DorsalFin3.setRotationPoint(0F, 15.30191F, 1.255631F);
		DorsalFin4 = new ModelRenderer(this, 39, 7);
		DorsalFin4.setTextureSize(64, 64);
		DorsalFin4.addBox(-0.5F, -1F, 0F, 1, 1, 2);
		DorsalFin4.setRotationPoint(0F, 14.60895F, 2.48844F);
		DorsalFin5 = new ModelRenderer(this, 45, 0);
		DorsalFin5.setTextureSize(64, 64);
		DorsalFin5.addBox(-0.5F, -1F, 0F, 1, 1, 1);
		DorsalFin5.setRotationPoint(0F, 14.15063F, 3.826327F);
	}

	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {

		
		float tailVertSpeed = 0.5f/2;
		float tailHorzSpeed = 0.25f/2;
		
		if(par1Entity instanceof EntityDolphin) {
			if(((EntityDolphin)par1Entity).getCurrentSwimSpeed() == 0) {
				tailVertSpeed = 0.125f/2;
				tailHorzSpeed = 0.125f/4;
			}else {
				tailVertSpeed = 0.5f/2;
				tailHorzSpeed = 0.25f/2;
			}
			if(((EntityDolphin)par1Entity).getAir() <= 0) {
				tailVertSpeed = 0.5f;
				tailHorzSpeed = 0.25f;
			}
		}
		
		Body1.rotateAngleX = 0F;
		Body1.rotateAngleY = 0F;
		Body1.rotateAngleZ = 0F;
		Body1.renderWithRotation(par7);

		Body2.rotateAngleX = 0F;
		Body2.rotateAngleY = 0F;
		Body2.rotateAngleZ = 0F;
		Body2.renderWithRotation(par7);

		Head1.rotateAngleX = 0F;
		Head1.rotateAngleY = 0F;
		Head1.rotateAngleZ = 0F;
		Head1.renderWithRotation(par7);

		LowerJaw1.rotateAngleX = 0F;
		LowerJaw1.rotateAngleY = 0F;
		LowerJaw1.rotateAngleZ = 0F;
		LowerJaw1.renderWithRotation(par7);

		LowerJaw2.rotateAngleX = 0F;
		LowerJaw2.rotateAngleY = 0F;
		LowerJaw2.rotateAngleZ = 0F;
		LowerJaw2.renderWithRotation(par7);

		LowerJaw3.rotateAngleX = 0.3490658F;
		LowerJaw3.rotateAngleY = 0F;
		LowerJaw3.rotateAngleZ = 0F;
		LowerJaw3.renderWithRotation(par7);

		LowerJaw4.rotateAngleX = 0F;
		LowerJaw4.rotateAngleY = 0F;
		LowerJaw4.rotateAngleZ = 0F;
		LowerJaw4.renderWithRotation(par7);

		LowerJaw5.rotateAngleX = -0.2275909F;
		LowerJaw5.rotateAngleY = 0F;
		LowerJaw5.rotateAngleZ = 0F;
		LowerJaw5.renderWithRotation(par7);

		UpperJaw1.rotateAngleX = 0F;
		UpperJaw1.rotateAngleY = 0F;
		UpperJaw1.rotateAngleZ = 0F;
		UpperJaw1.renderWithRotation(par7);

		UpperJaw2.rotateAngleX = 0.3490658F;
		UpperJaw2.rotateAngleY = 0F;
		UpperJaw2.rotateAngleZ = 0F;
		UpperJaw2.renderWithRotation(par7);

		UpperJaw3.rotateAngleX = 0F;
		UpperJaw3.rotateAngleY = 0F;
		UpperJaw3.rotateAngleZ = 0F;
		UpperJaw3.renderWithRotation(par7);

		UpperJaw4.rotateAngleX = -0.09110618F;
		UpperJaw4.rotateAngleY = 0F;
		UpperJaw4.rotateAngleZ = 0F;
		UpperJaw4.renderWithRotation(par7);

		UpperJaw5.rotateAngleX = 0.15132F;
		UpperJaw5.rotateAngleY = 0F;
		UpperJaw5.rotateAngleZ = 0F;
		UpperJaw5.renderWithRotation(par7);

		Head2.rotateAngleX = 0.1453859F;
		Head2.rotateAngleY = 0F;
		Head2.rotateAngleZ = 0F;
		Head2.renderWithRotation(par7);

		Head3.rotateAngleX = 0.4640831F;
		Head3.rotateAngleY = 0F;
		Head3.rotateAngleZ = 0F;
		Head3.renderWithRotation(par7);

		Head4.rotateAngleX = 0.737227F;
		Head4.rotateAngleY = 0F;
		Head4.rotateAngleZ = 0F;
		Head4.renderWithRotation(par7);

		Head5.rotateAngleX = 1.055924F;
		Head5.rotateAngleY = 0F;
		Head5.rotateAngleZ = 0F;
		Head5.renderWithRotation(par7);

		Body3.rotateAngleX = 0.04555309F;
		Body3.rotateAngleY = 0F;
		Body3.rotateAngleZ = 0F;
		Body3.renderWithRotation(par7);

		RightPectoralFin1.rotateAngleX = 0.1612329F;
		RightPectoralFin1.rotateAngleY = 0.2214468F;
		RightPectoralFin1.rotateAngleZ = -0.6194302F;
		RightPectoralFin1.renderWithRotation(par7);

		RightPectoralFin2.rotateAngleX = 0.2393862F;
		RightPectoralFin2.rotateAngleY = 0.3358756F;
		RightPectoralFin2.rotateAngleZ = -0.5966207F;
		RightPectoralFin2.renderWithRotation(par7);

		RightPectoralFin3.rotateAngleX = 0.3620028F;
		RightPectoralFin3.rotateAngleY = 0.5368112F;
		RightPectoralFin3.rotateAngleZ = -0.5368112F;
		RightPectoralFin3.renderWithRotation(par7);

		LeftPectoralFin1.rotateAngleX = 0.1612329F;
		LeftPectoralFin1.rotateAngleY = -0.2214468F;
		LeftPectoralFin1.rotateAngleZ = 0.6194302F + (float) (Math.sin(par4 * .025F)) * .3f;
		LeftPectoralFin1.renderWithRotation(par7);

		LeftPectoralFin2.rotateAngleX = 0.2393862F;
		LeftPectoralFin2.rotateAngleY = -0.3358756F;

		LeftPectoralFin2.rotateAngleZ = 0.5966207F + (float) (Math.sin(par4 * .025F)) * .35f;
		LeftPectoralFin2.renderWithRotation(par7);

		LeftPectoralFin3.rotateAngleX = 0.3620028F;
		LeftPectoralFin3.rotateAngleY = -0.5368112F;
		LeftPectoralFin3.rotateAngleZ = 0.5368112F + (float) (Math.sin(par4 * .025F)) * .4f;
		LeftPectoralFin3.renderWithRotation(par7);

		Tail1.rotateAngleX = -0.04555309F + (float) (Math.sin(par4 * tailVertSpeed)) * .1f;
		// System.out.println(par4);
		Tail1.rotateAngleY = (float) (Math.sin(par4 * tailHorzSpeed)) * .135F;
		Tail1.rotateAngleZ = 0F;
		Tail1.renderWithRotation(par7);

		Tail2.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 1;

		Tail2.rotationPointY = 20 - (float) (Math.sin(par4 * tailVertSpeed)) * 0.8F;

		Tail2.rotateAngleX = -0.1366593F + (float) (Math.sin(par4 * tailVertSpeed)) * .1f;
		Tail2.rotateAngleY = (float) (Math.sin(par4 * tailHorzSpeed)) * .135F;
		Tail2.rotateAngleZ = 0F;
		Tail2.renderWithRotation(par7);

		Tail3.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 1.85f;

		Tail3.rotationPointY = 20.5f - (float) (Math.sin(par4 * tailVertSpeed)) * 1.5F;

		Tail3.rotateAngleX = -0.2733185F + (float) (Math.sin(par4 * tailVertSpeed)) * .2f;
		Tail3.rotateAngleY = (float) (Math.sin(par4 * tailHorzSpeed)) * .135F;
		Tail3.rotateAngleZ = 0F;
		Tail3.renderWithRotation(par7);

		Tail4.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.4f;
		Tail4.rotationPointY = 21.5f - (float) (Math.sin(par4 * tailVertSpeed)) * 2.5F;

		Tail4.rotateAngleX = -0.3644247F + (float) (Math.sin(par4 * tailVertSpeed)) * .5f;
		Tail4.rotateAngleY = (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		Tail4.rotateAngleZ = 0F;
		Tail4.renderWithRotation(par7);

		Fluke1.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;
		Fluke1.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		Fluke1.rotateAngleX = -0.09128072F;
		Fluke1.rotateAngleY = (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		Fluke1.rotateAngleZ = 0F;
		Fluke1.renderWithRotation(par7);

		Fluke2.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		Fluke2.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;

		Fluke2.rotateAngleX = -0.09128071F;
		Fluke2.rotateAngleY = (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		Fluke2.rotateAngleZ = 0F;
		Fluke2.renderWithRotation(par7);

		Fluke3.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;
		Fluke3.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		Fluke3.rotateAngleX = -0.09118575F;
		Fluke3.rotateAngleY = -0.04574326F + (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		Fluke3.rotateAngleZ = 0.00416824F;
		Fluke3.renderWithRotation(par7);

		Fluke4.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		Fluke4.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;

		Fluke4.rotateAngleX = -0.08892051F + (float) (Math.sin(par4 * tailVertSpeed)) * .8f;
		;
		Fluke4.rotateAngleY = -0.2285096F + (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		Fluke4.rotateAngleZ = 0.02065023F;
		Fluke4.renderWithRotation(par7);

		Fluke5.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;
		Fluke5.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		Fluke5.rotateAngleX = -0.09118575F;
		Fluke5.rotateAngleY = 0.04574326F + (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		Fluke5.rotateAngleZ = -0.00416824F;
		Fluke5.renderWithRotation(par7);

		Fluke6.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;
		Fluke6.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		Fluke6.rotateAngleX = -0.08892051F + (float) (Math.sin(par4 * tailVertSpeed)) * .8f;
		Fluke6.rotateAngleY = 0.2285096F + (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		Fluke6.rotateAngleZ = -0.02065023F;
		Fluke6.renderWithRotation(par7);

		Fluke7.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;
		Fluke7.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		Fluke7.rotateAngleX = -0.09042732F + (float) (Math.sin(par4 * tailVertSpeed)) * .8f;
		Fluke7.rotateAngleY = -0.1372235F + (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		Fluke7.rotateAngleZ = 0.01246957F;
		Fluke7.renderWithRotation(par7);

		Fluke8.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;
		Fluke8.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		Fluke8.rotateAngleX = -0.09042732F + (float) (Math.sin(par4 * tailVertSpeed)) * .8f;
		;
		Fluke8.rotateAngleY = 0.1372235F + (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		Fluke8.rotateAngleZ = -0.01246957F;
		Fluke8.renderWithRotation(par7);

		DorsalFin1.rotateAngleX = -0.09110619F;
		DorsalFin1.rotateAngleY = 0F;
		DorsalFin1.rotateAngleZ = 0F;
		DorsalFin1.renderWithRotation(par7);

		DorsalFin2.rotateAngleX = -0.1822124F;
		DorsalFin2.rotateAngleY = 0F;
		DorsalFin2.rotateAngleZ = 0F;
		DorsalFin2.renderWithRotation(par7);

		DorsalFin3.rotateAngleX = -0.2733186F;
		DorsalFin3.rotateAngleY = 0F;
		DorsalFin3.rotateAngleZ = 0F;
		DorsalFin3.renderWithRotation(par7);

		DorsalFin4.rotateAngleX = -0.4553564F;
		DorsalFin4.rotateAngleY = 0F;
		DorsalFin4.rotateAngleZ = 0F;
		DorsalFin4.renderWithRotation(par7);

		DorsalFin5.rotateAngleX = -0.7285004F;
		DorsalFin5.rotateAngleY = 0F;
		DorsalFin5.rotateAngleZ = 0F;
		DorsalFin5.renderWithRotation(par7);

	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

}
