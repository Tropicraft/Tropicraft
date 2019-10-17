package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.tropicraft.core.common.entity.underdasea.TropicraftDolphinEntity;

public class TropicraftDolphinModel extends EntityModel<TropicraftDolphinEntity> {
	RendererModel body1;
	RendererModel body2;
	RendererModel head1;
	RendererModel lowerJaw1;
	RendererModel lowerJaw2;
	RendererModel lowerJaw3;
	RendererModel lowerJaw4;
	RendererModel lowerJaw5;
	RendererModel upperJaw1;
	RendererModel upperJaw2;
	RendererModel upperJaw3;
	RendererModel upperJaw4;
	RendererModel upperJaw5;
	RendererModel head2;
	RendererModel head3;
	RendererModel head4;
	RendererModel head5;
	RendererModel body3;
	RendererModel rightPectoralFin1;
	RendererModel rightPectoralFin2;
	RendererModel rightPectoralFin3;
	RendererModel leftPectoralFin1;
	RendererModel leftPectoralFin2;
	RendererModel leftPectoralFin3;
	RendererModel tail1;
	RendererModel tail2;
	RendererModel tail3;
	RendererModel tail4;
	RendererModel fluke1;
	RendererModel fluke2;
	RendererModel fluke3;
	RendererModel fluke4;
	RendererModel fluke5;
	RendererModel fluke6;
	RendererModel fluke7;
	RendererModel fluke8;
	RendererModel dorsalFin1;
	RendererModel dorsalFin2;
	RendererModel dorsalFin3;
	RendererModel dorsalFin4;
	RendererModel dorsalFin5;

	public float tailVertSpeed = 1f;
	public float tailHorzSpeed = 1f;

