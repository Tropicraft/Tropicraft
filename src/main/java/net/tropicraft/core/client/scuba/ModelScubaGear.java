package net.tropicraft.core.client.scuba;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;

public class ModelScubaGear extends BipedModel<LivingEntity> {
    
    public static final ModelScubaGear CHEST = new ModelScubaGear(0, EquipmentSlotType.CHEST);
    public static final ModelScubaGear FEET = new ModelScubaGear(0, EquipmentSlotType.FEET);
    public static final ModelScubaGear HEAD = new ModelScubaGear(0, EquipmentSlotType.HEAD);

    private boolean showHead;
    public boolean showChest;
    private boolean showLegs;
    private boolean isSneaking;

    ModelRenderer Fin1;
    ModelRenderer Fin1m1;
    ModelRenderer Fin1m2;
    ModelRenderer Fin1m3;
    ModelRenderer Fin1m4;
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
    
    public EquipmentSlotType slot;

    public ModelScubaGear() {
        this( 0.0f, null );
    }

    public ModelScubaGear(final float scale, final EquipmentSlotType slot) {
        super(RenderType::entityCutout, scale, 0, 64, 32);
        this.slot = slot;
        leftArmPose = BipedModel.ArmPose.EMPTY;
        rightArmPose = BipedModel.ArmPose.EMPTY;
        body = new ModelRenderer( this, 32, 16 );
        body.setTexSize( 128, 64 );
        body.addBox( -4F, -6F, -2F, 8, 12, 4);
        body.setPos( 0F, 6F, 0F );
        rightArm = new ModelRenderer( this, 56, 16 );
        rightArm.setTexSize( 128, 64 );
        rightArm.addBox( -4F, 0F, -2F, 4, 12, 4);
        rightArm.setPos( -4F, 0F, 0F );
        leftArm = new ModelRenderer( this, 72, 16 );
        leftArm.setTexSize( 128, 64 );
        leftArm.addBox( 0F, 0F, -2F, 4, 12, 4);
        leftArm.setPos( 4F, 0F, 0F );
        rightLeg = new ModelRenderer( this, 0, 16 );
        rightLeg.setTexSize( 128, 64 );
        rightLeg.addBox( -2F, -6F, -2F, 4, 12, 4);
        rightLeg.setPos( -2F, 18F, 0F );
        Fin1 = new ModelRenderer( this, 10, 38 );
        Fin1.setTexSize( 128, 64 );
        Fin1.addBox( -5F, 22F, -2.5F, 5, 2, 5);
        Fin1.setPos( 2.5F, -12F, 0F );
        Fin1.mirror = true;
        Fin1m1 = new ModelRenderer( this, 13, 47 );
        Fin1m1.setTexSize( 128, 64 );
        Fin1m1.addBox( -2.5F, -1.5F, -1F, 5, 1, 2);
        Fin1m1.setPos( -3.19707F, 24.5F, -3.288924F );
        setRotation(Fin1m1, 0F, 0F, 0F);
        Fin1m1.mirror = true;
        Fin1m2 = new ModelRenderer( this, 15, 45 );
        Fin1m2.setTexSize( 128, 64 );
        Fin1m2.addBox( -2F, -1.5F, -0.5F, 4, 1, 1);
        Fin1m2.setPos( -3.02606F, 23.5F, -2.819078F );
        setRotation(Fin1m2, 0F, 0F, 0F);
        Fin1m2.mirror = true;
        Fin1m3 = new ModelRenderer( this, 1, 52 );
        Fin1m3.setTexSize( 128, 64 );
        Fin1m3.addBox( -5F, -1F, -6F, 10, 0, 12);
        Fin1m3.setPos( -5.420201F, 24.5F, -9.396926F );
        setRotation(Fin1m3, 0F, 0F, 0F);
        Fin1m3.mirror = true;
        Fin1m4 = new ModelRenderer( this, 15, 50 );
        Fin1m4.setTexSize( 128, 64 );
        Fin1m4.addBox( -2F, -1.5F, -0.5F, 4, 1, 1);
        Fin1m4.setPos( -3.710101F, 24.5F, -4.698463F );
        setRotation(Fin1m4, 0F, 0F, 0F);
        Fin1m4.mirror = true;
        Fin1.addChild(Fin1m1);
        Fin1.addChild(Fin1m2);
        Fin1.addChild(Fin1m3);
        Fin1.addChild(Fin1m4);
        setRotation(Fin1, 0F, 0F, 0F);
        rightLeg.addChild(Fin1);
        leftLeg = new ModelRenderer( this, 16, 16 );
        leftLeg.setTexSize( 128, 64 );
        leftLeg.addBox( -2F, -6F, -2F, 4, 12, 4);
        leftLeg.setPos( 2F, 18F, 0F );
        Fin2 = new ModelRenderer( this, 10, 38 );
        Fin2.setTexSize( 128, 64 );
        Fin2.addBox( 0F, 22F, -2.5F, 5, 2, 5);
        Fin2.setPos( -2F, -12F, 0F );
        Fin2m1 = new ModelRenderer( this, 13, 47 );
        Fin2m1.setTexSize( 128, 64 );
        Fin2m1.addBox( -2.5F, -1.5F, -1F, 5, 1, 2);
        Fin2m1.setPos( 3.19707F, 24.5F, -3.288924F );
        setRotation(Fin2m1, 0F, 0F, 0F);
        Fin2m2 = new ModelRenderer( this, 15, 45 );
        Fin2m2.setTexSize( 128, 64 );
        Fin2m2.addBox( -2F, -1.5F, -0.5F, 4, 1, 1);
        Fin2m2.setPos( 3.02606F, 23.5F, -2.819078F );
        setRotation(Fin2m2, 0F, 0F, 0F);
        Fin2m3 = new ModelRenderer( this, 1, 52 );
        Fin2m3.setTexSize( 128, 64 );
        Fin2m3.addBox( -5F, -1.0F, -6F, 10, 0, 12);
        Fin2m3.setPos( 5.420201F, 24.5F, -9.396926F );
        setRotation(Fin2m3, 0F, 0F, 0F);
        Fin2m4 = new ModelRenderer( this, 15, 50 );
        Fin2m4.setTexSize( 128, 64 );
        Fin2m4.addBox( -2F, -1.5F, -0.5F, 4, 1, 1);
        Fin2m4.setPos( 3.710101F, 24.5F, -4.698463F );
        setRotation(Fin2m4, 0F, 0F, 0F);
        Fin2.addChild(Fin2m1);
        Fin2.addChild(Fin2m2);
        Fin2.addChild(Fin2m3);
        Fin2.addChild(Fin2m4);
        setRotation(Fin2, 0F, 0F, 0F);
        leftLeg.addChild(Fin2);
        BCD = new ModelRenderer( this, 65, 50 );
        BCD.setTexSize( 128, 64 );
        BCD.addBox( -4F, -6F, -1F, 8, 12, 2);
        BCD.setPos( 0F, 6.5F, 3F );
        BCD12 = new ModelRenderer( this, 102, 46 );
        BCD12.setTexSize( 128, 64 );
        BCD12.addBox( -0.5F, -0.5F, -0.5F, 1, 1, 1);
        BCD12.setPos( 0F, 10F, -2.7F );
        BCD11 = new ModelRenderer( this, 79, 42 );
        BCD11.setTexSize( 128, 64 );
        BCD11.addBox( -0.5F, -0.5F, -2F, 1, 1, 4);
        BCD11.setPos( 3.6F, 3F, 0F );
        BCD4 = new ModelRenderer( this, 97, 50 );
        BCD4.setTexSize( 128, 64 );
        BCD4.addBox( -1F, -5.5F, -0.5F, 2, 11, 1);
        BCD4.setPos( 3F, 5.5F, -2.5F );
        Tank2 = new ModelRenderer( this, 41, 50 );
        Tank2.setTexSize( 128, 64 );
        Tank2.addBox( -2F, -5F, -2F, 4, 10, 4);
        Tank2.setPos( -3F, 7F, 6.5F );
        Tank2m1 = new ModelRenderer( this, 45, 54 );
        Tank2m1.setTexSize( 128, 64 );
        Tank2m1.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank2m1.setPos( -3F, 7F, 8.5F );
        Tank2m2 = new ModelRenderer( this, 45, 54 );
        Tank2m2.setTexSize( 128, 64 );
        Tank2m2.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank2m2.setPos( -5F, 7F, 6.5F );
        Tank2m3 = new ModelRenderer( this, 45, 54 );
        Tank2m3.setTexSize( 128, 64 );
        Tank2m3.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank2m3.setPos( -1F, 7F, 6.5F );
        Tank2m4 = new ModelRenderer( this, 43, 46 );
        Tank2m4.setTexSize( 128, 64 );
        Tank2m4.addBox( -1.5F, -0.5F, -1.5F, 3, 1, 3);
        Tank2m4.setPos( -3F, 1.5F, 6.5F );
        Tank2m5 = new ModelRenderer( this, 38, 49 );
        Tank2m5.setTexSize( 128, 64 );
        Tank2m5.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
        Tank2m5.setPos( -3F, -0.5F, 6.5F );
        Tank2m6 = new ModelRenderer( this, 44, 44 );
        Tank2m6.setTexSize( 128, 64 );
        Tank2m6.addBox( -2F, -0.5F, -0.5F, 4, 1, 1);
        Tank2m6.setPos( -3.5F, -0.5F, 6.5F );
        Tank2m7 = new ModelRenderer( this, 36, 44 );
        Tank2m7.setTexSize( 128, 64 );
        Tank2m7.addBox( -1F, -1F, -1F, 2, 2, 2);
        Tank2m7.setPos( -5.5F, -0.5F, 6.5F );
        BCD2 = new ModelRenderer( this, 66, 51 );
        BCD2.setTexSize( 128, 64 );
        BCD2.addBox( -3.5F, -5F, -0.5F, 7, 10, 1);
        BCD2.setPos( 0F, 6.5F, 4F );
        Tank1 = new ModelRenderer( this, 41, 50 );
        Tank1.setTexSize( 128, 64 );
        Tank1.addBox( -2F, -5F, -2F, 4, 10, 4);
        Tank1.setPos( 3F, 7F, 6.5F );
        Tank1m1 = new ModelRenderer( this, 45, 54 );
        Tank1m1.setTexSize( 128, 64 );
        Tank1m1.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank1m1.setPos( 3F, 7F, 8.5F );
        Tank1m2 = new ModelRenderer( this, 45, 54 );
        Tank1m2.setTexSize( 128, 64 );
        Tank1m2.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank1m2.setPos( 1F, 7F, 6.5F );
        Tank1m3 = new ModelRenderer( this, 45, 54 );
        Tank1m3.setTexSize( 128, 64 );
        Tank1m3.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank1m3.setPos( 5F, 7F, 6.5F );
        Tank1m4 = new ModelRenderer( this, 43, 46 );
        Tank1m4.setTexSize( 128, 64 );
        Tank1m4.addBox( -1.5F, -0.5F, -1.5F, 3, 1, 3);
        Tank1m4.setPos( 3F, 1.5F, 6.5F );
        Tank1m5 = new ModelRenderer( this, 38, 49 );
        Tank1m5.setTexSize( 128, 64 );
        Tank1m5.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
        Tank1m5.setPos( 3F, -0.5F, 6.5F );
        Tank1m6 = new ModelRenderer( this, 44, 44 );
        Tank1m6.setTexSize( 128, 64 );
        Tank1m6.addBox( -2F, -0.5F, -0.5F, 4, 1, 1);
        Tank1m6.setPos( 3.5F, -0.5F, 6.5F );
        Tank1m7 = new ModelRenderer( this, 36, 44 );
        Tank1m7.setTexSize( 128, 64 );
        Tank1m7.addBox( -1F, -1F, -1F, 2, 2, 2);
        Tank1m7.setPos( 5.5F, -0.5F, 6.5F );
        BCD6 = new ModelRenderer( this, 68, 41 );
        BCD6.setTexSize( 128, 64 );
        BCD6.addBox( -0.5F, -1F, -2F, 1, 2, 4);
        BCD6.setPos( -3.6F, 10F, 0F );
        BCD5 = new ModelRenderer( this, 68, 41 );
        BCD5.setTexSize( 128, 64 );
        BCD5.addBox( -0.5F, -1F, -2F, 1, 2, 4);
        BCD5.setPos( 3.6F, 10F, 0F );
        BCD3 = new ModelRenderer( this, 91, 50 );
        BCD3.setTexSize( 128, 64 );
        BCD3.addBox( -1F, -5.5F, -0.5F, 2, 11, 1);
        BCD3.setPos( -3F, 5.5F, -2.5F );
        BCD7 = new ModelRenderer( this, 91, 45 );
        BCD7.setTexSize( 128, 64 );
        BCD7.addBox( -2F, -1F, -0.5F, 4, 2, 1);
        BCD7.setPos( 0F, 10F, -2.5F );
        BCD8 = new ModelRenderer( this, 91, 48 );
        BCD8.setTexSize( 128, 64 );
        BCD8.addBox( -2F, -0.5F, -0.5F, 4, 1, 1);
        BCD8.setPos( 0F, 3F, -2.5F );
        BCD9 = new ModelRenderer( this, 79, 42 );
        BCD9.setTexSize( 128, 64 );
        BCD9.addBox( -0.5F, -0.5F, -2F, 1, 1, 4);
        BCD9.setPos( -3.6F, 3F, 0F );
        BCD13 = new ModelRenderer( this, 91, 38 );
        BCD13.setTexSize( 128, 64 );
        BCD13.addBox( -4F, -0.5F, -0.5F, 8, 1, 1);
        BCD13.setPos( 0F, 0.5F, 2.5F );
        Mask = new ModelRenderer( this, 109, 60 );
        Mask.setTexSize( 128, 64 );
        Mask.addBox( -4F, -0.5F, -0.5F, 8, 1, 1);
        Mask.setPos( 0F, -6F, -4.5F );
        Mask2 = new ModelRenderer( this, 120, 55 );
        Mask2.setTexSize( 128, 64 );
        Mask2.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
        Mask2.setPos( -4F, -4F, -4.5F );
        Mask3 = new ModelRenderer( this, 116, 55 );
        Mask3.setTexSize( 128, 64 );
        Mask3.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
        Mask3.setPos( 4F, -4F, -4.5F );
        Mask4 = new ModelRenderer( this, 114, 51 );
        Mask4.setTexSize( 128, 64 );
        Mask4.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        Mask4.setPos( -2.5F, -2F, -4.5F );
        Mask5 = new ModelRenderer( this, 114, 53 );
        Mask5.setTexSize( 128, 64 );
        Mask5.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        Mask5.setPos( 2.5F, -2F, -4.5F );
        Mask6 = new ModelRenderer( this, 114, 49 );
        Mask6.setTexSize( 128, 64 );
        Mask6.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        Mask6.setPos( 0F, -3F, -4.5F );
        Mask7 = new ModelRenderer( this, 110, 38 );
        Mask7.setTexSize( 128, 64 );
        Mask7.addBox( -0.5F, -1F, -4F, 1, 2, 8);
        Mask7.setPos( 4F, -4.5F, 0F );
        Mask8 = new ModelRenderer( this, 110, 38 );
        Mask8.setTexSize( 128, 64 );
        Mask8.addBox( -0.5F, -1F, -4F, 1, 2, 8);
        Mask8.setPos( -4F, -4.5F, 0F );
        Mask9 = new ModelRenderer( this, 110, 35 );
        Mask9.setTexSize( 128, 64 );
        Mask9.addBox( -4F, -1F, -0.5F, 8, 2, 1);
        Mask9.setPos( 0F, -4.5F, 4F );
        mouthpiece = new ModelRenderer( this, 115, 28 );
        mouthpiece.setTexSize( 128, 64 );
        mouthpiece.addBox( -1.5F, -1.5F, -0.5F, 3, 3, 1);
        mouthpiece.setPos( 0F, 0F, -5F );
        mouthpiece2 = new ModelRenderer( this, 116, 25 );
        mouthpiece2.setTexSize( 128, 64 );
        mouthpiece2.addBox( -1F, -1F, -0.5F, 2, 2, 1);
        mouthpiece2.setPos( 0F, 0F, -5.5F );
        mouthpiece3 = new ModelRenderer( this, 116, 23 );
        mouthpiece3.setTexSize( 128, 64 );
        mouthpiece3.addBox( -1F, -0.5F, -0.5F, 2, 1, 1);
        mouthpiece3.setPos( 0F, -0.6000004F, -4F );
        head = new ModelRenderer( this, 0, 0 );
        head.setTexSize( 128, 64 );
        head.addBox( -4F, -4F, -4F, 8, 8, 8);
        head.setPos( 0F, -4F, 0F );
        head.mirror = true;
        setRotation(head, 0F, 0F, 0F);
        hose1 = new ModelRenderer( this, 117, 16 );
        hose1.setTexSize( 128, 64 );
        hose1.addBox( -0.5F, -0.5F, -0.5F, 1, 1, 1);
        hose1.setPos( 3F, -3F, 6.5F );
        hose2 = new ModelRenderer( this, 117, 16 );
        hose2.setTexSize( 128, 64 );
        hose2.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        hose2.setPos( 5F, -3F, 6.5F );
        hose3 = new ModelRenderer( this, 116, 15 );
        hose3.setTexSize( 128, 64 );
        hose3.addBox( -0.5F, -0.5F, -1F, 1, 1, 2);
        hose3.setPos( 6F, -3F, 5F );
        hose4 = new ModelRenderer( this, 106, 7 );
        hose4.setTexSize( 128, 64 );
        hose4.addBox( -0.5F, -0.5F, -10F, 1, 1, 10);
        hose4.setPos( 6F, -3F, 4.2F );
        hose5 = new ModelRenderer( this, 115, 16 );
        hose5.setTexSize( 128, 64 );
        hose5.addBox( -2.5F, -0.5F, -0.5F, 5, 1, 1);
        hose5.setPos( 4F, 0F, -5F );
        hose6 = new ModelRenderer( this, 115, 16 );
        hose6.setTexSize( 128, 64 );
        hose6.addBox( -1.5F, -0.5F, -0.5F, 3, 1, 1);
        hose6.setPos( 0F, -0.5F, 6.5F );
        head.addChild(Mask);
        head.addChild(Mask2);
        head.addChild(Mask3);
        head.addChild(Mask4);
        head.addChild(Mask5);
        head.addChild(Mask6);
        head.addChild(Mask7);
        head.addChild(Mask8);
        head.addChild(mouthpiece);
        head.addChild(mouthpiece2);
        head.addChild(mouthpiece3);
        head.addChild(hose1);
        head.addChild(hose2);
        head.addChild(hose3);
        head.addChild(hose4);
        head.addChild(hose5);
    }

