package net.tropicraft.client.entity.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.entity.placeable.EntityWallShell;
import net.tropicraft.entity.placeable.EntityWallStarfish;
import net.tropicraft.entity.underdasea.StarfishType;
import net.tropicraft.registry.TCItemRegistry;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderWallShell extends Render {

	public RenderWallShell() {
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
		EntityWallShell entityWallShell = (EntityWallShell) entity;
		int type = entityWallShell.getShellType();
		
		ItemStack itemstack = new ItemStack(TCItemRegistry.shells, 1, entityWallShell.getShellType ());

        EntityItem entityitem = new EntityItem(entityWallShell.worldObj, 0.0D, 0.0D, 0.0D, itemstack);
        entityitem.getEntityItem().stackSize = 1;
        
        // Without the below line, the item will spazz out
        entityitem.hoverStart = 0F;
        
        /*
        	Render the item sprite at the wall.
        	It needs to be 180 degrees rotated to face away from the wall.
         */
        GL11.glPushMatrix();
        GL11.glTranslated(x,y,z);
        RenderItem.renderInFrame = true;
        GL11.glRotatef(180.0F + entityWallShell.rotationYaw, 0.0F, 1.0F, 0.0F);
        RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        RenderItem.renderInFrame = false;
        GL11.glPopMatrix();
	}

	@Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {
		
		// Don't use this, let the item entity handle it.
		return null;
	}
}