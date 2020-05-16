package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.passive.fish.AbstractFishEntity;

public abstract class AbstractFishModel<T extends AbstractFishEntity> extends EntityModel<T> {
    public ModelRenderer body;
    public ModelRenderer tail;

    public AbstractFishModel() {
        body = new ModelRenderer(this);
        body.setRotationPoint(0F, 16F, 0F);
        body.addBox(0, 0, 0, 0, 1, 1);
        tail = new ModelRenderer(this);
        tail.setRotationPoint(0, 0, -1);
        tail.addBox(0, 0, 0, 0, 1, 1);
        body.addChild(tail);
    }

    @Override
    public void render(T entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(null, f, f1, f2, f3, f4, f5);
        body.render(f5);
    }

    @Override
    public void setRotationAngles(T ent, float f, float f1, float f2, float f3, float f4, float f5) {
        super.setRotationAngles(ent, f, f1, f2, f3, f4, f5);
        tail.rotateAngleY = (float) (Math.sin(f2 * .25F)) * .25F;
    }
}