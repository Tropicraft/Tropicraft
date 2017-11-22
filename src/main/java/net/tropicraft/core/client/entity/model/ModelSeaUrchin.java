package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

public class ModelSeaUrchin extends ModelBase {
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

	public ModelSeaUrchin() {
		textureWidth = 64;
		textureHeight = 64;

		base = new ModelRenderer(this, 0, 0);
		base.addBox(-3F, 16F, -3F, 6, 6, 6);
		base.setRotationPoint(0F, 0F, 0F);
		base.setTextureSize(64, 64);
		base.mirror = true;
		setRotation(base, 0F, 0F, 0F);
		top1 = new ModelRenderer(this, 0, 38);
		top1.addBox(-2F, 15F, -2F, 4, 1, 4);
		top1.setRotationPoint(0F, 0F, 0F);
		top1.setTextureSize(64, 64);
		top1.mirror = true;
		setRotation(top1, 0F, 0F, 0F);
		top2 = new ModelRenderer(this, 16, 38);
		top2.addBox(-1F, 14F, -1F, 2, 1, 2);
		top2.setRotationPoint(0F, 0F, 0F);
		top2.setTextureSize(64, 64);
		top2.mirror = true;
		setRotation(top2, 0F, 0F, 0F);
		front1 = new ModelRenderer(this, 0, 12);
		front1.addBox(-2F, 17F, -4F, 4, 4, 1);
		front1.setRotationPoint(0F, 0F, 0F);
		front1.setTextureSize(64, 64);
		front1.mirror = true;
		setRotation(front1, 0F, 0F, 0F);
		front2 = new ModelRenderer(this, 10, 12);
		front2.addBox(-1F, 18F, -5F, 2, 2, 1);
		front2.setRotationPoint(0F, 0F, 0F);
		front2.setTextureSize(64, 64);
		front2.mirror = true;
		setRotation(front2, 0F, 0F, 0F);
		left1 = new ModelRenderer(this, 0, 17);
		left1.addBox(3F, 17F, -2F, 1, 4, 4);
		left1.setRotationPoint(0F, 0F, 0F);
		left1.setTextureSize(64, 64);
		left1.mirror = true;
		setRotation(left1, 0F, 0F, 0F);
		left2 = new ModelRenderer(this, 10, 17);
		left2.addBox(4F, 18F, -1F, 1, 2, 2);
		left2.setRotationPoint(0F, 0F, 0F);
		left2.setTextureSize(64, 64);
		left2.mirror = true;
		setRotation(left2, 0F, 0F, 0F);
		back1 = new ModelRenderer(this, 0, 25);
		back1.addBox(-2F, 17F, 3F, 4, 4, 1);
		back1.setRotationPoint(0F, 0F, 0F);
		back1.setTextureSize(64, 64);
		back1.mirror = true;
		setRotation(back1, 0F, 0F, 0F);
		back2 = new ModelRenderer(this, 10, 25);
		back2.addBox(-1F, 18F, 4F, 2, 2, 1);
		back2.setRotationPoint(0F, 0F, 0F);
		back2.setTextureSize(64, 64);
		back2.mirror = true;
		setRotation(back2, 0F, 0F, 0F);
		right1 = new ModelRenderer(this, 0, 30);
		right1.addBox(-4F, 17F, -2F, 1, 4, 4);
		right1.setRotationPoint(0F, 0F, 0F);
		right1.setTextureSize(64, 64);
		right1.mirror = true;
		setRotation(right1, 0F, 0F, 0F);
		right2 = new ModelRenderer(this, 10, 30);
		right2.addBox(-5F, 18F, -1F, 1, 2, 2);
		right2.setRotationPoint(0F, 0F, 0F);
		right2.setTextureSize(64, 64);
		right2.mirror = true;
		setRotation(right2, 0F, 0F, 0F);
		bottom1 = new ModelRenderer(this, 0, 38);
		bottom1.addBox(-2F, 22F, -2F, 4, 1, 4);
		bottom1.setRotationPoint(0F, 0F, 0F);
		bottom1.setTextureSize(64, 64);
		bottom1.mirror = true;
		setRotation(bottom1, 0F, 0F, 0F);
		bottom2 = new ModelRenderer(this, 16, 38);
		bottom2.addBox(-1F, 23F, -1F, 2, 1, 2);
		bottom2.setRotationPoint(0F, 0F, 0F);
		bottom2.setTextureSize(64, 64);
		bottom2.mirror = true;
		setRotation(bottom2, 0F, 0F, 0F);
		spine = new ModelRenderer(this, 24, 0);
		spine.addBox(-0.5F, -9F, -0.5F, 1, 6, 1);
		spine.setRotationPoint(0F, 19F, 0F);
		spine.setTextureSize(64, 64);
		spine.mirror = true;
		setRotation(spine, 0F, 0F, 0F);
	}

	@Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
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

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
	}

}