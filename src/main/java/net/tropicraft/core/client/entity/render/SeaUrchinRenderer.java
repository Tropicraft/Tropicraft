package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.SeaUrchinModel;
import net.tropicraft.core.client.entity.render.state.SeaUrchinRenderState;
import net.tropicraft.core.common.entity.underdasea.SeaUrchinEntity;

public class SeaUrchinRenderer extends MobRenderer<SeaUrchinEntity, SeaUrchinRenderState, SeaUrchinModel> {
    /**
     * Amount freshly hatched sea urchins are scaled down while rendering.
     */
    public static final float BABY_RENDER_SCALE = 0.25f;

    /**
     * Amount mature sea urchins are scaled down while rendering.
     */
    public static final float ADULT_RENDER_SCALE = 0.5f;
    public static final Identifier SEA_URCHIN_TEXTURE = Tropicraft.id("textures/entity/seaurchin.png");

    public SeaUrchinRenderer(EntityRendererProvider.Context context) {
        super(context, new SeaUrchinModel(context.bakeLayer(TropicraftRenderLayers.SEA_URCHIN_LAYER)), 0.5f);
    }

    @Override
    public SeaUrchinRenderState createRenderState() {
        return new SeaUrchinRenderState();
    }

    @Override
    public void extractRenderState(SeaUrchinEntity entity, SeaUrchinRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.growthProgress = entity.getGrowthProgress();
    }

    @Override
    protected void scale(SeaUrchinRenderState state, PoseStack poseStack) {
        shadowRadius = 0.15f;
        shadowStrength = 0.5f;
        float scale = Mth.lerp(state.growthProgress, BABY_RENDER_SCALE, ADULT_RENDER_SCALE);
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public Identifier getTextureLocation(SeaUrchinRenderState state) {
        return SEA_URCHIN_TEXTURE;
    }
}
