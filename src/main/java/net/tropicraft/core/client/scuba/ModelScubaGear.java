package net.tropicraft.core.client.scuba;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;

import javax.annotation.Nullable;

public class ModelScubaGear extends HumanoidModel<LivingEntity> {

    @Nullable
    public static ModelScubaGear HEAD; //= ModelScubaGear.createModel(TropicraftRenderLayers.HEAD_SCUBA_LAYER, null, EquipmentSlot.CHEST);
    @Nullable
    public static ModelScubaGear CHEST; //= ModelScubaGear.createModel(TropicraftRenderLayers.CHEST_SCUBA_LAYER, null, EquipmentSlot.CHEST);
    @Nullable
    public static ModelScubaGear FEET; //= ModelScubaGear.createModel(TropicraftRenderLayers.FEET_SCUBA_LAYER, null, EquipmentSlot.CHEST);

    @Nullable
    public static ModelScubaGear tankModel; //= ModelScubaGear.createModel(TropicraftRenderLayers.TANK_SCUBA_LAYER, null, EquipmentSlot.CHEST); // Can't reuse the main one with a different scale

    private boolean showHead;
    public boolean showChest;
    private boolean showLegs;
    private boolean isSneaking;

    final ModelPart Fin1;
    final ModelPart Fin1m1;
    final ModelPart Fin1m2;
    final ModelPart Fin1m3;
    final ModelPart Fin1m4;
    final ModelPart Fin2;
    final ModelPart Fin2m1;
    final ModelPart Fin2m2;
    final ModelPart Fin2m3;
    final ModelPart Fin2m4;
    final ModelPart BCD;
    final ModelPart BCD12;
    final ModelPart BCD11;
    final ModelPart BCD4;
    final ModelPart Tank2;
    final ModelPart Tank2m1;
    final ModelPart Tank2m2;
    final ModelPart Tank2m3;
    final ModelPart Tank2m4;
    final ModelPart Tank2m5;
    final ModelPart Tank2m6;
    final ModelPart Tank2m7;
    final ModelPart BCD2;
    final ModelPart Tank1;
    final ModelPart Tank1m1;
    final ModelPart Tank1m2;
    final ModelPart Tank1m3;
    final ModelPart Tank1m4;
    final ModelPart Tank1m5;
    final ModelPart Tank1m6;
    final ModelPart Tank1m7;
    final ModelPart BCD6;
    final ModelPart BCD5;
    final ModelPart BCD3;
    final ModelPart BCD7;
    final ModelPart BCD8;
    final ModelPart BCD9;
    final ModelPart BCD13;
    final ModelPart Mask;
    final ModelPart Mask2;
    final ModelPart Mask3;
    final ModelPart Mask4;
    final ModelPart Mask5;
    final ModelPart Mask6;
    final ModelPart Mask7;
    final ModelPart Mask8;
    final ModelPart Mask9;
    final ModelPart mouthpiece;
    final ModelPart mouthpiece2;
    final ModelPart mouthpiece3;
    final ModelPart hose1;
    final ModelPart hose2;
    final ModelPart hose3;
    final ModelPart hose4;
    final ModelPart hose5;
    final ModelPart hose6;

    public final EquipmentSlot slot;

//    public ModelScubaGear() {
//        this( 0.0f, null );
//    }

