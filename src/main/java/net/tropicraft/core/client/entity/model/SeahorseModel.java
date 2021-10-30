package net.tropicraft.core.client.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.tropicraft.core.common.entity.underdasea.SeahorseEntity;

public class SeahorseModel extends ListModel<SeahorseEntity> {
    ModelPart head1;
    ModelPart snout1;
    ModelPart snout2;
    ModelPart snout3;
    ModelPart eye1;
    ModelPart eye2;
    ModelPart fin2;
    ModelPart fin4;
    ModelPart fin3;
    ModelPart neck1;
    ModelPart neck2;
    ModelPart belly;
    ModelPart tail1;
    ModelPart tail2;
    ModelPart tail3;
    ModelPart tail4;
    ModelPart tail5;
    ModelPart tail6;
    ModelPart tail7;
    ModelPart tail8;
    ModelPart tail9;
    ModelPart tail10;
    ModelPart tail11;
    ModelPart fin1;

    public SeahorseModel() {
        head1 = new ModelPart( this, 0, 0 );
        head1.setTexSize( 64, 64 );
        head1.addBox( -2.5F, -2.5F, -2.5F, 5, 5, 5);
        head1.setPos( 1F, -36F, 0.5F );

        snout1 = new ModelPart( this, 20, 0 );
        snout1.setTexSize( 64, 64 );
        snout1.addBox( -1.5F, -1F, -1.5F, 3, 3, 4);
        snout1.setPos( -2.448189F, -33.97269F, 2.980232E-08F );

        snout2 = new ModelPart( this, 34, 0 );
        snout2.setTexSize( 64, 64 );
        snout2.addBox( -2.5F, -0.5F, -0.5F, 5, 2, 2);
        snout2.setPos( -5.491952F, -31.3774F, 2.980232E-08F );

        snout3 = new ModelPart( this, 23, 7 );
        snout3.setTexSize( 64, 64 );
        snout3.addBox( -0.5F, -1F, -1F, 1, 3, 3);
        snout3.setPos( -7.54649F, -29.62558F, 0F );

        eye1 = new ModelPart( this, 40, 4 );
        eye1.setTexSize( 64, 64 );
        eye1.addBox( -1F, -1F, -0.5F, 2, 2, 1);
        eye1.setPos( -2.955017F, -34.83473F, -2F );

        eye2 = new ModelPart( this, 40, 4 );
        eye2.setTexSize( 64, 64 );
        eye2.addBox( -1F, -1F, -0.5F, 2, 2, 1);

        eye2.setPos( -2.958766F, -34.83232F, 3F );

        fin2 = new ModelPart( this, 39, 15 );
        fin2.setTexSize( 64, 64 );
        fin2.addBox( -3F, -2.5F, 0F, 6, 5, 0);
        fin2.setPos( 1.222835F, -38.81833F, 0.5F );

        fin4 = new ModelPart( this, 36, 9 );
        fin4.setTexSize( 64, 64 );
        fin4.addBox( -4F, -2.5F, 0F, 4, 5, 0);
        fin4.setPos( 1.000001F, -36F, -2F );

        fin3 = new ModelPart( this, 45, 9 );
        fin3.setTexSize( 64, 64 );
        fin3.addBox( -4F, -2.5F, 0F, 4, 5, 0);
        fin3.setPos( 1.000001F, -36F, 3F );

        neck1 = new ModelPart( this, 0, 10 );
        neck1.setTexSize( 64, 64 );
        neck1.addBox( -2F, -2F, -2F, 4, 4, 4);
        neck1.setPos( 3.5F, -33.5F, 0.5F );

        neck2 = new ModelPart( this, 0, 18 );
        neck2.setTexSize( 64, 64 );
        neck2.addBox( -2.5F, -2F, -2.5F, 5, 4, 5);
        neck2.setPos( 4.999997F, -31F, 0.5F );

        belly = new ModelPart( this, 0, 27 );
        belly.setTexSize( 64, 64 );
        belly.addBox( -3.5F, 0F, -3F, 7, 8, 6);
        belly.setPos( 5F, -30F, 0.5F );

        tail1 = new ModelPart( this, 0, 18 );
        tail1.setTexSize( 64, 64 );
        tail1.addBox( -2.5F, 0F, -2.5F, 5, 4, 5);
        tail1.setPos( 5.5F, -22.5F, 0.5F );

        tail2 = new ModelPart( this, 0, 41 );
        tail2.setTexSize( 64, 64 );
        tail2.addBox( -2F, 0F, -2F, 4, 4, 4);
        tail2.setPos( 5F, -19F, 0.5F );
        tail3 = new ModelPart( this, 0, 49 );
        tail3.setTexSize( 64, 64 );
        tail3.addBox( -2F, 0F, -1.5F, 3, 4, 3);
        tail3.setPos( 4.5F, -15.5F, 0.5F );
        tail4 = new ModelPart( this, 0, 56 );
        tail4.setTexSize( 64, 64 );
        tail4.addBox( -1F, 0F, -1F, 2, 4, 2);
        tail4.setPos( 2.652397F, -12.89918F, 0.5F );
        tail5 = new ModelPart( this, 8, 56 );
        tail5.setTexSize( 64, 64 );
        tail5.addBox( -0.5F, 0F, -0.5F, 1, 2, 1);
        tail5.setPos( -0.8942064F, -12.51931F, 0.5F );
        tail6 = new ModelPart( this, 12, 56 );
        tail6.setTexSize( 64, 64 );
        tail6.addBox( -0.5F, 0F, -0.5F, 1, 2, 1);
        tail6.setPos( -2.551666F, -13.06961F, 0.5F );
        tail7 = new ModelPart( this, 12, 56 );
        tail7.setTexSize( 64, 64 );
        tail7.addBox( -0.5F, 0F, -0.5F, 1, 2, 1);
        tail7.setPos( -3.685031F, -14.47157F, 0.5F );
        tail8 = new ModelPart( this, 12, 56 );
        tail8.setTexSize( 64, 64 );
        tail8.addBox( -0.5F, 0F, -0.5F, 1, 2, 1);
        tail8.setPos( -3.770199F, -16.05041F, 0.5F );
        tail9 = new ModelPart( this, 12, 56 );
        tail9.setTexSize( 64, 64 );
        tail9.addBox( -0.5F, 0F, -0.5F, 1, 2, 1);
        tail9.setPos( -2.846481F, -17.36065F, 0.5F );
        tail10 = new ModelPart( this, 12, 56 );
        tail10.setTexSize( 64, 64 );
        tail10.addBox( -0.5F, 0F, -0.5F, 1, 2, 1);
        tail10.setPos( -0.2576861F, -15.77428F, 0.5F );
        tail11 = new ModelPart( this, 12, 56 );
        tail11.setTexSize( 64, 64 );
        tail11.addBox( -0.5F, -1F, -0.5F, 1, 2, 1);
        tail11.setPos( -0.856306F, -15.47153F, 0.5F );
        fin1 = new ModelPart( this, 40, 22 );
        fin1.setTexSize( 64, 64 );
        fin1.addBox( -2.5F, -4F, 0F, 5, 8, 0);
        fin1.setPos( 8.5F, -20F, 0.5F );
    }

