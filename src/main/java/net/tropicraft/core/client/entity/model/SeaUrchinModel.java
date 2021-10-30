package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Vector3f;
import net.tropicraft.core.common.entity.underdasea.SeaUrchinEntity;

public class SeaUrchinModel extends SegmentedModel<SeaUrchinEntity> {
	private static final int VERTICAL_SPINES = 12;
	private static final int HORIZONTAL_SPINES = 12;

	public ModelRenderer base;
	public ModelRenderer top1;
	public ModelRenderer top2;
	public ModelRenderer front1;
	public ModelRenderer front2;
	public ModelRenderer left1;
	public ModelRenderer left2;
	public ModelRenderer back1;
	public ModelRenderer back2;
	public ModelRenderer right1;
	public ModelRenderer right2;
	public ModelRenderer bottom1;
	public ModelRenderer bottom2;
	public ModelRenderer spine;

	public SeaUrchinModel() {
		texWidth = 64;
		texHeight = 64;

		base = new ModelRenderer(this, 0, 0);
		base.addBox(-3F, 16F, -3F, 6, 6, 6);
		base.setPos(0F, 0F, 0F);
		base.setTexSize(64, 64);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
		top1 = new ModelRenderer(this, 0, 38);
		top1.addBox(-2F, 15F, -2F, 4, 1, 4);
		top1.setPos(0F, 0F, 0F);
		top1.setTexSize(64, 64);
		top1.mirror = true;
		setRotation(top1, 0F, 0F, 0F);
		top2 = new ModelRenderer(this, 16, 38);
		top2.addBox(-1F, 14F, -1F, 2, 1, 2);
		top2.setPos(0F, 0F, 0F);
		top2.setTexSize(64, 64);
		top2.mirror = true;
		setRotation(top2, 0F, 0F, 0F);
		front1 = new ModelRenderer(this, 0, 12);
		front1.addBox(-2F, 17F, -4F, 4, 4, 1);
		front1.setPos(0F, 0F, 0F);
		front1.setTexSize(64, 64);
		front1.mirror = true;
		setRotation(front1, 0F, 0F, 0F);
		front2 = new ModelRenderer(this, 10, 12);
		front2.addBox(-1F, 18F, -5F, 2, 2, 1);
		front2.setPos(0F, 0F, 0F);
		front2.setTexSize(64, 64);
		front2.mirror = true;
		setRotation(front2, 0F, 0F, 0F);
		left1 = new ModelRenderer(this, 0, 17);
		left1.addBox(3F, 17F, -2F, 1, 4, 4);
		left1.setPos(0F, 0F, 0F);
		left1.setTexSize(64, 64);
		left1.mirror = true;
		setRotation(left1, 0F, 0F, 0F);
		left2 = new ModelRenderer(this, 10, 17);
		left2.addBox(4F, 18F, -1F, 1, 2, 2);
		left2.setPos(0F, 0F, 0F);
		left2.setTexSize(64, 64);
		left2.mirror = true;
		setRotation(left2, 0F, 0F, 0F);
		back1 = new ModelRenderer(this, 0, 25);
		back1.addBox(-2F, 17F, 3F, 4, 4, 1);
		back1.setPos(0F, 0F, 0F);
		back1.setTexSize(64, 64);
		back1.mirror = true;
		setRotation(back1, 0F, 0F, 0F);
		back2 = new ModelRenderer(this, 10, 25);
		back2.addBox(-1F, 18F, 4F, 2, 2, 1);
		back2.setPos(0F, 0F, 0F);
		back2.setTexSize(64, 64);
		back2.mirror = true;
		setRotation(back2, 0F, 0F, 0F);
		right1 = new ModelRenderer(this, 0, 30);
		right1.addBox(-4F, 17F, -2F, 1, 4, 4);
		right1.setPos(0F, 0F, 0F);
		right1.setTexSize(64, 64);
		right1.mirror = true;
		setRotation(right1, 0F, 0F, 0F);
		right2 = new ModelRenderer(this, 10, 30);
		right2.addBox(-5F, 18F, -1F, 1, 2, 2);
		right2.setPos(0F, 0F, 0F);
		right2.setTexSize(64, 64);
		right2.mirror = true;
		setRotation(right2, 0F, 0F, 0F);
		bottom1 = new ModelRenderer(this, 0, 38);
		bottom1.addBox(-2F, 22F, -2F, 4, 1, 4);
		bottom1.setPos(0F, 0F, 0F);
		bottom1.setTexSize(64, 64);
		bottom1.mirror = true;
		setRotation(bottom1, 0F, 0F, 0F);
		bottom2 = new ModelRenderer(this, 16, 38);
		bottom2.addBox(-1F, 23F, -1F, 2, 1, 2);
		bottom2.setPos(0F, 0F, 0F);
		bottom2.setTexSize(64, 64);
		bottom2.mirror = true;
		setRotation(bottom2, 0F, 0F, 0F);
		spine = new ModelRenderer(this, 24, 0);
		spine.addBox(-0.5F, -9F, -0.5F, 1, 6, 1);
		spine.setPos(0F, 19F, 0F);
		spine.setTexSize(64, 64);
		spine.mirror = true;
		setRotation(spine, 0F, 0F, 0F);
	}

	@Override
	public void setupAnim(SeaUrchinEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// Yeah look it's a spiky ball okay
	}

	@Override
	public void renderToBuffer(MatrixStack ms, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
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
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(
			base, top1, top2, front1, front2, left1, left2, back1, back2, right1, right2, bottom1, bottom2
		);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.xRot = x;
		model.yRot = y;
		model.zRot = z;
	}
}
