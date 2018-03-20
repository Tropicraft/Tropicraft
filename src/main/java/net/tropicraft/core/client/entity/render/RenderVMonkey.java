package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Info;
import net.tropicraft.core.client.entity.model.ModelVMonkey;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;

import javax.annotation.Resource;

public class RenderVMonkey extends RenderLiving<EntityVMonkey> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Info.MODID + ":textures/entity/monkeytext.png");
	private static final ResourceLocation ANGRY_TEXTURE = new ResourceLocation(Info.MODID + ":textures/entity/monkey_angrytext.png");
	
    public RenderVMonkey() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelVMonkey(), 0.5F);
        this.shadowSize = 0.3f;
        this.shadowOpaque = 0.5f;
    }

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
    @Override
	protected ResourceLocation getEntityTexture(EntityVMonkey entity) {
    	if (entity.getState() == EntityVMonkey.STATE_ANGRY) {
    		return ANGRY_TEXTURE;
		}

		return TEXTURE;
	}
}
