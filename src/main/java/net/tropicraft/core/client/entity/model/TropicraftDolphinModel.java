package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.tropicraft.core.common.entity.underdasea.TropicraftDolphinEntity;

public class TropicraftDolphinModel extends SegmentedModel<TropicraftDolphinEntity> {
	ModelRenderer body1;
	ModelRenderer body2;
	ModelRenderer head1;
	ModelRenderer lowerJaw1;
	ModelRenderer lowerJaw2;
	ModelRenderer lowerJaw3;
	ModelRenderer lowerJaw4;
	ModelRenderer lowerJaw5;
	ModelRenderer upperJaw1;
	ModelRenderer upperJaw2;
	ModelRenderer upperJaw3;
	ModelRenderer upperJaw4;
	ModelRenderer upperJaw5;
	ModelRenderer head2;
	ModelRenderer head3;
	ModelRenderer head4;
	ModelRenderer head5;
	ModelRenderer body3;
	ModelRenderer rightPectoralFin1;
	ModelRenderer rightPectoralFin2;
	ModelRenderer rightPectoralFin3;
	ModelRenderer leftPectoralFin1;
	ModelRenderer leftPectoralFin2;
	ModelRenderer leftPectoralFin3;
	ModelRenderer tail1;
	ModelRenderer tail2;
	ModelRenderer tail3;
	ModelRenderer tail4;
	ModelRenderer fluke1;
	ModelRenderer fluke2;
	ModelRenderer fluke3;
	ModelRenderer fluke4;
	ModelRenderer fluke5;
	ModelRenderer fluke6;
	ModelRenderer fluke7;
	ModelRenderer fluke8;
	ModelRenderer dorsalFin1;
	ModelRenderer dorsalFin2;
	ModelRenderer dorsalFin3;
	ModelRenderer dorsalFin4;
	ModelRenderer dorsalFin5;

	public float tailVertSpeed = 1f;
	public float tailHorzSpeed = 1f;

