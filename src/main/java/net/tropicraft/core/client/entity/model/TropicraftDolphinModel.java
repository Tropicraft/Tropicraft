package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.underdasea.TropicraftDolphinEntity;

public class TropicraftDolphinModel extends HierarchicalModel<TropicraftDolphinEntity> {
	private final ModelPart root;
	private final ModelPart body1;
	private final ModelPart body2;
	private final ModelPart head1;
	private final ModelPart lowerJaw1;
	private final ModelPart lowerJaw2;
	private final ModelPart lowerJaw3;
	private final ModelPart lowerJaw4;
	private final ModelPart lowerJaw5;
	private final ModelPart upperJaw1;
	private final ModelPart upperJaw2;
	private final ModelPart upperJaw3;
	private final ModelPart upperJaw4;
	private final ModelPart upperJaw5;
	private final ModelPart head2;
	private final ModelPart head3;
	private final ModelPart head4;
	private final ModelPart head5;
	private final ModelPart body3;
	private final ModelPart rightPectoralFin1;
	private final ModelPart rightPectoralFin2;
	private final ModelPart rightPectoralFin3;
	private final ModelPart leftPectoralFin1;
	private final ModelPart leftPectoralFin2;
	private final ModelPart leftPectoralFin3;
	private final ModelPart tail1;
	private final ModelPart tail2;
	private final ModelPart tail3;
	private final ModelPart tail4;
	private final ModelPart fluke1;
	private final ModelPart fluke2;
	private final ModelPart fluke3;
	private final ModelPart fluke4;
	private final ModelPart fluke5;
	private final ModelPart fluke6;
	private final ModelPart fluke7;
	private final ModelPart fluke8;
	private final ModelPart dorsalFin1;
	private final ModelPart dorsalFin2;
	private final ModelPart dorsalFin3;
	private final ModelPart dorsalFin4;
	private final ModelPart dorsalFin5;

	public TropicraftDolphinModel(ModelPart root) {
		this.root = root;
		body1 = root.getChild("body1");
		body2 = root.getChild("body2");
		head1 = root.getChild("head1");
		lowerJaw1 = root.getChild("lowerJaw1");
		lowerJaw2 = root.getChild("lowerJaw2");
		lowerJaw3 = root.getChild("lowerJaw3");
		lowerJaw4 = root.getChild("lowerJaw4");
		lowerJaw5 = root.getChild("lowerJaw5");
		upperJaw1 = root.getChild("upperJaw1");
		upperJaw2 = root.getChild("upperJaw2");
		upperJaw3 = root.getChild("upperJaw3");
		upperJaw4 = root.getChild("upperJaw4");
		upperJaw5 = root.getChild("upperJaw5");
		head2 = root.getChild("head2");
		head3 = root.getChild("head3");
		head4 = root.getChild("head4");
		head5 = root.getChild("head5");
		body3 = root.getChild("body3");
		rightPectoralFin1 = root.getChild("rightPectoralFin1");
		rightPectoralFin2 = root.getChild("rightPectoralFin2");
		rightPectoralFin3 = root.getChild("rightPectoralFin3");
		leftPectoralFin1 = root.getChild("leftPectoralFin1");
		leftPectoralFin2 = root.getChild("leftPectoralFin2");
		leftPectoralFin3 = root.getChild("leftPectoralFin3");
		tail1 = root.getChild("tail1");
		tail2 = root.getChild("tail2");
		tail3 = root.getChild("tail3");
		tail4 = root.getChild("tail4");
		fluke1 = root.getChild("fluke1");
		fluke2 = root.getChild("fluke2");
		fluke3 = root.getChild("fluke3");
		fluke4 = root.getChild("fluke4");
		fluke5 = root.getChild("fluke5");
		fluke6 = root.getChild("fluke6");
		fluke7 = root.getChild("fluke7");
		fluke8 = root.getChild("fluke8");
		dorsalFin1 = root.getChild("dorsalFin1");
		dorsalFin2 = root.getChild("dorsalFin2");
		dorsalFin3 = root.getChild("dorsalFin3");
		dorsalFin4 = root.getChild("dorsalFin4");
		dorsalFin5 = root.getChild("dorsalFin5");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		root.addOrReplaceChild("body1",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-3F, -3F, -3F, 6, 6, 8),
				PartPose.offset(0F, 20F, 0F));

