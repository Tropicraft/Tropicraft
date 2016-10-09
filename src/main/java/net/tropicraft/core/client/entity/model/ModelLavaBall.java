package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLavaBall extends ModelBase {

	ModelRenderer ball;

	public ModelLavaBall() {
		textureWidth = 64;
		textureHeight = 32;
		setTextureOffset("Ball.Shape4", 22, 18);
		setTextureOffset("Ball.Shape1", 0, 0);
		setTextureOffset("Ball.Shape2", 22, 11);
		setTextureOffset("Ball.Shape3", 29, 4);

		ball = new ModelRenderer(this, "Ball");
		ball.setRotationPoint(0F, 17F, 0F);
		setRotation(ball, 0F, 0F, 0F);
		ball.mirror = true;
		ball.addBox("Shape4", -7F, -3.5F, -3.5F, 14, 7, 7);
		ball.addBox("Shape1", -5F, -5F, -5F, 10, 10, 10);
		ball.addBox("Shape2", -3.5F, -3.5F, -7F, 7, 7, 14);
		ball.addBox("Shape3", -3.5F, -7F, -3.5F, 7, 14, 7);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, null);
		ball.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
		ball.rotateAngleX += .05;
		ball.rotateAngleY += .05;
		ball.rotateAngleZ += .05;
	}
}
