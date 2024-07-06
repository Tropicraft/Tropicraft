package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.VMonkeyModel;
import net.tropicraft.core.client.entity.render.layer.VMonkeyHeldItemLayer;
import net.tropicraft.core.common.entity.neutral.VMonkeyEntity;

public class VMonkeyRenderer extends MobRenderer<VMonkeyEntity, VMonkeyModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Tropicraft.ID, "textures/entity/monkeytext.png");
    private static final ResourceLocation ANGRY_TEXTURE = ResourceLocation.fromNamespaceAndPath(Tropicraft.ID, "textures/entity/monkey_angrytext.png");

    public VMonkeyRenderer(EntityRendererProvider.Context context) {
        super(context, new VMonkeyModel(context.bakeLayer(TropicraftRenderLayers.V_MONKEY_LAYER)), 0.5f);
        shadowRadius = 0.3f;
        shadowStrength = 0.5f;
        addLayer(new VMonkeyHeldItemLayer<>(this, context.getItemRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(VMonkeyEntity entity) {
        return entity.isAggressive() ? ANGRY_TEXTURE : TEXTURE;
    }
}
