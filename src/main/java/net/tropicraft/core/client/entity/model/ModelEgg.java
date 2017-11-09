package net.tropicraft.core.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.tropicraft.core.common.entity.egg.EntityEgg;

public class ModelEgg extends ModelBase {

    public ModelRenderer Piece1;

    public ModelEgg() {
        textureWidth = 64;
        textureHeight = 32;
        setTextureOffset("Piece1.Shape1", 0, 16);
        setTextureOffset("Piece1.Shape2", 0, 0);
        setTextureOffset("Piece1.Shape3", 0, 7);
        setTextureOffset("Piece1.Shape3", 24, 9);
        setTextureOffset("Piece1.Shape3", 16, 7);
        setTextureOffset("Piece1.Shape3", 8, 9);

        Piece1 = new ModelRenderer(this, "Piece1");
        Piece1.setRotationPoint(0F, 24F, 0F);
        setRotation(Piece1, 0F, 0F, 0F);
        Piece1.mirror = true;
        Piece1.addBox("Shape1", -3F, -10F, -3F, 6, 10, 6);
        Piece1.addBox("Shape2", -1.5F, -11F, -1.5F, 3, 1, 3);
        Piece1.addBox("Shape3", 3F, -7F, -1.5F, 1, 6, 3);
        Piece1.addBox("Shape3", -1.5F, -7F, 3F, 3, 6, 1);
        Piece1.addBox("Shape3", -4F, -7F, -1.5F, 1, 6, 3);
        Piece1.addBox("Shape3", -1.5F, -7F, -4F, 3, 6, 1);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        Piece1.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entityliving, float f, float f1, float f2) {
        if(entityliving instanceof EntityEgg) {
	    		boolean hatching = ((EntityEgg) entityliving).isNearHatching();
	        double randRotator = ((EntityEgg) entityliving).rotationRand;
	        if (hatching) {
	        	
	            Piece1.rotateAngleY = 0F;
	            Piece1.rotateAngleY = (float) (Math.sin(entityliving.ticksExisted*.4))*.2f;
	            Piece1.rotateAngleX = (float) ((Math.sin(randRotator*2)))*.2f;
	            Piece1.rotateAngleZ = (float) ((Math.cos(randRotator * 2)))*.2f;
	          //  Piece1.rotateAngleZ = 0;
	
	        } else {
	            Piece1.rotateAngleY = 0F;
	            Piece1.rotateAngleX = 0F;
	            Piece1.rotateAngleZ = 0F;
	        }
        }
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
    }
}