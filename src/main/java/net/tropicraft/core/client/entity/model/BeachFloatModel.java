package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import com.mojang.math.Vector3f;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;

public class BeachFloatModel extends ListModel<BeachFloatEntity> {
    public ModelPart floatCross4;
    public ModelPart floatCross3;
    public ModelPart floatCross2;
    public ModelPart floatCross1;
    public ModelPart topFloatCross4;
    public ModelPart topFloatCross3;
    public ModelPart topFloatCross2;
    public ModelPart topFloatCross1;
    public ModelPart floatFoot;
    public ModelPart floatTop;
    public ModelPart headPillow;
    public ModelPart topBed;
    public ModelPart bottomBed;

    public BeachFloatModel() {
        floatCross4 = new ModelPart(this, 0, 0);
        floatCross4.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        floatCross4.setPos(0F, 23F, -6F);

        floatCross3 = new ModelPart(this, 0, 0);
        floatCross3.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        floatCross3.setPos(0F, 23F, -2F);

        floatCross2 = new ModelPart(this, 0, 0);
        floatCross2.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        floatCross2.setPos(0F, 23F, 2F);

        floatCross1 = new ModelPart(this, 0, 0);
        floatCross1.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        floatCross1.setPos(0F, 23F, 6F);

        topFloatCross4 = new ModelPart(this, 0, 0);
        topFloatCross4.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        topFloatCross4.setPos(0F, 23F, -6F);
        topFloatCross4.zRot = 3.141593F;

        topFloatCross3 = new ModelPart(this, 0, 0);
        topFloatCross3.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        topFloatCross3.setPos(0F, 23F, -2F);
        topFloatCross3.zRot = 3.141593F;

        topFloatCross2 = new ModelPart(this, 0, 0);
        topFloatCross2.addBox(0F, 0F, 1F, 16, 2, 2, 0F);
        topFloatCross2.setPos(0F, 24F, 0F);
        topFloatCross2.zRot = 3.141593F;

        topFloatCross1 = new ModelPart(this, 0, 0);
        topFloatCross1.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        topFloatCross1.setPos(0F, 23F, 6F);
        topFloatCross1.zRot = 3.141593F;

        floatFoot = new ModelPart(this, 0, 4);
        floatFoot.addBox(-7F, -1F, 0F, 14, 2, 2, 0F);
        floatFoot.setPos(16F, 23F, 0F);
        floatFoot.yRot = 1.570796F;

        floatTop = new ModelPart(this, 0, 4);
        floatTop.addBox(-7F, -1F, 0F, 14, 2, 2, 0F);
        floatTop.setPos(-17F, 24F, 0F);
        floatTop.xRot = 1.570796F;
        floatTop.yRot = -1.570796F;

        headPillow = new ModelPart(this, 0, 13);
        headPillow.addBox(-6F, -1.5F, -4F, 12, 2, 4, 0F);
        headPillow.setPos(-12F, 22F, 0F);
        headPillow.yRot = 1.570796F;

        topBed = new ModelPart(this, 0, 19);
        topBed.addBox(-6F, -0.5F, -6F, 14, 1, 12, 0F);
        topBed.setPos(-6F, 22F, 0F);

        bottomBed = new ModelPart(this, 0, 19);
        bottomBed.addBox(-6F, -0.5F, -6F, 14, 1, 12, 0F);
        bottomBed.setPos(8F, 22F, 0F);
    }

    @Override
    public void setupAnim(BeachFloatEntity beachFloat, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(
            floatCross4, floatCross3, floatCross2, floatCross1,
            topFloatCross4, topFloatCross3, topFloatCross2, topFloatCross1,
            floatFoot, floatTop, headPillow, topBed, bottomBed
        );
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90));
        super.renderToBuffer(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrixStackIn.popPose();
    }
}
