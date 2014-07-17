package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelSeaTurtle;
import net.tropicraft.entity.underdasea.EntityAmphibian;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderSeaTurtle extends RenderLiving {

    public ModelSeaTurtle turtle;

    public RenderSeaTurtle(ModelBase modelbase, float f) {
        super(modelbase, f);
        turtle = (ModelSeaTurtle) modelbase;
    }

    public void renderTurtle(EntityAmphibian entityTurtle, double d, double d1, double d2,
            float f, float f1) {

        turtle.inWater = entityTurtle.isInWater();

        GL11.glPushMatrix();
        GL11.glDisable(2884 /*GL_CULL_FACE*/);
        mainModel.onGround = renderSwingProgress(entityTurtle, f1);
        if (renderPassModel != null) {
            renderPassModel.onGround = mainModel.onGround;
        }
        mainModel.isRiding = entityTurtle.isRiding();
        if (renderPassModel != null) {
            renderPassModel.isRiding = mainModel.isRiding;
        }
        mainModel.isChild = entityTurtle.isChild();
        if (renderPassModel != null) {
            renderPassModel.isChild = mainModel.isChild;
        }
        try {
            float f2 = entityTurtle.prevRenderYawOffset + (entityTurtle.renderYawOffset - entityTurtle.prevRenderYawOffset) * f1;
            float f3 = entityTurtle.prevRotationYaw + (entityTurtle.rotationYaw - entityTurtle.prevRotationYaw) * f1;
            float f4 = entityTurtle.prevRotationPitch + (entityTurtle.rotationPitch - entityTurtle.prevRotationPitch) * f1;
            renderLivingAt(entityTurtle, d, d1, d2);
            float f5 = handleRotationFloat(entityTurtle, f1);
            rotateCorpse(entityTurtle, f5, f2, f1);
            if (entityTurtle.isInWater()) {
                GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
            }
            float f6 = 0.0625F;
            GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
            GL11.glScalef(-1F, -1F, 1.0F);
            preRenderCallback(entityTurtle, f1);
            GL11.glTranslatef(0.0F, -24F * f6 - 0.0078125F, 0.0F);
            float f7 = entityTurtle.prevLimbSwingAmount + (entityTurtle.limbSwingAmount - entityTurtle.prevLimbSwingAmount) * f1;
            float f8 = entityTurtle.limbSwing - entityTurtle.limbSwingAmount * (1.0F - f1);
            if (entityTurtle.isChild()) {
                f8 *= 3F;
            }
            if (f7 > 1.0F) {
                f7 = 1.0F;
            }
            GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
            mainModel.setLivingAnimations(entityTurtle, f8, f7, f1);
            renderModel(entityTurtle, f8, f7, f5, f3 - f2, f4, f6);
            for (int i = 0; i < 4; i++) {
                int j = shouldRenderPass(entityTurtle, i, f1);
                if (j <= 0) {
                    continue;
                }
                renderPassModel.render(entityTurtle, f8, f7, f5, f3 - f2, f4, f6);
                if (j == 15) {
                    float f10 = (float) entityTurtle.ticksExisted + f1;
                    //loadTexture("%blur%/misc/glint.png");
                    GL11.glEnable(3042 /*GL_BLEND*/);
                    float f12 = 0.5F;
                    GL11.glColor4f(f12, f12, f12, 1.0F);
                    GL11.glDepthFunc(514);
                    GL11.glDepthMask(false);
                    for (int i1 = 0; i1 < 2; i1++) {
                        GL11.glDisable(2896 /*GL_LIGHTING*/);
                        float f15 = 0.76F;
                        GL11.glColor4f(0.5F * f15, 0.25F * f15, 0.8F * f15, 1.0F);
                        GL11.glBlendFunc(768, 1);
                        GL11.glMatrixMode(5890 /*GL_TEXTURE*/);
                        GL11.glLoadIdentity();
                        float f17 = f10 * (0.001F + (float) i1 * 0.003F) * 20F;
                        float f18 = 0.3333333F;
                        GL11.glScalef(f18, f18, f18);
                        GL11.glRotatef(30F - (float) i1 * 60F, 0.0F, 0.0F, 1.0F);
                        GL11.glTranslatef(0.0F, f17, 0.0F);
                        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
                        renderPassModel.render(entityTurtle, f8, f7, f5, f3 - f2, f4, f6);
                    }

                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glMatrixMode(5890 /*GL_TEXTURE*/);
                    GL11.glDepthMask(true);
                    GL11.glLoadIdentity();
                    GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
                    GL11.glEnable(2896 /*GL_LIGHTING*/);
                    GL11.glDisable(3042 /*GL_BLEND*/);
                    GL11.glDepthFunc(515);
                }
                GL11.glDisable(3042 /*GL_BLEND*/);
                GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
            }

            renderEquippedItems(entityTurtle, f1);
            float f9 = entityTurtle.getBrightness(f1);
            int k = getColorMultiplier(entityTurtle, f9, f1);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            if ((k >> 24 & 0xff) > 0 || entityTurtle.hurtTime > 0 || entityTurtle.deathTime > 0) {
                GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
                GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
                GL11.glEnable(3042 /*GL_BLEND*/);
                GL11.glBlendFunc(770, 771);
                GL11.glDepthFunc(514);
                if (entityTurtle.hurtTime > 0 || entityTurtle.deathTime > 0) {
                    GL11.glColor4f(f9, 0.0F, 0.0F, 0.4F);
                    mainModel.render(entityTurtle, f8, f7, f5, f3 - f2, f4, f6);
                    for (int l = 0; l < 4; l++) {
                        if (inheritRenderPass(entityTurtle, l, f1) >= 0) {
                            GL11.glColor4f(f9, 0.0F, 0.0F, 0.4F);
                            renderPassModel.render(entityTurtle, f8, f7, f5, f3 - f2, f4, f6);
                        }
                    }

                }
                if ((k >> 24 & 0xff) > 0) {
                    float f11 = (float) (k >> 16 & 0xff) / 255F;
                    float f13 = (float) (k >> 8 & 0xff) / 255F;
                    float f14 = (float) (k & 0xff) / 255F;
                    float f16 = (float) (k >> 24 & 0xff) / 255F;
                    GL11.glColor4f(f11, f13, f14, f16);
                    mainModel.render(entityTurtle, f8, f7, f5, f3 - f2, f4, f6);
                    for (int j1 = 0; j1 < 4; j1++) {
                        if (inheritRenderPass(entityTurtle, j1, f1) >= 0) {
                            GL11.glColor4f(f11, f13, f14, f16);
                            renderPassModel.render(entityTurtle, f8, f7, f5, f3 - f2, f4, f6);
                        }
                    }

                }
                GL11.glDepthFunc(515);
                GL11.glDisable(3042 /*GL_BLEND*/);
                GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
                GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
            }
            GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(2884 /*GL_CULL_FACE*/);
        GL11.glPopMatrix();
        passSpecialRender(entityTurtle, d, d1, d2);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entityliving, float f) {
        preRenderScale((EntityAmphibian) entityliving, f);
    }

    protected void preRenderScale(EntityAmphibian entitymarlin, float f) {
        float f1 = (float) (Math.log(entitymarlin.getAge() + 1.75)) - 0.25F;
        GL11.glScalef(f1, f1, f1);
    }

    @Override
    public void doRender(EntityLiving entityTurtle, double d, double d1, double d2,
            float f, float f1) {
        renderTurtle((EntityAmphibian) entityTurtle, d, d1, d2, f, f1);
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1) {
        renderTurtle((EntityAmphibian) entity, d, d1, d2, f, f1);
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TropicraftUtils.bindTextureEntity("turtle/seaTurtle");
	}
}