	public TropicraftDolphinModel() {
		body1 = new RendererModel(this, 0, 0);
		body1.setTextureSize(64, 64);
		body1.addBox(-3F, -3F, -3F, 6, 6, 8);
		body1.setRotationPoint(0F, 20F, 0F);
		body2 = new RendererModel(this, 0, 14);
		body2.setTextureSize(64, 64);
		body2.addBox(-3F, -2F, -5F, 6, 5, 4);
		body2.setRotationPoint(0F, 19.8F, -2F);
		head1 = new RendererModel(this, 0, 57);
		head1.setTextureSize(64, 64);
		head1.addBox(-2.5F, -3F, -3F, 5, 4, 3);
		head1.setRotationPoint(0F, 21.4F, -6.3F);
		lowerJaw1 = new RendererModel(this, 16, 61);
		lowerJaw1.setTextureSize(64, 64);
		lowerJaw1.addBox(-2.5F, 0F, -1F, 5, 2, 1);
		lowerJaw1.setRotationPoint(0F, 20.4F, -9.3F);
		lowerJaw2 = new RendererModel(this, 29, 60);
		lowerJaw2.setTextureSize(64, 64);
		lowerJaw2.addBox(-2F, 0F, -3F, 4, 1, 3);
		lowerJaw2.setRotationPoint(0F, 21.4F, -10.3F);
		lowerJaw3 = new RendererModel(this, 29, 54);
		lowerJaw3.setTextureSize(64, 64);
		lowerJaw3.addBox(-2F, 0F, -3F, 4, 1, 3);
		lowerJaw3.setRotationPoint(0F, 20.4F, -10.3F);
		lowerJaw4 = new RendererModel(this, 44, 61);
		lowerJaw4.setTextureSize(64, 64);
		lowerJaw4.addBox(-1.5F, 0F, -2F, 3, 1, 2);
		lowerJaw4.setRotationPoint(0F, 21.4F, -13.3F);
		lowerJaw5 = new RendererModel(this, 45, 56);
		lowerJaw5.setTextureSize(64, 64);
		lowerJaw5.addBox(-1.5F, -1F, -1F, 3, 1, 1);
		lowerJaw5.setRotationPoint(0F, 22.4F, -15.3F);
		upperJaw1 = new RendererModel(this, 52, 0);
		upperJaw1.setTextureSize(64, 64);
		upperJaw1.addBox(-2.5F, 0F, -1F, 5, 1, 1);
		upperJaw1.setRotationPoint(0F, 19.4F, -9.3F);
		upperJaw2 = new RendererModel(this, 50, 3);
		upperJaw2.setTextureSize(64, 64);
		upperJaw2.addBox(-2F, 0F, -3F, 4, 1, 3);
		upperJaw2.setRotationPoint(0F, 19.4F, -10.3F);
		upperJaw3 = new RendererModel(this, 54, 8);
		upperJaw3.setTextureSize(64, 64);
		upperJaw3.addBox(-1.5F, -1F, -2F, 3, 1, 2);
		upperJaw3.setRotationPoint(0F, 21.36575F, -12.77706F);
		upperJaw4 = new RendererModel(this, 58, 12);
		upperJaw4.setTextureSize(64, 64);
		upperJaw4.addBox(-1F, -1F, -1F, 2, 1, 1);
		upperJaw4.setRotationPoint(0F, 21.36575F, -14.77706F);
		upperJaw5 = new RendererModel(this, 52, 15);
		upperJaw5.setTextureSize(64, 64);
		upperJaw5.addBox(-1F, 0F, -4F, 2, 1, 4);
		upperJaw5.setRotationPoint(0F, 19.74202F, -11.23969F);
		head2 = new RendererModel(this, 0, 49);
		head2.setTextureSize(64, 64);
		head2.addBox(-2F, -1F, -4F, 4, 2, 4);
		head2.setRotationPoint(0F, 18.4F, -6.3F);
		head3 = new RendererModel(this, 14, 49);
		head3.setTextureSize(64, 64);
		head3.addBox(-1.5F, 0F, -1F, 3, 2, 1);
		head3.setRotationPoint(0F, 17.99005F, -10.40267F);
		head4 = new RendererModel(this, 24, 49);
		head4.setTextureSize(64, 64);
		head4.addBox(-1.5F, 0F, -1F, 3, 2, 1);
		head4.setRotationPoint(0F, 18.43765F, -11.29691F);
		head5 = new RendererModel(this, 34, 49);
		head5.setTextureSize(64, 64);
		head5.addBox(-1.5F, 0F, -1F, 3, 1, 1);
		head5.setRotationPoint(0F, 19.10989F, -12.03724F);
		body3 = new RendererModel(this, 20, 14);
		body3.setTextureSize(64, 64);
		body3.addBox(-2.5F, 0F, -4.3F, 5, 1, 5);
		body3.setRotationPoint(0F, 17.1F, -2.5F);
		rightPectoralFin1 = new RendererModel(this, 0, 37);
		rightPectoralFin1.setTextureSize(64, 64);
		rightPectoralFin1.addBox(-3F, 0F, 0F, 3, 1, 3);
		rightPectoralFin1.setRotationPoint(-3F, 21.3F, -5F);
		rightPectoralFin2 = new RendererModel(this, 0, 41);
		rightPectoralFin2.setTextureSize(64, 64);
		rightPectoralFin2.addBox(-1F, 0F, 0F, 1, 1, 2);
		rightPectoralFin2.setRotationPoint(-5.104775F, 22.85859F, -3.227792F);
		rightPectoralFin3 = new RendererModel(this, 8, 42);
		rightPectoralFin3.setTextureSize(64, 64);
		rightPectoralFin3.addBox(-1F, 0F, 0F, 1, 1, 1);
		rightPectoralFin3.setRotationPoint(-5.521684F, 23.16731F, -1.912163F);

		leftPectoralFin1 = new RendererModel(this, 0, 37);
		leftPectoralFin1.setTextureSize(64, 64);
		leftPectoralFin1.addBox(0F, 0F, 0F, 3, 1, 3);
		leftPectoralFin1.setRotationPoint(3F, 21.3F, -5F);

		leftPectoralFin2 = new RendererModel(this, 0, 41);
		leftPectoralFin2.setTextureSize(64, 64);
		leftPectoralFin2.addBox(3, -0.1f, 0.5f, 1, 1, 2);
		leftPectoralFin2.setRotationPoint(3F, 21.3F, -5F);

		leftPectoralFin3 = new RendererModel(this, 8, 42);
		leftPectoralFin3.setTextureSize(64, 64);
		leftPectoralFin3.addBox(4, -0.15F, 0.5f, 1, 1, 1);
		leftPectoralFin3.setRotationPoint(3F, 21.3F, -5F);

		tail1 = new RendererModel(this, 0, 24);
		tail1.setTextureSize(64, 64);
		tail1.addBox(-2.5F, -2.5F, -1F, 5, 5, 7);
		tail1.setRotationPoint(0F, 19.8F, 5.1F);
		tail2 = new RendererModel(this, 24, 27);
		tail2.setTextureSize(64, 64);
		tail2.addBox(-2F, -2F, -1F, 4, 4, 5);
		tail2.setRotationPoint(0F, 20.07322F, 11.09378F);
		// Tail1.addChild(Tail2);
		tail3 = new RendererModel(this, 40, 24);
		tail3.setTextureSize(64, 64);
		tail3.addBox(-1.5F, -1.5F, -1F, 3, 3, 4);
		tail3.setRotationPoint(0F, 20.8163F, 15.02924F);
		tail4 = new RendererModel(this, 27, 30);
		tail4.setTextureSize(64, 64);
		tail4.addBox(-1F, -1F, 0F, 2, 2, 3);
		tail4.setRotationPoint(0F, 21.49112F, 17.43644F);
		fluke1 = new RendererModel(this, 44, 34);
		fluke1.setTextureSize(64, 64);
		fluke1.addBox(-3F, 0F, 0F, 6, 1, 1);
		fluke1.setRotationPoint(0F, 22.1683F, 19.21166F);
		fluke2 = new RendererModel(this, 43, 38);
		fluke2.setTextureSize(64, 64);
		fluke2.addBox(-4.5F, 0F, 0F, 9, 1, 1);
		fluke2.setRotationPoint(0F, 22.25945F, 20.2075F);
		fluke3 = new RendererModel(this, 30, 38);
		fluke3.setTextureSize(64, 64);
		fluke3.addBox(-5F, 0F, -1F, 5, 1, 1);
		fluke3.setRotationPoint(4.9F, 22.44176F, 22.19917F);
		fluke4 = new RendererModel(this, 14, 38);
		fluke4.setTextureSize(64, 64);
		fluke4.addBox(-5F, 0F, 0F, 6, 1, 1);
		fluke4.setRotationPoint(4.9F, 22.44176F, 22.19917F);
		fluke5 = new RendererModel(this, 30, 38);
		fluke5.setTextureSize(64, 64);
		fluke5.addBox(0F, 0F, -1F, 5, 1, 1);
		fluke5.setRotationPoint(-4.9F, 22.44176F, 22.19917F);
		fluke6 = new RendererModel(this, 14, 38);
		fluke6.setTextureSize(64, 64);
		fluke6.addBox(-1F, 0F, 0F, 6, 1, 1);
		fluke6.setRotationPoint(-4.9F, 22.44176F, 22.19917F);
		fluke7 = new RendererModel(this, 55, 30);
		fluke7.setTextureSize(64, 64);
		fluke7.addBox(-3F, 0F, 0F, 3, 1, 1);
		fluke7.setRotationPoint(0F, 22.43265F, 22.09959F);
		fluke8 = new RendererModel(this, 55, 30);
		fluke8.setTextureSize(64, 64);
		fluke8.addBox(0F, 0F, 0F, 3, 1, 1);
		fluke8.setRotationPoint(0F, 22.43265F, 22.09959F);
		dorsalFin1 = new RendererModel(this, 21, 0);
		dorsalFin1.setTextureSize(64, 64);
		dorsalFin1.addBox(-0.5F, -1F, -0.7F, 1, 1, 5);
		dorsalFin1.setRotationPoint(0F, 17.1F, 0F);
		dorsalFin2 = new RendererModel(this, 35, 0);
		dorsalFin2.setTextureSize(64, 64);
		dorsalFin2.addBox(-0.5F, -1F, 0F, 1, 1, 4);
		dorsalFin2.setRotationPoint(0F, 16.10415F, 0.09098025F);
		dorsalFin3 = new RendererModel(this, 30, 7);
		dorsalFin3.setTextureSize(64, 64);
		dorsalFin3.addBox(-0.5F, -1F, 0F, 1, 1, 3);
		dorsalFin3.setRotationPoint(0F, 15.30191F, 1.255631F);
		dorsalFin4 = new RendererModel(this, 39, 7);
		dorsalFin4.setTextureSize(64, 64);
		dorsalFin4.addBox(-0.5F, -1F, 0F, 1, 1, 2);
		dorsalFin4.setRotationPoint(0F, 14.60895F, 2.48844F);
		dorsalFin5 = new RendererModel(this, 45, 0);
		dorsalFin5.setTextureSize(64, 64);
		dorsalFin5.addBox(-0.5F, -1F, 0F, 1, 1, 1);
		dorsalFin5.setRotationPoint(0F, 14.15063F, 3.826327F);
	}