		root.addOrReplaceChild("body2",
				CubeListBuilder.create().texOffs(0, 14)
						.addBox(-3F, -2F, -5F, 6, 5, 4),
				PartPose.offset(0F, 19.8F, -2F));

		root.addOrReplaceChild("head1",
				CubeListBuilder.create().texOffs(0, 57)
						.addBox(-2.5F, -3F, -3F, 5, 4, 3),
				PartPose.offset(0F, 21.4F, -6.3F));

		root.addOrReplaceChild("lowerJaw1",
				CubeListBuilder.create().texOffs(16, 61)
						.addBox(-2.5F, 0F, -1F, 5, 2, 1),
				PartPose.offset(0F, 20.4F, -9.3F));

		root.addOrReplaceChild("lowerJaw2",
				CubeListBuilder.create().texOffs(29, 60)
						.addBox(-2F, 0F, -3F, 4, 1, 3),
				PartPose.offset(0F, 21.4F, -10.3F));

		root.addOrReplaceChild("lowerJaw3",
				CubeListBuilder.create().texOffs(29, 54)
						.addBox(-2F, 0F, -3F, 4, 1, 3),
				PartPose.offset(0F, 20.4F, -10.3F));

		root.addOrReplaceChild("lowerJaw4",
				CubeListBuilder.create().texOffs(44, 61)
						.addBox(-1.5F, 0F, -2F, 3, 1, 2),
				PartPose.offset(0F, 21.4F, -13.3F));

		root.addOrReplaceChild("lowerJaw5",
				CubeListBuilder.create().texOffs(45, 56)
						.addBox(-1.5F, -1F, -1F, 3, 1, 1),
				PartPose.offset(0F, 22.4F, -15.3F));

		root.addOrReplaceChild("upperJaw1",
				CubeListBuilder.create().texOffs(52, 0)
						.addBox(-2.5F, 0F, -1F, 5, 1, 1),
				PartPose.offset(0F, 19.4F, -9.3F));

		root.addOrReplaceChild("upperJaw2",
				CubeListBuilder.create().texOffs(50, 3)
						.addBox(-2F, 0F, -3F, 4, 1, 3),
				PartPose.offset(0F, 19.4F, -10.3F));

		root.addOrReplaceChild("upperJaw3",
				CubeListBuilder.create().texOffs(54, 8)
						.addBox(-1.5F, -1F, -2F, 3, 1, 2),
				PartPose.offset(0F, 21.36575F, -12.77706F));

		root.addOrReplaceChild("upperJaw4",
				CubeListBuilder.create().texOffs(58, 12)
						.addBox(-1F, -1F, -1F, 2, 1, 1),
				PartPose.offset(0F, 21.36575F, -14.77706F));

		root.addOrReplaceChild("upperJaw5",
				CubeListBuilder.create().texOffs(52, 15)
						.addBox(-1F, 0F, -4F, 2, 1, 4),
				PartPose.offset(0F, 19.74202F, -11.23969F));

		root.addOrReplaceChild("head2",
				CubeListBuilder.create().texOffs(0, 49)
						.addBox(-2F, -1F, -4F, 4, 2, 4),
				PartPose.offset(0F, 18.4F, -6.3F));

		root.addOrReplaceChild("head3",
				CubeListBuilder.create().texOffs(14, 49)
						.addBox(-1.5F, 0F, -1F, 3, 2, 1),
				PartPose.offset(0F, 17.99005F, -10.40267F));

		root.addOrReplaceChild("head4",
				CubeListBuilder.create().texOffs(24, 49)
						.addBox(-1.5F, 0F, -1F, 3, 2, 1),
				PartPose.offset(0F, 18.43765F, -11.29691F));

		root.addOrReplaceChild("head5",
				CubeListBuilder.create().texOffs(34, 49)
						.addBox(-1.5F, 0F, -1F, 3, 1, 1),
				PartPose.offset(0F, 19.10989F, -12.03724F));

		root.addOrReplaceChild("body3",
				CubeListBuilder.create().texOffs(20, 14)
						.addBox(-2.5F, 0F, -4.3F, 5, 1, 5),
				PartPose.offset(0F, 17.1F, -2.5F));

		root.addOrReplaceChild("rightPectoralFin1",
				CubeListBuilder.create().texOffs(0, 37)
						.addBox(-3F, 0F, 0F, 3, 1, 3),
				PartPose.offset(-3F, 21.3F, -5F));

