package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;

public class TropiCreeperModel extends SegmentedModel<TropiCreeperEntity> {
    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer leg3;
    private final ModelRenderer leg4;
    private final ModelRenderer leg1;
    private final ModelRenderer leg2;
    private final ModelRenderer hat1;
    private final ModelRenderer hat2;
    private final ModelRenderer hat3;

    public TropiCreeperModel() {
        int i = 4;
        head = new ModelRenderer(this, 0, 0);
        head.addBox(-4F, -8F, -4F, 8, 8, 8);
        head.setPos(0F, 6F, 0F);
        head.xRot = 0F;
        head.yRot = 0F;
        head.zRot = 0F;
        head.mirror = false;
        body = new ModelRenderer(this, 16, 16);
        body.addBox(-4F, 0F, -2F, 8, 12, 4);
        body.setPos(0F, 6F, 0F);
        leg3 = new ModelRenderer(this, 0, 16);
        leg3.addBox(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg3.setPos(-2F, 12 + i, -4F);
        leg4 = new ModelRenderer(this, 0, 16);
        leg4.addBox(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg4.setPos(2.0F, 12 + i, -4F);
        leg1 = new ModelRenderer(this, 0, 16);
        leg1.addBox(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg1.setPos(-2F, 12 + i, 4F);
        leg2 = new ModelRenderer(this, 0, 16);
        leg2.addBox(-2F, 0.0F, -2F, 4, 6, 4, 0);
        leg2.setPos(2.0F, 12 + i, 4F);
        hat1 = new ModelRenderer(this, 24, 0);
        hat1.addBox(-5F, -6F, -5F, 12, 1, 6);
        hat1.setPos(-1F, -3F, -1F);
        hat1.mirror = true;
        head.addChild(hat1);
        hat2 = new ModelRenderer(this, 40, 24);
        hat2.addBox(0F, -6F, 0F, 6, 2, 6);
        hat2.setPos(-3F, -5F, -3F);
        hat2.mirror = false;
        head.addChild(hat2);
        hat3 = new ModelRenderer(this, 24, 0);
        hat3.addBox(-5F, -6F, 0F, 12, 1, 6);
        hat3.setPos(-1F, -3F, 0F);
        hat3.mirror = false;
        head.addChild(hat3);
    }

    @Override
    public void setupAnim(TropiCreeperEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        head.xRot = headPitch * ((float) Math.PI / 180F);
        leg1.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        leg2.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        leg3.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        leg4.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(
            head, body, leg1, leg2, leg3, leg4
        );
    }
}