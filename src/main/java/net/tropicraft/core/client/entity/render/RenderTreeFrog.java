package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Info;
import net.tropicraft.core.client.entity.model.ModelTreeFrog;
import net.tropicraft.core.common.entity.hostile.EntityTreeFrogBase;

public class RenderTreeFrog extends RenderLiving<EntityTreeFrogBase> {

	private static final ResourceLocation TEXTURE_GREEN = new ResourceLocation(Info.MODID + ":textures/entity/treefrog/treefroggreen.png");
	private static final ResourceLocation TEXTURE_RED = new ResourceLocation(Info.MODID + ":textures/entity/treefrog/treefrogred.png");
	private static final ResourceLocation TEXTURE_BLUE = new ResourceLocation(Info.MODID + ":textures/entity/treefrog/treefrogblue.png");
	private static final ResourceLocation TEXTURE_YELLOW = new ResourceLocation(Info.MODID + ":textures/entity/treefrog/treefrogyellow.png");
	
    public RenderTreeFrog() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelTreeFrog(), 0.5F);
    }
    
    @Override
	protected ResourceLocation getEntityTexture(EntityTreeFrogBase entity) {
    	if (((EntityTreeFrogBase)entity).type == 0) {
    		return TEXTURE_GREEN;
		} else if (((EntityTreeFrogBase)entity).type == 1) {
			return TEXTURE_RED;
		} else if (((EntityTreeFrogBase)entity).type == 2) {
			return TEXTURE_BLUE;
		} else {
			return TEXTURE_YELLOW;
		}
	}
}
