package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelScubaGear extends ModelBiped {
    //   ModelRenderer bipedHead;
    //   ModelRenderer bipedBody;
    //   ModelRenderer bipedRightArm;
    //   ModelRenderer bipedLeftArm;
    //    ModelRenderer bipedRightLeg;
    ModelRenderer Fin1;
    ModelRenderer Fin1m1;
    ModelRenderer Fin1m2;
    ModelRenderer Fin1m3;
    ModelRenderer Fin1m4;
    //    ModelRenderer bipedLeftLeg;
    ModelRenderer Fin2;
    ModelRenderer Fin2m1;
    ModelRenderer Fin2m2;
    ModelRenderer Fin2m3;
    ModelRenderer Fin2m4;
    ModelRenderer BCD;
    ModelRenderer BCD12;
    ModelRenderer BCD11;
    ModelRenderer BCD4;
    ModelRenderer Tank2;
    ModelRenderer Tank2m1;
    ModelRenderer Tank2m2;
    ModelRenderer Tank2m3;
    ModelRenderer Tank2m4;
    ModelRenderer Tank2m5;
    ModelRenderer Tank2m6;
    ModelRenderer Tank2m7;
    ModelRenderer BCD2;
    ModelRenderer Tank1;
    ModelRenderer Tank1m1;
    ModelRenderer Tank1m2;
    ModelRenderer Tank1m3;
    ModelRenderer Tank1m4;
    ModelRenderer Tank1m5;
    ModelRenderer Tank1m6;
    ModelRenderer Tank1m7;
    ModelRenderer BCD6;
    ModelRenderer BCD5;
    ModelRenderer BCD3;
    ModelRenderer BCD7;
    ModelRenderer BCD8;
    ModelRenderer BCD9;
    ModelRenderer BCD13;
    ModelRenderer Mask;
    ModelRenderer Mask2;
    ModelRenderer Mask3;
    ModelRenderer Mask4;
    ModelRenderer Mask5;
    ModelRenderer Mask6;
    ModelRenderer Mask7;
    ModelRenderer Mask8;
    ModelRenderer Mask9;
    ModelRenderer mouthpiece;
    ModelRenderer mouthpiece2;
    ModelRenderer mouthpiece3;
    ModelRenderer hose1;
    ModelRenderer hose2;
    ModelRenderer hose3;
    ModelRenderer hose4;
    ModelRenderer hose5;
    ModelRenderer hose6;

    public ModelScubaGear() {
        this( 0.0f );
    }

    public ModelScubaGear( float par1 ) {
        this.leftArmPose = ModelBiped.ArmPose.EMPTY;
        this.rightArmPose = ModelBiped.ArmPose.EMPTY;
        bipedHead = new ModelRenderer( this, 0, 0 );
        bipedHead.setTextureSize( 128, 64 );
        bipedHead.addBox( -4F, -4F, -4F, 8, 8, 8);
        bipedHead.setRotationPoint( 0F, -4F, 0F );
        bipedBody = new ModelRenderer( this, 32, 16 );
        bipedBody.setTextureSize( 128, 64 );
        bipedBody.addBox( -4F, -6F, -2F, 8, 12, 4);
        bipedBody.setRotationPoint( 0F, 6F, 0F );
        bipedRightArm = new ModelRenderer( this, 56, 16 );
        bipedRightArm.setTextureSize( 128, 64 );
        bipedRightArm.addBox( -4F, 0F, -2F, 4, 12, 4);
        bipedRightArm.setRotationPoint( -4F, 0F, 0F );
        bipedLeftArm = new ModelRenderer( this, 72, 16 );
        bipedLeftArm.setTextureSize( 128, 64 );
        bipedLeftArm.addBox( 0F, 0F, -2F, 4, 12, 4);
        bipedLeftArm.setRotationPoint( 4F, 0F, 0F );
        bipedRightLeg = new ModelRenderer( this, 0, 16 );
        bipedRightLeg.setTextureSize( 128, 64 );
        bipedRightLeg.addBox( -2F, -6F, -2F, 4, 12, 4);
        bipedRightLeg.setRotationPoint( -2F, 18F, 0F );
        Fin1 = new ModelRenderer( this, 10, 38 );
        Fin1.setTextureSize( 128, 64 );
        Fin1.addBox( -2.5F, -2F, -2.5F, 5, 2, 5);
        Fin1.setRotationPoint( -2F, 24F, 0F );
        Fin1.mirror = true;
        // TODO: rotation point
        Fin1m1 = new ModelRenderer( this, 13, 47 );
        Fin1m1.setTextureSize( 128, 64 );
        Fin1m1.addBox( -2.5F, -1.5F, -1F, 5, 1, 2);
        Fin1m1.setRotationPoint( -3.19707F, 24.5F, -3.288924F );
        setRotation(Fin1m1, 0F, 0F, 0F);
        Fin1m1.mirror = true;
        Fin1m2 = new ModelRenderer( this, 15, 45 );
        Fin1m2.setTextureSize( 128, 64 );
        Fin1m2.addBox( -2F, -1.5F, -0.5F, 4, 1, 1);
        Fin1m2.setRotationPoint( -3.02606F, 23.5F, -2.819078F );
        setRotation(Fin1m2, 0F, 0F, 0F);
        Fin1m2.mirror = true;
        Fin1m3 = new ModelRenderer( this, 1, 52 );
        Fin1m3.setTextureSize( 128, 64 );
        Fin1m3.addBox( -5F, -1F, -6F, 10, 0, 12);
        Fin1m3.setRotationPoint( -5.420201F, 24.5F, -9.396926F );
        setRotation(Fin1m3, 0F, 0F, 0F);
        Fin1m3.mirror = true;
        Fin1m4 = new ModelRenderer( this, 15, 50 );
        Fin1m4.setTextureSize( 128, 64 );
        Fin1m4.addBox( -2F, -1.5F, -0.5F, 4, 1, 1);
        Fin1m4.setRotationPoint( -3.710101F, 24.5F, -4.698463F );
        setRotation(Fin1m4, 0F, 0F, 0F);
        Fin1m4.mirror = true;
//        Fin1.addChild(Fin1m1);
//        Fin1.addChild(Fin1m2);
//        Fin1.addChild(Fin1m3);
//        Fin1.addChild(Fin1m4);
        setRotation(Fin1, 0F, 0F, 0F);
       // bipedRightLeg.addChild(Fin1);
        bipedLeftLeg = new ModelRenderer( this, 16, 16 );
        bipedLeftLeg.setTextureSize( 128, 64 );
        bipedLeftLeg.addBox( -2F, -6F, -2F, 4, 12, 4);
        bipedLeftLeg.setRotationPoint( 2F, 18F, 0F );
        Fin2 = new ModelRenderer( this, 10, 38 );
        Fin2.setTextureSize( 128, 64 );
        Fin2.addBox( -2.5F, 0F, -2.5F, 5, 2, 5);
        Fin2.setRotationPoint( 2F, 24F, 0F );
        Fin2m1 = new ModelRenderer( this, 13, 47 );
        Fin2m1.setTextureSize( 128, 64 );
        Fin2m1.addBox( -2.5F, 0.5F, -1F, 5, 1, 2);
        Fin2m1.setRotationPoint( 3.19707F, 24.5F, -3.288924F );
        setRotation(Fin2m1, 0F, 0F, 0F);
        Fin2m2 = new ModelRenderer( this, 15, 45 );
        Fin2m2.setTextureSize( 128, 64 );
        Fin2m2.addBox( -2F, 0.5F, -0.5F, 4, 1, 1);
        Fin2m2.setRotationPoint( 3.02606F, 23.5F, -2.819078F );
        setRotation(Fin2m2, 0F, 0F, 0F);
        Fin2m3 = new ModelRenderer( this, 1, 52 );
        Fin2m3.setTextureSize( 128, 64 );
        Fin2m3.addBox( -5F, 1.0F, -6F, 10, 0, 12);
        Fin2m3.setRotationPoint( 5.420201F, 24.5F, -9.396926F );
        setRotation(Fin2m3, 0F, 0F, 0F);
        Fin2m4 = new ModelRenderer( this, 15, 50 );
        Fin2m4.setTextureSize( 128, 64 );
        Fin2m4.addBox( -2F, 0.5F, -0.5F, 4, 1, 1);
        Fin2m4.setRotationPoint( 3.710101F, 24.5F, -4.698463F );
        setRotation(Fin2m4, 0F, 0F, 0F);
//        Fin2.addChild(Fin2m1);
//        Fin2.addChild(Fin2m2);
//        Fin2.addChild(Fin2m3);
//        Fin2.addChild(Fin2m4);
        setRotation(Fin2, 0F, 0F, 0F);
        //bipedLeftLeg.addChild(Fin2);
        BCD = new ModelRenderer( this, 65, 50 );
        BCD.setTextureSize( 128, 64 );
        BCD.addBox( -4F, -6F, -1F, 8, 12, 2);
        BCD.setRotationPoint( 0F, 6.5F, 3F );
        BCD12 = new ModelRenderer( this, 102, 46 );
        BCD12.setTextureSize( 128, 64 );
        BCD12.addBox( -0.5F, -0.5F, -0.5F, 1, 1, 1);
        BCD12.setRotationPoint( 0F, 10F, -2.7F );
        BCD11 = new ModelRenderer( this, 79, 42 );
        BCD11.setTextureSize( 128, 64 );
        BCD11.addBox( -0.5F, -0.5F, -2F, 1, 1, 4);
        BCD11.setRotationPoint( 3.6F, 3F, 0F );
        BCD4 = new ModelRenderer( this, 97, 50 );
        BCD4.setTextureSize( 128, 64 );
        BCD4.addBox( -1F, -5.5F, -0.5F, 2, 11, 1);
        BCD4.setRotationPoint( 3F, 5.5F, -2.5F );
        Tank2 = new ModelRenderer( this, 41, 50 );
        Tank2.setTextureSize( 128, 64 );
        Tank2.addBox( -2F, -5F, -2F, 4, 10, 4);
        Tank2.setRotationPoint( -3F, 7F, 6.5F );
        Tank2m1 = new ModelRenderer( this, 45, 54 );
        Tank2m1.setTextureSize( 128, 64 );
        Tank2m1.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank2m1.setRotationPoint( -3F, 7F, 8.5F );
        Tank2m2 = new ModelRenderer( this, 45, 54 );
        Tank2m2.setTextureSize( 128, 64 );
        Tank2m2.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank2m2.setRotationPoint( -5F, 7F, 6.5F );
        Tank2m3 = new ModelRenderer( this, 45, 54 );
        Tank2m3.setTextureSize( 128, 64 );
        Tank2m3.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank2m3.setRotationPoint( -1F, 7F, 6.5F );
        Tank2m4 = new ModelRenderer( this, 43, 46 );
        Tank2m4.setTextureSize( 128, 64 );
        Tank2m4.addBox( -1.5F, -0.5F, -1.5F, 3, 1, 3);
        Tank2m4.setRotationPoint( -3F, 1.5F, 6.5F );
        Tank2m5 = new ModelRenderer( this, 38, 49 );
        Tank2m5.setTextureSize( 128, 64 );
        Tank2m5.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
        Tank2m5.setRotationPoint( -3F, -0.5F, 6.5F );
        Tank2m6 = new ModelRenderer( this, 44, 44 );
        Tank2m6.setTextureSize( 128, 64 );
        Tank2m6.addBox( -2F, -0.5F, -0.5F, 4, 1, 1);
        Tank2m6.setRotationPoint( -3.5F, -0.5F, 6.5F );
        Tank2m7 = new ModelRenderer( this, 36, 44 );
        Tank2m7.setTextureSize( 128, 64 );
        Tank2m7.addBox( -1F, -1F, -1F, 2, 2, 2);
        Tank2m7.setRotationPoint( -5.5F, -0.5F, 6.5F );
        BCD2 = new ModelRenderer( this, 66, 51 );
        BCD2.setTextureSize( 128, 64 );
        BCD2.addBox( -3.5F, -5F, -0.5F, 7, 10, 1);
        BCD2.setRotationPoint( 0F, 6.5F, 4F );
        Tank1 = new ModelRenderer( this, 41, 50 );
        Tank1.setTextureSize( 128, 64 );
        Tank1.addBox( -2F, -5F, -2F, 4, 10, 4);
        Tank1.setRotationPoint( 3F, 7F, 6.5F );
        Tank1m1 = new ModelRenderer( this, 45, 54 );
        Tank1m1.setTextureSize( 128, 64 );
        Tank1m1.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank1m1.setRotationPoint( 3F, 7F, 8.5F );
        Tank1m2 = new ModelRenderer( this, 45, 54 );
        Tank1m2.setTextureSize( 128, 64 );
        Tank1m2.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank1m2.setRotationPoint( 1F, 7F, 6.5F );
        Tank1m3 = new ModelRenderer( this, 45, 54 );
        Tank1m3.setTextureSize( 128, 64 );
        Tank1m3.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank1m3.setRotationPoint( 5F, 7F, 6.5F );
        Tank1m4 = new ModelRenderer( this, 43, 46 );
        Tank1m4.setTextureSize( 128, 64 );
        Tank1m4.addBox( -1.5F, -0.5F, -1.5F, 3, 1, 3);
        Tank1m4.setRotationPoint( 3F, 1.5F, 6.5F );
        Tank1m5 = new ModelRenderer( this, 38, 49 );
        Tank1m5.setTextureSize( 128, 64 );
        Tank1m5.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
        Tank1m5.setRotationPoint( 3F, -0.5F, 6.5F );
        Tank1m6 = new ModelRenderer( this, 44, 44 );
        Tank1m6.setTextureSize( 128, 64 );
        Tank1m6.addBox( -2F, -0.5F, -0.5F, 4, 1, 1);
        Tank1m6.setRotationPoint( 3.5F, -0.5F, 6.5F );
        Tank1m7 = new ModelRenderer( this, 36, 44 );
        Tank1m7.setTextureSize( 128, 64 );
        Tank1m7.addBox( -1F, -1F, -1F, 2, 2, 2);
        Tank1m7.setRotationPoint( 5.5F, -0.5F, 6.5F );
        BCD6 = new ModelRenderer( this, 68, 41 );
        BCD6.setTextureSize( 128, 64 );
        BCD6.addBox( -0.5F, -1F, -2F, 1, 2, 4);
        BCD6.setRotationPoint( -3.6F, 10F, 0F );
        BCD5 = new ModelRenderer( this, 68, 41 );
        BCD5.setTextureSize( 128, 64 );
        BCD5.addBox( -0.5F, -1F, -2F, 1, 2, 4);
        BCD5.setRotationPoint( 3.6F, 10F, 0F );
        BCD3 = new ModelRenderer( this, 91, 50 );
        BCD3.setTextureSize( 128, 64 );
        BCD3.addBox( -1F, -5.5F, -0.5F, 2, 11, 1);
        BCD3.setRotationPoint( -3F, 5.5F, -2.5F );
        BCD7 = new ModelRenderer( this, 91, 45 );
        BCD7.setTextureSize( 128, 64 );
        BCD7.addBox( -2F, -1F, -0.5F, 4, 2, 1);
        BCD7.setRotationPoint( 0F, 10F, -2.5F );
        BCD8 = new ModelRenderer( this, 91, 48 );
        BCD8.setTextureSize( 128, 64 );
        BCD8.addBox( -2F, -0.5F, -0.5F, 4, 1, 1);
        BCD8.setRotationPoint( 0F, 3F, -2.5F );
        BCD9 = new ModelRenderer( this, 79, 42 );
        BCD9.setTextureSize( 128, 64 );
        BCD9.addBox( -0.5F, -0.5F, -2F, 1, 1, 4);
        BCD9.setRotationPoint( -3.6F, 3F, 0F );
        BCD13 = new ModelRenderer( this, 91, 38 );
        BCD13.setTextureSize( 128, 64 );
        BCD13.addBox( -4F, -0.5F, -0.5F, 8, 1, 1);
        BCD13.setRotationPoint( 0F, 0.5F, 2.5F );
        Mask = new ModelRenderer( this, 109, 60 );
        Mask.setTextureSize( 128, 64 );
        Mask.addBox( -4F, -0.5F, -0.5F, 8, 1, 1);
        Mask.setRotationPoint( 0F, -6F, -4.5F );
        Mask2 = new ModelRenderer( this, 120, 55 );
        Mask2.setTextureSize( 128, 64 );
        Mask2.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
        Mask2.setRotationPoint( -4F, -4F, -4.5F );
        Mask3 = new ModelRenderer( this, 116, 55 );
        Mask3.setTextureSize( 128, 64 );
        Mask3.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
        Mask3.setRotationPoint( 4F, -4F, -4.5F );
        Mask4 = new ModelRenderer( this, 114, 51 );
        Mask4.setTextureSize( 128, 64 );
        Mask4.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        Mask4.setRotationPoint( -2.5F, -2F, -4.5F );
        Mask5 = new ModelRenderer( this, 114, 53 );
        Mask5.setTextureSize( 128, 64 );
        Mask5.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        Mask5.setRotationPoint( 2.5F, -2F, -4.5F );
        Mask6 = new ModelRenderer( this, 114, 49 );
        Mask6.setTextureSize( 128, 64 );
        Mask6.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        Mask6.setRotationPoint( 0F, -3F, -4.5F );
        Mask7 = new ModelRenderer( this, 110, 38 );
        Mask7.setTextureSize( 128, 64 );
        Mask7.addBox( -0.5F, -1F, -4F, 1, 2, 8);
        Mask7.setRotationPoint( 4F, -4.5F, 0F );
        Mask8 = new ModelRenderer( this, 110, 38 );
        Mask8.setTextureSize( 128, 64 );
        Mask8.addBox( -0.5F, -1F, -4F, 1, 2, 8);
        Mask8.setRotationPoint( -4F, -4.5F, 0F );
        Mask9 = new ModelRenderer( this, 110, 35 );
        Mask9.setTextureSize( 128, 64 );
        Mask9.addBox( -4F, -1F, -0.5F, 8, 2, 1);
        Mask9.setRotationPoint( 0F, -4.5F, 4F );
//        bipedHead.addChild(Mask);
//        bipedHead.addChild(Mask2);
//        bipedHead.addChild(Mask3);
//        bipedHead.addChild(Mask4);
//        bipedHead.addChild(Mask5);
//        bipedHead.addChild(Mask6);
//        bipedHead.addChild(Mask7);
//        bipedHead.addChild(Mask8);
        mouthpiece = new ModelRenderer( this, 115, 28 );
        mouthpiece.setTextureSize( 128, 64 );
        mouthpiece.addBox( -1.5F, -1.5F, -0.5F, 3, 3, 1);
        mouthpiece.setRotationPoint( 0F, 0F, -5F );
        mouthpiece2 = new ModelRenderer( this, 116, 25 );
        mouthpiece2.setTextureSize( 128, 64 );
        mouthpiece2.addBox( -1F, -1F, -0.5F, 2, 2, 1);
        mouthpiece2.setRotationPoint( 0F, 0F, -5.5F );
        mouthpiece3 = new ModelRenderer( this, 116, 23 );
        mouthpiece3.setTextureSize( 128, 64 );
        mouthpiece3.addBox( -1F, -0.5F, -0.5F, 2, 1, 1);
        mouthpiece3.setRotationPoint( 0F, -0.6000004F, -4F );
//        bipedHead.addChild(mouthpiece);
//        bipedHead.addChild(mouthpiece2);
//        bipedHead.addChild(mouthpiece3);
        hose1 = new ModelRenderer( this, 117, 16 );
        hose1.setTextureSize( 128, 64 );
        hose1.addBox( -0.5F, -0.5F, -0.5F, 1, 1, 1);
        hose1.setRotationPoint( 3F, -3F, 6.5F );
        hose2 = new ModelRenderer( this, 117, 16 );
        hose2.setTextureSize( 128, 64 );
        hose2.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        hose2.setRotationPoint( 5F, -3F, 6.5F );
        hose3 = new ModelRenderer( this, 116, 15 );
        hose3.setTextureSize( 128, 64 );
        hose3.addBox( -0.5F, -0.5F, -1F, 1, 1, 2);
        hose3.setRotationPoint( 6F, -3F, 5F );
        hose4 = new ModelRenderer( this, 106, 7 );
        hose4.setTextureSize( 128, 64 );
        hose4.addBox( -0.5F, -0.5F, -10F, 1, 1, 10);
        hose4.setRotationPoint( 6F, -3F, 4.2F );
        hose5 = new ModelRenderer( this, 115, 16 );
        hose5.setTextureSize( 128, 64 );
        hose5.addBox( -2.5F, -0.5F, -0.5F, 5, 1, 1);
        hose5.setRotationPoint( 4F, 0F, -5F );
        hose6 = new ModelRenderer( this, 115, 16 );
        hose6.setTextureSize( 128, 64 );
        hose6.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        hose6.setRotationPoint( 0F, -0.5F, 6.5F );
//        bipedBody.addChild(hose1);
//        bipedBody.addChild(hose2);
//        bipedBody.addChild(hose3);
//        bipedBody.addChild(hose4);
//        bipedBody.addChild(hose5);
//        bipedBody.addChild(hose6);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        GlStateManager.pushMatrix();

        if (this.isChild)
        {
            float f = 2.0F;
            GlStateManager.scale(0.75F, 0.75F, 0.75F);
            GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
            this.bipedHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.renderScubaGear(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, false);
        }
        else
        {
            if (entityIn.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.renderScubaGear(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, true);
        }

        GlStateManager.popMatrix();
    }

    public void renderScubaGear(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean renderHead) {
        if (renderHead) {
//            bipedHead.rotateAngleX = 0F;
//            bipedHead.rotateAngleY = 0F;
//            bipedHead.rotateAngleZ = 0F;
            bipedHead.render(scale);   
        }

//        bipedBody.rotateAngleX = 0F;
//        bipedBody.rotateAngleY = 0F;
//        bipedBody.rotateAngleZ = 0F;
        bipedBody.render(scale);

//        bipedRightArm.rotateAngleX = -0.9092643F;
//        bipedRightArm.rotateAngleY = 0F;
//        bipedRightArm.rotateAngleZ = 0F;
        bipedRightArm.render(scale);

//        bipedLeftArm.rotateAngleX = 0.298086F;
//        bipedLeftArm.rotateAngleY = 0F;
//        bipedLeftArm.rotateAngleZ = 0F;
        bipedLeftArm.render(scale);

//        bipedRightLeg.rotateAngleX = 0F;
//        bipedRightLeg.rotateAngleY = 0.3490658F;
//        bipedRightLeg.rotateAngleZ = 0F;
        bipedRightLeg.render(scale);

//        Fin1.rotateAngleX = 0F;
//        Fin1.rotateAngleY = 0.3490658F;
//        Fin1.rotateAngleZ = 0F;
        copyModelRotations(this.bipedRightLeg, this.Fin1);
        Fin1.render(scale);
//
//        Fin1m1.rotateAngleX = 0F;
//        Fin1m1.rotateAngleY = 0.3490658F;
//        Fin1m1.rotateAngleZ = 0F;
        copyModelRotations(this.bipedRightLeg, this.Fin1m1);
       Fin1m1.render(scale);
////
//        Fin1m2.rotateAngleX = 0F;
//        Fin1m2.rotateAngleY = 0.3490658F;
//        Fin1m2.rotateAngleZ = 0F;
       copyModelRotations(this.bipedRightLeg, this.Fin1m2);
        Fin1m2.render(scale);
////
//        Fin1m3.rotateAngleX = 0F;
//        Fin1m3.rotateAngleY = 0.3490658F;
//        Fin1m3.rotateAngleZ = 0F;
        copyModelRotations(this.bipedRightLeg, this.Fin1m3);
        Fin1m3.render(scale);
////
//        Fin1m4.rotateAngleX = 0F;
//        Fin1m4.rotateAngleY = 0.3490658F;
//        Fin1m4.rotateAngleZ = 0F;
        copyModelRotations(this.bipedRightLeg, this.Fin1m4);
        Fin1m4.render(scale);

//        bipedLeftLeg.rotateAngleX = 0F;
//        bipedLeftLeg.rotateAngleY = -0.3490658F;
//        bipedLeftLeg.rotateAngleZ = 0F;
        bipedLeftLeg.render(scale);

//        Fin2.rotateAngleX = 0F;
//        Fin2.rotateAngleY = -0.3490658F;
//        Fin2.rotateAngleZ = 0F;
        copyModelRotations(this.bipedLeftLeg, this.Fin2);
        Fin2.render(scale);
//
//        Fin2m1.rotateAngleX = 0F;
//        Fin2m1.rotateAngleY = -0.3490658F;
//        Fin2m1.rotateAngleZ = 0F;
        copyModelRotations(this.bipedLeftLeg, this.Fin2m1);
        Fin2m1.render(scale);
//
//        Fin2m2.rotateAngleX = 0F;
//        Fin2m2.rotateAngleY = -0.3490658F;
//        Fin2m2.rotateAngleZ = 0F;
        copyModelRotations(this.bipedLeftLeg, this.Fin2m2);
        Fin2m2.render(scale);
//
//        Fin2m3.rotateAngleX = 0F;
//        Fin2m3.rotateAngleY = -0.3490658F;
//        Fin2m3.rotateAngleZ = 0F;
        copyModelRotations(this.bipedLeftLeg, this.Fin2m3);
        Fin2m3.render(scale);
//
//        Fin2m4.rotateAngleX = 0F;
//        Fin2m4.rotateAngleY = -0.3490658F;
//        Fin2m4.rotateAngleZ = 0F;
        copyModelRotations(this.bipedLeftLeg, this.Fin2m4);
        Fin2m4.render(scale);

        BCD.rotateAngleX = 0F;
        BCD.rotateAngleY = 0F;
        BCD.rotateAngleZ = 0F;
        BCD.renderWithRotation(scale);

        BCD12.rotateAngleX = 0F;
        BCD12.rotateAngleY = 0F;
        BCD12.rotateAngleZ = 0F;
        BCD12.renderWithRotation(scale);

        BCD11.rotateAngleX = 0F;
        BCD11.rotateAngleY = 0F;
        BCD11.rotateAngleZ = 0F;
        BCD11.renderWithRotation(scale);

        BCD4.rotateAngleX = 0F;
        BCD4.rotateAngleY = 0F;
        BCD4.rotateAngleZ = 0F;
        BCD4.renderWithRotation(scale);

        Tank2.rotateAngleX = 0F;
        Tank2.rotateAngleY = 0F;
        Tank2.rotateAngleZ = 0F;
        Tank2.renderWithRotation(scale);

        Tank2m1.rotateAngleX = 0F;
        Tank2m1.rotateAngleY = 0F;
        Tank2m1.rotateAngleZ = 0F;
        Tank2m1.renderWithRotation(scale);

        Tank2m2.rotateAngleX = 0F;
        Tank2m2.rotateAngleY = -1.570796F;
        Tank2m2.rotateAngleZ = 0F;
        Tank2m2.renderWithRotation(scale);

        Tank2m3.rotateAngleX = 0F;
        Tank2m3.rotateAngleY = -1.570796F;
        Tank2m3.rotateAngleZ = 0F;
        Tank2m3.renderWithRotation(scale);

        Tank2m4.rotateAngleX = 0F;
        Tank2m4.rotateAngleY = 0F;
        Tank2m4.rotateAngleZ = 0F;
        Tank2m4.renderWithRotation(scale);

        Tank2m5.rotateAngleX = 0F;
        Tank2m5.rotateAngleY = 0F;
        Tank2m5.rotateAngleZ = 0F;
        Tank2m5.renderWithRotation(scale);

        Tank2m6.rotateAngleX = 0F;
        Tank2m6.rotateAngleY = 0F;
        Tank2m6.rotateAngleZ = 0F;
        Tank2m6.renderWithRotation(scale);

        Tank2m7.rotateAngleX = 0F;
        Tank2m7.rotateAngleY = 0F;
        Tank2m7.rotateAngleZ = 0F;
        Tank2m7.renderWithRotation(scale);

        BCD2.rotateAngleX = 0F;
        BCD2.rotateAngleY = 0F;
        BCD2.rotateAngleZ = 0F;
        BCD2.renderWithRotation(scale);

        Tank1.rotateAngleX = 0F;
        Tank1.rotateAngleY = 0F;
        Tank1.rotateAngleZ = 0F;
        Tank1.renderWithRotation(scale);

        Tank1m1.rotateAngleX = 0F;
        Tank1m1.rotateAngleY = 0F;
        Tank1m1.rotateAngleZ = 0F;
        Tank1m1.renderWithRotation(scale);

        Tank1m2.rotateAngleX = 0F;
        Tank1m2.rotateAngleY = -1.570796F;
        Tank1m2.rotateAngleZ = 0F;
        Tank1m2.renderWithRotation(scale);

        Tank1m3.rotateAngleX = 0F;
        Tank1m3.rotateAngleY = -1.570796F;
        Tank1m3.rotateAngleZ = 0F;
        Tank1m3.renderWithRotation(scale);

        Tank1m4.rotateAngleX = 0F;
        Tank1m4.rotateAngleY = 0F;
        Tank1m4.rotateAngleZ = 0F;
        Tank1m4.renderWithRotation(scale);

        Tank1m5.rotateAngleX = 0F;
        Tank1m5.rotateAngleY = 0F;
        Tank1m5.rotateAngleZ = 0F;
        Tank1m5.renderWithRotation(scale);

        Tank1m6.rotateAngleX = 0F;
        Tank1m6.rotateAngleY = 0F;
        Tank1m6.rotateAngleZ = 0F;
        Tank1m6.renderWithRotation(scale);

        Tank1m7.rotateAngleX = 0F;
        Tank1m7.rotateAngleY = 0F;
        Tank1m7.rotateAngleZ = 0F;
        Tank1m7.renderWithRotation(scale);

        BCD6.rotateAngleX = 0F;
        BCD6.rotateAngleY = 0F;
        BCD6.rotateAngleZ = 0F;
        BCD6.renderWithRotation(scale);

        BCD5.rotateAngleX = 0F;
        BCD5.rotateAngleY = 0F;
        BCD5.rotateAngleZ = 0F;
        BCD5.renderWithRotation(scale);

        BCD3.rotateAngleX = 0F;
        BCD3.rotateAngleY = 0F;
        BCD3.rotateAngleZ = 0F;
        BCD3.renderWithRotation(scale);

        BCD7.rotateAngleX = 0F;
        BCD7.rotateAngleY = 0F;
        BCD7.rotateAngleZ = 0F;
        BCD7.renderWithRotation(scale);

        BCD8.rotateAngleX = 0F;
        BCD8.rotateAngleY = 0F;
        BCD8.rotateAngleZ = 0F;
        BCD8.renderWithRotation(scale);

        BCD9.rotateAngleX = 0F;
        BCD9.rotateAngleY = 0F;
        BCD9.rotateAngleZ = 0F;
        BCD9.renderWithRotation(scale);

        BCD13.rotateAngleX = 0F;
        BCD13.rotateAngleY = 0F;
        BCD13.rotateAngleZ = 0F;
        BCD13.renderWithRotation(scale);

//        Mask.rotateAngleX = 0F;
//        Mask.rotateAngleY = 0F;
//        Mask.rotateAngleZ = 0F;
//        Mask.renderWithRotation(scale);
//
//        Mask2.rotateAngleX = 0F;
//        Mask2.rotateAngleY = 0F;
//        Mask2.rotateAngleZ = 0F;
//        Mask2.renderWithRotation(scale);
//
//        Mask3.rotateAngleX = 0F;
//        Mask3.rotateAngleY = 0F;
//        Mask3.rotateAngleZ = 0F;
//        Mask3.renderWithRotation(scale);
//
//        Mask4.rotateAngleX = 0F;
//        Mask4.rotateAngleY = 0F;
//        Mask4.rotateAngleZ = 0F;
//        Mask4.renderWithRotation(scale);
//
//        Mask5.rotateAngleX = 0F;
//        Mask5.rotateAngleY = 0F;
//        Mask5.rotateAngleZ = 0F;
//        Mask5.renderWithRotation(scale);
//
//        Mask6.rotateAngleX = 0F;
//        Mask6.rotateAngleY = 0F;
//        Mask6.rotateAngleZ = 0F;
//        Mask6.renderWithRotation(scale);
//
//        Mask7.rotateAngleX = 0F;
//        Mask7.rotateAngleY = 0F;
//        Mask7.rotateAngleZ = 0F;
//        Mask7.renderWithRotation(scale);
//
//        Mask8.rotateAngleX = 0F;
//        Mask8.rotateAngleY = 0F;
//        Mask8.rotateAngleZ = 0F;
//        Mask8.renderWithRotation(scale);
//
//        Mask9.rotateAngleX = 0F;
//        Mask9.rotateAngleY = 0F;
//        Mask9.rotateAngleZ = 0F;
//        Mask9.renderWithRotation(scale);
//
//        mouthpiece.rotateAngleX = 0F;
//        mouthpiece.rotateAngleY = 0F;
//        mouthpiece.rotateAngleZ = 0F;
//        mouthpiece.renderWithRotation(scale);
//
//        mouthpiece2.rotateAngleX = 0F;
//        mouthpiece2.rotateAngleY = 0F;
//        mouthpiece2.rotateAngleZ = 0F;
//        mouthpiece2.renderWithRotation(scale);
//
//        mouthpiece3.rotateAngleX = 0F;
//        mouthpiece3.rotateAngleY = 0F;
//        mouthpiece3.rotateAngleZ = 0F;
//        mouthpiece3.renderWithRotation(scale);

//        hose1.rotateAngleX = 0F;
//        hose1.rotateAngleY = 0F;
//        hose1.rotateAngleZ = 0F;
//        hose1.renderWithRotation(scale);
//
//        hose2.rotateAngleX = 0F;
//        hose2.rotateAngleY = 0F;
//        hose2.rotateAngleZ = 0F;
//        hose2.renderWithRotation(scale);
//
//        hose3.rotateAngleX = 0F;
//        hose3.rotateAngleY = 0F;
//        hose3.rotateAngleZ = 0F;
//        hose3.renderWithRotation(scale);
//
//        hose4.rotateAngleX = 0.3075211F;
//        hose4.rotateAngleY = 0F;
//        hose4.rotateAngleZ = 0F;
//        hose4.renderWithRotation(scale);
//
//        hose5.rotateAngleX = 0F;
//        hose5.rotateAngleY = 0F;
//        hose5.rotateAngleZ = 0F;
//        hose5.renderWithRotation(scale);
//
//        hose6.rotateAngleX = 0F;
//        hose6.rotateAngleY = 0F;
//        hose6.rotateAngleZ = 0F;
//        hose6.renderWithRotation(scale);

        //     super.render(par1Entity, par2, par3, par4, par5, par6, par7);
    }
    
    /**
     * Copies the angles from one object to another. This is used when objects should stay aligned with each other, like
     * the hair over a players head.
     */
    public static void copyModelRotations(ModelRenderer source, ModelRenderer dest)
    {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
    }
    
    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}