    public ModelScubaGear(ModelPart root, EquipmentSlot slot) {
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
//        body.addBox( -4.0f, -6.0f, -2.0f, 8, 12, 4);
//        body.setPos( 0.0f, 6.0f, 0.0f );
//
//        rightArm = new ModelPart( this, 56, 16 );
//        rightArm.setTexSize( 128, 64 );
//        rightArm.addBox( -4.0f, 0.0f, -2.0f, 4, 12, 4);
//        rightArm.setPos( -4.0f, 0.0f, 0.0f );
//
//        leftArm = new ModelPart( this, 72, 16 );
//        leftArm.setTexSize( 128, 64 );
//        leftArm.addBox( 0.0f, 0.0f, -2.0f, 4, 12, 4);
//        leftArm.setPos( 4.0f, 0.0f, 0.0f );
//
//        ///////////////////////////////////////////////////////////////////////////////////////////
//
//        rightLeg = new ModelPart( this, 0, 16 );
//        rightLeg.setTexSize( 128, 64 );
//        rightLeg.addBox( -2.0f, -6.0f, -2.0f, 4, 12, 4);
//        rightLeg.setPos( -2.0f, 18.0f, 0.0f );
//
//        Fin1 = new ModelPart( this, 10, 38 );
//        Fin1.setTexSize( 128, 64 );
//        Fin1.addBox( -5.0f, 22.0f, -2.5f, 5, 2, 5);
//        Fin1.setPos( 2.5f, -12.0f, 0.0f );
//        setRotation(Fin1, 0.0f, 0.0f, 0.0f);
//        Fin1.mirror = true;
//        rightLeg.addChild(Fin1);
//
//        Fin1m1 = new ModelPart( this, 13, 47 );
//        Fin1m1.setTexSize( 128, 64 );
//        Fin1m1.addBox( -2.5f, -1.5f, -1.0f, 5, 1, 2);
//        Fin1m1.setPos( -3.19707f, 24.5f, -3.288924f );
//        setRotation(Fin1m1, 0.0f, 0.0f, 0.0f);
//        Fin1m1.mirror = true;
//        Fin1.addChild(Fin1m1);
//
//        Fin1m2 = new ModelPart( this, 15, 45 );
//        Fin1m2.setTexSize( 128, 64 );
//        Fin1m2.addBox( -2.0f, -1.5f, -0.5f, 4, 1, 1);
//        Fin1m2.setPos( -3.02606f, 23.5f, -2.819078f );
//        setRotation(Fin1m2, 0.0f, 0.0f, 0.0f);
//        Fin1m2.mirror = true;
//        Fin1.addChild(Fin1m2);
//
//        Fin1m3 = new ModelPart( this, 1, 52 );
//        Fin1m3.setTexSize( 128, 64 );
//        Fin1m3.addBox( -5.0f, -1.0f, -6.0f, 10, 0, 12);
//        Fin1m3.setPos( -5.420201f, 24.5f, -9.396926f );
//        setRotation(Fin1m3, 0.0f, 0.0f, 0.0f);
//        Fin1m3.mirror = true;
//        Fin1.addChild(Fin1m3);
//
//        Fin1m4 = new ModelPart( this, 15, 50 );
//        Fin1m4.setTexSize( 128, 64 );
//        Fin1m4.addBox( -2.0f, -1.5f, -0.5f, 4, 1, 1);
//        Fin1m4.setPos( -3.710101f, 24.5f, -4.698463f );
//        setRotation(Fin1m4, 0.0f, 0.0f, 0.0f);
//        Fin1m4.mirror = true;
//        Fin1.addChild(Fin1m4);
//
//        ///////////////////////////////////////////////////////////////////////////////////////////
//
//        leftLeg = new ModelPart( this, 16, 16 );
//        leftLeg.setTexSize( 128, 64 );
//        leftLeg.addBox( -2.0f, -6.0f, -2.0f, 4, 12, 4);
//        leftLeg.setPos( 2.0f, 18.0f, 0.0f );
//
//        Fin2 = new ModelPart( this, 10, 38 );
//        Fin2.setTexSize( 128, 64 );
//        Fin2.addBox( 0.0f, 22.0f, -2.5f, 5, 2, 5);
//        Fin2.setPos( -2.0f, -12.0f, 0.0f );
//        setRotation(Fin2, 0.0f, 0.0f, 0.0f);
//        leftLeg.addChild(Fin2);
//
//        Fin2m1 = new ModelPart( this, 13, 47 );
//        Fin2m1.setTexSize( 128, 64 );
//        Fin2m1.addBox( -2.5f, -1.5f, -1.0f, 5, 1, 2);
//        Fin2m1.setPos( 3.19707f, 24.5f, -3.288924f );
//        setRotation(Fin2m1, 0.0f, 0.0f, 0.0f);
//        Fin2.addChild(Fin2m1);
//
//        Fin2m2 = new ModelPart( this, 15, 45 );
//        Fin2m2.setTexSize( 128, 64 );
//        Fin2m2.addBox( -2.0f, -1.5f, -0.5f, 4, 1, 1);
//        Fin2m2.setPos( 3.02606f, 23.5f, -2.819078f );
//        setRotation(Fin2m2, 0.0f, 0.0f, 0.0f);
//        Fin2.addChild(Fin2m2);
//
//        Fin2m3 = new ModelPart( this, 1, 52 );
//        Fin2m3.setTexSize( 128, 64 );
//        Fin2m3.addBox( -5.0f, -1.0f, -6.0f, 10, 0, 12);
//        Fin2m3.setPos( 5.420201f, 24.5f, -9.396926f );
//        setRotation(Fin2m3, 0.0f, 0.0f, 0.0f);
//        Fin2.addChild(Fin2m3);
//
//        Fin2m4 = new ModelPart( this, 15, 50 );
//        Fin2m4.setTexSize( 128, 64 );
//        Fin2m4.addBox( -2.0f, -1.5f, -0.5f, 4, 1, 1);
//        Fin2m4.setPos( 3.710101f, 24.5f, -4.698463f );
//        setRotation(Fin2m4, 0.0f, 0.0f, 0.0f);
//        Fin2.addChild(Fin2m4);
//
//        ///////////////////////////////////////////////////////////////////////////////////////////
//
//        BCD = new ModelPart( this, 65, 50 );
//        BCD.setTexSize( 128, 64 );
//        BCD.addBox( -4.0f, -6.0f, -1.0f, 8, 12, 2);
//        BCD.setPos( 0.0f, 6.5f, 3.0f );
//
//        BCD12 = new ModelPart( this, 102, 46 );
//        BCD12.setTexSize( 128, 64 );
//        BCD12.addBox( -0.5f, -0.5f, -0.5f, 1, 1, 1);
//        BCD12.setPos( 0.0f, 10.0f, -2.7f );
//
//        BCD11 = new ModelPart( this, 79, 42 );
//        BCD11.setTexSize( 128, 64 );
//        BCD11.addBox( -0.5f, -0.5f, -2.0f, 1, 1, 4);
//        BCD11.setPos( 3.6f, 3.0f, 0.0f );
//
//        BCD4 = new ModelPart( this, 97, 50 );
//        BCD4.setTexSize( 128, 64 );
//        BCD4.addBox( -1.0f, -5.5f, -0.5f, 2, 11, 1);
//        BCD4.setPos( 3.0f, 5.5f, -2.5f );
//
//        Tank2 = new ModelPart( this, 41, 50 );
//        Tank2.setTexSize( 128, 64 );
//        Tank2.addBox( -2.0f, -5.0f, -2.0f, 4, 10, 4);
//        Tank2.setPos( -3.0f, 7.0f, 6.5f );
//
//        Tank2m1 = new ModelPart( this, 45, 54 );
//        Tank2m1.setTexSize( 128, 64 );
//        Tank2m1.addBox( -1.5f, -4.5f, -0.5f, 3, 9, 1);
//        Tank2m1.setPos( -3.0f, 7.0f, 8.5f );
//
//        Tank2m2 = new ModelPart( this, 45, 54 );
//        Tank2m2.setTexSize( 128, 64 );
//        Tank2m2.addBox( -1.5f, -4.5f, -0.5f, 3, 9, 1);
//        Tank2m2.setPos( -5.0f, 7.0f, 6.5f );
//
//        Tank2m3 = new ModelPart( this, 45, 54 );
//        Tank2m3.setTexSize( 128, 64 );
//        Tank2m3.addBox( -1.5f, -4.5f, -0.5f, 3, 9, 1);
//        Tank2m3.setPos( -1.0f, 7.0f, 6.5f );
//
//        Tank2m4 = new ModelPart( this, 43, 46 );
//        Tank2m4.setTexSize( 128, 64 );
//        Tank2m4.addBox( -1.5f, -0.5f, -1.5f, 3, 1, 3);
//        Tank2m4.setPos( -3.0f, 1.5f, 6.5f );
//
//        Tank2m5 = new ModelPart( this, 38, 49 );
//        Tank2m5.setTexSize( 128, 64 );
//        Tank2m5.addBox( -0.5f, -2.0f, -0.5f, 1, 4, 1);
//        Tank2m5.setPos( -3.0f, -0.5f, 6.5f );
//
//        Tank2m6 = new ModelPart( this, 44, 44 );
//        Tank2m6.setTexSize( 128, 64 );
//        Tank2m6.addBox( -2.0f, -0.5f, -0.5f, 4, 1, 1);
//        Tank2m6.setPos( -3.5f, -0.5f, 6.5f );
//
//        Tank2m7 = new ModelPart( this, 36, 44 );
//        Tank2m7.setTexSize( 128, 64 );
//        Tank2m7.addBox( -1.0f, -1.0f, -1.0f, 2, 2, 2);
//        Tank2m7.setPos( -5.5f, -0.5f, 6.5f );
//
//        BCD2 = new ModelPart( this, 66, 51 );
//        BCD2.setTexSize( 128, 64 );
//        BCD2.addBox( -3.5f, -5.0f, -0.5f, 7, 10, 1);
//        BCD2.setPos( 0.0f, 6.5f, 4.0f );
//
//        Tank1 = new ModelPart( this, 41, 50 );
//        Tank1.setTexSize( 128, 64 );
//        Tank1.addBox( -2.0f, -5.0f, -2.0f, 4, 10, 4);
//        Tank1.setPos( 3.0f, 7.0f, 6.5f );
//
//        Tank1m1 = new ModelPart( this, 45, 54 );
//        Tank1m1.setTexSize( 128, 64 );
//        Tank1m1.addBox( -1.5f, -4.5f, -0.5f, 3, 9, 1);
//        Tank1m1.setPos( 3.0f, 7.0f, 8.5f );
//
//        Tank1m2 = new ModelPart( this, 45, 54 );
//        Tank1m2.setTexSize( 128, 64 );
//        Tank1m2.addBox( -1.5f, -4.5f, -0.5f, 3, 9, 1);
//        Tank1m2.setPos( 1.0f, 7.0f, 6.5f );
//
//        Tank1m3 = new ModelPart( this, 45, 54 );
//        Tank1m3.setTexSize( 128, 64 );
//        Tank1m3.addBox( -1.5f, -4.5f, -0.5f, 3, 9, 1);
//        Tank1m3.setPos( 5.0f, 7.0f, 6.5f );
//
//        Tank1m4 = new ModelPart( this, 43, 46 );
//        Tank1m4.setTexSize( 128, 64 );
//        Tank1m4.addBox( -1.5f, -0.5f, -1.5f, 3, 1, 3);
//        Tank1m4.setPos( 3.0f, 1.5f, 6.5f );
//
//        Tank1m5 = new ModelPart( this, 38, 49 );
//        Tank1m5.setTexSize( 128, 64 );
//        Tank1m5.addBox( -0.5f, -2.0f, -0.5f, 1, 4, 1);
//        Tank1m5.setPos( 3.0f, -0.5f, 6.5f );
//
//        Tank1m6 = new ModelPart( this, 44, 44 );
//        Tank1m6.setTexSize( 128, 64 );
//        Tank1m6.addBox( -2.0f, -0.5f, -0.5f, 4, 1, 1);
//        Tank1m6.setPos( 3.5f, -0.5f, 6.5f );
//
//        Tank1m7 = new ModelPart( this, 36, 44 );
//        Tank1m7.setTexSize( 128, 64 );
//        Tank1m7.addBox( -1.0f, -1.0f, -1.0f, 2, 2, 2);
//        Tank1m7.setPos( 5.5f, -0.5f, 6.5f );
//
//        BCD6 = new ModelPart( this, 68, 41 );
//        BCD6.setTexSize( 128, 64 );
//        BCD6.addBox( -0.5f, -1.0f, -2.0f, 1, 2, 4);
//        BCD6.setPos( -3.6f, 10.0f, 0.0f );
//
//        BCD5 = new ModelPart( this, 68, 41 );
//        BCD5.setTexSize( 128, 64 );
//        BCD5.addBox( -0.5f, -1.0f, -2.0f, 1, 2, 4);
//        BCD5.setPos( 3.6f, 10.0f, 0.0f );
//
//        BCD3 = new ModelPart( this, 91, 50 );
//        BCD3.setTexSize( 128, 64 );
//        BCD3.addBox( -1.0f, -5.5f, -0.5f, 2, 11, 1);
//        BCD3.setPos( -3.0f, 5.5f, -2.5f );
//
//        BCD7 = new ModelPart( this, 91, 45 );
//        BCD7.setTexSize( 128, 64 );
//        BCD7.addBox( -2.0f, -1.0f, -0.5f, 4, 2, 1);
//        BCD7.setPos( 0.0f, 10.0f, -2.5f );
//
//        BCD8 = new ModelPart( this, 91, 48 );
//        BCD8.setTexSize( 128, 64 );
//        BCD8.addBox( -2.0f, -0.5f, -0.5f, 4, 1, 1);
//        BCD8.setPos( 0.0f, 3.0f, -2.5f );
//
//        BCD9 = new ModelPart( this, 79, 42 );
//        BCD9.setTexSize( 128, 64 );
//        BCD9.addBox( -0.5f, -0.5f, -2.0f, 1, 1, 4);
//        BCD9.setPos( -3.6f, 3.0f, 0.0f );
//
//        BCD13 = new ModelPart( this, 91, 38 );
//        BCD13.setTexSize( 128, 64 );
//        BCD13.addBox( -4.0f, -0.5f, -0.5f, 8, 1, 1);
//        BCD13.setPos( 0.0f, 0.5f, 2.5f );
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
//        Mask.addBox( -4.0f, -0.5f, -0.5f, 8, 1, 1);
//        Mask.setPos( 0.0f, -6.0f, -4.5f );
//
//        Mask2 = new ModelPart( this, 120, 55 );
//        Mask2.setTexSize( 128, 64 );
//        Mask2.addBox( -0.5f, -2.0f, -0.5f, 1, 4, 1);
//        Mask2.setPos( -4.0f, -4.0f, -4.5f );
//
//        Mask3 = new ModelPart( this, 116, 55 );
//        Mask3.setTexSize( 128, 64 );
//        Mask3.addBox( -0.5f, -2.0f, -0.5f, 1, 4, 1);
//        Mask3.setPos( 4.0f, -4.0f, -4.5f );
//
//        Mask4 = new ModelPart( this, 114, 51 );
//        Mask4.setTexSize( 128, 64 );
//        Mask4.addBox( -1.5f, -0.5f, -0.5f, 3, 1, 1);
//        Mask4.setPos( -2.5f, -2.0f, -4.5f );
//
//        Mask5 = new ModelPart( this, 114, 53 );
//        Mask5.setTexSize( 128, 64 );
//        Mask5.addBox( -1.5f, -0.5f, -0.5f, 3, 1, 1);
//        Mask5.setPos( 2.5f, -2.0f, -4.5f );
//
//        Mask6 = new ModelPart( this, 114, 49 );
//        Mask6.setTexSize( 128, 64 );
//        Mask6.addBox( -1.5f, -0.5f, -0.5f, 3, 1, 1);
//        Mask6.setPos( 0.0f, -3.0f, -4.5f );
//
//        Mask7 = new ModelPart( this, 110, 38 );
//        Mask7.setTexSize( 128, 64 );
//        Mask7.addBox( -0.5f, -1.0f, -4.0f, 1, 2, 8);
//        Mask7.setPos( 4.0f, -4.5f, 0.0f );
//
//        Mask8 = new ModelPart( this, 110, 38 );
//        Mask8.setTexSize( 128, 64 );
//        Mask8.addBox( -0.5f, -1.0f, -4.0f, 1, 2, 8);
//        Mask8.setPos( -4.0f, -4.5f, 0.0f );
//
//        Mask9 = new ModelPart( this, 110, 35 );
//        Mask9.setTexSize( 128, 64 );
//        Mask9.addBox( -4.0f, -1.0f, -0.5f, 8, 2, 1);
//        Mask9.setPos( 0.0f, -4.5f, 4.0f );
//
//        mouthpiece = new ModelPart( this, 115, 28 );
//        mouthpiece.setTexSize( 128, 64 );
//        mouthpiece.addBox( -1.5f, -1.5f, -0.5f, 3, 3, 1);
//        mouthpiece.setPos( 0.0f, 0.0f, -5.0f );
//
//        mouthpiece2 = new ModelPart( this, 116, 25 );
//        mouthpiece2.setTexSize( 128, 64 );
//        mouthpiece2.addBox( -1.0f, -1.0f, -0.5f, 2, 2, 1);
//        mouthpiece2.setPos( 0.0f, 0.0f, -5.5f );
//
//        mouthpiece3 = new ModelPart( this, 116, 23 );
//        mouthpiece3.setTexSize( 128, 64 );
//        mouthpiece3.addBox( -1.0f, -0.5f, -0.5f, 2, 1, 1);
//        mouthpiece3.setPos( 0.0f, -0.6000004f, -4.0f );
//
//        head = new ModelPart( this, 0, 0 );
//        head.setTexSize( 128, 64 );
//        head.addBox( -4.0f, -4.0f, -4.0f, 8, 8, 8);
//        head.setPos( 0.0f, -4.0f, 0.0f );
//        head.mirror = true;
//        setRotation(head, 0.0f, 0.0f, 0.0f);
//
//        hose1 = new ModelPart( this, 117, 16 );
//        hose1.setTexSize( 128, 64 );
//        hose1.addBox( -0.5f, -0.5f, -0.5f, 1, 1, 1);
//        hose1.setPos( 3.0f, -3.0f, 6.5f );
//
//        hose2 = new ModelPart( this, 117, 16 );
//        hose2.setTexSize( 128, 64 );
//        hose2.addBox( -1.5f, -0.5f, -0.5f, 3, 1, 1);
//        hose2.setPos( 5.0f, -3.0f, 6.5f );
//
//        hose3 = new ModelPart( this, 116, 15 );
//        hose3.setTexSize( 128, 64 );
//        hose3.addBox( -0.5f, -0.5f, -1.0f, 1, 1, 2);
//        hose3.setPos( 6.0f, -3.0f, 5.0f );
//
//        hose4 = new ModelPart( this, 106, 7 );
//        hose4.setTexSize( 128, 64 );
//        hose4.addBox( -0.5f, -0.5f, -10.0f, 1, 1, 10);
//        hose4.setPos( 6.0f, -3.0f, 4.2f );
//
//        hose5 = new ModelPart( this, 115, 16 );
//        hose5.setTexSize( 128, 64 );
//        hose5.addBox( -2.5f, -0.5f, -0.5f, 5, 1, 1);
//        hose5.setPos( 4.0f, 0.0f, -5.0f );
//
//        hose6 = new ModelPart( this, 115, 16 );
//        hose6.setTexSize( 128, 64 );
//        hose6.addBox( -1.5f, -0.5f, -0.5f, 3, 1, 1);
//        hose6.setPos( 0.0f, -0.5f, 6.5f );
    }

