package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelBeachFloat extends ModelBase {

    public ModelRenderer floatCross4;
    public ModelRenderer floatCross3;
    public ModelRenderer floatCross2;
    public ModelRenderer floatCross1;
    public ModelRenderer topFloatCross4;
    public ModelRenderer topFloatCross3;
    public ModelRenderer topFloatCross2;
    public ModelRenderer topFloatCross1;
    public ModelRenderer floatFoot;
    public ModelRenderer floatTop;
    public ModelRenderer headPillow;
    public ModelRenderer topBed;
    public ModelRenderer bottomBed;

    public ModelBeachFloat() {
        floatCross4 = new ModelRenderer(this, 0, 0);
        floatCross4.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        floatCross4.setRotationPoint(0F, 23F, -6F);

        floatCross4.rotateAngleX = 0F;
        floatCross4.rotateAngleY = 0F;
        floatCross4.rotateAngleZ = 0F;
        floatCross4.mirror = false;

        floatCross3 = new ModelRenderer(this, 0, 0);
        floatCross3.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        floatCross3.setRotationPoint(0F, 23F, -2F);

        floatCross3.rotateAngleX = 0F;
        floatCross3.rotateAngleY = 0F;
        floatCross3.rotateAngleZ = 0F;
        floatCross3.mirror = false;

        floatCross2 = new ModelRenderer(this, 0, 0);
        floatCross2.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        floatCross2.setRotationPoint(0F, 23F, 2F);

        floatCross2.rotateAngleX = 0F;
        floatCross2.rotateAngleY = 0F;
        floatCross2.rotateAngleZ = 0F;
        floatCross2.mirror = false;

        floatCross1 = new ModelRenderer(this, 0, 0);
        floatCross1.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        floatCross1.setRotationPoint(0F, 23F, 6F);

        floatCross1.rotateAngleX = 0F;
        floatCross1.rotateAngleY = 0F;
        floatCross1.rotateAngleZ = 0F;
        floatCross1.mirror = false;

        topFloatCross4 = new ModelRenderer(this, 0, 0);
        topFloatCross4.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        topFloatCross4.setRotationPoint(0F, 23F, -6F);

        topFloatCross4.rotateAngleX = 0F;
        topFloatCross4.rotateAngleY = 0F;
        topFloatCross4.rotateAngleZ = 3.141593F;
        topFloatCross4.mirror = false;

        topFloatCross3 = new ModelRenderer(this, 0, 0);
        topFloatCross3.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        topFloatCross3.setRotationPoint(0F, 23F, -2F);

        topFloatCross3.rotateAngleX = 0F;
        topFloatCross3.rotateAngleY = 0F;
        topFloatCross3.rotateAngleZ = 3.141593F;
        topFloatCross3.mirror = false;

        topFloatCross2 = new ModelRenderer(this, 0, 0);
        topFloatCross2.addBox(0F, 0F, 1F, 16, 2, 2, 0F);
        topFloatCross2.setRotationPoint(0F, 24F, 0F);

        topFloatCross2.rotateAngleX = 0F;
        topFloatCross2.rotateAngleY = 0F;
        topFloatCross2.rotateAngleZ = 3.141593F;
        topFloatCross2.mirror = false;

        topFloatCross1 = new ModelRenderer(this, 0, 0);
        topFloatCross1.addBox(0F, -1F, -1F, 16, 2, 2, 0F);
        topFloatCross1.setRotationPoint(0F, 23F, 6F);

        topFloatCross1.rotateAngleX = 0F;
        topFloatCross1.rotateAngleY = 0F;
        topFloatCross1.rotateAngleZ = 3.141593F;
        topFloatCross1.mirror = false;

        floatFoot = new ModelRenderer(this, 0, 4);
        floatFoot.addBox(-7F, -1F, 0F, 14, 2, 2, 0F);
        floatFoot.setRotationPoint(16F, 23F, 0F);

        floatFoot.rotateAngleX = 0F;
        floatFoot.rotateAngleY = 1.570796F;
        floatFoot.rotateAngleZ = 0F;
        floatFoot.mirror = false;

        floatTop = new ModelRenderer(this, 0, 4);
        floatTop.addBox(-7F, -1F, 0F, 14, 2, 2, 0F);
        floatTop.setRotationPoint(-17F, 24F, 0F);

        floatTop.rotateAngleX = 1.570796F;
        floatTop.rotateAngleY = -1.570796F;
        floatTop.rotateAngleZ = 0F;
        floatTop.mirror = false;

        headPillow = new ModelRenderer(this, 0, 13);
        headPillow.addBox(-6F, -1.5F, -4F, 12, 2, 4, 0F);
        headPillow.setRotationPoint(-12F, 22F, 0F);

        headPillow.rotateAngleX = 0F;
        headPillow.rotateAngleY = 1.570796F;
        headPillow.rotateAngleZ = 0F;
        headPillow.mirror = false;

        topBed = new ModelRenderer(this, 0, 19);
        topBed.addBox(-6F, -0.5F, -6F, 14, 1, 12, 0F);
        topBed.setRotationPoint(-6F, 22F, 0F);

        topBed.rotateAngleX = 0F;
        topBed.rotateAngleY = 0F;
        topBed.rotateAngleZ = 0F;
        topBed.mirror = false;

        bottomBed = new ModelRenderer(this, 0, 19);
        bottomBed.addBox(-6F, -0.5F, -6F, 14, 1, 12, 0F);
        bottomBed.setRotationPoint(8F, 22F, 0F);

        bottomBed.rotateAngleX = 0F;
        bottomBed.rotateAngleY = 0F;
        bottomBed.rotateAngleZ = 0F;
        bottomBed.mirror = false;


    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-90, 0, 1, 0);
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        floatCross4.render(f5);
        floatCross3.render(f5);
        floatCross2.render(f5);
        floatCross1.render(f5);
        topFloatCross4.render(f5);
        topFloatCross3.render(f5);
        topFloatCross2.render(f5);
        topFloatCross1.render(f5);
        floatFoot.render(f5);
        floatTop.render(f5);
        headPillow.render(f5);
        topBed.render(f5);
        bottomBed.render(f5);
        GlStateManager.popMatrix();
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
    }
}