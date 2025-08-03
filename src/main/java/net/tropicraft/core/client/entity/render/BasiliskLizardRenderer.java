package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.BasiliskLizardModel;
import net.tropicraft.core.client.entity.render.state.BasiliskLizardRenderState;
import net.tropicraft.core.common.entity.passive.basilisk.BasiliskLizardEntity;

public class BasiliskLizardRenderer extends MobRenderer<BasiliskLizardEntity, BasiliskLizardRenderState, BasiliskLizardModel> {
    private static final ResourceLocation BROWN_TEXTURE = Tropicraft.location("textures/entity/basilisk_lizard_brown.png");
    private static final ResourceLocation GREEN_TEXTURE = Tropicraft.location("textures/entity/basilisk_lizard_green.png");

    private final ResourceLocation texture;

    public BasiliskLizardRenderer(EntityRendererProvider.Context context, ResourceLocation texture) {
        super(context, new BasiliskLizardModel(context.bakeLayer(TropicraftRenderLayers.BASILISK_LIZARD_LAYER)), 0.3f);
        this.texture = texture;
    }

    public static BasiliskLizardRenderer brown(EntityRendererProvider.Context context) {
        return new BasiliskLizardRenderer(context, BROWN_TEXTURE);
    }

    public static BasiliskLizardRenderer green(EntityRendererProvider.Context context) {
        return new BasiliskLizardRenderer(context, GREEN_TEXTURE);
    }

    @Override
    public BasiliskLizardRenderState createRenderState() {
        return new BasiliskLizardRenderState();
    }

    @Override
    public void extractRenderState(BasiliskLizardEntity entity, BasiliskLizardRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.runningAnimation = entity.getRunningAnimation(partialTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(BasiliskLizardRenderState state) {
        return texture;
    }
}
