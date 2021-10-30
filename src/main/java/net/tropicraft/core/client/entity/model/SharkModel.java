package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.tropicraft.core.common.entity.underdasea.SharkEntity;

public class SharkModel extends ListModel<SharkEntity> {
    ModelPart Head1;
    ModelPart Head3;
    ModelPart Body1Upper;
    ModelPart Body1Lower;
    ModelPart Body2Upper;
    ModelPart Body2Lower;
    ModelPart Body3UpperLeft;
    ModelPart Body3LowerLeft;
    ModelPart Body3LowerRight;
    ModelPart FinPectoralLeft;
    ModelPart FinPectoralRight;
    ModelPart FinDorsal;
    ModelPart FinPelvicLeft;
    ModelPart FinPelvicRight;
    ModelPart FinAdipose;
    ModelPart FinAnal;
    ModelPart FinCaudalUpper;
    ModelPart FinCaudalLower;
    ModelPart Body3Lower;
    ModelPart Body4Lower;
    ModelPart Head2;

    public SharkModel() {
        texWidth = 128;
        texHeight = 64;

        Head1 = new ModelPart(this, 0, 24);
        Head1.addBox(-8F, -11.8F, -2.6F, 16, 6, 2);
        Head1.setPos(0F, 0.5F, -14F);
        Head1.mirror = true;
        setRotation(Head1, 1.527163F, 0F, 0F);
        Head3 = new ModelPart(this, 0, 46);
        Head3.addBox(-2.5F, -7F, -3.9F, 5, 14, 2);
        Head3.setPos(0F, 0.5F, -14F);
        Head3.mirror = true;
        setRotation(Head3, 1.48353F, 0F, 0F);
        Body1Upper = new ModelPart(this, 18, 0);
        Body1Upper.addBox(-2.5F, -17F, 0F, 5, 18, 6);
        Body1Upper.setPos(0F, 0F, 3F);
        Body1Upper.mirror = true;
        setRotation(Body1Upper, 1.780236F, 0F, 0F);
        Body1Lower = new ModelPart(this, 28, 47);
        Body1Lower.addBox(-4F, -11F, -5F, 8, 12, 5);
        Body1Lower.setPos(0F, 0F, 3F);
        Body1Lower.mirror = true;
        setRotation(Body1Lower, 1.570796F, 0F, 0F);

        Body2Upper = new ModelPart(this, 40, 0);
        Body2Upper.addBox(-2F, -0.8F, 0F, 4, 21, 6);
        Body2Upper.setPos(0F, 0F, 3F);
        Body2Upper.mirror = true;
        setRotation(Body2Upper, 1.48353F, 0F, 0F);

        Body2Lower = new ModelPart(this, 52, 39);
        Body2Lower.addBox(-3F, 0F, -5F, 6, 20, 5);
        Body2Lower.setPos(0F, 0F, 3F);
        Body2Lower.mirror = true;
        setRotation(Body2Lower, 1.623156F, 0F, 0F);

        Body3UpperLeft = new ModelPart(this, 60, 0);
        Body3UpperLeft.addBox(-1F, -0.3F, -1F, 2, 15, 5);
        Body3UpperLeft.setPos(0F, 0F, 22F);
        Body3UpperLeft.mirror = true;
        setRotation(Body3UpperLeft, 1.48353F, 0F, 0F);

        Body3LowerLeft = new ModelPart(this, 74, 45);
        Body3LowerLeft.addBox(0F, 0F, -4F, 2, 14, 5);
        Body3LowerLeft.setPos(0F, 0F, 22F);
        Body3LowerLeft.mirror = true;
        setRotation(Body3LowerLeft, 1.692969F, -0.0698132F, 0F);

        Body3LowerRight = new ModelPart(this, 74, 45);
        Body3LowerRight.mirror = true;
        Body3LowerRight.addBox(-2F, 0F, -4F, 2, 14, 5);
        Body3LowerRight.setPos(0F, 0F, 22F);
        Body3LowerRight.mirror = true;
        setRotation(Body3LowerRight, 1.692969F, 0.0698132F, 0F);

        Body3LowerRight.mirror = false;
        FinPectoralLeft = new ModelPart(this, 88, 57);
        FinPectoralLeft.addBox(0F, 0F, 0F, 14, 7, 0);
        FinPectoralLeft.setPos(4F, 4F, -7F);
        FinPectoralLeft.mirror = true;
        setRotation(FinPectoralLeft, 2.007129F, -0.7853982F, 0.4363323F);

        FinPectoralRight = new ModelPart(this, 88, 57);
        FinPectoralRight.mirror = true;
        FinPectoralRight.addBox(-14F, 0F, 0F, 14, 7, 0);
        FinPectoralRight.setPos(-4F, 4F, -7F);
        FinPectoralRight.mirror = true;
        setRotation(FinPectoralRight, 2.007129F, 0.7853982F, -0.4363323F);
        FinPectoralRight.mirror = false;

        FinDorsal = new ModelPart(this, 94, -7);
        FinDorsal.addBox(0F, -15F, -2F, 0, 14, 7);
        FinDorsal.setPos(0F, -4F, 4F);
        FinDorsal.mirror = true;
        setRotation(FinDorsal, -0.5235988F, 0F, 0F);

        FinPelvicLeft = new ModelPart(this, 96, 52);
        FinPelvicLeft.addBox(0F, 0F, 0F, 5, 3, 0);
        FinPelvicLeft.setPos(3F, 4F, 17F);
        FinPelvicLeft.mirror = true;
        setRotation(FinPelvicLeft, 2.181662F, -0.7853982F, 0.6981317F);

        FinPelvicRight = new ModelPart(this, 96, 52);
        FinPelvicRight.mirror = true;
        FinPelvicRight.addBox(-5F, 0F, 0F, 5, 3, 0);
        FinPelvicRight.setPos(-3F, 4F, 17F);
        FinPelvicRight.mirror = true;
        setRotation(FinPelvicRight, 2.181662F, 0.7853982F, -0.6981317F);
        FinPelvicRight.mirror = false;

        FinAdipose = new ModelPart(this, 109, -3);
        FinAdipose.addBox(0F, -5F, 0F, 0, 5, 3);
        FinAdipose.setPos(0F, -3.8F, 24F);
        FinAdipose.mirror = true;
        setRotation(FinAdipose, -0.7853982F, 0F, 0F);

        FinAnal = new ModelPart(this, 108, 47);
        FinAnal.addBox(0F, 0F, 0F, 0, 5, 3);
        FinAnal.setPos(0F, 3.6F, 25F);
        FinAnal.mirror = true;
        setRotation(FinAnal, 0.8726646F, 0F, 0F);

        FinCaudalUpper = new ModelPart(this, 116, -6);
        FinCaudalUpper.addBox(0F, -20F, -2F, 0, 20, 6);
        FinCaudalUpper.setPos(0F, 0F, 35F);
        FinCaudalUpper.mirror = true;
        setRotation(FinCaudalUpper, -0.9599311F, 0F, 0F);

        FinCaudalLower = new ModelPart(this, 116, 46);
        FinCaudalLower.addBox(0F, -12.53333F, -4F, 0, 12, 6);
        FinCaudalLower.setPos(0F, 0F, 35F);
        FinCaudalLower.mirror = true;
        setRotation(FinCaudalLower, -2.356194F, 0F, 0F);

        Body3Lower = new ModelPart(this, 14, 48);
        Body3Lower.addBox(3F, -21F, -5.6F, 2, 11, 5);
        Body3Lower.setPos(0F, 0F, 3F);
        Body3Lower.mirror = true;
        setRotation(Body3Lower, 1.500983F, 0.0907571F, 0F);

        Body4Lower = new ModelPart(this, 14, 48);
        Body4Lower.mirror = true;
        Body4Lower.addBox(-5F, -21F, -5.6F, 2, 11, 5);
        Body4Lower.setPos(0F, 0F, 3F);
        Body4Lower.mirror = true;
        setRotation(Body4Lower, 1.500983F, -0.0907571F, 0F);
        Body1Lower.mirror = false;

        Head2 = new ModelPart(this, 0, 0);
        Head2.addBox(-3F, -8.8F, 0F, 6, 9, 3);
        Head2.setPos(0F, 0.5F, -14F);
        Head2.mirror = true;
        setRotation(Head2, 1.919862F, 0F, 0F);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        RenderSystem.enableCull();
        parts().forEach((part) -> {
            // TODO some fins had special offsets based on time = missing in 1.15
            part.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        });
        RenderSystem.disableCull();
    }

