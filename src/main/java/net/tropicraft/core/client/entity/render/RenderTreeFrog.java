package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelTreeFrog;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrog;

public class RenderTreeFrog extends RenderLiving<EntityTreeFrog> {

    public RenderTreeFrog() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelTreeFrog(), 0.5F);
        this.shadowOpaque = 0.5f;
        this.shadowSize = 0.3f;
    }
    
    @Override
	protected ResourceLocation getEntityTexture(EntityTreeFrog entity) {
    		return TropicraftRenderUtils.getTextureEntity("treefrog/treefrog"+entity.getType().getColor());
    }
}
