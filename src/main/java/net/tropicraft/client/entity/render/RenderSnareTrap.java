package net.tropicraft.client.entity.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.tropicraft.entity.placeable.EntitySnareTrap;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSnareTrap extends Render {

	public RenderSnareTrap() {
		this.shadowSize = 0.0f;
	}
	
	@Override
	public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
		doRenderTrap((EntitySnareTrap)var1, var2, var4, var6, var8, var9);
	}

	private void doRenderTrap(EntitySnareTrap trap, double x, double y, double z, float yaw, float ticks) {
		double xPos = trap.trapPosX-renderManager.renderPosX;
		double yPos = trap.trapPosY-renderManager.renderPosY;
		double zPos = trap.trapPosZ-renderManager.renderPosZ;
		int height = trap.trapHeight;
		ForgeDirection dir = trap.trapDirection;
		boolean full = trap.getFull();
		float entityHeight = trap.getEntityHeight();
		
		float vOff = 0.05f;
		float hOff = 0.2f;
		
		//GL11.glPushMatrix();
        //renderOffsetAABB(trap.boundingBox, x-trap.lastTickPosX, y-trap.lastTickPosY, z-trap.lastTickPosZ);
        //GL11.glPopMatrix();
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        Tessellator tessellator = Tessellator.instance;
        
        if (!full) {
	        tessellator.startDrawing(3);
	        tessellator.setColorRGBA_F(0.75f, 0.75f, 0.75f, 0.25f);
	        
	        tessellator.addVertex(xPos+hOff, yPos+vOff, zPos+hOff);
	        tessellator.addVertex(xPos+1-hOff, yPos+vOff, zPos+hOff);
	        tessellator.addVertex(xPos+1-hOff, yPos+vOff, zPos+1-hOff);
	        tessellator.addVertex(xPos+hOff, yPos+vOff, zPos+1-hOff);
	        tessellator.addVertex(xPos+hOff, yPos+vOff, zPos+hOff);
	
	        tessellator.draw();
	        
	        tessellator.startDrawing(3);
	        tessellator.setColorRGBA_F(0.75f, 0.75f, 0.75f, 0.5f);
	        
	        double startX = xPos+0.5-dir.offsetX*(1-2*hOff)/2.0;
	        double startY = yPos+vOff;
	        double startZ = zPos+0.5-dir.offsetZ*(1-2*hOff)/2.0;
	        
	        double dx = xPos+0.5-dir.offsetX/2.0-startX;
	        double dy = yPos+height-0.5-startY;
	        double dz = zPos+0.5-dir.offsetZ/2.0-startZ;
	        
	        int steps = 5;
	        
	        for (int i = 0; i <= steps; ++i) {
	        	float step = i/(float)steps;
	        	tessellator.addVertex(startX+step*dx, startY+step*dy, startZ+step*dz);
	        }
	        
	        tessellator.draw();
        } else {
	        tessellator.startDrawing(3);
	        tessellator.setColorRGBA_F(0.75f, 0.75f, 0.75f, 0.5f);
	        
	        double startX = xPos+0.5;
	        double startY = yPos+entityHeight;
	        double startZ = zPos+0.5;
	        
	        double dx = xPos+0.5-dir.offsetX/2.0-startX;
	        double dy = yPos+height-0.5-startY;
	        double dz = zPos+0.5-dir.offsetZ/2.0-startZ;
	        
	        int steps = 5;
	        
	        for (int i = 0; i <= steps; ++i) {
	        	float step = i/(float)steps;
	        	tessellator.addVertex(startX+step*dx, startY+step*dy, startZ+step*dz);
	        }
	        
	        tessellator.draw();
        }
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);	
	}

	@Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