    @Override
    public void setupAnim(SharkEntity shark, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float timeScale = 0.05f;

        if (!shark.isInWater()) {
            timeScale = 0.2f;
        }

        FinPectoralLeft.zRot = (0.4f -(float) Math.sin(ageInTicks * timeScale)*0.3f);
        FinPectoralRight.zRot = (-0.4f -(float) Math.sin(ageInTicks * timeScale)*0.3f);

        Body3UpperLeft.yRot =  -(float) Math.sin(ageInTicks * timeScale)*0.2f;
        Body3LowerLeft.yRot =  -(float) Math.sin(ageInTicks * timeScale)*0.2f;
        Body3LowerRight.yRot =  -(float) Math.sin(ageInTicks * timeScale)*0.2f;

        //FinCaudalUpper.offsetX = -(float) Math.sin(ageInTicks * timeScale)*0.175f;
        FinCaudalUpper.yRot =  -(float) Math.sin(ageInTicks * timeScale)*0.2f;

        //FinCaudalLower.offsetX = -(float) Math.sin(ageInTicks * timeScale)*0.175f;
        FinCaudalLower.yRot =  -(float) Math.sin(ageInTicks * timeScale)*0.2f;

        //FinAdipose.offsetX = -(float) Math.sin(ageInTicks * timeScale)*0.025f;
        FinAdipose.yRot =  -(float) Math.sin(ageInTicks * timeScale)*0.2f;

        //FinAnal.offsetX = -(float) Math.sin(ageInTicks * timeScale)*0.025f;
        FinAnal.yRot =  -(float) Math.sin(ageInTicks * timeScale)*0.2f;
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(
            Head1, Head2, Head3, Body1Upper, Body1Lower, Body2Upper, Body2Lower,
            Body3UpperLeft, Body3LowerLeft, Body3LowerRight, Body3Lower,
            FinCaudalLower, FinCaudalUpper, FinPectoralLeft, FinPectoralRight,
            FinDorsal, FinPelvicLeft, FinPelvicRight, FinAdipose, FinAnal,
            Body4Lower
        );
    }

    private void setRotation(ModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}
