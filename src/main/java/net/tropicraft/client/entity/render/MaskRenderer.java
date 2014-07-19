package net.tropicraft.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

public class MaskRenderer {

    private Minecraft mc;

    public void renderMask(int i) {
        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.instance;
        int j = i;
        float f = ((float) ((j % 8) * 32) + 0.0F) / 256F;
        float f1 = ((float) ((j % 8) * 32) + 31.99F) / 256F;
        float f2 = ((float) ((j / 8) * 32) + 0.0F) / 256F;
        float f3 = ((float) ((j / 8) * 32) + 31.99F) / 256F;
        float f1shifted = ((float) ((j / 8) * 32) + 128) / 256F;
        float f3shifted = ((float) ((j / 8) * 32) + 159.99F) / 256F;
        float f4 = 0.0F;
        float f5 = 0.3F;
        GL11.glEnable(32826 /*
                 * GL_RESCALE_NORMAL_EXT
                 */);
        GL11.glTranslatef(-f4, -f5, 0.0F);
        float f6 = 1.7F;
        GL11.glScalef(f6, f6, f6);
        GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
        popper(tessellator, f1, f2, f, f3, f1shifted, f3shifted);

        GL11.glDisable(32826 /*
                 * GL_RESCALE_NORMAL_EXT
                 */);

        GL11.glPopMatrix();
    }

    public void renderFish(int i) {
        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.instance;
        int j = i;
        float f = ((float) ((j % 8) * 32) + 0.0F) / 256F;
        float f1 = ((float) ((j % 8) * 32) + 31.99F) / 256F;
        float f2 = ((float) ((j / 8) * 32) + 0.0F) / 256F;
        float f3 = ((float) ((j / 8) * 32) + 31.99F) / 256F;
        float f4 = 0.0F;
        float f5 = 0.3F;
        GL11.glEnable(32826 /*
                 * GL_RESCALE_NORMAL_EXT
                 */);
        GL11.glTranslatef(-f4, -f5, 0.0F);
        float f6 = 1.7F;
        GL11.glScalef(f6, f6, f6);
        GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
        popper(tessellator, f1, f2, f, f3, f2, f3);

        GL11.glDisable(32826 /*
                 * GL_RESCALE_NORMAL_EXT
                 */);

        GL11.glPopMatrix();
    }

    public void renderItem(IIcon icon) {
        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.instance;
        //int j = i;
        float f = icon.getMinU();//((float) ((j % 16) * 16) + 0.0F) / 256F;
        float f1 = icon.getMaxU();//((float) ((j % 16) * 16) + 15.99F) / 256F;
        float f2 = icon.getMinV();//((float) ((j / 16) * 16) + 0.0F) / 256F;
        float f3 = icon.getMaxV();//((float) ((j / 16) * 16) + 15.99F) / 256F;
        float f4 = 0.0F;
        float f5 = 0.3F;
        GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
        GL11.glTranslatef(-f4, -f5, 0.0F);
        float f6 = .5F;
        GL11.glScalef(f6, f6, f6);
        GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
        popper2(tessellator, f1, f2, f, f3);
        GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
        GL11.glPopMatrix();
    }
    
    public void renderItem(int j) {
    	GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.instance;
        float f = ((float) ((j % 16) * 16) + 0.0F) / 256F;
        float f1 = ((float) ((j % 16) * 16) + 15.99F) / 256F;
        float f2 = ((float) ((j / 16) * 16) + 0.0F) / 256F;
        float f3 = ((float) ((j / 16) * 16) + 15.99F) / 256F;
        float f4 = 0.0F;
        float f5 = 0.3F;
        GL11.glEnable(32826 /* GL_RESCALE_NORMAL_EXT */);
        GL11.glTranslatef(-f4, -f5, 0.0F);
        float f6 = .5F;
        GL11.glScalef(f6, f6, f6);
        GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
        popper2(tessellator, f1, f2, f, f3);
        GL11.glDisable(32826 /* GL_RESCALE_NORMAL_EXT */);
        GL11.glPopMatrix();
    }

