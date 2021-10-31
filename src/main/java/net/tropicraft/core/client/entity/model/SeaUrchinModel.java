package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.underdasea.SeaUrchinEntity;

public class SeaUrchinModel extends ListModel<SeaUrchinEntity> {
	private static final int VERTICAL_SPINES = 12;
	private static final int HORIZONTAL_SPINES = 12;

	public ModelPart base;
	public ModelPart top1;
	public ModelPart top2;
	public ModelPart front1;
	public ModelPart front2;
	public ModelPart left1;
	public ModelPart left2;
	public ModelPart back1;
	public ModelPart back2;
	public ModelPart right1;
	public ModelPart right2;
	public ModelPart bottom1;
	public ModelPart bottom2;
	public ModelPart spine;

	public SeaUrchinModel(ModelPart root) {
		//textureWidth = 64;
		//textureHeight = 64;

		base = root.getChild("base");
		top1 = root.getChild("top1");
		top2 = root.getChild("top2");
		front1 = root.getChild("front1");
		front2 = root.getChild("front2");
		left1 = root.getChild("left1");
		left2 = root.getChild("left2");
		back1 = root.getChild("back1");
		back2 = root.getChild("back2");
		right1 = root.getChild("right1");
		right2 = root.getChild("right2");
		bottom1 = root.getChild("bottom1");
		bottom2 = root.getChild("bottom2");
		spine = root.getChild("spine");


		/*
		base = new ModelPart(this, 0, 0);
		base.addCuboid(-3F, 16F, -3F, 6, 6, 6);
		base.setPivot(0F, 0F, 0F);
		base.setTextureSize(64, 64);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);

		top1 = new ModelPart(this, 0, 38);
		top1.addCuboid(-2F, 15F, -2F, 4, 1, 4);
		top1.setPivot(0F, 0F, 0F);
		top1.setTextureSize(64, 64);
		top1.mirror = true;
		setRotation(top1, 0F, 0F, 0F);

		top2 = new ModelPart(this, 16, 38);
		top2.addCuboid(-1F, 14F, -1F, 2, 1, 2);
		top2.setPivot(0F, 0F, 0F);
		top2.setTextureSize(64, 64);
		top2.mirror = true;
		setRotation(top2, 0F, 0F, 0F);

		front1 = new ModelPart(this, 0, 12);
		front1.addCuboid(-2F, 17F, -4F, 4, 4, 1);
		front1.setPivot(0F, 0F, 0F);
		front1.setTextureSize(64, 64);
		front1.mirror = true;
		setRotation(front1, 0F, 0F, 0F);

		front2 = new ModelPart(this, 10, 12);
		front2.addCuboid(-1F, 18F, -5F, 2, 2, 1);
		front2.setPivot(0F, 0F, 0F);
		front2.setTextureSize(64, 64);
		front2.mirror = true;
		setRotation(front2, 0F, 0F, 0F);

		left1 = new ModelPart(this, 0, 17);
		left1.addCuboid(3F, 17F, -2F, 1, 4, 4);
		left1.setPivot(0F, 0F, 0F);
		left1.setTextureSize(64, 64);
		left1.mirror = true;
		setRotation(left1, 0F, 0F, 0F);

		left2 = new ModelPart(this, 10, 17);
		left2.addCuboid(4F, 18F, -1F, 1, 2, 2);
		left2.setPivot(0F, 0F, 0F);
		left2.setTextureSize(64, 64);
		left2.mirror = true;
		setRotation(left2, 0F, 0F, 0F);

		back1 = new ModelPart(this, 0, 25);
		back1.addCuboid(-2F, 17F, 3F, 4, 4, 1);
		back1.setPivot(0F, 0F, 0F);
		back1.setTextureSize(64, 64);
		back1.mirror = true;
		setRotation(back1, 0F, 0F, 0F);

		back2 = new ModelPart(this, 10, 25);
		back2.addCuboid(-1F, 18F, 4F, 2, 2, 1);
		back2.setPivot(0F, 0F, 0F);
		back2.setTextureSize(64, 64);
		back2.mirror = true;
		setRotation(back2, 0F, 0F, 0F);

		right1 = new ModelPart(this, 0, 30);
		right1.addCuboid(-4F, 17F, -2F, 1, 4, 4);
		right1.setPivot(0F, 0F, 0F);
		right1.setTextureSize(64, 64);
		right1.mirror = true;
		setRotation(right1, 0F, 0F, 0F);

		right2 = new ModelPart(this, 10, 30);
		right2.addCuboid(-5F, 18F, -1F, 1, 2, 2);
		right2.setPivot(0F, 0F, 0F);
		right2.setTextureSize(64, 64);
		right2.mirror = true;
		setRotation(right2, 0F, 0F, 0F);

		bottom1 = new ModelPart(this, 0, 38);
		bottom1.addCuboid(-2F, 22F, -2F, 4, 1, 4);
		bottom1.setPivot(0F, 0F, 0F);
		bottom1.setTextureSize(64, 64);
		bottom1.mirror = true;
		setRotation(bottom1, 0F, 0F, 0F);

		bottom2 = new ModelPart(this, 16, 38);
		bottom2.addCuboid(-1F, 23F, -1F, 2, 1, 2);
		bottom2.setPivot(0F, 0F, 0F);
		bottom2.setTextureSize(64, 64);
		bottom2.mirror = true;
		setRotation(bottom2, 0F, 0F, 0F);

		spine = new ModelPart(this, 24, 0);
		spine.addCuboid(-0.5F, -9F, -0.5F, 1, 6, 1);
		spine.setPivot(0F, 19F, 0F);
		spine.setTextureSize(64, 64);
		spine.mirror = true;
		setRotation(spine, 0F, 0F, 0F);
		 */
	}

