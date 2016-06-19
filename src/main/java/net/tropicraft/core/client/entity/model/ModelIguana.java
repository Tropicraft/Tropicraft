package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

public class ModelIguana extends ModelBase {

    public ModelRenderer head;
    public ModelRenderer body;
    public ModelRenderer fLleg;
    public ModelRenderer rLleg;
    public ModelRenderer fRleg;
    public ModelRenderer rRlet;
    public ModelRenderer Back1;
    public ModelRenderer Back2;
    public ModelRenderer headTop2;
    public ModelRenderer headTop1;
    public ModelRenderer jaw;
    public ModelRenderer Back3;
    public ModelRenderer dewLap;
    public ModelRenderer tailBase;
    public ModelRenderer tailMid;
    public ModelRenderer miscPart;

    public ModelIguana() {
        head = new ModelRenderer(this, 36, 23);
        head.addBox(-2.5F, -2F, -6F, 5, 3, 6);
        head.setRotationPoint(0F, 20F, -6F);
        head.rotateAngleX = 0F;
        head.rotateAngleY = 0F;
        head.rotateAngleZ = 0F;
        head.mirror = false;
        body = new ModelRenderer(this, 0, 16);
        body.addBox(-2.5F, -1.5F, -7.5F, 5, 3, 13);
        body.setRotationPoint(0F, 21.5F, 1F);
        body.rotateAngleX = 0F;
        body.rotateAngleY = 0F;
        body.rotateAngleZ = 0F;
        body.mirror = false;
        fLleg = new ModelRenderer(this, 24, 21);
        fLleg.addBox(0F, 0F, -1.5F, 2, 3, 3);
        fLleg.setRotationPoint(2.5F, 21F, -4F);
        fLleg.rotateAngleX = 0F;
        fLleg.rotateAngleY = 0F;
        fLleg.rotateAngleZ = 0F;
        fLleg.mirror = false;
        rLleg = new ModelRenderer(this, 24, 21);
        rLleg.addBox(0F, 0F, -1.5F, 2, 3, 3);
        rLleg.setRotationPoint(2.5F, 21F, 4F);
        rLleg.rotateAngleX = 0F;
        rLleg.rotateAngleY = 0F;
        rLleg.rotateAngleZ = 0F;
        rLleg.mirror = false;
        fRleg = new ModelRenderer(this, 0, 21);
        fRleg.addBox(-2F, 0F, -1.5F, 2, 3, 3);
        fRleg.setRotationPoint(-2.5F, 21F, -4F);
        fRleg.rotateAngleX = 0F;
        fRleg.rotateAngleY = 0F;
        fRleg.rotateAngleZ = 0F;
        fRleg.mirror = false;
        rRlet = new ModelRenderer(this, 0, 21);
        rRlet.addBox(-2F, 0F, -1.5F, 2, 3, 3);
        rRlet.setRotationPoint(-2.5F, 21F, 4F);
        rRlet.rotateAngleX = 0F;
        rRlet.rotateAngleY = 0F;
        rRlet.rotateAngleZ = 0F;
        rRlet.mirror = false;
        Back1 = new ModelRenderer(this, 0, 0);
        Back1.addBox(-1.5F, -1F, 0F, 3, 1, 10);
        Back1.setRotationPoint(0F, 20F, -5F);
        Back1.rotateAngleX = 0F;
        Back1.rotateAngleY = 0F;
        Back1.rotateAngleZ = 0F;
        Back1.mirror = false;
        Back2 = new ModelRenderer(this, 32, 0);
        Back2.addBox(-0.5F, -1F, -3F, 1, 1, 6);
        Back2.setRotationPoint(0F, 19F, 0F);
        Back2.rotateAngleX = 0F;
        Back2.rotateAngleY = 0F;
        Back2.rotateAngleZ = 0F;
        Back2.mirror = false;
        headTop2 = new ModelRenderer(this, 0, 0);
        headTop2.addBox(-0.5F, -4F, -4F, 1, 1, 2);
        headTop2.setRotationPoint(0F, 20F, -6F);
        headTop2.rotateAngleX = 0F;
        headTop2.rotateAngleY = 0F;
        headTop2.rotateAngleZ = 0F;
        headTop2.mirror = false;
        headTop1 = new ModelRenderer(this, 32, 7);
        headTop1.addBox(-0.5F, -3F, -5F, 1, 1, 4);
        headTop1.setRotationPoint(0F, 20F, -6F);
        headTop1.rotateAngleX = 0F;
        headTop1.rotateAngleY = 0F;
        headTop1.rotateAngleZ = 0F;
        headTop1.mirror = false;
        jaw = new ModelRenderer(this, 0, 11);
        jaw.addBox(-1F, 1F, -4F, 2, 1, 4);
        jaw.setRotationPoint(0F, 20F, -6F);
        jaw.rotateAngleX = 0F;
        jaw.rotateAngleY = 0F;
        jaw.rotateAngleZ = 0F;
        jaw.mirror = false;
        Back3 = new ModelRenderer(this, 32, 7);
        Back3.addBox(-0.5F, 0F, -2F, 1, 1, 4);
        Back3.setRotationPoint(0F, 17F, 0F);
        Back3.rotateAngleX = 0F;
        Back3.rotateAngleY = 0F;
        Back3.rotateAngleZ = 0F;
        Back3.mirror = false;
        dewLap = new ModelRenderer(this, 0, 4);
        dewLap.addBox(-0.5F, 2F, -3F, 1, 1, 3);
        dewLap.setRotationPoint(0F, 20F, -6F);
        dewLap.rotateAngleX = 0F;
        dewLap.rotateAngleY = 0F;
        dewLap.rotateAngleZ = 0F;
        dewLap.mirror = false;
        tailBase = new ModelRenderer(this, 46, 0);
        tailBase.addBox(-1.5F, -0.5F, 0F, 3, 1, 6);
        tailBase.setRotationPoint(0F, 21.5F, 6F);
        tailBase.rotateAngleX = 0F;
        tailBase.rotateAngleY = 0F;
        tailBase.rotateAngleZ = 0F;
        tailBase.mirror = false;
        tailMid = new ModelRenderer(this, 48, 7);
        tailMid.addBox(-1F, -0.5F, 0F, 2, 1, 6);
        miscPart = new ModelRenderer(this, 52, 14);
        miscPart.addBox(-0.5F, -0.5F, 0F, 1, 1, 5);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, null);
        head.render(f5);
        body.render(f5);
        fLleg.render(f5);
        rLleg.render(f5);
        fRleg.render(f5);
        rRlet.render(f5);
        Back1.render(f5);
        Back2.render(f5);
        headTop2.render(f5);
        headTop1.render(f5);
        jaw.render(f5);
        Back3.render(f5);
        dewLap.render(f5);
        tailBase.render(f5);
        tailMid.render(f5);
        miscPart.render(f5);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entityliving, float f, float f1, float f2) {
        fRleg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.75F * f1;
        fLleg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.75F * f1;
        rRlet.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.75F * f1;
        rLleg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.75F * f1;
        tailBase.rotateAngleY = MathHelper.cos(f * 0.6662F) * .25F * f1;
        tailMid.setRotationPoint(0F - (MathHelper.cos(tailBase.rotateAngleY + 1.570796F) * 6), 21.5F, 12F + MathHelper.sin(tailBase.rotateAngleX + 3.14159F) * 6);
        tailMid.rotateAngleY = tailBase.rotateAngleY + MathHelper.cos(f * 0.6662F) * .50F * f1;
        miscPart.setRotationPoint(0F - (MathHelper.cos(tailMid.rotateAngleY + 1.570796F) * 6), 21.5F, 18F + MathHelper.sin(tailMid.rotateAngleX + 3.14159F) * 6);
        miscPart.rotateAngleY = tailMid.rotateAngleY + MathHelper.cos(f * 0.6662F) * .75F * f1;;
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
        head.rotateAngleX = f4 / 57.29578F;
        head.rotateAngleY = f3 / 57.29578F;
        jaw.rotateAngleX = head.rotateAngleX;
        jaw.rotateAngleY = head.rotateAngleY;
        headTop2.rotateAngleX = head.rotateAngleX;
        headTop2.rotateAngleY = head.rotateAngleY;
        headTop1.rotateAngleX = head.rotateAngleX;
        headTop1.rotateAngleY = head.rotateAngleY;
        dewLap.rotateAngleX = head.rotateAngleX;
        dewLap.rotateAngleY = head.rotateAngleY;
    }
}