    @Override
    public void prepareMobModel(LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTick) {
        showHead = !entity.getItemBySlot(EquipmentSlotType.HEAD).isEmpty() && this.slot == EquipmentSlotType.HEAD;
        showChest = !entity.getItemBySlot(EquipmentSlotType.CHEST).isEmpty() && this.slot == EquipmentSlotType.CHEST;
        showLegs = !entity.getItemBySlot(EquipmentSlotType.FEET).isEmpty() && this.slot == EquipmentSlotType.FEET;
        isSneaking = entity.getPose() == Pose.CROUCHING;
    }
    
    private void renderArmor(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, boolean glintIn, BipedModel<?> modelIn, float red, float green, float blue, ResourceLocation armorResource) {
        IVertexBuilder ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, modelIn.renderType(armorResource), false, glintIn);
        modelIn.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
     }

    @Override
    public void renderToBuffer(MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
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

    public void renderScubaGear(MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, boolean renderHead) {
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

        rightLeg.mirror = true;
        
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

    private void renderTank(MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn) {
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

    private void renderBCD(MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn) {
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
    public static void copyModelRotations(ModelRenderer source, ModelRenderer dest)
    {
        dest.xRot = source.xRot;
        dest.yRot = source.yRot;
        dest.zRot = source.zRot;
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.xRot = x;
        model.yRot = y;
        model.zRot = z;
    }
}