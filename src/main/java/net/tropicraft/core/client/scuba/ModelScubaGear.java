package net.tropicraft.core.client.scuba;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.platform.GlStateManager;

import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.PlayerHeadpieceModel;

public class ModelScubaGear extends HumanoidModel<LivingEntity> {

    public static ModelScubaGear HEAD; //= ModelScubaGear.createModel(TropicraftRenderLayers.HEAD_SCUBA_LAYER, null, EquipmentSlot.CHEST);
    public static ModelScubaGear CHEST; //= ModelScubaGear.createModel(TropicraftRenderLayers.CHEST_SCUBA_LAYER, null, EquipmentSlot.CHEST);
    public static ModelScubaGear FEET; //= ModelScubaGear.createModel(TropicraftRenderLayers.FEET_SCUBA_LAYER, null, EquipmentSlot.CHEST);

    public static ModelScubaGear tankModel; //= ModelScubaGear.createModel(TropicraftRenderLayers.TANK_SCUBA_LAYER, null, EquipmentSlot.CHEST); // Can't reuse the main one with a different scale

    private boolean showHead;
    public boolean showChest;
    private boolean showLegs;
    private boolean isSneaking;

    ModelPart Fin1;
    ModelPart Fin1m1;
    ModelPart Fin1m2;
    ModelPart Fin1m3;
    ModelPart Fin1m4;
    ModelPart Fin2;
    ModelPart Fin2m1;
    ModelPart Fin2m2;
    ModelPart Fin2m3;
    ModelPart Fin2m4;
    ModelPart BCD;
    ModelPart BCD12;
    ModelPart BCD11;
    ModelPart BCD4;
    ModelPart Tank2;
    ModelPart Tank2m1;
    ModelPart Tank2m2;
    ModelPart Tank2m3;
    ModelPart Tank2m4;
    ModelPart Tank2m5;
    ModelPart Tank2m6;
    ModelPart Tank2m7;
    ModelPart BCD2;
    ModelPart Tank1;
    ModelPart Tank1m1;
    ModelPart Tank1m2;
    ModelPart Tank1m3;
    ModelPart Tank1m4;
    ModelPart Tank1m5;
    ModelPart Tank1m6;
    ModelPart Tank1m7;
    ModelPart BCD6;
    ModelPart BCD5;
    ModelPart BCD3;
    ModelPart BCD7;
    ModelPart BCD8;
    ModelPart BCD9;
    ModelPart BCD13;
    ModelPart Mask;
    ModelPart Mask2;
    ModelPart Mask3;
    ModelPart Mask4;
    ModelPart Mask5;
    ModelPart Mask6;
    ModelPart Mask7;
    ModelPart Mask8;
    ModelPart Mask9;
    ModelPart mouthpiece;
    ModelPart mouthpiece2;
    ModelPart mouthpiece3;
    ModelPart hose1;
    ModelPart hose2;
    ModelPart hose3;
    ModelPart hose4;
    ModelPart hose5;
    ModelPart hose6;
    
    public EquipmentSlot slot;

//    public ModelScubaGear() {
//        this( 0.0f, null );
//    }

