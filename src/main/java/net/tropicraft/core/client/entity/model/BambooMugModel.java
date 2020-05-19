package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

public class BambooMugModel extends Model {
    public ModelRenderer base;
    public ModelRenderer wall1;
    public ModelRenderer wall2;
    public ModelRenderer wall3;
    public ModelRenderer wall4;
    public ModelRenderer liquid;
    public ModelRenderer handletop;
    public ModelRenderer handlebottom;
    public ModelRenderer handle;

    public boolean renderLiquid;
    public int liquidColor;

    public BambooMugModel() {
        textureWidth = 64;
        textureHeight = 32;

        base = new ModelRenderer(this, 10, 0);
        base.addBox(-2F, 23F, -2F, 4, 1, 4);
        base.setRotationPoint(0F, 0F, 0F);
        base.setTextureSize(64, 32);
        base.mirror = true;
        setRotation(base, 0F, 0F, 0F);
        wall1 = new ModelRenderer(this, 0, 10);
        wall1.mirror = true;
        wall1.addBox(-2F, 17F, -3F, 4, 6, 1);
        wall1.setRotationPoint(0F, 0F, 0F);
        wall1.setTextureSize(64, 32);
        setRotation(wall1, 0F, 0F, 0F);
        wall2 = new ModelRenderer(this, 0, 10);
        wall2.addBox(-2F, 17F, 2F, 4, 6, 1);
        wall2.setRotationPoint(0F, 0F, 0F);
        wall2.setTextureSize(64, 32);
        wall2.mirror = true;
        setRotation(wall2, 0F, 0F, 0F);
        wall3 = new ModelRenderer(this, 0, 0);
        wall3.addBox(2F, 17F, -2F, 1, 6, 4);
        wall3.setRotationPoint(0F, 0F, 0F);
        wall3.setTextureSize(64, 32);
        setRotation(wall3, 0F, 0F, 0F);
        wall3.mirror = true;
        wall4 = new ModelRenderer(this, 0, 0);
        wall4.addBox(-3F, 17F, -2F, 1, 6, 4);
        wall4.setRotationPoint(0F, 0F, 0F);
        wall4.setTextureSize(64, 32);
        wall4.mirror = true;
        setRotation(wall4, 0F, 0F, 0F);
        liquid = new ModelRenderer(this, 10, 5);
        liquid.addBox(-2F, 18F, -2F, 4, 1, 4);
        liquid.setRotationPoint(0F, 0F, 0F);
        liquid.setTextureSize(64, 32);
        liquid.mirror = true;
        setRotation(liquid, 0F, 0F, 0F);
        handletop = new ModelRenderer(this, 26, 0);
        handletop.addBox(-1F, 18F, -4F, 2, 1, 1);
        handletop.setRotationPoint(0F, 0F, 0F);
        handletop.setTextureSize(64, 32);
        handletop.mirror = true;
        setRotation(handletop, 0F, 0F, 0F);
        handlebottom = new ModelRenderer(this, 26, 2);
        handlebottom.addBox(-1F, 21F, -4F, 2, 1, 1);
        handlebottom.setRotationPoint(0F, 0F, 0F);
        handlebottom.setTextureSize(64, 32);
        handlebottom.mirror = true;
        setRotation(handlebottom, 0F, 0F, 0F);
        handle = new ModelRenderer(this, 32, 0);
        handle.addBox(-1F, 19F, -5F, 2, 2, 1);
        handle.setRotationPoint(0F, 0F, 0F);
        handle.setTextureSize(64, 32);
        handle.mirror = true;
        setRotation(handle, 0F, 0F, 0F);
    }

    public void renderBambooMug() {
        float f5 = 0.0625F;
        base.render(f5);
        wall1.render(f5);
        wall2.render(f5);
        wall3.render(f5);
        wall4.render(f5);
        handletop.render(f5);
        handlebottom.render(f5);
        handle.render(f5);

        if (renderLiquid) {
            float red = (float)(liquidColor >> 16 & 255) / 255.0F;
            float green = (float)(liquidColor >> 8 & 255) / 255.0F;
            float blue = (float)(liquidColor & 255) / 255.0F;

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor3f(red, green, blue);
            liquid.render(f5);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glColor3f(1f, 1f, 1f);
        }
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        //TODO
    }
}
