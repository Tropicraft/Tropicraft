package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelChair extends ModelBase {

    public ModelRenderer seat;
    public ModelRenderer back;
    public ModelRenderer backRightLeg;
    public ModelRenderer backLeftLeg;
    public ModelRenderer frontLeftLeg;
    public ModelRenderer frontRightLeg;
    public ModelRenderer rightArm;
    public ModelRenderer leftArm;

    public ModelChair() {
        seat = new ModelRenderer(this, 0, 0);
        seat.addBox(-7F, 0F, -8F, 16, 1, 16, 0F);
        seat.setRotationPoint(-1F, 0F, 0F);

        seat.rotateAngleX = 0F;
        seat.rotateAngleY = 0F;
        seat.rotateAngleZ = 0F;
        seat.mirror = false;

        back = new ModelRenderer(this, 0, 0);
        back.addBox(-7F, 0F, 0F, 16, 1, 16, 0F);
        back.setRotationPoint(-1F, 0F, 8F);

        back.rotateAngleX = 1.169371F;
        back.rotateAngleY = 0F;
        back.rotateAngleZ = 0F;
        back.mirror = false;

        backRightLeg = new ModelRenderer(this, 0, 0);
        backRightLeg.addBox(-1F, -1F, 0F, 1, 10, 1, 0F);
        backRightLeg.setRotationPoint(-8F, -3F, 6F);

        backRightLeg.rotateAngleX = 0.4537856F;
        backRightLeg.rotateAngleY = 0F;
        backRightLeg.rotateAngleZ = 0F;
        backRightLeg.mirror = false;

        backLeftLeg = new ModelRenderer(this, 0, 0);
        backLeftLeg.addBox(0F, 0F, 0F, 1, 10, 1, 0F);
        backLeftLeg.setRotationPoint(8F, -4F, 5F);

        backLeftLeg.rotateAngleX = 0.4537856F;
        backLeftLeg.rotateAngleY = 0F;
        backLeftLeg.rotateAngleZ = 0F;
        backLeftLeg.mirror = false;

        frontLeftLeg = new ModelRenderer(this, 0, 0);
        frontLeftLeg.addBox(0F, 0F, -1F, 1, 10, 1, 0F);
        frontLeftLeg.setRotationPoint(8F, -4F, 0F);

        frontLeftLeg.rotateAngleX = -0.4537856F;
        frontLeftLeg.rotateAngleY = 0F;
        frontLeftLeg.rotateAngleZ = 0F;
        frontLeftLeg.mirror = false;

        frontRightLeg = new ModelRenderer(this, 0, 0);
        frontRightLeg.addBox(-1F, 0F, -1F, 1, 10, 1, 0F);
        frontRightLeg.setRotationPoint(-8F, -4F, 0F);

        frontRightLeg.rotateAngleX = -0.4537856F;
        frontRightLeg.rotateAngleY = 0F;
        frontRightLeg.rotateAngleZ = 0F;
        frontRightLeg.mirror = false;

        rightArm = new ModelRenderer(this, 0, 29);
        rightArm.addBox(0F, -1F, 0F, 14, 1, 2, 0F);
        rightArm.setRotationPoint(-10F, -4F, 11F);

        rightArm.rotateAngleX = 0F;
        rightArm.rotateAngleY = 1.570796F;
        rightArm.rotateAngleZ = 0F;
        rightArm.mirror = false;

        leftArm = new ModelRenderer(this, 0, 29);
        leftArm.addBox(0F, 0F, 0F, 14, 1, 2, 0F);
        leftArm.setRotationPoint(8F, -5F, 11F);

        leftArm.rotateAngleX = 0F;
        leftArm.rotateAngleY = 1.570796F;
        leftArm.rotateAngleZ = 0F;
        leftArm.mirror = false;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        seat.render(f5);
        back.render(f5);
        backRightLeg.render(f5);
        backLeftLeg.render(f5);
        frontLeftLeg.render(f5);
        frontRightLeg.render(f5);
        rightArm.render(f5);
        leftArm.render(f5);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
    }
}