		root.addOrReplaceChild("rightPectoralFin2",
				CubeListBuilder.create().texOffs(0, 41)
						.addBox(-1F, 0F, 0F, 1, 1, 2),
				PartPose.offset(-5.104775F, 22.85859F, -3.227792F));

		root.addOrReplaceChild("rightPectoralFin3",
				CubeListBuilder.create().texOffs(8, 42)
						.addBox(-1F, 0F, 0F, 1, 1, 1),
				PartPose.offset(-5.521684F, 23.16731F, -1.912163F));

		root.addOrReplaceChild("leftPectoralFin1",
				CubeListBuilder.create().texOffs(0, 37)
						.addBox(0F, 0F, 0F, 3, 1, 3),
				PartPose.offset(3F, 21.3F, -5F));

		root.addOrReplaceChild("leftPectoralFin2",
				CubeListBuilder.create().texOffs(0, 41)
						.addBox(3, -0.1f, 0.5f, 1, 1, 2),
				PartPose.offset(3F, 21.3F, -5F));

		root.addOrReplaceChild("leftPectoralFin3",
				CubeListBuilder.create().texOffs(8, 42)
						.addBox(4, -0.15F, 0.5f, 1, 1, 1),
				PartPose.offset(3F, 21.3F, -5F));

		root.addOrReplaceChild("tail1",
				CubeListBuilder.create().texOffs(0, 24)
						.addBox(-2.5F, -2.5F, -1F, 5, 5, 7),
				PartPose.offset(0F, 19.8F, 5.1F));

		root.addOrReplaceChild("tail2",
				CubeListBuilder.create().texOffs(24, 27)
						.addBox(-2F, -2F, -1F, 4, 4, 5),
				PartPose.offset(0F, 20.07322F, 11.09378F));

		root.addOrReplaceChild("tail3",
				CubeListBuilder.create().texOffs(40, 24)
						.addBox(-1.5F, -1.5F, -1F, 3, 3, 4),
				PartPose.offset(0F, 20.8163F, 15.02924F));

		root.addOrReplaceChild("tail4",
				CubeListBuilder.create().texOffs(27, 30)
						.addBox(-1F, -1F, 0F, 2, 2, 3),
				PartPose.offset(0F, 21.49112F, 17.43644F));

		root.addOrReplaceChild("fluke1",
				CubeListBuilder.create().texOffs(44, 34)
						.addBox(-3F, 0F, 0F, 6, 1, 1),
				PartPose.offset(0F, 22.1683F, 19.21166F));

		root.addOrReplaceChild("fluke2",
				CubeListBuilder.create().texOffs(43, 38)
						.addBox(-4.5F, 0F, 0F, 9, 1, 1),
				PartPose.offset(0F, 22.25945F, 20.2075F));

		root.addOrReplaceChild("fluke3",
				CubeListBuilder.create().texOffs(30, 38)
						.addBox(-5F, 0F, -1F, 5, 1, 1),
				PartPose.offset(4.9F, 22.44176F, 22.19917F));

		root.addOrReplaceChild("fluke4",
				CubeListBuilder.create().texOffs(14, 38)
						.addBox(-5F, 0F, 0F, 6, 1, 1),
				PartPose.offset(4.9F, 22.44176F, 22.19917F));

		root.addOrReplaceChild("fluke5",
				CubeListBuilder.create().texOffs(30, 38)
						.addBox(0F, 0F, -1F, 5, 1, 1),
				PartPose.offset(-4.9F, 22.44176F, 22.19917F));

		root.addOrReplaceChild("fluke6",
				CubeListBuilder.create().texOffs(14, 38)
						.addBox(-1F, 0F, 0F, 6, 1, 1),
				PartPose.offset(-4.9F, 22.44176F, 22.19917F));

		root.addOrReplaceChild("fluke7",
				CubeListBuilder.create().texOffs(55, 30)
						.addBox(-3F, 0F, 0F, 3, 1, 1),
				PartPose.offset(0F, 22.43265F, 22.09959F));

		root.addOrReplaceChild("fluke8",
				CubeListBuilder.create().texOffs(55, 30)
						.addBox(0F, 0F, 0F, 3, 1, 1),
				PartPose.offset(0F, 22.43265F, 22.09959F));

		root.addOrReplaceChild("dorsalFin1",
				CubeListBuilder.create().texOffs(21, 0)
						.addBox(-0.5F, -1F, -0.7F, 1, 1, 5),
				PartPose.offset(0F, 17.1F, 0F));

