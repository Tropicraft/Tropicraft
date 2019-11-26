package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

public class KoaModel extends BipedModel<EntityKoaBase> {
	
	private static class RendererModelCull extends RendererModel {
		
		public RendererModelCull(Model model, int texOffX, int texOffY) {
			super(model, texOffX, texOffY);
		}

		@Override
		public void render(float scale) {
			GlStateManager.enableCull();
			super.render(scale);
			GlStateManager.disableCull();
		}
	}

    /*public RendererModel field_78116_c;
    public RendererModel field_78115_e;
    public RendererModel bipedRightArm;
    public RendererModel bipedLeftArm;
    public RendererModel bipedRightLeg;
    public RendererModel bipedLeftLeg;*/
    public RendererModel headband;
    public RendererModel armbandR;
    public RendererModel leaf;
    public RendererModel leaf3;
    public RendererModel leaf2;
    public RendererModel leaf4;
    public RendererModel leaf5;
    public RendererModel leaf6;
    public RendererModel leaf7;
    public RendererModel leaf8;
    public RendererModel leaf9;
    public RendererModel leaf10;
    public RendererModel armbandL;

    public KoaModel() {
        
        //textureWidth = 64;
        //textureHeight = 32;

        bipedHead = new RendererModel(this, 0, 2);
        bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8);
        bipedHead.setRotationPoint(0F, 0F, 0F);
        bipedHead.setTextureSize(64, 32);
        bipedHead.mirror = true;
        setRotation(bipedHead, 0F, 0F, 0F);
        /*field_78115_e = new RendererModel(this, 16, 16);
        field_78115_e.addBox(-4F, 0F, -2F, 8, 12, 4);
        field_78115_e.setRotationPoint(0F, 0F, 0F);
        field_78115_e.setTextureSize(64, 32);
        field_78115_e.mirror = true;
        setRotation(field_78115_e, 0F, 0F, 0F);
        bipedRightArm = new RendererModel(this, 40, 16);
        bipedRightArm.addBox(-2F, -2F, -2F, 3, 12, 4);
        bipedRightArm.setRotationPoint(-4F, 3F, 0F);
        bipedRightArm.setTextureSize(64, 32);
        bipedRightArm.mirror = true;
        setRotation(bipedRightArm, 0F, 0F, 0F);
        bipedLeftArm = new RendererModel(this, 40, 16);
        bipedLeftArm.addBox(-1F, -2F, -2F, 3, 12, 4);
        bipedLeftArm.setRotationPoint(5F, 3F, 0F);
        bipedLeftArm.setTextureSize(64, 32);
        bipedLeftArm.mirror = true; //hey baby whats shakin		//bacon :D /me wants bacon mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
        setRotation(bipedLeftArm, 0F, 0F, 0F);
        bipedRightLeg = new RendererModel(this, 0, 16);
        bipedRightLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
        bipedRightLeg.setRotationPoint(-2F, 12F, 0F);
        bipedRightLeg.setTextureSize(64, 32);
        bipedRightLeg.mirror = true;
        setRotation(bipedRightLeg, 0F, 0F, 0F);
        bipedLeftLeg = new RendererModel(this, 0, 16);
        bipedLeftLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
        bipedLeftLeg.setRotationPoint(2F, 12F, 0F);
        bipedLeftLeg.setTextureSize(64, 32);
        bipedLeftLeg.mirror = true;
        setRotation(bipedLeftLeg, 0F, 0F, 0F);*/
        headband = new RendererModel(this, 24, 1);
        headband.addBox(-5F, 0F, -5F, 10, 2, 10);
        headband.setRotationPoint(0F, -7F, 0F);		//0,-7,0 before
        headband.setTextureSize(64, 32);
        headband.mirror = true;
        bipedHead.addChild(headband);
        setRotation(headband, 0F, 0F, 0F);
        armbandR = new RendererModel(this, 35, 6);
        armbandR.addBox(2.5F, -2F, -2.5F, 5, 1, 5);	//offset, dimensions
        armbandR.setRotationPoint(-6F, 3F, 0F);	//position
        armbandR.setTextureSize(64, 32);
        bipedRightArm.addChild(armbandR);
        setRotation(armbandR, 0F, 0F, 0F);
        armbandL = new RendererModel(this, 34, 1);
        armbandL.addBox(-7.5F, -2F, -2.5F, 5, 1, 5);		//offset, dimensions
        armbandL.setRotationPoint(6F, 3F, 0F);		//position
        armbandL.setTextureSize(64, 32);
        armbandL.mirror = true;
        bipedLeftArm.addChild(armbandL);
        setRotation(armbandL, 0F, 0F, 0F);
        leaf = new RendererModelCull(this, 0, 0);
        leaf.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf.setRotationPoint(2F, -6F, -6F);
        leaf.setTextureSize(64, 32);
        leaf.mirror = true;
        headband.addChild(leaf);
        setRotation(leaf, 0F, 0F, 0F);
        leaf3 = new RendererModelCull(this, 0, 0);
        leaf3.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf3.setRotationPoint(-1F, -6F, -6F);
        leaf3.setTextureSize(64, 32);
        leaf3.mirror = true;
        headband.addChild(leaf3);
        setRotation(leaf3, 0F, 0F, 0F);
        leaf2 = new RendererModelCull(this, 0, 0);
        leaf2.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf2.setRotationPoint(-4F, -6F, -6F);
        leaf2.setTextureSize(64, 32);
        leaf2.mirror = true;
        headband.addChild(leaf2);
        setRotation(leaf2, 0F, 0F, 0F);
        leaf4 = new RendererModelCull(this, 0, 0);
        leaf4.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf4.setRotationPoint(0F, -7F, -6F);
        leaf4.setTextureSize(64, 32);
        leaf4.mirror = true;
        headband.addChild(leaf4);
        setRotation(leaf4, 0F, 0F, 0F);
        leaf5 = new RendererModelCull(this, 0, 0);
        leaf5.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf5.setRotationPoint(5F, -6F, -1F);
        leaf5.setTextureSize(64, 32);
        leaf5.mirror = true;
        headband.addChild(leaf5);
        setRotation(leaf5, 0F, 0F, 0F);
        leaf6 = new RendererModelCull(this, 0, 0);
        leaf6.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf6.setRotationPoint(5F, -6F, 3F);
        leaf6.setTextureSize(64, 32);
        leaf6.mirror = true;
        headband.addChild(leaf6);
        setRotation(leaf6, 0F, 0F, 0F);
        leaf7 = new RendererModelCull(this, 0, 0);
        leaf7.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf7.setRotationPoint(-6F, -6F, 0F);
        leaf7.setTextureSize(64, 32);
        leaf7.mirror = true;
        headband.addChild(leaf7);
        setRotation(leaf7, 0F, 0F, 0F);
        leaf8 = new RendererModelCull(this, 0, 0);
        leaf8.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf8.setRotationPoint(-6F, -6F, -4F);
        leaf8.setTextureSize(64, 32);
        leaf8.mirror = true;
        headband.addChild(leaf8);
        setRotation(leaf8, 0F, 0F, 0F);
        leaf9 = new RendererModelCull(this, 0, 0);
        leaf9.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf9.setRotationPoint(-2F, -6F, 5F);
        leaf9.setTextureSize(64, 32);
        leaf9.mirror = true;
        headband.addChild(leaf9);
        setRotation(leaf9, 0F, 0F, 0F);
        leaf10 = new RendererModelCull(this, 0, 0);
        leaf10.addBox(0F, 7F, 0F, 1, 0, 1);
        leaf10.setRotationPoint(2F, -6F, 5F);
        leaf10.setTextureSize(64, 32);
        leaf10.mirror = true;
        headband.addChild(leaf10);
        setRotation(leaf10, 0F, 0F, 0F);
    }

    @Override
    public void render(EntityKoaBase entity, float f, float f1, float f2, float f3, float f4, float f5) {
        //super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(entity, f, f1, f2, f3, f4, f5);

        GlStateManager.pushMatrix();

        if (this.isChild)
        {
            //float f = 2.0F;
            GlStateManager.scalef(0.75F, 0.75F, 0.75F);
            GlStateManager.translatef(0.0F, 16.0F * f5, 0.0F);
            this.bipedHead.render(f5);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            GlStateManager.translatef(0.0F, 24.0F * f5, 0.0F);
            this.bipedBody.render(f5);
            this.bipedRightArm.render(f5);
            this.bipedLeftArm.render(f5);
            this.bipedRightLeg.render(f5);
            this.bipedLeftLeg.render(f5);
        }
        else {
            bipedHead.render(f5);
            bipedBody.render(f5);
            bipedRightArm.render(f5);
            bipedLeftArm.render(f5);
            bipedRightLeg.render(f5);
            bipedLeftLeg.render(f5);
        }

        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(EntityKoaBase entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {

        boolean isDancing = false;

        if (entityIn instanceof EntityKoaBase) {
            this.isSitting = ((EntityKoaBase) entityIn).isSitting() || entityIn.isPassenger();
            isDancing = ((EntityKoaBase) entityIn).isDancing();
        }

        if (this.isSitting)
        {
            if (this.isChild) {
                GlStateManager.translated(0, 0.1, 0);
            } else {
                GlStateManager.translated(0, 0.3, 0);
            }
        }

        float ticks = (entityIn.ticksExisted + Minecraft.getInstance().getRenderPartialTicks()) % 360;

        if (isDancing) {
            this.bipedHead.offsetY = 0.01F + (float)Math.sin(Math.toRadians(ticks * 35F)) * 0.02F;
            this.bipedHead.offsetX = (float)Math.cos(Math.toRadians(ticks * 35F)) * 0.02F;
            this.bipedHead.offsetZ = 0;
            this.bipedHead.rotateAngleZ = (float)Math.cos(Math.toRadians(ticks * 35F)) * 0.05F;
        } else {
            this.bipedHead.offsetY = 0;
            this.bipedHead.offsetX = 0;
            this.bipedHead.offsetZ = 0;
            this.bipedHead.rotateAngleZ = 0;
        }

        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

        if (isDancing) {
            this.bipedHead.rotateAngleX += (float) Math.sin(Math.toRadians((entityIn.world.getGameTime() % 360) * 35F)) * 0.05F;

            float amp = 0.5F;

            double x = Math.PI + Math.PI / 4 + (float) Math.sin(Math.toRadians(ticks * 35F)) * amp;
            double y = Math.sin(Math.toRadians(ticks * 35F)) * amp;
            double z = (float) Math.cos(Math.toRadians(ticks * 35F)) * amp;

            this.bipedRightArm.rotateAngleX += x;
            this.bipedRightArm.rotateAngleY += y;
            this.bipedRightArm.rotateAngleZ += z;

            this.bipedLeftArm.rotateAngleX += x;
            this.bipedLeftArm.rotateAngleY += y;
            this.bipedLeftArm.rotateAngleZ += z;
        }
    }

    private void setRotation(RendererModel model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}
