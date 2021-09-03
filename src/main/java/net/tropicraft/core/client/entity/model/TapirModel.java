package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class TapirModel<T extends Entity> extends TropicraftAgeableModel<T> {
    private final ModelRenderer body_base;
    private final ModelRenderer head_base;
    private final ModelRenderer tail_base;
    private final ModelRenderer trunk_base;
    private final ModelRenderer trunk_tip;
    private final ModelRenderer ear_left;
    private final ModelRenderer ear_left_r1;
    private final ModelRenderer ear_right;
    private final ModelRenderer ear_right_r1;
    private final ModelRenderer leg_front_left;
    private final ModelRenderer leg_front_right;
    private final ModelRenderer leg_back_left;
    private final ModelRenderer leg_back_right;

    public TapirModel() {
        textureWidth = 128;
        textureHeight = 128;

        body_base = new ModelRenderer(this);
        body_base.setRotationPoint(0.0F, 7.0F, 3.0F);
        body_base.setTextureOffset(0, 0).addBox(-5.0F, -3.0F, -11.0F, 10.0F, 10.0F, 18.0F, 0.0F, false);

        head_base = new ModelRenderer(this);
        head_base.setRotationPoint(0.0F, 6.0F, -8.0F);
        setRotationAngle(head_base, 0.0873F, 0.0F, 0.0F);
        head_base.setTextureOffset(0, 29).addBox(-4.0F, -3.0F, -10.0F, 8.0F, 8.0F, 10.0F, 0.0F, false);

        tail_base = new ModelRenderer(this);
        tail_base.setRotationPoint(0.0F, -2.0F, 7.0F);
        body_base.addChild(tail_base);
        setRotationAngle(tail_base, 0.3054F, 0.0F, 0.0F);
        tail_base.setTextureOffset(4, 0).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 5.0F, 1.0F, 0.0F, false);

        trunk_base = new ModelRenderer(this);
        trunk_base.setRotationPoint(0.0F, -2.0F, -10.0F);
        head_base.addChild(trunk_base);
        setRotationAngle(trunk_base, 0.6109F, 0.0F, 0.0F);
        trunk_base.setTextureOffset(41, 0).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        trunk_tip = new ModelRenderer(this);
        trunk_tip.setRotationPoint(0.0F, 0.0F, -4.0F);
        trunk_base.addChild(trunk_tip);
        setRotationAngle(trunk_tip, 0.2618F, 0.0F, 0.0F);
        trunk_tip.setTextureOffset(40, 10).addBox(-2.0F, 0.0F, -3.0F, 4.0F, 4.0F, 3.0F, 0.001F, false);

        ear_left = new ModelRenderer(this);
        ear_left.setRotationPoint(4.0F, -3.0F, -2.0F);
        head_base.addChild(ear_left);
        setRotationAngle(ear_left, 0.0F, 0.2618F, 0.3491F);

        ear_left_r1 = new ModelRenderer(this);
        ear_left_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        ear_left.addChild(ear_left_r1);
        setRotationAngle(ear_left_r1, 0.0F, 0.0F, 0.0F);
        ear_left_r1.setTextureOffset(17, 70).addBox(0.0F, -2.0F, -2.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);

        ear_right = new ModelRenderer(this);
        ear_right.setRotationPoint(-4.0F, -3.0F, -2.0F);
        head_base.addChild(ear_right);
        setRotationAngle(ear_right, 0.0F, -0.2618F, -0.3491F);

        ear_right_r1 = new ModelRenderer(this);
        ear_right_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        ear_right.addChild(ear_right_r1);
        setRotationAngle(ear_right_r1, 0.0F, 0.0F, 0.0F);
        ear_right_r1.setTextureOffset(17, 63).addBox(-1.0F, -2.0F, -2.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);

        leg_front_left = new ModelRenderer(this);
        leg_front_left.setRotationPoint(3.0F, 7.0F, -9.0F);
        body_base.addChild(leg_front_left);
        leg_front_left.setTextureOffset(0, 63).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);

        leg_front_right = new ModelRenderer(this);
        leg_front_right.setRotationPoint(-3.0F, 7.0F, -9.0F);
        body_base.addChild(leg_front_right);
        leg_front_right.setTextureOffset(34, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);

        leg_back_left = new ModelRenderer(this);
        leg_back_left.setRotationPoint(3.0F, 7.0F, 4.0F);
        body_base.addChild(leg_back_left);
        leg_back_left.setTextureOffset(17, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);

        leg_back_right = new ModelRenderer(this);
        leg_back_right.setRotationPoint(-3.0F, 7.0F, 4.0F);
        body_base.addChild(leg_back_right);
        leg_back_right.setTextureOffset(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(head_base, headYaw, headPitch);

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.1F, limbSwingAmount)) {
            leg_front_left.rotateAngleX = walk.eval(1.0F, 1.0F);
            leg_front_right.rotateAngleX = walk.eval(-1.0F, 1.0F);
            leg_back_left.rotateAngleX = walk.eval(-1.0F, 1.0F);
            leg_back_right.rotateAngleX = walk.eval(1.0F, 1.0F);
        }
    }

    @Override
    protected ModelRenderer getHead() {
        return head_base;
    }

    @Override
    protected ModelRenderer getBody() {
        return body_base;
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