    @Override
    public void prepareMobModel(SeahorseEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        head1.xRot = 0F;
        head1.yRot = 0F;
        head1.zRot = -0.7060349F;

        snout1.xRot = 0F;
        snout1.yRot = 0F;
        snout1.zRot = -0.7060349F;

        snout2.xRot = 0F;
        snout2.yRot = 0F;
        snout2.zRot = -0.7060349F;

        snout3.xRot = 0F;
        snout3.yRot = 0F;
        snout3.zRot = -1.055101F;

        eye1.xRot = -0.1802033F;
        eye1.yRot = 0.1073159F;
        eye1.zRot = -0.7155942F;

        eye2.xRot = -0.1327665F;
        eye2.yRot = 2.978997F;
        eye2.zRot = -2.432569F;

        fin2.xRot = 0F;
        fin2.yRot = 0F;
        fin2.zRot = -0.1043443F;

        fin4.xRot = -0.2562083F;
        fin4.yRot = -2.679784F;
        fin4.zRot = 0.4709548F;

        fin3.xRot = 0.2562083F;
        fin3.yRot = 2.679784F;
        fin3.zRot = 0.4709548F;

        neck1.xRot = 0F;
        neck1.yRot = 0F;
        neck1.zRot = -0.7853982F;

        neck2.xRot = 0F;
        neck2.yRot = 0F;
        neck2.zRot = -0.349066F;

        belly.xRot = 0F;
        belly.yRot = 0F;
        belly.zRot = 0F;

        tail1.xRot = 0F;
        tail1.yRot = 0F;
        tail1.zRot = 0.08726645F;

        tail2.xRot = 0F;
        tail2.yRot = 0F;
        tail2.zRot = 0.3490658F;

        tail3.xRot = 0F;
        tail3.yRot = 0F;
        tail3.zRot = 0.6981316F;

        tail4.xRot = 0F;
        tail4.yRot = 0F;
        tail4.zRot = 1.466756F;

        tail5.xRot = 0F;
        tail5.yRot = 0F;
        tail5.zRot = 1.947916F;

        tail6.xRot = 0F;
        tail6.yRot = 0F;
        tail6.zRot = 2.471515F;

        tail7.xRot = 0F;
        tail7.yRot = 0F;
        tail7.zRot = -3.113539F;

        tail8.xRot = 0F;
        tail8.yRot = 0F;
        tail8.zRot = -2.415407F;

        tail9.xRot = 0F;
        tail9.yRot = 0F;
        tail9.zRot = -1.542743F;

        tail10.xRot = 0F;
        tail10.yRot = 0F;
        tail10.zRot = 2.659437F;

        tail11.xRot = 0F;
        tail11.yRot = 0F;
        tail11.zRot = -2.415407F;

        fin1.xRot = 0F;
        fin1.yRot = 0F;
        fin1.zRot = 0.2188137F;
    }

    @Override
    public void setupAnim(SeahorseEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        fin2.zRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(
            head1, snout1, snout2, snout3, eye1, eye2, fin1, fin2, fin4, fin3, neck1, neck2, belly,
                tail1, tail2, tail3, tail4, tail5, tail6, tail7, tail8, tail9, tail10, tail11
        );
    }
}
