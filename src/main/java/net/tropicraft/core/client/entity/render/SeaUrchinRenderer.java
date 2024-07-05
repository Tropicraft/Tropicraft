package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.SeaUrchinModel;
import net.tropicraft.core.common.entity.underdasea.SeaUrchinEntity;

public class SeaUrchinRenderer extends MobRenderer<SeaUrchinEntity, SeaUrchinModel> {
    /**
     * Amount freshly hatched sea urchins are scaled down while rendering.
     */
    public static final float BABY_RENDER_SCALE = 0.5f;

    /**
     * Amount mature sea urchins are scaled down while rendering.
     */
    public static final float ADULT_RENDER_SCALE = 1f;
    public static final ResourceLocation SEA_URCHIN_TEXTURE = TropicraftRenderUtils.bindTextureEntity("seaurchin");

    public SeaUrchinRenderer(final EntityRendererProvider.Context context) {
        super(context, new SeaUrchinModel(context.bakeLayer(TropicraftRenderLayers.SEA_URCHIN_LAYER)), 0.5f);
    }

    @Override
    protected void scale(final SeaUrchinEntity urchin, final PoseStack stack, final float partialTickTime) {
        shadowRadius = 0.15f;
        shadowStrength = 0.5f;
        float growthProgress = urchin.getGrowthProgress();
        final float scale = BABY_RENDER_SCALE + growthProgress * (ADULT_RENDER_SCALE - BABY_RENDER_SCALE);
        final float scaleAmt = 0.5f * scale;

        stack.scale(scaleAmt, scaleAmt, scaleAmt);
    }

    @Override
    public ResourceLocation getTextureLocation(final SeaUrchinEntity entity) {
        return SEA_URCHIN_TEXTURE;
    }
}
