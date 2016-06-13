package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.tropicraft.entity.hostile.EntityLostMask;
import net.tropicraft.entity.placeable.EntityWallMask;
import net.tropicraft.entity.placeable.EntityWallShell;
import net.tropicraft.entity.placeable.EntityWallStarfish;
import net.tropicraft.entity.underdasea.StarfishType;
import net.tropicraft.registry.TCItemRegistry;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * Represents a mask that is worn by the player.
 * @maskType: determines which mask, is derived from item metadata.
 * 
 * @author CBaakman
 */
public class RenderArmorMask extends ModelBiped {
	
	private int maskType;
	protected MaskRenderer mask;
	
    public RenderArmorMask(int maskType){
        //load the normal player model from ModelBiped
        super();
        
        this.maskType = maskType;
        
        mask = new MaskRenderer();
    }
    
    /*
     * Arguments f0 to f5 are explained at:
     * http://schwarzeszeux.tumblr.com/post/14176343101/minecraft-modeling-animation-part-2-of-x
     */
  
	@Override
	public void render(Entity entity, float f0, float f1, float f2, float f3, float f4, float f5)
	{	
		EntityLivingBase livingEntity = (EntityLivingBase)entity;
		
		/*
			Don't call super class' render method, let the renderMask function handle it!
			SetRotationAngles might me necessary, but I'm not sure.
		 */
	    this.setRotationAngles(f0, f1, f2, f3, f4, f5, entity);
	    
        GL11.glPushMatrix();
        
        // Set head rotation to mask
        GL11.glRotatef(f3, 0, 1, 0);
        GL11.glRotatef(f4, 1, 0, 0);
    	
        // Flip mask to face away from the player
    	GL11.glRotatef(180, 0, 1, 0);
    	
    	// put it in the middle in front of the face, eyeholes at (Steve's) eye height
    	GL11.glTranslatef (-0.025F, 0.1F, 0.3F);
    	
    	/*
    		renderMask handles the rendering of the mask model, but it doesn't set the texture.
    		Setting the texture is handled in the item class.
    	 */
        mask.renderMask(this.maskType);

        GL11.glPopMatrix();
	}
}