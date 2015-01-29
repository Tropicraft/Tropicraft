package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelFish;
import net.tropicraft.entity.underdasea.EntityTropicalFish;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderTropicalFish extends RenderWaterMob {

    public ModelFish fish;
    private MaskRenderer mask;

    public RenderTropicalFish(ModelBase modelbase, float f) {
        super(modelbase, f);
        fish = (ModelFish) modelbase;
        mask = new MaskRenderer();
    }
    
    @Override

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity) {
        return TropicraftUtils.bindTextureEntity("tropicalFish");
    }

    public void renderTropicalFish(EntityTropicalFish fish, double d, double d1, double d2,
            float f, float f1) {
        super.renderWaterMob(fish, d, d1, d2, f, f1);
    }

    @Override
    public void func_110827_b(EntityLiving entityliving, double d, double d1, double d2,
            float f, float f1) {
        renderTropicalFish((EntityTropicalFish) entityliving, d, d1, d2, f, f1);
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1) {
        renderTropicalFish((EntityTropicalFish) entity, d, d1, d2, f, f1);
    }

    @Override
    protected void renderEquippedItems(EntityLivingBase entityliving, float f) {
        GL11.glPushMatrix();
        fish.Body.postRender(.045F);
        this.bindEntityTexture(entityliving);
        //loadTexture("/mods/TropicraftMod/textures/entities/tropicalFish.png");
        GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(.85F, 0.0F, 0.0F);
        mask.renderFish(((EntityTropicalFish) entityliving).getColor() * 2);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        fish.Tail.postRender(.045F);
        //loadTexture("/mods/TropicraftMod/textures/entities/tropicalFish.png");
        GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-.90F, 0.725F, 0.0F);
        mask.renderFish(((EntityTropicalFish) entityliving).getColor() * 2 + 1);
        GL11.glPopMatrix();

    }

    protected void preRenderScale(EntityTropicalFish entityTropicalFish, float f) {
        GL11.glScalef(.75F, .20F, .20F);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entityliving, float f) {
        preRenderScale((EntityTropicalFish) entityliving, f);
    }
}