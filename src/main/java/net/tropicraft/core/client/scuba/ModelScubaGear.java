package net.tropicraft.core.client.scuba;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.common.item.scuba.ScubaGogglesItem;
import net.tropicraft.core.common.item.scuba.ScubaType;

public class ModelScubaGear extends BipedModel<LivingEntity> {
    
    public static final ModelScubaGear CHEST = new ModelScubaGear(0, EquipmentSlotType.CHEST);
    public static final ModelScubaGear FEET = new ModelScubaGear(0, EquipmentSlotType.FEET);
    public static final ModelScubaGear HEAD = new ModelScubaGear(0, EquipmentSlotType.HEAD);
    
    //   RendererModel bipedHead;
    //   RendererModel bipedBody;
    //   RendererModel bipedRightArm;
    //   RendererModel bipedLeftArm;
    //    RendererModel bipedRightLeg;
    RendererModel Fin1;
    RendererModel Fin1m1;
    RendererModel Fin1m2;
    RendererModel Fin1m3;
    RendererModel Fin1m4;
    //    RendererModel bipedLeftLeg;
    RendererModel Fin2;
    RendererModel Fin2m1;
    RendererModel Fin2m2;
    RendererModel Fin2m3;
    RendererModel Fin2m4;
    RendererModel BCD;
    RendererModel BCD12;
    RendererModel BCD11;
    RendererModel BCD4;
    RendererModel Tank2;
    RendererModel Tank2m1;
    RendererModel Tank2m2;
    RendererModel Tank2m3;
    RendererModel Tank2m4;
    RendererModel Tank2m5;
    RendererModel Tank2m6;
    RendererModel Tank2m7;
    RendererModel BCD2;
    RendererModel Tank1;
    RendererModel Tank1m1;
    RendererModel Tank1m2;
    RendererModel Tank1m3;
    RendererModel Tank1m4;
    RendererModel Tank1m5;
    RendererModel Tank1m6;
    RendererModel Tank1m7;
    RendererModel BCD6;
    RendererModel BCD5;
    RendererModel BCD3;
    RendererModel BCD7;
    RendererModel BCD8;
    RendererModel BCD9;
    RendererModel BCD13;
    RendererModel Mask;
    RendererModel Mask2;
    RendererModel Mask3;
    RendererModel Mask4;
    RendererModel Mask5;
    RendererModel Mask6;
    RendererModel Mask7;
    RendererModel Mask8;
    RendererModel Mask9;
    RendererModel mouthpiece;
    RendererModel mouthpiece2;
    RendererModel mouthpiece3;
    RendererModel hose1;
    RendererModel hose2;
    RendererModel hose3;
    RendererModel hose4;
    RendererModel hose5;
    RendererModel hose6;
    
    public EquipmentSlotType slot;

    public ModelScubaGear() {
        this( 0.0f, null );
    }

