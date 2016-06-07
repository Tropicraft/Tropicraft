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
import net.minecraft.world.World;
import net.tropicraft.entity.hostile.EntityLostMask;
import net.tropicraft.entity.placeable.EntityWallMask;
import net.tropicraft.entity.placeable.EntityWallShell;
import net.tropicraft.entity.placeable.EntityWallStarfish;
import net.tropicraft.entity.underdasea.StarfishType;
import net.tropicraft.registry.TCItemRegistry;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderWallMask extends Render {
	
	protected MaskRenderer mask;
	 
	public RenderWallMask() {
        shadowSize = 0.5F;
        mask = new MaskRenderer();
	}
    
    @Override
	protected ResourceLocation getEntityTexture(Entity entity) {
    	return TropicraftUtils.bindTextureEntity("ashen/mask");
	}

	@Override
	public void doRender(Entity _entity, double x, double y, double z, float yaw, float partialTicks) {
		EntityWallMask entityWallMask = (EntityWallMask) _entity;
		int type = entityWallMask.getMaskType ();

		// Load and set the texture of themask
		TropicraftUtils.bindTextureEntity("ashen/mask");
        GL11.glPushMatrix();

        GL11.glTranslated(x, y - 0.3D, z);
        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
        
        // Mask needs to be flipped to face away from the wall
    	GL11.glRotatef(180, 1, 0, 0);
        
    	// Render the mask (does not set the texture)
        mask.renderMask(type);

        GL11.glPopMatrix();
	}
}