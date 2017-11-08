package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.hostile.EntityTropiSpider;

@SideOnly(Side.CLIENT)
public class RenderTropiSpider extends RenderSpider<EntityTropiSpider> {
	public RenderTropiSpider() {
		super(Minecraft.getMinecraft().getRenderManager());
		this.shadowOpaque = 0.5f;
	}

	@Override
	public void doRender(EntityTropiSpider entityliving, double x, double y, double z, float yaw, float pt) {
		GlStateManager.pushMatrix();
		float scale = 1f;
		if(entityliving.getType() == EntityTropiSpider.Type.CHILD) {
			scale = 0.5f;
		}
		if(entityliving.getType() == EntityTropiSpider.Type.MOTHER) {
			scale = 1.2f;
		}
		this.shadowSize = scale;
		
		GlStateManager.translate(x, y, z);
		GlStateManager.scale(scale, scale, scale);
		super.doRender(entityliving, 0, 0, 0, yaw, pt);
		GlStateManager.popMatrix();
	}

	
    @Override
	protected ResourceLocation getEntityTexture(EntityTropiSpider entity) {
    		ResourceLocation l = TropicraftRenderUtils.bindTextureEntity("spideradult");
    		
    		if(entity.getType() == EntityTropiSpider.Type.CHILD) {
    			l = TropicraftRenderUtils.bindTextureEntity("spiderchild");
    		}
    		if(entity.getType() == EntityTropiSpider.Type.MOTHER) {
    			l = TropicraftRenderUtils.bindTextureEntity("spidermother");
    		}
    		return l;
	}
}