package net.tropicraft.core.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

public class TropicraftSpecialRenderHelper {

    public void renderMask(PoseStack stack, VertexConsumer buffer, int maskIndex, int packedLightIn, int overlayLightIn) {
        stack.pushPose();
        float f = ((float) ((maskIndex % 8) * 32) + 0.0f) / 256.0f;
        float f1 = ((float) ((maskIndex % 8) * 32) + 31.99f) / 256.0f;
        float f2 = ((float) ((maskIndex / 8) * 32) + 0.0f) / 256.0f;
        float f3 = ((float) ((maskIndex / 8) * 32) + 31.99f) / 256.0f;
        float f1shifted = ((float) ((maskIndex / 8) * 32) + 128) / 256.0f;
        float f3shifted = ((float) ((maskIndex / 8) * 32) + 159.99f) / 256.0f;
        float f4 = 0.0f;
        float f5 = 0.3f;
        stack.translate(-f4, -f5, 0.0f);
        float f6 = 2.0f;
        stack.scale(f6, f6, f6);
        stack.mulPose(Axis.YP.rotationDegrees(180.0f));
        stack.mulPose(Axis.ZP.rotationDegrees(180.0f));
        stack.translate(-0.5f, -0.5f, 0.0f);
        popper(f1, f2, f, f3, f1shifted, f3shifted, stack, buffer, packedLightIn, overlayLightIn);
        stack.popPose();
    }

    public void renderFish(PoseStack stack, VertexConsumer buffer, int index, int packedLightIn, int overlayLightIn) {
        float f = ((float) ((index % 8) * 32) + 0.0f) / 256.0f;
        float f1 = ((float) ((index % 8) * 32) + 31.99f) / 256.0f;
        float f2 = ((float) ((index / 8) * 32) + 0.0f) / 256.0f;
        float f3 = ((float) ((index / 8) * 32) + 31.99f) / 256.0f;
        float f4 = 0.0f;
        float f5 = 0.3f;
        stack.pushPose();
        stack.translate(-f4, -f5, 0.0f);
        float f6 = 1.7f;
        stack.scale(f6, f6, f6);
        stack.mulPose(Axis.YP.rotationDegrees(180.0f));
        stack.mulPose(Axis.ZP.rotationDegrees(180.0f));
        stack.translate(-0.5f, -0.5f, 0.0f);
        popper(f1, f2, f, f3, f2, f3, stack, buffer, packedLightIn, overlayLightIn);
        stack.popPose();
    }

    public static void vertex(VertexConsumer bufferIn, PoseStack ms, double x, double y, double z, float red, float green, float blue, float alpha, float texU, float texV, Direction normal, int packedLight, int packedOverlay) {
        vertex(bufferIn, ms.last(), x, y, z, red, green, blue, alpha, texU, texV, normal, packedLight, packedOverlay);
    }

    public static void vertex(VertexConsumer bufferIn, PoseStack.Pose pose, double x, double y, double z, float red, float green, float blue, float alpha, float texU, float texV, Direction normal, int packedLight, int packedOverlay) {
        Vec3i normalVec = normal.getNormal();
        bufferIn.addVertex(pose, (float) x, (float) y, (float) z).setColor(red, green, blue, alpha).setUv(texU, texV).setOverlay(packedOverlay).setLight(packedLight).setNormal(pose, normalVec.getX(), normalVec.getY(), normalVec.getZ());
    }

