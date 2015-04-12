package net.tropicraft.client.entity.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelFailgull;
import net.tropicraft.util.TropicraftUtils;

public class RenderFailgull extends RenderLiving
{

	public RenderFailgull(float f)
	{
		super(new ModelFailgull(), f);
	}

	public void renderNew(EntityLiving entityliving, double d, double d1, double d2, 
			float f, float f1)
	{
		super.doRender(entityliving, d, d1, d2, f, f1);
	}

	public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, 
			float f, float f1)
	{
		renderNew(entityliving, d, d1, d2, f, f1);
	}

	public void doRender(Entity entity, double d, double d1, double d2, 
			float f, float f1)
	{
		renderNew((EntityLiving)entity, d, d1, d2, f, f1);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TropicraftUtils.bindTextureEntity("failgull");
	}
}