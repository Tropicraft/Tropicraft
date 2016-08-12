package net.tropicraft.core.client.entity.model;

import java.util.Random;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;

public class ModelVMonkey extends ModelBase {

    public ModelRenderer body;
    public ModelRenderer lLegUpper;
    public ModelRenderer rLegUpper;
    public ModelRenderer rArmUpper;
    public ModelRenderer lArmUpper;
    public ModelRenderer tailBase;
    public ModelRenderer tailMid;
    public ModelRenderer tailTop;
    public ModelRenderer rArmLower;
    public ModelRenderer lArmLower;
    public ModelRenderer lLegLower;
    public ModelRenderer rLegLower;
    public ModelRenderer Face;
    public ModelRenderer Head;
    protected Random rand;
    public float herps;

    public ModelVMonkey() {

        body = new ModelRenderer(this, 0, 8);
        body.addBox(-1F, -2F, -4F, 2, 4, 9, 0F);
        body.setRotationPoint(0F, 16F, 0F);
        body.rotateAngleX = 0F;
        body.rotateAngleY = 3.141593F;
        body.rotateAngleZ = 0F;
        body.mirror = false;
        lLegUpper = new ModelRenderer(this, 7, 0);
        lLegUpper.addBox(-1F, 0F, -0.5F, 1, 5, 1, 0F);
        lLegUpper.setRotationPoint(-1F, 14F, -3.5F);
        lLegUpper.rotateAngleX = 0F;
        lLegUpper.rotateAngleY = 0F;
        lLegUpper.rotateAngleZ = 0F;
        lLegUpper.mirror = false;
        rLegUpper = new ModelRenderer(this, 0, 0);
        rLegUpper.addBox(0F, 0F, -0.5F, 1, 5, 1, 0F);
        rLegUpper.setRotationPoint(1F, 14F, -3.5F);
        rLegUpper.rotateAngleX = 0F;
        rLegUpper.rotateAngleY = 0F;
        rLegUpper.rotateAngleZ = 0F;
        rLegUpper.mirror = false;
        rArmUpper = new ModelRenderer(this, 0, 0);
        rArmUpper.addBox(0F, 0F, -0.5F, 1, 5, 1, 0F);
        rArmUpper.setRotationPoint(1F, 14F, 3.5F);
        rArmUpper.rotateAngleX = 0F;
        rArmUpper.rotateAngleY = 0F;
        rArmUpper.rotateAngleZ = 0F;
        rArmUpper.mirror = false;
        lArmUpper = new ModelRenderer(this, 7, 0);
        lArmUpper.addBox(-1F, 0F, -0.5F, 1, 5, 1, 0F);
        lArmUpper.setRotationPoint(-1F, 14F, 3.5F);
        lArmUpper.rotateAngleX = 0F;
        lArmUpper.rotateAngleY = 0F;
        lArmUpper.rotateAngleZ = 0F;
        lArmUpper.mirror = false;
        tailBase = new ModelRenderer(this, 20, 27);
        tailBase.addBox(-0.5F, -4F, -0.5F, 1, 3, 1, 0F);
        tailBase.setRotationPoint(0F, 15F, 3.5F);
        tailBase.rotateAngleX = 0F;
        tailBase.rotateAngleY = 3.141593F;
        tailBase.rotateAngleZ = 0F;
        tailBase.mirror = false;
        tailMid = new ModelRenderer(this, 20, 24);
        tailMid.addBox(-0.5F, -2F, -0.5F, 1, 2, 1, 0F);
        tailMid.setRotationPoint(0F, 11F, 3.5F);
        tailMid.rotateAngleX = 0F;
        tailMid.rotateAngleY = 3.141593F;
        tailMid.rotateAngleZ = 0F;
        tailMid.mirror = false;
        tailTop = new ModelRenderer(this, 20, 21);
        tailTop.addBox(-0.5F, -2F, -0.5F, 1, 2, 1, 0F);
        tailTop.setRotationPoint(0F, 9F, 3.5F);
        tailTop.rotateAngleX = 0F;
        tailTop.rotateAngleY = 3.141593F;
        tailTop.rotateAngleZ = 0F;
        tailTop.mirror = false;
        rArmLower = new ModelRenderer(this, 0, 7);
        rArmLower.addBox(0F, 0F, -0.5F, 1, 5, 1, 0F);
        rArmLower.setRotationPoint(1F, 19F, 3.5F);
        rArmLower.rotateAngleX = 0F;
        rArmLower.rotateAngleY = 0F;
        rArmLower.rotateAngleZ = 0F;
        rArmLower.mirror = false;
        lArmLower = new ModelRenderer(this, 12, 0);
        lArmLower.addBox(-1F, 0F, -0.5F, 1, 5, 1, 0F);
        lArmLower.setRotationPoint(-1F, 19F, 3.5F);
        lArmLower.rotateAngleX = 0F;
        lArmLower.rotateAngleY = 0F;
        lArmLower.rotateAngleZ = 0F;
        lArmLower.mirror = false;
        lLegLower = new ModelRenderer(this, 12, 0);
        lLegLower.addBox(-1F, 0F, -0.5F, 1, 5, 1, 0F);
        lLegLower.setRotationPoint(-1F, 19F, -3.5F);
        lLegLower.rotateAngleX = 0F;
        lLegLower.rotateAngleY = 0F;
        lLegLower.rotateAngleZ = 0F;
        lLegLower.mirror = false;
        rLegLower = new ModelRenderer(this, 0, 7);
        rLegLower.addBox(0F, 0F, -0.5F, 1, 5, 1, 0F);
        rLegLower.setRotationPoint(1F, 19F, -3.5F);
        rLegLower.rotateAngleX = 0F;
        rLegLower.rotateAngleY = 0F;
        rLegLower.rotateAngleZ = 0F;
        rLegLower.mirror = false;
        Face = new ModelRenderer(this, 0, 25);
        Face.addBox(-2F, -1F, 0F, 4, 4, 3, 0F);
        Face.setRotationPoint(0F, 15F, -5F);
        Face.rotateAngleX = 0F;
        Face.rotateAngleY = 3.141593F;
        Face.rotateAngleZ = 0F;
        Face.mirror = false;
        Head = new ModelRenderer(this, 25, 25);
        Head.addBox(-3F, -2F, 0F, 6, 5, 2, 0F);
        Head.setRotationPoint(0F, 15F, -5F);
        Head.rotateAngleX = 0F;
        Head.rotateAngleY = 3.141593F;
        Head.rotateAngleZ = 0F;
        Head.mirror = false;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        body.render(f5);
        lLegUpper.render(f5);
        rLegUpper.render(f5);
        rArmUpper.render(f5);
        lArmUpper.render(f5);
        tailBase.render(f5);
        tailMid.render(f5);
        tailTop.render(f5);
        rArmLower.render(f5);
        lArmLower.render(f5);
        lLegLower.render(f5);
        rLegLower.render(f5);
        Face.render(f5);
        Head.render(f5);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
        Face.rotateAngleX = f4 / 57.29578F + herps;
        Face.rotateAngleY = f3 / 57.29578F + 3.141593F;
        Head.rotateAngleX = Face.rotateAngleX;
        Head.rotateAngleY = Face.rotateAngleY;
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entityliving, float f, float f1, float f2) {
        EntityVMonkey entityvmonkey = (EntityVMonkey) entityliving;

        if (entityvmonkey.isSitting()) {

            body.setRotationPoint(0F, 20F, 0F);
            body.rotateAngleX = 0.9320058F;
            body.rotateAngleY = 3.141593F;
            lLegUpper.setRotationPoint(-1F, 16F, -1.5F);
            lLegUpper.rotateAngleX = -0.2792527F;
            rLegUpper.setRotationPoint(1F, 16F, -1.5F);
            rLegUpper.rotateAngleX = -0.2792527F;
            rLegUpper.rotateAngleY = 0.005817764F;
            rArmUpper.setRotationPoint(1F, 22F, 3.5F);
            rArmUpper.rotateAngleX = -2.142101F;
            lArmUpper.setRotationPoint(-1F, 22F, 3.5F);
            lArmUpper.rotateAngleX = -2.142043F;
            tailBase.setRotationPoint(0F, 22F, 2.466667F);
            tailBase.rotateAngleX = 1.902409F;
            tailBase.rotateAngleY = 3.141593F;
            tailMid.setRotationPoint(0F, 23.3F, 5.966667F);
            tailMid.rotateAngleX = 1.570796F;
            tailMid.rotateAngleY = 2.111848F;
            tailMid.rotateAngleZ = -0.2617994F;
            tailTop.setRotationPoint(-1F, 23.2F, 7F);
            tailTop.rotateAngleX = 1.570796F;
            tailTop.rotateAngleY = 0.8377581F;
            tailTop.rotateAngleZ = 0.01745329F;
            rArmLower.setRotationPoint(1F, 19F, -0.5F);
            rArmLower.rotateAngleX = -0.1489348F;
            lArmLower.setRotationPoint(-1F, 19F, -0.3F);
            lArmLower.rotateAngleX = -0.1492257F;
            lLegLower.setRotationPoint(-1F, 21F, -2.8F);
            lLegLower.rotateAngleX = -0.9599311F;
            rLegLower.setRotationPoint(1F, 21F, -2.833333F);
            rLegLower.rotateAngleX = -0.9599311F;
            Face.setRotationPoint(0F, 15F, -3F);
            Head.setRotationPoint(0F, 15F, -3F);
            herps = 0;

        } else if (entityvmonkey.isClimbing) {

            body.rotateAngleX = 1.570796F;
            body.setRotationPoint(0F, 16F, 0F);
            lLegUpper.setRotationPoint(-1F, 12F, 2F);
            //lLegUpper.rotateAngleX = -1.570796F;	
            rLegUpper.setRotationPoint(1F, 12F, 2F);
            //rLegUpper.rotateAngleX = -1.570796F;		
            rArmUpper.setRotationPoint(1F, 19.5F, 2F);
            //rArmUpper.rotateAngleX = -1.570796F;		
            lArmUpper.setRotationPoint(-1F, 19.5F, 2F);
            //lArmUpper.rotateAngleX = -1.570796F;		
            tailBase.setRotationPoint(0F, 19.5F, 0.5F);
            tailBase.rotateAngleX = 1.570796F;
            tailBase.rotateAngleY = 3.141593F;
            tailMid.setRotationPoint(0F, 19.5F, 4.5F);

            tailMid.rotateAngleX = 1.570796F;
            tailMid.rotateAngleY = 3.141593F;
            tailTop.setRotationPoint(0F, 19.5F, 6.5F);
            tailTop.rotateAngleX = 1.570796F;
            tailTop.rotateAngleY = 3.141593F;
            rArmLower.setRotationPoint(1F, 19.5F, -3F);
            //rArmLower.rotateAngleX = -0.6981317F;
            lArmLower.setRotationPoint(-1F, 19.5F, -3F);
            //lArmLower.rotateAngleX = -0.6981317F;		
            lLegLower.setRotationPoint(-1F, 12F, -3F);
            //lLegLower.rotateAngleX = -2.443461F;	
            rLegLower.setRotationPoint(1F, 12F, -3F);
            //rLegLower.rotateAngleX = -2.443461F;		
            Face.setRotationPoint(0F, 11F, 1F);
            herps = 1.570796F;
            Head.setRotationPoint(0F, 11F, 1F);
            Head.rotateAngleX = 1.570796F;


            rLegUpper.rotateAngleX = MathHelper.cos(f * .5F) * .75F * f2 - 1.570796F;
            rArmUpper.rotateAngleX = MathHelper.cos(f * .5F) * .75F * f2 - 1.570796F;
            lArmUpper.rotateAngleX = MathHelper.cos(f * .5F) * .75F * f2 - 1.570796F;
            lLegUpper.rotateAngleX = MathHelper.cos(f * .5F) * .75F * f2 - 1.570796F;
            rLegLower.setRotationPoint(1F, 12F + (MathHelper.cos(rLegUpper.rotateAngleX) * 5), -3F - (5 + MathHelper.sin(rLegUpper.rotateAngleX) * 5));
            rArmLower.setRotationPoint(1F, 19.5F + (MathHelper.cos(rArmUpper.rotateAngleX) * 5), -3F - (5 + MathHelper.sin(rArmUpper.rotateAngleX) * 5));
            lArmLower.setRotationPoint(-1F, 19.5F + (MathHelper.cos(lArmUpper.rotateAngleX) * 5), -3F - (5 + MathHelper.sin(lArmUpper.rotateAngleX) * 5));
            lLegLower.setRotationPoint(-1F, 12F + (MathHelper.cos(lLegUpper.rotateAngleX) * 5), -3F - (5 + MathHelper.sin(lLegUpper.rotateAngleX) * 5));
            rLegLower.rotateAngleX = rLegUpper.rotateAngleX - 0.6981317F;
            rArmLower.rotateAngleX = rArmUpper.rotateAngleX + 0.6981317F;
            lLegLower.rotateAngleX = lLegUpper.rotateAngleX - 0.6981317F;
            lArmLower.rotateAngleX = lArmUpper.rotateAngleX + 0.6981317F;
        } else {
            body.setRotationPoint(0F, 16F, 0F);
            body.rotateAngleY = 3.141593F;
            body.rotateAngleX = 0F;
            lLegUpper.setRotationPoint(-1F, 14F, -3.5F);
            rLegUpper.setRotationPoint(1F, 14F, -3.5F);
            rArmUpper.setRotationPoint(1F, 14F, 3.5F);
            lArmUpper.setRotationPoint(-1F, 14F, 3.5F);
            tailBase.setRotationPoint(0F, 15F, 3.5F);
            tailBase.rotateAngleX = 0F;
            tailBase.rotateAngleY = 3.141593F;
            tailBase.rotateAngleZ = 0F;
            tailMid.setRotationPoint(0F, 11F, 3.5F);
            tailMid.rotateAngleX = 0F;
            tailMid.rotateAngleY = 3.141593F;
            tailMid.rotateAngleZ = 0F;
            tailTop.setRotationPoint(0F, 9F, 3.5F);
            tailTop.rotateAngleX = 0F;
            tailTop.rotateAngleY = 3.141593F;
            tailTop.rotateAngleZ = 0F;
            Face.setRotationPoint(0F, 15F, -5F);
            Head.setRotationPoint(0F, 15F, -5F);

            rLegUpper.rotateAngleX = MathHelper.cos(f * 0.6662F) * .75F * f1;
            rArmUpper.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * .75F * f1;
            lLegUpper.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * .75F * f1;
            lArmUpper.rotateAngleX = MathHelper.cos(f * 0.6662F) * .75F * f1;
            rLegLower.setRotationPoint(1F, 19F - (5 - MathHelper.sin(rLegUpper.rotateAngleX + 1.570796327F) * 5), -3.5F - (MathHelper.cos(rLegUpper.rotateAngleX + 1.570796327F) * 5));
            rArmLower.setRotationPoint(1F, 19F - (5 - MathHelper.sin(rArmUpper.rotateAngleX + 1.570796327F) * 5), 3.5F - (MathHelper.cos(rArmUpper.rotateAngleX + 1.570796327F) * 5));
            lArmLower.setRotationPoint(-1F, 19F - (5 - MathHelper.sin(lArmUpper.rotateAngleX + 1.570796327F) * 5), 3.5F - (MathHelper.cos(lArmUpper.rotateAngleX + 1.570796327F) * 5));
            lLegLower.setRotationPoint(-1F, 19F - (5 - MathHelper.sin(lLegUpper.rotateAngleX + 1.570796327F) * 5), -3.5F - (MathHelper.cos(lLegUpper.rotateAngleX + 1.570796327F) * 5));
            rLegLower.rotateAngleX = rLegUpper.rotateAngleX;
            rArmLower.rotateAngleX = rArmUpper.rotateAngleX;
            lLegLower.rotateAngleX = lLegUpper.rotateAngleX;
            lArmLower.rotateAngleX = lArmUpper.rotateAngleX;

            tailBase.rotateAngleX = MathHelper.cos(f * 0.6662F) * .50F * f1;
            tailBase.rotateAngleZ = MathHelper.cos(f * 0.6662F) * .50F * f1;
            tailMid.setRotationPoint(0F - (MathHelper.cos(tailBase.rotateAngleZ + ((float) Math.PI) / 2F) * 3), 11F + (3 - MathHelper.sin(tailBase.rotateAngleX + ((float) Math.PI) / 2F) * 3), 3.5F - (MathHelper.cos(tailBase.rotateAngleX + ((float) Math.PI) / 2F) * 3));
            tailMid.rotateAngleX = tailBase.rotateAngleX + MathHelper.cos(f * 0.6662F) * .75F * f1;
            tailMid.rotateAngleZ = tailBase.rotateAngleZ + MathHelper.cos(f * 0.6662F) * .75F * f1;
            tailTop.setRotationPoint(0F - (MathHelper.cos(tailMid.rotateAngleZ + ((float) Math.PI) / 2F) * 2), 9F + (2 - MathHelper.sin(tailMid.rotateAngleX + ((float) Math.PI) / 2F) * 2), 3.5F - (MathHelper.cos(tailMid.rotateAngleX + ((float) Math.PI) / 2F) * 2));
            tailTop.rotateAngleX = tailMid.rotateAngleX + MathHelper.cos(f * 0.6662F) * 1.75F * f1;
            tailTop.rotateAngleZ = tailMid.rotateAngleX + MathHelper.cos(f * 0.6662F) * 1.75F * f1;
            herps = 0;
        }

    }
}
