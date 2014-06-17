package net.tropicraft.client.entity.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.entity.projectile.EntityDart;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderDart extends Render {
    public RenderDart() {

    }

    public void renderDart(EntityDart dart, double x, double y, double z, float f, float f1) {
        TropicraftUtils.bindTextureEntity("dart");
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glRotatef((dart.prevRotationYaw + (dart.rotationYaw - dart.prevRotationYaw) * f1) - 90F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(dart.prevRotationPitch + (dart.rotationPitch - dart.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.instance;
        int i = 0;
        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = (float) (0 + i * 10) / 32F;
        float f5 = (float) (5 + i * 10) / 32F;
        float f6 = 0.0F;
        float f7 = 0.15625F;
        float f8 = (float) (5 + i * 10) / 32F;
        float f9 = (float) (10 + i * 10) / 32F;
        float f10 = 0.03625F; //controls scale
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        float f11 = (float) dart.dartShake - f1;
        if (f11 > 0.0F) {
            float f12 = -MathHelper.sin(f11 * 3F) * f11;
            GL11.glRotatef(f12, 0.0F, 0.0F, 1.0F);
        }
        GL11.glRotatef(45F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(f10, f10, f10);
        GL11.glTranslatef(-4F, 0.0F, 0.0F);
        GL11.glNormal3f(f10, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7D, -2D, -2D, f6, f8);
        tessellator.addVertexWithUV(-7D, -2D, 2D, f7, f8);
        tessellator.addVertexWithUV(-7D, 2D, 2D, f7, f9);
        tessellator.addVertexWithUV(-7D, 2D, -2D, f6, f9);
        tessellator.draw();
        GL11.glNormal3f(-f10, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7D, 2D, -2D, f6, f8);
        tessellator.addVertexWithUV(-7D, 2D, 2D, f7, f8);
        tessellator.addVertexWithUV(-7D, -2D, 2D, f7, f9);
        tessellator.addVertexWithUV(-7D, -2D, -2D, f6, f9);
        tessellator.draw();
        for (int j = 0; j < 4; j++) {
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f10);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-8D, -2D, 0.0D, f2, f4);
            tessellator.addVertexWithUV(8D, -2D, 0.0D, f3, f4);
            tessellator.addVertexWithUV(8D, 2D, 0.0D, f3, f5);
            tessellator.addVertexWithUV(-8D, 2D, 0.0D, f2, f5);
            tessellator.draw();
        }

        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
        renderDart((EntityDart) entity, d, d1, d2, f, f1);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TropicraftUtils.getTextureEntity("dart");
    }

}
