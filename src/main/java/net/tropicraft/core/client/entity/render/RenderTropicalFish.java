package net.tropicraft.core.client.entity.render;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelFish;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;
import net.tropicraft.core.common.entity.underdasea.atlantoku.IAtlasFish;

public class RenderTropicalFish extends RenderLiving<EntityTropicraftWaterBase> {

	public ModelFish fish;
	private TropicraftSpecialRenderHelper renderHelper;
	private static final Logger LOGGER = LogManager.getLogger();
	

	public RenderTropicalFish(ModelBase modelbase, float f) {
		super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
		fish = (ModelFish) modelbase;
		renderHelper = new TropicraftSpecialRenderHelper();
	}
	
	public RenderTropicalFish() {
		this(new ModelFish(), 1f);
	}
	
	/**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityTropicraftWaterBase entity, double x, double y, double z, float entityYaw, float partialTicks) {
    		this.shadowSize = 0.08f;
		this.shadowOpaque = 0.3f;
    		GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
        boolean shouldSit = entity.isRiding() && (entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
        this.mainModel.isRiding = shouldSit;
        this.mainModel.isChild = entity.isChild();

        float offset = 0f;
        try
        {
            float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
            float f1 = this.interpolateRotation(entity.prevSwimYaw+offset, entity.swimYaw+offset, partialTicks);
            float f2 = f1 - f;
            
            f1 = -f1;
            f = f1;

            float f7 = entity.prevSwimPitch + (entity.swimPitch - entity.prevSwimPitch) * partialTicks;
            this.renderLivingAt(entity, x, y, z);
            float f8 = this.handleRotationFloat(entity, partialTicks);
            this.applyRotations(entity, f8, f, partialTicks);
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
            
            if(entity.hurtTime > 0){

				GL11.glColor4f(2f, 0f, 0f, 1f);
			}
        
            
            if(!entity.isInWater()) {
    			entity.outOfWaterTime++;
    			if(entity.outOfWaterTime > 90) {
    				entity.outOfWaterTime = 90;
    			}
    		}else {
    			if(entity.outOfWaterTime > 0) {
    				entity.outOfWaterTime--;
    			}
    		}

           this.renderFishy(entity);
			GL11.glColor4f(1f, 1f, 1f, 1f);

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

	protected void renderFishy(EntityTropicraftWaterBase entityliving) {
		GlStateManager.pushMatrix();
		
		fish.Body.postRender(.045F);
		TropicraftRenderUtils.bindTextureEntity("tropicalFish");
		GlStateManager.rotate(90F, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(.85F, 0.0F, 0.0F);
		
		int fishTex = 0;
		if(entityliving instanceof IAtlasFish) {
			fishTex = ((IAtlasFish)entityliving).getAtlasSlot()*2;
		}
	
		renderHelper.renderFish(fishTex);
		GlStateManager.popMatrix();
		
		GlStateManager.pushMatrix();
		fish.Tail.postRender(.045F);
		GlStateManager.rotate(90F, 0.0F, 1.0F, 0.0F);
		GlStateManager.translate(-.90F, 0.725F, 0.0F);
		renderHelper.renderFish(fishTex+1);
		GlStateManager.popMatrix();
	}

	protected void preRenderScale(EntityTropicraftWaterBase entityTropicalFish, float f) {
		GlStateManager.scale(.75F, .20F, .20F);
	}

	@Override
	protected void preRenderCallback(EntityTropicraftWaterBase entityliving, float f) {
		preRenderScale(entityliving, f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTropicraftWaterBase entity) {
		return TropicraftRenderUtils.bindTextureEntity("tropicalFish");
	}
}