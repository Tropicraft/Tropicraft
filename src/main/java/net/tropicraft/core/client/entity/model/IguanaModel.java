package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.entity.neutral.IguanaEntity;

public class IguanaModel extends EntityModel<IguanaEntity> {
    public RendererModel head;
    public RendererModel headTop1;
    public RendererModel headTop2;
    public RendererModel body;
    public RendererModel fLleg;
    public RendererModel rLleg;
    public RendererModel fRleg;
    public RendererModel rRleg;
    public RendererModel back1;
    public RendererModel back2;
    public RendererModel back3;
    public RendererModel jaw;
    public RendererModel dewLap;
    public RendererModel tailBase;
    public RendererModel tailMid;
    public RendererModel miscPart;

    public IguanaModel() {
        head = new RendererModel(this, 36, 23);
        head.addBox(-2.5F, -2F, -6F, 5, 3, 6);
        head.setRotationPoint(0F, 20F, -6F);

        body = new RendererModel(this, 0, 16);
        body.addBox(-2.5F, -1.5F, -7.5F, 5, 3, 13);
        body.setRotationPoint(0F, 21.5F, 1F);

        fLleg = new RendererModel(this, 24, 21);
        fLleg.addBox(0F, 0F, -1.5F, 2, 3, 3);
        fLleg.setRotationPoint(2.5F, 21F, -4F);

        rLleg = new RendererModel(this, 24, 21);
        rLleg.addBox(0F, 0F, -1.5F, 2, 3, 3);
        rLleg.setRotationPoint(2.5F, 21F, 4F);

        fRleg = new RendererModel(this, 0, 21);
        fRleg.addBox(-2F, 0F, -1.5F, 2, 3, 3);
        fRleg.setRotationPoint(-2.5F, 21F, -4F);

        rRleg = new RendererModel(this, 0, 21);
        rRleg.addBox(-2F, 0F, -1.5F, 2, 3, 3);
        rRleg.setRotationPoint(-2.5F, 21F, 4F);

        back1 = new RendererModel(this, 0, 0);
        back1.addBox(-1.5F, -1F, 0F, 3, 1, 10);
        back1.setRotationPoint(0F, 20F, -5F);

        back2 = new RendererModel(this, 32, 0);
        back2.addBox(-0.5F, -1F, -3F, 1, 1, 6);
        back2.setRotationPoint(0F, 19F, 0F);

        headTop2 = new RendererModel(this, 0, 0);
        headTop2.addBox(-0.5F, -4F, -4F, 1, 1, 2);
        headTop2.setRotationPoint(0F, 20F, -6F);

        headTop1 = new RendererModel(this, 32, 7);
        headTop1.addBox(-0.5F, -3F, -5F, 1, 1, 4);
        headTop1.setRotationPoint(0F, 20F, -6F);

        jaw = new RendererModel(this, 0, 11);
        jaw.addBox(-1F, 1F, -4F, 2, 1, 4);
        jaw.setRotationPoint(0F, 20F, -6F);

        back3 = new RendererModel(this, 32, 7);
        back3.addBox(-0.5F, 0F, -2F, 1, 1, 4);
        back3.setRotationPoint(0F, 17F, 0F);

        dewLap = new RendererModel(this, 0, 4);
        dewLap.addBox(-0.5F, 2F, -3F, 1, 1, 3);
        dewLap.setRotationPoint(0F, 20F, -6F);

        tailBase = new RendererModel(this, 46, 0);
        tailBase.addBox(-1.5F, -0.5F, 0F, 3, 1, 6);
        tailBase.setRotationPoint(0F, 21.5F, 6F);

        tailMid = new RendererModel(this, 48, 7);
        tailMid.addBox(-1F, -0.5F, 0F, 2, 1, 6);
        miscPart = new RendererModel(this, 52, 14);
        miscPart.addBox(-0.5F, -0.5F, 0F, 1, 1, 5);
    }

    @Override
    public void render(IguanaEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(entity, f, f1, f2, f3, f4, f5);
        head.render(f5);
        body.render(f5);
        fLleg.render(f5);
        rLleg.render(f5);
        fRleg.render(f5);
        rRleg.render(f5);
        back1.render(f5);
        back2.render(f5);
        headTop2.render(f5);
        headTop1.render(f5);
        jaw.render(f5);
        back3.render(f5);
        dewLap.render(f5);
        tailBase.render(f5);
        tailMid.render(f5);
        miscPart.render(f5);
    }

    @Override
    public void setLivingAnimations(final IguanaEntity iggy, float f, float f1, float f2) {
        fRleg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.75F * f1;
        fLleg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.75F * f1;
        rRleg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.75F * f1;
        rLleg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.75F * f1;
        tailBase.rotateAngleY = MathHelper.cos(f * 0.6662F) * .25F * f1;
        tailMid.setRotationPoint(0F - (MathHelper.cos(tailBase.rotateAngleY + 1.570796F) * 6), 21.5F, 12F + MathHelper.sin(tailBase.rotateAngleX + 3.14159F) * 6);
        tailMid.rotateAngleY = tailBase.rotateAngleY + MathHelper.cos(f * 0.6662F) * .50F * f1;
        miscPart.setRotationPoint(0F - (MathHelper.cos(tailMid.rotateAngleY + 1.570796F) * 6), 21.5F, 18F + MathHelper.sin(tailMid.rotateAngleX + 3.14159F) * 6);
        miscPart.rotateAngleY = tailMid.rotateAngleY + MathHelper.cos(f * 0.6662F) * .75F * f1;;
    }

    @Override
    public void setRotationAngles(final IguanaEntity iguana, float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(iguana, f, f1, f2, f3, f4, f5);
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