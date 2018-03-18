package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelSeahorse extends ModelBase {

    ModelRenderer head1;
    ModelRenderer snout1;
    ModelRenderer snout2;
    ModelRenderer snout3;
    ModelRenderer eye1;
    ModelRenderer eye2;
    ModelRenderer fin2;
    ModelRenderer fin4;
    ModelRenderer fin3;
    ModelRenderer neck1;
    ModelRenderer neck2;
    ModelRenderer belly;
    ModelRenderer tail1;
    ModelRenderer tail2;
    ModelRenderer tail3;
    ModelRenderer tail4;
    ModelRenderer tail5;
    ModelRenderer tail6;
    ModelRenderer tail7;
    ModelRenderer tail8;
    ModelRenderer tail9;
    ModelRenderer tail10;
    ModelRenderer tail11;
    ModelRenderer fin1;

    public ModelSeahorse() {
        this( 0.0f );
    }

    public ModelSeahorse( float par1 ) {
        head1 = new ModelRenderer( this, 0, 0 );
        head1.setTextureSize( 64, 64 );
        head1.addBox( -2.5F, -2.5F, -2.5F, 5, 5, 5);
        head1.setRotationPoint( 1F, -36F, 0.5F );
        snout1 = new ModelRenderer( this, 20, 0 );
        snout1.setTextureSize( 64, 64 );
        snout1.addBox( -1.5F, -1F, -1.5F, 3, 3, 4);
        snout1.setRotationPoint( -2.448189F, -33.97269F, 2.980232E-08F );
        snout2 = new ModelRenderer( this, 34, 0 );
        snout2.setTextureSize( 64, 64 );
        snout2.addBox( -2.5F, -0.5F, -0.5F, 5, 2, 2);
        snout2.setRotationPoint( -5.491952F, -31.3774F, 2.980232E-08F );
        snout3 = new ModelRenderer( this, 23, 7 );
        snout3.setTextureSize( 64, 64 );
        snout3.addBox( -0.5F, -1F, -1F, 1, 3, 3);
        snout3.setRotationPoint( -7.54649F, -29.62558F, 0F );
        eye1 = new ModelRenderer( this, 40, 4 );
        eye1.setTextureSize( 64, 64 );
        eye1.addBox( -1F, -1F, -0.5F, 2, 2, 1);
        eye1.setRotationPoint( -2.955017F, -34.83473F, -2F );
        eye2 = new ModelRenderer( this, 40, 4 );
        eye2.setTextureSize( 64, 64 );
        eye2.addBox( -1F, -1F, -0.5F, 2, 2, 1);
        eye2.setRotationPoint( -2.958766F, -34.83232F, 3F );
        fin2 = new ModelRenderer( this, 39, 15 );
        fin2.setTextureSize( 64, 64 );
        fin2.addBox( -3F, -2.5F, 0F, 6, 5, 0);
        fin2.setRotationPoint( 1.222835F, -38.81833F, 0.5F );
        fin4 = new ModelRenderer( this, 36, 9 );
        fin4.setTextureSize( 64, 64 );
        fin4.addBox( -4F, -2.5F, 0F, 4, 5, 0);
        fin4.setRotationPoint( 1.000001F, -36F, -2F );
        fin3 = new ModelRenderer( this, 45, 9 );
        fin3.setTextureSize( 64, 64 );
        fin3.addBox( -4F, -2.5F, 0F, 4, 5, 0);
        fin3.setRotationPoint( 1.000001F, -36F, 3F );
        neck1 = new ModelRenderer( this, 0, 10 );
        neck1.setTextureSize( 64, 64 );
        neck1.addBox( -2F, -2F, -2F, 4, 4, 4);
        neck1.setRotationPoint( 3.5F, -33.5F, 0.5F );
        neck2 = new ModelRenderer( this, 0, 18 );
        neck2.setTextureSize( 64, 64 );
        neck2.addBox( -2.5F, -2F, -2.5F, 5, 4, 5);
        neck2.setRotationPoint( 4.999997F, -31F, 0.5F );
        belly = new ModelRenderer( this, 0, 27 );
        belly.setTextureSize( 64, 64 );
        belly.addBox( -3.5F, 0F, -3F, 7, 8, 6);
        belly.setRotationPoint( 5F, -30F, 0.5F );
        tail1 = new ModelRenderer( this, 0, 18 );
        tail1.setTextureSize( 64, 64 );
        tail1.addBox( -2.5F, 0F, -2.5F, 5, 4, 5);
        tail1.setRotationPoint( 5.5F, -22.5F, 0.5F );
        tail2 = new ModelRenderer( this, 0, 41 );
        tail2.setTextureSize( 64, 64 );
        tail2.addBox( -2F, 0F, -2F, 4, 4, 4);
        tail2.setRotationPoint( 5F, -19F, 0.5F );
        tail3 = new ModelRenderer( this, 0, 49 );
        tail3.setTextureSize( 64, 64 );
        tail3.addBox( -2F, 0F, -1.5F, 3, 4, 3);
        tail3.setRotationPoint( 4.5F, -15.5F, 0.5F );
        tail4 = new ModelRenderer( this, 0, 56 );
        tail4.setTextureSize( 64, 64 );
        tail4.addBox( -1F, 0F, -1F, 2, 4, 2);
        tail4.setRotationPoint( 2.652397F, -12.89918F, 0.5F );
        tail5 = new ModelRenderer( this, 8, 56 );
        tail5.setTextureSize( 64, 64 );
        tail5.addBox( -0.5F, 0F, -0.5F, 1, 2, 1);
        tail5.setRotationPoint( -0.8942064F, -12.51931F, 0.5F );
        tail6 = new ModelRenderer( this, 12, 56 );
        tail6.setTextureSize( 64, 64 );
        tail6.addBox( -0.5F, 0F, -0.5F, 1, 2, 1);
        tail6.setRotationPoint( -2.551666F, -13.06961F, 0.5F );
        tail7 = new ModelRenderer( this, 12, 56 );
        tail7.setTextureSize( 64, 64 );
        tail7.addBox( -0.5F, 0F, -0.5F, 1, 2, 1);
        tail7.setRotationPoint( -3.685031F, -14.47157F, 0.5F );
        tail8 = new ModelRenderer( this, 12, 56 );
        tail8.setTextureSize( 64, 64 );
        tail8.addBox( -0.5F, 0F, -0.5F, 1, 2, 1);
        tail8.setRotationPoint( -3.770199F, -16.05041F, 0.5F );
        tail9 = new ModelRenderer( this, 12, 56 );
        tail9.setTextureSize( 64, 64 );
        tail9.addBox( -0.5F, 0F, -0.5F, 1, 2, 1);
        tail9.setRotationPoint( -2.846481F, -17.36065F, 0.5F );
        tail10 = new ModelRenderer( this, 12, 56 );
        tail10.setTextureSize( 64, 64 );
        tail10.addBox( -0.5F, 0F, -0.5F, 1, 2, 1);
        tail10.setRotationPoint( -0.2576861F, -15.77428F, 0.5F );
        tail11 = new ModelRenderer( this, 12, 56 );
        tail11.setTextureSize( 64, 64 );
        tail11.addBox( -0.5F, -1F, -0.5F, 1, 2, 1);
        tail11.setRotationPoint( -0.856306F, -15.47153F, 0.5F );
        fin1 = new ModelRenderer( this, 40, 22 );
        fin1.setTextureSize( 64, 64 );
        fin1.addBox( -2.5F, -4F, 0F, 5, 8, 0);
        fin1.setRotationPoint( 8.5F, -20F, 0.5F );
    }

   @Override
   public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        head1.rotateAngleX = 0F;
        head1.rotateAngleY = 0F;
        head1.rotateAngleZ = -0.7060349F;
        head1.renderWithRotation(par7);

        snout1.rotateAngleX = 0F;
        snout1.rotateAngleY = 0F;
        snout1.rotateAngleZ = -0.7060349F;
        snout1.renderWithRotation(par7);

        snout2.rotateAngleX = 0F;
        snout2.rotateAngleY = 0F;
        snout2.rotateAngleZ = -0.7060349F;
        snout2.renderWithRotation(par7);

        snout3.rotateAngleX = 0F;
        snout3.rotateAngleY = 0F;
        snout3.rotateAngleZ = -1.055101F;
        snout3.renderWithRotation(par7);

        eye1.rotateAngleX = -0.1802033F;
        eye1.rotateAngleY = 0.1073159F;
        eye1.rotateAngleZ = -0.7155942F;
        eye1.renderWithRotation(par7);

        eye2.rotateAngleX = -0.1327665F;
        eye2.rotateAngleY = 2.978997F;
        eye2.rotateAngleZ = -2.432569F;
        eye2.renderWithRotation(par7);

        fin2.rotateAngleX = 0F;
        fin2.rotateAngleY = 0F;
        fin2.rotateAngleZ = -0.1043443F;
        fin2.renderWithRotation(par7);

        fin4.rotateAngleX = -0.2562083F;
        fin4.rotateAngleY = -2.679784F;
        fin4.rotateAngleZ = 0.4709548F;
        fin4.renderWithRotation(par7);

        fin3.rotateAngleX = 0.2562083F;
        fin3.rotateAngleY = 2.679784F;
        fin3.rotateAngleZ = 0.4709548F;
        fin3.renderWithRotation(par7);

        neck1.rotateAngleX = 0F;
        neck1.rotateAngleY = 0F;
        neck1.rotateAngleZ = -0.7853982F;
        neck1.renderWithRotation(par7);

        neck2.rotateAngleX = 0F;
        neck2.rotateAngleY = 0F;
        neck2.rotateAngleZ = -0.349066F;
        neck2.renderWithRotation(par7);

        belly.rotateAngleX = 0F;
        belly.rotateAngleY = 0F;
        belly.rotateAngleZ = 0F;
        belly.renderWithRotation(par7);

        tail1.rotateAngleX = 0F;
        tail1.rotateAngleY = 0F;
        tail1.rotateAngleZ = 0.08726645F;
        tail1.renderWithRotation(par7);

        tail2.rotateAngleX = 0F;
        tail2.rotateAngleY = 0F;
        tail2.rotateAngleZ = 0.3490658F;
        tail2.renderWithRotation(par7);

        tail3.rotateAngleX = 0F;
        tail3.rotateAngleY = 0F;
        tail3.rotateAngleZ = 0.6981316F;
        tail3.renderWithRotation(par7);

        tail4.rotateAngleX = 0F;
        tail4.rotateAngleY = 0F;
        tail4.rotateAngleZ = 1.466756F;
        tail4.renderWithRotation(par7);

        tail5.rotateAngleX = 0F;
        tail5.rotateAngleY = 0F;
        tail5.rotateAngleZ = 1.947916F;
        tail5.renderWithRotation(par7);

        tail6.rotateAngleX = 0F;
        tail6.rotateAngleY = 0F;
        tail6.rotateAngleZ = 2.471515F;
        tail6.renderWithRotation(par7);

        tail7.rotateAngleX = 0F;
        tail7.rotateAngleY = 0F;
        tail7.rotateAngleZ = -3.113539F;
        tail7.renderWithRotation(par7);

        tail8.rotateAngleX = 0F;
        tail8.rotateAngleY = 0F;
        tail8.rotateAngleZ = -2.415407F;
        tail8.renderWithRotation(par7);

        tail9.rotateAngleX = 0F;
        tail9.rotateAngleY = 0F;
        tail9.rotateAngleZ = -1.542743F;
        tail9.renderWithRotation(par7);

        tail10.rotateAngleX = 0F;
        tail10.rotateAngleY = 0F;
        tail10.rotateAngleZ = 2.659437F;
        tail10.renderWithRotation(par7);

        tail11.rotateAngleX = 0F;
        tail11.rotateAngleY = 0F;
        tail11.rotateAngleZ = -2.415407F;
        tail11.renderWithRotation(par7);

        fin1.rotateAngleX = 0F;
        fin1.rotateAngleY = 0F;
        fin1.rotateAngleZ = 0.2188137F;
        fin1.renderWithRotation(par7);
        
        this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);

    }
   
   /**
    * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
    * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
    * "far" arms and legs can swing at most.
    */
   @Override
   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
       fin2.rotateAngleZ = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
 //      this.leftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * 1.4F * par2;
   }

}