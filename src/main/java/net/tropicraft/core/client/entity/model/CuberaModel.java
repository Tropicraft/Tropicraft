package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class CuberaModel<T extends Entity> extends EntityModel<T> {
    private final ModelRenderer body_base;
    private final ModelRenderer fin_anal;
    private final ModelRenderer fin_pelvic_right;
    private final ModelRenderer fin_pelvic_right_r1;
    private final ModelRenderer fin_pelvic_left;
    private final ModelRenderer fin_pelvic_left_r1;
    private final ModelRenderer fin_pectoral_left;
    private final ModelRenderer fin_pectoral_right;
    private final ModelRenderer fin_dorsal;
    private final ModelRenderer body_connection;
    private final ModelRenderer jaw_lower;
    private final ModelRenderer head_base;
    private final ModelRenderer head_snout;
    private final ModelRenderer head_snout_r1;
    private final ModelRenderer tail_base;
    private final ModelRenderer tail_main;
    private final ModelRenderer fin_tail;

    public boolean inWater;

    public CuberaModel() {
        textureWidth = 64;
        textureHeight = 64;

        body_base = new ModelRenderer(this);
        body_base.setRotationPoint(0.0F, 20.0F, 0.0F);
        setRotationAngle(body_base, 0.0436F, 0.0F, 0.0F);
        body_base.setTextureOffset(0, 0).addBox(-2.0F, -5.0F, -3.0F, 4.0F, 6.0F, 8.0F, 0.0F, false);

        fin_anal = new ModelRenderer(this);
        fin_anal.setRotationPoint(0.0F, 1.0F, 4.0F);
        body_base.addChild(fin_anal);
        setRotationAngle(fin_anal, -0.3054F, 0.0F, 0.0F);
        fin_anal.setTextureOffset(11, 37).addBox(0.0F, -1.0F, -1.0F, 0.0F, 2.0F, 4.0F, 0.0F, false);

        fin_pelvic_right = new ModelRenderer(this);
        fin_pelvic_right.setRotationPoint(-1.5F, 1.0F, -2.0F);
        body_base.addChild(fin_pelvic_right);

        fin_pelvic_right_r1 = new ModelRenderer(this);
        fin_pelvic_right_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        fin_pelvic_right.addChild(fin_pelvic_right_r1);
        setRotationAngle(fin_pelvic_right_r1, -1.3526F, -0.2182F, 0.0F);
        fin_pelvic_right_r1.setTextureOffset(20, 37).addBox(0.0F, -1.5F, -0.5F, 0.0F, 2.0F, 3.0F, 0.0F, false);

        fin_pelvic_left = new ModelRenderer(this);
        fin_pelvic_left.setRotationPoint(1.5F, 1.0F, -2.0F);
        body_base.addChild(fin_pelvic_left);

        fin_pelvic_left_r1 = new ModelRenderer(this);
        fin_pelvic_left_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        fin_pelvic_left.addChild(fin_pelvic_left_r1);
        setRotationAngle(fin_pelvic_left_r1, -1.3526F, 0.2182F, 0.0F);
        fin_pelvic_left_r1.setTextureOffset(27, 37).addBox(0.0F, -1.5F, -0.5F, 0.0F, 2.0F, 3.0F, 0.0F, false);

        fin_pectoral_left = new ModelRenderer(this);
        fin_pectoral_left.setRotationPoint(2.0F, -1.0F, -2.0F);
        body_base.addChild(fin_pectoral_left);
        setRotationAngle(fin_pectoral_left, 0.4363F, 0.5672F, 0.0F);
        fin_pectoral_left.setTextureOffset(7, 45).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);

        fin_pectoral_right = new ModelRenderer(this);
        fin_pectoral_right.setRotationPoint(-2.0F, -1.0F, -2.0F);
        body_base.addChild(fin_pectoral_right);
        setRotationAngle(fin_pectoral_right, 0.4363F, -0.5672F, 0.0F);
        fin_pectoral_right.setTextureOffset(0, 45).addBox(0.0F, 0.0F, 0.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);

        fin_dorsal = new ModelRenderer(this);
        fin_dorsal.setRotationPoint(0.0F, -5.0F, -1.0F);
        body_base.addChild(fin_dorsal);
        setRotationAngle(fin_dorsal, -0.3054F, 0.0F, 0.0F);
        fin_dorsal.setTextureOffset(25, 0).addBox(0.0F, -3.0F, 0.0F, 0.0F, 3.0F, 7.0F, 0.0F, false);

        body_connection = new ModelRenderer(this);
        body_connection.setRotationPoint(0.0F, 1.0F, -3.0F);
        body_base.addChild(body_connection);
        body_connection.setTextureOffset(28, 15).addBox(-2.0F, -2.0F, -4.0F, 4.0F, 2.0F, 4.0F, 0.0F, false);

        jaw_lower = new ModelRenderer(this);
        jaw_lower.setRotationPoint(0.0F, 0.0F, -4.0F);
        body_connection.addChild(jaw_lower);
        setRotationAngle(jaw_lower, -0.1309F, 0.0F, 0.0F);
        jaw_lower.setTextureOffset(15, 29).addBox(-2.0F, -1.0F, -3.0F, 4.0F, 1.0F, 3.0F, 0.0F, false);

        head_base = new ModelRenderer(this);
        head_base.setRotationPoint(0.0F, -5.0F, -3.0F);
        body_base.addChild(head_base);
        setRotationAngle(head_base, 0.4363F, 0.0F, 0.0F);
        head_base.setTextureOffset(0, 15).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 4.0F, 4.0F, 0.01F, false);

        head_snout = new ModelRenderer(this);
        head_snout.setRotationPoint(0.0F, 0.0F, -4.0F);
        head_base.addChild(head_snout);
        setRotationAngle(head_snout, 0.3054F, 0.0F, 0.0F);

        head_snout_r1 = new ModelRenderer(this);
        head_snout_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        head_snout.addChild(head_snout_r1);
        setRotationAngle(head_snout_r1, -0.1309F, 0.0F, 0.0F);
        head_snout_r1.setTextureOffset(0, 29).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 3.0F, 3.0F, 0.005F, false);

        tail_base = new ModelRenderer(this);
        tail_base.setRotationPoint(0.0F, -4.5F, 5.0F);
        body_base.addChild(tail_base);
        setRotationAngle(tail_base, -0.0436F, 0.0F, 0.0F);
        tail_base.setTextureOffset(0, 37).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 5.0F, 2.0F, 0.0F, false);

        tail_main = new ModelRenderer(this);
        tail_main.setRotationPoint(0.0F, 0.5F, 2.0F);
        tail_base.addChild(tail_main);
        tail_main.setTextureOffset(30, 29).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 3.0F, 0.0F, false);

        fin_tail = new ModelRenderer(this);
        fin_tail.setRotationPoint(0.0F, 0.0F, 3.0F);
        tail_main.addChild(fin_tail);
        fin_tail.setTextureOffset(17, 15).addBox(0.0F, -2.0F, -1.0F, 0.0F, 8.0F, 5.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        if (inWater) {
            body_base.rotateAngleZ = 0.0F;
            body_base.rotationPointY = 20.0F;
        } else {
            body_base.rotateAngleZ = 90.0F * ModelAnimator.DEG_TO_RAD;
            body_base.rotationPointY = 22.0F;
        }

        try (ModelAnimator.Cycle swim = ModelAnimator.cycle(limbSwing * 0.4F, limbSwingAmount)) {
            tail_base.rotateAngleY = swim.eval(1.0F, 1.0F, 0.0F, 0.0F);
            tail_main.rotateAngleY = swim.eval(1.0F, 1.0F, 0.25F, 0.0F);
            fin_tail.rotateAngleY = swim.eval(1.0F, 1.0F, 0.5F, 0.0F);

            fin_dorsal.rotateAngleY = swim.eval(1.0F, 0.125F);
            fin_anal.rotateAngleY = swim.eval(1.0F, 0.125F);
        }

        try (ModelAnimator.Cycle idle = ModelAnimator.cycle(age * 0.05F, 0.1F)) {
            body_base.rotationPointY += idle.eval(0.5F, 2.0F);

            jaw_lower.rotateAngleX = -7.5F * ModelAnimator.DEG_TO_RAD - idle.eval(1.0F, 0.5F, 0.0F, 1.0F);

            fin_pectoral_left.rotateAngleY = 32.5F * ModelAnimator.DEG_TO_RAD + idle.eval(0.5F, 1.0F);
            fin_pectoral_right.rotateAngleY = -32.5F * ModelAnimator.DEG_TO_RAD + idle.eval(0.5F, -1.0F);

            fin_pelvic_left_r1.rotateAngleZ = idle.eval(0.5F, -1.0F, 0.2F, 0.0F);
            fin_pelvic_right_r1.rotateAngleZ = idle.eval(0.5F, 1.0F, 0.2F, 0.0F);
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
