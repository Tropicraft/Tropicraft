package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

public class KoaModel extends HumanoidModel<EntityKoaBase> {
    
    private static class ModelRendererCull extends ModelPart {
        
        public ModelRendererCull(Model model, int texOffX, int texOffY) {
            super(model, texOffX, texOffY);
        }

        @Override
        public void render(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
//            RenderSystem.enableCull();
            super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
//            RenderSystem.disableCull();
        }
    }

    public ModelPart headband;
    public ModelPart armbandR;
    public ModelPart leaf;
    public ModelPart leaf3;
    public ModelPart leaf2;
    public ModelPart leaf4;
    public ModelPart leaf5;
    public ModelPart leaf6;
    public ModelPart leaf7;
    public ModelPart leaf8;
    public ModelPart leaf9;
    public ModelPart leaf10;
    public ModelPart armbandL;

    public KoaModel(float modelSize) {
        super(modelSize);
        head = new ModelPart(this, 0, 2);
        head.addBox(-4F, -8F, -4F, 8, 8, 8);
        head.setPos(0F, 0F, 0F);
        head.setTexSize(64, 32);
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);
        headband = new ModelPart(this, 24, 1);
        headband.addBox(-5F, 0F, -5F, 10, 2, 10);
        headband.setPos(0F, -7F, 0F);        //0,-7,0 before
        headband.setTexSize(64, 32);
        headband.mirror = true;
        head.addChild(headband);
        setRotation(headband, 0F, 0F, 0F);
        armbandR = new ModelPart(this, 35, 6);
        armbandR.addBox(2.5F, -2F, -2.5F, 5, 1, 5);    //offset, dimensions
        armbandR.setPos(-6F, 3F, 0F);    //position
        armbandR.setTexSize(64, 32);
        rightArm.addChild(armbandR);
        setRotation(armbandR, 0F, 0F, 0F);
        armbandL = new ModelPart(this, 34, 1);
        armbandL.addBox(-7.5F, -2F, -2.5F, 5, 1, 5);        //offset, dimensions
        armbandL.setPos(6F, 3F, 0F);        //position
        armbandL.setTexSize(64, 32);
        armbandL.mirror = true;
        leftArm.addChild(armbandL);
        setRotation(armbandL, 0F, 0F, 0F);
        leaf = new ModelRendererCull(this, 0, 0);
        leaf.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf.setPos(2F, -6F, -6F);
        leaf.setTexSize(64, 32);
        leaf.mirror = true;
        headband.addChild(leaf);
        setRotation(leaf, 0F, 0F, 0F);
        leaf3 = new ModelRendererCull(this, 0, 0);
        leaf3.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf3.setPos(-1F, -6F, -6F);
        leaf3.setTexSize(64, 32);
        leaf3.mirror = true;
        headband.addChild(leaf3);
        setRotation(leaf3, 0F, 0F, 0F);
        leaf2 = new ModelRendererCull(this, 0, 0);
        leaf2.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf2.setPos(-4F, -6F, -6F);
        leaf2.setTexSize(64, 32);
        leaf2.mirror = true;
        headband.addChild(leaf2);
        setRotation(leaf2, 0F, 0F, 0F);
        leaf4 = new ModelRendererCull(this, 0, 0);
        leaf4.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf4.setPos(0F, -7F, -6F);
        leaf4.setTexSize(64, 32);
        leaf4.mirror = true;
        headband.addChild(leaf4);
        setRotation(leaf4, 0F, 0F, 0F);
        leaf5 = new ModelRendererCull(this, 0, 0);
        leaf5.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf5.setPos(5F, -6F, -1F);
        leaf5.setTexSize(64, 32);
        leaf5.mirror = true;
        headband.addChild(leaf5);
        setRotation(leaf5, 0F, 0F, 0F);
        leaf6 = new ModelRendererCull(this, 0, 0);
        leaf6.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf6.setPos(5F, -6F, 3F);
        leaf6.setTexSize(64, 32);
        leaf6.mirror = true;
        headband.addChild(leaf6);
        setRotation(leaf6, 0F, 0F, 0F);
        leaf7 = new ModelRendererCull(this, 0, 0);
        leaf7.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf7.setPos(-6F, -6F, 0F);
        leaf7.setTexSize(64, 32);
        leaf7.mirror = true;
        headband.addChild(leaf7);
        setRotation(leaf7, 0F, 0F, 0F);
        leaf8 = new ModelRendererCull(this, 0, 0);
        leaf8.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf8.setPos(-6F, -6F, -4F);
        leaf8.setTexSize(64, 32);
        leaf8.mirror = true;
        headband.addChild(leaf8);
        setRotation(leaf8, 0F, 0F, 0F);
        leaf9 = new ModelRendererCull(this, 0, 0);
        leaf9.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf9.setPos(-2F, -6F, 5F);
        leaf9.setTexSize(64, 32);
        leaf9.mirror = true;
        headband.addChild(leaf9);
        setRotation(leaf9, 0F, 0F, 0F);
        leaf10 = new ModelRendererCull(this, 0, 0);
        leaf10.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf10.setPos(2F, -6F, 5F);
        leaf10.setTexSize(64, 32);
        leaf10.mirror = true;
        headband.addChild(leaf10);
        setRotation(leaf10, 0F, 0F, 0F);
        
