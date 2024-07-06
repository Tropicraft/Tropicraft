package net.tropicraft.core.client.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.JaguarModel;
import net.tropicraft.core.common.entity.neutral.JaguarEntity;

@OnlyIn(Dist.CLIENT)
public class JaguarRenderer extends MobRenderer<JaguarEntity, JaguarModel<JaguarEntity>> {
    private static final ResourceLocation TEXTURE = Tropicraft.location("textures/entity/jaguar.png");

    public JaguarRenderer(EntityRendererProvider.Context context) {
        super(context, new JaguarModel<>(context.bakeLayer(TropicraftRenderLayers.JAGUAR_LAYER)), 0.7f);
    }

    @Override
    public ResourceLocation getTextureLocation(JaguarEntity entity) {
        return TEXTURE;
    }
}
