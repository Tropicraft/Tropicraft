package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.client.entity.model.KoaModel;
import net.tropicraft.core.common.entity.passive.EntityKoaBase;

public class KoaRenderer extends BipedRenderer<EntityKoaBase, KoaModel>
{

    public KoaRenderer(EntityRendererManager rendermanagerIn)
    {
        super(rendermanagerIn, new KoaModel(), 0.5F);
        this.shadowOpaque = 0.5f;
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
		return new ResourceLocation(Constants.MODID + ":textures/entity/koa/koa_" + gender + "_" + role + ".png");
	}
}
