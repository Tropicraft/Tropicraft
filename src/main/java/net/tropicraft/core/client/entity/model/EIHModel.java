package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.tropicraft.core.common.entity.neutral.EIHEntity;

public class EIHModel extends EntityModel<EIHEntity> {
    private RendererModel body;
    private RendererModel base;
    private RendererModel nose;
    private RendererModel mouth;
    private RendererModel top;
    private RendererModel leye;
    private RendererModel reye;

    public EIHModel() {
        body = new RendererModel(this, 34, 8);
        body.addBox(-4F, 1.0F, -1F, 8, 17, 7, 0.0F);
        body.setRotationPoint(0.0F, -2F, 0.0F);

        base = new RendererModel(this, 0, 0);
        base.addBox(-4F, 11F, -3F, 8, 8, 11, 0.0F);
        base.setRotationPoint(0.0F, 5F, -2F);

        nose = new RendererModel(this, 27, 2);
        nose.addBox(13.5F, -1F, -3F, 13, 2, 3, 0.0F);
        nose.setRotationPoint(0.0F, -14.8F, -1F);
        nose.rotateAngleZ = 1.570796F;

        mouth = new RendererModel(this, 56, 11);
        mouth.addBox(-1.5F, 4F, -1F, 3, 3, 1, 0.0F);
        mouth.setRotationPoint(0.0F, 7.5F, -0.5F);

        top = new RendererModel(this, 0, 17);
        top.addBox(-4F, -1F, -10F, 8, 5, 10, 0.0F);
        top.setRotationPoint(0.0F, -5F, 6F);

        leye = new RendererModel(this, 56, 7);
        leye.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1, 0.0F);
        leye.setRotationPoint(1.0F, -1F, -2F);
        leye.mirror = true;

        reye = new RendererModel(this, 56, 7);
        reye.addBox(-1.5F, -1F, -1F, 3, 3, 1, 0.0F);
        reye.setRotationPoint(-2.5F, 0.0F, -1F);
    }

    @Override
    public void render(EIHEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(entity, f, f1, f2, f3, f4, f5);
        body.render(f5);
        base.render(f5);
        nose.render(f5);
        mouth.render(f5);
        top.render(f5);
        leye.render(f5);
        reye.render(f5);
    }

}
