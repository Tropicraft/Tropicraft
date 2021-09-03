package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class HummingbirdModel<T extends Entity> extends EntityModel<T> {
    private final ModelRenderer body_base;
    private final ModelRenderer tail_base;
    private final ModelRenderer wing_left;
    private final ModelRenderer head_base;
    private final ModelRenderer beak_base;
    private final ModelRenderer wing_right;

    public HummingbirdModel() {
        textureWidth = 32;
        textureHeight = 32;

        body_base = new ModelRenderer(this);
        body_base.setRotationPoint(0.0F, 20.0F, 0.0F);
        setRotationAngle(body_base, 0.4363F, 0.0F, 0.0F);
        body_base.setTextureOffset(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        tail_base = new ModelRenderer(this);
        tail_base.setRotationPoint(0.0F, 1.0F, 1.0F);
        body_base.addChild(tail_base);
        setRotationAngle(tail_base, 0.2618F, 0.0F, 0.0F);
        tail_base.setTextureOffset(0, 6).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 4.0F, 0.0F, 0.0F, false);

        wing_left = new ModelRenderer(this);
        wing_left.setRotationPoint(1.0F, -2.0F, 1.0F);
        body_base.addChild(wing_left);
        wing_left.setTextureOffset(9, 11).addBox(0.0F, 0.0F, 0.0F, 4.0F, 2.0F, 0.0F, 0.0F, false);

        head_base = new ModelRenderer(this);
        head_base.setRotationPoint(0.0F, -2.0F, 0.0F);
        body_base.addChild(head_base);
        setRotationAngle(head_base, -0.2618F, 0.0F, 0.0F);
        head_base.setTextureOffset(9, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.001F, false);

        beak_base = new ModelRenderer(this);
        beak_base.setRotationPoint(0.0F, -2.0F, -1.0F);
        head_base.addChild(beak_base);
        setRotationAngle(beak_base, 0.3927F, 0.0F, 0.0F);
        beak_base.setTextureOffset(7, 6).addBox(0.0F, 0.0F, -3.0F, 0.0F, 1.0F, 3.0F, 0.0F, false);

        wing_right = new ModelRenderer(this);
        wing_right.setRotationPoint(-1.0F, -2.0F, 1.0F);
        body_base.addChild(wing_right);
        wing_right.setTextureOffset(0, 11).addBox(-4.0F, 0.0F, 0.0F, 4.0F, 2.0F, 0.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(head_base, headYaw, headPitch);

        try (ModelAnimator.Cycle fly = ModelAnimator.cycle(age * 0.25F, 1.0F)) {
            body_base.rotationPointY = 20.0F + fly.eval(1.0F, 0.1F);

            wing_right.rotateAngleY = fly.eval(1.0F, 1.0F, 0.0F, 0.0F);
            wing_left.rotateAngleY = fly.eval(1.0F, -1.0F, 0.0F, 0.0F);
            wing_right.rotateAngleZ = fly.eval(1.0F, 0.4F, 0.0F, 0.3F);
            wing_left.rotateAngleZ = fly.eval(1.0F, -0.4F, 0.0F, -0.3F);
            wing_right.rotateAngleX = fly.eval(1.0F, 0.4F, 0.1F, 0.2F);
            wing_left.rotateAngleX = fly.eval(1.0F, 0.4F, 0.1F, 0.2F);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body_base.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
