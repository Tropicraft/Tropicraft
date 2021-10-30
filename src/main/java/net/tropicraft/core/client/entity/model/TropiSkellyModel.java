package net.tropicraft.core.client.entity.model;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.model.AbstractZombieModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;

public class TropiSkellyModel extends AbstractZombieModel<TropiSkellyEntity> implements IHasArm {
    
    private final ModelRenderer skirt;
    
    public TropiSkellyModel() {
        super(0.0F, 0.0F, 64, 64);
        float g = 0.0F;
        texWidth = 64;
        texHeight = 64;
        rightArm = new ModelRenderer(this, 40, 16);
        rightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, g);
        rightArm.setPos(-5.0F, 2.0F, 0.0F);
        leftArm = new ModelRenderer(this, 40, 16);
        leftArm.mirror = true;
        leftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, g);
        leftArm.setPos(5.0F, 2.0F, 0.0F);
        rightLeg = new ModelRenderer(this, 0, 16);
        rightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, g);
        rightLeg.setPos(-2.0F, 12.0F, 0.0F);
        leftLeg = new ModelRenderer(this, 0, 16);
        leftLeg.mirror = true;
        leftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, g);
        leftLeg.setPos(2.0F, 12.0F, 0.0F);

        // Hula Skirt
        skirt = new ModelRenderer(this, 40, 0);
        skirt.addBox(-4.0F, 12.0F, -2.0F, 8, 3, 4, 0.0F);
        body.addChild(skirt);;
    }

    @Override
    public void translateToHand(HandSide side, final MatrixStack stack) {
        super.translateToHand(side, stack);
        stack.translate((side == HandSide.LEFT ? -1 : 1) * 0.1f, 0, 0.0F);
    }

    @Override
    public boolean isAggressive(TropiSkellyEntity entityIn) {
        return entityIn.isAggressive();
    }
}