    public ModelScubaGear(ModelPart root, final EquipmentSlot slot) {
        super(root, RenderType::entityCutout);
        this.slot = slot;

        leftArmPose = HumanoidModel.ArmPose.EMPTY;
        rightArmPose = HumanoidModel.ArmPose.EMPTY;

        Fin1 = root.getChild("right_leg").getChild("Fin1");
        Fin1m1 = Fin1.getChild("Fin1m1");
        Fin1m2 = Fin1.getChild("Fin1m2");
        Fin1m3 = Fin1.getChild("Fin1m3");
        Fin1m4 = Fin1.getChild("Fin1m4");

        Fin2 = root.getChild("left_leg").getChild("Fin2");
        Fin2m1 = Fin2.getChild("Fin2m1");
        Fin2m2 = Fin2.getChild("Fin2m2");
        Fin2m3 = Fin2.getChild("Fin2m3");
        Fin2m4 = Fin2.getChild("Fin2m4");

        BCD = root.getChild("BCD");
        BCD12 = root.getChild("BCD12");
        BCD11 = root.getChild("BCD11");
        BCD4 = root.getChild("BCD4");
        Tank2 = root.getChild("Tank2");
        Tank2m1 = root.getChild("Tank2m1");
        Tank2m2 = root.getChild("Tank2m2");
        Tank2m3 = root.getChild("Tank2m3");
        Tank2m4 = root.getChild("Tank2m4");
        Tank2m5 = root.getChild("Tank2m5");
        Tank2m6 = root.getChild("Tank2m6");
        Tank2m7 = root.getChild("Tank2m7");
        BCD2 = root.getChild("BCD2");
        Tank1 = root.getChild("Tank1");
        Tank1m1 = root.getChild("Tank1m1");
        Tank1m2 = root.getChild("Tank1m2");
        Tank1m3 = root.getChild("Tank1m3");
        Tank1m4 = root.getChild("Tank1m4");
        Tank1m5 = root.getChild("Tank1m5");
        Tank1m6 = root.getChild("Tank1m6");
        Tank1m7 = root.getChild("Tank1m7");
        BCD6 = root.getChild("BCD6");
        BCD5 = root.getChild("BCD5");
        BCD3 = root.getChild("BCD3");
        BCD7 = root.getChild("BCD7");
        BCD8 = root.getChild("BCD8");
        BCD9 = root.getChild("BCD9");
        BCD13 = root.getChild("BCD13");

        ModelPart Head = root.getChild("head");

        Mask = Head.getChild("Mask");
        Mask2 = Head.getChild("Mask2");
        Mask3 = Head.getChild("Mask3");
        Mask4 = Head.getChild("Mask4");
        Mask5 = Head.getChild("Mask5");
        Mask6 = Head.getChild("Mask6");
        Mask7 = Head.getChild("Mask7");
        Mask8 = Head.getChild("Mask8");

        Mask9 = root.getChild("Mask9"); //Head.getChild("Mask9");

        mouthpiece = Head.getChild("mouthpiece");
        mouthpiece2 = Head.getChild("mouthpiece2");
        mouthpiece3 = Head.getChild("mouthpiece3");
        hose1 = Head.getChild("hose1");
        hose2 = Head.getChild("hose2");
        hose3 = Head.getChild("hose3");
        hose4 = Head.getChild("hose4");
        hose5 = Head.getChild("hose5");

        hose6 = root.getChild("hose6"); //Head.getChild("hose6");

//        body = new ModelPart( this, 32, 16 );
//        body.setTexSize( 128, 64 );
//        body.addBox( -4F, -6F, -2F, 8, 12, 4);
//        body.setPos( 0F, 6F, 0F );
//
//        rightArm = new ModelPart( this, 56, 16 );
//        rightArm.setTexSize( 128, 64 );
//        rightArm.addBox( -4F, 0F, -2F, 4, 12, 4);
//        rightArm.setPos( -4F, 0F, 0F );
//
//        leftArm = new ModelPart( this, 72, 16 );
//        leftArm.setTexSize( 128, 64 );
//        leftArm.addBox( 0F, 0F, -2F, 4, 12, 4);
//        leftArm.setPos( 4F, 0F, 0F );
//
//        ///////////////////////////////////////////////////////////////////////////////////////////
//
//        rightLeg = new ModelPart( this, 0, 16 );
//        rightLeg.setTexSize( 128, 64 );
//        rightLeg.addBox( -2F, -6F, -2F, 4, 12, 4);
//        rightLeg.setPos( -2F, 18F, 0F );
//
//        Fin1 = new ModelPart( this, 10, 38 );
//        Fin1.setTexSize( 128, 64 );
//        Fin1.addBox( -5F, 22F, -2.5F, 5, 2, 5);
//        Fin1.setPos( 2.5F, -12F, 0F );
//        setRotation(Fin1, 0F, 0F, 0F);
//        Fin1.mirror = true;
//        rightLeg.addChild(Fin1);
//
//        Fin1m1 = new ModelPart( this, 13, 47 );
//        Fin1m1.setTexSize( 128, 64 );
//        Fin1m1.addBox( -2.5F, -1.5F, -1F, 5, 1, 2);
//        Fin1m1.setPos( -3.19707F, 24.5F, -3.288924F );
//        setRotation(Fin1m1, 0F, 0F, 0F);
//        Fin1m1.mirror = true;
//        Fin1.addChild(Fin1m1);
//
//        Fin1m2 = new ModelPart( this, 15, 45 );
//        Fin1m2.setTexSize( 128, 64 );
//        Fin1m2.addBox( -2F, -1.5F, -0.5F, 4, 1, 1);
//        Fin1m2.setPos( -3.02606F, 23.5F, -2.819078F );
//        setRotation(Fin1m2, 0F, 0F, 0F);
//        Fin1m2.mirror = true;
//        Fin1.addChild(Fin1m2);
//
//        Fin1m3 = new ModelPart( this, 1, 52 );
//        Fin1m3.setTexSize( 128, 64 );
//        Fin1m3.addBox( -5F, -1F, -6F, 10, 0, 12);
//        Fin1m3.setPos( -5.420201F, 24.5F, -9.396926F );
//        setRotation(Fin1m3, 0F, 0F, 0F);
//        Fin1m3.mirror = true;
//        Fin1.addChild(Fin1m3);
//
//        Fin1m4 = new ModelPart( this, 15, 50 );
//        Fin1m4.setTexSize( 128, 64 );
//        Fin1m4.addBox( -2F, -1.5F, -0.5F, 4, 1, 1);
//        Fin1m4.setPos( -3.710101F, 24.5F, -4.698463F );
//        setRotation(Fin1m4, 0F, 0F, 0F);
//        Fin1m4.mirror = true;
//        Fin1.addChild(Fin1m4);
//
//        ///////////////////////////////////////////////////////////////////////////////////////////
//
//        leftLeg = new ModelPart( this, 16, 16 );
//        leftLeg.setTexSize( 128, 64 );
//        leftLeg.addBox( -2F, -6F, -2F, 4, 12, 4);
//        leftLeg.setPos( 2F, 18F, 0F );
//
//        Fin2 = new ModelPart( this, 10, 38 );
//        Fin2.setTexSize( 128, 64 );
//        Fin2.addBox( 0F, 22F, -2.5F, 5, 2, 5);
//        Fin2.setPos( -2F, -12F, 0F );
//        setRotation(Fin2, 0F, 0F, 0F);
//        leftLeg.addChild(Fin2);
//
//        Fin2m1 = new ModelPart( this, 13, 47 );
//        Fin2m1.setTexSize( 128, 64 );
//        Fin2m1.addBox( -2.5F, -1.5F, -1F, 5, 1, 2);
//        Fin2m1.setPos( 3.19707F, 24.5F, -3.288924F );
//        setRotation(Fin2m1, 0F, 0F, 0F);
//        Fin2.addChild(Fin2m1);
//
//        Fin2m2 = new ModelPart( this, 15, 45 );
//        Fin2m2.setTexSize( 128, 64 );
//        Fin2m2.addBox( -2F, -1.5F, -0.5F, 4, 1, 1);
//        Fin2m2.setPos( 3.02606F, 23.5F, -2.819078F );
//        setRotation(Fin2m2, 0F, 0F, 0F);
//        Fin2.addChild(Fin2m2);
//
//        Fin2m3 = new ModelPart( this, 1, 52 );
//        Fin2m3.setTexSize( 128, 64 );
//        Fin2m3.addBox( -5F, -1.0F, -6F, 10, 0, 12);
//        Fin2m3.setPos( 5.420201F, 24.5F, -9.396926F );
//        setRotation(Fin2m3, 0F, 0F, 0F);
//        Fin2.addChild(Fin2m3);
//
//        Fin2m4 = new ModelPart( this, 15, 50 );
//        Fin2m4.setTexSize( 128, 64 );
//        Fin2m4.addBox( -2F, -1.5F, -0.5F, 4, 1, 1);
//        Fin2m4.setPos( 3.710101F, 24.5F, -4.698463F );
//        setRotation(Fin2m4, 0F, 0F, 0F);
//        Fin2.addChild(Fin2m4);
//
//        ///////////////////////////////////////////////////////////////////////////////////////////
//
//        BCD = new ModelPart( this, 65, 50 );
//        BCD.setTexSize( 128, 64 );
//        BCD.addBox( -4F, -6F, -1F, 8, 12, 2);
//        BCD.setPos( 0F, 6.5F, 3F );
//
//        BCD12 = new ModelPart( this, 102, 46 );
//        BCD12.setTexSize( 128, 64 );
//        BCD12.addBox( -0.5F, -0.5F, -0.5F, 1, 1, 1);
//        BCD12.setPos( 0F, 10F, -2.7F );
//
//        BCD11 = new ModelPart( this, 79, 42 );
//        BCD11.setTexSize( 128, 64 );
//        BCD11.addBox( -0.5F, -0.5F, -2F, 1, 1, 4);
//        BCD11.setPos( 3.6F, 3F, 0F );
//
//        BCD4 = new ModelPart( this, 97, 50 );
//        BCD4.setTexSize( 128, 64 );
//        BCD4.addBox( -1F, -5.5F, -0.5F, 2, 11, 1);
//        BCD4.setPos( 3F, 5.5F, -2.5F );
//
//        Tank2 = new ModelPart( this, 41, 50 );
//        Tank2.setTexSize( 128, 64 );
//        Tank2.addBox( -2F, -5F, -2F, 4, 10, 4);
//        Tank2.setPos( -3F, 7F, 6.5F );
//
//        Tank2m1 = new ModelPart( this, 45, 54 );
//        Tank2m1.setTexSize( 128, 64 );
//        Tank2m1.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
//        Tank2m1.setPos( -3F, 7F, 8.5F );
//
//        Tank2m2 = new ModelPart( this, 45, 54 );
//        Tank2m2.setTexSize( 128, 64 );
//        Tank2m2.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
//        Tank2m2.setPos( -5F, 7F, 6.5F );
//
//        Tank2m3 = new ModelPart( this, 45, 54 );
//        Tank2m3.setTexSize( 128, 64 );
//        Tank2m3.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
//        Tank2m3.setPos( -1F, 7F, 6.5F );
//
//        Tank2m4 = new ModelPart( this, 43, 46 );
//        Tank2m4.setTexSize( 128, 64 );
//        Tank2m4.addBox( -1.5F, -0.5F, -1.5F, 3, 1, 3);
//        Tank2m4.setPos( -3F, 1.5F, 6.5F );
//
//        Tank2m5 = new ModelPart( this, 38, 49 );
//        Tank2m5.setTexSize( 128, 64 );
//        Tank2m5.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
//        Tank2m5.setPos( -3F, -0.5F, 6.5F );
//
//        Tank2m6 = new ModelPart( this, 44, 44 );
//        Tank2m6.setTexSize( 128, 64 );
//        Tank2m6.addBox( -2F, -0.5F, -0.5F, 4, 1, 1);
//        Tank2m6.setPos( -3.5F, -0.5F, 6.5F );
//
//        Tank2m7 = new ModelPart( this, 36, 44 );
//        Tank2m7.setTexSize( 128, 64 );
//        Tank2m7.addBox( -1F, -1F, -1F, 2, 2, 2);
//        Tank2m7.setPos( -5.5F, -0.5F, 6.5F );
//
//        BCD2 = new ModelPart( this, 66, 51 );
//        BCD2.setTexSize( 128, 64 );
//        BCD2.addBox( -3.5F, -5F, -0.5F, 7, 10, 1);
//        BCD2.setPos( 0F, 6.5F, 4F );
//
//        Tank1 = new ModelPart( this, 41, 50 );
//        Tank1.setTexSize( 128, 64 );
//        Tank1.addBox( -2F, -5F, -2F, 4, 10, 4);
//        Tank1.setPos( 3F, 7F, 6.5F );
//
//        Tank1m1 = new ModelPart( this, 45, 54 );
//        Tank1m1.setTexSize( 128, 64 );
//        Tank1m1.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
//        Tank1m1.setPos( 3F, 7F, 8.5F );
//
//        Tank1m2 = new ModelPart( this, 45, 54 );
//        Tank1m2.setTexSize( 128, 64 );
//        Tank1m2.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
//        Tank1m2.setPos( 1F, 7F, 6.5F );
//
//        Tank1m3 = new ModelPart( this, 45, 54 );
//        Tank1m3.setTexSize( 128, 64 );
//        Tank1m3.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
//        Tank1m3.setPos( 5F, 7F, 6.5F );
//
//        Tank1m4 = new ModelPart( this, 43, 46 );
//        Tank1m4.setTexSize( 128, 64 );
//        Tank1m4.addBox( -1.5F, -0.5F, -1.5F, 3, 1, 3);
//        Tank1m4.setPos( 3F, 1.5F, 6.5F );
//
//        Tank1m5 = new ModelPart( this, 38, 49 );
//        Tank1m5.setTexSize( 128, 64 );
//        Tank1m5.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
//        Tank1m5.setPos( 3F, -0.5F, 6.5F );
//
//        Tank1m6 = new ModelPart( this, 44, 44 );
//        Tank1m6.setTexSize( 128, 64 );
//        Tank1m6.addBox( -2F, -0.5F, -0.5F, 4, 1, 1);
//        Tank1m6.setPos( 3.5F, -0.5F, 6.5F );
//
//        Tank1m7 = new ModelPart( this, 36, 44 );
//        Tank1m7.setTexSize( 128, 64 );
//        Tank1m7.addBox( -1F, -1F, -1F, 2, 2, 2);
//        Tank1m7.setPos( 5.5F, -0.5F, 6.5F );
//
//        BCD6 = new ModelPart( this, 68, 41 );
//        BCD6.setTexSize( 128, 64 );
//        BCD6.addBox( -0.5F, -1F, -2F, 1, 2, 4);
//        BCD6.setPos( -3.6F, 10F, 0F );
//
//        BCD5 = new ModelPart( this, 68, 41 );
//        BCD5.setTexSize( 128, 64 );
//        BCD5.addBox( -0.5F, -1F, -2F, 1, 2, 4);
//        BCD5.setPos( 3.6F, 10F, 0F );
//
//        BCD3 = new ModelPart( this, 91, 50 );
//        BCD3.setTexSize( 128, 64 );
//        BCD3.addBox( -1F, -5.5F, -0.5F, 2, 11, 1);
//        BCD3.setPos( -3F, 5.5F, -2.5F );
//
//        BCD7 = new ModelPart( this, 91, 45 );
//        BCD7.setTexSize( 128, 64 );
//        BCD7.addBox( -2F, -1F, -0.5F, 4, 2, 1);
//        BCD7.setPos( 0F, 10F, -2.5F );
//
//        BCD8 = new ModelPart( this, 91, 48 );
//        BCD8.setTexSize( 128, 64 );
//        BCD8.addBox( -2F, -0.5F, -0.5F, 4, 1, 1);
//        BCD8.setPos( 0F, 3F, -2.5F );
//
//        BCD9 = new ModelPart( this, 79, 42 );
//        BCD9.setTexSize( 128, 64 );
//        BCD9.addBox( -0.5F, -0.5F, -2F, 1, 1, 4);
//        BCD9.setPos( -3.6F, 3F, 0F );
//
//        BCD13 = new ModelPart( this, 91, 38 );
//        BCD13.setTexSize( 128, 64 );
//        BCD13.addBox( -4F, -0.5F, -0.5F, 8, 1, 1);
//        BCD13.setPos( 0F, 0.5F, 2.5F );
//
//        ///////////////////////////////////////////////////////////////////////////////////////////
//
//        head.addChild(Mask);
//        head.addChild(Mask2);
//        head.addChild(Mask3);
//        head.addChild(Mask4);
//        head.addChild(Mask5);
//        head.addChild(Mask6);
//        head.addChild(Mask7);
//        head.addChild(Mask8);
//        head.addChild(mouthpiece);
//        head.addChild(mouthpiece2);
//        head.addChild(mouthpiece3);
//        head.addChild(hose1);
//        head.addChild(hose2);
//        head.addChild(hose3);
//        head.addChild(hose4);
//        head.addChild(hose5);
//
//        Mask = new ModelPart( this, 109, 60 );
//        Mask.setTexSize( 128, 64 );
//        Mask.addBox( -4F, -0.5F, -0.5F, 8, 1, 1);
//        Mask.setPos( 0F, -6F, -4.5F );
//
//        Mask2 = new ModelPart( this, 120, 55 );
//        Mask2.setTexSize( 128, 64 );
//        Mask2.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
//        Mask2.setPos( -4F, -4F, -4.5F );
//
//        Mask3 = new ModelPart( this, 116, 55 );
//        Mask3.setTexSize( 128, 64 );
//        Mask3.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
//        Mask3.setPos( 4F, -4F, -4.5F );
//
//        Mask4 = new ModelPart( this, 114, 51 );
//        Mask4.setTexSize( 128, 64 );
//        Mask4.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
//        Mask4.setPos( -2.5F, -2F, -4.5F );
//
//        Mask5 = new ModelPart( this, 114, 53 );
//        Mask5.setTexSize( 128, 64 );
//        Mask5.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
//        Mask5.setPos( 2.5F, -2F, -4.5F );
//
//        Mask6 = new ModelPart( this, 114, 49 );
//        Mask6.setTexSize( 128, 64 );
//        Mask6.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
//        Mask6.setPos( 0F, -3F, -4.5F );
//
//        Mask7 = new ModelPart( this, 110, 38 );
//        Mask7.setTexSize( 128, 64 );
//        Mask7.addBox( -0.5F, -1F, -4F, 1, 2, 8);
//        Mask7.setPos( 4F, -4.5F, 0F );
//
//        Mask8 = new ModelPart( this, 110, 38 );
//        Mask8.setTexSize( 128, 64 );
//        Mask8.addBox( -0.5F, -1F, -4F, 1, 2, 8);
//        Mask8.setPos( -4F, -4.5F, 0F );
//
//        Mask9 = new ModelPart( this, 110, 35 );
//        Mask9.setTexSize( 128, 64 );
//        Mask9.addBox( -4F, -1F, -0.5F, 8, 2, 1);
//        Mask9.setPos( 0F, -4.5F, 4F );
//
//        mouthpiece = new ModelPart( this, 115, 28 );
//        mouthpiece.setTexSize( 128, 64 );
//        mouthpiece.addBox( -1.5F, -1.5F, -0.5F, 3, 3, 1);
//        mouthpiece.setPos( 0F, 0F, -5F );
//
//        mouthpiece2 = new ModelPart( this, 116, 25 );
//        mouthpiece2.setTexSize( 128, 64 );
//        mouthpiece2.addBox( -1F, -1F, -0.5F, 2, 2, 1);
//        mouthpiece2.setPos( 0F, 0F, -5.5F );
//
//        mouthpiece3 = new ModelPart( this, 116, 23 );
//        mouthpiece3.setTexSize( 128, 64 );
//        mouthpiece3.addBox( -1F, -0.5F, -0.5F, 2, 1, 1);
//        mouthpiece3.setPos( 0F, -0.6000004F, -4F );
//
//        head = new ModelPart( this, 0, 0 );
//        head.setTexSize( 128, 64 );
//        head.addBox( -4F, -4F, -4F, 8, 8, 8);
//        head.setPos( 0F, -4F, 0F );
//        head.mirror = true;
//        setRotation(head, 0F, 0F, 0F);
//
//        hose1 = new ModelPart( this, 117, 16 );
//        hose1.setTexSize( 128, 64 );
//        hose1.addBox( -0.5F, -0.5F, -0.5F, 1, 1, 1);
//        hose1.setPos( 3F, -3F, 6.5F );
//
//        hose2 = new ModelPart( this, 117, 16 );
//        hose2.setTexSize( 128, 64 );
//        hose2.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
//        hose2.setPos( 5F, -3F, 6.5F );
//
//        hose3 = new ModelPart( this, 116, 15 );
//        hose3.setTexSize( 128, 64 );
//        hose3.addBox( -0.5F, -0.5F, -1F, 1, 1, 2);
//        hose3.setPos( 6F, -3F, 5F );
//
//        hose4 = new ModelPart( this, 106, 7 );
//        hose4.setTexSize( 128, 64 );
//        hose4.addBox( -0.5F, -0.5F, -10F, 1, 1, 10);
//        hose4.setPos( 6F, -3F, 4.2F );
//
//        hose5 = new ModelPart( this, 115, 16 );
//        hose5.setTexSize( 128, 64 );
//        hose5.addBox( -2.5F, -0.5F, -0.5F, 5, 1, 1);
//        hose5.setPos( 4F, 0F, -5F );
//
//        hose6 = new ModelPart( this, 115, 16 );
//        hose6.setTexSize( 128, 64 );
//        hose6.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
//        hose6.setPos( 0F, -0.5F, 6.5F );
    }

