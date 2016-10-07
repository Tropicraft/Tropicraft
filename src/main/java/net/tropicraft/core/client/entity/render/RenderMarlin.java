package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelMarlin;
import net.tropicraft.core.common.entity.underdasea.EntityMarlin;

import org.lwjgl.opengl.GL11;

public class RenderMarlin extends RenderLiving<EntityMarlin> {

	private ModelMarlin marlin;

	public RenderMarlin(ModelBase modelbase, float f) {
		super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
		marlin = (ModelMarlin) mainModel;
	}
//
//	public void renderMarlin(EntityMarlin entityliving, double d, double d1, double d2,
//			float f, float f1) {
//		marlin.inWater = entityliving.isInWater();
//		GL11.glPushMatrix();
//		GL11.glDisable(2884 /*GL_CULL_FACE*/);
//		mainModel.onGround = renderSwingProgress(entityliving, f1);
//		if (renderPassModel != null) {
//			renderPassModel.onGround = mainModel.onGround;
//		}
//		mainModel.isRiding = entityliving.isRiding();
//		if (renderPassModel != null) {
//			renderPassModel.isRiding = mainModel.isRiding;
//		}
//		mainModel.isChild = entityliving.isChild();
//		if (renderPassModel != null) {
//			renderPassModel.isChild = mainModel.isChild;
//		}
//		try {
//			float f2 = entityliving.prevRenderYawOffset + (entityliving.renderYawOffset - entityliving.prevRenderYawOffset) * f1;
//			float f3 = entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * f1;
//			float f4 = entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * f1;
//			renderLivingAt(entityliving, d, d1, d2);
//
//			float f5 = handleRotationFloat(entityliving, f1);
//
//			//if (entityliving.isInWater() || entityliving.surfaceTick != 0) 
//			{
//				rotateCorpse(entityliving, f5, f2, f1);
//				if(entityliving.isInWater())
//				GL11.glRotatef(entityliving.rotationPitch, 1.0F, 0.0F, 0.0F);
//				else
//				GL11.glRotatef((float) Math.toRadians(entityliving.motionY*45 < 0 ? -(entityliving.motionY*45) : entityliving.motionY*45), 1.0F, 0.0F, 0.0F);
//			}
//			if (!entityliving.isInWater()) {
//				float rot = 0F;
//				if(entityliving.outOfWaterTick*4 < 91 && entityliving.getHookID() == -1){
//					rot = (float)entityliving.outOfWaterTick*4;
//				}else{
//					rot = 90F;
//				}
//				GL11.glTranslatef(0.0F, .125F, 0.0F);
//				GL11.glRotatef(rot, 0.0F, 0.0F, 1.0F);
//				GL11.glRotatef(entityliving.rotationPitch, 0.0F, 1.0F, 0.0F);
//			}
//			float f6 = 0.0625F;
//			GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
//			GL11.glScalef(-1F, -1F, 1.0F);
//			preRenderCallback(entityliving, f1);
//			GL11.glTranslatef(0.0F, -24F * f6 - 0.0078125F, 0.0F);
//			float f7 = entityliving.prevLimbSwingAmount + (entityliving.limbSwingAmount - entityliving.prevLimbSwingAmount) * f1;
//			float f8 = entityliving.limbSwing - entityliving.limbSwingAmount * (1.0F - f1);
//			if (entityliving.isChild()) {
//				f8 *= 3F;
//			}
//			if (f7 > 1.0F) {
//				f7 = 1.0F;
//			}
//			GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
//			mainModel.setLivingAnimations(entityliving, f8, f7, f1);
//			renderModel(entityliving, f8, f7, f5, f3 - f2, f4, f6);
//			for (int i = 0; i < 4; i++) {
//				int j = shouldRenderPass(entityliving, i, f1);
//				if (j <= 0) {
//					continue;
//				}
//				renderPassModel.render(entityliving, f8, f7, f5, f3 - f2, f4, f6);
//				if (j == 15) {
//					float f10 = (float) entityliving.ticksExisted + f1;
//					this.bindEntityTexture(entityliving);
//					//loadTexture("%blur%/misc/glint.png");
//					GL11.glEnable(3042 /*GL_BLEND*/);
//					float f12 = 0.5F;
//					GL11.glColor4f(f12, f12, f12, 1.0F);
//					GL11.glDepthFunc(514);
//					GL11.glDepthMask(false);
//					for (int i1 = 0; i1 < 2; i1++) {
//						GL11.glDisable(2896 /*GL_LIGHTING*/);
//						float f15 = 0.76F;
//						GL11.glColor4f(0.5F * f15, 0.25F * f15, 0.8F * f15, 1.0F);
//						GL11.glBlendFunc(768, 1);
//						GL11.glMatrixMode(5890 /*GL_TEXTURE*/);
//						GL11.glLoadIdentity();
//						float f17 = f10 * (0.001F + (float) i1 * 0.003F) * 20F;
//						float f18 = 0.3333333F;
//						GL11.glScalef(f18, f18, f18);
//						GL11.glRotatef(30F - (float) i1 * 60F, 0.0F, 0.0F, 1.0F);
//						GL11.glTranslatef(0.0F, f17, 0.0F);
//						GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
//						renderPassModel.render(entityliving, f8, f7, f5, f3 - f2, f4, f6);
//					}
//
//					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//					GL11.glMatrixMode(5890 /*GL_TEXTURE*/);
//					GL11.glDepthMask(true);
//					GL11.glLoadIdentity();
//					GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
//					GL11.glEnable(2896 /*GL_LIGHTING*/);
//					GL11.glDisable(3042 /*GL_BLEND*/);
//					GL11.glDepthFunc(515);
//				}
//				GL11.glDisable(3042 /*GL_BLEND*/);
//				GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
//			}
//
//			renderEquippedItems(entityliving, f1);
//			float f9 = entityliving.getBrightness(f1);
//			int k = getColorMultiplier(entityliving, f9, f1);
//			OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
//			GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
//			OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
//			if ((k >> 24 & 0xff) > 0 || entityliving.hurtTime > 0 || entityliving.deathTime > 0) {
//				GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
//				GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
//				GL11.glEnable(3042 /*GL_BLEND*/);
//				GL11.glBlendFunc(770, 771);
//				GL11.glDepthFunc(514);
//				if (entityliving.hurtTime > 0 || entityliving.deathTime > 0) {
//					GL11.glColor4f(f9, 0.0F, 0.0F, 0.4F);
//					//GL11.glBindTexture(GL11.GL_TEXTURE_2D, Minecraft.getMinecraft().renderEngine.getTexture(entityliving.getTexture()));
//					mainModel.render(entityliving, f8, f7, f5, f3 - f2, f4, f6);
//					for (int l = 0; l < 4; l++) {
//						if (inheritRenderPass(entityliving, l, f1) >= 0) {
//							GL11.glColor4f(f9, 0.0F, 0.0F, 0.4F);
//							renderPassModel.render(entityliving, f8, f7, f5, f3 - f2, f4, f6);
//						}
//					}
//
//				}
//				if ((k >> 24 & 0xff) > 0) {
//					float f11 = (float) (k >> 16 & 0xff) / 255F;
//					float f13 = (float) (k >> 8 & 0xff) / 255F;
//					float f14 = (float) (k & 0xff) / 255F;
//					float f16 = (float) (k >> 24 & 0xff) / 255F;
//					GL11.glColor4f(f11, f13, f14, f16);
//					mainModel.render(entityliving, f8, f7, f5, f3 - f2, f4, f6);
//					for (int j1 = 0; j1 < 4; j1++) {
//						if (inheritRenderPass(entityliving, j1, f1) >= 0) {
//							GL11.glColor4f(f11, f13, f14, f16);
//							renderPassModel.render(entityliving, f8, f7, f5, f3 - f2, f4, f6);
//						}
//					}
//
//				}
//				GL11.glDepthFunc(515);
//				GL11.glDisable(3042 /*GL_BLEND*/);
//				GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
//				GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
//			}
//			GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
//		} catch (Exception exception) {
//			exception.printStackTrace();
//		}
//		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
//		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
//		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
//		GL11.glEnable(2884 /*GL_CULL_FACE*/);
//		GL11.glPopMatrix();
//		//TODO: passSpecialRender(entityliving, d, d1, d2);
//	}
//
//	@Override
//	public void doRender(EntityMarlin marlin, double x, double y, double z, float entityYaw, float partialTicks) {
//		renderMarlin((EntityMarlin) marlin, x, y, z, entityYaw, partialTicks);
//	}

	@Override
	protected void preRenderCallback(EntityMarlin marlin, float partialTickTime) {
		preRenderScale((EntityMarlin) marlin, partialTickTime);
	}

	protected void preRenderScale(EntityMarlin entitymarlin, float f) {
		GL11.glScalef(1.5F, 1.5F, 1.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMarlin entity) {
		return TropicraftRenderUtils.bindTextureEntity("marlin");
	}
}
