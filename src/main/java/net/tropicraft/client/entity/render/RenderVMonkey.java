package net.tropicraft.client.entity.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelVMonkey;
import net.tropicraft.util.TropicraftUtils;

public class RenderVMonkey extends RenderLiving {
    
    protected ModelVMonkey modelVMonkey;

    public RenderVMonkey(ModelVMonkey modelbase, float f) {
        super(modelbase, f);
        modelVMonkey = modelbase;
    }

	@Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TropicraftUtils.bindTextureEntity("monkeytext");
	}
}
