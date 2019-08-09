package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.CreeperModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;

public class TropiCreeperModel extends CreeperModel<TropiCreeperEntity> {
    private final RendererModel head;
    private final RendererModel body;
    private final RendererModel leg3;
    private final RendererModel leg4;
    private final RendererModel leg1;
    private final RendererModel leg2;
    private final RendererModel hat1;
    private final RendererModel hat2;
    private final RendererModel hat3;

    public TropiCreeperModel() {
        int i = 4;
        head = new RendererModel(this, 0, 0);
        head.addBox(-4F, -8F, -4F, 8, 8, 8);
        head.setRotationPoint(0F, 6F, 0F);
        head.rotateAngleX = 0F;
        head.rotateAngleY = 0F;
        head.rotateAngleZ = 0F;
        head.mirror = false;
        body = new RendererModel(this, 16, 16);
        body.addBox(-4F, 0F, -2F, 8, 12, 4);
        body.setRotationPoint(0F, 6F, 0F);
        body.rotateAngleX = 0F;
        body.rotateAngleY = 0F;
        body.rotateAngleZ = 0F;
        body.mirror = false;
        leg3 = new RendererModel(this, 0, 16);
        leg3.addBox(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg3.setRotationPoint(-2F, 12 + i, -4F);
        leg3.rotateAngleX = 0F;
        leg3.rotateAngleY = 0F;
        leg3.rotateAngleZ = 0F;
        leg3.mirror = false;
        leg4 = new RendererModel(this, 0, 16);
        leg4.addBox(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg4.setRotationPoint(2.0F, 12 + i, -4F);
        leg4.rotateAngleX = 0F;
        leg4.rotateAngleY = 0F;
        leg4.rotateAngleZ = 0F;
        leg4.mirror = false;
        leg1 = new RendererModel(this, 0, 16);
        leg1.addBox(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg1.setRotationPoint(-2F, 12 + i, 4F);
        leg1.rotateAngleX = 0F;
        leg1.rotateAngleY = 0F;
        leg1.rotateAngleZ = 0F;
        leg1.mirror = false;
        leg2 = new RendererModel(this, 0, 16);
        leg2.addBox(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg2.setRotationPoint(2.0F, 12 + i, 4F);
        leg2.rotateAngleX = 0F;
        leg2.rotateAngleY = 0F;
        leg2.rotateAngleZ = 0F;
        leg2.mirror = false;
        hat1 = new RendererModel(this, 24, 0);
        hat1.addBox(-5F, -6F, -5F, 12, 1, 6);
        hat1.setRotationPoint(-1F, -3F, -1F);
        hat1.rotateAngleX = 0F;
        hat1.rotateAngleY = 0F;
        hat1.rotateAngleZ = 0F;
        hat1.mirror = true;
        head.addChild(hat1);
        hat2 = new RendererModel(this, 40, 24);
        hat2.addBox(0F, -6F, 0F, 6, 2, 6);
        hat2.setRotationPoint(-3F, -5F, -3F);
        hat2.rotateAngleX = 0F;
        hat2.rotateAngleY = 0F;
        hat2.rotateAngleZ = 0F;
        hat2.mirror = false;
        head.addChild(hat2);
        hat3 = new RendererModel(this, 24, 0);
        hat3.addBox(-5F, -6F, 0F, 12, 1, 6);
        hat3.setRotationPoint(-1F, -3F, 0F);
        hat3.rotateAngleX = 0F;
        hat3.rotateAngleY = 0F;
        hat3.rotateAngleZ = 0F;
        hat3.mirror = false;
        head.addChild(hat3);
    }

    @Override
    public void render(TropiCreeperEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(entity, f, f1, f2, f3, f4, f5);
        head.render(f5);
        body.render(f5);
        leg3.render(f5);
        leg4.render(f5);
        leg1.render(f5);
        leg2.render(f5);
    }

    @Override
    public void setRotationAngles(final TropiCreeperEntity ent, float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(ent, f, f1, f2, f3, f4, f5);
        head.rotateAngleY = f3 / 57.29578F;
        head.rotateAngleX = f4 / 57.29578F;
        leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F) * 1.4F * f1;
        leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    }
}