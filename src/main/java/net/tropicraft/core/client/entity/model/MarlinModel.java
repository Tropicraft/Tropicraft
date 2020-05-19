package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.tropicraft.core.common.entity.underdasea.MarlinEntity;

public class MarlinModel extends SegmentedModel<MarlinEntity> {
    ModelRenderer body;
    ModelRenderer dorsalFin1;
    ModelRenderer leftFin;
    ModelRenderer rightFin;
    ModelRenderer bottomFin;
    ModelRenderer head;
    ModelRenderer tail;
    ModelRenderer tail1;
    ModelRenderer sword;
    ModelRenderer tail3;
    ModelRenderer tailEndB;
    ModelRenderer tailEndT;
    public boolean inWater;

    public MarlinModel() {
        textureWidth = 64;
        textureHeight = 32;

        body = new ModelRenderer(this, 0, 22);
        body.addBox(-5F, -3F, -2F, 7, 6, 4);
        body.setRotationPoint(0F, 19F, 0F);
        body.mirror = true;
        setRotation(body, 0F, -1.570796F, 0F);
        dorsalFin1 = new ModelRenderer(this, 24, 20);
        dorsalFin1.addBox(-0.5F, -0.5F, -0.5F, 1, 2, 10);
        dorsalFin1.setRotationPoint(0F, 15.5F, -5F);
        dorsalFin1.mirror = true;
        leftFin = new ModelRenderer(this, 12, 10);
        leftFin.addBox(0F, -0.5F, -2F, 4, 1, 2);
        leftFin.setRotationPoint(2F, 21F, -3F);
        leftFin.mirror = true;
        rightFin = new ModelRenderer(this, 12, 7);
        rightFin.addBox(-4F, -0.5F, -2F, 4, 1, 2);
        rightFin.setRotationPoint(-2F, 21F, -3F);
        rightFin.mirror = true;
        bottomFin = new ModelRenderer(this, 52, 0);
        bottomFin.addBox(-0.5F, 2F, -2.5F, 1, 3, 2);
        bottomFin.setRotationPoint(0F, 19F, 0F);
        bottomFin.mirror = true;
        setRotation(bottomFin, 0.6981317F, 0F, 0F);
        head = new ModelRenderer(this, 46, 24);
        head.setRotationPoint(0F, 20F, -5F);
        head.mirror = true;
        head.addBox(-1.5F, -3F, -3F, 3, 5, 3);
        head.setTextureOffset(28, 0).addBox(-1F, -1.5F, -4F, 2, 3, 1);
        head.setTextureOffset(22, 0).addBox(-0.5F, -0.5F, -6F, 1, 2, 2);
        head.setTextureOffset(23, 24).addBox(-0.5F, -6F, -2.5F, 1, 3, 2);
        sword = new ModelRenderer(this);
        sword.setRotationPoint(0F, 0F, 0F);
        setRotation(sword, 0F, 1.5707F, 0F);
        sword.mirror = true;
        sword.setTextureOffset(0, 0).addBox(4F, -1.5F, -0.5F, 10, 1, 1);
        head.addChild(sword);
        tail = new ModelRenderer(this);
        tail.setRotationPoint(0F, 19F, 2F);
        tail.mirror = true;
        tail.setTextureOffset(0, 13).addBox(-1.5F, -2F, 0F, 3, 5, 4);
        tail1 = new ModelRenderer(this);
        tail1.setRotationPoint(0F, 0F, 4F);
        tail1.mirror = true;
        tail1.setTextureOffset(0, 5).addBox(-1F, -1.5F, 0F, 2, 4, 4);
        tail3 = new ModelRenderer(this);
        tail3.setRotationPoint(0F, 1F, 4F);
        tail3.mirror = true;
        tail3.setTextureOffset(46, 0).addBox(-0.5F, -1.5F, 0F, 1, 3, 2);
        tailEndB = new ModelRenderer(this);
        tailEndB.setRotationPoint(0F, 0F, 0F);
        setRotation(tailEndB, 0.593411F, 0F, 0F);
        tailEndB.mirror = true;
        tailEndB.setTextureOffset(40, 0).addBox(-0.5F, 1F, -1F, 1, 5, 2);
        tail3.addChild(tailEndB);
        tailEndT = new ModelRenderer(this);
        tailEndT.setRotationPoint(0F, 0F, 0F);
        setRotation(tailEndT, 2.548179F, 0F, 0F);
        tailEndT.mirror = true;
        tailEndT.setTextureOffset(34, 0).addBox(-0.5F, 1F, -1F, 1, 5, 2);
        tail3.addChild(tailEndT);
        tail1.addChild(tail3);
        tail.addChild(tail1);
    }

    @Override
    public void setRotationAngles(MarlinEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        double yAngleRot = Math.sin(ageInTicks * .25F);
        float zWaveFloat = (float) yAngleRot * .165F;
        if (!inWater) {
            float yWaveRot = (float) (Math.sin(ageInTicks * .55F) * .260F);
            head.rotateAngleY = yWaveRot;
            tail.rotateAngleY = yWaveRot;
            tail1.rotateAngleY = yWaveRot;
            tail3.rotateAngleY = yWaveRot;
            leftFin.rotateAngleZ = zWaveFloat + 0.523598F;
            rightFin.rotateAngleZ = -(float) yAngleRot * .165F - 0.523598F;
            leftFin.rotateAngleY = -1.5F;
            rightFin.rotateAngleY = 1.5F - zWaveFloat - 0.523598F;
        } else {
            head.rotateAngleY = (float) yAngleRot * .135F;
            tail.rotateAngleY = (float) yAngleRot * .135F;
            tail1.rotateAngleY = (float) Math.sin(ageInTicks * .35F) * .150F;
            tail3.rotateAngleY = (float) Math.sin(ageInTicks * .45F) * .160F;
            leftFin.rotateAngleZ = zWaveFloat + 0.523598F;
            rightFin.rotateAngleZ = -(float) yAngleRot * .165F - 0.523598F;
            leftFin.rotateAngleY = -0.392699F;
            rightFin.rotateAngleY = 0.392699F;
        }
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(body, dorsalFin1, leftFin, rightFin, bottomFin, head, tail);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
