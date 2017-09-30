package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Info;
import net.tropicraft.core.client.entity.model.ModelKoaMan;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

public class RenderKoaMan extends RenderBiped<EntityKoaBase>
{

	private static final ResourceLocation TEXTURE = new ResourceLocation(Info.MODID + ":textures/entity/koa/KoaManHunter.png");

	public RenderKoaMan(RenderManager rendermanagerIn, ModelKoaMan modelbase, float f)
	{
		super(rendermanagerIn, modelbase, f);
		mainModel = modelbase;
		shadowSize = f;
	}
	
	@Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityKoaBase entity) {
		return TEXTURE;
	}
}
