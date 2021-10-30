package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.underdasea.MarlinEntity;

public class MarlinModel extends ListModel<MarlinEntity> {
    ModelPart body;
    ModelPart dorsalFin1;
    ModelPart leftFin;
    ModelPart rightFin;
    ModelPart bottomFin;
    ModelPart head;
    ModelPart tail1;
    ModelPart tail2;
    ModelPart sword;
    ModelPart tail3;
    ModelPart tailEndB;
    ModelPart tailEndT;
    public boolean inWater;

    public MarlinModel(ModelPart root) {
        head = root.getChild("head");
        body = root.getChild("body");
        sword = head.getChild("sword");
        tail1 = root.getChild("tail1");
        tail2 = tail1.getChild("tail2");
        tail3 = tail2.getChild("tail3");
        tailEndB = tail3.getChild("tailEndB");
        tailEndT = tail3.getChild("tailEndT");
        dorsalFin1 = root.getChild("dorsalFin1");
        leftFin = root.getChild("leftFin");
        rightFin = root.getChild("rightFin");
        bottomFin = root.getChild("bottomFin");


        /*
        textureWidth = 64;
        textureHeight = 32;

        body = new ModelPart(this, 0, 22);
        body.addCuboid(-5F, -3F, -2F, 7, 6, 4);
        body.setPivot(0F, 19F, 0F);
        body.mirror = true;
        setRotation(body, 0F, -1.570796F, 0F);

        dorsalFin1 = new ModelPart(this, 24, 20);
        dorsalFin1.addCuboid(-0.5F, -0.5F, -0.5F, 1, 2, 10);
        dorsalFin1.setPivot(0F, 15.5F, -5F);
        dorsalFin1.mirror = true;

        leftFin = new ModelPart(this, 12, 10);
        leftFin.addCuboid(0F, -0.5F, -2F, 4, 1, 2);
        leftFin.setPivot(2F, 21F, -3F);
        leftFin.mirror = true;

        rightFin = new ModelPart(this, 12, 7);
        rightFin.addCuboid(-4F, -0.5F, -2F, 4, 1, 2);
        rightFin.setPivot(-2F, 21F, -3F);
        rightFin.mirror = true;

        bottomFin = new ModelPart(this, 52, 0);
        bottomFin.addCuboid(-0.5F, 2F, -2.5F, 1, 3, 2);
        bottomFin.setPivot(0F, 19F, 0F);
        bottomFin.mirror = true;
        setRotation(bottomFin, 0.6981317F, 0F, 0F);

        head = new ModelPart(this, 46, 24);
        head.setPivot(0F, 20F, -5F);
        head.mirror = true;
        head.addCuboid(-1.5F, -3F, -3F, 3, 5, 3);
        head.setTextureOffset(28, 0).addCuboid(-1F, -1.5F, -4F, 2, 3, 1);
        head.setTextureOffset(22, 0).addCuboid(-0.5F, -0.5F, -6F, 1, 2, 2);
        head.setTextureOffset(23, 24).addCuboid(-0.5F, -6F, -2.5F, 1, 3, 2);

        sword = new ModelPart(this);
        sword.setPivot(0F, 0F, 0F);
        setRotation(sword, 0F, 1.5707F, 0F);
        sword.mirror = true;
        sword.setTextureOffset(0, 0).addCuboid(4F, -1.5F, -0.5F, 10, 1, 1);
        head.addChild(sword);

        tail1 = new ModelPart(this);
        tail1.setPivot(0F, 19F, 2F);
        tail1.mirror = true;
        tail1.setTextureOffset(0, 13).addCuboid(-1.5F, -2F, 0F, 3, 5, 4);

        tail2 = new ModelPart(this);
        tail2.setPivot(0F, 0F, 4F);
        tail2.mirror = true;
        tail2.setTextureOffset(0, 5).addCuboid(-1F, -1.5F, 0F, 2, 4, 4);
        tail1.addChild(tail2);

        tail3 = new ModelPart(this);
        tail3.setPivot(0F, 1F, 4F);
        tail3.mirror = true;
        tail3.setTextureOffset(46, 0).addCuboid(-0.5F, -1.5F, 0F, 1, 3, 2);
        tail2.addChild(tail3);

        tailEndB = new ModelPart(this);
        tailEndB.setPivot(0F, 0F, 0F);
        setRotation(tailEndB, 0.593411F, 0F, 0F);
        tailEndB.mirror = true;
        tailEndB.setTextureOffset(40, 0).addCuboid(-0.5F, 1F, -1F, 1, 5, 2);
        tail3.addChild(tailEndB);

        tailEndT = new ModelPart(this);
        tailEndT.setPivot(0F, 0F, 0F);
        setRotation(tailEndT, 2.548179F, 0F, 0F);
        tailEndT.mirror = true;
        tailEndT.setTextureOffset(34, 0).addCuboid(-0.5F, 1F, -1F, 1, 5, 2);
        tail3.addChild(tailEndT);

         */
    }

