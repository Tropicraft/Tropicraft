package net.tropicraft.core.client.entity.render;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public abstract class RenderTropicraftWaterMob extends RenderLiving<EntityTropicraftWaterBase>{

	public RenderTropicraftWaterMob(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn) {
		super(rendermanagerIn, modelbaseIn, shadowsizeIn);
		// TODO Auto-generated constructor stub
	}
	
	public void renderWaterMob(EntityTropicraftWaterBase entityliving, double d, double d1, double d2, float f1) {
		this.renderWaterMob(entityliving, d, d1, d2, f1, 0, 0);
	}

	
	public void renderWaterMob(EntityTropicraftWaterBase entityliving, double d, double d1, double d2, float f1, float yawOffset, float pitchOffset) {
		GL11.glPushMatrix();
		GL11.glDisable(2884 /*GL_CULL_FACE*/);
	
	
			float f2 = entityliving.prevRenderYawOffset + (entityliving.renderYawOffset - entityliving.prevRenderYawOffset) * f1;
			float f3 = (entityliving.prevSwimYaw+yawOffset) + ((entityliving.swimYaw+yawOffset) - (entityliving.prevSwimYaw+yawOffset)) * f1;
			float f4 = (entityliving.prevSwimPitch+pitchOffset) + ((entityliving.swimPitch+pitchOffset) - (entityliving.prevSwimPitch+pitchOffset)) * f1;
			renderLivingAt(entityliving, d, d1, d2);

			f2 = 0f;
		//	f3 = 0f; 
		//	f4 = 0f;
			
			float f5 = handleRotationFloat(entityliving, f1);


			if(entityliving.hurtTime > 0){

				GL11.glColor4f(2f, 0f, 0f, 1f);
			}
			//if (entityliving.isInWater() || entityliving.surfaceTick != 0) 
			{
				
				if(entityliving.deathTime > 0) {
		            GlStateManager.rotate(f1 * this.getDeathMaxRotation(entityliving), 0.0F, 0.0F, 1.0F);
				}else {
					GlStateManager.translate(0f, -0.1f, 0f);

		        //   GlStateManager.rotate(entityliving.ticksExisted, 0.0F, 0.0F, 1.0F);

				}
				//applyRotations(entityliving, f5, f2, f1);
//				rotateCorpse(entityliving, f5, f2, f1);
				

	            GlStateManager.rotate(entityliving.swimYaw+yawOffset+180, 0F, 1.0F, 0.0F);

	            GlStateManager.rotate(entityliving.swimPitch+pitchOffset, 1.0F, 0.0F, 0.0F);


			}
			if (!entityliving.isInWater()) {
				float rot = 0F;
				if(entityliving.outOfWaterTime*4 < 91){
					rot = (float)entityliving.outOfWaterTime*4;
				}else{
					rot = 90F;
				}
				GL11.glTranslatef(0.0F, .125F, 0.0F);
				GL11.glRotatef(rot, 0.0F, 0.0F, 1.0F);
			//	GL11.glRotatef(entityliving.rotationPitch, 0.0F, 1.0F, 0.0F);
			}
			float f6 = 0.0625F;
			GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
			GL11.glScalef(-1F, -1F, 1.0F);
			preRenderCallback(entityliving, f1);
			GL11.glTranslatef(0.0F, -24F * f6 - 0.0078125F, 0.0F);
			float f7 = 0f;
			float f8 = 0f;
			if (entityliving.isChild()) {
				f8 *= 3F;
			}
			if (f7 > 1.0F) {
				f7 = 1.0F;
			}
			GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
			mainModel.setLivingAnimations(entityliving, f8, f7, f1);
			renderModel(entityliving, f8, f7, f5, f3 - f2, f4, 0.0625f);
			GL11.glColor4f(1f, 1f, 1f, 1f);

		GL11.glPopMatrix();
	}

}
