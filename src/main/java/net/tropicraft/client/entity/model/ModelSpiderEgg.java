package net.tropicraft.client.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

import org.lwjgl.opengl.GL11;

public class ModelSpiderEgg extends ModelBase {

    public ModelRenderer body;
    public ModelRenderer body1;
    public ModelRenderer body2;
    public ModelRenderer body3;
    public ModelRenderer body4;
    

    public ModelSpiderEgg() {

        body = new ModelRenderer(this, 0, 8);
        body.addBox(-1F, -2F, -4F, 2, 4, 9, 0F);
        body.setRotationPoint(0F, 16F, 0F);
        body.rotateAngleX = 0F;
        body.rotateAngleY = 3.141593F;
        body.rotateAngleZ = 0F;
        body.mirror = false;
        
        float bodyWidth = (float) (10F/* + (Math.sin(entity.worldObj.getWorldTime()) * 0.0001F)*/);
        float bodyHeight = 5F;
        
        float entHeight = 2F;
        
        body3 = new ModelRenderer(this, 0, 8);
        body3.addBox(-bodyWidth/2, -bodyHeight/2, -bodyWidth/2,(int)bodyWidth, (int)bodyHeight, (int)bodyWidth, 0F);
        body3.setRotationPoint(0F, 16F, 0F);
        
        bodyWidth = 8F;
        bodyHeight = 3F;
        
        body2 = new ModelRenderer(this, 0, 8);
        body2.addBox(-bodyWidth/2, (-bodyHeight/2)-4, -bodyWidth/2,(int)bodyWidth, (int)bodyHeight, (int)bodyWidth, 0F);
        body2.setRotationPoint(0F, 16F, 0F);
        
        body4 = new ModelRenderer(this, 0, 8);
        body4.addBox(-bodyWidth/2, (-bodyHeight/2)+4, -bodyWidth/2,(int)bodyWidth, (int)bodyHeight, (int)bodyWidth, 0F);
        body4.setRotationPoint(0F, 16F, 0F);
        
        bodyWidth = 6F;
        bodyHeight = 2F;
        
        body1 = new ModelRenderer(this, 0, 8);
        body1.addBox(-bodyWidth/2, (-bodyHeight/2)-6, -bodyWidth/2,(int)bodyWidth, (int)bodyHeight, (int)bodyWidth, 0F);
        body1.setRotationPoint(0F, 16F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        //body.render(f5);
        
        
        
        
        
        //System.out.println(bodyWidth);
        
        
        
        float scale = 1F - (float) (Math.sin(entity.worldObj.getWorldTime() / 2F) * 0.05F);
        
        GL11.glPushMatrix();
        //GL11.glTranslatef(-(offset/2), 0, -(offset/2));
        GL11.glScalef(scale, 1, scale);
        body3.render(f5);
        //GL11.glScalef(2F - scale, 1, 2F - scale);
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
        scale = 1F - (float) (Math.sin((entity.worldObj.getWorldTime() + 2F) / 2F) * 0.05F);
        GL11.glScalef(scale, 1, scale);
        body2.render(f5);
        body4.render(f5);
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
        scale = 1F - (float) (Math.sin((entity.worldObj.getWorldTime() + 4F) / 2F) * 0.05F);
        GL11.glScalef(scale, 1, scale);
        body1.render(f5);
        GL11.glPopMatrix();
        //GL11.glScalef(-scale, 1, -scale);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
        /*Face.rotateAngleX = f4 / 57.29578F + herps;
        Face.rotateAngleY = f3 / 57.29578F + 3.141593F;
        Head.rotateAngleX = Face.rotateAngleX;
        Head.rotateAngleY = Face.rotateAngleY;*/
    }
    
    @Override
    public void setLivingAnimations(EntityLivingBase entityliving, float f, float f1, float f2) {
        
        setLivingAnimationStand(entityliving, f, f1, f2);
        
    }

    public void setLivingAnimationSit(EntityLivingBase entityliving, float f, float f1, float f2) {
    	body.setRotationPoint(0F, 20F, 0F);
        body.rotateAngleX = 0.9320058F;
        body.rotateAngleY = 3.141593F;
    }
    
    public void setLivingAnimationClimb(EntityLivingBase entityliving, float f, float f1, float f2) {
    	body.rotateAngleX = 1.570796F;
        body.setRotationPoint(0F, 16F, 0F);
    }
    
    public void setLivingAnimationStand(EntityLivingBase entityliving, float f, float f1, float f2) {
    	body.setRotationPoint(0F, 16F, 0F);
        body.rotateAngleY = 3.141593F;
        body.rotateAngleX = 0F;
    }
    
    
}
