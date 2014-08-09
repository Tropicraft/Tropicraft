package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.entity.hostile.SpiderBase;
import net.tropicraft.entity.hostile.SpiderChild;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTropiSpider extends RenderLiving
{
    public RenderTropiSpider()
    {
        super(new ModelSpider(), 1.0F);
        this.setRenderPassModel(new ModelSpider());
    }
    
    @Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {
    	if (entity instanceof SpiderChild) {
    		return TropicraftUtils.bindTextureEntity("spiderchild");
    	} else {
	    	boolean isMother = entity.getDataWatcher().getWatchableObjectInt(25) == 1;
			if (isMother) {
				return TropicraftUtils.bindTextureEntity("spidermother");
			} else {
				return TropicraftUtils.bindTextureEntity("spideradult");
			}
    	}
	}

    protected float setSpiderDeathMaxRotation(SpiderBase par1EntitySpider)
    {
        return 180.0F;
    }

    /**
     * Sets the spider's glowing eyes
     */
    protected int setSpiderEyeBrightness(SpiderBase par1EntitySpider, int par2, float par3)
    {
        if (par2 != 0)
        {
            return -1;
        }
        else
        {
        	renderManager.renderEngine.bindTexture(new ResourceLocation("textures/entity/spider_eyes.png"));
            //this.loadTexture("/mob/spider_eyes.png");
            float f1 = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

            if (par1EntitySpider.isInvisible())
            {
                GL11.glDepthMask(false);
            }
            else
            {
                GL11.glDepthMask(true);
            }

            char c0 = 61680;
            int j = c0 % 65536;
            int k = c0 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
            return 1;
        }
    }

    protected void scaleSpider(SpiderBase par1EntitySpider, float par2)
    {
        float f1 = par1EntitySpider.spiderScaleAmount();
        GL11.glScalef(f1, f1, f1);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    @Override
    protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2)
    {
        this.scaleSpider((SpiderBase)par1EntityLiving, par2);
    }

    @Override
    protected float getDeathMaxRotation(EntityLivingBase par1EntityLiving)
    {
        return this.setSpiderDeathMaxRotation((SpiderBase)par1EntityLiving);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    @Override
    protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3)
    {
        return this.setSpiderEyeBrightness((SpiderBase)par1EntityLiving, par2, par3);
    }
}
