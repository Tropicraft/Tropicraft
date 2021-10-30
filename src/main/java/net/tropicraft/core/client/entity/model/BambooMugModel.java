package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Function;

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

    public BambooMugModel(Function<ResourceLocation, RenderType> renderTypeIn) {
        super(renderTypeIn);
        texWidth = 64;
        texHeight = 32;

        base = new ModelRenderer(this, 10, 0);
        base.addBox(-2F, 23F, -2F, 4, 1, 4);
        base.setPos(0F, 0F, 0F);
        base.setTexSize(64, 32);
        base.mirror = true;
        wall1 = new ModelRenderer(this, 0, 10);
        wall1.mirror = true;
        wall1.addBox(-2F, 17F, -3F, 4, 6, 1);
        wall1.setPos(0F, 0F, 0F);
        wall1.setTexSize(64, 32);
        wall2 = new ModelRenderer(this, 0, 10);
        wall2.addBox(-2F, 17F, 2F, 4, 6, 1);
        wall2.setPos(0F, 0F, 0F);
        wall2.setTexSize(64, 32);
        wall2.mirror = true;
        wall3 = new ModelRenderer(this, 0, 0);
        wall3.addBox(2F, 17F, -2F, 1, 6, 4);
        wall3.setPos(0F, 0F, 0F);
        wall3.setTexSize(64, 32);
        wall3.mirror = true;
        wall4 = new ModelRenderer(this, 0, 0);
        wall4.addBox(-3F, 17F, -2F, 1, 6, 4);
        wall4.setPos(0F, 0F, 0F);
        wall4.setTexSize(64, 32);
        wall4.mirror = true;
        liquid = new ModelRenderer(this, 10, 5);
        liquid.addBox(-2F, 18F, -2F, 4, 1, 4);
        liquid.setPos(0F, 0F, 0F);
        liquid.setTexSize(64, 32);
        liquid.mirror = true;
        handletop = new ModelRenderer(this, 26, 0);
        handletop.addBox(-1F, 18F, -4F, 2, 1, 1);
        handletop.setPos(0F, 0F, 0F);
        handletop.setTexSize(64, 32);
        handletop.mirror = true;
        handlebottom = new ModelRenderer(this, 26, 2);
        handlebottom.addBox(-1F, 21F, -4F, 2, 1, 1);
        handlebottom.setPos(0F, 0F, 0F);
        handlebottom.setTexSize(64, 32);
        handlebottom.mirror = true;
        handle = new ModelRenderer(this, 32, 0);
        handle.addBox(-1F, 19F, -5F, 2, 2, 1);
        handle.setPos(0F, 0F, 0F);
        handle.setTexSize(64, 32);
        handle.mirror = true;
    }

    public Iterable<ModelRenderer> getMugParts() {
        return ImmutableList.of(
            base, wall1, wall2, wall3, wall4, handletop, handlebottom, handle
        );
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        getMugParts().forEach((part) -> {
            part.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        });

        if (renderLiquid) {
            float r = (float)(liquidColor >> 16 & 255) / 255.0F;
            float g = (float)(liquidColor >> 8 & 255) / 255.0F;
            float b = (float)(liquidColor & 255) / 255.0F;

            liquid.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red * r, green * g, blue * b, alpha);
        }
    }
}