		root.addOrReplaceChild("dorsalFin2",
				CubeListBuilder.create().texOffs(35, 0)
						.addBox(-0.5F, -1F, 0F, 1, 1, 4),
				PartPose.offset(0F, 16.10415F, 0.09098025F));

		root.addOrReplaceChild("dorsalFin3",
				CubeListBuilder.create().texOffs(30, 7)
						.addBox(-0.5F, -1F, 0F, 1, 1, 3),
				PartPose.offset(0F, 15.30191F, 1.255631F));

		root.addOrReplaceChild("dorsalFin4",
				CubeListBuilder.create().texOffs(39, 7)
						.addBox(-0.5F, -1F, 0F, 1, 1, 2),
				PartPose.offset(0F, 14.60895F, 2.48844F));

		root.addOrReplaceChild("dorsalFin5",
				CubeListBuilder.create().texOffs(45, 0)
						.addBox(-0.5F, -1F, 0F, 1, 1, 1),
				PartPose.offset(0F, 14.15063F, 3.826327F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public ModelPart root() {
		return root;
	}

	@Override
	public void setupAnim(TropicraftDolphinEntity dolphin, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		final boolean mouthOpen = dolphin.getMouthOpen();

		float tailVertSpeed = 1.0f;
		float tailHorzSpeed;
		if (dolphin.isInWater()) {
			tailVertSpeed = 0.5f / 2;
			tailHorzSpeed = 0.25f / 2;
			if (dolphin.getAirSupply() <= 0) {
				tailVertSpeed = 0.5f;
				tailHorzSpeed = 0.25f;
			}
		} else {
			if (dolphin.onGround()) {
				tailVertSpeed = 0.0f;
				tailHorzSpeed = 0.05f;
			} else {
				tailHorzSpeed = 0.5f;
			}
		}

		lowerJaw3.xRot = 0.3490658F;
		if (mouthOpen) {
			lowerJaw5.setPos(0F, 23.4F, -15.3F+0.52f);
			lowerJaw4.xRot = 0.5F;
		} else {
			lowerJaw5.setPos(0F, 22.4F, -15.3F);
			lowerJaw4.xRot = 0F;
		}

		lowerJaw5.xRot = -0.2275909F;
		upperJaw2.xRot = 0.3490658F;
		upperJaw4.xRot = -0.09110618F;
		upperJaw5.xRot = 0.15132F;
		head2.xRot = 0.1453859F;
		head3.xRot = 0.4640831F;
		head4.xRot = 0.737227F;
		head5.xRot = 1.055924F;
		body3.xRot = 0.04555309F;

		rightPectoralFin1.xRot = 0.1612329F;
		rightPectoralFin1.yRot = 0.2214468F;
		rightPectoralFin1.zRot = -0.6194302F+ Mth.sin(ageInTicks * .025F) * .3f;

		rightPectoralFin2.xRot = 0.2393862F;
		rightPectoralFin2.yRot = 0.3358756F;
		rightPectoralFin2.zRot = -0.5966207F+ Mth.sin(ageInTicks * .025F) * .45f;

		rightPectoralFin3.xRot = 0.3620028F;
		rightPectoralFin3.yRot = 0.5368112F;
		rightPectoralFin3.zRot = -0.5368112F+ Mth.sin(ageInTicks * .025F) * .5f;

		leftPectoralFin1.xRot = 0.1612329F;
		leftPectoralFin1.yRot = -0.2214468F;
		leftPectoralFin1.zRot = 0.6194302F + Mth.sin(ageInTicks * .025F) * .3f;

		leftPectoralFin2.xRot = 0.2393862F;
		leftPectoralFin2.yRot = -0.3358756F;
		leftPectoralFin2.zRot = 0.5966207F + Mth.sin(ageInTicks * .025F) * .35f;

		leftPectoralFin3.xRot = 0.3620028F;
		leftPectoralFin3.yRot = -0.5368112F;
		leftPectoralFin3.zRot = 0.5368112F + Mth.sin(ageInTicks * .025F) * .4f;

		tail1.xRot = -0.04555309F + Mth.sin(ageInTicks * tailVertSpeed) * .1f;
		tail1.yRot = Mth.sin(ageInTicks * tailHorzSpeed) * .135F;
		tail1.zRot = 0F;

		tail2.x = Mth.sin(ageInTicks * tailHorzSpeed) * 1;

		tail2.y = 20 - Mth.sin(ageInTicks * tailVertSpeed) * 0.8F;

		tail2.xRot = -0.1366593F + Mth.sin(ageInTicks * tailVertSpeed) * .1f;
		tail2.yRot = Mth.sin(ageInTicks * tailHorzSpeed) * .135F;
		tail2.zRot = 0F;

		tail3.x = Mth.sin(ageInTicks * tailHorzSpeed) * 1.85f;

		tail3.y = 20.5f - Mth.sin(ageInTicks * tailVertSpeed) * 1.5F;

		tail3.xRot = -0.2733185F + Mth.sin(ageInTicks * tailVertSpeed) * .2f;
		tail3.yRot = Mth.sin(ageInTicks * tailHorzSpeed) * .135F;
		tail3.zRot = 0F;

		tail4.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.4f;
		tail4.y = 21.5f - Mth.sin(ageInTicks * tailVertSpeed) * 2.5F;

		tail4.xRot = -0.3644247F + Mth.sin(ageInTicks * tailVertSpeed) * .5f;
		tail4.yRot = Mth.sin(ageInTicks * tailHorzSpeed) * .35F;
		tail4.zRot = 0F;

		fluke1.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;
		fluke1.y = 22f - Mth.sin(ageInTicks * tailVertSpeed) * 4F;

		fluke1.xRot = -0.09128072F;
		fluke1.yRot = Mth.sin(ageInTicks * tailHorzSpeed) * .35F;
		fluke1.zRot = 0F;

		fluke2.y = 22f - Mth.sin(ageInTicks * tailVertSpeed) * 4F;

		fluke2.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;

		fluke2.xRot = -0.09128071F;
		fluke2.yRot = Mth.sin(ageInTicks * tailHorzSpeed) * .35F;
		fluke2.zRot = 0F;

		fluke3.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;
		fluke3.y = 22f - Mth.sin(ageInTicks * tailVertSpeed) * 4F;

		fluke3.xRot = -0.09118575F;
		fluke3.yRot = -0.04574326F + Mth.sin(ageInTicks * tailHorzSpeed) * .35F;
		fluke3.zRot = 0.00416824F;

		fluke4.y = 22f - Mth.sin(ageInTicks * tailVertSpeed) * 4F;

		fluke4.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;

		fluke4.xRot = -0.08892051F + Mth.sin(ageInTicks * tailVertSpeed) * .8f;

		fluke4.yRot = -0.2285096F + Mth.sin(ageInTicks * tailHorzSpeed) * .35F;
		fluke4.zRot = 0.02065023F;

		fluke5.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;
		fluke5.y = 22f - Mth.sin(ageInTicks * tailVertSpeed) * 4F;

		fluke5.xRot = -0.09118575F;
		fluke5.yRot = 0.04574326F + Mth.sin(ageInTicks * tailHorzSpeed) * .35F;
		fluke5.zRot = -0.00416824F;

		fluke6.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;
		fluke6.y = 22f - Mth.sin(ageInTicks * tailVertSpeed) * 4F;

		fluke6.xRot = -0.08892051F + Mth.sin(ageInTicks * tailVertSpeed) * .8f;
		fluke6.yRot = 0.2285096F + Mth.sin(ageInTicks * tailHorzSpeed) * .35F;
		fluke6.zRot = -0.02065023F;

		fluke7.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;
		fluke7.y = 22f - Mth.sin(ageInTicks * tailVertSpeed) * 4F;

		fluke7.xRot = -0.09042732F + Mth.sin(ageInTicks * tailVertSpeed) * .8f;
		fluke7.yRot = -0.1372235F + Mth.sin(ageInTicks * tailHorzSpeed) * .35F;
		fluke7.zRot = 0.01246957F;

		fluke8.x = Mth.sin(ageInTicks * tailHorzSpeed) * 2.8f;
		fluke8.y = 22f - Mth.sin(ageInTicks * tailVertSpeed) * 4F;

		fluke8.xRot = -0.09042732F + Mth.sin(ageInTicks * tailVertSpeed) * .8f;

		fluke8.yRot = 0.1372235F + Mth.sin(ageInTicks * tailHorzSpeed) * .35F;
		fluke8.zRot = -0.01246957F;

		dorsalFin1.xRot = -0.09110619F;
		dorsalFin2.xRot = -0.1822124F;
		dorsalFin3.xRot = -0.2733186F;
		dorsalFin4.xRot = -0.4553564F;
		dorsalFin5.xRot = -0.7285004F;
	}
}
