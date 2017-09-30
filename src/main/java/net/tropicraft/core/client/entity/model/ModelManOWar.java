package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelManOWar extends ModelBase {

    ModelRenderer Body;
    ModelRenderer CenterTent;
    ModelRenderer CenterTent2;
    ModelRenderer CenterTent3;
    ModelRenderer Tent1;
    ModelRenderer Tent2;
    ModelRenderer Tent3;
    ModelRenderer Tent4;
    public boolean isOnGround;

    public ModelManOWar(int i, int j, boolean derp) {
        isOnGround = false;
        textureWidth = 64;
        textureHeight = 32;
        setTextureOffset("Body.float", i, j);
        if (derp) {
            setTextureOffset("Body.tentbase", 0, 14);
            setTextureOffset("CenterTent.tent51", 7, 0);
            setTextureOffset("CenterTent2.tent52", 11, 0);
            setTextureOffset("CenterTent3.tent53", 11, 5);
            setTextureOffset("Tent1.tent1", 0, 0);
            setTextureOffset("Tent2.tent2", 0, 0);
            setTextureOffset("Tent3.tent3", 0, 0);
            setTextureOffset("Tent4.tent4", 0, 0);
            setTextureOffset("Body.Shape1", 32, 20);
        }
        if (!derp) {
            setTextureOffset("Body.Shape1", 15, -10);
            setTextureOffset("Body.tentbase", 32, 20);
            setTextureOffset("CenterTent.tent51", 32, 20);
            setTextureOffset("CenterTent2.tent52", 32, 20);
            setTextureOffset("CenterTent3.tent53", 32, 20);
            setTextureOffset("Tent1.tent1", 32, 20);
            setTextureOffset("Tent2.tent2", 32, 20);
            setTextureOffset("Tent3.tent3", 32, 20);
            setTextureOffset("Tent4.tent4", 32, 20);
        }

        Body = new ModelRenderer(this, "Body");
        Body.setRotationPoint(0F, 18F, 0F);
        setRotation(Body, 0F, 0F, 0F);
        Body.mirror = true;
        Body.addBox("Shape1", 0F, -6F, -2F, 0, 6, 10);
        Body.addBox("tentbase", -2F, 0F, -2F, 4, 2, 4);
        CenterTent = new ModelRenderer(this, "CenterTent");
        CenterTent.setRotationPoint(0F, 2F, 0F);
        setRotation(CenterTent, 0F, 0F, 0F);
        CenterTent.mirror = true;
        CenterTent.addBox("tent51", -0.5F, 0F, -0.5F, 1, 10, 1);
        CenterTent2 = new ModelRenderer(this, "CenterTent2");
        CenterTent2.setRotationPoint(0F, 10F, 0F);
        setRotation(CenterTent2, 0F, 0F, 0F);
        CenterTent2.mirror = true;
        CenterTent2.addBox("tent52", -0.5F, 0F, -0.5F, 1, 4, 1);
        CenterTent3 = new ModelRenderer(this, "CenterTent3");
        CenterTent3.setRotationPoint(0F, 4F, 0F);
        setRotation(CenterTent3, 0F, 0F, 0F);
        CenterTent3.mirror = true;
        CenterTent3.addBox("tent53", -0.5F, 0F, -0.5F, 1, 5, 1);
        CenterTent2.addChild(CenterTent3);
        CenterTent.addChild(CenterTent2);
        Body.addChild(CenterTent);
        Body.addBox("float", -2F, -4F, -2F, 4, 4, 8);
        Tent1 = new ModelRenderer(this, "Tent1");
        Tent1.setRotationPoint(-1.5F, 2F, -1.5F);
        setRotation(Tent1, 0F, 0F, 0F);
        Tent1.mirror = true;
        Tent1.addBox("tent1", -0.5F, 0F, -0.5F, 1, 11, 1);
        Body.addChild(Tent1);
        Tent2 = new ModelRenderer(this, "Tent2");
        Tent2.setRotationPoint(1.5F, 2F, 1.5F);
        setRotation(Tent2, 0F, 0F, 0F);
        Tent2.mirror = true;
        Tent2.addBox("tent2", -0.5F, 0F, -0.5F, 1, 11, 1);
        Body.addChild(Tent2);
        Tent3 = new ModelRenderer(this, "Tent3");
        Tent3.setRotationPoint(-1.5F, 2F, 1.5F);
        setRotation(Tent3, 0F, 0F, 0F);
        Tent3.mirror = true;
        Tent3.addBox("tent3", -0.5F, 0F, -0.5F, 1, 11, 1);
        Body.addChild(Tent3);
        Tent4 = new ModelRenderer(this, "Tent4");
        Tent4.setRotationPoint(1.5F, 2F, -1.5F);
        setRotation(Tent4, 0F, 0F, 0F);
        Tent4.mirror = true;
        Tent4.addBox("tent4", -0.5F, 0F, -0.5F, 1, 11, 1);
        Body.addChild(Tent4);

    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, null);
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
        if (isOnGround) {

            Tent3.rotateAngleZ = 0F;
            Tent3.rotateAngleX = 0F;
            Tent1.rotateAngleZ = 0F;
            Tent1.rotateAngleX = 0F;
            Tent4.rotateAngleZ = 0F;
            Tent4.rotateAngleX = 0F;
            Tent2.rotateAngleZ = 0f;
            Tent2.rotateAngleX = 0F;
            CenterTent.rotateAngleX = 0F;
            CenterTent2.rotateAngleX = 0F;
            CenterTent3.rotateAngleX = 0F;
        } else {

            Tent3.rotateAngleZ = (float) (Math.sin(f2 * .1F)) * .07F + .4F;
            Tent3.rotateAngleX = (float) (Math.sin(f2 * .1F)) * .05F + .4F;
            Tent1.rotateAngleZ = -(float) (Math.sin(f2 * .1F)) * .06F + .4F;
            Tent1.rotateAngleX = -(float) (Math.sin(f2 * .1F)) * .05F + .4F;
            Tent4.rotateAngleZ = -(float) (Math.sin(f2 * .1F)) * .06F - .4F;
            Tent4.rotateAngleX = -(float) (Math.sin(f2 * .1F)) * .04F + .4F;
            Tent2.rotateAngleZ = (float) (Math.sin(f2 * .025F)) * .05F - .4f;
            Tent2.rotateAngleX = (float) (Math.sin(f2 * .025F)) * .05F + .4F;
            CenterTent.rotateAngleX = (float) (Math.sin(f2 * .0125F)) * .05F + .2F;
            CenterTent2.rotateAngleX = (float) (Math.sin(f2 * .0125F)) * .65F + 1.507F;
            CenterTent3.rotateAngleX = Math.abs((float) (Math.sin(f2 * .0125F)) * .35F) + -1.25F;

        }
    }
}