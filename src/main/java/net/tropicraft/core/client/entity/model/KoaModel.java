package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

public class KoaModel extends BipedModel<EntityKoaBase> {
    
    private static class ModelRendererCull extends ModelRenderer {
        
        public ModelRendererCull(Model model, int texOffX, int texOffY) {
            super(model, texOffX, texOffY);
        }

        @Override
        public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn) {
//            RenderSystem.enableCull();
            super.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
//            RenderSystem.disableCull();
        }
    }

    public ModelRenderer headband;
    public ModelRenderer armbandR;
    public ModelRenderer leaf;
    public ModelRenderer leaf3;
    public ModelRenderer leaf2;
    public ModelRenderer leaf4;
    public ModelRenderer leaf5;
    public ModelRenderer leaf6;
    public ModelRenderer leaf7;
    public ModelRenderer leaf8;
    public ModelRenderer leaf9;
    public ModelRenderer leaf10;
    public ModelRenderer armbandL;

    public KoaModel(float modelSize) {
        super(modelSize);
        bipedHead = new ModelRenderer(this, 0, 2);
        bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8);
        bipedHead.setRotationPoint(0F, 0F, 0F);
        bipedHead.setTextureSize(64, 32);
        bipedHead.mirror = true;
        setRotation(bipedHead, 0F, 0F, 0F);
        headband = new ModelRenderer(this, 24, 1);
        headband.addBox(-5F, 0F, -5F, 10, 2, 10);
        headband.setRotationPoint(0F, -7F, 0F);        //0,-7,0 before
        headband.setTextureSize(64, 32);
        headband.mirror = true;
        bipedHead.addChild(headband);
        setRotation(headband, 0F, 0F, 0F);
        armbandR = new ModelRenderer(this, 35, 6);
        armbandR.addBox(2.5F, -2F, -2.5F, 5, 1, 5);    //offset, dimensions
        armbandR.setRotationPoint(-6F, 3F, 0F);    //position
        armbandR.setTextureSize(64, 32);
        bipedRightArm.addChild(armbandR);
        setRotation(armbandR, 0F, 0F, 0F);
        armbandL = new ModelRenderer(this, 34, 1);
        armbandL.addBox(-7.5F, -2F, -2.5F, 5, 1, 5);        //offset, dimensions
        armbandL.setRotationPoint(6F, 3F, 0F);        //position
        armbandL.setTextureSize(64, 32);
        armbandL.mirror = true;
        bipedLeftArm.addChild(armbandL);
        setRotation(armbandL, 0F, 0F, 0F);
        leaf = new ModelRendererCull(this, 0, 0);
        leaf.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf.setRotationPoint(2F, -6F, -6F);
        leaf.setTextureSize(64, 32);
        leaf.mirror = true;
        headband.addChild(leaf);
        setRotation(leaf, 0F, 0F, 0F);
        leaf3 = new ModelRendererCull(this, 0, 0);
        leaf3.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf3.setRotationPoint(-1F, -6F, -6F);
        leaf3.setTextureSize(64, 32);
        leaf3.mirror = true;
        headband.addChild(leaf3);
        setRotation(leaf3, 0F, 0F, 0F);
        leaf2 = new ModelRendererCull(this, 0, 0);
        leaf2.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf2.setRotationPoint(-4F, -6F, -6F);
        leaf2.setTextureSize(64, 32);
        leaf2.mirror = true;
        headband.addChild(leaf2);
        setRotation(leaf2, 0F, 0F, 0F);
        leaf4 = new ModelRendererCull(this, 0, 0);
        leaf4.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf4.setRotationPoint(0F, -7F, -6F);
        leaf4.setTextureSize(64, 32);
        leaf4.mirror = true;
        headband.addChild(leaf4);
        setRotation(leaf4, 0F, 0F, 0F);
        leaf5 = new ModelRendererCull(this, 0, 0);
        leaf5.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf5.setRotationPoint(5F, -6F, -1F);
        leaf5.setTextureSize(64, 32);
        leaf5.mirror = true;
        headband.addChild(leaf5);
        setRotation(leaf5, 0F, 0F, 0F);
        leaf6 = new ModelRendererCull(this, 0, 0);
        leaf6.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf6.setRotationPoint(5F, -6F, 3F);
        leaf6.setTextureSize(64, 32);
        leaf6.mirror = true;
        headband.addChild(leaf6);
        setRotation(leaf6, 0F, 0F, 0F);
        leaf7 = new ModelRendererCull(this, 0, 0);
        leaf7.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf7.setRotationPoint(-6F, -6F, 0F);
        leaf7.setTextureSize(64, 32);
        leaf7.mirror = true;
        headband.addChild(leaf7);
        setRotation(leaf7, 0F, 0F, 0F);
        leaf8 = new ModelRendererCull(this, 0, 0);
        leaf8.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf8.setRotationPoint(-6F, -6F, -4F);
        leaf8.setTextureSize(64, 32);
        leaf8.mirror = true;
        headband.addChild(leaf8);
        setRotation(leaf8, 0F, 0F, 0F);
        leaf9 = new ModelRendererCull(this, 0, 0);
        leaf9.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf9.setRotationPoint(-2F, -6F, 5F);
        leaf9.setTextureSize(64, 32);
        leaf9.mirror = true;
        headband.addChild(leaf9);
        setRotation(leaf9, 0F, 0F, 0F);
        leaf10 = new ModelRendererCull(this, 0, 0);
        leaf10.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf10.setRotationPoint(2F, -6F, 5F);
        leaf10.setTextureSize(64, 32);
        leaf10.mirror = true;
        headband.addChild(leaf10);
        setRotation(leaf10, 0F, 0F, 0F);
        
        bipedHeadwear.showModel = false;
    }

    @Override
    public void render(MatrixStack ms, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
            float red, float green, float blue, float alpha) {

        ms.push();
//        if (isSitting) {
//            if (isChild) {
//                ms.translate(0, 0.3, 0);
//            } else {
//                ms.translate(0, 0.6, 0);
//            }
//        }
        
        if (this.isChild) {
            ms.push();
            ms.scale(0.75F, 0.75F, 0.75F);
            ms.translate(0.0F, 1.0F, 0.0F);
            this.bipedHead.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            ms.pop();
            ms.push();
            ms.scale(0.5F, 0.5F, 0.5F);
            ms.translate(0.0F, 1.5F, 0.0F);
            this.bipedBody.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            this.bipedRightArm.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            this.bipedLeftArm.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            this.bipedRightLeg.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            this.bipedLeftLeg.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            ms.pop();
        } else {
            bipedHead.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            bipedBody.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            bipedRightArm.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            bipedLeftArm.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            bipedRightLeg.render(ms, bufferIn, packedLightIn, packedOverlayIn);
            bipedLeftLeg.render(ms, bufferIn, packedLightIn, packedOverlayIn);
        }
        ms.pop();
    }

    @Override
    public void setRotationAngles(EntityKoaBase entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        bipedHeadwear.showModel = false;

        isSitting = entityIn.isSitting() || entityIn.isPassenger();
        final boolean isDancing = entityIn.isDancing();


        float ticks = (entityIn.ticksExisted + Minecraft.getInstance().getRenderPartialTicks()) % 360;

        final double headRot = Math.cos(Math.toRadians(ticks * 35F));
        if (isDancing) {
            // TODO remove translate calls in this method
       //     RenderSystem.translated(0, 0.01F + (float)Math.sin(Math.toRadians(ticks * 35F)) * 0.02F, 0);
      //      RenderSystem.translated((float) headRot * 0.02F, 0, 0);
//            bipedHead.offsetY = 0.01F + (float)Math.sin(Math.toRadians(ticks * 35F)) * 0.02F;
//            bipedHead.offsetX = (float)Math.cos(Math.toRadians(ticks * 35F)) * 0.02F;
//            bipedHead.offsetZ = 0;
            bipedHead.rotateAngleZ = (float) headRot * 0.05F;
        } else {
           // RenderSystem.translated(0, 0, 0);
//            bipedHead.offsetY = 0;
//            bipedHead.offsetX = 0;
//            bipedHead.offsetZ = 0;
            bipedHead.rotateAngleZ = 0;
        }

        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        if (isDancing) {
            this.bipedHead.rotateAngleX += (float) Math.sin(Math.toRadians((entityIn.world.getGameTime() % 360) * 35F)) * 0.05F;

            float amp = 0.5F;

            final double armRot = Math.sin(Math.toRadians(ticks * 35F));
            double x = Math.PI + Math.PI / 4 + (float) armRot * amp;
            double y = armRot * amp;
            double z = (float) headRot * amp;

            bipedRightArm.rotateAngleX += x;
            bipedRightArm.rotateAngleY += y;
            bipedRightArm.rotateAngleZ += z;

            bipedLeftArm.rotateAngleX += x;
            bipedLeftArm.rotateAngleY += y;
            bipedLeftArm.rotateAngleZ += z;
        }
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
