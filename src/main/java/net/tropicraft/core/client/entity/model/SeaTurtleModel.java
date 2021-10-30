package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.SeaTurtleEntity;

// TODO - extend QuadrupedModel?
public class SeaTurtleModel extends ListModel<SeaTurtleEntity> {
    public ModelPart body;
    public ModelPart frFlipper;
    public ModelPart flFlipper;
    public ModelPart head;
    public ModelPart rlFlipper;
    public ModelPart rrFlipper;
    public boolean inWater;

    public SeaTurtleModel() {
        inWater = false;
        texWidth = 64;
        texHeight = 64;

        body = new ModelPart(this);
        body.setPos(0F, 19F, 0F);
        setRotation(body, 0F, 0F, 0F);
        body.mirror = true;
        frFlipper = new ModelPart(this);
        frFlipper.setPos(-7F, 2F, -6F);
        setRotation(frFlipper, 0F, 0F, 0F);
        frFlipper.mirror = true;
        frFlipper.texOffs(0, 20).addBox(-10F, 0F, -3F, 10, 1, 4);
        body.addChild(frFlipper);
        flFlipper = new ModelPart(this);
        flFlipper.setPos(7F, 2F, -6F);
        setRotation(flFlipper, 0F, 0F, 0F);
        flFlipper.mirror = true;
        flFlipper.texOffs(0, 20).addBox(0F, 0F, -3F, 10, 1, 4);
        body.addChild(flFlipper);
        body.texOffs(0, 29).addBox(-4.5F, -1F, -9F, 9, 2, 1);
        body.texOffs(43, 40).addBox(-3F, -2F, 1F, 6, 1, 4);
        body.texOffs(0, 52).addBox(-7F, -2F, -8F, 14, 4, 8);
        body.texOffs(0, 41).addBox(-5F, -1F, 0F, 10, 3, 8);
        body.texOffs(0, 32).addBox(-4F, -2.5F, -6F, 8, 2, 7);
        body.texOffs(44, 55).addBox(-6F, -0.5F, 0F, 1, 2, 7);
        body.texOffs(44, 55).addBox(5F, -0.5F, 0F, 1, 2, 7);
        body.texOffs(0, 25).addBox(-4F, -0.5F, 8F, 8, 2, 2);
        head = new ModelPart(this);
        head.setPos(0F, 1F, -8F);
        setRotation(head, 0F, 0F, 0F);
        head.mirror = true;
        head.texOffs(0, 0).addBox(-1.5F, -1.5F, -6F, 3, 3, 6);
        body.addChild(head);
        rlFlipper = new ModelPart(this);
        rlFlipper.setPos(-4F, 2F, 7F);
        setRotation(rlFlipper, 0F, 0F, 0F);
        rlFlipper.mirror = true;
        rlFlipper.texOffs(0, 16).addBox(-7F, 0F, -1F, 7, 1, 3);
        body.addChild(rlFlipper);
        rrFlipper = new ModelPart(this);
        rrFlipper.setPos(4F, 2F, 7F);
        setRotation(rrFlipper, 0F, 0F, 0F);
        rrFlipper.mirror = true;
        rrFlipper.texOffs(0, 16).addBox(-1F, 0F, -1F, 7, 1, 3);
        body.addChild(rrFlipper);
    }

    @Override
    public void setupAnim(SeaTurtleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float defFront = 0.3927F;
        float defFront2 = 0.3F;
        float defRear = .5F;

        if (!entity.isInWater() && !entity.isVehicle()) {

            limbSwingAmount *= 3f;
            limbSwing *= 2f;

            body.xRot = -Math.abs(Mth.sin(limbSwing * 0.25F) * 1.25F * limbSwingAmount) - .10F;
            frFlipper.xRot = defFront2;
            frFlipper.yRot = swimRotate(limbSwing, limbSwingAmount, 0.5F, 5F, 0, defFront);
            frFlipper.zRot = swimRotate(limbSwing, limbSwingAmount, 0.5f, 1.25f, 0, -defFront2);
            flFlipper.xRot = defFront2;
            flFlipper.yRot = swimRotate(limbSwing, limbSwingAmount, 0.5f, 5f, (float) Math.PI, -defFront2);
            flFlipper.zRot = -swimRotate(limbSwing, limbSwingAmount, 0.5f, 1.25f, 0, -defFront2);
            rrFlipper.xRot = 0F;
            rrFlipper.yRot = -swimRotate(limbSwing, limbSwingAmount, 3f, 2f, 0, defRear);
            rrFlipper.zRot = 0F;
            rlFlipper.xRot = 0F;
            rlFlipper.yRot = -swimRotate(limbSwing, limbSwingAmount, 3f, 2f, 0, -defRear);
            rlFlipper.zRot = 0F;
        } else {
            limbSwingAmount *= 0.75f;
            limbSwing *= 0.1f;
            body.xRot = (float) Math.toRadians(headPitch);
            frFlipper.yRot = swimRotate(limbSwing, limbSwingAmount, 1.25f, 1.5f, 0, defFront);
            frFlipper.xRot = swimRotate(limbSwing, limbSwingAmount, 1.25f, 1.5f, (float) Math.PI / 4, defFront2 + 0.25f);
            frFlipper.zRot = 0;
            flFlipper.yRot = -swimRotate(limbSwing, limbSwingAmount, 1.25f, 1.5f, 0, defFront);
            flFlipper.zRot = 0;
            flFlipper.xRot = swimRotate(limbSwing, limbSwingAmount, 1.25f, 1.5f, (float) Math.PI / 4, defFront2 + 0.25f);
            rlFlipper.xRot = swimRotate(limbSwing, limbSwingAmount, 5f, 0.5f, (float) Math.PI / 4, 0);
            rrFlipper.xRot = swimRotate(limbSwing, limbSwingAmount, 5f, 0.5f, (float) Math.PI / 4, 0);
            rrFlipper.yRot = -0.5f;
            rlFlipper.yRot = 0.5f;
            rrFlipper.zRot = swimRotate(limbSwing, limbSwingAmount, 5f, 0.5f, 0, 0.5f);
            rlFlipper.zRot = swimRotate(limbSwing, limbSwingAmount, 5f, 0.5f, (float) Math.PI, -0.5f);;
        }
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(body);
    }
    
    private float swimRotate(float swing, float amount, float rot, float intensity, float rotOffset, float offset) {
        return Mth.cos(swing * rot + rotOffset) * amount * intensity + offset;
    }

    private void setRotation(ModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}
