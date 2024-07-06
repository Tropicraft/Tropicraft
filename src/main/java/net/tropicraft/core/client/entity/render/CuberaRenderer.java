package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.CuberaModel;
import net.tropicraft.core.common.entity.underdasea.CuberaEntity;

public class CuberaRenderer extends MobRenderer<CuberaEntity, CuberaModel<CuberaEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Tropicraft.ID, "textures/entity/cubera.png");

    public CuberaRenderer(EntityRendererProvider.Context context) {
        super(context, new CuberaModel<>(context.bakeLayer(TropicraftRenderLayers.CUBERA_LAYER)), 0.6f);
    }

    @Override
    protected void scale(CuberaEntity entity, PoseStack matrixStack, float partialTicks) {
        matrixStack.scale(1.25f, 1.25f, 1.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(CuberaEntity entity) {
        return TEXTURE;
    }
}