	public TropicraftDolphinModel() {
		body1 = new ModelRenderer(this, 0, 0);
		body1.setTextureSize(64, 64);
		body1.addBox(-3F, -3F, -3F, 6, 6, 8);
		body1.setRotationPoint(0F, 20F, 0F);
		body2 = new ModelRenderer(this, 0, 14);
		body2.setTextureSize(64, 64);
		body2.addBox(-3F, -2F, -5F, 6, 5, 4);
		body2.setRotationPoint(0F, 19.8F, -2F);
		head1 = new ModelRenderer(this, 0, 57);
		head1.setTextureSize(64, 64);
		head1.addBox(-2.5F, -3F, -3F, 5, 4, 3);
		head1.setRotationPoint(0F, 21.4F, -6.3F);
		lowerJaw1 = new ModelRenderer(this, 16, 61);
		lowerJaw1.setTextureSize(64, 64);
		lowerJaw1.addBox(-2.5F, 0F, -1F, 5, 2, 1);
		lowerJaw1.setRotationPoint(0F, 20.4F, -9.3F);
		lowerJaw2 = new ModelRenderer(this, 29, 60);
		lowerJaw2.setTextureSize(64, 64);
		lowerJaw2.addBox(-2F, 0F, -3F, 4, 1, 3);
		lowerJaw2.setRotationPoint(0F, 21.4F, -10.3F);
		lowerJaw3 = new ModelRenderer(this, 29, 54);
		lowerJaw3.setTextureSize(64, 64);
		lowerJaw3.addBox(-2F, 0F, -3F, 4, 1, 3);
		lowerJaw3.setRotationPoint(0F, 20.4F, -10.3F);
		lowerJaw4 = new ModelRenderer(this, 44, 61);
		lowerJaw4.setTextureSize(64, 64);
		lowerJaw4.addBox(-1.5F, 0F, -2F, 3, 1, 2);
		lowerJaw4.setRotationPoint(0F, 21.4F, -13.3F);
		lowerJaw5 = new ModelRenderer(this, 45, 56);
		lowerJaw5.setTextureSize(64, 64);
		lowerJaw5.addBox(-1.5F, -1F, -1F, 3, 1, 1);
		lowerJaw5.setRotationPoint(0F, 22.4F, -15.3F);
		upperJaw1 = new ModelRenderer(this, 52, 0);
		upperJaw1.setTextureSize(64, 64);
		upperJaw1.addBox(-2.5F, 0F, -1F, 5, 1, 1);
		upperJaw1.setRotationPoint(0F, 19.4F, -9.3F);
		upperJaw2 = new ModelRenderer(this, 50, 3);
		upperJaw2.setTextureSize(64, 64);
		upperJaw2.addBox(-2F, 0F, -3F, 4, 1, 3);
		upperJaw2.setRotationPoint(0F, 19.4F, -10.3F);
		upperJaw3 = new ModelRenderer(this, 54, 8);
		upperJaw3.setTextureSize(64, 64);
		upperJaw3.addBox(-1.5F, -1F, -2F, 3, 1, 2);
		upperJaw3.setRotationPoint(0F, 21.36575F, -12.77706F);
		upperJaw4 = new ModelRenderer(this, 58, 12);
		upperJaw4.setTextureSize(64, 64);
		upperJaw4.addBox(-1F, -1F, -1F, 2, 1, 1);
		upperJaw4.setRotationPoint(0F, 21.36575F, -14.77706F);
		upperJaw5 = new ModelRenderer(this, 52, 15);
		upperJaw5.setTextureSize(64, 64);
		upperJaw5.addBox(-1F, 0F, -4F, 2, 1, 4);
		upperJaw5.setRotationPoint(0F, 19.74202F, -11.23969F);
		head2 = new ModelRenderer(this, 0, 49);
		head2.setTextureSize(64, 64);
		head2.addBox(-2F, -1F, -4F, 4, 2, 4);
		head2.setRotationPoint(0F, 18.4F, -6.3F);
		head3 = new ModelRenderer(this, 14, 49);
		head3.setTextureSize(64, 64);
		head3.addBox(-1.5F, 0F, -1F, 3, 2, 1);
		head3.setRotationPoint(0F, 17.99005F, -10.40267F);
		head4 = new ModelRenderer(this, 24, 49);
		head4.setTextureSize(64, 64);
		head4.addBox(-1.5F, 0F, -1F, 3, 2, 1);
		head4.setRotationPoint(0F, 18.43765F, -11.29691F);
		head5 = new ModelRenderer(this, 34, 49);
		head5.setTextureSize(64, 64);
		head5.addBox(-1.5F, 0F, -1F, 3, 1, 1);
		head5.setRotationPoint(0F, 19.10989F, -12.03724F);
		body3 = new ModelRenderer(this, 20, 14);
		body3.setTextureSize(64, 64);
		body3.addBox(-2.5F, 0F, -4.3F, 5, 1, 5);
		body3.setRotationPoint(0F, 17.1F, -2.5F);
		rightPectoralFin1 = new ModelRenderer(this, 0, 37);
		rightPectoralFin1.setTextureSize(64, 64);
		rightPectoralFin1.addBox(-3F, 0F, 0F, 3, 1, 3);
		rightPectoralFin1.setRotationPoint(-3F, 21.3F, -5F);
		rightPectoralFin2 = new ModelRenderer(this, 0, 41);
		rightPectoralFin2.setTextureSize(64, 64);
		rightPectoralFin2.addBox(-1F, 0F, 0F, 1, 1, 2);
		rightPectoralFin2.setRotationPoint(-5.104775F, 22.85859F, -3.227792F);
		rightPectoralFin3 = new ModelRenderer(this, 8, 42);
		rightPectoralFin3.setTextureSize(64, 64);
		rightPectoralFin3.addBox(-1F, 0F, 0F, 1, 1, 1);
		rightPectoralFin3.setRotationPoint(-5.521684F, 23.16731F, -1.912163F);

		leftPectoralFin1 = new ModelRenderer(this, 0, 37);
		leftPectoralFin1.setTextureSize(64, 64);
		leftPectoralFin1.addBox(0F, 0F, 0F, 3, 1, 3);
		leftPectoralFin1.setRotationPoint(3F, 21.3F, -5F);

		leftPectoralFin2 = new ModelRenderer(this, 0, 41);
		leftPectoralFin2.setTextureSize(64, 64);
		leftPectoralFin2.addBox(3, -0.1f, 0.5f, 1, 1, 2);
		leftPectoralFin2.setRotationPoint(3F, 21.3F, -5F);

		leftPectoralFin3 = new ModelRenderer(this, 8, 42);
		leftPectoralFin3.setTextureSize(64, 64);
		leftPectoralFin3.addBox(4, -0.15F, 0.5f, 1, 1, 1);
		leftPectoralFin3.setRotationPoint(3F, 21.3F, -5F);

		tail1 = new ModelRenderer(this, 0, 24);
		tail1.setTextureSize(64, 64);
		tail1.addBox(-2.5F, -2.5F, -1F, 5, 5, 7);
		tail1.setRotationPoint(0F, 19.8F, 5.1F);
		tail2 = new ModelRenderer(this, 24, 27);
		tail2.setTextureSize(64, 64);
		tail2.addBox(-2F, -2F, -1F, 4, 4, 5);
		tail2.setRotationPoint(0F, 20.07322F, 11.09378F);
		// Tail1.addChild(Tail2);
		tail3 = new ModelRenderer(this, 40, 24);
		tail3.setTextureSize(64, 64);
		tail3.addBox(-1.5F, -1.5F, -1F, 3, 3, 4);
		tail3.setRotationPoint(0F, 20.8163F, 15.02924F);
		tail4 = new ModelRenderer(this, 27, 30);
		tail4.setTextureSize(64, 64);
		tail4.addBox(-1F, -1F, 0F, 2, 2, 3);
		tail4.setRotationPoint(0F, 21.49112F, 17.43644F);
		fluke1 = new ModelRenderer(this, 44, 34);
		fluke1.setTextureSize(64, 64);
		fluke1.addBox(-3F, 0F, 0F, 6, 1, 1);
		fluke1.setRotationPoint(0F, 22.1683F, 19.21166F);
		fluke2 = new ModelRenderer(this, 43, 38);
		fluke2.setTextureSize(64, 64);
		fluke2.addBox(-4.5F, 0F, 0F, 9, 1, 1);
		fluke2.setRotationPoint(0F, 22.25945F, 20.2075F);
		fluke3 = new ModelRenderer(this, 30, 38);
		fluke3.setTextureSize(64, 64);
		fluke3.addBox(-5F, 0F, -1F, 5, 1, 1);
		fluke3.setRotationPoint(4.9F, 22.44176F, 22.19917F);
		fluke4 = new ModelRenderer(this, 14, 38);
		fluke4.setTextureSize(64, 64);
		fluke4.addBox(-5F, 0F, 0F, 6, 1, 1);
		fluke4.setRotationPoint(4.9F, 22.44176F, 22.19917F);
		fluke5 = new ModelRenderer(this, 30, 38);
		fluke5.setTextureSize(64, 64);
		fluke5.addBox(0F, 0F, -1F, 5, 1, 1);
		fluke5.setRotationPoint(-4.9F, 22.44176F, 22.19917F);
		fluke6 = new ModelRenderer(this, 14, 38);
		fluke6.setTextureSize(64, 64);
		fluke6.addBox(-1F, 0F, 0F, 6, 1, 1);
		fluke6.setRotationPoint(-4.9F, 22.44176F, 22.19917F);
		fluke7 = new ModelRenderer(this, 55, 30);
		fluke7.setTextureSize(64, 64);
		fluke7.addBox(-3F, 0F, 0F, 3, 1, 1);
		fluke7.setRotationPoint(0F, 22.43265F, 22.09959F);
		fluke8 = new ModelRenderer(this, 55, 30);
		fluke8.setTextureSize(64, 64);
		fluke8.addBox(0F, 0F, 0F, 3, 1, 1);
		fluke8.setRotationPoint(0F, 22.43265F, 22.09959F);
		dorsalFin1 = new ModelRenderer(this, 21, 0);
		dorsalFin1.setTextureSize(64, 64);
		dorsalFin1.addBox(-0.5F, -1F, -0.7F, 1, 1, 5);
		dorsalFin1.setRotationPoint(0F, 17.1F, 0F);
		dorsalFin2 = new ModelRenderer(this, 35, 0);
		dorsalFin2.setTextureSize(64, 64);
		dorsalFin2.addBox(-0.5F, -1F, 0F, 1, 1, 4);
		dorsalFin2.setRotationPoint(0F, 16.10415F, 0.09098025F);
		dorsalFin3 = new ModelRenderer(this, 30, 7);
		dorsalFin3.setTextureSize(64, 64);
		dorsalFin3.addBox(-0.5F, -1F, 0F, 1, 1, 3);
		dorsalFin3.setRotationPoint(0F, 15.30191F, 1.255631F);
		dorsalFin4 = new ModelRenderer(this, 39, 7);
		dorsalFin4.setTextureSize(64, 64);
		dorsalFin4.addBox(-0.5F, -1F, 0F, 1, 1, 2);
		dorsalFin4.setRotationPoint(0F, 14.60895F, 2.48844F);
		dorsalFin5 = new ModelRenderer(this, 45, 0);
		dorsalFin5.setTextureSize(64, 64);
		dorsalFin5.addBox(-0.5F, -1F, 0F, 1, 1, 1);
		dorsalFin5.setRotationPoint(0F, 14.15063F, 3.826327F);
	}

