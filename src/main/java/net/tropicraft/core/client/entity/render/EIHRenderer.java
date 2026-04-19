package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.EIHModel;
import net.tropicraft.core.client.entity.render.state.EIHRenderState;
import net.tropicraft.core.common.entity.neutral.EIHEntity;

public class EIHRenderer extends MobRenderer<EIHEntity, EIHRenderState, EIHModel> {

    private static final Identifier TEXTURE_SLEEP = Tropicraft.id("textures/entity/eih/headtext.png");
    private static final Identifier TEXTURE_AWARE = Tropicraft.id("textures/entity/eih/headawaretext.png");
    private static final Identifier TEXTURE_ANGRY = Tropicraft.id("textures/entity/eih/headangrytext.png");

    public EIHRenderer(EntityRendererProvider.Context context) {
        super(context, new EIHModel(context.bakeLayer(TropicraftRenderLayers.EIH_LAYER)), 1.2f);
    }

    @Override
    public EIHRenderState createRenderState() {
        return new EIHRenderState();
    }

    @Override
    public void extractRenderState(EIHEntity entity, EIHRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.angry = entity.isAngry();
        state.aware = entity.isAware();
    }

    @Override
    public Identifier getTextureLocation(EIHRenderState state) {
        if (state.aware) {
            return TEXTURE_AWARE;
        } else if (state.angry) {
            return TEXTURE_ANGRY;
        } else {
            return TEXTURE_SLEEP;
        }
    }
}
