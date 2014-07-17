package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.tropicraft.entity.underdasea.EntityEagleRay;

import org.lwjgl.opengl.GL11;

public class ModelEagleRay extends ModelBase {
	/**
	 * Wing joint amplitudes, linearly interpolated between previous tick and this tick using partialTicks.
	 */
	private float[] interpolatedWingAmplitudes = new float[EntityEagleRay.WING_JOINTS];

    private ModelRenderer body;
    
    public ModelEagleRay() {
    	textureWidth = 128;
    	textureHeight = 64;

        body = new ModelRenderer(this, 32, 0);
        body.addBox(-2F, 0F, 0F, 5, 3, 32);
        body.setRotationPoint(0F, 0F, -8F);
        body.setTextureSize(128, 64);
        body.mirror = true;
    }

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		body.render(f5);
		renderWings();
		renderTailSimple();
	}
	
	private void renderTailSimple() {
		Tessellator tessellator = Tessellator.instance;

		float minU = 0.75f;
		float maxU = 1.0f;
		float minV = 0.0f;
		float maxV = 0.5f;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0.55f, 0f, 1.5f);
		GL11.glRotatef(-90f, 0f, 1f, 0f);
		GL11.glScalef(1.5f, 1f, 1f);
		
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, minU, minV);
		tessellator.addVertexWithUV(0.0D, 0.0D, 1.0D, minU, maxV);
		tessellator.addVertexWithUV(1.0D, 0.0D, 1.0D, maxU, maxV);
		tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, maxU, minV);
		tessellator.draw();

		GL11.glPopMatrix();
	}

	private void renderWings() {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glPushMatrix();
		GL11.glScalef(2f, 0.5f, 2f);
		GL11.glTranslatef(0.1f, 0f, -0.25f);
		renderWing(false);
		GL11.glRotatef(180f, 0f, 1f, 0f);
		GL11.glTranslatef(0.10f, 0f, -1f);
		renderWing(true);
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	private void renderWing(boolean reverse) {
		Tessellator tessellator = Tessellator.instance;

		float minUFront = 0f;
		float maxUFront = 0.25f;
		float minVFront = 0f;
		float maxVFront = 0.5f;
		
		float minUBack = 0f;
		float maxUBack = 0.25f;
		float minVBack = 0.5f;
		float maxVBack = 1f;
		
		for (int i = 1; i < EntityEagleRay.WING_JOINTS; i++) {
			float prevAmplitude = interpolatedWingAmplitudes[i-1];
			float amplitude = interpolatedWingAmplitudes[i];
			
			float prevX = (i-1)/(EntityEagleRay.WING_JOINTS-1f);
			float x = i/(EntityEagleRay.WING_JOINTS-1f);
			
			float prevUFront = minUFront + (maxUFront-minUFront)*prevX;
			float uFront = minUFront + (maxUFront-minUFront)*x;
			float prevUBack = minUBack + (maxUBack-minUBack)*prevX;
			float uBack = minUBack + (maxUBack-minUBack)*x;
			
			float offset = -0.001f;
			// Bottom
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(x, amplitude-offset, 0.0D, uBack, reverse ? maxVBack : minVBack);
			tessellator.addVertexWithUV(x, amplitude-offset, 1.0D, uBack, reverse ? minVBack : maxVBack);
			tessellator.addVertexWithUV(prevX, prevAmplitude-offset, 1.0D, prevUBack, reverse ? minVBack : maxVBack);
			tessellator.addVertexWithUV(prevX, prevAmplitude-offset, 0.0D, prevUBack, reverse ? maxVBack : minVBack);
			tessellator.draw();

			// Top
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(prevX, prevAmplitude, 0.0D, prevUFront, reverse ? maxVFront : minVFront);
			tessellator.addVertexWithUV(prevX, prevAmplitude, 1.0D, prevUFront, reverse ? minVFront : maxVFront);
			tessellator.addVertexWithUV(x, amplitude, 1.0D, uFront, reverse ? minVFront : maxVFront);
			tessellator.addVertexWithUV(x, amplitude, 0.0D, uFront, reverse ? maxVFront : minVFront);
			tessellator.draw();
		}
	}
	
	private float lerp(float start, float end, float partialTicks) {
		return start + (end-start)*partialTicks;
	}

	@Override
	public void setLivingAnimations(EntityLivingBase entityLiving, float par2, float par3, float partialTicks) {
		EntityEagleRay ray = (EntityEagleRay) entityLiving;
		float[] prevWingAmplitudes = ray.getPrevWingAmplitudes();
		float[] wingAmplitudes = ray.getWingAmplitudes();

		for (int i = 1; i < EntityEagleRay.WING_JOINTS; i++) {
			float prevWingAmplitude = prevWingAmplitudes[i];
			float wingAmplitude = wingAmplitudes[i];
			interpolatedWingAmplitudes[i] = prevWingAmplitude + partialTicks*(wingAmplitude - prevWingAmplitude);
		}
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
