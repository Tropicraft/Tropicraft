package net.tropicraft.client.entity.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.entity.hostile.EntityLostMask;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderLostMask extends Render {

    protected MaskRenderer mask;

    public RenderLostMask() {
        shadowSize = 0.5F;
        mask = new MaskRenderer();
    }
    
    @Override
	protected ResourceLocation getEntityTexture(Entity entity) {
    	return TropicraftUtils.bindTextureEntity("ashen/mask");
	}

    public void renderLostMask(EntityLostMask entity, double d, double d1, double d2,
            float f, float f1) {

    	//loadTexture("/mods/TropicraftMod/textures/entities/mask.png");
    	this.bindEntityTexture(entity);
        GL11.glPushMatrix();

        GL11.glTranslatef((float) d, (float) d1, (float) d2);
        GL11.glRotatef(f, 0.0F, 1.0F, 0.0F);

        if (!entity.onGround && !entity.checkForWater(0) && !entity.checkForWater(-1) ) {
        	int[] a = entity.getRotator();
            GL11.glRotatef(a[0] + f1, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(a[1] + f1, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(a[2] + f1, 0.0F, 0.0F, 1.0F);
        }
        else{
        	GL11.glRotatef(270, 1, 0, 0);
        	GL11.glRotatef(180, 0, 0, 1);
        }
        

        mask.renderMask(entity.getColor());

        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1) {
        renderLostMask((EntityLostMask) entity, d, d1, d2, f, f1);
    }

    private void lightingHelper(EntityLostMask mask, float f, float f1) {
        int i = MathHelper.floor_double(mask.posX);
        int j = MathHelper.floor_double(mask.posY + (double) (f1 / 16F));
        int k = MathHelper.floor_double(mask.posZ);
        int direction = mask.getDirection();
        //System.out.println(direction);
        if (direction == 0) {
            i = MathHelper.floor_double(mask.posX + (double) (f / 16F));
           
        }
        if (direction == 1) {
        	 //System.out.println("X = " + i + " Z = " + k);
            k = MathHelper.floor_double(mask.posZ - (double) (f/ 16F));
        }
        if (direction == 2) {
            i = MathHelper.floor_double(mask.posX - (double) (f / 16F));
        }
        if (direction == 3) {
            k = MathHelper.floor_double(mask.posZ + (double) (f / 16F));
        }
     
        int l = renderManager.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
        int i1 = l % 0x10000;
        int j1 = l / 0x10000;
        int var7 = this.renderManager.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
        int var8 = var7 % 65536;
        int var9 = var7 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) var8, (float) var9);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
    }
}