	@Override
	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(
			body1, body2, head1, lowerJaw1, lowerJaw2, lowerJaw3,
			lowerJaw4, lowerJaw5, upperJaw1, upperJaw2, upperJaw3,
			upperJaw4, upperJaw5, head2, head3, head4, head5,
			body3, rightPectoralFin1, rightPectoralFin2, rightPectoralFin3,
			leftPectoralFin1, leftPectoralFin2, leftPectoralFin3,
			tail1, tail2, tail3, tail4, fluke1, fluke2, fluke3,
			fluke4, fluke5, fluke6, fluke7, fluke8, dorsalFin1,
			dorsalFin2, dorsalFin3, dorsalFin4, dorsalFin5
		);
	}

	@Override
	public void setRotationAngles(TropicraftDolphinEntity dolphin, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		final boolean mouthOpen = dolphin.getMouthOpen();

		if (dolphin.isInWater()) {
			tailVertSpeed = 0.5f / 2;
			tailHorzSpeed = 0.25f / 2;
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

		lowerJaw3.rotateAngleX = 0.3490658F;
		if (mouthOpen) {
			lowerJaw5.setRotationPoint(0F, 23.4F, -15.3F+0.52f);
			lowerJaw4.rotateAngleX = 0.5F;
		} else {
			lowerJaw5.setRotationPoint(0F, 22.4F, -15.3F);
			lowerJaw4.rotateAngleX = 0F;
		}

		lowerJaw5.rotateAngleX = -0.2275909F;
		upperJaw2.rotateAngleX = 0.3490658F;
		upperJaw4.rotateAngleX = -0.09110618F;
		upperJaw5.rotateAngleX = 0.15132F;
		head2.rotateAngleX = 0.1453859F;
		head3.rotateAngleX = 0.4640831F;
		head4.rotateAngleX = 0.737227F;
		head5.rotateAngleX = 1.055924F;
		body3.rotateAngleX = 0.04555309F;

		rightPectoralFin1.rotateAngleX = 0.1612329F;
		rightPectoralFin1.rotateAngleY = 0.2214468F;
		rightPectoralFin1.rotateAngleZ = -0.6194302F+ (float) (Math.sin(ageInTicks * .025F)) * .3f;

		rightPectoralFin2.rotateAngleX = 0.2393862F;
		rightPectoralFin2.rotateAngleY = 0.3358756F;
		rightPectoralFin2.rotateAngleZ = -0.5966207F+ (float) (Math.sin(ageInTicks * .025F)) * .45f;

		rightPectoralFin3.rotateAngleX = 0.3620028F;
		rightPectoralFin3.rotateAngleY = 0.5368112F;
		rightPectoralFin3.rotateAngleZ = -0.5368112F+ (float) (Math.sin(ageInTicks * .025F)) * .5f;

		leftPectoralFin1.rotateAngleX = 0.1612329F;
		leftPectoralFin1.rotateAngleY = -0.2214468F;
		leftPectoralFin1.rotateAngleZ = 0.6194302F + (float) (Math.sin(ageInTicks * .025F)) * .3f;

		leftPectoralFin2.rotateAngleX = 0.2393862F;
		leftPectoralFin2.rotateAngleY = -0.3358756F;
		leftPectoralFin2.rotateAngleZ = 0.5966207F + (float) (Math.sin(ageInTicks * .025F)) * .35f;

		leftPectoralFin3.rotateAngleX = 0.3620028F;
		leftPectoralFin3.rotateAngleY = -0.5368112F;
		leftPectoralFin3.rotateAngleZ = 0.5368112F + (float) (Math.sin(ageInTicks * .025F)) * .4f;

		tail1.rotateAngleX = -0.04555309F + (float) (Math.sin(ageInTicks * tailVertSpeed)) * .1f;
		tail1.rotateAngleY = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * .135F;
		tail1.rotateAngleZ = 0F;

		tail2.rotationPointX = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * 1;

		tail2.rotationPointY = 20 - (float) (Math.sin(ageInTicks * tailVertSpeed)) * 0.8F;

		tail2.rotateAngleX = -0.1366593F + (float) (Math.sin(ageInTicks * tailVertSpeed)) * .1f;
		tail2.rotateAngleY = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * .135F;
		tail2.rotateAngleZ = 0F;

		tail3.rotationPointX = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * 1.85f;

		tail3.rotationPointY = 20.5f - (float) (Math.sin(ageInTicks * tailVertSpeed)) * 1.5F;

		tail3.rotateAngleX = -0.2733185F + (float) (Math.sin(ageInTicks * tailVertSpeed)) * .2f;
		tail3.rotateAngleY = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * .135F;
		tail3.rotateAngleZ = 0F;

		tail4.rotationPointX = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * 2.4f;
		tail4.rotationPointY = 21.5f - (float) (Math.sin(ageInTicks * tailVertSpeed)) * 2.5F;

		tail4.rotateAngleX = -0.3644247F + (float) (Math.sin(ageInTicks * tailVertSpeed)) * .5f;
		tail4.rotateAngleY = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * .35F;
		tail4.rotateAngleZ = 0F;

		fluke1.rotationPointX = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * 2.8f;
		fluke1.rotationPointY = 22f - (float) (Math.sin(ageInTicks * tailVertSpeed)) * 4F;

		fluke1.rotateAngleX = -0.09128072F;
		fluke1.rotateAngleY = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * .35F;
		fluke1.rotateAngleZ = 0F;

		fluke2.rotationPointY = 22f - (float) (Math.sin(ageInTicks * tailVertSpeed)) * 4F;

		fluke2.rotationPointX = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * 2.8f;

		fluke2.rotateAngleX = -0.09128071F;
		fluke2.rotateAngleY = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * .35F;
		fluke2.rotateAngleZ = 0F;

		fluke3.rotationPointX = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * 2.8f;
		fluke3.rotationPointY = 22f - (float) (Math.sin(ageInTicks * tailVertSpeed)) * 4F;

		fluke3.rotateAngleX = -0.09118575F;
		fluke3.rotateAngleY = -0.04574326F + (float) (Math.sin(ageInTicks * tailHorzSpeed)) * .35F;
		fluke3.rotateAngleZ = 0.00416824F;

		fluke4.rotationPointY = 22f - (float) (Math.sin(ageInTicks * tailVertSpeed)) * 4F;

		fluke4.rotationPointX = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * 2.8f;

		fluke4.rotateAngleX = -0.08892051F + (float) (Math.sin(ageInTicks * tailVertSpeed)) * .8f;

		fluke4.rotateAngleY = -0.2285096F + (float) (Math.sin(ageInTicks * tailHorzSpeed)) * .35F;
		fluke4.rotateAngleZ = 0.02065023F;

		fluke5.rotationPointX = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * 2.8f;
		fluke5.rotationPointY = 22f - (float) (Math.sin(ageInTicks * tailVertSpeed)) * 4F;

		fluke5.rotateAngleX = -0.09118575F;
		fluke5.rotateAngleY = 0.04574326F + (float) (Math.sin(ageInTicks * tailHorzSpeed)) * .35F;
		fluke5.rotateAngleZ = -0.00416824F;

		fluke6.rotationPointX = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * 2.8f;
		fluke6.rotationPointY = 22f - (float) (Math.sin(ageInTicks * tailVertSpeed)) * 4F;

		fluke6.rotateAngleX = -0.08892051F + (float) (Math.sin(ageInTicks * tailVertSpeed)) * .8f;
		fluke6.rotateAngleY = 0.2285096F + (float) (Math.sin(ageInTicks * tailHorzSpeed)) * .35F;
		fluke6.rotateAngleZ = -0.02065023F;

		fluke7.rotationPointX = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * 2.8f;
		fluke7.rotationPointY = 22f - (float) (Math.sin(ageInTicks * tailVertSpeed)) * 4F;

		fluke7.rotateAngleX = -0.09042732F + (float) (Math.sin(ageInTicks * tailVertSpeed)) * .8f;
		fluke7.rotateAngleY = -0.1372235F + (float) (Math.sin(ageInTicks * tailHorzSpeed)) * .35F;
		fluke7.rotateAngleZ = 0.01246957F;

		fluke8.rotationPointX = (float) (Math.sin(ageInTicks * tailHorzSpeed)) * 2.8f;
		fluke8.rotationPointY = 22f - (float) (Math.sin(ageInTicks * tailVertSpeed)) * 4F;

		fluke8.rotateAngleX = -0.09042732F + (float) (Math.sin(ageInTicks * tailVertSpeed)) * .8f;

		fluke8.rotateAngleY = 0.1372235F + (float) (Math.sin(ageInTicks * tailHorzSpeed)) * .35F;
		fluke8.rotateAngleZ = -0.01246957F;

		dorsalFin1.rotateAngleX = -0.09110619F;
		dorsalFin2.rotateAngleX = -0.1822124F;
		dorsalFin3.rotateAngleX = -0.2733186F;
		dorsalFin4.rotateAngleX = -0.4553564F;
		dorsalFin5.rotateAngleX = -0.7285004F;
	}
}
