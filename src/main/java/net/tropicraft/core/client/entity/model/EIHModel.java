package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.tropicraft.core.common.entity.neutral.EIHEntity;

public class EIHModel extends SegmentedModel<EIHEntity> {
    private ModelRenderer body;
    private ModelRenderer base;
    private ModelRenderer nose;
    private ModelRenderer mouth;
    private ModelRenderer top;
    private ModelRenderer leye;
    private ModelRenderer reye;

    public EIHModel() {
        body = new ModelRenderer(this, 34, 8);
        body.addBox(-4F, 1.0F, -1F, 8, 17, 7, 0.0F);
        body.setPos(0.0F, -2F, 0.0F);

        base = new ModelRenderer(this, 0, 0);
        base.addBox(-4F, 11F, -3F, 8, 8, 11, 0.0F);
        base.setPos(0.0F, 5F, -2F);

        nose = new ModelRenderer(this, 27, 2);
        nose.addBox(13.5F, -1F, -3F, 13, 2, 3, 0.0F);
        nose.setPos(0.0F, -14.8F, -1F);
        nose.zRot = 1.570796F;

        mouth = new ModelRenderer(this, 56, 11);
        mouth.addBox(-1.5F, 4F, -1F, 3, 3, 1, 0.0F);
        mouth.setPos(0.0F, 7.5F, -0.5F);

        top = new ModelRenderer(this, 0, 17);
        top.addBox(-4F, -1F, -10F, 8, 5, 10, 0.0F);
        top.setPos(0.0F, -5F, 6F);

        leye = new ModelRenderer(this, 56, 7);
        leye.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1, 0.0F);
        leye.setPos(1.0F, -1F, -2F);
        leye.mirror = true;

        reye = new ModelRenderer(this, 56, 7);
        reye.addBox(-1.5F, -1F, -1F, 3, 3, 1, 0.0F);
        reye.setPos(-2.5F, 0.0F, -1F);
    }

    @Override
    public void setupAnim(EIHEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // it's a statue, what do you want from me
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(body, base, nose, mouth, top, leye, reye);
    }
}