    public ModelScubaGear( float par1, EquipmentSlotType slot ) {
        //super(par1);
        this.slot = slot;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        bipedBody = new RendererModel( this, 32, 16 );
        bipedBody.setTextureSize( 128, 64 );
        bipedBody.addBox( -4F, -6F, -2F, 8, 12, 4);
        bipedBody.setRotationPoint( 0F, 6F, 0F );
        bipedRightArm = new RendererModel( this, 56, 16 );
        bipedRightArm.setTextureSize( 128, 64 );
        bipedRightArm.addBox( -4F, 0F, -2F, 4, 12, 4);
        bipedRightArm.setRotationPoint( -4F, 0F, 0F );
        bipedLeftArm = new RendererModel( this, 72, 16 );
        bipedLeftArm.setTextureSize( 128, 64 );
        bipedLeftArm.addBox( 0F, 0F, -2F, 4, 12, 4);
        bipedLeftArm.setRotationPoint( 4F, 0F, 0F );
        bipedRightLeg = new RendererModel( this, 0, 16 );
        bipedRightLeg.setTextureSize( 128, 64 );
        bipedRightLeg.addBox( -2F, -6F, -2F, 4, 12, 4);
        bipedRightLeg.setRotationPoint( -2F, 18F, 0F );
        Fin1 = new RendererModel( this, 10, 38 );
        Fin1.setTextureSize( 128, 64 );
        Fin1.addBox( -5F, 22F, -2.5F, 5, 2, 5);
        Fin1.setRotationPoint( 2.5F, -12F, 0F );
        Fin1.mirror = true;
        Fin1m1 = new RendererModel( this, 13, 47 );
        Fin1m1.setTextureSize( 128, 64 );
        Fin1m1.addBox( -2.5F, -1.5F, -1F, 5, 1, 2);
        Fin1m1.setRotationPoint( -3.19707F, 24.5F, -3.288924F );
        setRotation(Fin1m1, 0F, 0F, 0F);
        Fin1m1.mirror = true;
        Fin1m2 = new RendererModel( this, 15, 45 );
        Fin1m2.setTextureSize( 128, 64 );
        Fin1m2.addBox( -2F, -1.5F, -0.5F, 4, 1, 1);
        Fin1m2.setRotationPoint( -3.02606F, 23.5F, -2.819078F );
        setRotation(Fin1m2, 0F, 0F, 0F);
        Fin1m2.mirror = true;
        Fin1m3 = new RendererModel( this, 1, 52 );
        Fin1m3.setTextureSize( 128, 64 );
        Fin1m3.addBox( -5F, -1F, -6F, 10, 0, 12);
        Fin1m3.setRotationPoint( -5.420201F, 24.5F, -9.396926F );
        setRotation(Fin1m3, 0F, 0F, 0F);
        Fin1m3.mirror = true;
        Fin1m4 = new RendererModel( this, 15, 50 );
        Fin1m4.setTextureSize( 128, 64 );
        Fin1m4.addBox( -2F, -1.5F, -0.5F, 4, 1, 1);
        Fin1m4.setRotationPoint( -3.710101F, 24.5F, -4.698463F );
        setRotation(Fin1m4, 0F, 0F, 0F);
        Fin1m4.mirror = true;
        Fin1.addChild(Fin1m1);
        Fin1.addChild(Fin1m2);
        Fin1.addChild(Fin1m3);
        Fin1.addChild(Fin1m4);
        setRotation(Fin1, 0F, 0F, 0F);
        bipedRightLeg.addChild(Fin1);
        bipedLeftLeg = new RendererModel( this, 16, 16 );
        bipedLeftLeg.setTextureSize( 128, 64 );
        bipedLeftLeg.addBox( -2F, -6F, -2F, 4, 12, 4);
        bipedLeftLeg.setRotationPoint( 2F, 18F, 0F );
        Fin2 = new RendererModel( this, 10, 38 );
        Fin2.setTextureSize( 128, 64 );
        Fin2.addBox( 0F, 22F, -2.5F, 5, 2, 5);
        Fin2.setRotationPoint( -2F, -12F, 0F );
        Fin2m1 = new RendererModel( this, 13, 47 );
        Fin2m1.setTextureSize( 128, 64 );
        Fin2m1.addBox( -2.5F, -1.5F, -1F, 5, 1, 2);
        Fin2m1.setRotationPoint( 3.19707F, 24.5F, -3.288924F );
        setRotation(Fin2m1, 0F, 0F, 0F);
        Fin2m2 = new RendererModel( this, 15, 45 );
        Fin2m2.setTextureSize( 128, 64 );
        Fin2m2.addBox( -2F, -1.5F, -0.5F, 4, 1, 1);
        Fin2m2.setRotationPoint( 3.02606F, 23.5F, -2.819078F );
        setRotation(Fin2m2, 0F, 0F, 0F);
        Fin2m3 = new RendererModel( this, 1, 52 );
        Fin2m3.setTextureSize( 128, 64 );
        Fin2m3.addBox( -5F, -1.0F, -6F, 10, 0, 12);
        Fin2m3.setRotationPoint( 5.420201F, 24.5F, -9.396926F );
        setRotation(Fin2m3, 0F, 0F, 0F);
        Fin2m4 = new RendererModel( this, 15, 50 );
        Fin2m4.setTextureSize( 128, 64 );
        Fin2m4.addBox( -2F, -1.5F, -0.5F, 4, 1, 1);
        Fin2m4.setRotationPoint( 3.710101F, 24.5F, -4.698463F );
        setRotation(Fin2m4, 0F, 0F, 0F);
        Fin2.addChild(Fin2m1);
        Fin2.addChild(Fin2m2);
        Fin2.addChild(Fin2m3);
        Fin2.addChild(Fin2m4);
        setRotation(Fin2, 0F, 0F, 0F);
        bipedLeftLeg.addChild(Fin2);
        BCD = new RendererModel( this, 65, 50 );
        BCD.setTextureSize( 128, 64 );
        BCD.addBox( -4F, -6F, -1F, 8, 12, 2);
        BCD.setRotationPoint( 0F, 6.5F, 3F );
        BCD12 = new RendererModel( this, 102, 46 );
        BCD12.setTextureSize( 128, 64 );
        BCD12.addBox( -0.5F, -0.5F, -0.5F, 1, 1, 1);
        BCD12.setRotationPoint( 0F, 10F, -2.7F );
        BCD11 = new RendererModel( this, 79, 42 );
        BCD11.setTextureSize( 128, 64 );
        BCD11.addBox( -0.5F, -0.5F, -2F, 1, 1, 4);
        BCD11.setRotationPoint( 3.6F, 3F, 0F );
        BCD4 = new RendererModel( this, 97, 50 );
        BCD4.setTextureSize( 128, 64 );
        BCD4.addBox( -1F, -5.5F, -0.5F, 2, 11, 1);
        BCD4.setRotationPoint( 3F, 5.5F, -2.5F );
        Tank2 = new RendererModel( this, 41, 50 );
        Tank2.setTextureSize( 128, 64 );
        Tank2.addBox( -2F, -5F, -2F, 4, 10, 4);
        Tank2.setRotationPoint( -3F, 7F, 6.5F );
        Tank2m1 = new RendererModel( this, 45, 54 );
        Tank2m1.setTextureSize( 128, 64 );
        Tank2m1.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank2m1.setRotationPoint( -3F, 7F, 8.5F );
        Tank2m2 = new RendererModel( this, 45, 54 );
        Tank2m2.setTextureSize( 128, 64 );
        Tank2m2.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank2m2.setRotationPoint( -5F, 7F, 6.5F );
        Tank2m3 = new RendererModel( this, 45, 54 );
        Tank2m3.setTextureSize( 128, 64 );
        Tank2m3.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank2m3.setRotationPoint( -1F, 7F, 6.5F );
        Tank2m4 = new RendererModel( this, 43, 46 );
        Tank2m4.setTextureSize( 128, 64 );
        Tank2m4.addBox( -1.5F, -0.5F, -1.5F, 3, 1, 3);
        Tank2m4.setRotationPoint( -3F, 1.5F, 6.5F );
        Tank2m5 = new RendererModel( this, 38, 49 );
        Tank2m5.setTextureSize( 128, 64 );
        Tank2m5.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
        Tank2m5.setRotationPoint( -3F, -0.5F, 6.5F );
        Tank2m6 = new RendererModel( this, 44, 44 );
        Tank2m6.setTextureSize( 128, 64 );
        Tank2m6.addBox( -2F, -0.5F, -0.5F, 4, 1, 1);
        Tank2m6.setRotationPoint( -3.5F, -0.5F, 6.5F );
        Tank2m7 = new RendererModel( this, 36, 44 );
        Tank2m7.setTextureSize( 128, 64 );
        Tank2m7.addBox( -1F, -1F, -1F, 2, 2, 2);
        Tank2m7.setRotationPoint( -5.5F, -0.5F, 6.5F );
        BCD2 = new RendererModel( this, 66, 51 );
        BCD2.setTextureSize( 128, 64 );
        BCD2.addBox( -3.5F, -5F, -0.5F, 7, 10, 1);
        BCD2.setRotationPoint( 0F, 6.5F, 4F );
        Tank1 = new RendererModel( this, 41, 50 );
        Tank1.setTextureSize( 128, 64 );
        Tank1.addBox( -2F, -5F, -2F, 4, 10, 4);
        Tank1.setRotationPoint( 3F, 7F, 6.5F );
        Tank1m1 = new RendererModel( this, 45, 54 );
        Tank1m1.setTextureSize( 128, 64 );
        Tank1m1.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank1m1.setRotationPoint( 3F, 7F, 8.5F );
        Tank1m2 = new RendererModel( this, 45, 54 );
        Tank1m2.setTextureSize( 128, 64 );
        Tank1m2.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank1m2.setRotationPoint( 1F, 7F, 6.5F );
        Tank1m3 = new RendererModel( this, 45, 54 );
        Tank1m3.setTextureSize( 128, 64 );
        Tank1m3.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank1m3.setRotationPoint( 5F, 7F, 6.5F );
        Tank1m4 = new RendererModel( this, 43, 46 );
        Tank1m4.setTextureSize( 128, 64 );
        Tank1m4.addBox( -1.5F, -0.5F, -1.5F, 3, 1, 3);
        Tank1m4.setRotationPoint( 3F, 1.5F, 6.5F );
        Tank1m5 = new RendererModel( this, 38, 49 );
        Tank1m5.setTextureSize( 128, 64 );
        Tank1m5.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
        Tank1m5.setRotationPoint( 3F, -0.5F, 6.5F );
        Tank1m6 = new RendererModel( this, 44, 44 );
        Tank1m6.setTextureSize( 128, 64 );
        Tank1m6.addBox( -2F, -0.5F, -0.5F, 4, 1, 1);
        Tank1m6.setRotationPoint( 3.5F, -0.5F, 6.5F );
        Tank1m7 = new RendererModel( this, 36, 44 );
        Tank1m7.setTextureSize( 128, 64 );
        Tank1m7.addBox( -1F, -1F, -1F, 2, 2, 2);
        Tank1m7.setRotationPoint( 5.5F, -0.5F, 6.5F );
        BCD6 = new RendererModel( this, 68, 41 );
        BCD6.setTextureSize( 128, 64 );
        BCD6.addBox( -0.5F, -1F, -2F, 1, 2, 4);
        BCD6.setRotationPoint( -3.6F, 10F, 0F );
        BCD5 = new RendererModel( this, 68, 41 );
        BCD5.setTextureSize( 128, 64 );
        BCD5.addBox( -0.5F, -1F, -2F, 1, 2, 4);
        BCD5.setRotationPoint( 3.6F, 10F, 0F );
        BCD3 = new RendererModel( this, 91, 50 );
        BCD3.setTextureSize( 128, 64 );
        BCD3.addBox( -1F, -5.5F, -0.5F, 2, 11, 1);
        BCD3.setRotationPoint( -3F, 5.5F, -2.5F );
        BCD7 = new RendererModel( this, 91, 45 );
        BCD7.setTextureSize( 128, 64 );
        BCD7.addBox( -2F, -1F, -0.5F, 4, 2, 1);
        BCD7.setRotationPoint( 0F, 10F, -2.5F );
        BCD8 = new RendererModel( this, 91, 48 );
        BCD8.setTextureSize( 128, 64 );
        BCD8.addBox( -2F, -0.5F, -0.5F, 4, 1, 1);
        BCD8.setRotationPoint( 0F, 3F, -2.5F );
        BCD9 = new RendererModel( this, 79, 42 );
        BCD9.setTextureSize( 128, 64 );
        BCD9.addBox( -0.5F, -0.5F, -2F, 1, 1, 4);
        BCD9.setRotationPoint( -3.6F, 3F, 0F );
        BCD13 = new RendererModel( this, 91, 38 );
        BCD13.setTextureSize( 128, 64 );
        BCD13.addBox( -4F, -0.5F, -0.5F, 8, 1, 1);
        BCD13.setRotationPoint( 0F, 0.5F, 2.5F );
        Mask = new RendererModel( this, 109, 60 );
        Mask.setTextureSize( 128, 64 );
        Mask.addBox( -4F, -0.5F, -0.5F, 8, 1, 1);
        Mask.setRotationPoint( 0F, -6F, -4.5F );
        Mask2 = new RendererModel( this, 120, 55 );
        Mask2.setTextureSize( 128, 64 );
        Mask2.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
        Mask2.setRotationPoint( -4F, -4F, -4.5F );
        Mask3 = new RendererModel( this, 116, 55 );
        Mask3.setTextureSize( 128, 64 );
        Mask3.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
        Mask3.setRotationPoint( 4F, -4F, -4.5F );
        Mask4 = new RendererModel( this, 114, 51 );
        Mask4.setTextureSize( 128, 64 );
        Mask4.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        Mask4.setRotationPoint( -2.5F, -2F, -4.5F );
        Mask5 = new RendererModel( this, 114, 53 );
        Mask5.setTextureSize( 128, 64 );
        Mask5.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        Mask5.setRotationPoint( 2.5F, -2F, -4.5F );
        Mask6 = new RendererModel( this, 114, 49 );
        Mask6.setTextureSize( 128, 64 );
        Mask6.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        Mask6.setRotationPoint( 0F, -3F, -4.5F );
        Mask7 = new RendererModel( this, 110, 38 );
        Mask7.setTextureSize( 128, 64 );
        Mask7.addBox( -0.5F, -1F, -4F, 1, 2, 8);
        Mask7.setRotationPoint( 4F, -4.5F, 0F );
        Mask8 = new RendererModel( this, 110, 38 );
        Mask8.setTextureSize( 128, 64 );
        Mask8.addBox( -0.5F, -1F, -4F, 1, 2, 8);
        Mask8.setRotationPoint( -4F, -4.5F, 0F );
        Mask9 = new RendererModel( this, 110, 35 );
        Mask9.setTextureSize( 128, 64 );
        Mask9.addBox( -4F, -1F, -0.5F, 8, 2, 1);
        Mask9.setRotationPoint( 0F, -4.5F, 4F );
        mouthpiece = new RendererModel( this, 115, 28 );
        mouthpiece.setTextureSize( 128, 64 );
        mouthpiece.addBox( -1.5F, -1.5F, -0.5F, 3, 3, 1);
        mouthpiece.setRotationPoint( 0F, 0F, -5F );
        mouthpiece2 = new RendererModel( this, 116, 25 );
        mouthpiece2.setTextureSize( 128, 64 );
        mouthpiece2.addBox( -1F, -1F, -0.5F, 2, 2, 1);
        mouthpiece2.setRotationPoint( 0F, 0F, -5.5F );
        mouthpiece3 = new RendererModel( this, 116, 23 );
        mouthpiece3.setTextureSize( 128, 64 );
        mouthpiece3.addBox( -1F, -0.5F, -0.5F, 2, 1, 1);
        mouthpiece3.setRotationPoint( 0F, -0.6000004F, -4F );
        bipedHead = new RendererModel( this, 0, 0 );
        bipedHead.setTextureSize( 128, 64 );
        bipedHead.addBox( -4F, -4F, -4F, 8, 8, 8);
        bipedHead.setRotationPoint( 0F, -4F, 0F );
        bipedHead.mirror = true;
        setRotation(bipedHead, 0F, 0F, 0F);
        hose1 = new RendererModel( this, 117, 16 );
        hose1.setTextureSize( 128, 64 );
        hose1.addBox( -0.5F, -0.5F, -0.5F, 1, 1, 1);
        hose1.setRotationPoint( 3F, -3F, 6.5F );
        hose2 = new RendererModel( this, 117, 16 );
        hose2.setTextureSize( 128, 64 );
        hose2.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        hose2.setRotationPoint( 5F, -3F, 6.5F );
        hose3 = new RendererModel( this, 116, 15 );
        hose3.setTextureSize( 128, 64 );
        hose3.addBox( -0.5F, -0.5F, -1F, 1, 1, 2);
        hose3.setRotationPoint( 6F, -3F, 5F );
        hose4 = new RendererModel( this, 106, 7 );
        hose4.setTextureSize( 128, 64 );
        hose4.addBox( -0.5F, -0.5F, -10F, 1, 1, 10);
        hose4.setRotationPoint( 6F, -3F, 4.2F );
        hose5 = new RendererModel( this, 115, 16 );
        hose5.setTextureSize( 128, 64 );
        hose5.addBox( -2.5F, -0.5F, -0.5F, 5, 1, 1);
        hose5.setRotationPoint( 4F, 0F, -5F );
        hose6 = new RendererModel( this, 115, 16 );
        hose6.setTextureSize( 128, 64 );
        hose6.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        hose6.setRotationPoint( 0F, -0.5F, 6.5F );
        bipedHead.addChild(Mask);
        bipedHead.addChild(Mask2);
        bipedHead.addChild(Mask3);
        bipedHead.addChild(Mask4);
        bipedHead.addChild(Mask5);
        bipedHead.addChild(Mask6);
        bipedHead.addChild(Mask7);
        bipedHead.addChild(Mask8);
        bipedHead.addChild(mouthpiece);
        bipedHead.addChild(mouthpiece2);
        bipedHead.addChild(mouthpiece3);
        bipedHead.addChild(hose1);
        bipedHead.addChild(hose2);
        bipedHead.addChild(hose3);
        bipedHead.addChild(hose4);
        bipedHead.addChild(hose5);
    }

