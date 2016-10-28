package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelFailgull;
import net.tropicraft.core.common.entity.passive.EntityFailgull;

public class RenderFailgull extends RenderLiving<EntityFailgull> {

	public RenderFailgull(float f) {
		super(Minecraft.getMinecraft().getRenderManager(), new ModelFailgull(), f);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityFailgull entity) {
		return TropicraftRenderUtils.bindTextureEntity("failgull");
	}
}