    public static LayerDefinition create() {
        MeshDefinition modelData = createMesh(CubeDeformation.NONE, 0);
        PartDefinition modelPartData = modelData.getRoot();

        modelPartData.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(32, 16)
                        .addBox(-4.0f, -6.0f, -2.0f, 8, 12, 4),
                PartPose.offset(0.0f, 6.0f, 0.0f));

        modelPartData.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                        .texOffs(56, 16)
                        .addBox(-4.0f, 0.0f, -2.0f, 4, 12, 4),
                PartPose.offset(-4.0f, 0.0f, 0.0f));

        modelPartData.addOrReplaceChild("left_arm",
                CubeListBuilder.create()
                        .texOffs(72, 16)
                        .addBox(0.0f, 0.0f, -2.0f, 4, 12, 4),
                PartPose.offset(4.0f, 0.0f, 0.0f));

        ///////////////////////////////////////////////////////////////////////////////////////////

        PartDefinition modelPartLegRight = modelPartData.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(-2.0f, -6.0f, -2.0f, 4, 12, 4),
                PartPose.offset(-2.0f, 18.0f, 0.0f));

        PartDefinition modelPartFinRight = modelPartLegRight.addOrReplaceChild("Fin1",
                CubeListBuilder.create().mirror()
                        .texOffs(10, 38)
                        .addBox(-5.0f, 22.0f, -2.5f, 5, 2, 5),
                PartPose.offset(2.5f, -12.0f, 0.0f));

        modelPartFinRight.addOrReplaceChild("Fin1m1",
                CubeListBuilder.create().mirror()
                        .texOffs(13, 47)
                        .addBox(-2.5f, -1.5f, -1.0f, 5, 1, 2),
                PartPose.offset(-3.19707f, 24.5f, -3.288924f));

        modelPartFinRight.addOrReplaceChild("Fin1m2",
                CubeListBuilder.create().mirror()
                        .texOffs(15, 45)
                        .addBox(-2.0f, -1.5f, -0.5f, 4, 1, 1),
                PartPose.offset(-3.02606f, 23.5f, -2.819078f));

        modelPartFinRight.addOrReplaceChild("Fin1m3",
                CubeListBuilder.create().mirror()
                        .texOffs(1, 52)
                        .addBox(-5.0f, -1.0f, -6.0f, 10, 0, 12),
                PartPose.offset(-5.420201f, 24.5f, -9.396926f));

        modelPartFinRight.addOrReplaceChild("Fin1m4",
                CubeListBuilder.create().mirror()
                        .texOffs(15, 50)
                        .addBox(-2.0f, -1.5f, -0.5f, 4, 1, 1),
                PartPose.offset(-3.710101f, 24.5f, -4.698463f));

        ///////////////////////////////////////////////////////////////////////////////////////////

        PartDefinition modelPartLegLeft = modelPartData.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(16, 16)
                        .addBox(-2.0f, -6.0f, -2.0f, 4, 12, 4),
                PartPose.offset(2.0f, 18.0f, 0.0f));

        PartDefinition modelPartFinleft = modelPartLegLeft.addOrReplaceChild("Fin2",
                CubeListBuilder.create().mirror()
                        .texOffs(10, 38)
                        .addBox(0.0f, 22.0f, -2.5f, 5, 2, 5),
                PartPose.offset(-2.0f, -12.0f, 0.0f));

        modelPartFinleft.addOrReplaceChild("Fin2m1",
                CubeListBuilder.create().mirror()
                        .texOffs(13, 47)
                        .addBox(-2.5f, -1.5f, -1.0f, 5, 1, 2),
                PartPose.offset(3.19707f, 24.5f, -3.288924f));

        modelPartFinleft.addOrReplaceChild("Fin2m2",
                CubeListBuilder.create().mirror()
                        .texOffs(15, 45)
                        .addBox(-2.0f, -1.5f, -0.5f, 4, 1, 1),
                PartPose.offset(3.02606f, 23.5f, -2.819078f));

        modelPartFinleft.addOrReplaceChild("Fin2m3",
                CubeListBuilder.create().mirror()
                        .texOffs(1, 52)
                        .addBox(-5.0f, -1.0f, -6.0f, 10, 0, 12),
                PartPose.offset(5.420201f, 24.5f, -9.396926f));

        modelPartFinleft.addOrReplaceChild("Fin2m4",
                CubeListBuilder.create().mirror()
                        .texOffs(15, 50)
                        .addBox(-2.0f, -1.5f, -0.5f, 4, 1, 1),
                PartPose.offset(3.710101f, 24.5f, -4.698463f));

        ///////////////////////////////////////////////////////////////////////////////////////////

        modelPartData.addOrReplaceChild("BCD",
                CubeListBuilder.create()
                        .texOffs(65, 50)
                        .addBox(-4.0f, -6.0f, -1.0f, 8, 12, 2),
                PartPose.offset(0.0f, 6.5f, 3.0f));

        modelPartData.addOrReplaceChild("BCD12",
                CubeListBuilder.create()
                        .texOffs(102, 46)
                        .addBox(-0.5f, -0.5f, -0.5f, 1, 1, 1),
                PartPose.offset(0.0f, 10.0f, -2.7f));

        modelPartData.addOrReplaceChild("BCD11",
                CubeListBuilder.create()
                        .texOffs(79, 42)
                        .addBox(-0.5f, -0.5f, -2.0f, 1, 1, 4),
                PartPose.offset(3.6f, 3.0f, 0.0f));

        modelPartData.addOrReplaceChild("BCD4",
                CubeListBuilder.create()
                        .texOffs(97, 50)
                        .addBox(-1.0f, -5.5f, -0.5f, 2, 11, 1),
                PartPose.offset(3.0f, 5.5f, -2.5f));

        modelPartData.addOrReplaceChild("Tank2",
                CubeListBuilder.create()
                        .texOffs(41, 50)
                        .addBox(-2.0f, -5.0f, -2.0f, 4, 10, 4),
                PartPose.offset(-3.0f, 7.0f, 6.5f));

        modelPartData.addOrReplaceChild("Tank2m1",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1),
                PartPose.offset(-3.0f, 7.0f, 8.5f));

        modelPartData.addOrReplaceChild("Tank2m2",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1),
                PartPose.offset(-5.0f, 7.0f, 6.5f));

        modelPartData.addOrReplaceChild("Tank2m3",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1),
                PartPose.offset(-1.0f, 7.0f, 6.5f));

        modelPartData.addOrReplaceChild("Tank2m4",
                CubeListBuilder.create()
                        .texOffs(43, 46)
                        .addBox(-1.5f, -0.5f, -1.5f, 3, 1, 3),
                PartPose.offset(-3.0f, 1.5f, 6.5f));

        modelPartData.addOrReplaceChild("Tank2m5",
                CubeListBuilder.create()
                        .texOffs(38, 49)
                        .addBox(-0.5f, -2.0f, -0.5f, 1, 4, 1),
                PartPose.offset(-3.0f, -0.5f, 6.5f));

        modelPartData.addOrReplaceChild("Tank2m6",
                CubeListBuilder.create()
                        .texOffs(44, 44)
                        .addBox(-2.0f, -0.5f, -0.5f, 4, 1, 1),
                PartPose.offset(-3.5f, -0.5f, 6.5f));

        modelPartData.addOrReplaceChild("Tank2m7",
                CubeListBuilder.create()
                        .texOffs(36, 44)
                        .addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2),
                PartPose.offset(-5.5f, -0.5f, 6.5f));

        modelPartData.addOrReplaceChild("BCD2",
                CubeListBuilder.create()
                        .texOffs(66, 51)
                        .addBox(-3.5f, -5.0f, -0.5f, 7, 10, 1),
                PartPose.offset(0.0f, 6.5f, 4.0f));

        modelPartData.addOrReplaceChild("Tank1",
                CubeListBuilder.create()
                        .texOffs(41, 50)
                        .addBox(-2.0f, -5.0f, -2.0f, 4, 10, 4),
                PartPose.offset(3.0f, 7.0f, 6.5f));

        modelPartData.addOrReplaceChild("Tank1m1",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1),
                PartPose.offset(3.0f, 7.0f, 8.5f));

        modelPartData.addOrReplaceChild("Tank1m2",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1),
                PartPose.offset(1.0f, 7.0f, 6.5f));

        modelPartData.addOrReplaceChild("Tank1m3",
                CubeListBuilder.create()
                        .texOffs(45, 54)
                        .addBox(-1.5f, -4.5f, -0.5f, 3, 9, 1),
                PartPose.offset(5.0f, 7.0f, 6.5f));

        modelPartData.addOrReplaceChild("Tank1m4",
                CubeListBuilder.create()
                        .texOffs(43, 46)
                        .addBox(-1.5f, -0.5f, -1.5f, 3, 1, 3),
                PartPose.offset(3.0f, 1.5f, 6.5f));

        modelPartData.addOrReplaceChild("Tank1m5",
                CubeListBuilder.create()
                        .texOffs(38, 49)
                        .addBox(-0.5f, -2.0f, -0.5f, 1, 4, 1),
                PartPose.offset(3.0f, -0.5f, 6.5f));

        modelPartData.addOrReplaceChild("Tank1m6",
                CubeListBuilder.create()
                        .texOffs(44, 44)
                        .addBox(-2.0f, -0.5f, -0.5f, 4, 1, 1),
                PartPose.offset(3.5f, -0.5f, 6.5f));

        modelPartData.addOrReplaceChild("Tank1m7",
                CubeListBuilder.create()
                        .texOffs(36, 44)
                        .addBox(-1.0f, -1.0f, -1.0f, 2, 2, 2),
                PartPose.offset(5.5f, -0.5f, 6.5f));

        modelPartData.addOrReplaceChild("BCD6",
                CubeListBuilder.create()
                        .texOffs(68, 41)
                        .addBox(-0.5f, -1.0f, -2.0f, 1, 2, 4),
                PartPose.offset(-3.6f, 10.0f, 0.0f));

        modelPartData.addOrReplaceChild("BCD5",
                CubeListBuilder.create()
                        .texOffs(68, 41)
                        .addBox(-0.5f, -1.0f, -2.0f, 1, 2, 4),
                PartPose.offset(3.6f, 10.0f, 0.0f));

        modelPartData.addOrReplaceChild("BCD3",
                CubeListBuilder.create()
                        .texOffs(91, 50)
                        .addBox(-1.0f, -5.5f, -0.5f, 2, 11, 1),
                PartPose.offset(-3.0f, 5.5f, -2.5f));

        modelPartData.addOrReplaceChild("BCD7",
                CubeListBuilder.create()
                        .texOffs(91, 45)
                        .addBox(-2.0f, -1.0f, -0.5f, 4, 2, 1),
                PartPose.offset(0.0f, 10.0f, -2.5f));

        modelPartData.addOrReplaceChild("BCD8",
                CubeListBuilder.create()
                        .texOffs(91, 48)
                        .addBox(-2.0f, -0.5f, -0.5f, 4, 1, 1),
                PartPose.offset(0.0f, 3.0f, -2.5f));

        modelPartData.addOrReplaceChild("BCD9",
                CubeListBuilder.create()
                        .texOffs(79, 42)
                        .addBox(-0.5f, -0.5f, -2.0f, 1, 1, 4),
                PartPose.offset(-3.6f, 3.0f, 0.0f));

        modelPartData.addOrReplaceChild("BCD13",
                CubeListBuilder.create()
                        .texOffs(91, 38)
                        .addBox(-4.0f, -0.5f, -0.5f, 8, 1, 1),
                PartPose.offset(0.0f, 0.5f, 2.5f));

        ///////////////////////////////////////////////////////////////////////////////////////////

        PartDefinition modelPartHead = modelPartData.addOrReplaceChild("head",
                CubeListBuilder.create().mirror()
                        .texOffs(0, 0)
                        .addBox(-4.0f, -4.0f, -4.0f, 8, 8, 8),
                PartPose.offsetAndRotation(0.0f, -4.0f, 0.0f, 0.0f, 0.0f, 0.0f));

        modelPartHead.addOrReplaceChild("Mask",
                CubeListBuilder.create()
                        .texOffs(109, 60)
                        .addBox(-4.0f, -0.5f, -0.5f, 8, 1, 1),
                PartPose.offset(0.0f, -6.0f, -4.5f));

        modelPartHead.addOrReplaceChild("Mask2",
                CubeListBuilder.create()
                        .texOffs(120, 55)
                        .addBox(-0.5f, -2.0f, -0.5f, 1, 4, 1),
                PartPose.offset(-4.0f, -4.0f, -4.5f));

        modelPartHead.addOrReplaceChild("Mask3",
                CubeListBuilder.create()
                        .texOffs(116, 55)
                        .addBox(-0.5f, -2.0f, -0.5f, 1, 4, 1),
                PartPose.offset(4.0f, -4.0f, -4.5f));

        modelPartHead.addOrReplaceChild("Mask4",
                CubeListBuilder.create()
                        .texOffs(114, 51)
                        .addBox(-1.5f, -0.5f, -0.5f, 3, 1, 1),
                PartPose.offset(-2.5f, -2.0f, -4.5f));

        modelPartHead.addOrReplaceChild("Mask5",
                CubeListBuilder.create()
                        .texOffs(114, 53)
                        .addBox(-1.5f, -0.5f, -0.5f, 3, 1, 1),
                PartPose.offset(2.5f, -2.0f, -4.5f));

        modelPartHead.addOrReplaceChild("Mask6",
                CubeListBuilder.create()
                        .texOffs(114, 49)
                        .addBox(-1.5f, -0.5f, -0.5f, 3, 1, 1),
                PartPose.offset(0.0f, -3.0f, -4.5f));

        modelPartHead.addOrReplaceChild("Mask7",
                CubeListBuilder.create()
                        .texOffs(110, 38)
                        .addBox(-0.5f, -1.0f, -4.0f, 1, 2, 8),
                PartPose.offset(4.0f, -4.5f, 0.0f));

        modelPartHead.addOrReplaceChild("Mask8",
                CubeListBuilder.create()
                        .texOffs(110, 38)
                        .addBox(-0.5f, -1.0f, -4.0f, 1, 2, 8),
                PartPose.offset(-4.0f, -4.5f, 0.0f));

        modelPartData.addOrReplaceChild("Mask9",
                CubeListBuilder.create()
                        .texOffs(110, 35)
                        .addBox(-4.0f, -1.0f, -0.5f, 8, 2, 1),
                PartPose.offset(0.0f, -4.5f, 4.0f));

        modelPartHead.addOrReplaceChild("mouthpiece",
                CubeListBuilder.create()
                        .texOffs(115, 28)
                        .addBox(-1.5f, -1.5f, -0.5f, 3, 3, 1),
                PartPose.offset(0.0f, 0.0f, -5.0f));

        modelPartHead.addOrReplaceChild("mouthpiece2",
                CubeListBuilder.create()
                        .texOffs(116, 25)
                        .addBox(-1.0f, -1.0f, -0.5f, 2, 2, 1),
                PartPose.offset(0.0f, 0.0f, -5.5f));

        modelPartHead.addOrReplaceChild("mouthpiece3",
                CubeListBuilder.create()
                        .texOffs(116, 23)
                        .addBox(-1.0f, -0.5f, -0.5f, 2, 1, 1),
                PartPose.offset(0.0f, -0.6000004f, -4.0f));

        modelPartHead.addOrReplaceChild("hose1",
                CubeListBuilder.create()
                        .texOffs(117, 16)
                        .addBox(-0.5f, -0.5f, -0.5f, 1, 1, 1),
                PartPose.offset(3.0f, -3.0f, 6.5f));

        modelPartHead.addOrReplaceChild("hose2",
                CubeListBuilder.create()
                        .texOffs(117, 16)
                        .addBox(-1.5f, -0.5f, -0.5f, 3, 1, 1),
                PartPose.offset(5.0f, -3.0f, 6.5f));

        modelPartHead.addOrReplaceChild("hose3",
                CubeListBuilder.create()
                        .texOffs(116, 15)
                        .addBox(-0.5f, -0.5f, -1.0f, 1, 1, 2),
                PartPose.offset(6.0f, -3.0f, 5.0f));

        modelPartHead.addOrReplaceChild("hose4",
                CubeListBuilder.create()
                        .texOffs(106, 7)
                        .addBox(-0.5f, -0.5f, -10.0f, 1, 1, 10),
                PartPose.offset(6.0f, -3.0f, 4.2f));

        modelPartHead.addOrReplaceChild("hose5",
                CubeListBuilder.create()
                        .texOffs(115, 16)
                        .addBox(-2.5f, -0.5f, -0.5f, 5, 1, 1),
                PartPose.offset(4.0f, 0.0f, -5.0f));

        modelPartData.addOrReplaceChild("hose6",
                CubeListBuilder.create()
                        .texOffs(115, 16)
                        .addBox(-1.5f, -0.5f, -0.5f, 3, 1, 1),
                PartPose.offset(0.0f, -0.5f, 6.5f));

        return LayerDefinition.create(modelData, 128, 64);
    }

    public static ModelScubaGear createModel(ModelLayerLocation entityModelLayer, @Nullable EntityModelSet entityModelLoader, EquipmentSlot slot) {
        return new ModelScubaGear(entityModelLoader == null ?
                create().bakeRoot() :
                entityModelLoader.bakeLayer(entityModelLayer), slot);
    }

    @Override
    public void prepareMobModel(LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
        showHead = !entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && slot == EquipmentSlot.HEAD;
        showChest = !entity.getItemBySlot(EquipmentSlot.CHEST).isEmpty() && slot == EquipmentSlot.CHEST;
        showLegs = !entity.getItemBySlot(EquipmentSlot.FEET).isEmpty() && slot == EquipmentSlot.FEET;
        isSneaking = entity.getPose() == Pose.CROUCHING;
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, int color) {
        stack.pushPose();

        if (young) {
            stack.scale(0.75f, 0.75f, 0.75f);
            stack.translate(0.0f, 16.0f * 1, 0.0f);
            head.render(stack, bufferIn, packedLightIn, packedOverlayIn);
            stack.popPose();
            stack.pushPose();
            stack.scale(0.5f, 0.5f, 0.5f);
            stack.translate(0.0f, 24.0f * 1, 0.0f);
            renderScubaGear(stack, bufferIn, packedLightIn, packedOverlayIn, false);
        } else {
            if (isSneaking) {
                stack.translate(0.0f, 0.2f, 0.0f);
            }

            renderScubaGear(stack, bufferIn, packedLightIn, packedOverlayIn, true);
        }

        stack.popPose();
    }

    public void renderScubaGear(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, boolean renderHead) {
        hose4.xRot = 0.3075211f;

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
//            hose6.rotateAngleX = 0.0f;
//            hose6.rotateAngleY = 0.0f;
//            hose6.rotateAngleZ = 0.0f;
//            hose6.render(stack, bufferIn, packedLightIn, packedOverlayIn);
//        }

        if (showChest) {
            renderTank(stack, bufferIn, packedLightIn, packedOverlayIn);
            renderBCD(stack, bufferIn, packedLightIn, packedOverlayIn);
        }

        rightLeg.visible = showLegs;
        leftLeg.visible = showLegs;

        if (renderHead) {
            stack.pushPose();
            if (isSneaking) {
                stack.translate(0, -(0.112f + 0.0625f), 0);
            }

            head.render(stack, bufferIn, packedLightIn, packedOverlayIn);
            stack.popPose();
        }

        body.render(stack, bufferIn, packedLightIn, packedOverlayIn);
        rightArm.render(stack, bufferIn, packedLightIn, packedOverlayIn);
        leftArm.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        //TODO [Port]: Not possible now within minecraft 1.17?
        //rightLeg.mirror = true;

//        bipedLeftLeg.rotationPointY = 0;
        //TODO bipedLeftLeg.offsetY = 0.763f;

        if (showLegs) {
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



                GlStateManager.translate(0.0f, 0.0f, 0.22f+((-MathHelper.cos(limbSwing * 0.6662f))/(1.7f*Math.PI)));
                GlStateManager.translate(0.0f, -0.05f+(float)(-MathHelper.cos(limbSwing)/64), 0.0f);


                GlStateManager.translate(0.0f, offsetY, -0.4f);

                GlStateManager.rotate(-paddleAngle+((float)Math.sin(ageInTicks*paddleSpeed)*paddleAmount), 1.0f, 0.0f, 0.0f);

                GlStateManager.translate(0.0f, -offsetY, 0.2f);



                this.Fin2m3.render(scale);

                GlStateManager.popMatrix();*/






          /*  GlStateManager.pushMatrix();

            GlStateManager.translate(0, 0, 0.1f);

            GlStateManager.translate(0.0f, 0.0f, 0.23f+((MathHelper.cos(limbSwing * 0.6662f))/(1.7f*Math.PI)));
            GlStateManager.translate(0.0f, -0.05f+(float)(MathHelper.cos(limbSwing)/64), 0.0f);


            GlStateManager.translate(0.0f, offsetY, -0.4f);

            GlStateManager.rotate(-paddleAngle-((float)Math.sin(ageInTicks*paddleSpeed)*paddleAmount), 1.0f, 0.0f, 0.0f);

            GlStateManager.translate(0.0f, -offsetY, 0.2f);


            this.Fin1m3.render(scale);

            GlStateManager.popMatrix();*/

//        }else {
            //TODO  this.Fin2m3.offsetX = 0.0f;

            //TODO  this.Fin1m3.offsetX = 0.0f;

            //TODO  this.Fin2m3.offsetY = 0.0f;

            //TODO   this.Fin1m3.offsetY = 0.0f;

            Fin2m3.xRot = 0.0f;

            Fin1m3.xRot = 0.0f;

            leftLeg.render(stack, bufferIn, packedLightIn, packedOverlayIn);

            rightLeg.render(stack, bufferIn, packedLightIn, packedOverlayIn);

//        }

        }
    }

    private void renderTank(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
        Tank2.xRot = 0.0f;
        Tank2.yRot = 0.0f;
        Tank2.zRot = 0.0f;
        Tank2.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank2m1.xRot = 0.0f;
        Tank2m1.yRot = 0.0f;
        Tank2m1.zRot = 0.0f;
        Tank2m1.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank2m2.xRot = 0.0f;
        Tank2m2.yRot = -Mth.HALF_PI;
        Tank2m2.zRot = 0.0f;
        Tank2m2.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank2m3.xRot = 0.0f;
        Tank2m3.yRot = -Mth.HALF_PI;
        Tank2m3.zRot = 0.0f;
        Tank2m3.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank2m4.xRot = 0.0f;
        Tank2m4.yRot = 0.0f;
        Tank2m4.zRot = 0.0f;
        Tank2m4.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank2m5.xRot = 0.0f;
        Tank2m5.yRot = 0.0f;
        Tank2m5.zRot = 0.0f;
        Tank2m5.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank2m6.xRot = 0.0f;
        Tank2m6.yRot = 0.0f;
        Tank2m6.zRot = 0.0f;
        Tank2m6.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank2m7.xRot = 0.0f;
        Tank2m7.yRot = 0.0f;
        Tank2m7.zRot = 0.0f;
        Tank2m7.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1.xRot = 0.0f;
        Tank1.yRot = 0.0f;
        Tank1.zRot = 0.0f;
        Tank1.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1m1.xRot = 0.0f;
        Tank1m1.yRot = 0.0f;
        Tank1m1.zRot = 0.0f;
        Tank1m1.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1m2.xRot = 0.0f;
        Tank1m2.yRot = -Mth.HALF_PI;
        Tank1m2.zRot = 0.0f;
        Tank1m2.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1m3.xRot = 0.0f;
        Tank1m3.yRot = -Mth.HALF_PI;
        Tank1m3.zRot = 0.0f;
        Tank1m3.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1m4.xRot = 0.0f;
        Tank1m4.yRot = 0.0f;
        Tank1m4.zRot = 0.0f;
        Tank1m4.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1m5.xRot = 0.0f;
        Tank1m5.yRot = 0.0f;
        Tank1m5.zRot = 0.0f;
        Tank1m5.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1m6.xRot = 0.0f;
        Tank1m6.yRot = 0.0f;
        Tank1m6.zRot = 0.0f;
        Tank1m6.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        Tank1m7.xRot = 0.0f;
        Tank1m7.yRot = 0.0f;
        Tank1m7.zRot = 0.0f;
        Tank1m7.render(stack, bufferIn, packedLightIn, packedOverlayIn);
    }

    private void renderBCD(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
        BCD.xRot = 0.0f;
        BCD.yRot = 0.0f;
        BCD.zRot = 0.0f;
        BCD.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD12.xRot = 0.0f;
        BCD12.yRot = 0.0f;
        BCD12.zRot = 0.0f;
        BCD12.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD11.xRot = 0.0f;
        BCD11.yRot = 0.0f;
        BCD11.zRot = 0.0f;
        BCD11.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD4.xRot = 0.0f;
        BCD4.yRot = 0.0f;
        BCD4.zRot = 0.0f;
        BCD4.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD2.xRot = 0.0f;
        BCD2.yRot = 0.0f;
        BCD2.zRot = 0.0f;
        BCD2.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD6.xRot = 0.0f;
        BCD6.yRot = 0.0f;
        BCD6.zRot = 0.0f;
        BCD6.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD5.xRot = 0.0f;
        BCD5.yRot = 0.0f;
        BCD5.zRot = 0.0f;
        BCD5.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD3.xRot = 0.0f;
        BCD3.yRot = 0.0f;
        BCD3.zRot = 0.0f;
        BCD3.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD7.xRot = 0.0f;
        BCD7.yRot = 0.0f;
        BCD7.zRot = 0.0f;
        BCD7.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD8.xRot = 0.0f;
        BCD8.yRot = 0.0f;
        BCD8.zRot = 0.0f;
        BCD8.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD9.xRot = 0.0f;
        BCD9.yRot = 0.0f;
        BCD9.zRot = 0.0f;
        BCD9.render(stack, bufferIn, packedLightIn, packedOverlayIn);

        BCD13.xRot = 0.0f;
        BCD13.yRot = 0.0f;
        BCD13.zRot = 0.0f;
        BCD13.render(stack, bufferIn, packedLightIn, packedOverlayIn);
    }

    /**
     * Copies the angles from one object to another. This is used when objects should stay aligned with each other, like
     * the hair over a players head.
     */
    public static void copyModelRotations(ModelPart source, ModelPart dest) {
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
