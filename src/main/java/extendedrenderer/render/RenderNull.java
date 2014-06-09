package extendedrenderer.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderNull extends Render
{
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9) {
    	var1.lastTickPosX = var1.posX;
    	var1.lastTickPosY = var1.posY;
    	var1.lastTickPosZ = var1.posZ;
    }

	@Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {
		// TODO Auto-generated method stub
		return null;
	}
}
