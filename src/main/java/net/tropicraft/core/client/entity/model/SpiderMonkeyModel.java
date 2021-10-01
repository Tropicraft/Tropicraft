package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.tropicraft.core.common.entity.passive.monkey.SpiderMonkeyEntity;

public class SpiderMonkeyModel<T extends SpiderMonkeyEntity> extends EntityModel<T> {
    private final ModelRenderer body_base;
    private final ModelRenderer head_base;
    private final ModelRenderer monke;
    private final ModelRenderer tail_a;
    private final ModelRenderer tail_b;
    private final ModelRenderer arm_left_a;
    private final ModelRenderer leg_left_a;
    private final ModelRenderer leg_left_a_r1;
    private final ModelRenderer leg_right_a;
    private final ModelRenderer leg_right_a_r1;
    private final ModelRenderer arm_right_a;

    public SpiderMonkeyModel() {
        textureWidth = 64;
        textureHeight = 64;

        body_base = new ModelRenderer(this);
        body_base.setRotationPoint(0.0F, 15.0F, 4.0F);
        body_base.setTextureOffset(0, 0).addBox(-2.5F, -9.0F, -2.0F, 5.0F, 10.0F, 3.0F, 0.0F, false);

        head_base = new ModelRenderer(this);
        head_base.setRotationPoint(0.0F, -9.0F, 0.0F);
        body_base.addChild(head_base);
        head_base.setTextureOffset(17, 0).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        monke = new ModelRenderer(this);
        monke.setRotationPoint(0.0F, -1.5F, -2.5F);
        head_base.addChild(monke);
        monke.setTextureOffset(9, 39).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        tail_a = new ModelRenderer(this);
        tail_a.setRotationPoint(0.0F, 1.0F, 0.0F);
        body_base.addChild(tail_a);
        tail_a.setTextureOffset(0, 28).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

        tail_b = new ModelRenderer(this);
        tail_b.setRotationPoint(0.0F, 7.5F, 1.0F);
        tail_a.addChild(tail_b);
        tail_b.setTextureOffset(0, 39).addBox(-0.99F, 0.0F, -2.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        arm_left_a = new ModelRenderer(this);
        arm_left_a.setRotationPoint(-2.5F, -7.5F, -0.5F);
        body_base.addChild(arm_left_a);
        arm_left_a.setTextureOffset(9, 14).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);

        leg_left_a = new ModelRenderer(this);
        leg_left_a.setRotationPoint(-2.0F, 1.0F, -1.5F);
        body_base.addChild(leg_left_a);

        leg_left_a_r1 = new ModelRenderer(this);
        leg_left_a_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        leg_left_a.addChild(leg_left_a_r1);
        leg_left_a_r1.setTextureOffset(18, 28).addBox(-0.5F, -0.5F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

        leg_right_a = new ModelRenderer(this);
        leg_right_a.setRotationPoint(2.0F, 1.0F, -1.5F);
        body_base.addChild(leg_right_a);

        leg_right_a_r1 = new ModelRenderer(this);
        leg_right_a_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        leg_right_a.addChild(leg_right_a_r1);
        leg_right_a_r1.setTextureOffset(9, 28).addBox(-1.5F, -0.5F, -1.0F, 2.0F, 8.0F, 2.0F, 0.0F, false);

        arm_right_a = new ModelRenderer(this);
        arm_right_a.setRotationPoint(2.5F, -7.5F, -0.5F);
        body_base.addChild(arm_right_a);
        arm_right_a.setTextureOffset(0, 14).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 11.0F, 2.0F, 0.0F, false);

        this.setDefaultRotationAngles();
    }

    private void setDefaultRotationAngles() {
        setRotationAngle(body_base, 80.0F, 0.0F, 0.0F);
        setRotationAngle(head_base, -75.0F, 0.0F, 0.0F);
        setRotationAngle(tail_a, 65.0F, 0.0F, 0.0F);
        setRotationAngle(tail_b, -35.0F, 0.0F, 0.0F);
        setRotationAngle(arm_left_a, -82.5F, 2.5F, 2.5F);
        setRotationAngle(leg_left_a_r1, -75.0F, 5.0F, -2.5F);
        setRotationAngle(leg_right_a_r1, -75.0F, -5.0F, 2.5F);
        setRotationAngle(arm_right_a, -82.5F, -2.5F, -2.5F);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        head_base.rotateAngleX += headPitch * ModelAnimator.DEG_TO_RAD;
        head_base.rotateAngleZ -= headYaw * ModelAnimator.DEG_TO_RAD;

        try (ModelAnimator.Cycle idle = ModelAnimator.cycle(age * 0.025F, 0.05F)) {
            tail_a.rotateAngleX += idle.eval(1.0F, 1.0F, 0.0F, 0.0F);
            tail_b.rotateAngleX += idle.eval(1.0F, 1.0F, 0.2F, 0.0F);
        }
    }

    @Override
    public void setLivingAnimations(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
        this.setDefaultRotationAngles();

        float standAnimation = entity.getStandAnimation(partialTicks);
        float standAngle = standAnimation * 70.0F * ModelAnimator.DEG_TO_RAD;

        body_base.rotateAngleX -= standAngle;
        head_base.rotateAngleX += standAngle;
        arm_right_a.rotateAngleX += standAngle;
        arm_left_a.rotateAngleX += standAngle;
        leg_left_a_r1.rotateAngleX += standAngle;
        leg_right_a_r1.rotateAngleX += standAngle;

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.2F, limbSwingAmount)) {
            arm_left_a.rotateAngleX += walk.eval(1.0F, 1.0F);
            arm_right_a.rotateAngleX += walk.eval(-1.0F, 1.0F);
            leg_left_a_r1.rotateAngleX += walk.eval(-1.0F, 1.0F);
            leg_right_a_r1.rotateAngleX += walk.eval(1.0F, 1.0F);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body_base.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    private void setRotationAngle(ModelRenderer part, float x, float y, float z) {
        part.rotateAngleX = x * ModelAnimator.DEG_TO_RAD;
        part.rotateAngleY = y * ModelAnimator.DEG_TO_RAD;
        part.rotateAngleZ = z * ModelAnimator.DEG_TO_RAD;
    }
}