    @Override
    public void render(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.pushMatrix();

        if (this.isChild)
        {
            GlStateManager.scalef(0.75F, 0.75F, 0.75F);
            GlStateManager.translatef(0.0F, 16.0F * scale, 0.0F);
            this.bipedHead.render(scale);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scalef(0.5F, 0.5F, 0.5F);
            GlStateManager.translatef(0.0F, 24.0F * scale, 0.0F);
            this.renderScubaGear(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, false);
        }
        else
        {
            if (entityIn.getPose() == Pose.SNEAKING)
            {
                GlStateManager.translatef(0.0F, 0.2F, 0.0F);
            }

            this.renderScubaGear(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, true);
        }

        GlStateManager.popMatrix();
    }

    public void renderScubaGear(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale, boolean renderHead) {
        hose4.rotateAngleX = 0.3075211F;

        LivingEntity player = (LivingEntity)entityIn;

        boolean showHead = !player.getItemStackFromSlot(EquipmentSlotType.HEAD).isEmpty() && this.slot == EquipmentSlotType.HEAD;
        bipedHead.showModel = showHead;
        boolean showChest = !player.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty() && this.slot == EquipmentSlotType.CHEST;
        bipedBody.showModel = showChest;
        boolean showLegs = !player.getItemStackFromSlot(EquipmentSlotType.FEET).isEmpty() && this.slot == EquipmentSlotType.FEET;
        
        mouthpiece.showModel = showChest;
        mouthpiece2.showModel = showChest;
        mouthpiece3.showModel = showChest;
        hose1.showModel = showChest;
        hose2.showModel = showChest;
        hose3.showModel = showChest;
        hose4.showModel = showChest;
        hose5.showModel = showChest;
        hose6.showModel = showChest;

        // TODO what was this doing?
//        if (showHead) {
//            hose6.rotateAngleX = 0F;
//            hose6.rotateAngleY = 0F;
//            hose6.rotateAngleZ = 0F;
//            hose6.renderWithRotation(scale);
//        }

        if (showChest) {
            renderTank(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            renderBCD(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
        
        bipedRightLeg.showModel = showLegs;
        bipedLeftLeg.showModel = showLegs;

        if (renderHead) {
            bipedHead.render(scale);
        }

        bipedBody.render(scale);
        bipedRightArm.render(scale);
        bipedLeftArm.render(scale);
       
        
        GlStateManager.enableCull();
        bipedRightLeg.mirror = true;
        
        bipedLeftLeg.rotationPointY = 0;
        bipedLeftLeg.offsetY = 0.763f;

        
        if(showLegs) {
        if(entityIn.isInWater()) {
	        this.Fin2m3.offsetX = -0.2f;
	        this.Fin1m3.offsetX = 0.2f;
	
	
	       this.Fin1m3.offsetZ = 0.1f;
	       this.Fin2m3.offsetZ = 0.1f;
	        
	        
	        
	        bipedLeftLeg.render(scale);
	        bipedRightLeg.render(scale);
	        
	     
	        
	      /*  GlStateManager.pushMatrix();
		     
		        

		        GlStateManager.translate(0f, 0f, 0.22f+((-MathHelper.cos(limbSwing * 0.6662F))/(1.7f*Math.PI)));
		        GlStateManager.translate(0f, -0.05f+(float)(-MathHelper.cos(limbSwing)/64), 0f);

		        
		        GlStateManager.translate(0f, offsetY, -0.4f);
		        
		        GlStateManager.rotate(-paddleAngle+((float)Math.sin(ageInTicks*paddleSpeed)*paddleAmount), 1f, 0f, 0f);
		   
		        GlStateManager.translate(0f, -offsetY, 0.2f);
		        


		        this.Fin2m3.render(scale);
		        
		        GlStateManager.popMatrix();*/

	       	        
		        
		     
	        
	        
	      /*  GlStateManager.pushMatrix();
	     
	        GlStateManager.translate(0, 0, 0.1f);

	        GlStateManager.translate(0f, 0f, 0.23f+((MathHelper.cos(limbSwing * 0.6662f))/(1.7f*Math.PI)));
	        GlStateManager.translate(0f, -0.05f+(float)(MathHelper.cos(limbSwing)/64), 0f);

	        
	        GlStateManager.translate(0f, offsetY, -0.4f);
	        
	        GlStateManager.rotate(-paddleAngle-((float)Math.sin(ageInTicks*paddleSpeed)*paddleAmount), 1f, 0f, 0f);
	   
	        GlStateManager.translate(0f, -offsetY, 0.2f);


	        this.Fin1m3.render(scale);
	        
	        GlStateManager.popMatrix();*/

	   
		
	        
        }else {
        		this.Fin2m3.offsetX = 0f;
        		
 	        this.Fin1m3.offsetX = 0f;
 	
 	        this.Fin2m3.offsetY = 0f;
 	
 	        this.Fin1m3.offsetY = 0f;
 	        
 	        this.Fin2m3.rotateAngleX = 0f;
 	
 	        this.Fin1m3.rotateAngleX = 0f;
 	        
 	       bipedLeftLeg.render(scale);

 	        bipedRightLeg.render(scale);
 	    
        }

        }
        
         
    }

    private void renderTank(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        TextureManager tm = Minecraft.getInstance().getTextureManager();

        ItemStack gearStack = entityIn.getItemStackFromSlot(EquipmentSlotType.CHEST);
        ScubaType material = ScubaType.YELLOW; // TODO Scuba gear.getStackInSlot(0).getItem() == ItemRegistry.pinkScubaTank ? ScubaMaterial.PINK : ScubaMaterial.YELLOW;
        tm.bindTexture(ScubaGogglesItem.getArmorTexture(material));
        
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
 
        tm.bindTexture(new ResourceLocation(((ArmorItem)gearStack.getItem()).getArmorTexture(gearStack, entityIn, EquipmentSlotType.CHEST, null)));
    }

    private void renderBCD(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
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

        BCD2.rotateAngleX = 0F;
        BCD2.rotateAngleY = 0F;
        BCD2.rotateAngleZ = 0F;
        BCD2.renderWithRotation(scale);

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
    }

    /**
     * Copies the angles from one object to another. This is used when objects should stay aligned with each other, like
     * the hair over a players head.
     */
    public static void copyModelRotations(RendererModel source, RendererModel dest)
    {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
    }

    private void setRotation(RendererModel model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}