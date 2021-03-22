package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.vector.Vector3f;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;

public class BeachFloatModel extends SegmentedModel<BeachFloatEntity> {
    public ModelRenderer floatCross4;
    public ModelRenderer floatCross3;
    public ModelRenderer floatCross2;
    public ModelRenderer floatCross1;
    public ModelRenderer topFloatCross4;
    public ModelRenderer topFloatCross3;
    public ModelRenderer topFloatCross2;
    public ModelRenderer topFloatCross1;
    public ModelRenderer floatFoot;
    public ModelRenderer floatTop;
    public ModelRenderer headPillow;
    public ModelRenderer topBed;
    public ModelRenderer bottomBed;

    public BeachFloatModel() {
        floatCross4 = new ModelRenderer(this, 0, 0);
        floatCross4.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        floatCross4.setRotationPoint(0F, 23F, -6F);

        floatCross3 = new ModelRenderer(this, 0, 0);
        floatCross3.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        floatCross3.setRotationPoint(0F, 23F, -2F);

        floatCross2 = new ModelRenderer(this, 0, 0);
        floatCross2.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        floatCross2.setRotationPoint(0F, 23F, 2F);

        floatCross1 = new ModelRenderer(this, 0, 0);
        floatCross1.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        floatCross1.setRotationPoint(0F, 23F, 6F);

        topFloatCross4 = new ModelRenderer(this, 0, 0);
        topFloatCross4.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        topFloatCross4.setRotationPoint(0F, 23F, -6F);
        topFloatCross4.rotateAngleZ = 3.141593F;

        topFloatCross3 = new ModelRenderer(this, 0, 0);
        topFloatCross3.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        topFloatCross3.setRotationPoint(0F, 23F, -2F);
        topFloatCross3.rotateAngleZ = 3.141593F;

        topFloatCross2 = new ModelRenderer(this, 0, 0);
        topFloatCross2.addBox(0F, 0F, 1F, 16, 2, 2, 0F);
        topFloatCross2.setRotationPoint(0F, 24F, 0F);
        topFloatCross2.rotateAngleZ = 3.141593F;

        topFloatCross1 = new ModelRenderer(this, 0, 0);
        topFloatCross1.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        topFloatCross1.setRotationPoint(0F, 23F, 6F);
        topFloatCross1.rotateAngleZ = 3.141593F;

        floatFoot = new ModelRenderer(this, 0, 4);
        floatFoot.addBox(-7F, -1F, 0F, 14, 2, 2, 0F);
        floatFoot.setRotationPoint(16F, 23F, 0F);
        floatFoot.rotateAngleY = 1.570796F;

        floatTop = new ModelRenderer(this, 0, 4);
        floatTop.addBox(-7F, -1F, 0F, 14, 2, 2, 0F);
        floatTop.setRotationPoint(-17F, 24F, 0F);
        floatTop.rotateAngleX = 1.570796F;
        floatTop.rotateAngleY = -1.570796F;

        headPillow = new ModelRenderer(this, 0, 13);
        headPillow.addBox(-6F, -1.5F, -4F, 12, 2, 4, 0F);
        headPillow.setRotationPoint(-12F, 22F, 0F);
        headPillow.rotateAngleY = 1.570796F;

        topBed = new ModelRenderer(this, 0, 19);
        topBed.addBox(-6F, -0.5F, -6F, 14, 1, 12, 0F);
        topBed.setRotationPoint(-6F, 22F, 0F);

        bottomBed = new ModelRenderer(this, 0, 19);
        bottomBed.addBox(-6F, -0.5F, -6F, 14, 1, 12, 0F);
        bottomBed.setRotationPoint(8F, 22F, 0F);
    }

    @Override
    public void setRotationAngles(BeachFloatEntity beachFloat, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(
            floatCross4, floatCross3, floatCross2, floatCross1,
            topFloatCross4, topFloatCross3, topFloatCross2, topFloatCross1,
            floatFoot, floatTop, headPillow, topBed, bottomBed
        );
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-90));
        super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrixStackIn.pop();
    }
}
