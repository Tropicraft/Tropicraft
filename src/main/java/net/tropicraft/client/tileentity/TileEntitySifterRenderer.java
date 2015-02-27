package net.tropicraft.client.tileentity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.tropicraft.block.tileentity.TileEntitySifter;

import org.lwjgl.opengl.GL11;

public class TileEntitySifterRenderer extends TileEntitySpecialRenderer {

    private Entity item;
    
    public TileEntitySifterRenderer() {
        
    }

    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
       renderSifter((TileEntitySifter)var1, var2, var4, var6, var8);
    }

    public void renderSifter(TileEntitySifter sifter, double x, double y, double z, float rotation) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y, (float)z + 0.5F);

        if(item == null && sifter.isSifting()) {
            item = (EntityItem)(new EntityItem(sifter.getWorldObj()));
            ((EntityItem)item).setEntityItemStack(sifter.siftItem.copy());
        }

        if(item != null)  {
            item.setWorld(sifter.getWorldObj());    //allows entity to be placed into world
            //f1=size of object inside spawner
            float f1 = 0.4375F;                     
            GL11.glTranslatef(0.0F, 0.7F, 0.0F);        //height of thing inside spawner
            GL11.glScalef(f1*3, f1*3, f1*3);            //size of thing inside spawner, scaled    
            GL11.glRotatef((float)(sifter.yaw2 + (sifter.yaw - sifter.yaw2) * (double)rotation) * 10.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-20F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.4F, 0.0F);       //other height of thing inside spawner

            item.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
            if(sifter.isSifting())
                RenderManager.instance.renderEntityWithPosYaw(item, 0, 0, 0, 0.0F, rotation);
            else
                item = null;
        }
        GL11.glPopMatrix();
    }
}
