package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.TropiCreeperModel;
import net.tropicraft.core.common.entity.passive.TropiCreeperEntity;

public class TropiCreeperRenderer extends MobRenderer<TropiCreeperEntity, CreeperRenderState, TropiCreeperModel> {
    private static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/tropicreeper.png");

    public TropiCreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new TropiCreeperModel(context.bakeLayer(TropicraftRenderLayers.TROPI_CREEPER_LAYER)), 0.5f);
    }

    @Override
    public CreeperRenderState createRenderState() {
        return new CreeperRenderState();
    }

    @Override
    public void extractRenderState(TropiCreeperEntity entity, CreeperRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.swelling = entity.getCreeperFlashIntensity(partialTicks);
    }

    @Override
    protected void scale(CreeperRenderState state, PoseStack poseStack) {
        float swelling = state.swelling;
        float skew = 1.0f + Mth.sin(swelling * 100.0f) * swelling * 0.01f;
        swelling = Mth.clamp(swelling, 0.0f, 1.0f);
        swelling *= swelling;
        swelling *= swelling;
        float scaleXz = (1.0f + swelling * 0.4f) * skew;
        float scaleY = (1.0f + swelling * 0.1f) / skew;
        poseStack.scale(scaleXz, scaleY, scaleXz);
    }

    @Override
    protected float getWhiteOverlayProgress(CreeperRenderState state) {
        if ((int) (state.swelling * 10.0f) % 2 == 0) {
            return 0.0f;
        }
        return Mth.clamp(state.swelling, 0.5f, 1.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(CreeperRenderState state) {
        return TEXTURE;
    }
}