	@Override
    public void render(TropicraftDolphinEntity dolphin, float par2, float par3, float par4, float par5, float par6, float par7) {
		boolean mouthOpen = dolphin.getMouthOpen();

		if (dolphin.isInWater()) {
				tailVertSpeed = 0.5f/2;
				tailHorzSpeed = 0.25f/2;
			if (dolphin.getAir() <= 0) {
				tailVertSpeed = 0.5f;
				tailHorzSpeed = 0.25f;
			}
		} else {
			if (dolphin.onGround) {
				tailVertSpeed = 0.0f;
				tailHorzSpeed = 0.05f;
			} else {
				tailVertSpeed = 1f;
				tailHorzSpeed = 0.5f;
			}
		}
		
		body1.rotateAngleX = 0F;
		body1.rotateAngleY = 0F;
		body1.rotateAngleZ = 0F;
		body1.renderWithRotation(par7);

		body2.rotateAngleX = 0F;
		body2.rotateAngleY = 0F;
		body2.rotateAngleZ = 0F;
		body2.renderWithRotation(par7);

		head1.rotateAngleX = 0F;
		head1.rotateAngleY = 0F;
		head1.rotateAngleZ = 0F;
		head1.renderWithRotation(par7);

		lowerJaw1.rotateAngleX = 0F;
		lowerJaw1.rotateAngleY = 0F;
		lowerJaw1.rotateAngleZ = 0F;
		lowerJaw1.renderWithRotation(par7);

		lowerJaw2.rotateAngleX = 0F;
		lowerJaw2.rotateAngleY = 0F;
		lowerJaw2.rotateAngleZ = 0F;
		lowerJaw2.renderWithRotation(par7);

		lowerJaw3.rotateAngleX = 0.3490658F;
		lowerJaw3.rotateAngleY = 0F;
		lowerJaw3.rotateAngleZ = 0F;
		lowerJaw3.renderWithRotation(par7);

		if (mouthOpen) {
			lowerJaw5.setRotationPoint(0F, 23.4F, -15.3F+0.52f);
			lowerJaw4.rotateAngleX = 0.5F;
		} else {
			lowerJaw5.setRotationPoint(0F, 22.4F, -15.3F);
			lowerJaw4.rotateAngleX = 0F;
		}
		lowerJaw4.rotateAngleY = 0F;
		lowerJaw4.rotateAngleZ = 0F;
		lowerJaw4.renderWithRotation(par7);

		lowerJaw5.rotateAngleX = -0.2275909F;
		lowerJaw5.rotateAngleY = 0F;
		lowerJaw5.rotateAngleZ = 0F;
		lowerJaw5.renderWithRotation(par7);

		upperJaw1.rotateAngleX = 0F;
		upperJaw1.rotateAngleY = 0F;
		upperJaw1.rotateAngleZ = 0F;
		upperJaw1.renderWithRotation(par7);

		upperJaw2.rotateAngleX = 0.3490658F;
		upperJaw2.rotateAngleY = 0F;
		upperJaw2.rotateAngleZ = 0F;
		upperJaw2.renderWithRotation(par7);

		upperJaw3.rotateAngleX = 0F;
		upperJaw3.rotateAngleY = 0F;
		upperJaw3.rotateAngleZ = 0F;
		upperJaw3.renderWithRotation(par7);

		upperJaw4.rotateAngleX = -0.09110618F;
		upperJaw4.rotateAngleY = 0F;
		upperJaw4.rotateAngleZ = 0F;
		upperJaw4.renderWithRotation(par7);

		upperJaw5.rotateAngleX = 0.15132F;
		upperJaw5.rotateAngleY = 0F;
		upperJaw5.rotateAngleZ = 0F;
		upperJaw5.renderWithRotation(par7);

		head2.rotateAngleX = 0.1453859F;
		head2.rotateAngleY = 0F;
		head2.rotateAngleZ = 0F;
		head2.renderWithRotation(par7);

		head3.rotateAngleX = 0.4640831F;
		head3.rotateAngleY = 0F;
		head3.rotateAngleZ = 0F;
		head3.renderWithRotation(par7);

		head4.rotateAngleX = 0.737227F;
		head4.rotateAngleY = 0F;
		head4.rotateAngleZ = 0F;
		head4.renderWithRotation(par7);

		head5.rotateAngleX = 1.055924F;
		head5.rotateAngleY = 0F;
		head5.rotateAngleZ = 0F;
		head5.renderWithRotation(par7);

		body3.rotateAngleX = 0.04555309F;
		body3.rotateAngleY = 0F;
		body3.rotateAngleZ = 0F;
		body3.renderWithRotation(par7);

		rightPectoralFin1.rotateAngleX = 0.1612329F;
		rightPectoralFin1.rotateAngleY = 0.2214468F;
		rightPectoralFin1.rotateAngleZ = -0.6194302F+ (float) (Math.sin(par4 * .025F)) * .3f;
		rightPectoralFin1.renderWithRotation(par7);

		rightPectoralFin2.rotateAngleX = 0.2393862F;
		rightPectoralFin2.rotateAngleY = 0.3358756F;
		rightPectoralFin2.rotateAngleZ = -0.5966207F+ (float) (Math.sin(par4 * .025F)) * .45f;
		rightPectoralFin2.renderWithRotation(par7);

		rightPectoralFin3.rotateAngleX = 0.3620028F;
		rightPectoralFin3.rotateAngleY = 0.5368112F;
		rightPectoralFin3.rotateAngleZ = -0.5368112F+ (float) (Math.sin(par4 * .025F)) * .5f;
		rightPectoralFin3.renderWithRotation(par7);

		leftPectoralFin1.rotateAngleX = 0.1612329F;
		leftPectoralFin1.rotateAngleY = -0.2214468F;
		leftPectoralFin1.rotateAngleZ = 0.6194302F + (float) (Math.sin(par4 * .025F)) * .3f;
		leftPectoralFin1.renderWithRotation(par7);

		leftPectoralFin2.rotateAngleX = 0.2393862F;
		leftPectoralFin2.rotateAngleY = -0.3358756F;
		leftPectoralFin2.rotateAngleZ = 0.5966207F + (float) (Math.sin(par4 * .025F)) * .35f;
		leftPectoralFin2.renderWithRotation(par7);
		
		leftPectoralFin3.rotateAngleX = 0.3620028F;
		leftPectoralFin3.rotateAngleY = -0.5368112F;
		leftPectoralFin3.rotateAngleZ = 0.5368112F + (float) (Math.sin(par4 * .025F)) * .4f;
		leftPectoralFin3.renderWithRotation(par7);

		tail1.rotateAngleX = -0.04555309F + (float) (Math.sin(par4 * tailVertSpeed)) * .1f;
		// System.out.println(par4);
		tail1.rotateAngleY = (float) (Math.sin(par4 * tailHorzSpeed)) * .135F;
		tail1.rotateAngleZ = 0F;
		tail1.renderWithRotation(par7);

		tail2.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 1;

		tail2.rotationPointY = 20 - (float) (Math.sin(par4 * tailVertSpeed)) * 0.8F;

		tail2.rotateAngleX = -0.1366593F + (float) (Math.sin(par4 * tailVertSpeed)) * .1f;
		tail2.rotateAngleY = (float) (Math.sin(par4 * tailHorzSpeed)) * .135F;
		tail2.rotateAngleZ = 0F;
		tail2.renderWithRotation(par7);

		tail3.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 1.85f;

		tail3.rotationPointY = 20.5f - (float) (Math.sin(par4 * tailVertSpeed)) * 1.5F;

		tail3.rotateAngleX = -0.2733185F + (float) (Math.sin(par4 * tailVertSpeed)) * .2f;
		tail3.rotateAngleY = (float) (Math.sin(par4 * tailHorzSpeed)) * .135F;
		tail3.rotateAngleZ = 0F;
		tail3.renderWithRotation(par7);

		tail4.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.4f;
		tail4.rotationPointY = 21.5f - (float) (Math.sin(par4 * tailVertSpeed)) * 2.5F;

		tail4.rotateAngleX = -0.3644247F + (float) (Math.sin(par4 * tailVertSpeed)) * .5f;
		tail4.rotateAngleY = (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		tail4.rotateAngleZ = 0F;
		tail4.renderWithRotation(par7);

		fluke1.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;
		fluke1.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		fluke1.rotateAngleX = -0.09128072F;
		fluke1.rotateAngleY = (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		fluke1.rotateAngleZ = 0F;
		fluke1.renderWithRotation(par7);

		fluke2.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		fluke2.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;

		fluke2.rotateAngleX = -0.09128071F;
		fluke2.rotateAngleY = (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		fluke2.rotateAngleZ = 0F;
		fluke2.renderWithRotation(par7);

		fluke3.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;
		fluke3.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		fluke3.rotateAngleX = -0.09118575F;
		fluke3.rotateAngleY = -0.04574326F + (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		fluke3.rotateAngleZ = 0.00416824F;
		fluke3.renderWithRotation(par7);

		fluke4.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		fluke4.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;

		fluke4.rotateAngleX = -0.08892051F + (float) (Math.sin(par4 * tailVertSpeed)) * .8f;
		
		fluke4.rotateAngleY = -0.2285096F + (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		fluke4.rotateAngleZ = 0.02065023F;
		fluke4.renderWithRotation(par7);

		fluke5.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;
		fluke5.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		fluke5.rotateAngleX = -0.09118575F;
		fluke5.rotateAngleY = 0.04574326F + (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		fluke5.rotateAngleZ = -0.00416824F;
		fluke5.renderWithRotation(par7);

		fluke6.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;
		fluke6.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		fluke6.rotateAngleX = -0.08892051F + (float) (Math.sin(par4 * tailVertSpeed)) * .8f;
		fluke6.rotateAngleY = 0.2285096F + (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		fluke6.rotateAngleZ = -0.02065023F;
		fluke6.renderWithRotation(par7);

		fluke7.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;
		fluke7.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		fluke7.rotateAngleX = -0.09042732F + (float) (Math.sin(par4 * tailVertSpeed)) * .8f;
		fluke7.rotateAngleY = -0.1372235F + (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		fluke7.rotateAngleZ = 0.01246957F;
		fluke7.renderWithRotation(par7);

		fluke8.rotationPointX = (float) (Math.sin(par4 * tailHorzSpeed)) * 2.8f;
		fluke8.rotationPointY = 22f - (float) (Math.sin(par4 * tailVertSpeed)) * 4F;

		fluke8.rotateAngleX = -0.09042732F + (float) (Math.sin(par4 * tailVertSpeed)) * .8f;
		
		fluke8.rotateAngleY = 0.1372235F + (float) (Math.sin(par4 * tailHorzSpeed)) * .35F;
		fluke8.rotateAngleZ = -0.01246957F;
		fluke8.renderWithRotation(par7);

		dorsalFin1.rotateAngleX = -0.09110619F;
		dorsalFin1.rotateAngleY = 0F;
		dorsalFin1.rotateAngleZ = 0F;
		dorsalFin1.renderWithRotation(par7);

		dorsalFin2.rotateAngleX = -0.1822124F;
		dorsalFin2.rotateAngleY = 0F;
		dorsalFin2.rotateAngleZ = 0F;
		dorsalFin2.renderWithRotation(par7);

		dorsalFin3.rotateAngleX = -0.2733186F;
		dorsalFin3.rotateAngleY = 0F;
		dorsalFin3.rotateAngleZ = 0F;
		dorsalFin3.renderWithRotation(par7);

		dorsalFin4.rotateAngleX = -0.4553564F;
		dorsalFin4.rotateAngleY = 0F;
		dorsalFin4.rotateAngleZ = 0F;
		dorsalFin4.renderWithRotation(par7);

		dorsalFin5.rotateAngleX = -0.7285004F;
		dorsalFin5.rotateAngleY = 0F;
		dorsalFin5.rotateAngleZ = 0F;
		dorsalFin5.renderWithRotation(par7);
	}
}
