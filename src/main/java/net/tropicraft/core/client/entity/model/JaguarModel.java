package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class JaguarModel<T extends Entity> extends TropicraftAgeableModel<T> {
    private final ModelRenderer body_base;
    private final ModelRenderer tail_base;
    private final ModelRenderer tail_tip;
    private final ModelRenderer tail_tip_r1;
    private final ModelRenderer leg_back_left;
    private final ModelRenderer torso_main;
    private final ModelRenderer leg_front_left;
    private final ModelRenderer head_base;
    private final ModelRenderer ear_left;
    private final ModelRenderer ear_left_r1;
    private final ModelRenderer head_snout;
    private final ModelRenderer ear_right;
    private final ModelRenderer ear_right_r1;
    private final ModelRenderer leg_front_right;
    private final ModelRenderer leg_back_right;

    public JaguarModel() {
        textureWidth = 128;
        textureHeight = 128;

        body_base = new ModelRenderer(this);
        body_base.setRotationPoint(0.0F, 9.0F, 4.0F);
        body_base.setTextureOffset(37, 0).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 8.0F, 10.0F, 0.0F, false);

        tail_base = new ModelRenderer(this);
        tail_base.setRotationPoint(0.0F, -1.0F, 6.0F);
        body_base.addChild(tail_base);
        setRotationAngle(tail_base, -1.0472F, 0.0F, 0.0F);
        tail_base.setTextureOffset(54, 20).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 3.0F, 9.0F, 0.0F, false);

        tail_tip = new ModelRenderer(this);
        tail_tip.setRotationPoint(0.0F, 3.0F, 9.0F);
        tail_base.addChild(tail_tip);
        setRotationAngle(tail_tip, 0.4363F, 0.0F, 0.0F);

        tail_tip_r1 = new ModelRenderer(this);
        tail_tip_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        tail_tip.addChild(tail_tip_r1);
        setRotationAngle(tail_tip_r1, 0.1745F, 0.0F, 0.0F);
        tail_tip_r1.setTextureOffset(29, 20).addBox(-1.5F, -3.0F, 0.0F, 3.0F, 3.0F, 9.0F, 0.001F, false);

        leg_back_left = new ModelRenderer(this);
        leg_back_left.setRotationPoint(4.5F, 4.0F, 3.0F);
        body_base.addChild(leg_back_left);
        leg_back_left.setTextureOffset(0, 55).addBox(-3.0F, -2.0F, -2.0F, 3.0F, 13.0F, 4.0F, 0.0F, false);

        torso_main = new ModelRenderer(this);
        torso_main.setRotationPoint(0.0F, 0.0F, -4.0F);
        body_base.addChild(torso_main);
        torso_main.setTextureOffset(0, 0).addBox(-4.0F, -1.0F, -10.0F, 8.0F, 9.0F, 10.0F, 0.0F, false);

        leg_front_left = new ModelRenderer(this);
        leg_front_left.setRotationPoint(4.0F, 2.0F, -8.0F);
        torso_main.addChild(leg_front_left);
        leg_front_left.setTextureOffset(15, 35).addBox(-2.0F, -2.0F, -1.0F, 3.0F, 15.0F, 4.0F, 0.0F, false);

        head_base = new ModelRenderer(this);
        head_base.setRotationPoint(0.0F, 9.0F, -10.0F);
        setRotationAngle(head_base, 0.0436F, 0.0F, 0.0F);
        head_base.setTextureOffset(0, 20).addBox(-3.5F, -2.0F, -7.0F, 7.0F, 7.0F, 7.0F, 0.0F, false);

        ear_left = new ModelRenderer(this);
        ear_left.setRotationPoint(2.0F, -2.0F, -3.0F);
        head_base.addChild(ear_left);
        setRotationAngle(ear_left, 0.0F, -0.5672F, 0.3927F);

        ear_left_r1 = new ModelRenderer(this);
        ear_left_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        ear_left.addChild(ear_left_r1);
        setRotationAngle(ear_left_r1, 0.0F, 0.0F, 0.0F);
        ear_left_r1.setTextureOffset(34, 55).addBox(0.0F, -2.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

        head_snout = new ModelRenderer(this);
        head_snout.setRotationPoint(0.0F, -1.0F, -7.0F);
        head_base.addChild(head_snout);
        setRotationAngle(head_snout, 0.2618F, 0.0F, 0.0F);
        head_snout.setTextureOffset(15, 55).addBox(-2.5F, 0.0F, -4.0F, 5.0F, 6.0F, 4.0F, 0.0F, false);

        ear_right = new ModelRenderer(this);
        ear_right.setRotationPoint(-2.0F, -2.0F, -3.0F);
        head_base.addChild(ear_right);
        setRotationAngle(ear_right, 0.0F, 0.5672F, -0.3927F);

        ear_right_r1 = new ModelRenderer(this);
        ear_right_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        ear_right.addChild(ear_right_r1);
        setRotationAngle(ear_right_r1, 0.0F, 0.0F, 0.0F);
        ear_right_r1.setTextureOffset(15, 66).addBox(-3.0F, -2.0F, 0.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

        leg_front_right = new ModelRenderer(this);
        leg_front_right.setRotationPoint(-4.0F, 2.0F, -8.0F);
        torso_main.addChild(leg_front_right);
        leg_front_right.setTextureOffset(0, 35).addBox(-1.0F, -2.0F, -1.0F, 3.0F, 15.0F, 4.0F, 0.0F, false);

        leg_back_right = new ModelRenderer(this);
        leg_back_right.setRotationPoint(-4.5F, 4.0F, 3.0F);
        body_base.addChild(leg_back_right);
        leg_back_right.setTextureOffset(30, 35).addBox(0.0F, -2.0F, -2.0F, 3.0F, 13.0F, 4.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
        ModelAnimator.look(head_base, headYaw, headPitch);

        try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.1F, limbSwingAmount)) {
            leg_front_left.rotateAngleX = walk.eval(1.0F, 0.8F);
            leg_front_right.rotateAngleX = walk.eval(-1.0F, 0.8F);
            leg_back_left.rotateAngleX = walk.eval(-1.0F, 0.8F);
            leg_back_right.rotateAngleX = walk.eval(1.0F, 0.8F);
        }
    }

    @Override
    protected ModelRenderer getHead() {
        return this.head_base;
    }

    @Override
    protected ModelRenderer getBody() {
        return this.body_base;
    }

    private void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
