package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.ManOWarModel;
import net.tropicraft.core.client.entity.render.layer.ManOWarGelLayer;
import net.tropicraft.core.common.entity.underdasea.ManOWarEntity;

public class ManOWarRenderer extends MobRenderer<ManOWarEntity, LivingEntityRenderState, ManOWarModel> {
    public static final Identifier TEXTURE_LOCATION = Tropicraft.id("textures/entity/manowar.png");

    public ManOWarRenderer(EntityRendererProvider.Context context) {
        super(context, new ManOWarModel(context.bakeLayer(TropicraftRenderLayers.MAN_O_WAR_OUTER_LAYER)), 0.35f);
        addLayer(new ManOWarGelLayer(this, new ManOWarModel(context.bakeLayer(TropicraftRenderLayers.MAN_O_WAR_GEL_LAYER))));
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE_LOCATION;
    }
}
