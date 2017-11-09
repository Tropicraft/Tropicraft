package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelScubaTank extends ModelBase {

    ModelRenderer Tank1;
    ModelRenderer Tank1m1;
    ModelRenderer Tank1m2;
    ModelRenderer Tank1m3;
    ModelRenderer Tank1m4;
    ModelRenderer Tank1m5;
    ModelRenderer Tank1m6;
    ModelRenderer Tank1m7;

    public ModelScubaTank() {
        Tank1 = new ModelRenderer( this, 41, 50 );
        Tank1.setTextureSize( 128, 64 );
        Tank1.addBox( -2F, -5F, -2F, 4, 10, 4);
        Tank1.setRotationPoint( 3F, 7F, 6.5F );
        Tank1m1 = new ModelRenderer( this, 45, 54 );
        Tank1m1.setTextureSize( 128, 64 );
        Tank1m1.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank1m1.setRotationPoint( 3F, 7F, 8.5F );
        Tank1m2 = new ModelRenderer( this, 45, 54 );
        Tank1m2.setTextureSize( 128, 64 );
        Tank1m2.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank1m2.setRotationPoint( 1F, 7F, 6.5F );
        Tank1m3 = new ModelRenderer( this, 45, 54 );
        Tank1m3.setTextureSize( 128, 64 );
        Tank1m3.addBox( -1.5F, -4.5F, -0.5F, 3, 9, 1);
        Tank1m3.setRotationPoint( 5F, 7F, 6.5F );
        Tank1m4 = new ModelRenderer( this, 43, 46 );
        Tank1m4.setTextureSize( 128, 64 );
        Tank1m4.addBox( -1.5F, -0.5F, -1.5F, 3, 1, 3);
        Tank1m4.setRotationPoint( 3F, 1.5F, 6.5F );
        Tank1m5 = new ModelRenderer( this, 38, 49 );
        Tank1m5.setTextureSize( 128, 64 );
        Tank1m5.addBox( -0.5F, -2F, -0.5F, 1, 4, 1);
        Tank1m5.setRotationPoint( 3F, -0.5F, 6.5F );
        Tank1m6 = new ModelRenderer( this, 44, 44 );
        Tank1m6.setTextureSize( 128, 64 );
        Tank1m6.addBox( -2F, -0.5F, -0.5F, 4, 1, 1);
        Tank1m6.setRotationPoint( 3.5F, -0.5F, 6.5F );
        Tank1m7 = new ModelRenderer( this, 36, 44 );
        Tank1m7.setTextureSize( 128, 64 );
        Tank1m7.addBox( -1F, -1F, -1F, 2, 2, 2);
        Tank1m7.setRotationPoint( 5.5F, -0.5F, 6.5F );    
    }
    
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        super.render(par1Entity, par2, par3, par4, par5, par6, par7);
        Tank1.rotateAngleX = 0F;
        Tank1.rotateAngleY = 0F;
        Tank1.rotateAngleZ = 0F;
        Tank1.renderWithRotation(par7);

        Tank1m1.rotateAngleX = 0F;
        Tank1m1.rotateAngleY = 0F;
        Tank1m1.rotateAngleZ = 0F;
        Tank1m1.renderWithRotation(par7);

        Tank1m2.rotateAngleX = 0F;
        Tank1m2.rotateAngleY = -1.570796F;
        Tank1m2.rotateAngleZ = 0F;
        Tank1m2.renderWithRotation(par7);

        Tank1m3.rotateAngleX = 0F;
        Tank1m3.rotateAngleY = -1.570796F;
        Tank1m3.rotateAngleZ = 0F;
        Tank1m3.renderWithRotation(par7);

        Tank1m4.rotateAngleX = 0F;
        Tank1m4.rotateAngleY = 0F;
        Tank1m4.rotateAngleZ = 0F;
        Tank1m4.renderWithRotation(par7);

        Tank1m5.rotateAngleX = 0F;
        Tank1m5.rotateAngleY = 0F;
        Tank1m5.rotateAngleZ = 0F;
        Tank1m5.renderWithRotation(par7);

        Tank1m6.rotateAngleX = 0F;
        Tank1m6.rotateAngleY = 0F;
        Tank1m6.rotateAngleZ = 0F;
        Tank1m6.renderWithRotation(par7);

        Tank1m7.rotateAngleX = 0F;
        Tank1m7.rotateAngleY = 0F;
        Tank1m7.rotateAngleZ = 0F;
        Tank1m7.renderWithRotation(par7);
    }
    
    public void render() {
        float f5 = 0.0625F;
        Tank1.render(f5);
        Tank1m1.render(f5);
        Tank1m2.render(f5);
        Tank1m3.render(f5);
        Tank1m4.render(f5);
        Tank1m5.render(f5);
        Tank1m6.render(f5);
        Tank1m7.render(f5);

       
    }
}
