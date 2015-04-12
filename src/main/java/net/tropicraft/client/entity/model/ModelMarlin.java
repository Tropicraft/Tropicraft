package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMarlin extends ModelBase {

    ModelRenderer Body;
    ModelRenderer DorsalFin1;
    ModelRenderer LeftFin;
    ModelRenderer RightFin;
    ModelRenderer BottomFIn;
    ModelRenderer head;
    ModelRenderer tail;
    ModelRenderer tail1;
    ModelRenderer sword;
    ModelRenderer tail3;
    ModelRenderer tailEndB;
    ModelRenderer tailEndT;
    public boolean inWater;

    public ModelMarlin() {
        textureWidth = 64;
        textureHeight = 32;
        setTextureOffset("head.Head", 46, 24);
        setTextureOffset("head.Nose2", 28, 0);
        setTextureOffset("head.Nose3", 22, 0);
        setTextureOffset("head.DorsalFin2", 23, 24);
        setTextureOffset("sword.Nose1", 0, 0);
        setTextureOffset("tail.TailBase", 0, 13);
        setTextureOffset("tail1.TailMid", 0, 5);
        setTextureOffset("tail3.TailEnd", 46, 0);
        setTextureOffset("tailEndB.TailFinBottom", 40, 0);
        setTextureOffset("tailEndT.TailFinTop", 34, 0);

        Body = new ModelRenderer(this, 0, 22);
        Body.addBox(-5F, -3F, -2F, 7, 6, 4);
        Body.setRotationPoint(0F, 19F, 0F);
        Body.setTextureSize(64, 32);
        Body.mirror = true;
        setRotation(Body, 0F, -1.570796F, 0F);
        DorsalFin1 = new ModelRenderer(this, 24, 20);
        DorsalFin1.addBox(-0.5F, -0.5F, -0.5F, 1, 2, 10);
        DorsalFin1.setRotationPoint(0F, 15.5F, -5F);
        DorsalFin1.setTextureSize(64, 32);
        DorsalFin1.mirror = true;
        setRotation(DorsalFin1, 0F, 0F, 0F);
        LeftFin = new ModelRenderer(this, 12, 10);
        LeftFin.addBox(0F, -0.5F, -2F, 4, 1, 2);
        LeftFin.setRotationPoint(2F, 21F, -3F);
        LeftFin.setTextureSize(64, 32);
        LeftFin.mirror = true;
        setRotation(LeftFin, 0F, 0F, 0F);
        RightFin = new ModelRenderer(this, 12, 7);
        RightFin.addBox(-4F, -0.5F, -2F, 4, 1, 2);
        RightFin.setRotationPoint(-2F, 21F, -3F);
        RightFin.setTextureSize(64, 32);
        RightFin.mirror = true;
        setRotation(RightFin, 0F, 0F, 0F);
        BottomFIn = new ModelRenderer(this, 52, 0);
        BottomFIn.addBox(-0.5F, 2F, -2.5F, 1, 3, 2);
        BottomFIn.setRotationPoint(0F, 19F, 0F);
        BottomFIn.setTextureSize(64, 32);
        BottomFIn.mirror = true;
        setRotation(BottomFIn, 0.6981317F, 0F, 0F);
        head = new ModelRenderer(this, "head");
        head.setRotationPoint(0F, 20F, -5F);
        setRotation(head, 0F, 0F, 0F);
        head.mirror = true;
        head.addBox("Head", -1.5F, -3F, -3F, 3, 5, 3);
        head.addBox("Nose2", -1F, -1.5F, -4F, 2, 3, 1);
        head.addBox("Nose3", -0.5F, -0.5F, -6F, 1, 2, 2);
        head.addBox("DorsalFin2", -0.5F, -6F, -2.5F, 1, 3, 2);
        sword = new ModelRenderer(this, "sword");
        sword.setRotationPoint(0F, 0F, 0F);
        setRotation(sword, 0F, 1.5707F, 0F);
        sword.mirror = true;
        sword.addBox("Nose1", 4F, -1.5F, -0.5F, 10, 1, 1);
        head.addChild(sword);
        tail = new ModelRenderer(this, "tail");
        tail.setRotationPoint(0F, 19F, 2F);
        setRotation(tail, 0F, 0F, 0F);
        tail.mirror = true;
        tail.addBox("TailBase", -1.5F, -2F, 0F, 3, 5, 4);
        tail1 = new ModelRenderer(this, "tail1");
        tail1.setRotationPoint(0F, 0F, 4F);
        setRotation(tail1, 0F, 0F, 0F);
        tail1.mirror = true;
        tail1.addBox("TailMid", -1F, -1.5F, 0F, 2, 4, 4);
        tail3 = new ModelRenderer(this, "tail3");
        tail3.setRotationPoint(0F, 1F, 4F);
        setRotation(tail3, 0F, 0F, 0F);
        tail3.mirror = true;
        tail3.addBox("TailEnd", -0.5F, -1.5F, 0F, 1, 3, 2);
        tailEndB = new ModelRenderer(this, "tailEndB");
        tailEndB.setRotationPoint(0F, 0F, 0F);
        setRotation(tailEndB, 0.593411F, 0F, 0F);
        tailEndB.mirror = true;
        tailEndB.addBox("TailFinBottom", -0.5F, 1F, -1F, 1, 5, 2);
        tail3.addChild(tailEndB);
        tailEndT = new ModelRenderer(this, "tailEndT");
        tailEndT.setRotationPoint(0F, 0F, 0F);
        setRotation(tailEndT, 2.548179F, 0F, 0F);
        tailEndT.mirror = true;
        tailEndT.addBox("TailFinTop", -0.5F, 1F, -1F, 1, 5, 2);
        tail3.addChild(tailEndT);
        tail1.addChild(tail3);
        tail.addChild(tail1);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Body.render(f5);
        DorsalFin1.render(f5);
        LeftFin.render(f5);
        RightFin.render(f5);
        BottomFIn.render(f5);
        head.render(f5);
        tail.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        
        float angle = f2 * 3.14159F / 180F;
        //System.out.println(angle);
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
        //System.out.println("f = " + f);
        //System.out.println("f1 = " + f1);

        // head.rotateAngleZ += MathHelper.sin(f2 * 0.25F) * 0.0020F;

        if (!inWater) {
            head.rotateAngleY = (float) (Math.sin(f2 * .55F)) * .260F;
            tail.rotateAngleY = (float) (Math.sin(f2 * .55F)) * .260F;
            tail1.rotateAngleY = (float) Math.sin(f2 * .55F) * .260F;
            tail3.rotateAngleY = (float) Math.sin(f2 * .55F) * .260F;
            LeftFin.rotateAngleZ = (float) (Math.sin(f2 * .25F)) * .165F + 0.523598F;
            RightFin.rotateAngleZ = -(float) (Math.sin(f2 * .25F)) * .165F - 0.523598F;
            LeftFin.rotateAngleY = -1.5F;
            RightFin.rotateAngleY = 1.5F - (float) (Math.sin(f2 * .25F)) * .165F - 0.523598F;
        } else {
            head.rotateAngleY = (float) (Math.sin(f2 * .25F)) * .135F;
            tail.rotateAngleY = (float) (Math.sin(f2 * .25F)) * .135F;
            tail1.rotateAngleY = (float) Math.sin(f2 * .35F) * .150F;
            tail3.rotateAngleY = (float) Math.sin(f2 * .45F) * .160F;
            LeftFin.rotateAngleZ = (float) (Math.sin(f2 * .25F)) * .165F + 0.523598F;
            RightFin.rotateAngleZ = -(float) (Math.sin(f2 * .25F)) * .165F - 0.523598F;
            LeftFin.rotateAngleY = -0.392699F;
            RightFin.rotateAngleY = 0.392699F;
        }
        //System.out.println("f2 = " + f2);
        //System.out.println("f3 = " + f3);
        //System.out.println("f4 = " + f4);
        //System.out.println("f5 = " + f5);
    }
}
