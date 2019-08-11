package net.tropicraft.core.client.entity.model;

import net.minecraft.client.renderer.entity.model.AbstractZombieModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.ModelBox;
import net.tropicraft.core.common.entity.hostile.TropiSkellyEntity;

public class TropiSkellyModel extends AbstractZombieModel<TropiSkellyEntity> {

    public TropiSkellyModel() {
        super(0.0F, 0.0F, 64, 64);
        float g = 0.0F;
        textureWidth = 64;
        textureHeight = 64;
        bipedRightArm = new RendererModel(this, 40, 16);
        bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, g);
        bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        bipedLeftArm = new RendererModel(this, 40, 16);
        bipedLeftArm.mirror = true;
        bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, g);
        bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        bipedRightLeg = new RendererModel(this, 0, 16);
        bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, g);
        bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        bipedLeftLeg = new RendererModel(this, 0, 16);
        bipedLeftLeg.mirror = true;
        bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, g);
        bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);

        final ModelBox hulaSkirt = new ModelBox(this.bipedBody, 40, 0, -4.0F, 12.0F, -2.0F, 8, 3, 4, 0.0F);
        bipedBody.cubeList.add(hulaSkirt);
    }

    @Override
    public boolean func_212850_a_(TropiSkellyEntity tropiSkellyEntity) {
        return false;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(TropiSkellyEntity entity, float par2, float par3, float par4, float par5, float par6, float scale) {
        this.setRotationAngles(entity, par2, par3, par4, par5, par6, scale);
        this.bipedHead.render(scale);
        this.bipedHeadwear.render(scale);
        this.bipedBody.render(scale);
        this.bipedRightArm.render(scale);
        this.bipedLeftArm.render(scale);
        this.bipedRightLeg.render(scale);
        this.bipedLeftLeg.render(scale);
    }
}