        hat.visible = false;
    }

    @Override
    public void renderToBuffer(PoseStack ms, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn,
            float red, float green, float blue, float alpha) {

        ms.pushPose();
//        if (isSitting) {
//            if (isChild) {
//                ms.translate(0, 0.3, 0);
//            } else {
//                ms.translate(0, 0.6, 0);
//            }
//        }
        
        if (this.young) {
            ms.pushPose();
            ms.scale(0.75F, 0.75F, 0.75F);
            ms.translate(0.0F, 1.0F, 0.0F);
            this.head.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            ms.popPose();
            ms.pushPose();
            ms.scale(0.5F, 0.5F, 0.5F);
            ms.translate(0.0F, 1.5F, 0.0F);
            this.body.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            this.rightArm.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            this.leftArm.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            this.rightLeg.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            this.leftLeg.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            ms.popPose();
        } else {
            head.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            body.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            rightArm.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            leftArm.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            rightLeg.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            leftLeg.render(ms, bufferIn, packedLightIn, packedOverlayIn);
        }
        ms.popPose();
    }

    @Override
    public void setupAnim(EntityKoaBase entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        hat.visible = false;

        riding = entityIn.isSitting() || entityIn.isPassenger();
        final boolean isDancing = entityIn.isDancing();


        float ticks = (entityIn.tickCount + Minecraft.getInstance().getFrameTime()) % 360;

        final double headRot = Math.cos(Math.toRadians(ticks * 35F));
        if (isDancing) {
            // TODO remove translate calls in this method
       //     RenderSystem.translated(0, 0.01F + (float)Math.sin(Math.toRadians(ticks * 35F)) * 0.02F, 0);
      //      RenderSystem.translated((float) headRot * 0.02F, 0, 0);
//            bipedHead.offsetY = 0.01F + (float)Math.sin(Math.toRadians(ticks * 35F)) * 0.02F;
//            bipedHead.offsetX = (float)Math.cos(Math.toRadians(ticks * 35F)) * 0.02F;
//            bipedHead.offsetZ = 0;
            head.zRot = (float) headRot * 0.05F;
        } else {
           // RenderSystem.translated(0, 0, 0);
//            bipedHead.offsetY = 0;
//            bipedHead.offsetX = 0;
//            bipedHead.offsetZ = 0;
            head.zRot = 0;
        }

        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (isDancing) {
            this.head.xRot += (float) Math.sin(Math.toRadians((entityIn.level.getGameTime() % 360) * 35F)) * 0.05F;

            float amp = 0.5F;

            final double armRot = Math.sin(Math.toRadians(ticks * 35F));
            double x = Math.PI + Math.PI / 4 + (float) armRot * amp;
            double y = armRot * amp;
            double z = (float) headRot * amp;

            rightArm.xRot += x;
            rightArm.yRot += y;
            rightArm.zRot += z;

            leftArm.xRot += x;
            leftArm.yRot += y;
            leftArm.zRot += z;
        }
    }

    private void setRotation(ModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}
