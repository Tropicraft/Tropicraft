package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.tropicraft.core.common.entity.egg.EggEntity;

public class EggModel extends ListModel<EggEntity> {
    public ModelPart body;

    public EggModel(ModelPart root) {
        this.body = root.getChild("body");

        /*
        textureWidth = 64;
        textureHeight = 32;

        body = new ModelPart(this);
        body.setPivot(0F, 24F, 0F);
        body.mirror = true;
        body.setTextureOffset(0, 16).addCuboid(-3F, -10F, -3F, 6, 10, 6);
        body.setTextureOffset(0, 0).addCuboid(-1.5F, -11F, -1.5F, 3, 1, 3);
        body.setTextureOffset(0, 7).addCuboid(3F, -7F, -1.5F, 1, 6, 3);
        body.setTextureOffset(24, 9).addCuboid(-1.5F, -7F, 3F, 3, 6, 1);
        body.setTextureOffset(16, 7).addCuboid(-4F, -7F, -1.5F, 1, 6, 3);
        body.setTextureOffset(8, 9).addCuboid(-1.5F, -7F, -4F, 3, 6, 1);
         */
    }

    public static LayerDefinition create() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        //body.mirror = true;
        modelPartData.addOrReplaceChild("body", CubeListBuilder.create().mirror(true)
                .texOffs(0,16)
                .addBox(-3F, -10F, -3F, 6, 10, 6)
                .texOffs(0,0)
                .addBox(-1.5F, -11F, -1.5F, 3, 1, 3)
                .texOffs(0,7)
                .addBox(3F, -7F, -1.5F, 1, 6, 3)
                .texOffs(24,9)
                .addBox(-1.5F, -7F, 3F, 3, 6, 1)
                .texOffs(16,7)
                .addBox(-4F, -7F, -1.5F, 1, 6, 3)
                .texOffs(8,9)
                .addBox(-1.5F, -7F, -4F, 3, 6, 1), PartPose.offset(0F,24F,0F));
        return LayerDefinition.create(modelData,64,32);
    }

    @Override
    public void setupAnim(EggEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(body);
    }

    @Override
    public void prepareMobModel(EggEntity entityliving, float limbSwing, float limbSwingAmount, float partialTick) {
        boolean hatching = entityliving.isNearHatching();
        double randRotator = entityliving.rotationRand;
        body.yRot = 0F;
        if (hatching) {
            body.yRot = (float) (Math.sin((entityliving.tickCount + partialTick) * .6)) * .6f;
            body.xRot = (float) ((Math.sin(randRotator * 4))) * .6f;
            body.zRot = (float) ((Math.cos(randRotator * 4))) * .6f;
        } else {
            body.xRot = 0F;
            body.zRot = 0F;
        }
    }
    
    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn,
            float red, float green, float blue, float alpha) {
        super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}