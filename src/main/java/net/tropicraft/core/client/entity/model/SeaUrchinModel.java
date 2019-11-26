package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.tropicraft.core.common.entity.underdasea.SeaUrchinEntity;
import org.lwjgl.opengl.GL11;

public class SeaUrchinModel extends EntityModel<SeaUrchinEntity> {
	public RendererModel base;
	public RendererModel top1;
	public RendererModel top2;
	public RendererModel front1;
	public RendererModel front2;
	public RendererModel left1;
	public RendererModel left2;
	public RendererModel back1;
	public RendererModel back2;
	public RendererModel right1;
	public RendererModel right2;
	public RendererModel bottom1;
	public RendererModel bottom2;
	public RendererModel spine;

	public SeaUrchinModel() {
		textureWidth = 64;
		textureHeight = 64;

		base = new RendererModel(this, 0, 0);
		base.addBox(-3F, 16F, -3F, 6, 6, 6);
		base.setRotationPoint(0F, 0F, 0F);
		base.setTextureSize(64, 64);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
		top1 = new RendererModel(this, 0, 38);
		top1.addBox(-2F, 15F, -2F, 4, 1, 4);
		top1.setRotationPoint(0F, 0F, 0F);
		top1.setTextureSize(64, 64);
		top1.mirror = true;
		setRotation(top1, 0F, 0F, 0F);
		top2 = new RendererModel(this, 16, 38);
		top2.addBox(-1F, 14F, -1F, 2, 1, 2);
		top2.setRotationPoint(0F, 0F, 0F);
		top2.setTextureSize(64, 64);
		top2.mirror = true;
		setRotation(top2, 0F, 0F, 0F);
		front1 = new RendererModel(this, 0, 12);
		front1.addBox(-2F, 17F, -4F, 4, 4, 1);
		front1.setRotationPoint(0F, 0F, 0F);
		front1.setTextureSize(64, 64);
		front1.mirror = true;
		setRotation(front1, 0F, 0F, 0F);
		front2 = new RendererModel(this, 10, 12);
		front2.addBox(-1F, 18F, -5F, 2, 2, 1);
		front2.setRotationPoint(0F, 0F, 0F);
		front2.setTextureSize(64, 64);
		front2.mirror = true;
		setRotation(front2, 0F, 0F, 0F);
		left1 = new RendererModel(this, 0, 17);
		left1.addBox(3F, 17F, -2F, 1, 4, 4);
		left1.setRotationPoint(0F, 0F, 0F);
		left1.setTextureSize(64, 64);
		left1.mirror = true;
		setRotation(left1, 0F, 0F, 0F);
		left2 = new RendererModel(this, 10, 17);
		left2.addBox(4F, 18F, -1F, 1, 2, 2);
		left2.setRotationPoint(0F, 0F, 0F);
		left2.setTextureSize(64, 64);
		left2.mirror = true;
		setRotation(left2, 0F, 0F, 0F);
		back1 = new RendererModel(this, 0, 25);
		back1.addBox(-2F, 17F, 3F, 4, 4, 1);
		back1.setRotationPoint(0F, 0F, 0F);
		back1.setTextureSize(64, 64);
		back1.mirror = true;
		setRotation(back1, 0F, 0F, 0F);
		back2 = new RendererModel(this, 10, 25);
		back2.addBox(-1F, 18F, 4F, 2, 2, 1);
		back2.setRotationPoint(0F, 0F, 0F);
		back2.setTextureSize(64, 64);
		back2.mirror = true;
		setRotation(back2, 0F, 0F, 0F);
		right1 = new RendererModel(this, 0, 30);
		right1.addBox(-4F, 17F, -2F, 1, 4, 4);
		right1.setRotationPoint(0F, 0F, 0F);
		right1.setTextureSize(64, 64);
		right1.mirror = true;
		setRotation(right1, 0F, 0F, 0F);
		right2 = new RendererModel(this, 10, 30);
		right2.addBox(-5F, 18F, -1F, 1, 2, 2);
		right2.setRotationPoint(0F, 0F, 0F);
		right2.setTextureSize(64, 64);
		right2.mirror = true;
		setRotation(right2, 0F, 0F, 0F);
		bottom1 = new RendererModel(this, 0, 38);
		bottom1.addBox(-2F, 22F, -2F, 4, 1, 4);
		bottom1.setRotationPoint(0F, 0F, 0F);
		bottom1.setTextureSize(64, 64);
		bottom1.mirror = true;
		setRotation(bottom1, 0F, 0F, 0F);
		bottom2 = new RendererModel(this, 16, 38);
		bottom2.addBox(-1F, 23F, -1F, 2, 1, 2);
		bottom2.setRotationPoint(0F, 0F, 0F);
		bottom2.setTextureSize(64, 64);
		bottom2.mirror = true;
		setRotation(bottom2, 0F, 0F, 0F);
		spine = new RendererModel(this, 24, 0);
		spine.addBox(-0.5F, -9F, -0.5F, 1, 6, 1);
		spine.setRotationPoint(0F, 19F, 0F);
		spine.setTextureSize(64, 64);
		spine.mirror = true;
		setRotation(spine, 0F, 0F, 0F);
	}

	@Override
    public void render(SeaUrchinEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(entity, f, f1, f2, f3, f4, f5);
		base.render(f5);
		top1.render(f5);
		top2.render(f5);
		front1.render(f5);
		front2.render(f5);
		left1.render(f5);
		left2.render(f5);
		back1.render(f5);
		back2.render(f5);
		right1.render(f5);
		right2.render(f5);
		bottom1.render(f5);
		bottom2.render(f5);
		
		int verticalSpines = 12;
		int horizontalSpines = 12;
		
		for (int v = 0; v < verticalSpines; v++) {
			for (int h = 0; h < horizontalSpines; h++) {
				GL11.glPushMatrix();
				GL11.glTranslatef(0f, 1.25f, 0f);
				GL11.glRotatef(360*((float)v)/verticalSpines, 0f, 0f, 1f);
				GL11.glRotatef(360*((float)h)/horizontalSpines, 1f, 0f, 0f);
				GL11.glTranslatef(0f, -0.4f, 0f);
				GL11.glScalef(0.33f, 1f, 0.33f);
				spine.render(f5);
				GL11.glPopMatrix();
			}
		}
	}

	private void setRotation(RendererModel model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(SeaUrchinEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.setRotationAngles(entity, f, f1, f2, f3, f4, f5);
	}

}