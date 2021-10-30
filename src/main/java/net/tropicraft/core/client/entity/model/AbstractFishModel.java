package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.passive.fish.AbstractFishEntity;

public abstract class AbstractFishModel<T extends AbstractFishEntity> extends SegmentedModel<T> {
    public ModelRenderer body;
    public ModelRenderer tail;

    public AbstractFishModel() {
        body = new ModelRenderer(this);
        body.setPos(0F, 16F, 0F);
        body.addBox(0, 0, 0, 0, 1, 1);
        tail = new ModelRenderer(this);
        tail.setPos(0, 0, -1);
        tail.addBox(0, 0, 0, 0, 1, 1);
        body.addChild(tail);
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(body);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        tail.yRot = (float) (Math.sin(ageInTicks * .25F)) * .25F;
    }
}