	public static LayerDefinition create() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();

		modelPartData.addOrReplaceChild("base",
				CubeListBuilder.create().mirror()
						.texOffs(0,0).addBox(-3F, 16F, -3F, 6, 6, 6),
				PartPose.offset(0F, 0F, 0F));

		modelPartData.addOrReplaceChild("top1",
				CubeListBuilder.create().mirror()
						.texOffs(0,38).addBox(-2F, 15F, -2F, 4, 1, 4),
				PartPose.offset(0F, 0F, 0F));

		modelPartData.addOrReplaceChild("top2",
				CubeListBuilder.create().mirror()
						.texOffs(16,38).addBox(-1F, 14F, -1F, 2, 1, 2),
				PartPose.offset(0F, 0F, 0F));

		modelPartData.addOrReplaceChild("front1",
				CubeListBuilder.create().mirror()
						.texOffs(0,12).addBox(-2F, 17F, -4F, 4, 4, 1),
				PartPose.offset(0F, 0F, 0F));

		modelPartData.addOrReplaceChild("front2",
				CubeListBuilder.create().mirror()
						.texOffs(10, 12).addBox(-1F, 18F, -5F, 2, 2, 1),
				PartPose.offset(0F, 0F, 0F));

		modelPartData.addOrReplaceChild("left1",
				CubeListBuilder.create().mirror()
						.texOffs(0,17).addBox(3F, 17F, -2F, 1, 4, 4),
				PartPose.offset(0F, 0F, 0F));

		modelPartData.addOrReplaceChild("left2",
				CubeListBuilder.create().mirror()
						.texOffs(10,17).addBox(4F, 18F, -1F, 1, 2, 2),
				PartPose.offset(0F, 0F, 0F));

		modelPartData.addOrReplaceChild("back1",
				CubeListBuilder.create().mirror()
						.texOffs(0,25).addBox(-2F, 17F, 3F, 4, 4, 1),
				PartPose.offset(0F, 0F, 0F));

		modelPartData.addOrReplaceChild("back2",
				CubeListBuilder.create().mirror()
						.texOffs(10,25).addBox(-1F, 18F, 4F, 2, 2, 1),
				PartPose.offset(0F, 0F, 0F));

		modelPartData.addOrReplaceChild("right1",
				CubeListBuilder.create().mirror()
						.texOffs(0,30).addBox(-4F, 17F, -2F, 1, 4, 4),
				PartPose.offset(0F, 0F, 0F));

		modelPartData.addOrReplaceChild("right2",
				CubeListBuilder.create().mirror()
						.texOffs(10,30).addBox(-5F, 18F, -1F, 1, 2, 2),
				PartPose.offset(0F, 0F, 0F));

		modelPartData.addOrReplaceChild("bottom1",
				CubeListBuilder.create().mirror()
						.texOffs(0,38).addBox(-2F, 22F, -2F, 4, 1, 4),
				PartPose.offset(0F, 0F, 0F));

		modelPartData.addOrReplaceChild("bottom2",
				CubeListBuilder.create().mirror()
						.texOffs(16,38).addBox(-1F, 23F, -1F, 2, 1, 2),
				PartPose.offset(0F, 0F, 0F));

		modelPartData.addOrReplaceChild("spine",
				CubeListBuilder.create().mirror()
						.texOffs(24, 0).addBox(-0.5F, -9F, -0.5F, 1, 6, 1),
				PartPose.offset(0F, 19F, 0F));

		return LayerDefinition.create(modelData,64,64);
	}

	@Override
	public void setupAnim(SeaUrchinEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// Yeah look it's a spiky ball okay
	}

	@Override
	public void renderToBuffer(PoseStack ms, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		parts().forEach((part) -> {
			part.render(ms, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		});

		for (int v = 0; v < VERTICAL_SPINES; v++) {
			for (int h = 0; h < HORIZONTAL_SPINES; h++) {
				ms.pushPose();
				ms.translate(0f, 1.25f, 0f);
				ms.mulPose(Vector3f.ZP.rotationDegrees(360 * ((float) v) / VERTICAL_SPINES));
				ms.mulPose(Vector3f.XP.rotationDegrees(360 * ((float) h) / HORIZONTAL_SPINES));
				ms.translate(0f, -0.4f, 0f);
				ms.scale(0.33f, 1f, 0.33f);
				spine.render(ms, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				ms.popPose();
			}
		}
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(
			base, top1, top2, front1, front2, left1, left2, back1, back2, right1, right2, bottom1, bottom2
		);
	}

	private void setRotation(ModelPart model, float x, float y, float z) {
		model.xRot = x;
		model.yRot = y;
		model.zRot = z;
	}
}