    public static LayerDefinition create() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        modelPartData.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0,22).mirror()
                        .addBox(-5F, -3F, -2F, 7, 6, 4),
                PartPose.offsetAndRotation(0F, 19F, 0F, 0F, -1.570796F, 0F));

        modelPartData.addOrReplaceChild("dorsalFin1",
                CubeListBuilder.create().texOffs(24,20).mirror()
                        .addBox(-0.5F, -0.5F, -0.5F, 1, 2, 10),
                PartPose.offset(0F, 15.5F, -5F));

        modelPartData.addOrReplaceChild("leftFin",
                CubeListBuilder.create().texOffs(12, 10).mirror()
                        .addBox(0F, -0.5F, -2F, 4, 1, 2),
                PartPose.offset(2F, 21F, -3F));

        modelPartData.addOrReplaceChild("rightFin",
                CubeListBuilder.create().texOffs(12,7).mirror()
                        .addBox(-4F, -0.5F, -2F, 4, 1, 2),
                PartPose.offset(-2F, 21F, -3F));

        modelPartData.addOrReplaceChild("bottomFin",
                CubeListBuilder.create().texOffs(52, 0).mirror()
                        .addBox(-0.5F, 2F, -2.5F, 1, 3, 2),
                PartPose.offsetAndRotation(0F, 19F, 0F, 0.6981317F, 0F, 0F));

        PartDefinition modelPartHead = modelPartData.addOrReplaceChild("head",
                CubeListBuilder.create().mirror()
                        .texOffs(46, 24)
                        .addBox(-1.5F, -3F, -3F, 3, 5, 3)
                        .texOffs(28, 0)
                        .addBox(-1F, -1.5F, -4F, 2, 3, 1)
                        .texOffs(22, 0)
                        .addBox(-0.5F, -0.5F, -6F, 1, 2, 2)
                        .texOffs(23, 24)
                        .addBox(-0.5F, -6F, -2.5F, 1, 3, 2),
                PartPose.offset(0F, 20F, -5F));

        modelPartHead.addOrReplaceChild("sword",
                CubeListBuilder.create().texOffs(0, 0).mirror()
                        .addBox(4F, -1.5F, -0.5F, 10, 1, 1),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 1.5707F, 0F));

        PartDefinition modelPartTail1 = modelPartData.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(0, 13).mirror()
                        .addBox(-1.5F, -2F, 0F, 3, 5, 4),
                PartPose.offset(0F, 19F, 2F));

        PartDefinition modelPartTail2 = modelPartTail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(0, 5).mirror()
                        .addBox(-1F, -1.5F, 0F, 2, 4, 4),
                PartPose.offset(0F, 0F, 4F));

        PartDefinition modelPartTail3 = modelPartTail2.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(46, 0).mirror()
                        .addBox(-0.5F, -1.5F, 0F, 1, 3, 2),
                PartPose.offset(0F, 1F, 4F));

        modelPartTail3.addOrReplaceChild("tailEndB",
                CubeListBuilder.create().texOffs(40, 0).mirror()
                        .addBox(-0.5F, 1F, -1F, 1, 5, 2),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0.593411F, 0F, 0F));

        modelPartTail3.addOrReplaceChild("tailEndT",
                CubeListBuilder.create().texOffs(34, 0).mirror()
                        .addBox(-0.5F, 1F, -1F, 1, 5, 2),
                PartPose.offsetAndRotation(0F, 0F, 0F, 2.548179F, 0F, 0F));


        return LayerDefinition.create(modelData,64,32);
    }

    @Override
    public void setupAnim(MarlinEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        double yAngleRot = Math.sin(ageInTicks * .25F);
        float zWaveFloat = (float) yAngleRot * .165F;
        if (!inWater) {
            float yWaveRot = (float) (Math.sin(ageInTicks * .55F) * .260F);
            head.yRot = yWaveRot;
            tail1.yRot = yWaveRot;
            tail1.yRot = yWaveRot;
            tail3.yRot = yWaveRot;
            leftFin.zRot = zWaveFloat + 0.523598F;
            rightFin.zRot = -(float) yAngleRot * .165F - 0.523598F;
            leftFin.yRot = -1.5F;
            rightFin.yRot = 1.5F - zWaveFloat - 0.523598F;
        } else {
            head.yRot = (float) yAngleRot * .135F;
            tail1.yRot = (float) yAngleRot * .135F;
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
        return ImmutableList.of(body, dorsalFin1, leftFin, rightFin, bottomFin, head, tail1);
    }

    private void setRotation(ModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}
