package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.tropicraft.core.common.entity.neutral.EIHEntity;

public class EIHModel extends ListModel<EIHEntity> {
    private ModelPart body;
    private ModelPart base;
    private ModelPart nose;
    private ModelPart mouth;
    private ModelPart top;
    private ModelPart leye;
    private ModelPart reye;

    public EIHModel() {
        body = new ModelPart(this, 34, 8);
        body.addBox(-4F, 1.0F, -1F, 8, 17, 7, 0.0F);
        body.setPos(0.0F, -2F, 0.0F);

        base = new ModelPart(this, 0, 0);
        base.addBox(-4F, 11F, -3F, 8, 8, 11, 0.0F);
        base.setPos(0.0F, 5F, -2F);

        nose = new ModelPart(this, 27, 2);
        nose.addBox(13.5F, -1F, -3F, 13, 2, 3, 0.0F);
        nose.setPos(0.0F, -14.8F, -1F);
        nose.zRot = 1.570796F;

        mouth = new ModelPart(this, 56, 11);
        mouth.addBox(-1.5F, 4F, -1F, 3, 3, 1, 0.0F);
        mouth.setPos(0.0F, 7.5F, -0.5F);

        top = new ModelPart(this, 0, 17);
        top.addBox(-4F, -1F, -10F, 8, 5, 10, 0.0F);
        top.setPos(0.0F, -5F, 6F);

        leye = new ModelPart(this, 56, 7);
        leye.addBox(0.0F, 0.0F, 0.0F, 3, 3, 1, 0.0F);
        leye.setPos(1.0F, -1F, -2F);
        leye.mirror = true;

        reye = new ModelPart(this, 56, 7);
        reye.addBox(-1.5F, -1F, -1F, 3, 3, 1, 0.0F);
        reye.setPos(-2.5F, 0.0F, -1F);
    }

    @Override
    public void setupAnim(EIHEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // it's a statue, what do you want from me
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(body, base, nose, mouth, top, leye, reye);
    }
}
