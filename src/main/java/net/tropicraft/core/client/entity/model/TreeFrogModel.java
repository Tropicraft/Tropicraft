package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.entity.neutral.TreeFrogEntity;

public class TreeFrogModel extends EntityModel<TreeFrogEntity> {

    public RendererModel frontleft_leg;
    public RendererModel frontright_leg;
    public RendererModel body;
    public RendererModel rear_right_leg;
    public RendererModel rear_left_leg;
    public RendererModel right_eye;
    public RendererModel left_eye;

    public TreeFrogModel() {
        frontleft_leg = new RendererModel(this, 12, 19);
        frontleft_leg.addBox(-2F, 0.0F, -2F, 4, 1, 4, 0.0F);
        frontleft_leg.setRotationPoint(2.0F, 23F, -3F);
        frontleft_leg.rotateAngleX = 0.0F;
        frontleft_leg.rotateAngleY = 0.0F;
        frontleft_leg.rotateAngleZ = 0.0F;
        frontleft_leg.mirror = false;
        frontright_leg = new RendererModel(this, 12, 14);
        frontright_leg.addBox(-2F, 0.0F, -2F, 4, 1, 4, 0.0F);
        frontright_leg.setRotationPoint(-2F, 23F, -3F);
        frontright_leg.rotateAngleX = 0.0F;
        frontright_leg.rotateAngleY = 0.0F;
        frontright_leg.rotateAngleZ = 0.0F;
        frontright_leg.mirror = false;
        body = new RendererModel(this, 28, 8);
        body.addBox(-2F, -5F, -2F, 4, 9, 4, 0.0F);
        body.setRotationPoint(0.0F, 21F, 1.0F);
        body.rotateAngleX = 1.570796F;
        body.rotateAngleY = 0.0F;
        body.rotateAngleZ = 0.0F;
        body.mirror = false;
        rear_right_leg = new RendererModel(this, 0, 16);
        rear_right_leg.addBox(-3F, 0.0F, -2F, 3, 5, 3, 0.0F);
        rear_right_leg.setRotationPoint(-2F, 19F, 4F);
        rear_right_leg.rotateAngleX = 0.0F;
        rear_right_leg.rotateAngleY = 0.0F;
        rear_right_leg.rotateAngleZ = 0.0F;
        rear_right_leg.mirror = false;
        rear_left_leg = new RendererModel(this, 0, 8);
        rear_left_leg.addBox(0.0F, 0.0F, -2F, 3, 5, 3, 0.0F);
        rear_left_leg.setRotationPoint(2.0F, 19F, 4F);
        rear_left_leg.rotateAngleX = 0.0F;
        rear_left_leg.rotateAngleY = 0.0F;
        rear_left_leg.rotateAngleZ = 0.0F;
        rear_left_leg.mirror = false;
        right_eye = new RendererModel(this, 0, 0);
        right_eye.addBox(-2F, -1F, -1F, 2, 2, 2, 0.0F);
        right_eye.setRotationPoint(-1F, 19F, -1F);
        right_eye.rotateAngleX = 0.0F;
        right_eye.rotateAngleY = 0.0F;
        right_eye.rotateAngleZ = 0.0F;
        right_eye.mirror = false;
        left_eye = new RendererModel(this, 0, 4);
        left_eye.addBox(0.0F, -1F, -1F, 2, 2, 2, 0.0F);
        left_eye.setRotationPoint(1.0F, 19F, -1F);
        left_eye.rotateAngleX = 0.0F;
        left_eye.rotateAngleY = 0.0F;
        left_eye.rotateAngleZ = 0.0F;
        left_eye.mirror = false;
    }

    @Override
    public void render(TreeFrogEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(entity, f, f1, f2, f3, f4, f5);
        frontleft_leg.render(f5);
        frontright_leg.render(f5);
        body.render(f5);
        rear_right_leg.render(f5);
        rear_left_leg.render(f5);
        right_eye.render(f5);
        left_eye.render(f5);
    }

    @Override
    public void setRotationAngles(TreeFrogEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        frontleft_leg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        rear_left_leg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        rear_right_leg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        frontright_leg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }
}
