package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelTropiCreeper;
import net.tropicraft.entity.hostile.EntityTropiCreeper;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderTropiCreeper extends RenderLiving {

    private ModelBase field_27008_a = new ModelTropiCreeper();
    
    private static final ResourceLocation armoredCreeperTextures = new ResourceLocation("textures/entity/creeper/creeper_armor.png");

    public RenderTropiCreeper() {
        super(new ModelTropiCreeper(), 0.5F);
    }

    /**
     * Updates creeper scale in prerender callback
     */
    protected void updateCreeperScale(EntityTropiCreeper par1EntityVolleyballCreeper, float par2) {
        float var4 = par1EntityVolleyballCreeper.getCreeperFlashIntensity(par2);
        float var5 = 1.0F + MathHelper.sin(var4 * 100.0F) * var4 * 0.01F;

        if (var4 < 0.0F) {
            var4 = 0.0F;
        }

        if (var4 > 1.0F) {
            var4 = 1.0F;
        }

        var4 *= var4;
        var4 *= var4;
        float var6 = (1.0F + var4 * 0.4F) * var5;
        float var7 = (1.0F + var4 * 0.1F) / var5;
        GL11.glScalef(var6, var7, var6);
    }

    /**
     * Updates color multiplier based on creeper state called by
     * getColorMultiplier
     */
    protected int updateCreeperColorMultiplier(EntityTropiCreeper par1EntityVolleyballCreeper, float par2, float par3) {
        float var5 = par1EntityVolleyballCreeper.getCreeperFlashIntensity(par3);

        if ((int) (var5 * 10.0F) % 2 == 0) {
            return 0;
        } else {
            int var6 = (int) (var5 * 0.2F * 255.0F);

            if (var6 < 0) {
                var6 = 0;
            }

            if (var6 > 255) {
                var6 = 255;
            }

            short var7 = 255;
            short var8 = 255;
            short var9 = 255;
            return var6 << 24 | var7 << 16 | var8 << 8 | var9;
        }
    }

    protected int func_27006_a(EntityTropiCreeper par1EntityVolleyballCreeper, int par2, float par3) {
        if (par1EntityVolleyballCreeper.getPowered()) {
            if (par2 == 1) {
                float var4 = (float) par1EntityVolleyballCreeper.ticksExisted + par3;
                this.renderManager.renderEngine.bindTexture(armoredCreeperTextures);
                //this.loadTexture("/armor/power.png");
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                float var5 = var4 * 0.01F;
                float var6 = var4 * 0.01F;
                GL11.glTranslatef(var5, var6, 0.0F);
                this.setRenderPassModel(this.field_27008_a);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glEnable(GL11.GL_BLEND);
                float var7 = 0.5F;
                GL11.glColor4f(var7, var7, var7, 1.0F);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                return 1;
            }

            if (par2 == 2) {
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }

        return -1;
    }

    protected int func_27007_b(EntityTropiCreeper par1EntityVolleyballCreeper, int par2, float par3) {
        return -1;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before
     * the model is rendered. Args: entityLiving, partialTickTime
     */
    @Override
    protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
        this.updateCreeperScale((EntityTropiCreeper) par1EntityLiving, par2);
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness,
     * partialTickTime
     */
    @Override
    protected int getColorMultiplier(EntityLivingBase par1EntityLiving, float par2, float par3) {
        return this.updateCreeperColorMultiplier((EntityTropiCreeper) par1EntityLiving, par2, par3);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    @Override
    protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
        return this.func_27006_a((EntityTropiCreeper) par1EntityLiving, par2, par3);
    }

    @Override
    protected int inheritRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3) {
        return this.func_27007_b((EntityTropiCreeper) par1EntityLiving, par2, par3);
    }

	@Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TropicraftUtils.bindTextureEntity("creeper");
	}
}
