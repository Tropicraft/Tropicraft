package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelLavaBall;
import net.tropicraft.core.common.entity.EntityLavaBall;

import org.lwjgl.opengl.GL11;

public class RenderLavaBall extends Render<EntityLavaBall> {

    protected ModelLavaBall ball;

    public RenderLavaBall() {
    	super(Minecraft.getMinecraft().getRenderManager());
        shadowSize = .5F;
        ball = new ModelLavaBall();
    }

    @Override
    public void doRender(EntityLavaBall lavaBall, double d, double d1, double d2,
            float f, float f1) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) d, (float) d1 - .5F, (float) d2);
        GL11.glScalef(lavaBall.size, lavaBall.size, lavaBall.size);
        TropicraftRenderUtils.bindTextureEntity("lavaball");
        GL11.glColor3f(1.0F, 1.0F, f1);
        ball.render(lavaBall, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

	@Override
	protected ResourceLocation getEntityTexture(EntityLavaBall entity) {
		return TropicraftRenderUtils.getTextureEntity("lavaball");
	}
}
