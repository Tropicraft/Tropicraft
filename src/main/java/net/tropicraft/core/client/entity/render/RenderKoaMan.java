package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Info;
import net.tropicraft.core.client.entity.model.ModelKoaMan;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

public class RenderKoaMan extends RenderBiped<EntityKoaBase>
{

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
		String gender = "man";
		String role = "fisher";
		if (entity.getGender() == EntityKoaBase.Genders.FEMALE) {
			gender = "woman";
		}
		if (entity.getRole() == EntityKoaBase.Roles.HUNTER) {
			role = "hunter";
		}
		return new ResourceLocation(Info.MODID + ":textures/entity/koa/koa_" + gender + "_" + role + ".png");
	}
}