    private void popper(Tessellator tessellator, float f, float f1, float f2, float f3, float f1shifted, float f3shifted) {
        float f4 = 1.0F;
        float f5 = 0.03125F;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);

        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, f, f3shifted);
        tessellator.addVertexWithUV(f4, 0.0D, 0.0D, f2, f3shifted);
        tessellator.addVertexWithUV(f4, 1.0D, 0.0D, f2, f1shifted);
        tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, f, f1shifted);

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        tessellator.addVertexWithUV(0.0D, 1.0D, 0.0F - f5, f, f1);
        tessellator.addVertexWithUV(f4, 1.0D, 0.0F - f5, f2, f1);
        tessellator.addVertexWithUV(f4, 0.0D, 0.0F - f5, f2, f3);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0F - f5, f, f3);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        for (int i = 0; i < 32; i++) {
            float f6 = (float) i / 32F;
            float f10 = (f + (f2 - f) * f6) - 0.001953125F;
            float f14 = f4 * f6;
            tessellator.addVertexWithUV(f14, 0.0D, 0.0F - f5, f10, f3);
            tessellator.addVertexWithUV(f14, 0.0D, 0.0D, f10, f3);
            tessellator.addVertexWithUV(f14, 1.0D, 0.0D, f10, f1);
            tessellator.addVertexWithUV(f14, 1.0D, 0.0F - f5, f10, f1);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        for (int j = 0; j < 32; j++) {
            float f7 = (float) j / 32F;
            float f11 = (f + (f2 - f) * f7) - 0.001953125F;
            float f15 = f4 * f7 + 0.03125F;
            tessellator.addVertexWithUV(f15, 1.0D, 0.0F - f5, f11, f1);
            tessellator.addVertexWithUV(f15, 1.0D, 0.0D, f11, f1);
            tessellator.addVertexWithUV(f15, 0.0D, 0.0D, f11, f3);
            tessellator.addVertexWithUV(f15, 0.0D, 0.0F - f5, f11, f3);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        for (int k = 0; k < 32; k++) {
            float f8 = (float) k / 32F;
            float f12 = (f3 + (f1 - f3) * f8) - 0.001953125F;
            float f16 = f4 * f8 + 0.03125F;
            tessellator.addVertexWithUV(0.0D, f16, 0.0D, f, f12);
            tessellator.addVertexWithUV(f4, f16, 0.0D, f2, f12);
            tessellator.addVertexWithUV(f4, f16, 0.0F - f5, f2, f12);
            tessellator.addVertexWithUV(0.0D, f16, 0.0F - f5, f, f12);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        for (int l = 0; l < 32; l++) {
            float f9 = (float) l / 32F;
            float f13 = (f3 + (f1 - f3) * f9) - 0.001953125F;
            float f17 = f4 * f9;
            tessellator.addVertexWithUV(f4, f17, 0.0D, f2, f13);
            tessellator.addVertexWithUV(0.0D, f17, 0.0D, f, f13);
            tessellator.addVertexWithUV(0.0D, f17, 0.0F - f5, f, f13);
            tessellator.addVertexWithUV(f4, f17, 0.0F - f5, f2, f13);
        }

        tessellator.draw();
    }

    private void popper2(Tessellator tessellator, float f, float f1, float f2, float f3) {
        float f4 = 1.0F;
        float f5 = 0.0625F;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, f, f3);
        tessellator.addVertexWithUV(f4, 0.0D, 0.0D, f2, f3);
        tessellator.addVertexWithUV(f4, 1.0D, 0.0D, f2, f1);
        tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, f, f1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        tessellator.addVertexWithUV(0.0D, 1.0D, 0.0F - f5, f, f1);
        tessellator.addVertexWithUV(f4, 1.0D, 0.0F - f5, f2, f1);
        tessellator.addVertexWithUV(f4, 0.0D, 0.0F - f5, f2, f3);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0F - f5, f, f3);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        for (int i = 0; i < 16; i++) {
            float f6 = (float) i / 16F;
            float f10 = (f + (f2 - f) * f6) - 0.001953125F;
            float f14 = f4 * f6;
            tessellator.addVertexWithUV(f14, 0.0D, 0.0F - f5, f10, f3);
            tessellator.addVertexWithUV(f14, 0.0D, 0.0D, f10, f3);
            tessellator.addVertexWithUV(f14, 1.0D, 0.0D, f10, f1);
            tessellator.addVertexWithUV(f14, 1.0D, 0.0F - f5, f10, f1);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        for (int j = 0; j < 16; j++) {
            float f7 = (float) j / 16F;
            float f11 = (f + (f2 - f) * f7) - 0.001953125F;
            float f15 = f4 * f7 + 0.0625F;
            tessellator.addVertexWithUV(f15, 1.0D, 0.0F - f5, f11, f1);
            tessellator.addVertexWithUV(f15, 1.0D, 0.0D, f11, f1);
            tessellator.addVertexWithUV(f15, 0.0D, 0.0D, f11, f3);
            tessellator.addVertexWithUV(f15, 0.0D, 0.0F - f5, f11, f3);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        for (int k = 0; k < 16; k++) {
            float f8 = (float) k / 16F;
            float f12 = (f3 + (f1 - f3) * f8) - 0.001953125F;
            float f16 = f4 * f8 + 0.0625F;
            tessellator.addVertexWithUV(0.0D, f16, 0.0D, f, f12);
            tessellator.addVertexWithUV(f4, f16, 0.0D, f2, f12);
            tessellator.addVertexWithUV(f4, f16, 0.0F - f5, f2, f12);
            tessellator.addVertexWithUV(0.0D, f16, 0.0F - f5, f, f12);
        }

        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        for (int l = 0; l < 16; l++) {
            float f9 = (float) l / 16F;
            float f13 = (f3 + (f1 - f3) * f9) - 0.001953125F;
            float f17 = f4 * f9;
            tessellator.addVertexWithUV(f4, f17, 0.0D, f2, f13);
            tessellator.addVertexWithUV(0.0D, f17, 0.0D, f, f13);
            tessellator.addVertexWithUV(0.0D, f17, 0.0F - f5, f, f13);
            tessellator.addVertexWithUV(f4, f17, 0.0F - f5, f2, f13);
        }

        tessellator.draw();
    }
}
