package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class WhiteLippedPeccaryModel<T extends Entity> extends TropicraftAgeableModel<T> {
    private final ModelRenderer head_base;
    private final ModelRenderer head_connection;
    private final ModelRenderer ear_right;
    private final ModelRenderer ear_left;
    private final ModelRenderer head_snout_bridge;
    private final ModelRenderer head_snout;
    private final ModelRenderer head_snout_r1;
    private final ModelRenderer body_base;
    private final ModelRenderer hair_base_right;
    private final ModelRenderer hair_base_left;
    private final ModelRenderer leg_left_ba;
    private final ModelRenderer leg_right_ba;
    private final ModelRenderer leg_left_fr;
    private final ModelRenderer leg_right_fr;

    public WhiteLippedPeccaryModel() {
        textureWidth = 64;
        textureHeight = 64;

        head_base = new ModelRenderer(this);
        head_base.setRotationPoint(0.0F, 14.0F, -5.5F);
        setRotationAngle(head_base, 0.0873F, 0.0F, 0.0F);
        head_base.setTextureOffset(0, 20).addBox(-2.5F, -3.0F, -3.0F, 5.0F, 7.0F, 4.0F, 0.0F, false);

        head_connection = new ModelRenderer(this);
        head_connection.setRotationPoint(0.0F, 2.0F, -3.0F);
        head_base.addChild(head_connection);
        head_connection.setTextureOffset(0, 32).addBox(-1.5F, -1.0F, -5.0F, 3.0F, 3.0F, 5.0F, 0.005F, false);

        ear_right = new ModelRenderer(this);
        ear_right.setRotationPoint(-1.5F, -3.0F, -1.0F);
        head_base.addChild(ear_right);
        setRotationAngle(ear_right, -0.829F, -0.2618F, -0.3491F);
        ear_right.setTextureOffset(27, 41).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        ear_left = new ModelRenderer(this);
        ear_left.setRotationPoint(1.5F, -3.0F, -1.0F);
        head_base.addChild(ear_left);
        setRotationAngle(ear_left, -0.829F, 0.2618F, 0.3491F);
        ear_left.setTextureOffset(0, 50).addBox(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

        head_snout_bridge = new ModelRenderer(this);
        head_snout_bridge.setRotationPoint(0.0F, -3.0F, -3.0F);
        head_base.addChild(head_snout_bridge);
        setRotationAngle(head_snout_bridge, 0.48F, 0.0F, 0.0F);
        head_snout_bridge.setTextureOffset(19, 20).addBox(-1.5F, 0.0F, -6.0F, 3.0F, 4.0F, 6.0F, 0.0F, false);

        head_snout = new ModelRenderer(this);
        head_snout.setRotationPoint(0.0F, 0.0F, -6.0F);
        head_snout_bridge.addChild(head_snout);
        setRotationAngle(head_snout, -0.1309F, 0.0F, 0.0F);

        head_snout_r1 = new ModelRenderer(this);
        head_snout_r1.setRotationPoint(0.0F, 0.0F, 0.0F);
        head_snout.addChild(head_snout_r1);
        setRotationAngle(head_snout_r1, -0.2182F, 0.0F, 0.0F);
        head_snout_r1.setTextureOffset(18, 41).addBox(-1.5F, 0.0F, -0.5F, 3.0F, 3.0F, 1.0F, 0.006F, false);

        body_base = new ModelRenderer(this);
        body_base.setRotationPoint(0.0F, 12.0F, 4.0F);
        body_base.setTextureOffset(0, 0).addBox(-3.0F, -1.0F, -9.0F, 6.0F, 7.0F, 12.0F, 0.0F, false);

        hair_base_right = new ModelRenderer(this);
        hair_base_right.setRotationPoint(0.0F, -1.0F, -9.0F);
        body_base.addChild(hair_base_right);
        setRotationAngle(hair_base_right, -0.1309F, 0.0F, -0.2182F);
        hair_base_right.setTextureOffset(37, 14).addBox(-1.5F, -3.0F, 0.0F, 1.0F, 3.0F, 10.0F, 0.0F, false);

        hair_base_left = new ModelRenderer(this);
        hair_base_left.setRotationPoint(0.0F, -1.0F, -9.0F);
        body_base.addChild(hair_base_left);
        setRotationAngle(hair_base_left, -0.1309F, 0.0F, 0.2182F);
        hair_base_left.setTextureOffset(37, 0).addBox(0.5F, -3.0F, 0.0F, 1.0F, 3.0F, 10.0F, 0.0F, true);

        leg_left_ba = new ModelRenderer(this);
        leg_left_ba.setRotationPoint(2.0F, 5.9F, 2.0F);
        body_base.addChild(leg_left_ba);
        leg_left_ba.setTextureOffset(26, 32).addBox(-1.005F, 0.1F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        leg_right_ba = new ModelRenderer(this);
        leg_right_ba.setRotationPoint(-2.0F, 5.9F, 2.0F);
        body_base.addChild(leg_right_ba);
        leg_right_ba.setTextureOffset(17, 32).addBox(-0.995F, 0.1F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        leg_left_fr = new ModelRenderer(this);
        leg_left_fr.setRotationPoint(2.0F, 5.9F, -8.0F);
        body_base.addChild(leg_left_fr);
        leg_left_fr.setTextureOffset(9, 41).addBox(-1.005F, 0.1F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        leg_right_fr = new ModelRenderer(this);
        leg_right_fr.setRotationPoint(-2.0F, 5.9F, -8.0F);
        body_base.addChild(leg_right_fr);
        leg_right_fr.setTextureOffset(0, 41).addBox(-0.995F, 0.1F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
    }

	@Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float age, float headYaw, float headPitch) {
		ModelAnimator.look(head_base, headYaw, headPitch);

		try (ModelAnimator.Cycle walk = ModelAnimator.cycle(limbSwing * 0.2F, limbSwingAmount)) {
			leg_left_fr.rotateAngleX = walk.eval(1.0F, 1.0F);
			leg_right_fr.rotateAngleX = walk.eval(-1.0F, 1.0F);
			leg_left_ba.rotateAngleX = walk.eval(-1.0F, 1.0F);
			leg_right_ba.rotateAngleX = walk.eval(1.0F, 1.0F);
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
