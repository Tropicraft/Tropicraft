package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.ManateeModel;
import net.tropicraft.core.client.entity.render.state.ManateeRenderState;
import net.tropicraft.core.common.entity.underdasea.ManateeEntity;

public class ManateeRenderer extends MobRenderer<ManateeEntity, ManateeRenderState, ManateeModel> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/manatee.png");

    public ManateeRenderer(EntityRendererProvider.Context context) {
        super(context, new ManateeModel(context.bakeLayer(TropicraftRenderLayers.MANATEE_LAYER)), 1.5f);
    }

    @Override
    public ManateeRenderState createRenderState() {
        return new ManateeRenderState();
    }

    @Override
    public void extractRenderState(ManateeEntity entity, ManateeRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xBodyRot = entity.getXBodyRot(partialTicks);
    }

    @Override
    public Identifier getTextureLocation(ManateeRenderState state) {
        return TEXTURE;
    }
}
