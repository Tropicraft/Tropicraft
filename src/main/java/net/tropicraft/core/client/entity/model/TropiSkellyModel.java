package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;

public class TropiSkellyModel extends BipedModel<TropiSkellyEntity> {
    public TropiSkellyModel() {
        super(0.0F, 0.0F, 64, 64);
        float g = 0.0F;
        textureWidth = 64;
        textureHeight = 64;
        bipedRightArm = new ModelRenderer(this, 40, 16);
        bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, g);
        bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        bipedLeftArm = new ModelRenderer(this, 40, 16);
        bipedLeftArm.mirror = true;
        bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, g);
        bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        bipedRightLeg = new ModelRenderer(this, 0, 16);
        bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, g);
        bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        bipedLeftLeg = new ModelRenderer(this, 0, 16);
        bipedLeftLeg.mirror = true;
        bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, g);
        bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);

        // Hula Skirt
        bipedBody.addBox(40, 0, -4.0F, 12.0F, -2.0F, 8, 3, 4, 0.0F);
    }
}