    public static LayerDefinition create() {
        MeshDefinition modelData = createMesh(CubeDeformation.NONE,0);
        PartDefinition modelPartData = modelData.getRoot();

        modelPartData.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(32, 16)
                        .addBox(-4F, -6F, -2F, 8, 12, 4),
                PartPose.offset(0F, 6F, 0F));

        modelPartData.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                        .texOffs(56, 16)
                        .addBox(-4F, 0F, -2F, 4, 12, 4),
                PartPose.offset(-4F, 0F, 0F));

        modelPartData.addOrReplaceChild("left_arm",
                CubeListBuilder.create()
                        .texOffs(72, 16)
                        .addBox(0F, 0F, -2F, 4, 12, 4),
                PartPose.offset(4F, 0F, 0F));

        ///////////////////////////////////////////////////////////////////////////////////////////

        PartDefinition modelPartLegRight = modelPartData.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-2F, -6F, -2F, 4, 12, 4),
                PartPose.offset(-2F, 18F, 0F));

        PartDefinition modelPartFinRight = modelPartLegRight.addOrReplaceChild("Fin1",
                CubeListBuilder.create().mirror()
                        .texOffs(10, 38)
                        .addBox(-5F, 22F, -2.5F, 5, 2, 5),
                PartPose.offset(2.5F, -12F, 0F));

        modelPartFinRight.addOrReplaceChild("Fin1m1",
                CubeListBuilder.create().mirror()
                        .texOffs(13, 47)
                        .addBox(-2.5F, -1.5F, -1F, 5, 1, 2),
                PartPose.offset(-3.19707F, 24.5F, -3.288924F));

        modelPartFinRight.addOrReplaceChild("Fin1m2",
                CubeListBuilder.create().mirror()
                        .texOffs(15, 45)
                        .addBox(-2F, -1.5F, -0.5F, 4, 1, 1),
                PartPose.offset(-3.02606F, 23.5F, -2.819078F));

        modelPartFinRight.addOrReplaceChild("Fin1m3",
                CubeListBuilder.create().mirror()
                        .texOffs(1, 52)
                        .addBox(-5F, -1F, -6F, 10, 0, 12),
                PartPose.offset(-5.420201F, 24.5F, -9.396926F));

        modelPartFinRight.addOrReplaceChild("Fin1m4",
                CubeListBuilder.create().mirror()
                        .texOffs(15, 50)
                        .addBox(-2F, -1.5F, -0.5F, 4, 1, 1),
                PartPose.offset(-3.710101F, 24.5F, -4.698463F));

        ///////////////////////////////////////////////////////////////////////////////////////////

        PartDefinition modelPartLegLeft = modelPartData.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(16, 16)
                        .addBox(-2F, -6F, -2F, 4, 12, 4),
                PartPose.offset(2F, 18F, 0F));

        PartDefinition modelPartFinleft = modelPartLegLeft.addOrReplaceChild("Fin2",
                CubeListBuilder.create().mirror()
                        .texOffs(10, 38)
                        .addBox(0F, 22F, -2.5F, 5, 2, 5),
                PartPose.offset(-2F, -12F, 0F));

        modelPartFinleft.addOrReplaceChild("Fin2m1",
                CubeListBuilder.create().mirror()
                        .texOffs(13, 47)
                        .addBox(-2.5F, -1.5F, -1F, 5, 1, 2),
                PartPose.offset(3.19707F, 24.5F, -3.288924F));

        modelPartFinleft.addOrReplaceChild("Fin2m2",
                CubeListBuilder.create().mirror()
                        .texOffs(15, 45)
                        .addBox(-2F, -1.5F, -0.5F, 4, 1, 1),
                PartPose.offset(3.02606F, 23.5F, -2.819078F));

        modelPartFinleft.addOrReplaceChild("Fin2m3",
                CubeListBuilder.create().mirror()
                        .texOffs(1, 52)
                        .addBox(-5F, -1F, -6F, 10, 0, 12),
                PartPose.offset(5.420201F, 24.5F, -9.396926F));

        modelPartFinleft.addOrReplaceChild("Fin2m4",
                CubeListBuilder.create().mirror()
                        .texOffs(15, 50)
                        .addBox(-2F, -1.5F, -0.5F, 4, 1, 1),
                PartPose.offset(3.710101F, 24.5F, -4.698463F));

        ///////////////////////////////////////////////////////////////////////////////////////////

        modelPartData.addOrReplaceChild("BCD",
                CubeListBuilder.create()
                        .texOffs(65, 50)
                        .addBox(-4F, -6F, -1F, 8, 12, 2),
                PartPose.offset(0F, 6.5F, 3F));

        modelPartData.addOrReplaceChild("BCD12",
                CubeListBuilder.create()
                        .texOffs(102, 46)
                        .addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
                PartPose.offset(0F, 10F, -2.7F));

        modelPartData.addOrReplaceChild("BCD11",
                CubeListBuilder.create()
                        .texOffs(79, 42)
                        .addBox(-0.5F, -0.5F, -2F, 1, 1, 4),
                PartPose.offset(3.6F, 3F, 0F));

        modelPartData.addOrReplaceChild("BCD4",
                CubeListBuilder.create()
                        .texOffs(97, 50)
                        .addBox(-1F, -5.5F, -0.5F, 2, 11, 1),
                PartPose.offset(3F, 5.5F, -2.5F));

        modelPartData.addOrReplaceChild("Tank2",
                CubeListBuilder.create()
                        .texOffs(41, 50)
                        .addBox(-2F, -5F, -2F, 4, 10, 4),
                PartPose.offset(-3F, 7F, 6.5F));

        modelPartData.addOrReplaceChild("Tank2m1",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5F, -4.5F, -0.5F, 3, 9, 1),
                PartPose.offset(-3F, 7F, 8.5F));

        modelPartData.addOrReplaceChild("Tank2m2",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5F, -4.5F, -0.5F, 3, 9, 1),
                PartPose.offset(-5F, 7F, 6.5F));

        modelPartData.addOrReplaceChild("Tank2m3",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5F, -4.5F, -0.5F, 3, 9, 1),
                PartPose.offset(-1F, 7F, 6.5F));

        modelPartData.addOrReplaceChild("Tank2m4",
                CubeListBuilder.create()
                        .texOffs(43, 46)
                        .addBox(-1.5F, -0.5F, -1.5F, 3, 1, 3),
                PartPose.offset(-3F, 1.5F, 6.5F));

        modelPartData.addOrReplaceChild("Tank2m5",
                CubeListBuilder.create()
                        .texOffs(38, 49)
                        .addBox(-0.5F, -2F, -0.5F, 1, 4, 1),
                PartPose.offset(-3F, -0.5F, 6.5F));

        modelPartData.addOrReplaceChild("Tank2m6",
                CubeListBuilder.create()
                        .texOffs(44, 44)
                        .addBox(-2F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offset(-3.5F, -0.5F, 6.5F));

        modelPartData.addOrReplaceChild("Tank2m7",
                CubeListBuilder.create()
                        .texOffs(36, 44)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offset(-5.5F, -0.5F, 6.5F));

        modelPartData.addOrReplaceChild("BCD2",
                CubeListBuilder.create()
                        .texOffs(66, 51)
                        .addBox(-3.5F, -5F, -0.5F, 7, 10, 1),
                PartPose.offset(0F, 6.5F, 4F));

        modelPartData.addOrReplaceChild("Tank1",
                CubeListBuilder.create()
                        .texOffs(41, 50)
                        .addBox(-2F, -5F, -2F, 4, 10, 4),
                PartPose.offset(3F, 7F, 6.5F));

        modelPartData.addOrReplaceChild("Tank1m1",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5F, -4.5F, -0.5F, 3, 9, 1),
                PartPose.offset(3F, 7F, 8.5F));

        modelPartData.addOrReplaceChild("Tank1m2",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5F, -4.5F, -0.5F, 3, 9, 1),
                PartPose.offset(1F, 7F, 6.5F));

        modelPartData.addOrReplaceChild("Tank1m3",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5F, -4.5F, -0.5F, 3, 9, 1),
                PartPose.offset(5F, 7F, 6.5F));

        modelPartData.addOrReplaceChild("Tank1m4",
                CubeListBuilder.create()
                        .texOffs(43, 46)
                        .addBox(-1.5F, -0.5F, -1.5F, 3, 1, 3),
                PartPose.offset(3F, 1.5F, 6.5F));

        modelPartData.addOrReplaceChild("Tank1m5",
                CubeListBuilder.create()
                        .texOffs(38, 49)
                        .addBox(-0.5F, -2F, -0.5F, 1, 4, 1),
                PartPose.offset(3F, -0.5F, 6.5F));

        modelPartData.addOrReplaceChild("Tank1m6",
                CubeListBuilder.create()
                        .texOffs(44, 44)
                        .addBox(-2F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offset(3.5F, -0.5F, 6.5F));

        modelPartData.addOrReplaceChild("Tank1m7",
                CubeListBuilder.create()
                        .texOffs(36, 44)
                        .addBox(-1F, -1F, -1F, 2, 2, 2),
                PartPose.offset(5.5F, -0.5F, 6.5F));

        modelPartData.addOrReplaceChild("BCD6",
                CubeListBuilder.create()
                        .texOffs(68, 41)
                        .addBox(-0.5F, -1F, -2F, 1, 2, 4),
                PartPose.offset(-3.6F, 10F, 0F));

        modelPartData.addOrReplaceChild("BCD5",
                CubeListBuilder.create()
                        .texOffs(68, 41)
                        .addBox(-0.5F, -1F, -2F, 1, 2, 4),
                PartPose.offset(3.6F, 10F, 0F));

        modelPartData.addOrReplaceChild("BCD3",
                CubeListBuilder.create()
                        .texOffs(91, 50)
                        .addBox(-1F, -5.5F, -0.5F, 2, 11, 1),
                PartPose.offset(-3F, 5.5F, -2.5F));

        modelPartData.addOrReplaceChild("BCD7",
                CubeListBuilder.create()
                        .texOffs(91, 45)
                        .addBox(-2F, -1F, -0.5F, 4, 2, 1),
                PartPose.offset(0F, 10F, -2.5F));

        modelPartData.addOrReplaceChild("BCD8",
                CubeListBuilder.create()
                        .texOffs(91, 48)
                        .addBox(-2F, -0.5F, -0.5F, 4, 1, 1),
                PartPose.offset(0F, 3F, -2.5F));

        modelPartData.addOrReplaceChild("BCD9",
                CubeListBuilder.create()
                        .texOffs(79, 42)
                        .addBox(-0.5F, -0.5F, -2F, 1, 1, 4),
                PartPose.offset(-3.6F, 3F, 0F));

        modelPartData.addOrReplaceChild("BCD13",
                CubeListBuilder.create()
                        .texOffs(91, 38)
                        .addBox(-4F, -0.5F, -0.5F, 8, 1, 1),
                PartPose.offset(0F, 0.5F, 2.5F));

        ///////////////////////////////////////////////////////////////////////////////////////////

        PartDefinition modelPartHead = modelPartData.addOrReplaceChild("head",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 0)
                        .addBox(-4F, -4F, -4F, 8, 8, 8),
                PartPose.offsetAndRotation(0F, -4F, 0F, 0F, 0F, 0F));

        modelPartHead.addOrReplaceChild("Mask",
                CubeListBuilder.create()
                        .texOffs(109, 60)
                        .addBox(-4F, -0.5F, -0.5F, 8, 1, 1),
                PartPose.offset(0F, -6F, -4.5F));

        modelPartHead.addOrReplaceChild("Mask2",
                CubeListBuilder.create()
                        .texOffs(120, 55)
                        .addBox(-0.5F, -2F, -0.5F, 1, 4, 1),
                PartPose.offset(-4F, -4F, -4.5F));

        modelPartHead.addOrReplaceChild("Mask3",
                CubeListBuilder.create()
                        .texOffs(116, 55)
                        .addBox(-0.5F, -2F, -0.5F, 1, 4, 1),
                PartPose.offset(4F, -4F, -4.5F));

        modelPartHead.addOrReplaceChild("Mask4",
                CubeListBuilder.create()
                        .texOffs(114, 51)
                        .addBox(-1.5F, -0.5F, -0.5F, 3, 1, 1),
                PartPose.offset(-2.5F, -2F, -4.5F));

        modelPartHead.addOrReplaceChild("Mask5",
                CubeListBuilder.create()
                        .texOffs(114, 53)
                        .addBox(-1.5F, -0.5F, -0.5F, 3, 1, 1),
                PartPose.offset(2.5F, -2F, -4.5F));

        modelPartHead.addOrReplaceChild("Mask6",
                CubeListBuilder.create()
                        .texOffs(114, 49)
                        .addBox(-1.5F, -0.5F, -0.5F, 3, 1, 1),
                PartPose.offset(0F, -3F, -4.5F));

        modelPartHead.addOrReplaceChild("Mask7",
                CubeListBuilder.create()
                        .texOffs(110, 38)
                        .addBox(-0.5F, -1F, -4F, 1, 2, 8),
                PartPose.offset(4F, -4.5F, 0F));

        modelPartHead.addOrReplaceChild("Mask8",
                CubeListBuilder.create()
                        .texOffs(110, 38)
                        .addBox(-0.5F, -1F, -4F, 1, 2, 8),
                PartPose.offset(-4F, -4.5F, 0F));

        modelPartData.addOrReplaceChild("Mask9",
                CubeListBuilder.create()
                        .texOffs(110, 35)
                        .addBox(-4F, -1F, -0.5F, 8, 2, 1),
                PartPose.offset(0F, -4.5F, 4F));

        modelPartHead.addOrReplaceChild("mouthpiece",
                CubeListBuilder.create()
                        .texOffs(115, 28)
                        .addBox(-1.5F, -1.5F, -0.5F, 3, 3, 1),
                PartPose.offset(0F, 0F, -5F));

        modelPartHead.addOrReplaceChild("mouthpiece2",
                CubeListBuilder.create()
                        .texOffs(116, 25)
                        .addBox(-1F, -1F, -0.5F, 2, 2, 1),
                PartPose.offset(0F, 0F, -5.5F));

        modelPartHead.addOrReplaceChild("mouthpiece3",
                CubeListBuilder.create()
                        .texOffs(116, 23)
                        .addBox(-1F, -0.5F, -0.5F, 2, 1, 1),
                PartPose.offset(0F, -0.6000004F, -4F));

        modelPartHead.addOrReplaceChild("hose1",
                CubeListBuilder.create()
                        .texOffs(117, 16)
                        .addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1),
                PartPose.offset(3F, -3F, 6.5F));

        modelPartHead.addOrReplaceChild("hose2",
                CubeListBuilder.create()
                        .texOffs(117, 16)
                        .addBox(-1.5F, -0.5F, -0.5F, 3, 1, 1),
                PartPose.offset(5F, -3F, 6.5F));

        modelPartHead.addOrReplaceChild("hose3",
                CubeListBuilder.create()
                        .texOffs(116, 15)
                        .addBox(-0.5F, -0.5F, -1F, 1, 1, 2),
                PartPose.offset(6F, -3F, 5F));

        modelPartHead.addOrReplaceChild("hose4",
                CubeListBuilder.create()
                        .texOffs(106, 7)
                        .addBox(-0.5F, -0.5F, -10F, 1, 1, 10),
                PartPose.offset(6F, -3F, 4.2F));

        modelPartHead.addOrReplaceChild("hose5",
                CubeListBuilder.create()
                        .texOffs(115, 16)
                        .addBox(-2.5F, -0.5F, -0.5F, 5, 1, 1),
                PartPose.offset(4F, 0F, -5F));

        modelPartData.addOrReplaceChild("hose6",
                CubeListBuilder.create()
                        .texOffs(115, 16)
                        .addBox(-1.5F, -0.5F, -0.5F, 3, 1, 1),
                PartPose.offset(0F, -0.5F, 6.5F));

        return LayerDefinition.create(modelData, 128, 64);
    }

    public static ModelScubaGear createModel(ModelLayerLocation entityModelLayer, EntityModelSet entityModelLoader, final EquipmentSlot slot) {
        return new ModelScubaGear(entityModelLoader == null ?
                create().bakeRoot() :
                entityModelLoader.bakeLayer(entityModelLayer), slot);
    }

    @Override
    public void prepareMobModel(LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
        showHead = !entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && this.slot == EquipmentSlot.HEAD;
        showChest = !entity.getItemBySlot(EquipmentSlot.CHEST).isEmpty() && this.slot == EquipmentSlot.CHEST;
        showLegs = !entity.getItemBySlot(EquipmentSlot.FEET).isEmpty() && this.slot == EquipmentSlot.FEET;
        isSneaking = entity.getPose() == Pose.CROUCHING;
    }
    
    private void renderArmor(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel<?> modelIn, float red, float green, float blue, ResourceLocation armorResource) {
        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, modelIn.renderType(armorResource), false, glintIn);
        modelIn.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
     }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        stack.pushPose();

        if (young) {
            stack.scale(0.75F, 0.75F, 0.75F);
            stack.translate(0.0F, 16.0F * 1, 0.0F);
            head.render(stack, bufferIn, packedLightIn, packedOverlayIn);
            stack.popPose();
            stack.pushPose();
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.translate(0.0F, 24.0F * 1, 0.0F);
            renderScubaGear(stack, bufferIn, packedLightIn, packedOverlayIn, false);
        } else {
            if (isSneaking) {
                stack.translate(0.0F, 0.2F, 0.0F);
            }

            renderScubaGear(stack, bufferIn, packedLightIn, packedOverlayIn, true);
        }

        stack.popPose();
    }

    public void renderScubaGear(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, boolean renderHead) {
        hose4.xRot = 0.3075211F;

        body.visible = showChest;
        head.visible = showHead;
        mouthpiece.visible = showChest;
        mouthpiece2.visible = showChest;
        mouthpiece3.visible = showChest;
        hose1.visible = showChest;
        hose2.visible = showChest;
        hose3.visible = showChest;
        hose4.visible = showChest;
        hose5.visible = showChest;
        hose6.visible = showChest;

        // TODO what was this doing?
//        if (showHead) {
//            hose6.rotateAngleX = 0F;
//            hose6.rotateAngleY = 0F;
//            hose6.rotateAngleZ = 0F;
//            hose6.render(stack, bufferIn, packedLightIn, packedOverlayIn);
//        }

        if (showChest) {
            renderTank(stack, bufferIn, packedLightIn, packedOverlayIn);
            renderBCD(stack, bufferIn, packedLightIn, packedOverlayIn);
        }
        
        rightLeg.visible = showLegs;
        leftLeg.visible = showLegs;

        if (renderHead) {
            head.render(stack, bufferIn, packedLightIn, packedOverlayIn);
        }

        body.render(stack, bufferIn, packedLightIn, packedOverlayIn);
        rightArm.render(stack, bufferIn, packedLightIn, packedOverlayIn);
        leftArm.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        //TODO [Port]: Not possible now within minecraft 1.17?
        //rightLeg.mirror = true;
        
//        bipedLeftLeg.rotationPointY = 0;
       //TODO bipedLeftLeg.offsetY = 0.763f;

        
        if(showLegs) {
            // TODO is this necessary?
//        if(entityIn.isInWater()) {
//            this.Fin2m3.offsetX = -0.2f;
//            this.Fin1m3.offsetX = 0.2f;
//    
//    
//           this.Fin1m3.offsetZ = 0.1f;
//           this.Fin2m3.offsetZ = 0.1f;
//            
//            
//            
//            bipedLeftLeg.render(scale);
//            bipedRightLeg.render(scale);
            
         
            
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

       
        
            
//        }else {
          //TODO  this.Fin2m3.offsetX = 0f;
                
           //TODO  this.Fin1m3.offsetX = 0f;
     
           //TODO  this.Fin2m3.offsetY = 0f;
     
          //TODO   this.Fin1m3.offsetY = 0f;
             
             this.Fin2m3.xRot = 0f;
     
             this.Fin1m3.xRot = 0f;
             
            leftLeg.render(stack, bufferIn, packedLightIn, packedOverlayIn);

             rightLeg.render(stack, bufferIn, packedLightIn, packedOverlayIn);
         
//        }

        }
        
         
    }

    private void renderTank(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
        Tank2.xRot = 0F;
        Tank2.yRot = 0F;
        Tank2.zRot = 0F;
        Tank2.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank2m1.xRot = 0F;
        Tank2m1.yRot = 0F;
        Tank2m1.zRot = 0F;
        Tank2m1.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank2m2.xRot = 0F;
        Tank2m2.yRot = -1.570796F;
        Tank2m2.zRot = 0F;
        Tank2m2.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank2m3.xRot = 0F;
        Tank2m3.yRot = -1.570796F;
        Tank2m3.zRot = 0F;
        Tank2m3.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank2m4.xRot = 0F;
        Tank2m4.yRot = 0F;
        Tank2m4.zRot = 0F;
        Tank2m4.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank2m5.xRot = 0F;
        Tank2m5.yRot = 0F;
        Tank2m5.zRot = 0F;
        Tank2m5.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank2m6.xRot = 0F;
        Tank2m6.yRot = 0F;
        Tank2m6.zRot = 0F;
        Tank2m6.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank2m7.xRot = 0F;
        Tank2m7.yRot = 0F;
        Tank2m7.zRot = 0F;
        Tank2m7.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1.xRot = 0F;
        Tank1.yRot = 0F;
        Tank1.zRot = 0F;
        Tank1.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1m1.xRot = 0F;
        Tank1m1.yRot = 0F;
        Tank1m1.zRot = 0F;
        Tank1m1.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1m2.xRot = 0F;
        Tank1m2.yRot = -1.570796F;
        Tank1m2.zRot = 0F;
        Tank1m2.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1m3.xRot = 0F;
        Tank1m3.yRot = -1.570796F;
        Tank1m3.zRot = 0F;
        Tank1m3.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1m4.xRot = 0F;
        Tank1m4.yRot = 0F;
        Tank1m4.zRot = 0F;
        Tank1m4.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1m5.xRot = 0F;
        Tank1m5.yRot = 0F;
        Tank1m5.zRot = 0F;
        Tank1m5.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1m6.xRot = 0F;
        Tank1m6.yRot = 0F;
        Tank1m6.zRot = 0F;
        Tank1m6.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1m7.xRot = 0F;
        Tank1m7.yRot = 0F;
        Tank1m7.zRot = 0F;
        Tank1m7.render(stack, bufferIn, packedLightIn, packedOverlayIn);
    }

    private void renderBCD(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
        BCD.xRot = 0F;
        BCD.yRot = 0F;
        BCD.zRot = 0F;
        BCD.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD12.xRot = 0F;
        BCD12.yRot = 0F;
        BCD12.zRot = 0F;
        BCD12.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD11.xRot = 0F;
        BCD11.yRot = 0F;
        BCD11.zRot = 0F;
        BCD11.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD4.xRot = 0F;
        BCD4.yRot = 0F;
        BCD4.zRot = 0F;
        BCD4.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD2.xRot = 0F;
        BCD2.yRot = 0F;
        BCD2.zRot = 0F;
        BCD2.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD6.xRot = 0F;
        BCD6.yRot = 0F;
        BCD6.zRot = 0F;
        BCD6.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD5.xRot = 0F;
        BCD5.yRot = 0F;
        BCD5.zRot = 0F;
        BCD5.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD3.xRot = 0F;
        BCD3.yRot = 0F;
        BCD3.zRot = 0F;
        BCD3.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD7.xRot = 0F;
        BCD7.yRot = 0F;
        BCD7.zRot = 0F;
        BCD7.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD8.xRot = 0F;
        BCD8.yRot = 0F;
        BCD8.zRot = 0F;
        BCD8.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD9.xRot = 0F;
        BCD9.yRot = 0F;
        BCD9.zRot = 0F;
        BCD9.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD13.xRot = 0F;
        BCD13.yRot = 0F;
        BCD13.zRot = 0F;
        BCD13.render(stack, bufferIn, packedLightIn, packedOverlayIn);
    }

    /**
     * Copies the angles from one object to another. This is used when objects should stay aligned with each other, like
     * the hair over a players head.
     */
    public static void copyModelRotations(ModelPart source, ModelPart dest)
    {
        dest.xRot = source.xRot;
        dest.yRot = source.yRot;
        dest.zRot = source.zRot;
    }

    private void setRotation(ModelPart model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}