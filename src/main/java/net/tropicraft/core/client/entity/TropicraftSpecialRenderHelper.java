package net.tropicraft.core.client.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class TropicraftSpecialRenderHelper {

    public void renderMask(MatrixStack stack, IVertexBuilder buffer, int maskIndex) {
        stack.push();
        float f = ((float) ((maskIndex % 8) * 32) + 0.0F) / 256F;
        float f1 = ((float) ((maskIndex % 8) * 32) + 31.99F) / 256F;
        float f2 = ((float) ((maskIndex / 8) * 32) + 0.0F) / 256F;
        float f3 = ((float) ((maskIndex / 8) * 32) + 31.99F) / 256F;
        float f1shifted = ((float) ((maskIndex / 8) * 32) + 128) / 256F;
        float f3shifted = ((float) ((maskIndex / 8) * 32) + 159.99F) / 256F;
        float f4 = 0.0F;
        float f5 = 0.3F;
        RenderSystem.enableRescaleNormal();
        stack.translate(-f4, -f5, 0.0F);
        float f6 = 1.7F;
        stack.scale(f6, f6, f6);
        stack.rotate(Vector3f.YP.rotationDegrees(180F));
        stack.rotate(Vector3f.ZP.rotationDegrees(180F));
        stack.translate(-0.5F, -0.5F, 0.0F);
        popper(null, f1, f2, f, f3, f1shifted, f3shifted, stack, buffer);
        RenderSystem.disableRescaleNormal();
        stack.pop();
    }

    public void renderFish(final MatrixStack stack, final IVertexBuilder buffer, final int index) {
        GlStateManager.pushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        float f = ((float) ((index % 8) * 32) + 0.0F) / 256F;
        float f1 = ((float) ((index % 8) * 32) + 31.99F) / 256F;
        float f2 = ((float) ((index / 8) * 32) + 0.0F) / 256F;
        float f3 = ((float) ((index / 8) * 32) + 31.99F) / 256F;
        float f4 = 0.0F;
        float f5 = 0.3F;
        GlStateManager.enableRescaleNormal();
        GlStateManager.translatef(-f4, -f5, 0.0F);
        float f6 = 1.7F;
        GlStateManager.scalef(f6, f6, f6);
        GlStateManager.rotatef(180F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(180F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translatef(-0.5F, -0.5F, 0.0F);
        popper(tessellator, f1, f2, f, f3, f2, f3, stack, buffer);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }
    
    private void buf(IVertexBuilder buffer, double x, double y, double z, float u, float v) {
    	buffer.pos(x, y, z).tex(u, v).endVertex();
    }

    private void popper(Tessellator tessellator, float f, float f1, float f2, float f3, float f1shifted, float f3shifted, MatrixStack stack, IVertexBuilder buffer) {
        float f4 = 1.0F;
        float f5 = 0.03125F;
//        BufferBuilder vertexbuffer = tessellator.getBuffer();
//        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        GlStateManager.normal3f(0.0F, 0.0F, 1.0F);
        
        //vertexbuffer.pos(0D, 0D, 0D).tex((double)f, (double)f3shifted).endVertex();
        buf(buffer, 0.0D, 0.0D, 0.0D, f, f3shifted);
        buf(buffer, f4, 0.0D, 0.0D, f2, f3shifted);
        buf(buffer, f4, 1.0D, 0.0D, f2, f1shifted);
        buf(buffer, 0.0D, 1.0D, 0.0D, f, f1shifted);
  //      tessellator.draw();

     //   vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        GlStateManager.normal3f(0.0F, 0.0F, -1F);
        buf(buffer, 0.0D, 1.0D, 0.0F - f5, f, f1);
        buf(buffer, f4, 1.0D, 0.0F - f5, f2, f1);
        buf(buffer, f4, 0.0D, 0.0F - f5, f2, f3);
        buf(buffer, 0.0D, 0.0D, 0.0F - f5, f, f3);
   //    tessellator.draw();

//        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        RenderSystem.normal3f(-1F, 0.0F, 0.0F);
        for (int i = 0; i < 32; i++) {
            float f6 = (float) i / 32F;
            float f10 = (f + (f2 - f) * f6) - 0.001953125F;
            float f14 = f4 * f6;
            buf(buffer, f14, 0.0D, 0.0F - f5, f10, f3);
            buf(buffer, f14, 0.0D, 0.0D, f10, f3);
            buf(buffer, f14, 1.0D, 0.0D, f10, f1);
            buf(buffer, f14, 1.0D, 0.0F - f5, f10, f1);
        }
  //      tessellator.draw();
        
  //      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        GlStateManager.normal3f(1.0F, 0.0F, 0.0F);
        for (int j = 0; j < 32; j++) {
            float f7 = (float) j / 32F;
            float f11 = (f + (f2 - f) * f7) - 0.001953125F;
            float f15 = f4 * f7 + 0.03125F;
            buf(buffer, f15, 1.0D, 0.0F - f5, f11, f1);
            buf(buffer, f15, 1.0D, 0.0D, f11, f1);
            buf(buffer, f15, 0.0D, 0.0D, f11, f3);
            buf(buffer, f15, 0.0D, 0.0F - f5, f11, f3);
        }
   //     tessellator.draw();
 
//        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        GlStateManager.normal3f(0.0F, 1.0F, 0.0F);
        for (int k = 0; k < 32; k++) {
            float f8 = (float) k / 32F;
            float f12 = (f3 + (f1 - f3) * f8) - 0.001953125F;
            float f16 = f4 * f8 + 0.03125F;
            buf(buffer, 0.0D, f16, 0.0D, f, f12);
            buf(buffer, f4, f16, 0.0D, f2, f12);
            buf(buffer, f4, f16, 0.0F - f5, f2, f12);
            buf(buffer, 0.0D, f16, 0.0F - f5, f, f12);
        }
   //     tessellator.draw();

  //      vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        GlStateManager.normal3f(0.0F, -1F, 0.0F);
        for (int l = 0; l < 32; l++) {
            float f9 = (float) l / 32F;
            float f13 = (f3 + (f1 - f3) * f9) - 0.001953125F;
            float f17 = f4 * f9;
            buf(buffer, f4, f17, 0.0D, f2, f13);
            buf(buffer, 0.0D, f17, 0.0D, f, f13);
            buf(buffer, 0.0D, f17, 0.0F - f5, f, f13);
            buf(buffer, f4, f17, 0.0F - f5, f2, f13);
        }

      //  tessellator.draw();
    }

    private void popper2(Tessellator tessellator, float f, float f1, float f2, float f3) {
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        float f4 = 1.0F;
        float f5 = 0.0625F;
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        GlStateManager.normal3f(0.0F, 0.0F, 1.0F);
        buf(vertexbuffer, 0.0D, 0.0D, 0.0D, f, f3);
        buf(vertexbuffer, f4, 0.0D, 0.0D, f2, f3);
        buf(vertexbuffer, f4, 1.0D, 0.0D, f2, f1);
        buf(vertexbuffer, 0.0D, 1.0D, 0.0D, f, f1);
        tessellator.draw();

        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        GlStateManager.normal3f(0.0F, 0.0F, -1F);
        buf(vertexbuffer, 0.0D, 1.0D, 0.0F - f5, f, f1);
        buf(vertexbuffer, f4, 1.0D, 0.0F - f5, f2, f1);
        buf(vertexbuffer, f4, 0.0D, 0.0F - f5, f2, f3);
        buf(vertexbuffer, 0.0D, 0.0D, 0.0F - f5, f, f3);
        tessellator.draw();

        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        GlStateManager.normal3f(-1F, 0.0F, 0.0F);
        for (int i = 0; i < 16; i++) {
            float f6 = (float) i / 16F;
            float f10 = (f + (f2 - f) * f6) - 0.001953125F;
            float f14 = f4 * f6;
            buf(vertexbuffer, f14, 0.0D, 0.0F - f5, f10, f3);
            buf(vertexbuffer, f14, 0.0D, 0.0D, f10, f3);
            buf(vertexbuffer, f14, 1.0D, 0.0D, f10, f1);
            buf(vertexbuffer, f14, 1.0D, 0.0F - f5, f10, f1);
        }
        tessellator.draw();

        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        GlStateManager.normal3f(1.0F, 0.0F, 0.0F);
        for (int j = 0; j < 16; j++) {
            float f7 = (float) j / 16F;
            float f11 = (f + (f2 - f) * f7) - 0.001953125F;
            float f15 = f4 * f7 + 0.0625F;
            buf(vertexbuffer, f15, 1.0D, 0.0F - f5, f11, f1);
            buf(vertexbuffer, f15, 1.0D, 0.0D, f11, f1);
            buf(vertexbuffer, f15, 0.0D, 0.0D, f11, f3);
            buf(vertexbuffer, f15, 0.0D, 0.0F - f5, f11, f3);
        }
        tessellator.draw();

        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        GlStateManager.normal3f(0.0F, 1.0F, 0.0F);
        for (int k = 0; k < 16; k++) {
            float f8 = (float) k / 16F;
            float f12 = (f3 + (f1 - f3) * f8) - 0.001953125F;
            float f16 = f4 * f8 + 0.0625F;
            buf(vertexbuffer, 0.0D, f16, 0.0D, f, f12);
            buf(vertexbuffer, f4, f16, 0.0D, f2, f12);
            buf(vertexbuffer, f4, f16, 0.0F - f5, f2, f12);
            buf(vertexbuffer, 0.0D, f16, 0.0F - f5, f, f12);
        }
        tessellator.draw();

        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        GlStateManager.normal3f(0.0F, -1F, 0.0F);
        for (int l = 0; l < 16; l++) {
            float f9 = (float) l / 16F;
            float f13 = (f3 + (f1 - f3) * f9) - 0.001953125F;
            float f17 = f4 * f9;
            buf(vertexbuffer, f4, f17, 0.0D, f2, f13);
            buf(vertexbuffer, 0.0D, f17, 0.0D, f, f13);
            buf(vertexbuffer, 0.0D, f17, 0.0F - f5, f, f13);
            buf(vertexbuffer, f4, f17, 0.0F - f5, f2, f13);
        }

        tessellator.draw();
    }
}
