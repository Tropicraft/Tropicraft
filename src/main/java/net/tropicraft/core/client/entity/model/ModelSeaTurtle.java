package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelSeaTurtle extends ModelBase {

    public ModelRenderer Body;
    public ModelRenderer FRFlipper;
    public ModelRenderer FLFlipper;
    public ModelRenderer Head;
    public ModelRenderer RLFlipper;
    public ModelRenderer RRFlipper;
    public boolean inWater;

    public ModelSeaTurtle() {
        inWater = false;
        textureWidth = 64;
        textureHeight = 64;
        setTextureOffset("FRFlipper.Shape8", 0, 20);
        setTextureOffset("FLFlipper.Shape9", 0, 20);
        setTextureOffset("Body.Shape6", 0, 29);
        setTextureOffset("Body.Shape4", 43, 40);
        setTextureOffset("Body.Shape1", 0, 52);
        setTextureOffset("Body.Shape2", 0, 41);
        setTextureOffset("Body.Shape3", 0, 32);
        setTextureOffset("Body.Shape5", 44, 55);
        setTextureOffset("Body.Shape5", 44, 55);
        setTextureOffset("Body.Shape7", 0, 25);
        setTextureOffset("Head.Shape10", 0, 0);
        setTextureOffset("RLFlipper.Shape11", 0, 16);
        setTextureOffset("RRFlipper.Shape12", 0, 16);

        Body = new ModelRenderer(this, "Body");
        Body.setRotationPoint(0F, 19F, 0F);
        setRotation(Body, 0F, 0F, 0F);
        Body.mirror = true;
        FRFlipper = new ModelRenderer(this, "FRFlipper");
        FRFlipper.setRotationPoint(-7F, 2F, -6F);
        setRotation(FRFlipper, 0F, 0F, 0F);
        FRFlipper.mirror = true;
        FRFlipper.addBox("Shape8", -10F, 0F, -3F, 10, 1, 4);
        Body.addChild(FRFlipper);
        FLFlipper = new ModelRenderer(this, "FLFlipper");
        FLFlipper.setRotationPoint(7F, 2F, -6F);
        setRotation(FLFlipper, 0F, 0F, 0F);
        FLFlipper.mirror = true;
        FLFlipper.addBox("Shape9", 0F, 0F, -3F, 10, 1, 4);
        Body.addChild(FLFlipper);
        Body.addBox("Shape6", -4.5F, -1F, -9F, 9, 2, 1);
        Body.addBox("Shape4", -3F, -2F, 1F, 6, 1, 4);
        Body.addBox("Shape1", -7F, -2F, -8F, 14, 4, 8);
        Body.addBox("Shape2", -5F, -1F, 0F, 10, 3, 8);
        Body.addBox("Shape3", -4F, -2.5F, -6F, 8, 2, 7);
        Body.addBox("Shape5", -6F, -0.5F, 0F, 1, 2, 7);
        Body.addBox("Shape5", 5F, -0.5F, 0F, 1, 2, 7);
        Body.addBox("Shape7", -4F, -0.5F, 8F, 8, 2, 2);
        Head = new ModelRenderer(this, "Head");
        Head.setRotationPoint(0F, 1F, -8F);
        setRotation(Head, 0F, 0F, 0F);
        Head.mirror = true;
        Head.addBox("Shape10", -1.5F, -1.5F, -6F, 3, 3, 6);
        Body.addChild(Head);
        RLFlipper = new ModelRenderer(this, "RLFlipper");
        RLFlipper.setRotationPoint(-4F, 2F, 7F);
        setRotation(RLFlipper, 0F, 0F, 0F);
        RLFlipper.mirror = true;
        RLFlipper.addBox("Shape11", -7F, 0F, -1F, 7, 1, 3);
        Body.addChild(RLFlipper);
        RRFlipper = new ModelRenderer(this, "RRFlipper");
        RRFlipper.setRotationPoint(4F, 2F, 7F);
        setRotation(RRFlipper, 0F, 0F, 0F);
        RRFlipper.mirror = true;
        RRFlipper.addBox("Shape12", -1F, 0F, -1F, 7, 1, 3);
        Body.addChild(RRFlipper);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
    	
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        float swimSpeed = 0.05f;
        float swimDist = 0.5f;
        
        if(entity.isInWater()) {
	        FLFlipper.rotateAngleY = (float)Math.cos(f2*swimSpeed)*swimDist;
	        FRFlipper.rotateAngleY = (float)Math.cos(f2*swimSpeed)*swimDist;
	        RLFlipper.rotateAngleY = (float)-Math.cos(f2*swimSpeed)*swimDist;
	        RRFlipper.rotateAngleY = (float)-Math.cos(f2*swimSpeed)*swimDist;
        }

        Body.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
        Head.rotateAngleX = f4 / 125F;
        Head.rotateAngleY = f3 / 125F;
    }
}