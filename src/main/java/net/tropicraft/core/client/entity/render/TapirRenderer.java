package net.tropicraft.core.client.entity.render;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.TapirModel;
import net.tropicraft.core.client.entity.render.layer.SunglassesLayer;
import net.tropicraft.core.client.entity.render.state.TapirRenderState;
import net.tropicraft.core.common.entity.passive.TapirEntity;

public class TapirRenderer extends AgeableMobRenderer<TapirEntity, TapirRenderState, TapirModel> {
    private static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/tapir.png");
    private static final ResourceLocation BABY_TEXTURE = Tropicraft.location("textures/entity/tapir_baby.png");

    public TapirRenderer(EntityRendererProvider.Context context) {
        super(context, new TapirModel(context.bakeLayer(TropicraftRenderLayers.TAPIR_LAYER)), new TapirModel(context.bakeLayer(TropicraftRenderLayers.TAPIR_BABY_LAYER)), 0.6f);
        addLayer(new SunglassesLayer<>(this, state -> state.isUndercover, (poseStack, state, model) -> {
            ModelPart head = model.head();
            head.translateAndRotate(poseStack);
            poseStack.translate(0.5f / 16.0f, 2.0f / 16.0f, -10.0f / 16.0f);
            final float scale = 20.0f / 16.0f;
            poseStack.scale(scale, scale, scale);
        }));
    }

    @Override
    public TapirRenderState createRenderState() {
        return new TapirRenderState();
    }

    @Override
    public void extractRenderState(TapirEntity entity, TapirRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isUndercover = entity.isUndercover();
    }

    @Override
    public ResourceLocation getTextureLocation(TapirRenderState state) {
        return state.isBaby ? BABY_TEXTURE : TEXTURE;
    }
}
