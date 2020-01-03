package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.AbstractFishModel;
import net.tropicraft.core.common.entity.underdasea.IAtlasFish;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TropicraftFishRenderer<T extends AbstractFishEntity, M extends AbstractFishModel<T>> extends MobRenderer<T, M> {
    private TropicraftSpecialRenderHelper renderHelper;
    private static final Logger LOGGER = LogManager.getLogger();

    public TropicraftFishRenderer(final EntityRendererManager manager, M modelbase, float f) {
        super(manager, modelbase, f);
        renderHelper = new TropicraftSpecialRenderHelper();
    }

    /**
     * This override is a hack
     */
    @Override
    protected void renderModel(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        boolean isVisible = this.isVisible(entity);
        boolean shouldRender = !isVisible && !entity.isInvisibleToPlayer(Minecraft.getInstance().player);
        if (isVisible || shouldRender) {
            if (!this.bindEntityTexture(entity)) {
                return;
            }

            if (shouldRender) {
                GlStateManager.setProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }

            renderFishy(entity);

            //this.entityModel.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            if (shouldRender) {
                GlStateManager.unsetProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
    }

    protected void renderFishy(T entity) {
        GlStateManager.pushMatrix();

        entityModel.body.postRender(.045F);
        bindEntityTexture(entity);
        GlStateManager.rotatef(90F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translatef(.85F, 0.0F, 0.0F);

        int fishTex = 0;
        if (entity instanceof IAtlasFish) {
            fishTex = ((IAtlasFish) entity).getAtlasSlot()*2;
        }

        renderHelper.renderFish(fishTex);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        entityModel.tail.postRender(.045F);
        GlStateManager.rotatef(90F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translatef(-.90F, 0.725F, 0.0F);
        renderHelper.renderFish(fishTex+1);
        GlStateManager.popMatrix();
    }

    @Override
    protected void preRenderCallback(T entityliving, float f) {
        GlStateManager.scalef(.75F, .20F, .20F);
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return TropicraftRenderUtils.getTextureEntity("tropical_fish");
    }
}