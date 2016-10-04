package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Info;
import net.tropicraft.core.client.entity.model.ModelIguana;
import net.tropicraft.core.common.entity.hostile.EntityIguana;

public class RenderIguana extends RenderLiving<EntityIguana> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(Info.MODID + ":textures/entity/iggytexture.png");

    public RenderIguana() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelIguana(), 0.5F);
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityIguana entity) {
		return TEXTURE;
	}
}
