package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelFish;
import net.tropicraft.core.common.entity.underdasea.EntityTropicalFish;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class RenderTropicalFish extends RenderLiving<EntityTropicalFish> {

	public ModelFish fish;
	private TropicraftSpecialRenderHelper renderHelper;
	private static final Logger LOGGER = LogManager.getLogger();

	public RenderTropicalFish(ModelBase modelbase, float f) {
		super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
		fish = (ModelFish) modelbase;
		renderHelper = new TropicraftSpecialRenderHelper();
	}
	
	/**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityTropicalFish entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
        boolean shouldSit = entity.isRiding() && (entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
        this.mainModel.isRiding = shouldSit;
        this.mainModel.isChild = entity.isChild();

        try
        {
            float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
            float f1 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
            float f2 = f1 - f;

            if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase)
            {
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity.getRidingEntity();
                f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                f2 = f1 - f;
                float f3 = MathHelper.wrapDegrees(f2);

                if (f3 < -85.0F)
                {
                    f3 = -85.0F;
                }

                if (f3 >= 85.0F)
                {
                    f3 = 85.0F;
                }

                f = f1 - f3;

                if (f3 * f3 > 2500.0F)
                {
                    f += f3 * 0.2F;
                }
            }

            float f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            this.renderLivingAt(entity, x, y, z);
            float f8 = this.handleRotationFloat(entity, partialTicks);
            this.rotateCorpse(entity, f8, f, partialTicks);
            float f4 = this.prepareScale(entity, partialTicks);
            float f5 = 0.0F;
            float f6 = 0.0F;

            if (!entity.isRiding()) {
                f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);

                if (entity.isChild()) {
                    f6 *= 3.0F;
                }

                if (f5 > 1.0F) {
                    f5 = 1.0F;
                }
            }

            this.renderFishy(entity);
            
            GlStateManager.enableAlpha();
            this.mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
            this.mainModel.setRotationAngles(f6, f5, f8, f2, f7, f4, entity);

            if (this.renderOutlines) {
                boolean flag1 = this.setScoreTeamColor(entity);
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(this.getTeamColor(entity));

                if (!this.renderMarker) {
                    this.renderModel(entity, f6, f5, f8, f2, f7, f4);
                }

                this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);

                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();

                if (flag1)
                {
                    this.unsetScoreTeamColor();
                }
            }
            else
            {
                boolean flag = this.setDoRenderBrightness(entity, partialTicks);
                this.renderModel(entity, f6, f5, f8, f2, f7, f4);

                if (flag)
                {
                    this.unsetBrightness();
                }

                GlStateManager.depthMask(true);
                this.renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
            }

            GlStateManager.disableRescaleNormal();
        }
        catch (Exception exception)
        {
            LOGGER.error((String)"Couldn\'t render entity", (Throwable)exception);
        }

        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

	protected void renderFishy(EntityTropicalFish entityliving) {
		GL11.glPushMatrix();
		fish.Body.postRender(.045F);
		TropicraftRenderUtils.bindTextureEntity("tropicalFish");
		GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(.85F, 0.0F, 0.0F);
		renderHelper.renderFish(((EntityTropicalFish) entityliving).getColor() * 2);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		fish.Tail.postRender(.045F);
		GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-.90F, 0.725F, 0.0F);
		renderHelper.renderFish(((EntityTropicalFish) entityliving).getColor() * 2 + 1);
		GL11.glPopMatrix();
	}

	protected void preRenderScale(EntityTropicalFish entityTropicalFish, float f) {
		GL11.glScalef(.75F, .20F, .20F);
	}

	@Override
	protected void preRenderCallback(EntityTropicalFish entityliving, float f) {
		preRenderScale(entityliving, f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTropicalFish entity) {
		return TropicraftRenderUtils.bindTextureEntity("tropicalFish");
	}
}