    public static void popper(float f, float f1, float f2, float f3, float f1shifted, float f3shifted, float layerHeight, PoseStack stack, VertexConsumer buffer, int packedLightIn, int overlayLightIn, float red, float green, float blue, float alpha) {
        float f4 = 1.0f;

        vertex(buffer, stack.last(), 0.0, 0.0, 0.0, red, green, blue, alpha, f, f3shifted, Direction.SOUTH, packedLightIn, overlayLightIn);
        vertex(buffer, stack.last(), f4, 0.0, 0.0, red, green, blue, alpha, f2, f3shifted, Direction.SOUTH, packedLightIn, overlayLightIn);
        vertex(buffer, stack.last(), f4, 1.0, 0.0, red, green, blue, alpha, f2, f1shifted, Direction.SOUTH, packedLightIn, overlayLightIn);
        vertex(buffer, stack.last(), 0.0, 1.0, 0.0, red, green, blue, alpha, f, f1shifted, Direction.SOUTH, packedLightIn, overlayLightIn);

        vertex(buffer, stack.last(), 0.0, 1.0, 0.0f - layerHeight, red, green, blue, alpha, f, f1, Direction.NORTH, packedLightIn, overlayLightIn);
        vertex(buffer, stack.last(), f4, 1.0, 0.0f - layerHeight, red, green, blue, alpha, f2, f1, Direction.NORTH, packedLightIn, overlayLightIn);
        vertex(buffer, stack.last(), f4, 0.0, 0.0f - layerHeight, red, green, blue, alpha, f2, f3, Direction.NORTH, packedLightIn, overlayLightIn);
        vertex(buffer, stack.last(), 0.0, 0.0, 0.0f - layerHeight, red, green, blue, alpha, f, f3, Direction.NORTH, packedLightIn, overlayLightIn);

        for (int i = 0; i < 32; i++) {
            float f6 = (float) i / 32.0f;
            float f10 = (f + (f2 - f) * f6) - 0.001953125f;
            float f14 = f4 * f6;
            vertex(buffer, stack.last(), f14, 0.0, 0.0f - layerHeight, red, green, blue, alpha, f10, f3, Direction.EAST, packedLightIn, overlayLightIn);
            vertex(buffer, stack.last(), f14, 0.0, 0.0, red, green, blue, alpha, f10, f3, Direction.EAST, packedLightIn, overlayLightIn);
            vertex(buffer, stack.last(), f14, 1.0, 0.0, red, green, blue, alpha, f10, f1, Direction.EAST, packedLightIn, overlayLightIn);
            vertex(buffer, stack.last(), f14, 1.0, 0.0f - layerHeight, red, green, blue, alpha, f10, f1, Direction.EAST, packedLightIn, overlayLightIn);
        }

        for (int j = 0; j < 32; j++) {
            float f7 = (float) j / 32.0f;
            float f11 = (f + (f2 - f) * f7) - 0.001953125f;
            float f15 = f4 * f7 + 0.03125f;
            vertex(buffer, stack.last(), f15, 1.0, 0.0f - layerHeight, red, green, blue, alpha, f11, f1, Direction.WEST, packedLightIn, overlayLightIn);
            vertex(buffer, stack.last(), f15, 1.0, 0.0, red, green, blue, alpha, f11, f1, Direction.WEST, packedLightIn, overlayLightIn);
            vertex(buffer, stack.last(), f15, 0.0, 0.0, red, green, blue, alpha, f11, f3, Direction.WEST, packedLightIn, overlayLightIn);
            vertex(buffer, stack.last(), f15, 0.0, 0.0f - layerHeight, red, green, blue, alpha, f11, f3, Direction.WEST, packedLightIn, overlayLightIn);
        }

        for (int k = 0; k < 32; k++) {
            float f8 = (float) k / 32.0f;
            float f12 = (f3 + (f1 - f3) * f8) - 0.001953125f;
            float f16 = f4 * f8 + 0.03125f;
            vertex(buffer, stack.last(), 0.0, f16, 0.0, red, green, blue, alpha, f, f12, Direction.UP, packedLightIn, overlayLightIn);
            vertex(buffer, stack.last(), f4, f16, 0.0, red, green, blue, alpha, f2, f12, Direction.UP, packedLightIn, overlayLightIn);
            vertex(buffer, stack.last(), f4, f16, 0.0f - layerHeight, red, green, blue, alpha, f2, f12, Direction.UP, packedLightIn, overlayLightIn);
            vertex(buffer, stack.last(), 0.0, f16, 0.0f - layerHeight, red, green, blue, alpha, f, f12, Direction.UP, packedLightIn, overlayLightIn);
        }

        for (int l = 0; l < 32; l++) {
            float f9 = (float) l / 32.0f;
            float f13 = (f3 + (f1 - f3) * f9) - 0.001953125f;
            float f17 = f4 * f9;
            vertex(buffer, stack.last(), f4, f17, 0.0, red, green, blue, alpha, f2, f13, Direction.DOWN, packedLightIn, overlayLightIn);
            vertex(buffer, stack.last(), 0.0, f17, 0.0, red, green, blue, alpha, f, f13, Direction.DOWN, packedLightIn, overlayLightIn);
            vertex(buffer, stack.last(), 0.0, f17, 0.0f - layerHeight, red, green, blue, alpha, f, f13, Direction.DOWN, packedLightIn, overlayLightIn);
            vertex(buffer, stack.last(), f4, f17, 0.0f - layerHeight, red, green, blue, alpha, f2, f13, Direction.DOWN, packedLightIn, overlayLightIn);
        }
    }

    public static void popper(float f, float f1, float f2, float f3, float f1shifted, float f3shifted, float layerHeight, PoseStack stack, VertexConsumer buffer, int packedLightIn, int overlayLightIn) {
        popper(f, f1, f2, f3, f1shifted, f3shifted, layerHeight, stack, buffer, packedLightIn, overlayLightIn, 1, 1, 1, 1);
    }

    public static void popper(float f, float f1, float f2, float f3, float f1shifted, float f3shifted, PoseStack stack, VertexConsumer buffer, int packedLightIn, int overlayLightIn) {
        popper(f, f1, f2, f3, f1shifted, f3shifted, 0.03125f, stack, buffer, packedLightIn, overlayLightIn);
    }
}
