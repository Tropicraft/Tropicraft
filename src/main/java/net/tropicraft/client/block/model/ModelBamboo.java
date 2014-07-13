package net.tropicraft.client.block.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBamboo extends ModelBase
{
    ModelRenderer chute1;
    ModelRenderer chute1m1;
    ModelRenderer chute1m2;
    ModelRenderer chute1m3;
    ModelRenderer chute2;
    ModelRenderer chute2m1;
    ModelRenderer chute2m2;
    ModelRenderer chute3m3;
    ModelRenderer leaves1;
    ModelRenderer leaves4;
    ModelRenderer leaves2;
    ModelRenderer leaves3;
    ModelRenderer leaves5;
    ModelRenderer leaves6;
    ModelRenderer leaves7;
    ModelRenderer leaves8;

    public ModelBamboo()
    {
        this( 0.0f );
    }

    public ModelBamboo( float par1 )
    {
        chute1 = new ModelRenderer( this, 1, 1 );
        chute1.setTextureSize( 64, 64 );
        chute1.addBox( -2.5F, -0.5F, -2.5F, 5, 1, 5);
        chute1.setRotationPoint( -4F, 23.5F, 4F );
        chute1m1 = new ModelRenderer( this, 0, 7 );
        chute1m1.setTextureSize( 64, 64 );
        chute1m1.addBox( -3F, -3.5F, -3F, 6, 7, 6);
        chute1m1.setRotationPoint( -4F, 19.5F, 4F );
        chute1m2 = new ModelRenderer( this, 1, 1 );
        chute1m2.setTextureSize( 64, 64 );
        chute1m2.addBox( -2.5F, -0.5F, -2.5F, 5, 1, 5);
        chute1m2.setRotationPoint( -4F, 15.5F, 4F );
        chute1m3 = new ModelRenderer( this, 25, 7 );
        chute1m3.setTextureSize( 64, 64 );
        chute1m3.addBox( -3F, -3.5F, -3F, 6, 7, 6);
        chute1m3.setRotationPoint( -4F, 11.5F, 4F );
        chute2 = new ModelRenderer( this, 1, 1 );
        chute2.setTextureSize( 64, 64 );
        chute2.addBox( -2.5F, -0.5F, -2.5F, 5, 1, 5);
        chute2.setRotationPoint( 4F, 23.5F, -4F );
        chute2m1 = new ModelRenderer( this, 25, 7 );
        chute2m1.setTextureSize( 64, 64 );
        chute2m1.addBox( -3F, -3.5F, -3F, 6, 7, 6);
        chute2m1.setRotationPoint( 4F, 19.5F, -4F );
        chute2m2 = new ModelRenderer( this, 1, 1 );
        chute2m2.setTextureSize( 64, 64 );
        chute2m2.addBox( -2.5F, -0.5F, -2.5F, 5, 1, 5);
        chute2m2.setRotationPoint( 4F, 15.5F, -4F );
        chute3m3 = new ModelRenderer( this, 0, 7 );
        chute3m3.setTextureSize( 64, 64 );
        chute3m3.addBox( -3F, -3.5F, -3F, 6, 7, 6);
        chute3m3.setRotationPoint( 4F, 11.5F, -4F );
        leaves1 = new ModelRenderer( this, 21, 41 );
        leaves1.setTextureSize( 64, 64 );
        leaves1.addBox( 0F, -5F, -4F, 0, 10, 8);
        leaves1.setRotationPoint( 6F, 14F, -11F );
        leaves4 = new ModelRenderer( this, 21, 22 );
        leaves4.setTextureSize( 64, 64 );
        leaves4.addBox( 0F, -5F, -4F, 0, 10, 8);
        leaves4.setRotationPoint( 11F, 14F, -2F );
        leaves2 = new ModelRenderer( this, 4, 41 );
        leaves2.setTextureSize( 64, 64 );
        leaves2.addBox( 0F, -5F, -4F, 0, 10, 8);
        leaves2.setRotationPoint( 3F, 17F, 3F );
        leaves3 = new ModelRenderer( this, 4, 22 );
        leaves3.setTextureSize( 64, 64 );
        leaves3.addBox( 0F, -5F, -4F, 0, 10, 8);
        leaves3.setRotationPoint( -3F, 13F, -5F );
        leaves5 = new ModelRenderer( this, 21, 41 );
        leaves5.setTextureSize( 64, 64 );
        leaves5.addBox( 0F, -5F, -4F, 0, 10, 8);
        leaves5.setRotationPoint( -2F, 16F, -3F );
        leaves6 = new ModelRenderer( this, 21, 22 );
        leaves6.setTextureSize( 64, 64 );
        leaves6.addBox( 0F, -5F, -4F, 0, 10, 8);
        leaves6.setRotationPoint( 3F, 16F, 6F );
        leaves7 = new ModelRenderer( this, 4, 41 );
        leaves7.setTextureSize( 64, 64 );
        leaves7.addBox( 0F, -5F, -4F, 0, 10, 8);
        leaves7.setRotationPoint( -5F, 19F, 11F );
        leaves8 = new ModelRenderer( this, 4, 22 );
        leaves8.setTextureSize( 64, 64 );
        leaves8.addBox( 0F, -5F, -4F, 0, 10, 8);
        leaves8.setRotationPoint( -11F, 15F, 3F );
    }

   public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7)
   {
        chute1.rotateAngleX = 0F;
        chute1.rotateAngleY = 0F;
        chute1.rotateAngleZ = 0F;
        chute1.renderWithRotation(par7);

        chute1m1.rotateAngleX = 0F;
        chute1m1.rotateAngleY = 0F;
        chute1m1.rotateAngleZ = 0F;
        chute1m1.renderWithRotation(par7);

        chute1m2.rotateAngleX = 0F;
        chute1m2.rotateAngleY = 0F;
        chute1m2.rotateAngleZ = 0F;
        chute1m2.renderWithRotation(par7);

        chute1m3.rotateAngleX = 0F;
        chute1m3.rotateAngleY = 0F;
        chute1m3.rotateAngleZ = 0F;
        chute1m3.renderWithRotation(par7);

        chute2.rotateAngleX = 0F;
        chute2.rotateAngleY = 0F;
        chute2.rotateAngleZ = 0F;
        chute2.renderWithRotation(par7);

        chute2m1.rotateAngleX = 0F;
        chute2m1.rotateAngleY = 0F;
        chute2m1.rotateAngleZ = 0F;
        chute2m1.renderWithRotation(par7);

        chute2m2.rotateAngleX = 0F;
        chute2m2.rotateAngleY = 0F;
        chute2m2.rotateAngleZ = 0F;
        chute2m2.renderWithRotation(par7);

        chute3m3.rotateAngleX = 0F;
        chute3m3.rotateAngleY = 0F;
        chute3m3.rotateAngleZ = 0F;
        chute3m3.renderWithRotation(par7);

        leaves1.rotateAngleX = 0F;
        leaves1.rotateAngleY = 0F;
        leaves1.rotateAngleZ = 0F;
        leaves1.renderWithRotation(par7);

        leaves4.rotateAngleX = 0F;
        leaves4.rotateAngleY = -1.570796F;
        leaves4.rotateAngleZ = 0F;
        leaves4.renderWithRotation(par7);

        leaves2.rotateAngleX = 0F;
        leaves2.rotateAngleY = 0F;
        leaves2.rotateAngleZ = 0F;
        leaves2.renderWithRotation(par7);

        leaves3.rotateAngleX = 0F;
        leaves3.rotateAngleY = -1.570796F;
        leaves3.rotateAngleZ = 0F;
        leaves3.renderWithRotation(par7);

        leaves5.rotateAngleX = 0F;
        leaves5.rotateAngleY = 0F;
        leaves5.rotateAngleZ = 0F;
        leaves5.renderWithRotation(par7);

        leaves6.rotateAngleX = 0F;
        leaves6.rotateAngleY = -1.570796F;
        leaves6.rotateAngleZ = 0F;
        leaves6.renderWithRotation(par7);

        leaves7.rotateAngleX = 0F;
        leaves7.rotateAngleY = 0F;
        leaves7.rotateAngleZ = 0F;
        leaves7.renderWithRotation(par7);

        leaves8.rotateAngleX = 0F;
        leaves8.rotateAngleY = -1.570796F;
        leaves8.rotateAngleZ = 0F;
        leaves8.renderWithRotation(par7);

    }

}
