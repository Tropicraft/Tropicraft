package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelTropiCreeper extends ModelBase {

    ModelRenderer head;
    ModelRenderer body;
    ModelRenderer leg3;
    ModelRenderer leg4;
    ModelRenderer leg1;
    ModelRenderer leg2;
    ModelRenderer hat1;
    ModelRenderer hat2;
    ModelRenderer hat3;

    public ModelTropiCreeper() {
        int i = 4;
        head = new ModelRenderer(this, 0, 0);
        head.addBox(-4F, -8F, -4F, 8, 8, 8);
        head.setRotationPoint(0F, 6F, 0F);
        head.rotateAngleX = 0F;
        head.rotateAngleY = 0F;
        head.rotateAngleZ = 0F;
        head.mirror = false;
        body = new ModelRenderer(this, 16, 16);
        body.addBox(-4F, 0F, -2F, 8, 12, 4);
        body.setRotationPoint(0F, 6F, 0F);
        body.rotateAngleX = 0F;
        body.rotateAngleY = 0F;
        body.rotateAngleZ = 0F;
        body.mirror = false;
        leg3 = new ModelRenderer(this, 0, 16);
        leg3.addBox(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg3.setRotationPoint(-2F, 12 + i, -4F);
        leg3.rotateAngleX = 0F;
        leg3.rotateAngleY = 0F;
        leg3.rotateAngleZ = 0F;
        leg3.mirror = false;
        leg4 = new ModelRenderer(this, 0, 16);
        leg4.addBox(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg4.setRotationPoint(2.0F, 12 + i, -4F);
        leg4.rotateAngleX = 0F;
        leg4.rotateAngleY = 0F;
        leg4.rotateAngleZ = 0F;
        leg4.mirror = false;
        leg1 = new ModelRenderer(this, 0, 16);
        leg1.addBox(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg1.setRotationPoint(-2F, 12 + i, 4F);
        leg1.rotateAngleX = 0F;
        leg1.rotateAngleY = 0F;
        leg1.rotateAngleZ = 0F;
        leg1.mirror = false;
        leg2 = new ModelRenderer(this, 0, 16);
        leg2.addBox(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg2.setRotationPoint(2.0F, 12 + i, 4F);
        leg2.rotateAngleX = 0F;
        leg2.rotateAngleY = 0F;
        leg2.rotateAngleZ = 0F;
        leg2.mirror = false;
        hat1 = new ModelRenderer(this, 24, 0);
        hat1.addBox(-5F, 0F, -5F, 12, 1, 6);
        hat1.setRotationPoint(-1F, -3F, -1F);
        hat1.rotateAngleX = 0F;
        hat1.rotateAngleY = 0F;
        hat1.rotateAngleZ = 0F;
        hat1.mirror = true;
        hat2 = new ModelRenderer(this, 40, 24);
        hat2.addBox(0F, 0F, 0F, 6, 2, 6);
        hat2.setRotationPoint(-3F, -5F, -3F);
        hat2.rotateAngleX = 0F;
        hat2.rotateAngleY = 0F;
        hat2.rotateAngleZ = 0F;
        hat2.mirror = false;
        hat3 = new ModelRenderer(this, 24, 0);
        hat3.addBox(-5F, 0F, 0F, 12, 1, 6);
        hat3.setRotationPoint(-1F, -3F, 0F);
        hat3.rotateAngleX = 0F;
        hat3.rotateAngleY = 0F;
        hat3.rotateAngleZ = 0F;
        hat3.mirror = false;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        head.render(f5);
        body.render(f5);
        leg3.render(f5);
        leg4.render(f5);
        leg1.render(f5);
        leg2.render(f5);
        hat1.render(f5);
        hat2.render(f5);
        hat3.render(f5);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
        head.rotateAngleY = f3 / 57.29578F;
        head.rotateAngleX = f4 / 57.29578F;
        leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        hat1.rotateAngleX = hat2.rotateAngleX = hat3.rotateAngleX = head.rotateAngleX / 6.234234234234F;
        hat1.rotateAngleY = hat2.rotateAngleY = hat3.rotateAngleY = head.rotateAngleY / 6.2342342344F;
    }
}