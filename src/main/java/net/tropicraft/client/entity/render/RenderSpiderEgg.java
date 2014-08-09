package net.tropicraft.client.entity.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelSpiderEgg;
import net.tropicraft.entity.hostile.SpiderEgg;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderSpiderEgg extends RenderLiving {
    
    protected ModelSpiderEgg modelVMonkey;

    public RenderSpiderEgg(ModelSpiderEgg modelbase, float f) {
        super(modelbase, f);
        modelVMonkey = modelbase;
    }
    
    @Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {
    	return TropicraftUtils.bindTextureEntity("spideregg");
	}
}
