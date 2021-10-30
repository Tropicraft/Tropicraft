package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.tropicraft.core.common.entity.underdasea.MarlinEntity;

public class MarlinModel extends ListModel<MarlinEntity> {
    ModelPart body;
    ModelPart dorsalFin1;
    ModelPart leftFin;
    ModelPart rightFin;
    ModelPart bottomFin;
    ModelPart head;
    ModelPart tail;
    ModelPart tail1;
    ModelPart sword;
    ModelPart tail3;
    ModelPart tailEndB;
    ModelPart tailEndT;
    public boolean inWater;

    public MarlinModel() {
        texWidth = 64;
        texHeight = 32;

        body = new ModelPart(this, 0, 22);
        body.addBox(-5F, -3F, -2F, 7, 6, 4);
        body.setPos(0F, 19F, 0F);
        body.mirror = true;
        setRotation(body, 0F, -1.570796F, 0F);
        dorsalFin1 = new ModelPart(this, 24, 20);
        dorsalFin1.addBox(-0.5F, -0.5F, -0.5F, 1, 2, 10);
        dorsalFin1.setPos(0F, 15.5F, -5F);
        dorsalFin1.mirror = true;
        leftFin = new ModelPart(this, 12, 10);
        leftFin.addBox(0F, -0.5F, -2F, 4, 1, 2);
        leftFin.setPos(2F, 21F, -3F);
        leftFin.mirror = true;
        rightFin = new ModelPart(this, 12, 7);
        rightFin.addBox(-4F, -0.5F, -2F, 4, 1, 2);
        rightFin.setPos(-2F, 21F, -3F);
        rightFin.mirror = true;
        bottomFin = new ModelPart(this, 52, 0);
        bottomFin.addBox(-0.5F, 2F, -2.5F, 1, 3, 2);
        bottomFin.setPos(0F, 19F, 0F);
        bottomFin.mirror = true;
        setRotation(bottomFin, 0.6981317F, 0F, 0F);
        head = new ModelPart(this, 46, 24);
        head.setPos(0F, 20F, -5F);
        head.mirror = true;
        head.addBox(-1.5F, -3F, -3F, 3, 5, 3);
        head.texOffs(28, 0).addBox(-1F, -1.5F, -4F, 2, 3, 1);
        head.texOffs(22, 0).addBox(-0.5F, -0.5F, -6F, 1, 2, 2);
        head.texOffs(23, 24).addBox(-0.5F, -6F, -2.5F, 1, 3, 2);
        sword = new ModelPart(this);
        sword.setPos(0F, 0F, 0F);
        setRotation(sword, 0F, 1.5707F, 0F);
        sword.mirror = true;
        sword.texOffs(0, 0).addBox(4F, -1.5F, -0.5F, 10, 1, 1);
        head.addChild(sword);
        tail = new ModelPart(this);
        tail.setPos(0F, 19F, 2F);
        tail.mirror = true;
        tail.texOffs(0, 13).addBox(-1.5F, -2F, 0F, 3, 5, 4);
        tail1 = new ModelPart(this);
        tail1.setPos(0F, 0F, 4F);
        tail1.mirror = true;
        tail1.texOffs(0, 5).addBox(-1F, -1.5F, 0F, 2, 4, 4);
        tail3 = new ModelPart(this);
        tail3.setPos(0F, 1F, 4F);
        tail3.mirror = true;
        tail3.texOffs(46, 0).addBox(-0.5F, -1.5F, 0F, 1, 3, 2);
        tailEndB = new ModelPart(this);
        tailEndB.setPos(0F, 0F, 0F);
        setRotation(tailEndB, 0.593411F, 0F, 0F);
        tailEndB.mirror = true;
        tailEndB.texOffs(40, 0).addBox(-0.5F, 1F, -1F, 1, 5, 2);
        tail3.addChild(tailEndB);
        tailEndT = new ModelPart(this);
        tailEndT.setPos(0F, 0F, 0F);
        setRotation(tailEndT, 2.548179F, 0F, 0F);
        tailEndT.mirror = true;
        tailEndT.texOffs(34, 0).addBox(-0.5F, 1F, -1F, 1, 5, 2);
        tail3.addChild(tailEndT);
        tail1.addChild(tail3);
        tail.addChild(tail1);
    }

    @Override
    public void setupAnim(MarlinEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        double yAngleRot = Math.sin(ageInTicks * .25F);
        float zWaveFloat = (float) yAngleRot * .165F;
        if (!inWater) {
            float yWaveRot = (float) (Math.sin(ageInTicks * .55F) * .260F);
            head.yRot = yWaveRot;
            tail.yRot = yWaveRot;
            tail1.yRot = yWaveRot;
            tail3.yRot = yWaveRot;
            leftFin.zRot = zWaveFloat + 0.523598F;
            rightFin.zRot = -(float) yAngleRot * .165F - 0.523598F;
            leftFin.yRot = -1.5F;
            rightFin.yRot = 1.5F - zWaveFloat - 0.523598F;
        } else {
            head.yRot = (float) yAngleRot * .135F;
            tail.yRot = (float) yAngleRot * .135F;
            tail1.yRot = (float) Math.sin(ageInTicks * .35F) * .150F;
            tail3.yRot = (float) Math.sin(ageInTicks * .45F) * .160F;
            leftFin.zRot = zWaveFloat + 0.523598F;
            rightFin.zRot = -(float) yAngleRot * .165F - 0.523598F;
            leftFin.yRot = -0.392699F;
            rightFin.yRot = 0.392699F;
        }
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(body, dorsalFin1, leftFin, rightFin, bottomFin, head, tail);
    }

    private void setRotation(ModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}
